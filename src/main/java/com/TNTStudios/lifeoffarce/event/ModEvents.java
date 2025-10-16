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

        // ¡MODIFICADO! Obtengo los stats ya cargados y procesados para "El Gigante".
        ResourceLocation elGiganteId = new ResourceLocation(Lifeoffarce.MOD_ID, "el_gigante");
        EntityStatsConfig.LoadedStats elGiganteStats = EntityStatsConfig.getStats(elGiganteId);

        // Verifico que los stats no sean nulos para evitar errores.
        if (elGiganteStats != null) {
            event.put(ModEntities.EL_GIGANTE.get(),
                    Monster.createMonsterAttributes()
                            // ¡CORREGIDO! Ahora uso los valores del objeto 'LoadedStats'.
                            // Estos valores ya fueron leídos del archivo de configuración de forma segura.
                            // Ya no se llama a .get() aquí, eliminando la causa del crash.
                            .add(Attributes.MAX_HEALTH, elGiganteStats.maxHealth)
                            .add(Attributes.ATTACK_DAMAGE, elGiganteStats.attackDamage)
                            .add(Attributes.MOVEMENT_SPEED, elGiganteStats.movementSpeed)
                            .add(Attributes.FOLLOW_RANGE, elGiganteStats.followRange)
                            // También es una buena práctica añadir resistencia al retroceso (knockback).
                            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                            .build());
        }
    }
}