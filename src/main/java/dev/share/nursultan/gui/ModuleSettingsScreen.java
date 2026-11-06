package dev.share.nursultan.gui;

import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.config.ConfigManager;
import dev.share.nursultan.module.Module;
import dev.share.nursultan.setting.NumberSetting;
import dev.share.nursultan.setting.Setting;
import dev.share.nursultan.util.KeyNames;
import dev.share.nursultan.util.KeyUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Settings and key bind of one module, drawn in the same dark style as the main menu. */
public final class ModuleSettingsScreen extends Screen {
    private static final int PANEL_BG = 0xCC12121A;
    private static final int HEADER_BG = 0xFF1C1C28;
    private static final int ACCENT = 0xFF7C5CFF;
    private static final int BUTTON_BG = 0xFF23232F;
    private static final int HOVER = 0x33FFFFFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GRAY = 0xFFAAAAB4;

    private static final int LINE_HEIGHT = 18;
    private static final int PANEL_WIDTH = 210;

    private final Module module;
    private final Screen parent;
    private final List<Line> lines = new ArrayList<>();
    private int panelX;
    private int panelY;
    private int panelHeight;
    private boolean listening;

    public ModuleSettingsScreen(Module module, Screen parent) {
        super(Text.literal(module.getName() + " settings"));
        this.module = module;
        this.parent = parent;
    }

    @Override
    protected void init() {
        lines.clear();
        panelX = (this.width - PANEL_WIDTH) / 2;
        panelY = 24;
        int y = panelY + 4;

        lines.add(new Line(panelX + 4, y, PANEL_WIDTH - 8, () -> module.getName(), true));
        y += LINE_HEIGHT + 2;

        for (Setting setting : module.getSettings()) {
            if (setting instanceof NumberSetting) {
                NumberSetting number = (NumberSetting) setting;
                int x = panelX + 4;
                int fullWidth = PANEL_WIDTH - 8;
                addClickArea(x, y, 24, () -> number.adjust(-1));
                addClickArea(x + fullWidth - 24, y, 24, () -> number.adjust(1));
                lines.add(new Line(x, y, fullWidth, number::label, false).withNumberButtons());
            } else {
                addClickArea(panelX + 4, y, PANEL_WIDTH - 8, setting::cycle);
                lines.add(new Line(panelX + 4, y, PANEL_WIDTH - 8, setting::label, false));
            }
            y += LINE_HEIGHT + 2;
        }

        addClickArea(panelX + 4, y, PANEL_WIDTH - 8, () -> listening = true);
        lines.add(new Line(panelX + 4, y, PANEL_WIDTH - 8, this::bindText, false));
        y += LINE_HEIGHT + 2;

        addClickArea(panelX + 4, y, PANEL_WIDTH - 8, this::goBack);
        lines.add(new Line(panelX + 4, y, PANEL_WIDTH - 8, () -> "Back", false));
        y += LINE_HEIGHT + 2;

        panelHeight = y - panelY + 2;
    }

    private void addClickArea(int x, int y, int width, Runnable action) {
        ButtonWidget button = ButtonWidget.builder(Text.empty(), btn -> action.run())
                .dimensions(x, y, width, LINE_HEIGHT).build();
        button.setAlpha(0.0f);
        this.addDrawableChild(button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + panelHeight, PANEL_BG);
        for (Line line : lines) {
            int background = line.header ? HEADER_BG : BUTTON_BG;
            context.fill(line.x, line.y, line.x + line.width, line.y + LINE_HEIGHT, background);
            if (line.header) {
                context.fill(line.x, line.y + LINE_HEIGHT - 2, line.x + line.width, line.y + LINE_HEIGHT, ACCENT);
            } else if (mouseX >= line.x && mouseX < line.x + line.width
                    && mouseY >= line.y && mouseY < line.y + LINE_HEIGHT) {
                context.fill(line.x, line.y, line.x + line.width, line.y + LINE_HEIGHT, HOVER);
            }

            String text = this.textRenderer.trimToWidth(line.text.get(), line.width - 8);
            int textX = line.x + (line.width - this.textRenderer.getWidth(text)) / 2;
            context.drawTextWithShadow(this.textRenderer, text, textX, line.y + 5, line.header ? WHITE : GRAY);

            if (line.numberButtons) {
                context.drawTextWithShadow(this.textRenderer, "-", line.x + 9, line.y + 5, WHITE);
                context.drawTextWithShadow(this.textRenderer, "+", line.x + line.width - 15, line.y + 5, WHITE);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!listening || this.client == null) {
            return;
        }
        try {
            int key = KeyUtil.firstPressed(this.client.getWindow().getHandle());
            if (key == -1) {
                return;
            }
            if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                module.setKeyBind(0);
            } else if (key != GLFW.GLFW_KEY_ESCAPE) {
                module.setKeyBind(key);
            }
        } catch (Throwable t) {
            NursultanClient.LOGGER.warn("Key binding is not supported here, use /nur bind", t);
        }
        listening = false;
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

    private String bindText() {
        if (listening) {
            return "Press a key... (Esc cancel, Del clear)";
        }
        return "Bind: " + KeyNames.of(module.getKeyBind());
    }

    private static final class Line {
        final int x;
        final int y;
        final int width;
        final Supplier<String> text;
        final boolean header;
        boolean numberButtons;

        Line(int x, int y, int width, Supplier<String> text, boolean header) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.text = text;
            this.header = header;
        }

        Line withNumberButtons() {
            this.numberButtons = true;
            return this;
        }
    }
}
