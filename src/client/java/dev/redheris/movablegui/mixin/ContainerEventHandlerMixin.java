package dev.redheris.movablegui.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.redheris.movablegui.MovableGUIClient;
import dev.redheris.movablegui.state.GUIViewState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
interface ContainerEventHandlerMixin {
    @WrapMethod(method = "mouseDragged")
    private boolean drag(MouseButtonEvent event, double mouseX, double mouseY, Operation<Boolean> original) {
        if (updatePos(event)) {
            return true;
        }
        return original.call(event, mouseX, mouseY);
    }

    @WrapMethod(method = "mouseClicked")
    private boolean click(MouseButtonEvent event, boolean isDoubleClick, Operation<Boolean> original) {
        if (updatePos(event)) {
            return true;
        }
        return original.call(event, isDoubleClick);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void limitDarkeningBackground(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof ContainerScreen) {
            GUIViewState guiViewState = GUIViewState.getInstance();
            int key = KeyBindingHelper.getBoundKeyOf(MovableGUIClient.toggleBackground).getValue();
            if (!guiViewState.isBackgroundKeyToggled() && keyEvent.hasShiftDown() && keyEvent.key() == key) {
                guiViewState.setBackgroundKeyToggled(true);
                guiViewState.toggleTransparentBackground();
            }
        }
    }

    @Inject(method = "keyReleased", at = @At("HEAD"))
    private void resetKeyToggled(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        GUIViewState.getInstance().setBackgroundKeyToggled(false);
    }

    @Unique
    private boolean updatePos(MouseButtonEvent btn) {
        if ((Object) this instanceof ContainerScreen screen) {
            ScreenAccessor acc = (ScreenAccessor) screen;
            double mouseX = btn.x();
            double mouseY = btn.y();

            if (acc.movablegui$getHoveredSlot() == null && btn.modifiers() == 4 && btn.button() == 0) {
                GUIViewState guiViewState = GUIViewState.getInstance();
                if (mouseX >= 0 && mouseX <= screen.width - 20) {
                    guiViewState.setX((int) mouseX);
                    acc.movablegui$setLeftPos(guiViewState.getX(screen.width));
                }
                if (mouseY >= 0 && mouseY <= screen.height - 20) {
                    guiViewState.setY((int) mouseY);
                    acc.movablegui$setTopPos(guiViewState.getY(screen.height));
                }
                return true;
            }
        }
        return false;
    }
}
