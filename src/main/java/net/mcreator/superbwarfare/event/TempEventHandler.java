package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.item.PerkItem;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// TODO 移除此类，功能移动到枪械重铸台
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempEventHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() instanceof PerkItem perkItem && left.is(ModTags.Items.GUN)) {
            ItemStack output = left.copy();

            int level = PerkHelper.getItemPerkLevel(perkItem.getPerk(), output);
            PerkHelper.setPerk(output, perkItem.getPerk(), level + 1);

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(1);
        }

        if (right.is(ModTags.Items.GUN) && left.getItem() instanceof PerkItem perkItem) {
            ItemStack output = right.copy();

            PerkHelper.removePerkByType(output, perkItem.getPerk().type);

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(0);
        }
    }
}
