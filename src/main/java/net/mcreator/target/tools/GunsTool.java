package net.mcreator.target.tools;

import net.mcreator.target.TargetMod;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunsTool {

    // TODO 通过配置json的方式来初始化枪械
    public static void initGun(Level level, ItemStack stack, String location) {
        if (level.getServer() != null) {
            return;
        }

        var manager = level.getServer().getResourceManager();

        ResourceLocation resourceLocation = new ResourceLocation(TargetMod.MODID, "guns/" + location);
        manager.getResource(resourceLocation).ifPresent(
                resource -> {

                }
        );



    }

    public static void pvpModeCheck(ItemStack stack, Level level) {
        if (!TargetModVariables.MapVariables.get(level).pvpmode) {
            if (stack.getOrCreateTag().getDouble("level") >= 10) {
                stack.getOrCreateTag().putDouble("damageadd", 1 + 0.05 * (stack.getOrCreateTag().getDouble("level") - 10));
            } else {
                stack.getOrCreateTag().putDouble("damageadd", 1);
            }
        } else {
            stack.getOrCreateTag().putDouble("damageadd", 1);
        }
    }
}
