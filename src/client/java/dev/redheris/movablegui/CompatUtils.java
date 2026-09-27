package dev.redheris.movablegui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class CompatUtils {
    public static boolean isKeyDown(int key) {
        //? if >=26.3 {
        /*return InputConstants.isKeyDown(key);
        *///?} else
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
    }
}
