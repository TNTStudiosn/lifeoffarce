package com.TNTStudios.lifeoffarce.entity.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestiono de manera centralizada todas las configuraciones de estadísticas para las entidades del mod.
 * Así, cada entidad que añada en el futuro tendrá su propio apartado en el archivo de configuración,
 * manteniendo todo ordenado y fácil de modificar.
 */
public class EntityStatsConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Uso un mapa para mantener las configuraciones de cada entidad. La clave es su ID (ej: "lifeoffarce:el_gigante")
    // y el valor es un objeto que contiene todas sus estadísticas configurables.
    private static final Map<ResourceLocation, EntityConfig> ENTITY_CONFIGS = new HashMap<>();

    // Defino una estructura interna para agrupar las estadísticas de una entidad.
    // De esta forma, en el archivo .toml se verá como una categoría separada para cada mob.
    public static class EntityConfig {
        public final ForgeConfigSpec.DoubleValue maxHealth;
        public final ForgeConfigSpec.DoubleValue attackDamage;
        public final ForgeConfigSpec.DoubleValue movementSpeed;
        public final ForgeConfigSpec.DoubleValue followRange;

        // El constructor se encarga de crear las variables de configuración dentro de su propia categoría.
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

    // El bloque estático es donde registro las entidades cuyas estadísticas quiero que sean configurables.
    // Se ejecuta una sola vez cuando la clase es cargada por Java.
    static {
        // Para "El Gigante", defino sus valores por defecto. Si el archivo de config no existe, usará estos.
        registerEntityConfig(
                new ResourceLocation("lifeoffarce", "el_gigante"),
                "ElGigante",
                150.0, // Vida por defecto
                15.0,  // Daño por defecto
                0.25,  // Velocidad por defecto
                35.0   // Rango de seguimiento por defecto
        );

        // Si quisiera añadir otra entidad, simplemente la registro aquí.
        // registerEntityConfig(new ResourceLocation("lifeoffarce", "otra_entidad"), "OtraEntidad", 20.0, 5.0, 0.3, 20.0);

        SPEC = BUILDER.build();
    }

    /**
     * Un método de ayuda para registrar la configuración de una nueva entidad y añadirla a nuestro mapa.
     * Esto evita repetir código y mantiene el bloque 'static' más limpio.
     */
    private static void registerEntityConfig(ResourceLocation entityId, String categoryName, double health, double damage, double speed, double followRange) {
        EntityConfig config = new EntityConfig(BUILDER, categoryName, health, damage, speed, followRange);
        ENTITY_CONFIGS.put(entityId, config);
    }

    /**
     * Este método me permite obtener la configuración específica de una entidad usando su ID.
     * Es la forma en que el resto del código accederá a los valores de la configuración.
     * @param entityId El ID de la entidad (ej: new ResourceLocation("lifeoffarce", "el_gigante")).
     * @return La configuración de la entidad, o null si no existe.
     */
    public static EntityConfig getConfig(ResourceLocation entityId) {
        return ENTITY_CONFIGS.get(entityId);
    }
}