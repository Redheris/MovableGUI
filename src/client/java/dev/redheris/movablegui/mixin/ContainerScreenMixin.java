package dev.redheris.movablegui.mixin;

import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin {
    //~ if >=26.1 'renderBg' -> 'extractBackground'
    @ModifyVariable(method = "renderBg", at = @At(value = "STORE"), index = 5)
    private int updateX(int k) {
        GUIViewState guiViewState = GUIViewState.getInstance();
        Screen screen = (Screen) (Object) this;
        return guiViewState.isChangedPos() ? guiViewState.getX(screen.width) : k;
    }

    //~ if >=26.1 'renderBg' -> 'extractBackground'
    @ModifyVariable(method = "renderBg", at = @At(value = "STORE"), index = 6)
    private int updateY(int l) {
        GUIViewState guiViewState = GUIViewState.getInstance();
        Screen screen = (Screen) (Object) this;
        return guiViewState.isChangedPos() ? guiViewState.getY(screen.height) : l;
    }
}
