package dev.share.nursultan.util;

import org.lwjgl.glfw.GLFW;

/** Crash-safe keyboard polling (some launchers do not implement every GLFW call). */
public final class KeyUtil {
    private static final int[] BINDABLE = buildBindable();

    private KeyUtil() {
    }

    private static int[] buildBindable() {
        int[] keys = new int[512];
        int count = 0;
        keys[count++] = 32;
        keys[count++] = 39;
        for (int k = 44; k <= 57; k++) {
            keys[count++] = k;
        }
        keys[count++] = 59;
        keys[count++] = 61;
        for (int k = 65; k <= 93; k++) {
            keys[count++] = k;
        }
        keys[count++] = 96;
        for (int k = 256; k <= 269; k++) {
            keys[count++] = k;
        }
        keys[count++] = 280;
        for (int k = 290; k <= 301; k++) {
            keys[count++] = k;
        }
        for (int k = 320; k <= 329; k++) {
            keys[count++] = k;
        }
        for (int k = 340; k <= 348; k++) {
            keys[count++] = k;
        }
        int[] result = new int[count];
        System.arraycopy(keys, 0, result, 0, count);
        return result;
    }

    public static boolean isDown(long window, int key) {
        try {
            return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
        } catch (Throwable t) {
            return false;
        }
    }

    /** First pressed key from the bindable list, or -1. */
    public static int firstPressed(long window) {
        for (int key : BINDABLE) {
            if (isDown(window, key)) {
                return key;
            }
        }
        return -1;
    }
}
