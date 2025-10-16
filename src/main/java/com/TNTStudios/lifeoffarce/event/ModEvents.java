package com.TNTStudios.lifeoffarce.event;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.ModEntities;
import com.TNTStudios.lifeoffarce.entity.config.EntityStats;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

// NOTA: Esta clase fue reescrita para usar el sistema moderno de 'Biome Modifiers'.
// El antiguo 'BiomeLoadingEvent' ya no existe en Forge 1.20.1.
public class ModEvents {

    // Un registro para nuestros modificadores de bioma.
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Lifeoffarce.MODID);

    // Registramos nuestro modificador de spawn personalizado.
    private static final RegistryObject<Codec<CustomSpawnModifier>> CUSTOM_SPAWN_MODIFIER =
            BIOME_MODIFIER_SERIALIZERS.register("custom_spawn", () -> CustomSpawnModifier.CODEC);


    // Este es el modificador real. Aquí es donde ocurre la magia.
    private static class CustomSpawnModifier implements BiomeModifier {
        // Un 'Codec' es básicamente la forma que tiene Minecraft de leer y escribir datos (en este caso, nuestro modificador).
        public static final Codec<CustomSpawnModifier> CODEC = Codec.unit(CustomSpawnModifier::new);

        @Override
        public void modify(Holder<Biome> biome, Phase phase, net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            // Solo nos interesa la fase de 'ADD' (añadir cosas).
            if (phase == Phase.ADD) {
                // Reviso mi configuración, igual que antes.
                Optional<EntityStats> statsOptional = Lifeoffarce.ENTITY_STAT_MANAGER.getStats(ModEntities.EL_GIGANTE.get());

                // Si mi gigante debe spawnear...
                if (statsOptional.isPresent() && statsOptional.get().shouldSpawnNaturally()) {
                    EntityStats stats = statsOptional.get();

                    // Añado la información de spawn al bioma.
                    builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER,
                            new MobSpawnSettings.SpawnerData(
                                    ModEntities.EL_GIGANTE.get(),
                                    stats.getSpawnWeight(),
                                    stats.getMinSpawnCount(),
                                    stats.getMaxSpawnCount()
                            )
                    );
                }
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return CODEC;
        }
    }

    // Método para que la clase principal pueda registrar nuestro sistema.
    public static void register(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}