package com.TNTStudios.lifeoffarce.command;

import com.TNTStudios.lifeoffarce.Lifeoffarce;
import com.TNTStudios.lifeoffarce.entity.config.EntityStatsConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Lifeoffarce.MOD_ID)
public class ModCommands {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("lof")
                        .requires(source -> source.hasPermission(2))
                        .then(
                                Commands.literal("entity")
                                        .then(
                                                Commands.argument("entity_id", StringArgumentType.string())
                                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                                ForgeRegistries.ENTITY_TYPES.getKeys().stream()
                                                                        .filter(id -> id.getNamespace().equals(Lifeoffarce.MOD_ID))
                                                                        .map(ResourceLocation::toString),
                                                                builder
                                                        ))
                                                        .then(createStatSetter("maxHealth"))
                                                        .then(createStatSetter("attackDamage"))
                                                        .then(createStatSetter("movementSpeed"))
                                                        .then(createStatSetter("followRange"))
                                        )
                        )
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createStatSetter(String statName) {
        return Commands.literal(statName)
                .then(
                        Commands.argument("value", DoubleArgumentType.doubleArg(0.0))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    String entityIdStr = StringArgumentType.getString(context, "entity_id");
                                    double value = DoubleArgumentType.getDouble(context, "value");
                                    ResourceLocation entityId = new ResourceLocation(entityIdStr);

                                    EntityStatsConfig.EntityConfig config = EntityStatsConfig.getConfig(entityId);

                                    if (config == null) {
                                        source.sendFailure(Component.literal("Error: La entidad '" + entityIdStr + "' no tiene estadisticas configurables."));
                                        return 0; // Fallo
                                    }

                                    ForgeConfigSpec.DoubleValue configValue;
                                    switch (statName.toLowerCase()) {
                                        case "maxhealth":
                                            configValue = config.maxHealth;
                                            break;
                                        case "attackdamage":
                                            configValue = config.attackDamage;
                                            break;
                                        case "movementspeed":
                                            configValue = config.movementSpeed;
                                            break;
                                        case "followrange":
                                            configValue = config.followRange;
                                            break;
                                        default:
                                            source.sendFailure(Component.literal("Error: Estadistica desconocida '" + statName + "'."));
                                            return 0; // Fallo
                                    }

                                    configValue.set(value);

                                    // ¡AQUÍ ESTÁ LA CORRECCIÓN!
                                    // En lugar de intentar guardar desde el valor, guardamos desde la especificación principal,
                                    // que está disponible de forma estática en nuestra clase de configuración.
                                    EntityStatsConfig.SPEC.save();

                                    source.sendSuccess(() -> Component.literal("Se ha actualizado " + statName + " para '" + entityIdStr + "' a " + value + ". Los cambios se aplicaran en las nuevas entidades."), true);

                                    return 1; // Éxito
                                })
                );
    }
}