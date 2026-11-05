package dev.share.nursultan.gui;

import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.config.ConfigManager;
import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Module menu with category tabs, toggle buttons and a settings button per module. */
public final class ClickGuiScreen extends Screen {
    private static Category selected = Category.COMBAT;

    public ClickGuiScreen() {
        super(Text.literal("Nursultan"));
    }

    @Override
    protected void init() {
        Category[] categories = Category.values();
        int tabWidth = 72;
        int gap = 4;
        int total = categories.length * tabWidth + (categories.length - 1) * gap;
        int tabX = (this.width - total) / 2;
        for (Category category : categories) {
            String text = category == selected ? "> " + category.getDisplayName() : category.getDisplayName();
            this.addDrawableChild(ButtonWidget.builder(Text.literal(text), btn -> {
                selected = category;
                this.clearAndInit();
            }).dimensions(tabX, 20, tabWidth, 20).build());
            tabX += tabWidth + gap;
        }

        int y = 48;
        for (Module module : NursultanClient.MODULES.byCategory(selected)) {
            this.addDrawableChild(ButtonWidget.builder(label(module), btn -> {
                module.toggle();
                btn.setMessage(label(module));
            }).dimensions(this.width / 2 - 82, y, 120, 20).build());
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Set"), btn ->
                    this.client.setScreen(new ModuleSettingsScreen(module, this)))
                    .dimensions(this.width / 2 + 42, y, 40, 20).build());
            y += 22;
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> this.close())
                .dimensions(this.width / 2 - 60, y + 6, 120, 20).build());
    }

    @Override
    public void removed() {
        ConfigManager.save(NursultanClient.MODULES);
    }

    private static Text label(Module module) {
        Text state = module.isEnabled()
                ? Text.literal("ON").formatted(Formatting.GREEN)
                : Text.literal("OFF").formatted(Formatting.RED);
        return Text.literal(module.getName() + ": ").append(state);
    }
}
