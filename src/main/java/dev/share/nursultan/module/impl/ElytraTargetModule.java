package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.ModeSetting;
import dev.share.nursultan.setting.NumberSetting;
import dev.share.nursultan.util.Targeting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;

/** While airborne with an elytra equipped, turns your view toward the best target. */
public final class ElytraTargetModule extends Module {
    private final NumberSetting range = addSetting(
            new NumberSetting("range", "Search distance in blocks", 64.0, 8.0, 128.0));
    private final ModeSetting targets = addSetting(
            new ModeSetting("targets", "Which entities to follow", "both", Targeting.TARGET_MODES));
    private final ModeSetting sort = addSetting(
            new ModeSetting("sort", "How to pick between targets", "distance", Targeting.SORT_MODES));

    public ElytraTargetModule() {
        super("ElytraTarget", "Aims at the best target while flying with an elytra.");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.currentScreen != null || client.player.isOnGround()) {
            return;
        }
        if (!client.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            return;
        }
        LivingEntity target = Targeting.find(client, range.get(), targets.get(), sort.get(), 360.0, true);
        if (target != null) {
            Targeting.faceEntity(client.player, target);
        }
    }
}
