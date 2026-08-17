package dev.redheris.movablegui.state;

import net.minecraft.client.gui.components.Button;

public class GUIViewState {
    public static Button resetButton;
    private static boolean changedPos = false;
    private static int x = 0;
    private static int y = 0;
    private static boolean renderTransparentBackground = true;
    private static boolean backgroundKeyToggled = false;
    private static boolean animationCompleted = true;

    private static void setChanged(boolean changed) {
        GUIViewState.changedPos = changed;
        if (resetButton != null)
            resetButton.visible = changed;
    }

    public static int getX(int width) {
        return Math.clamp(x, 0, width - 20);
    }

    public static void setX(int x) {
        setChanged(true);
        GUIViewState.x = x;
        resetButton.setX(x);
    }

    public static int getY(int height) {
        return Math.clamp(y, 0, height - 20);
    }

    public static void setY(int y) {
        setChanged(true);
        GUIViewState.y = y;
        resetButton.setY(y - 20);
    }

    public static boolean isAnimationCompleted() {
        return animationCompleted;
    }

    public static void setAnimationCompleted(boolean animationCompleted) {
        GUIViewState.animationCompleted = animationCompleted;
    }

    public static boolean doRenderTransparentBackground() {
        return renderTransparentBackground;
    }

    public static void toggleTransparentBackground() {
        renderTransparentBackground = !renderTransparentBackground;
        if (!changedPos) {
            resetButton.visible = !renderTransparentBackground;
        }
        animationCompleted = false;
    }

    public static boolean isBackgroundKeyToggled() {
        return backgroundKeyToggled;
    }

    public static void setBackgroundKeyToggled(boolean backgroundKeyToggled) {
        GUIViewState.backgroundKeyToggled = backgroundKeyToggled;
    }

    public static void reset(int leftPos, int topPos) {
        setChanged(false);
        x = leftPos;
        y = topPos;
        renderTransparentBackground = true;
        GUIViewState.resetButton.visible = false;
        animationCompleted = false;
    }


    public static boolean isDefault() {
        return !changedPos && renderTransparentBackground;
    }

    public static boolean isChangedPos() {
        return changedPos;
    }

    public static void updateButtonPos(Integer containerX, Integer containerY) {
        resetButton.setX(containerX);
        resetButton.setY(containerY - 20);
    }
}
