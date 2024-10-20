package net.mcreator.superbwarfare.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;

public class ItemModelHelper {

    public static void handleGunAttachments(GeoBone bone, ItemStack stack, String name) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("Attachments");

        try {
            if (name.startsWith("Scope")) {
                String[] parts = name.split("(?<=\\D)(?=\\d)");
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    bone.setHidden(tag.getInt("Scope") != index);
                }
            }

            if (name.startsWith("Magazine")) {
                String[] parts = name.split("(?<=\\D)(?=\\d)");
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    bone.setHidden(tag.getInt("Magazine") != index);
                }
            }

            if (name.startsWith("Barrel")) {
                String[] parts = name.split("(?<=\\D)(?=\\d)");
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    bone.setHidden(tag.getInt("Barrel") != index);
                }
            }

            if (name.startsWith("Stock")) {
                String[] parts = name.split("(?<=\\D)(?=\\d)");
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    bone.setHidden(tag.getInt("Stock") != index);
                }
            }
        } catch (NumberFormatException ignored) {
        }
    }

}
