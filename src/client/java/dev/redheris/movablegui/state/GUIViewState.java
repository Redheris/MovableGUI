package dev.redheris.movablegui.state;

import net.minecraft.client.gui.components.Button;

public class GUIViewState {
    private static final GUIViewState INSTANCE = new GUIViewState();

    public Button resetButton;
    private boolean changedPos = false;
    private int x = 0;
    private int y = 0;
    private boolean renderTransparentBackground = true;
    private boolean backgroundKeyToggled = false;
    private boolean animationCompleted = true;

    public static GUIViewState getInstance() {
        return INSTANCE;
    }

    private void setChanged(boolean changed) {
        this.changedPos = changed;
        if (resetButton != null)
            resetButton.visible = changed;
    }

    public int getX(int width) {
        return Math.clamp(x, 0, width - 20);
    }

    public void setX(int x) {
        setChanged(true);
        this.x = x;
        resetButton.setX(x);
    }

    public int getY(int height) {
        return Math.clamp(y, 0, height - 20);
    }

    public void setY(int y) {
        setChanged(true);
        this.y = y;
        resetButton.setY(y - 20);
    }

    public boolean isAnimationCompleted() {
        return animationCompleted;
    }

    public void setAnimationCompleted(boolean animationCompleted) {
        this.animationCompleted = animationCompleted;
    }

    public boolean doRenderTransparentBackground() {
        return renderTransparentBackground;
    }

    public void toggleTransparentBackground() {
        renderTransparentBackground = !renderTransparentBackground;
        if (!changedPos) {
            resetButton.visible = !renderTransparentBackground;
        }
        animationCompleted = false;
    }

    public boolean isBackgroundKeyToggled() {
        return backgroundKeyToggled;
    }

    public void setBackgroundKeyToggled(boolean backgroundKeyToggled) {
        this.backgroundKeyToggled = backgroundKeyToggled;
    }

    public void reset(int leftPos, int topPos) {
        setChanged(false);
        x = leftPos;
        y = topPos;
        renderTransparentBackground = true;
        this.resetButton.visible = false;
        animationCompleted = false;
    }


    public boolean isDefault() {
        return !changedPos && renderTransparentBackground;
    }

    public boolean isChangedPos() {
        return changedPos;
    }

    public void updateButtonPos(Integer containerX, Integer containerY) {
        resetButton.setX(containerX);
        resetButton.setY(containerY - 20);
    }
}
