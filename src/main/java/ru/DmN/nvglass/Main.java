package ru.DmN.nvglass;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class Main implements ModInitializer, ClientModInitializer {
    private static final Identifier pid = new Identifier("modid", "packet");
    private static KeyBinding bind;

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("nvglass", "glasses"), new NightVisionGlasses(ArmorMaterials.LEATHER, new Item.Settings().group(ItemGroup.COMBAT)));
        ServerPlayNetworking.registerGlobalReceiver(pid, (server, player, handler, buf, responseSender) -> {
            var stack = player.getInventory().getArmorStack(3);
            if (stack.getItem() instanceof NightVisionGlasses) {
                var nbt = stack.getOrCreateNbt();
                nbt.putBoolean("enable", !nbt.getBoolean("enable"));
            }
        });
    }

    @Override
    public void onInitializeClient() {
        bind = KeyBindingHelper.registerKeyBinding(new KeyBinding("modid.bind", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "modid.category"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (bind.wasPressed()) {
                ClientPlayNetworking.send(pid, PacketByteBufs.empty());
            }
        });
    }
}
