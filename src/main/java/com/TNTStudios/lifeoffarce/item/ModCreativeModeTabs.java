package com.TNTStudios.lifeoffarce.item;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    // Creo un DeferredRegister para mis pestañas de creativo.
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Lifeoffarce.MODID);

    // Registro mi nueva pestaña.
    public static final RegistryObject<CreativeModeTab> LOF_TAB = CREATIVE_MODE_TABS.register("lof_tab",
            () -> CreativeModeTab.builder()
                    // El ícono de la pestaña será la medalla de cocina.
                    .icon(() -> new ItemStack(ModItems.MEDALLA_COCINA.get()))
                    // El título que se mostrará. Lo defino en el archivo de idioma.
                    .title(Component.translatable("creativetab.lof_tab"))
                    // Aquí especifico qué ítems aparecerán en esta pestaña.
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.MEDALLA_COCINA.get());
                        pOutput.accept(ModItems.MEDALLA_CUIDADODELANATURALEZA.get());
                        pOutput.accept(ModItems.MEDALLA_MECANICAYCARPINTERIA.get());
                        pOutput.accept(ModItems.MEDALLA_DEFENSAPERSONALYMEDITACION.get());
                        pOutput.accept(ModItems.MEDALLA_FILOSOFIA.get());
                        pOutput.accept(ModItems.MEDALLA_MEDICINA.get());
                        pOutput.accept(ModItems.TICKET.get());
                    })
                    .build());

    // Método para que la clase principal pueda registrar la pestaña.
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}