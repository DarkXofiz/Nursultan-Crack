package dev.share.nursultan.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.module.ModuleManager;
import dev.share.nursultan.setting.Setting;
import dev.share.nursultan.util.KeyNames;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.Optional;

/**
 * Client-side commands:
 * /nur gui, /nur list, /nur toggle MODULE, /nur settings MODULE,
 * /nur set MODULE SETTING VALUE, /nur coords
 */
public final class NurCommand {
    private NurCommand() {
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, ModuleManager modules) {
        dispatcher.register(ClientCommandManager.literal("nur")
                .then(ClientCommandManager.literal("gui").executes(ctx -> {
                    NursultanClient.requestGui();
                    ctx.getSource().sendFeedback(Text.literal("Opening menu..."));
                    return 1;
                }))
                .then(ClientCommandManager.literal("list").executes(ctx -> {
                    for (Module module : modules.getModules()) {
                        String state = module.isEnabled() ? "ON" : "OFF";
                        ctx.getSource().sendFeedback(Text.literal(
                                module.getName() + " [" + state + "] - " + module.getDescription()));
                    }
                    return 1;
                }))
                .then(ClientCommandManager.literal("toggle")
                        .then(ClientCommandManager.argument("module", StringArgumentType.word()).executes(ctx -> {
                            Module module = findModule(ctx.getSource(), modules,
                                    StringArgumentType.getString(ctx, "module"));
                            if (module == null) {
                                return 0;
                            }
                            module.toggle();
                            ctx.getSource().sendFeedback(Text.literal(
                                    module.getName() + " is now " + (module.isEnabled() ? "ON" : "OFF")));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("settings")
                        .then(ClientCommandManager.argument("module", StringArgumentType.word()).executes(ctx -> {
                            Module module = findModule(ctx.getSource(), modules,
                                    StringArgumentType.getString(ctx, "module"));
                            if (module == null) {
                                return 0;
                            }
                            if (module.getSettings().isEmpty()) {
                                ctx.getSource().sendFeedback(Text.literal(module.getName() + " has no settings."));
                                return 1;
                            }
                            for (Setting setting : module.getSettings()) {
                                ctx.getSource().sendFeedback(Text.literal(setting.getName() + " = "
                                        + setting.valueToString() + " - " + setting.getDescription()));
                            }
                            return 1;
                        })))
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.argument("module", StringArgumentType.word())
                                .then(ClientCommandManager.argument("setting", StringArgumentType.word())
                                        .then(ClientCommandManager.argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    Module module = findModule(ctx.getSource(), modules,
                                                            StringArgumentType.getString(ctx, "module"));
                                                    if (module == null) {
                                                        return 0;
                                                    }
                                                    String settingName = StringArgumentType.getString(ctx, "setting");
                                                    Optional<Setting> found = module.findSetting(settingName);
                                                    if (found.isEmpty()) {
                                                        ctx.getSource().sendError(Text.literal(
                                                                "Unknown setting: " + settingName));
                                                        return 0;
                                                    }
                                                    Setting setting = found.get();
                                                    String error = setting.setFromString(
                                                            StringArgumentType.getString(ctx, "value"));
                                                    if (error != null) {
                                                        ctx.getSource().sendError(Text.literal(error));
                                                        return 0;
                                                    }
                                                    ctx.getSource().sendFeedback(Text.literal(
                                                            module.getName() + "." + setting.getName()
                                                                    + " = " + setting.valueToString()));
                                                    return 1;
                                                })))))
                .then(ClientCommandManager.literal("bind")
                        .then(ClientCommandManager.argument("module", StringArgumentType.word())
                                .then(ClientCommandManager.argument("key", StringArgumentType.word()).executes(ctx -> {
                                    Module module = findModule(ctx.getSource(), modules,
                                            StringArgumentType.getString(ctx, "module"));
                                    if (module == null) {
                                        return 0;
                                    }
                                    String keyName = StringArgumentType.getString(ctx, "key");
                                    int key = KeyNames.parse(keyName);
                                    if (key < 0) {
                                        ctx.getSource().sendError(Text.literal("Unknown key: " + keyName));
                                        return 0;
                                    }
                                    module.setKeyBind(key);
                                    ctx.getSource().sendFeedback(Text.literal(
                                            module.getName() + " bound to " + KeyNames.of(key)));
                                    return 1;
                                }))))
                .then(ClientCommandManager.literal("coords").executes(ctx -> {
                    var player = ctx.getSource().getPlayer();
                    ctx.getSource().sendFeedback(Text.literal(String.format(
                            "XYZ: %.1f %.1f %.1f", player.getX(), player.getY(), player.getZ())));
                    return 1;
                })));
    }

    private static Module findModule(FabricClientCommandSource source, ModuleManager modules, String name) {
        Optional<Module> found = modules.find(name);
        if (found.isEmpty()) {
            source.sendError(Text.literal("Unknown module: " + name));
            return null;
        }
        return found.get();
    }
}
