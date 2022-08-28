package ru.DmN.nvglass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Optional;

public class NightVisionGlasses extends ArmorItem {
    public NightVisionGlasses(ArmorMaterial material, Item.Settings settings) {
        super(material, EquipmentSlot.HEAD, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (slot == EquipmentSlot.HEAD.getEntitySlotId() && entity instanceof ServerPlayerEntity player && stack.hasNbt() && stack.getNbt().getBoolean("enable")) {
            var i = world.getLightLevel(LightType.SKY, entity.getBlockPos()) - world.getAmbientDarkness();
            var j = world.getLightLevel(LightType.BLOCK, entity.getBlockPos());
            player.addStatusEffect(new StatusEffectInstance(i < 9 && j < 8 ? StatusEffects.NIGHT_VISION : StatusEffects.BLINDNESS, 21, 0, false, false, false, null, Optional.empty()));
        }
    }
}
