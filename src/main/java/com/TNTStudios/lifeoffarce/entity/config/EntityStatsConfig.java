package com.TNTStudios.lifeoffarce.entity.config;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashMap;
import java.util.Map;

// ¡AÑADIDO! Hago que esta clase escuche eventos del bus de mods para poder cargar la configuración en el momento correcto.
@Mod.EventBusSubscriber(modid = Lifeoffarce.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityStatsConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Este mapa sigue siendo necesario para construir la config y para que los comandos puedan modificarla.
    private static final Map<ResourceLocation, EntityConfig> ENTITY_CONFIGS = new HashMap<>();

    // ¡NUEVO! Un mapa para almacenar los valores ya cargados desde el archivo.
    // Esto evita llamar a .get() antes de tiempo y resuelve el crash.
    private static final Map<ResourceLocation, LoadedStats> LOADED_STATS = new HashMap<>();

    // ¡NUEVO! Una clase interna simple para guardar los valores leídos de forma segura.
    public static class LoadedStats {
        public final double maxHealth;
        public final double attackDamage;
        public final double movementSpeed;
        public final double followRange;

        private LoadedStats(EntityConfig config) {
            this.maxHealth = config.maxHealth.get();
            this.attackDamage = config.attackDamage.get();
            this.movementSpeed = config.movementSpeed.get();
            this.followRange = config.followRange.get();
        }
    }

    public static class EntityConfig {
        public final ForgeConfigSpec.DoubleValue maxHealth;
        public final ForgeConfigSpec.DoubleValue attackDamage;
        public final ForgeConfigSpec.DoubleValue movementSpeed;
        public final ForgeConfigSpec.DoubleValue followRange;

        public EntityConfig(ForgeConfigSpec.Builder builder, String entityName, double defaultHealth, double defaultDamage, double defaultSpeed, double defaultFollowRange) {
            builder.push(entityName); // Inicio una nueva categoría para la entidad.

            maxHealth = builder
                    .comment("Vida maxima de la entidad " + entityName)
                    .defineInRange("maxHealth", defaultHealth, 1.0, 10000.0);

            attackDamage = builder
                    .comment("Dano de ataque de la entidad " + entityName)
                    .defineInRange("attackDamage", defaultDamage, 0.0, 1000.0);

            movementSpeed = builder
                    .comment("Velocidad de movimiento de la entidad " + entityName)
                    .defineInRange("movementSpeed", defaultSpeed, 0.1, 5.0);

            followRange = builder
                    .comment("Rango en el que la entidad " + entityName + " detecta y sigue a los jugadores.")
                    .defineInRange("followRange", defaultFollowRange, 1.0, 200.0);

            builder.pop(); // Cierro la categoría de la entidad.
        }
    }

    static {
        registerEntityConfig(
                new ResourceLocation("lifeoffarce", "el_gigante"),
                "ElGigante",
                150.0, // Vida por defecto
                15.0,  // Daño por defecto
                0.25,  // Velocidad por defecto
                35.0   // Rango de seguimiento por defecto
        );
        SPEC = BUILDER.build();
    }

    private static void registerEntityConfig(ResourceLocation entityId, String categoryName, double health, double damage, double speed, double followRange) {
        EntityConfig config = new EntityConfig(BUILDER, categoryName, health, damage, speed, followRange);
        ENTITY_CONFIGS.put(entityId, config);
    }

    /**
     * ¡MODIFICADO! Este método ahora devuelve los valores YA CARGADOS y seguros de usar.
     * Lo usaré para registrar los atributos de las entidades.
     */
    public static LoadedStats getStats(ResourceLocation entityId) {
        return LOADED_STATS.get(entityId);
    }

    /**
     * ¡RESTAURADO! Este método devuelve el objeto de configuración original.
     * Es necesario para que mis comandos puedan leer y modificar los valores dinámicamente.
     */
    public static EntityConfig getConfig(ResourceLocation entityId) {
        return ENTITY_CONFIGS.get(entityId);
    }

    // ¡NUEVO! Este método se suscribe al evento de carga/recarga de la configuración.
    // Se asegura de leer los valores del archivo ANTES de que el juego los necesite.
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        // Me aseguro de reaccionar solo a la carga de mi archivo de configuración específico.
        if (event.getConfig().getSpec() == SPEC) {
            LOADED_STATS.clear();
            for (Map.Entry<ResourceLocation, EntityConfig> entry : ENTITY_CONFIGS.entrySet()) {
                // Aquí es el único lugar donde llamo a .get(), una vez que sé que es seguro.
                LoadedStats stats = new LoadedStats(entry.getValue());
                LOADED_STATS.put(entry.getKey(), stats);
            }
        }
    }
}