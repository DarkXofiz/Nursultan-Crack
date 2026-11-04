package dev.share.nursultan;

import dev.share.nursultan.command.NurCommand;
import dev.share.nursultan.module.ModuleManager;
import dev.share.nursultan.module.impl.AutoSprintModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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

    @Override
    public void onInitializeClient() {
        MODULES.register(new AutoSprintModule());

        ClientTickEvents.END_CLIENT_TICK.register(MODULES::tick);
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> NurCommand.register(dispatcher, MODULES));

        LOGGER.info("Nursultan Open Source initialized for Minecraft 1.21.1.");
    }
}
