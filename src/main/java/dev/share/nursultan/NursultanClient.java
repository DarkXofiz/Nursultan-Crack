package dev.share.nursultan;

import dev.share.nursultan.command.NurCommand;
import dev.share.nursultan.gui.ClickGuiScreen;
import dev.share.nursultan.module.ModuleManager;
import dev.share.nursultan.module.impl.AntiAfkModule;
import dev.share.nursultan.module.impl.AttackAuraModule;
import dev.share.nursultan.module.impl.AutoEatModule;
import dev.share.nursultan.module.impl.AutoJumpModule;
import dev.share.nursultan.module.impl.AutoRespawnModule;
import dev.share.nursultan.module.impl.AutoSprintModule;
import dev.share.nursultan.module.impl.AutoWalkModule;
import dev.share.nursultan.module.impl.DeathCoordsModule;
import dev.share.nursultan.module.impl.ElytraHelperModule;
import dev.share.nursultan.module.impl.ElytraTargetModule;
import dev.share.nursultan.module.impl.FullbrightModule;
import dev.share.nursultan.module.impl.InfoHudModule;
import dev.share.nursultan.module.impl.TargetInfoModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clean Fabric entrypoint. Optional features should be implemented as separate,
 * documented modules and must not collect credentials or execute shell commands.
 */
public final class NursultanClient implements ClientModInitializer {
    public static final String MOD_ID = "nursultan_opensource";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ModuleManager MODULES = new ModuleManager();

    /** Right Shift opens/closes the menu on devices with a keyboard. */
    private static final int MENU_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;

    private static boolean menuKeyWasDown;
    private static boolean openRequested;

    /** Used by /nur gui; the menu opens on the next client tick. */
    public static void requestGui() {
        openRequested = true;
    }

    @Override
    public void onInitializeClient() {
        MODULES.register(new AutoSprintModule());
        MODULES.register(new FullbrightModule());
        MODULES.register(new InfoHudModule());
        MODULES.register(new AutoRespawnModule());
        MODULES.register(new AntiAfkModule());
        MODULES.register(new AutoWalkModule());
        MODULES.register(new AutoJumpModule());
        MODULES.register(new AutoEatModule());
        MODULES.register(new DeathCoordsModule());
        MODULES.register(new TargetInfoModule());
        MODULES.register(new ElytraHelperModule());
        MODULES.register(new AttackAuraModule());
        MODULES.register(new ElytraTargetModule());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MODULES.tick(client);
            handleMenu(client);
        });
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> NurCommand.register(dispatcher, MODULES));

        LOGGER.info("Nursultan Open Source initialized for Minecraft 1.21.1.");
    }

    private static void handleMenu(MinecraftClient client) {
        boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(), MENU_KEY) == GLFW.GLFW_PRESS;
        boolean pressed = down && !menuKeyWasDown;
        menuKeyWasDown = down;

        if (client.currentScreen == null) {
            if (pressed || openRequested) {
                openRequested = false;
                client.setScreen(new ClickGuiScreen(MODULES));
            }
        } else if (client.currentScreen instanceof ClickGuiScreen && pressed) {
            client.setScreen(null);
        }
    }
}
