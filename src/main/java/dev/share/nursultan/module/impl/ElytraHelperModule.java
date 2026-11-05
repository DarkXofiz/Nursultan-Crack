package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

/**
 * One-shot swap: puts an elytra on if you wear a chestplate, or puts a chestplate
 * back on if you wear an elytra. Turns itself off afterwards.
 */
public final class ElytraHelperModule extends Module {
    private static final int CHEST_SLOT = 6;
    private static final int FIRST_INVENTORY_SLOT = 9;
    private static final int LAST_INVENTORY_SLOT = 44;

    private static final Item[] CHESTPLATES = {
            Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE,
            Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.LEATHER_CHESTPLATE
    };

    public ElytraHelperModule() {
        super("ElytraHelper", "Swaps elytra and chestplate once, then turns off.", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        setEnabled(false);

        ScreenHandler handler = client.player.playerScreenHandler;
        boolean wearingElytra = handler.getSlot(CHEST_SLOT).getStack().isOf(Items.ELYTRA);

        int source = -1;
        for (int slot = FIRST_INVENTORY_SLOT; slot <= LAST_INVENTORY_SLOT; slot++) {
            ItemStack stack = handler.getSlot(slot).getStack();
            boolean match = wearingElytra ? isChestplate(stack) : stack.isOf(Items.ELYTRA);
            if (match) {
                source = slot;
                break;
            }
        }

        if (source == -1) {
            client.player.sendMessage(Text.literal(wearingElytra
                    ? "ElytraHelper: no chestplate found in your inventory."
                    : "ElytraHelper: no elytra found in your inventory."), false);
            return;
        }

        int syncId = handler.syncId;
        client.interactionManager.clickSlot(syncId, source, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, CHEST_SLOT, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, source, 0, SlotActionType.PICKUP, client.player);
    }

    private static boolean isChestplate(ItemStack stack) {
        for (Item item : CHESTPLATES) {
            if (stack.isOf(item)) {
                return true;
            }
        }
        return false;
    }
}
