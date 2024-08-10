package net.mcreator.superbwarfare.item.gun;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.EnchantmentCategoryTool;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.mcreator.superbwarfare.tools.TooltipTool;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
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
        if (entity instanceof LivingEntity living) {
            ItemStack mainHandItem = living.getMainHandItem();
            if (!itemstack.is(ModTags.Items.GUN)) {
                return;
            }

            if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
                GunsTool.initGun(level, itemstack, this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf('.') + 1));
                GunsTool.genUUID(itemstack);
                ItemNBTTool.setBoolean(itemstack, "init", true);
            }
            GunsTool.pvpModeCheck(itemstack, level);

            if (itemstack.getOrCreateTag().getBoolean("draw")) {
                itemstack.getOrCreateTag().putBoolean("draw", false);
                itemstack.getOrCreateTag().putInt("draw_time", 0);
                entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.zooming = false;
                    capability.syncPlayerVariables(entity);
                });

                if (itemstack.getItem() == ModItems.RPG.get() && itemstack.getOrCreateTag().getInt("ammo") == 0) {
                    itemstack.getOrCreateTag().putDouble("empty", 1);
                }
                if (itemstack.getItem() == ModItems.SKS.get() && itemstack.getOrCreateTag().getInt("ammo") == 0) {
                    itemstack.getOrCreateTag().putBoolean("HoldOpen", true);
                }
                if (itemstack.getItem() == ModItems.M_60.get() && itemstack.getOrCreateTag().getInt("ammo") <= 5) {
                    itemstack.getOrCreateTag().putBoolean("bullet_chain", true);
                }
            }

            if (mainHandItem.getItem() == itemstack.getItem()) {
                if (itemstack.getOrCreateTag().getInt("draw_time") < 50) {
                    itemstack.getOrCreateTag().putInt("draw_time", (itemstack.getOrCreateTag().getInt("draw_time") + 1));
                }
            }

            if (itemstack.getOrCreateTag().getInt("fire_animation") > 0) {
                itemstack.getOrCreateTag().putInt("fire_animation", (itemstack.getOrCreateTag().getInt("fire_animation") - 1));
            }

            if (itemstack.getOrCreateTag().getDouble("flash_time") > 0) {
                itemstack.getOrCreateTag().putDouble("flash_time", (itemstack.getOrCreateTag().getDouble("flash_time") - 1));
            }

            handleGunPerks(itemstack);
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        TooltipTool.addGunTips(list, stack);
    }

    public Set<SoundEvent> getReloadSound() {
        return Set.of();
    }

    public ResourceLocation getGunIcon() {
        return new ResourceLocation(ModUtils.MODID, "textures/gun_icon/default_icon.png");
    }

    public String getGunDisplayName() {
        return "";
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        if (event.getItem().getItem().is(ModTags.Items.GUN)) {
            event.getItem().getItem().getOrCreateTag().putBoolean("draw", true);
        }
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategoryTool.GUN;
    }

    private void handleGunPerks(ItemStack stack) {
        if (stack.getOrCreateTag().getInt("HealClipTime") > 0) {
            stack.getOrCreateTag().putInt("HealClipTime", Math.max(0, stack.getOrCreateTag().getInt("HealClipTime") - 1));
        }

        if (stack.getOrCreateTag().getInt("KillClipReloadTime") > 0) {
            stack.getOrCreateTag().putInt("KillClipReloadTime", Math.max(0, stack.getOrCreateTag().getInt("KillClipReloadTime") - 1));
        }

        if (stack.getOrCreateTag().getInt("KillClipTime") > 0) {
            stack.getOrCreateTag().putInt("KillClipTime", Math.max(0, stack.getOrCreateTag().getInt("KillClipTime") - 1));
        }

        if (stack.getOrCreateTag().getInt("FourthTimesCharmTick") > 0) {
            stack.getOrCreateTag().putInt("FourthTimesCharmTick",
                    Math.max(0, stack.getOrCreateTag().getInt("FourthTimesCharmTick") - 1));
        }

        if (PerkHelper.getItemPerkLevel(ModPerks.FOURTH_TIMES_CHARM.get(), stack) > 0) {
            int count = stack.getOrCreateTag().getInt("FourthTimesCharmCount");
            if (count >= 4) {
                stack.getOrCreateTag().putInt("FourthTimesCharmTick", 0);
                stack.getOrCreateTag().putInt("FourthTimesCharmCount", 0);

                int mag = stack.getOrCreateTag().getInt("mag");
                stack.getOrCreateTag().putInt("ammo", Math.min(mag, stack.getOrCreateTag().getInt("ammo") + 2));
            }
        }

        if (stack.getOrCreateTag().getInt("HeadSeeker") > 0) {
            stack.getOrCreateTag().putInt("HeadSeeker", Math.max(0, stack.getOrCreateTag().getInt("HeadSeeker") - 1));
        }
    }

    public boolean canApplyPerk(ItemStack stack, Perk perk, Perk.Type slot) {
        return perk.type == slot;
    }
}
