package com.TNTStudios.lifeoffarce.event;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.ModEntities;
import com.TNTStudios.lifeoffarce.entity.custom.ElGiganteEntity; // ¡Asegúrate de importar la clase!
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Lifeoffarce.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        // Para cada una de mis entidades, llamo a este evento para asignarle sus atributos base.
        // Estos son los valores que tendrán al ser creadas, antes de cualquier modificador (pociones, equipo, etc.).

        // =================================================================================
        // CORRECCIÓN FINAL
        // =================================================================================
        // Ahora, en lugar de intentar leer la configuración (que puede no estar lista),
        // llamo directamente al método estático 'createAttributes()' que acabo de añadir
        // en la clase ElGiganteEntity.
        // Esto soluciona el crasheo de forma definitiva.
        event.put(ModEntities.EL_GIGANTE.get(), ElGiganteEntity.createAttributes().build());
    }
}