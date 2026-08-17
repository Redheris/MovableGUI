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
            defaultX = value;
            if (GUIViewState.isChangedPos()) {
                original.call(instance, GUIViewState.getX(width));
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
            defaultY = value;
            original.call(instance, GUIViewState.isChangedPos() ? GUIViewState.getY(height) : value);
        } else {
            original.call(instance, value);
        }
    }

    @WrapMethod(method = "init")
    private void initResetButton(Operation<Void> original) {
        if ((Object) this instanceof ContainerScreen) {
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
        }

        original.call();

        if ((Object) this instanceof ContainerScreen) {
            // Add reset button
            this.addRenderableWidget(GUIViewState.resetButton);
            GUIViewState.resetButton.visible = !GUIViewState.isDefault();
            // Move slots
            if (GUIViewState.isChangedPos()) {
                ScreenAccessor acc = (ScreenAccessor) this;
                acc.movablegui$setLeftPos(GUIViewState.getX(width));
                acc.movablegui$setTopPos(GUIViewState.getY(height));
            }
            GUIViewState.updateButtonPos(leftPos, topPos);
        }
    }
}
