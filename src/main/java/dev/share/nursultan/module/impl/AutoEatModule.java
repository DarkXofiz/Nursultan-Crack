package dev.share.nursultan.module.impl;

import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

/** Eats the food in your main hand when your hunger bar is low. */
public final class AutoEatModule extends Module {
    private static final int HUNGER_THRESHOLD = 14;
    private boolean eating;

    public AutoEatModule() {
        super("AutoEat", "Eats held food when hunger is low.", Category.PLAYER);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        boolean hungry = client.player.getHungerManager().getFoodLevel() <= HUNGER_THRESHOLD;
        ItemStack held = client.player.getMainHandStack();
        boolean holdingFood = held.contains(DataComponentTypes.FOOD);

        if (hungry && holdingFood && client.currentScreen == null) {
            client.options.useKey.setPressed(true);
            eating = true;
        } else if (eating) {
            client.options.useKey.setPressed(false);
            eating = false;
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (eating && client != null && client.options != null) {
            client.options.useKey.setPressed(false);
        }
        eating = false;
    }
}
