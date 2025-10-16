package com.TNTStudios.lifeoffarce.entity.config;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Mi gestor para cargar y administrar las estadísticas de todas mis entidades.
// Lo hago 'singleton' para tener una única instancia y no consumir memoria de más.
public class EntityStatManager {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    // La carpeta dentro de 'resources/data/lifeoffarce/' donde guardaré mis JSONs.
    private static final String STATS_DIRECTORY = "entities";

    // Un mapa para acceder rápidamente a las estadísticas de una entidad por su ID.
    private final Map<ResourceLocation, EntityStats> entityStatsMap = new HashMap<>();

    // Cargo todas las estadísticas desde los archivos JSON.
    public void loadStats(ResourceManager resourceManager) {
        // Limpio el mapa por si estoy recargando la configuración.
        entityStatsMap.clear();

        // Busco todos los archivos que terminen en '.json' dentro de mi directorio 'entities'.
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(STATS_DIRECTORY, path -> path.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            try (InputStreamReader reader = new InputStreamReader(entry.getValue().open())) {
                // Convierto el JSON a mi clase EntityStats.
                EntityStats stats = GSON.fromJson(reader, EntityStats.class);

                // Extraigo el nombre del archivo (sin la ruta ni la extensión) para usarlo como clave.
                String path = resourceLocation.getPath().substring(STATS_DIRECTORY.length() + 1, resourceLocation.getPath().length() - ".json".length());
                ResourceLocation entityId = new ResourceLocation(resourceLocation.getNamespace(), path);

                // Guardo las estadísticas en mi mapa.
                entityStatsMap.put(entityId, stats);
                LOGGER.info("Estadísticas de entidad cargadas: {}", entityId);

            } catch (Exception e) {
                LOGGER.error("No pude cargar las estadísticas para: {}", resourceLocation, e);
            }
        }
    }

    // Método para obtener las estadísticas de una entidad.
    // Devuelve un 'Optional' por si la entidad no tiene un JSON definido.
    public Optional<EntityStats> getStats(EntityType<?> entityType) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        return Optional.ofNullable(entityStatsMap.get(id));
    }
}