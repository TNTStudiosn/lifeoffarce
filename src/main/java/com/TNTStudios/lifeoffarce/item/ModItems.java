package com.TNTStudios.lifeoffarce.item;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Creo un DeferredRegister para mis ítems.
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Lifeoffarce.MODID);

    // Aquí registro cada una de las medallas.
    // Son ítems simples, sin ninguna propiedad especial.
    public static final RegistryObject<Item> MEDALLA_COCINA = ITEMS.register("medalla_cocina",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDALLA_CUIDADODELANATURALEZA = ITEMS.register("medalla_cuidadodelanaturaleza",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDALLA_MECANICAYCARPINTERIA = ITEMS.register("medalla_mecanicaycarpinteria",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDALLA_DEFENSAPERSONALYMEDITACION = ITEMS.register("medalla_defensapersonalymeditacion",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDALLA_FILOSOFIA = ITEMS.register("medalla_filosofia",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDALLA_MEDICINA = ITEMS.register("medalla_medicina",
            () -> new Item(new Item.Properties()));

    // Método para que la clase principal pueda registrar todo lo de aquí.
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}