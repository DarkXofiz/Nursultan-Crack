package dev.share.nursultan.gui;

import dev.share.nursultan.module.Module;
import dev.share.nursultan.module.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Module menu: one button per module (two columns), tap to toggle. */
public final class ClickGuiScreen extends Screen {
    private final ModuleManager modules;

    public ClickGuiScreen(ModuleManager modules) {
        super(Text.literal("Nursultan"));
        this.modules = modules;
    }

    @Override
    protected void init() {
        int buttonWidth = 120;
        int buttonHeight = 20;
        int gap = 4;
        int left = this.width / 2 - buttonWidth - gap / 2;
        int top = 30;

        int index = 0;
        for (Module module : modules.getModules()) {
            int column = index % 2;
            int row = index / 2;
            int x = left + column * (buttonWidth + gap);
            int y = top + row * (buttonHeight + gap);
            ButtonWidget button = ButtonWidget.builder(label(module), btn -> {
                module.toggle();
                btn.setMessage(label(module));
            }).dimensions(x, y, buttonWidth, buttonHeight).build();
            this.addDrawableChild(button);
            index++;
        }

        int rows = (index + 1) / 2;
        int closeY = top + rows * (buttonHeight + gap) + 8;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> this.close())
                .dimensions(this.width / 2 - buttonWidth / 2, closeY, buttonWidth, buttonHeight).build());
    }

    private static Text label(Module module) {
        Text state = module.isEnabled()
                ? Text.literal("ON").formatted(Formatting.GREEN)
                : Text.literal("OFF").formatted(Formatting.RED);
        return Text.literal(module.getName() + ": ").append(state);
    }
}
