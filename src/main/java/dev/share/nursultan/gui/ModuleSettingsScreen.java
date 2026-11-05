package dev.share.nursultan.gui;

import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.config.ConfigManager;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.NumberSetting;
import dev.share.nursultan.setting.Setting;
import dev.share.nursultan.util.KeyNames;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** Settings and key bind of one module. */
public final class ModuleSettingsScreen extends Screen {
    private final Module module;
    private final Screen parent;
    private ButtonWidget bindButton;
    private boolean listening;

    public ModuleSettingsScreen(Module module, Screen parent) {
        super(Text.literal(module.getName() + " settings"));
        this.module = module;
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = 30;

        ButtonWidget header = ButtonWidget.builder(Text.literal(module.getName()), btn -> {
        }).dimensions(cx - 100, y, 200, 20).build();
        header.active = false;
        this.addDrawableChild(header);
        y += 26;

        for (Setting setting : module.getSettings()) {
            if (setting instanceof NumberSetting) {
                NumberSetting number = (NumberSetting) setting;
                ButtonWidget valueLabel = ButtonWidget.builder(Text.literal(number.label()), btn -> {
                }).dimensions(cx - 70, y, 140, 20).build();
                valueLabel.active = false;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("-"), btn -> {
                    number.adjust(-1);
                    valueLabel.setMessage(Text.literal(number.label()));
                }).dimensions(cx - 100, y, 28, 20).build());
                this.addDrawableChild(valueLabel);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), btn -> {
                    number.adjust(1);
                    valueLabel.setMessage(Text.literal(number.label()));
                }).dimensions(cx + 72, y, 28, 20).build());
            } else {
                this.addDrawableChild(ButtonWidget.builder(Text.literal(setting.label()), btn -> {
                    setting.cycle();
                    btn.setMessage(Text.literal(setting.label()));
                }).dimensions(cx - 100, y, 200, 20).build());
            }
            y += 22;
        }

        bindButton = ButtonWidget.builder(bindLabel(), btn -> {
            listening = true;
            btn.setMessage(Text.literal("Press a key... (Esc cancel, Del clear)"));
        }).dimensions(cx - 100, y, 200, 20).build();
        this.addDrawableChild(bindButton);
        y += 22;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), btn -> this.goBack())
                .dimensions(cx - 100, y + 6, 200, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        if (!listening || this.client == null) {
            return;
        }
        long handle = this.client.getWindow().getHandle();
        for (int key = 32; key <= 348; key++) {
            if (GLFW.glfwGetKey(handle, key) != GLFW.GLFW_PRESS) {
                continue;
            }
            if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                module.setKeyBind(0);
            } else if (key != GLFW.GLFW_KEY_ESCAPE) {
                module.setKeyBind(key);
            }
            listening = false;
            bindButton.setMessage(bindLabel());
            break;
        }
    }

    @Override
    public void removed() {
        ConfigManager.save(NursultanClient.MODULES);
    }

    private void goBack() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    private Text bindLabel() {
        return Text.literal("Bind: " + KeyNames.of(module.getKeyBind()));
    }
}
