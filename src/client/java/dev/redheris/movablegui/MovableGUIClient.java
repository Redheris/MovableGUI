package dev.redheris.movablegui;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class MovableGUIClient implements ClientModInitializer {
    public static KeyMapping toggleBackground;

    @Override
    public void onInitializeClient() {
        toggleBackground = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.movablegui.background",
                        InputConstants.KEY_V,
                        KeyMapping.Category.MISC
                ));
    }
}
