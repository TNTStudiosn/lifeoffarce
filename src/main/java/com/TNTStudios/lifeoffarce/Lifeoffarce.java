package com.TNTStudios.lifeoffarce;

import com.TNTStudios.lifeoffarce.entity.ModEntities;
import com.TNTStudios.lifeoffarce.entity.config.EntityStatManager;
import com.TNTStudios.lifeoffarce.entity.custom.ElGiganteEntity;
import com.TNTStudios.lifeoffarce.event.ModEvents;
import com.TNTStudios.lifeoffarce.item.ModCreativeModeTabs;
import com.TNTStudios.lifeoffarce.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.TNTStudios.lifeoffarce.entity.config.EntityStatsConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

// La anotación @Mod le dice a Forge que esta clase es la principal de mi mod.
@Mod(Lifeoffarce.MODID)
public class Lifeoffarce {

    // =================================================================================
    // 1. CONSTANTES Y VARIABLES ESTÁTICAS
    // Aquí defino los valores clave que usaré en todo el mod.
    // =================================================================================

    // Mi ID de mod. Es fundamental para que Forge identifique mis bloques, ítems, etc.
    public static final String MODID = "lifeoffarce";
    public static final String MOD_ID = "lifeoffarce";
    // El logger, para poder escribir mensajes en la consola de Minecraft.
    private static final Logger LOGGER = LogUtils.getLogger();
    // La instancia única de mi gestor de estadísticas. Así controlo los datos de mis mobs.
    public static final EntityStatManager ENTITY_STAT_MANAGER = new EntityStatManager();

    // =================================================================================
    // 2. REGISTROS DIFERIDOS (DEFERRED REGISTERS)
    // Estos son como "listas de espera" donde anoto todo lo que quiero añadir al juego.
    // Forge los procesará en el momento adecuado.
    // =================================================================================

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // =================================================================================
    // 3. REGISTRO DE OBJETOS DEL JUEGO (BLOQUES, ÍTEMS, ETC.)
    // Aquí defino cada cosa que mi mod va a añadir.
    // =================================================================================

    // Un bloque de ejemplo, para tener una base.
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // El ítem correspondiente a mi bloque de ejemplo, para poder ponerlo en el inventario.
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
    // Un ítem de comida de ejemplo.
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(1).saturationMod(2f).build())));
    // Una pestaña de creativo de ejemplo.
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> output.accept(EXAMPLE_ITEM.get()))
            .build());

    // =================================================================================
    // 4. CONSTRUCTOR PRINCIPAL
    // Este es el punto de partida. Aquí le digo a Forge cómo y cuándo cargar todo.
    // =================================================================================

    public Lifeoffarce() {
        // Obtengo el "bus de eventos" del mod. Es el canal por donde mi mod se comunica con Forge.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // --- Registro de mis clases ---
        // Le digo a Forge que revise mis otras clases para registrar sus contenidos.
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEntities.register(modEventBus);

        // --- Registro de los Deferred Registers ---
        // Le paso mis "listas de espera" al bus de eventos para que las procese.
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // --- Suscripción a eventos del mod ---
        // Aquí conecto mis métodos a eventos específicos del ciclo de vida del mod.
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // --- Suscripción a eventos generales de Forge ---
        // Registro la clase actual en el bus principal de Forge para eventos como el inicio del servidor.
        MinecraftForge.EVENT_BUS.register(this);
        // Añado un listener específico para el evento de recarga de recursos (/reload).
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EntityStatsConfig.SPEC, "lifeoffarce-entity-stats.toml");

        // --- Registro de la configuración ---
        // Le digo a Forge que cargue mi archivo de configuración (Config.java).
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // =================================================================================
    // 5. MÉTODOS DE EVENTOS
    // Estos métodos se ejecutan automáticamente cuando ocurre un evento específico.
    // =================================================================================

    // Se ejecuta cuando el mod se carga en ambos lados (cliente y servidor).
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Life of Farce Mod inicializado.");
        if (Config.logDirtBlock) {
            LOGGER.info("Bloque de tierra (DIRT BLOCK) >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        }
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach((item) -> LOGGER.info("Ítem de config >> {}", item.toString()));
    }

    // Se ejecuta cuando el juego recarga recursos (ej: con el comando /reload).
    // CORRECCIÓN: Esta es la nueva forma correcta de registrar un listener de recarga.
    private void onAddReloadListener(AddReloadListenerEvent event) {
        // Le paso un objeto que sabe cómo manejar la recarga de mis estadísticas.
        event.addListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                // Esta parte se puede usar para preparar datos en un hilo secundario.
                // Como mi carga es rápida, no necesito preparar nada, así que devuelvo null.
                return null;
            }

            @Override
            protected void apply(Void pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                // Esta parte se ejecuta en el hilo principal. Aquí es donde recargo mis stats.
                ENTITY_STAT_MANAGER.loadStats(pResourceManager);
            }
        });
    }

    // Se ejecuta para añadir ítems a las pestañas de creativo existentes.
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // Se ejecuta cuando el servidor está arrancando.
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("El servidor está iniciando. ¡Hola desde Life of Farce!");
    }

    // =================================================================================
    // 6. CLASE INTERNA PARA EVENTOS DEL LADO CLIENTE
    // Agrupo aquí todo lo que solo debe ejecutarse en el juego del cliente (gráficos, etc.).
    // =================================================================================

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        // Se ejecuta solo en el cliente cuando el mod se está configurando.
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Aquí le decimos a Minecraft cómo debe renderizar (dibujar) nuestra entidad.
            net.minecraft.client.renderer.entity.EntityRenderers.register(ModEntities.EL_GIGANTE.get(), com.TNTStudios.lifeoffarce.entity.client.ElGiganteRenderer::new);

            LOGGER.info("Configuración del cliente finalizada. Jugador: {}", Minecraft.getInstance().getUser().getName());
        }
    }
}