package dev.share.nursultan.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.module.ModuleManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.Optional;

/** Client-side commands: /nur list, /nur toggle <module>, /nur coords */
public final class NurCommand {
    private NurCommand() {
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, ModuleManager modules) {
        dispatcher.register(ClientCommandManager.literal("nur")
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
                            String name = StringArgumentType.getString(ctx, "module");
                            Optional<Module> found = modules.find(name);
                            if (found.isEmpty()) {
                                ctx.getSource().sendError(Text.literal("Unknown module: " + name));
                                return 0;
                            }
                            Module module = found.get();
                            module.toggle();
                            ctx.getSource().sendFeedback(Text.literal(
                                    module.getName() + " is now " + (module.isEnabled() ? "ON" : "OFF")));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("coords").executes(ctx -> {
                    var player = ctx.getSource().getPlayer();
                    ctx.getSource().sendFeedback(Text.literal(String.format(
                            "XYZ: %.1f %.1f %.1f", player.getX(), player.getY(), player.getZ())));
                    return 1;
                })));
    }
}
