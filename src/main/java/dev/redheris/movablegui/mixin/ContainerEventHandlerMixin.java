package dev.redheris.movablegui.mixin;

import dev.redheris.movablegui.client.MovableGUIModClient;
import dev.redheris.movablegui.state.GUIViewState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
interface ContainerEventHandlerMixin {
    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void drag(MouseButtonEvent mouseButtonEvent, double x, double y, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ContainerScreen screen) {
            ScreenAccessor acc = (ScreenAccessor) screen;
            double mouseX = mouseButtonEvent.x();
            double mouseY = mouseButtonEvent.y();
            if (mouseButtonEvent.hasAltDown() && mouseButtonEvent.button() == 0) {
                GUIViewState.setX((int) mouseX);
                GUIViewState.setY((int) mouseY);
                acc.movablegui$setLeftPos(GUIViewState.getX());
                acc.movablegui$setTopPos(GUIViewState.getY());
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void limitDarkeningBackground(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof ContainerScreen) {
            int key = KeyBindingHelper.getBoundKeyOf(MovableGUIModClient.toggleBackground).getValue();
            if (!GUIViewState.isKeyToggled() && keyEvent.hasShiftDown() && keyEvent.key() == key) {
                GUIViewState.setKeyToggled(true);
                GUIViewState.toggleTransparentBackground();
            }
        }
    }

    @Inject(method = "keyReleased", at = @At("HEAD"))
    private void resetKeyToggled(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        GUIViewState.setKeyToggled(false);
    }
}
