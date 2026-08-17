package dev.redheris.movablegui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class MovableGUIClient implements ClientModInitializer {
    public static KeyMapping toggleBackground;

    @Override
    public void onInitializeClient() {
        toggleBackground = KeyBindingHelper.registerKeyBinding(
                new KeyMapping("[MovableGUI] Background: Shift +", GLFW.GLFW_KEY_V, KeyMapping.Category.MISC)
        );
    }
}
