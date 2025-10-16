package com.TNTStudios.lifeoffarce.event;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.ModEntities;
import com.TNTStudios.lifeoffarce.entity.config.EntityStats;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.level.BiomeLoadingEvent; // <-- CORRECCIÓN 1: La ruta correcta del evento.
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

// Mi clase para manejar eventos del mod, como el spawn de entidades.
@Mod.EventBusSubscriber(modid = Lifeoffarce.MODID)
public class ModEvents {

    // Este evento se llama cada vez que un bioma se carga en el juego.
    // Es el lugar perfecto y más optimizado para añadir spawns.
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {

        // Primero, consulto si mi gigante debe spawnear de forma natural.
        Optional<EntityStats> statsOptional = Lifeoffarce.ENTITY_STAT_MANAGER.getStats(ModEntities.EL_GIGANTE.get());

        // Si tiene configuración y 'naturalSpawn' es true...
        if (statsOptional.isPresent() && statsOptional.get().shouldSpawnNaturally()) {
            EntityStats stats = statsOptional.get();

            // CORRECCIÓN 2: He quitado el 'if' que comprobaba la categoría del bioma.
            // Ahora se añadirá el spawn a CUALQUIER bioma, incluyendo Nether y End.
            event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                    ModEntities.EL_GIGANTE.get(),
                    stats.getSpawnWeight(),
                    stats.getMinSpawnCount(),
                    stats.getMaxSpawnCount()
            ));
        }
    }
}