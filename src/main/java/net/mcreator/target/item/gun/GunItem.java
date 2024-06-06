package net.mcreator.target.item.gun;

import net.mcreator.target.TargetMod;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.tools.GunsTool;
import net.mcreator.target.tools.ItemNBTTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
        Item mainHandItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem();
        CompoundTag tag = itemstack.getOrCreateTag();

        if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
            GunsTool.initGun(level, itemstack, this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf('.') + 1));
            GunsTool.genUUID(itemstack);
            ItemNBTTool.setBoolean(itemstack, "init", true);
        }
        GunsTool.pvpModeCheck(itemstack, level);

        if (tag.getBoolean("draw")) {
            tag.putBoolean("draw", false);
            tag.putInt("draw_time", 0);
            entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.zooming = false;
                capability.syncPlayerVariables(entity);
            });

            if (entity instanceof Player player) {
                double weight = tag.getDouble("weight");

                if (weight == 0) {
                    player.getCooldowns().addCooldown(itemstack.getItem(), 12);
                } else if (weight == 1) {
                    player.getCooldowns().addCooldown(itemstack.getItem(), 17);
                } else if (weight == 2) {
                    player.getCooldowns().addCooldown(itemstack.getItem(), 30);
                }
            }

            if (itemstack.getItem() == TargetModItems.RPG.get() && tag.getInt("ammo") == 0) {
                tag.putDouble("empty", 1);
            }
            if (itemstack.getItem() == TargetModItems.SKS.get() && tag.getInt("ammo") == 0) {
                tag.putDouble("HoldOpen", 1);
            }
            if (itemstack.getItem() == TargetModItems.M_60.get() && tag.getInt("ammo") <= 5) {
                tag.putDouble("empty", 1);
            }
        }

        if (mainHandItem == itemstack.getItem()) {
            if (tag.getInt("draw_time") < 50) {
                tag.putInt("draw_time", (tag.getInt("draw_time") + 1));
            }
        }
        if (tag.getInt("fire_animation") > 0) {
            tag.putInt("fire_animation", (tag.getInt("fire_animation") - 1));
        }
        if (tag.getDouble("flash_time") > 0) {
            tag.putDouble("flash_time", (tag.getDouble("flash_time") - 1));
        }
    }

    public Set<SoundEvent> getReloadSound() {
        return Set.of();
    }

    public ResourceLocation getGunIcon() {
        return new ResourceLocation(TargetMod.MODID, "textures/gun_icon/default_icon.png");
    }

    public String getGunDisplayName() {
        return "";
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("beast");
    }

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        if (event.getItem().getItem().is(TargetModTags.Items.GUN)) {
            event.getItem().getItem().getOrCreateTag().putBoolean("draw", true);
        }
    }
}
