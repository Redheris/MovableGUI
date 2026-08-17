package dev.redheris.movablegui.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.util.ARGB;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Unique
    private int originalAlphaFrom = -1;
    @Unique
    private int originalAlphaTo = -1;
    @Unique
    private int newAlphaFrom;
    @Unique
    private int newAlphaTo;

    @WrapOperation(method = "renderTransparentBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"
            )
    )
    private void fadeDarkeningBackground(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int colorFrom, int colorTo, Operation<Void> original) {
        if ((Object) this instanceof ContainerScreen ) {
            GUIViewState guiViewState = GUIViewState.getInstance();

            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_ALT)) {
                instance.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (originalAlphaFrom == -1 || originalAlphaTo == -1) {
                originalAlphaFrom = newAlphaFrom = ARGB.alpha(colorFrom);
                originalAlphaTo = newAlphaTo = ARGB.alpha(colorTo);
            }
            if (!guiViewState.isAnimationCompleted()) {
                int step = 5;
                if (guiViewState.doRenderTransparentBackground()) {
                    newAlphaFrom = Math.min(newAlphaFrom + step, originalAlphaFrom);
                    newAlphaTo = Math.min(newAlphaTo + step, originalAlphaTo);
                    if (newAlphaFrom == originalAlphaFrom && newAlphaTo == originalAlphaTo) {
                        guiViewState.setAnimationCompleted(true);
                    }
                } else {
                    newAlphaFrom = Math.max(newAlphaFrom - step, 0);
                    newAlphaTo = Math.max(newAlphaTo - step, 0);
                    if (newAlphaFrom == 0 && newAlphaTo == 0) {
                        guiViewState.setAnimationCompleted(true);
                    }
                }
            } else if (!guiViewState.doRenderTransparentBackground()) {
                newAlphaFrom = 0;
                newAlphaTo = 0;
            }
            colorFrom = ARGB.color(newAlphaFrom, colorFrom);
            colorTo = ARGB.color(newAlphaTo, colorTo);
        }
        original.call(instance, minX, minY, maxX, maxY, colorFrom, colorTo);
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void finishAnimation(CallbackInfo ci) {
        GUIViewState.getInstance().setAnimationCompleted(true);
    }
}
