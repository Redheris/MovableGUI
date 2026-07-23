package dev.redheris.movablegui.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface ScreenAccessor {
    @Accessor("topPos")
    void movablegui$setTopPos(int topPos);

    @Accessor("leftPos")
    void movablegui$setLeftPos(int leftPos);

    @Accessor("hoveredSlot")
    Slot getHoveredSlot();
}
