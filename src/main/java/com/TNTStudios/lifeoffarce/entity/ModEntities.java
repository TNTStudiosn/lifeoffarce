package com.TNTStudios.lifeoffarce.entity;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.custom.ElGiganteEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    // Mi DeferredRegister para los tipos de entidades.
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Lifeoffarce.MODID);

    // Registro la entidad "El gigante".
    public static final RegistryObject<EntityType<ElGiganteEntity>> EL_GIGANTE =
            ENTITY_TYPES.register("el_gigante",
                    // Uso el Builder para definir sus propiedades básicas.
                    () -> EntityType.Builder.of(ElGiganteEntity::new, MobCategory.MONSTER)
                            // Defino el tamaño de su hitbox.
                            .sized(1.9f, 3.5f)
                            // Le asigno un ID único dentro de mi mod.
                            .build("el_gigante"));

    // Método para que la clase principal pueda registrar todo.
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}