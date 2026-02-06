package dev.redheris.movablegui.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class MovableGUIModClient implements ClientModInitializer {
    public static KeyMapping toggleBackground = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("Toggle Translucent Background", GLFW.GLFW_KEY_V, KeyMapping.Category.MISC)
    );

    @Override
    public void onInitializeClient() {

    }
}
