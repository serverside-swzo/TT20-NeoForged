package net.swzo.tt20neoforged.command;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swzo.tt20neoforged.Tt20NeoForged;
import net.swzo.tt20neoforged.config.BlockEntityMaskConfig;
import net.swzo.tt20neoforged.config.MainConfig;
import net.swzo.tt20neoforged.util.TPSUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MainCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tt20")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("status").executes(MainCommand::executeStatus))
                        .then(Commands.literal("reload").executes(MainCommand::executeReload))
                        .then(Commands.literal("toggle")
                                .then(Commands.argument("key", StringArgumentType.string())
                                        .suggests(MainCommand::suggestConfigKeys)
                                        .executes(MainCommand::executeToggle)))
                        .then(Commands.literal("mask")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                                .executes(MainCommand::executeMaskAdd)))
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                                .suggests(MainCommand::suggestMaskedBlocks)
                                                .executes(MainCommand::executeMaskRemove)))
                                .then(Commands.literal("list")
                                        .executes(MainCommand::executeMaskList)))

                        .then(Commands.literal("set-tick-repeat")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1, 1000))
                                        .executes(MainCommand::executeSetTickRepeat)))
        );
    }

    private static CompletableFuture<Suggestions> suggestConfigKeys(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {

        List<String> keys = MainConfig.SPEC.getValues().entrySet().stream()
                .filter(entry -> entry.getValue() instanceof ModConfigSpec.BooleanValue)
                .map(entry -> {

                    String fullPath = entry.getKey();
                    return fullPath.substring(fullPath.lastIndexOf('.') + 1);
                })
                .collect(Collectors.toList());
        return SharedSuggestionProvider.suggest(keys, builder);
    }

    private static CompletableFuture<Suggestions> suggestMaskedBlocks(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(BlockEntityMaskConfig.INSTANCE.getBlocks(), builder);
    }

    private static int executeStatus(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (Tt20NeoForged.TPS_CALCULATOR == null) {
            source.sendFailure(Component.literal("TPS Calculator is not initialized!"));
            return 0;
        }

        double tps = Tt20NeoForged.TPS_CALCULATOR.getTPS();
        double mspt = Tt20NeoForged.TPS_CALCULATOR.getSmoothedMSPT();

        source.sendSystemMessage(Component.translatable("tt20.command.status.header"));
        source.sendSystemMessage(Component.translatable("tt20.command.status.tps",
                TPSUtil.colorizeTPS(tps, true),
                String.format("%.2f", mspt)));

        source.sendSystemMessage(Component.translatable("tt20.command.status.missed_ticks",
                TPSUtil.formatMissedTicks(Tt20NeoForged.TPS_CALCULATOR.getAllMissedTicks())));

        return 1;
    }
    private static List<ModConfigSpec.ConfigValue<?>> getAllConfigValues(UnmodifiableConfig config) {
        List<ModConfigSpec.ConfigValue<?>> values = new ArrayList<>();
        if (config == null) return values;

        for (Object obj : config.valueMap().values()) {
            if (obj instanceof ModConfigSpec.ConfigValue) {
                values.add((ModConfigSpec.ConfigValue<?>) obj);
            } else if (obj instanceof UnmodifiableConfig) {

                values.addAll(getAllConfigValues((UnmodifiableConfig) obj));
            }
        }
        return values;
    }
    private static int executeToggle(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String key = StringArgumentType.getString(context, "key");

        ModConfigSpec.ConfigValue<?> targetValue = null;

        List<ModConfigSpec.ConfigValue<?>> allValues = getAllConfigValues(MainConfig.SPEC.getValues());

        for (ModConfigSpec.ConfigValue<?> value : allValues) {
            List<String> path = value.getPath();
            if (path.get(path.size() - 1).equals(key)) {
                targetValue = value;
                break;
            }
        }

        if (targetValue instanceof ModConfigSpec.BooleanValue booleanValue) {
            boolean newValue = !booleanValue.get();
            booleanValue.set(newValue);

            Component enabledText = Component.translatable(newValue ? "tt20.status.enabled" : "tt20.status.disabled");
            source.sendSystemMessage(Component.translatable("tt20.command.toggle.feedback", key, enabledText));
            return 1;
        } else {
            source.sendFailure(Component.translatable("tt20.command.toggle.unknown_key", key));
            return 0;
        }
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        BlockEntityMaskConfig.INSTANCE.reload();

        source.sendSystemMessage(Component.translatable("tt20.command.reload.success"));
        return 1;
    }

    private static int executeMaskAdd(CommandContext<CommandSourceStack> context) {
        ResourceLocation id = ResourceLocationArgument.getId(context, "id");
        String idString = id.toString();

        if (BlockEntityMaskConfig.INSTANCE.addBlock(idString)) {
            BlockEntityMaskConfig.INSTANCE.refreshMask(); 
            context.getSource().sendSystemMessage(Component.translatable("tt20.command.mask.add.success", idString));
            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("tt20.command.mask.add.exists", idString));
            return 0;
        }
    }

    private static int executeMaskRemove(CommandContext<CommandSourceStack> context) {
        ResourceLocation id = ResourceLocationArgument.getId(context, "id");
        String idString = id.toString();

        if (BlockEntityMaskConfig.INSTANCE.removeBlock(idString)) {
            BlockEntityMaskConfig.INSTANCE.refreshMask(); 
            context.getSource().sendSystemMessage(Component.translatable("tt20.command.mask.remove.success", idString));
            return 1;
        } else {
            context.getSource().sendFailure(Component.translatable("tt20.command.mask.remove.not_found", idString));
            return 0;
        }
    }

    private static int executeMaskList(CommandContext<CommandSourceStack> context) {
        List<String> blocks = BlockEntityMaskConfig.INSTANCE.getBlocks();
        context.getSource().sendSystemMessage(Component.translatable("tt20.command.mask.list.header", blocks.size()));

        for (String block : blocks) {
            context.getSource().sendSystemMessage(Component.literal("- " + block));
        }
        return 1;
    }

    private static int executeSetTickRepeat(CommandContext<CommandSourceStack> context) {
        int val = IntegerArgumentType.getInteger(context, "value");
        MainConfig.INSTANCE.setTickRepeatCap(val);
        context.getSource().sendSystemMessage(Component.translatable("tt20.command.set_cap.success", val));
        return 1;
    }
}