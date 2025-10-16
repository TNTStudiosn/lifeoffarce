package com.TNTStudios.lifeoffarce.event;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.ModEntities;
import com.TNTStudios.lifeoffarce.entity.config.EntityStatsConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Lifeoffarce.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        // Para cada una de mis entidades, llamo a este evento para asignarle sus atributos base.
        // Estos son los valores que tendrán al ser creadas, antes de cualquier modificador (pociones, equipo, etc.).

        // Obtengo la configuración específica para "El Gigante".
        ResourceLocation elGiganteId = new ResourceLocation(Lifeoffarce.MOD_ID, "el_gigante");
        EntityStatsConfig.EntityConfig elGiganteConfig = EntityStatsConfig.getConfig(elGiganteId);

        // Verifico que la configuración no sea nula para evitar errores.
        if (elGiganteConfig != null) {
            event.put(ModEntities.EL_GIGANTE.get(),
                    Monster.createMonsterAttributes()
                            // En lugar de valores fijos, ahora uso los valores leídos desde el archivo de configuración.
                            // El .get() me da el valor actual, ya sea el por defecto o el modificado por el usuario.
                            .add(Attributes.MAX_HEALTH, elGiganteConfig.maxHealth.get())
                            .add(Attributes.ATTACK_DAMAGE, elGiganteConfig.attackDamage.get())
                            .add(Attributes.MOVEMENT_SPEED, elGiganteConfig.movementSpeed.get())
                            .add(Attributes.FOLLOW_RANGE, elGiganteConfig.followRange.get())
                            // También es una buena práctica añadir resistencia al retroceso (knockback).
                            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                            .build());
        }
    }
}