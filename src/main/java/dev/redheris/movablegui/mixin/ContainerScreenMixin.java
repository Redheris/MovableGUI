package dev.redheris.movablegui.mixin;

import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ContainerScreen.class)
abstract class ContainerScreenMixin {
    @ModifyVariable(method = "renderBg", at = @At(value = "STORE"), index = 5)
    private int updateX(int k) {
        return GUIViewState.isChangedPos() ? GUIViewState.getX() : k;
    }

    @ModifyVariable(method = "renderBg", at = @At(value = "STORE"), index = 6)
    private int updateY(int l) {
        return GUIViewState.isChangedPos() ? GUIViewState.getY() : l;
    }
}
