package net.mcreator.target.tools;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunsTool {

    // TODO 通过配置/json/枚举类的方式来初始化枪械
    public static void initGun(ItemStack stack) {


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
