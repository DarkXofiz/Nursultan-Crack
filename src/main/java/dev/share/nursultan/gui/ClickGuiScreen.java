package dev.share.nursultan.gui;

import dev.share.nursultan.NursultanClient;
import dev.share.nursultan.config.ConfigManager;
import dev.share.nursultan.module.Category;
import dev.share.nursultan.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Dark panel menu: one column per category, one row per module.
 * Click a module to toggle it, click the dots on its right to open its settings.
 * The widgets are invisible click areas; everything you see is drawn in render().
 */
public final class ClickGuiScreen extends Screen {
    private static final int PANEL_BG = 0xCC12121A;
    private static final int HEADER_BG = 0xFF1C1C28;
    private static final int ACCENT = 0xFF7C5CFF;
    private static final int ROW_ON = 0x887C5CFF;
    private static final int ROW_HOVER = 0x33FFFFFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int GRAY = 0xFFAAAAB4;

    private static final int HEADER_HEIGHT = 18;
    private static final int ROW_HEIGHT = 16;
    private static final int SET_WIDTH = 18;

    private final List<Panel> panels = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.literal("Nursultan"));
    }

    @Override
    protected void init() {
        panels.clear();
        rows.clear();

        Category[] categories = Category.values();
        int gap = 6;
        int columnWidth = Math.min(110, (this.width - gap * (categories.length + 1)) / categories.length);
        int totalWidth = categories.length * columnWidth + (categories.length - 1) * gap;
        int startX = (this.width - totalWidth) / 2;
        int top = 24;

        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            int x = startX + i * (columnWidth + gap);
            List<Module> modules = NursultanClient.MODULES.byCategory(category);
            panels.add(new Panel(category.getDisplayName(), x, top, columnWidth,
                    HEADER_HEIGHT + modules.size() * ROW_HEIGHT + 4));

            int y = top + HEADER_HEIGHT;
            int toggleWidth = columnWidth - SET_WIDTH;
            for (Module module : modules) {
                this.addDrawableChild(invisibleButton(x, y, toggleWidth, ROW_HEIGHT, module::toggle));
                this.addDrawableChild(invisibleButton(x + toggleWidth, y, SET_WIDTH, ROW_HEIGHT,
                        () -> this.client.setScreen(new ModuleSettingsScreen(module, this))));
                rows.add(new Row(module, x, y, columnWidth, toggleWidth));
                y += ROW_HEIGHT;
            }
        }

        int closeX = this.width / 2 - 30;
        int closeY = this.height - 22;
        this.addDrawableChild(invisibleButton(closeX, closeY, 60, 16, this::close));
        panels.add(new Panel("Close", closeX, closeY, 60, 16));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (Panel panel : panels) {
            context.fill(panel.x, panel.y, panel.x + panel.width, panel.y + panel.height, PANEL_BG);
            if (panel.height > 16) {
                context.fill(panel.x, panel.y, panel.x + panel.width, panel.y + 16, HEADER_BG);
                context.fill(panel.x, panel.y + 16, panel.x + panel.width, panel.y + 18, ACCENT);
            }
            int textX = panel.x + (panel.width - this.textRenderer.getWidth(panel.title)) / 2;
            context.drawTextWithShadow(this.textRenderer, panel.title, textX, panel.y + 4, WHITE);
        }

        for (Row row : rows) {
            boolean hovered = mouseX >= row.x && mouseX < row.x + row.width
                    && mouseY >= row.y && mouseY < row.y + ROW_HEIGHT;
            boolean enabled = row.module.isEnabled();
            if (enabled) {
                context.fill(row.x + 2, row.y, row.x + row.width - 2, row.y + ROW_HEIGHT - 1, ROW_ON);
            } else if (hovered) {
                context.fill(row.x + 2, row.y, row.x + row.width - 2, row.y + ROW_HEIGHT - 1, ROW_HOVER);
            }
            String name = this.textRenderer.trimToWidth(row.module.getName(), row.toggleWidth - 8);
            context.drawTextWithShadow(this.textRenderer, name, row.x + 5, row.y + 4, enabled ? WHITE : GRAY);
            context.drawTextWithShadow(this.textRenderer, "...", row.x + row.toggleWidth + 3, row.y + 4, GRAY);
        }
    }

    @Override
    public void removed() {
        ConfigManager.save(NursultanClient.MODULES);
    }

    private static ButtonWidget invisibleButton(int x, int y, int width, int height, Runnable action) {
        ButtonWidget button = ButtonWidget.builder(Text.empty(), btn -> action.run())
                .dimensions(x, y, width, height).build();
        button.setAlpha(0.0f);
        return button;
    }

    private static final class Panel {
        final String title;
        final int x;
        final int y;
        final int width;
        final int height;

        Panel(String title, int x, int y, int width, int height) {
            this.title = title;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private static final class Row {
        final Module module;
        final int x;
        final int y;
        final int width;
        final int toggleWidth;

        Row(Module module, int x, int y, int width, int toggleWidth) {
            this.module = module;
            this.x = x;
            this.y = y;
            this.width = width;
            this.toggleWidth = toggleWidth;
        }
    }
}
