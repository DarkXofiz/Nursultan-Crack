package dev.share.nursultan.util;

import org.lwjgl.glfw.GLFW;

/** Key names without calling any native GLFW lookup. */
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
        if (key >= 65 && key <= 90) {
            return String.valueOf((char) key);
        }
        if (key >= 48 && key <= 57) {
            return String.valueOf((char) key);
        }
        if (key >= GLFW.GLFW_KEY_F1 && key <= GLFW.GLFW_KEY_F12) {
            return "F" + (key - GLFW.GLFW_KEY_F1 + 1);
        }
        if (key >= 320 && key <= 329) {
            return "NUM" + (key - 320);
        }
        return "KEY" + key;
    }

    /** Parses a key name (A-Z, 0-9, F1-F12, SPACE, LSHIFT, ...). Returns 0 for NONE, -1 if unknown. */
    public static int parse(String name) {
        String n = name.toUpperCase();
        if (n.equals("NONE")) {
            return 0;
        }
        if (n.length() == 1) {
            char c = n.charAt(0);
            if ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                return c;
            }
        }
        if (n.matches("F([1-9]|1[0-2])")) {
            return GLFW.GLFW_KEY_F1 + Integer.parseInt(n.substring(1)) - 1;
        }
        switch (n) {
            case "SPACE":
                return GLFW.GLFW_KEY_SPACE;
            case "LSHIFT":
                return GLFW.GLFW_KEY_LEFT_SHIFT;
            case "RSHIFT":
                return GLFW.GLFW_KEY_RIGHT_SHIFT;
            case "LCTRL":
                return GLFW.GLFW_KEY_LEFT_CONTROL;
            case "RCTRL":
                return GLFW.GLFW_KEY_RIGHT_CONTROL;
            case "LALT":
                return GLFW.GLFW_KEY_LEFT_ALT;
            case "RALT":
                return GLFW.GLFW_KEY_RIGHT_ALT;
            case "TAB":
                return GLFW.GLFW_KEY_TAB;
            case "ENTER":
                return GLFW.GLFW_KEY_ENTER;
            case "UP":
                return GLFW.GLFW_KEY_UP;
            case "DOWN":
                return GLFW.GLFW_KEY_DOWN;
            case "LEFT":
                return GLFW.GLFW_KEY_LEFT;
            case "RIGHT":
                return GLFW.GLFW_KEY_RIGHT;
            default:
                return -1;
        }
    }
}
