package dev.redheris.movablegui.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.redheris.movablegui.state.GUIViewState;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

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

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;leftPos:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void initLeftPos(AbstractContainerScreen<?> instance, int value, Operation<Void> original) {
        if (instance instanceof ContainerScreen) {
            GUIViewState guiViewState = GUIViewState.getInstance();
            defaultX = value;
            if (guiViewState.isChangedPos()) {
                original.call(instance, guiViewState.getX(width));
            }
        }
        original.call(instance, value);
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;topPos:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void initTopPos(AbstractContainerScreen<?> instance, int value, Operation<Void> original) {
        if (instance instanceof ContainerScreen) {
            GUIViewState guiViewState = GUIViewState.getInstance();
            defaultY = value;
            original.call(instance, guiViewState.isChangedPos() ? guiViewState.getY(height) : value);
        } else {
            original.call(instance, value);
        }
    }

    @WrapMethod(method = "init")
    private void initResetButton(Operation<Void> original) {
        GUIViewState guiViewState = GUIViewState.getInstance();

        if ((Object) this instanceof ContainerScreen) {
            guiViewState.resetButton = Button.builder(
                    Component.translatable("movablegui.gui.reset"),
                    btn -> {
                        guiViewState.reset(defaultX, defaultY);
                        ScreenAccessor acc = (ScreenAccessor) this;
                        acc.movablegui$setLeftPos(defaultX);
                        acc.movablegui$setTopPos(defaultY);
                        guiViewState.updateButtonPos(defaultX, defaultY);
                    }
            ).size(40, 20).pos(10, 10).build();
        }

        original.call();

        if ((Object) this instanceof ContainerScreen) {
            // Add reset button
            this.addRenderableWidget(guiViewState.resetButton);
            guiViewState.resetButton.visible = !guiViewState.isDefault();
            // Move slots
            if (guiViewState.isChangedPos()) {
                ScreenAccessor acc = (ScreenAccessor) this;
                acc.movablegui$setLeftPos(guiViewState.getX(width));
                acc.movablegui$setTopPos(guiViewState.getY(height));
            }
            guiViewState.updateButtonPos(leftPos, topPos);
        }
    }
}
