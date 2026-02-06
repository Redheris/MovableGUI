package dev.redheris.movablegui.state;

import net.minecraft.client.gui.components.Button;

public class GUIViewState {
    public static Button resetButton;
    private static boolean changedPos = false;
    private static int x = 0;
    private static int y = 0;
    private static boolean renderTransparentBackground = true;
    private static boolean keyToggled = false;

    private static void setChanged(boolean changed) {
        GUIViewState.changedPos = changed;
        if (resetButton != null)
            resetButton.visible = changed;
    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        setChanged(true);
        GUIViewState.x = x;
        resetButton.setX(x);
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        setChanged(true);
        GUIViewState.y = y;
        resetButton.setY(y - 20);
    }

    public static boolean doRenderTransparentBackground() {
        return renderTransparentBackground;
    }

    public static void toggleTransparentBackground() {
        renderTransparentBackground = !renderTransparentBackground;
        if (!changedPos) {
            resetButton.visible = !renderTransparentBackground;
        }
    }

    public static boolean isKeyToggled() {
        return keyToggled;
    }

    public static void setKeyToggled(boolean keyToggled) {
        GUIViewState.keyToggled = keyToggled;
    }

    public static void reset(int leftPos, int topPos) {
        setChanged(false);
        x = leftPos;
        y = topPos;
        renderTransparentBackground = true;
        GUIViewState.resetButton.visible = false;
    }


    public static boolean isDefault() {
        return !changedPos && renderTransparentBackground;
    }

    public static boolean isChangedPos() {
        return changedPos;
    }

    public static void updateButtonPos(Integer defaultX, Integer defaultY) {
        resetButton.setX(defaultX);
        resetButton.setY(defaultY - 20);
    }
}
