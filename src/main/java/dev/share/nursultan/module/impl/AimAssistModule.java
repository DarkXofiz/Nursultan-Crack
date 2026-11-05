package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.ModeSetting;
import dev.share.nursultan.setting.NumberSetting;
import dev.share.nursultan.util.Targeting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

/** Gently turns your view toward the best target in range. */
public final class AimAssistModule extends Module {
    private final NumberSetting range = addSetting(
            new NumberSetting("range", "Distance in blocks", 4.5, 1.0, 8.0));
    private final NumberSetting fov = addSetting(
            new NumberSetting("fov", "Field of view for targets", 90.0, 10.0, 360.0));
    private final NumberSetting speed = addSetting(
            new NumberSetting("speed", "How fast the view turns (0.1 slow, 1.0 instant)", 0.3, 0.1, 1.0));
    private final ModeSetting targets = addSetting(
            new ModeSetting("targets", "Which entities to aim at", "both", Targeting.TARGET_MODES));
    private final ModeSetting sort = addSetting(
            new ModeSetting("sort", "How to pick between targets", "distance", Targeting.SORT_MODES));

    public AimAssistModule() {
        super("AimAssist", "Smoothly aims at the best target.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null) {
            return;
        }
        LivingEntity target = Targeting.find(client, range.get(), targets.get(), sort.get(), fov.get(), false);
        if (target == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        float factor = (float) speed.get();
        float yawDiff = MathHelper.wrapDegrees(Targeting.yawTo(player, target) - player.getYaw());
        float pitchDiff = Targeting.pitchTo(player, target) - player.getPitch();
        player.setYaw(player.getYaw() + yawDiff * factor);
        player.setPitch(MathHelper.clamp(player.getPitch() + pitchDiff * factor, -90.0f, 90.0f));
    }
}
