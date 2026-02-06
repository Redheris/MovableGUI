package dev.redheris.movablegui.mixin;

import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "renderTransparentBackground", at = @At("HEAD"), cancellable = true)
    private void limitDarkeningBackground(GuiGraphics guiGraphics, CallbackInfo ci) {
        if ((Object) this instanceof ContainerScreen && !GUIViewState.doRenderTransparentBackground())
            ci.cancel();
    }
}
