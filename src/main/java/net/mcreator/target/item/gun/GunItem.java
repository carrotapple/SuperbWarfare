package net.mcreator.target.item.gun;

import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.ItemNBTTool;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber
public abstract class GunItem extends Item {
    public GunItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, level, entity, slot, selected);

        if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
            GunsTool.initGun(level, itemstack, this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf('.') + 1));
            GunsTool.genUUID(itemstack);
            ItemNBTTool.setBoolean(itemstack, "init", true);
        }
        GunsTool.pvpModeCheck(itemstack, level);
    }

    public Set<SoundEvent> getReloadSound() {
        return Set.of();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("beast");
    }

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        if (event.getItem().getItem().is(TargetModTags.Items.GUN)) {
            event.getItem().getItem().getOrCreateTag().putDouble("drawtime", 0);
        }
    }
}
