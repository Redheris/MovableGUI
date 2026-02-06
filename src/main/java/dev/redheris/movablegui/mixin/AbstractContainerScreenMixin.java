package dev.redheris.movablegui.mixin;

import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    @Unique
    private Integer defaultX;
    @Unique
    private Integer defaultY;

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initPosition(CallbackInfo ci) {
        if ((Object) this instanceof ContainerScreen screen) {
            defaultX = leftPos;
            defaultY = topPos;

            GUIViewState.resetButton = Button.builder(
                    Component.literal("Reset"),
                    btn -> {
                        GUIViewState.reset(defaultX, defaultY);
                        ScreenAccessor acc = (ScreenAccessor) this;
                        acc.movablegui$setLeftPos(defaultX);
                        acc.movablegui$setTopPos(defaultY);
                        GUIViewState.updateButtonPos(defaultX, defaultY);
                    }
            ).size(40, 20).pos(10, 10).build();

            // Init position
            if (GUIViewState.isChangedPos()) {
                leftPos = GUIViewState.getX();
                topPos = GUIViewState.getY();
                GUIViewState.updateButtonPos(leftPos, topPos);
            } else {
                GUIViewState.updateButtonPos(defaultX, defaultY);
            }

            // Add reset button
            this.addRenderableWidget(GUIViewState.resetButton);
            GUIViewState.resetButton.visible = !GUIViewState.isDefault();
            // Move slots
            if (GUIViewState.isChangedPos()) {
                ScreenAccessor acc = (ScreenAccessor) screen;
                acc.movablegui$setLeftPos(GUIViewState.getX());
                acc.movablegui$setTopPos(GUIViewState.getY());
            }
        }
    }
}
