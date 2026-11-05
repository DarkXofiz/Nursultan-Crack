package dev.share.nursultan.util;

import org.lwjgl.glfw.GLFW;

public final class KeyNames {
    private KeyNames() {
    }

    public static String of(int key) {
        if (key == 0) {
            return "NONE";
        }
        switch (key) {
            case GLFW.GLFW_KEY_SPACE:
                return "SPACE";
            case GLFW.GLFW_KEY_LEFT_SHIFT:
                return "LSHIFT";
            case GLFW.GLFW_KEY_RIGHT_SHIFT:
                return "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_CONTROL:
                return "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                return "RCTRL";
            case GLFW.GLFW_KEY_LEFT_ALT:
                return "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT:
                return "RALT";
            case GLFW.GLFW_KEY_TAB:
                return "TAB";
            case GLFW.GLFW_KEY_ENTER:
                return "ENTER";
            case GLFW.GLFW_KEY_UP:
                return "UP";
            case GLFW.GLFW_KEY_DOWN:
                return "DOWN";
            case GLFW.GLFW_KEY_LEFT:
                return "LEFT";
            case GLFW.GLFW_KEY_RIGHT:
                return "RIGHT";
            default:
                break;
        }
        if (key >= GLFW.GLFW_KEY_F1 && key <= GLFW.GLFW_KEY_F12) {
            return "F" + (key - GLFW.GLFW_KEY_F1 + 1);
        }
        String name = GLFW.glfwGetKeyName(key, 0);
        if (name != null) {
            return name.toUpperCase();
        }
        return "KEY" + key;
    }
}
