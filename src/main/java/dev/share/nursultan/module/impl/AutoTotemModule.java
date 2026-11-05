package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

/** Moves a totem of undying into your off hand when it is empty of one. */
public final class AutoTotemModule extends Module {
    private static final int OFFHAND_SLOT = 45;
    private static final int FIRST_INVENTORY_SLOT = 9;
    private static final int LAST_INVENTORY_SLOT = 44;

    private final NumberSetting delay = addSetting(
            new NumberSetting("delay", "Ticks between swaps", 4.0, 1.0, 20.0));
    private final NumberSetting health = addSetting(
            new NumberSetting("health", "Only swap when health is at or below this (20 = always)", 20.0, 1.0, 20.0));
    private int cooldown;

    public AutoTotemModule() {
        super("AutoTotem", "Keeps a totem in your off hand.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (client.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)
                || client.player.getHealth() > health.get()) {
            return;
        }
        ScreenHandler handler = client.player.playerScreenHandler;
        int source = -1;
        for (int slot = FIRST_INVENTORY_SLOT; slot <= LAST_INVENTORY_SLOT; slot++) {
            if (handler.getSlot(slot).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                source = slot;
                break;
            }
        }
        if (source == -1) {
            return;
        }
        int syncId = handler.syncId;
        client.interactionManager.clickSlot(syncId, source, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, OFFHAND_SLOT, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, source, 0, SlotActionType.PICKUP, client.player);
        cooldown = (int) delay.get();
    }
}
