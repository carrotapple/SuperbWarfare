package net.mcreator.superbwarfare.item.gun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.tooltip.component.GunImageComponent;
import net.mcreator.superbwarfare.init.ModPerks;
import net.mcreator.superbwarfare.init.ModTags;
import net.mcreator.superbwarfare.network.ModVariables;
import net.mcreator.superbwarfare.perk.Perk;
import net.mcreator.superbwarfare.perk.PerkHelper;
import net.mcreator.superbwarfare.tools.GunsTool;
import net.mcreator.superbwarfare.tools.ItemNBTTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        if (entity instanceof LivingEntity) {
            if (!itemstack.is(ModTags.Items.GUN)) {
                return;
            }

            if (!ItemNBTTool.getBoolean(itemstack, "init", false)) {
                GunsTool.initGun(level, itemstack, this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf('.') + 1));
                GunsTool.genUUID(itemstack);
                ItemNBTTool.setBoolean(itemstack, "init", true);
            }

            if (itemstack.getOrCreateTag().getBoolean("draw")) {
                itemstack.getOrCreateTag().putBoolean("draw", false);
            }

            handleGunPerks(itemstack);
            handleGunAttachment(itemstack);

            if ((itemstack.is(ModTags.Items.EXTRA_ONE_AMMO) && itemstack.getOrCreateTag().getInt("ammo") > itemstack.getOrCreateTag().getInt("mag") + itemstack.getOrCreateTag().getInt("customMag") + 1)
                    || (!itemstack.is(ModTags.Items.EXTRA_ONE_AMMO) && itemstack.getOrCreateTag().getInt("ammo") > itemstack.getOrCreateTag().getInt("mag") + itemstack.getOrCreateTag().getInt("customMag"))
            ) {
                int count = itemstack.getOrCreateTag().getInt("ammo") - itemstack.getOrCreateTag().getInt("mag") + itemstack.getOrCreateTag().getInt("customMag") - (itemstack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0);
                entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

                    if (itemstack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                        capability.shotgunAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).shotgunAmmo + count;
                    } else if (itemstack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                        capability.sniperAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).sniperAmmo + count;
                    } else if (itemstack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                        capability.handgunAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).handgunAmmo + count;
                    } else if (itemstack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                        capability.rifleAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).rifleAmmo + count;
                    }
                    capability.syncPlayerVariables(entity);
                });

                itemstack.getOrCreateTag().putInt("ammo", itemstack.getOrCreateTag().getInt("mag") + itemstack.getOrCreateTag().getInt("customMag") + (itemstack.is(ModTags.Items.EXTRA_ONE_AMMO) ? 1 : 0));
            }
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        UUID uuid = new UUID(slot.toString().hashCode(), 0);
        if (slot == EquipmentSlot.MAINHAND) {
            map = HashMultimap.create(map);
            map.put(Attributes.MOVEMENT_SPEED,
                    new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER,
                            -0.01f - 0.005f * (stack.getOrCreateTag().getDouble("weight") + stack.getOrCreateTag().getDouble("CustomWeight")),
                            AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return map;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        return Optional.of(new GunImageComponent(pStack));
    }

    public Set<SoundEvent> getReloadSound() {
        return Set.of();
    }

    public ResourceLocation getGunIcon() {
        return ModUtils.loc("textures/gun_icon/default_icon.png");
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
            event.getItem().getItem().getOrCreateTag().putBoolean("init", false);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    private void handleGunPerks(ItemStack stack) {
        reducePerkTagTime(stack, "HealClipTime", "KillClipReloadTime", "KillClipTime", "FourthTimesCharmTick", "HeadSeeker",
                "DesperadoTime", "DesperadoTimePost");

        if (PerkHelper.getItemPerkLevel(ModPerks.FOURTH_TIMES_CHARM.get(), stack) > 0) {
            int count = GunsTool.getPerkIntTag(stack, "FourthTimesCharmCount");
            if (count >= 4) {
                GunsTool.setPerkIntTag(stack, "FourthTimesCharmTick", 0);
                GunsTool.setPerkIntTag(stack, "FourthTimesCharmCount", 0);

                int mag = stack.getOrCreateTag().getInt("mag") + stack.getOrCreateTag().getInt("customMag");
                stack.getOrCreateTag().putInt("ammo", Math.min(mag, stack.getOrCreateTag().getInt("ammo") + 2));
            }
        }
    }

    private void handleGunAttachment(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("Attachments");

        double scopeWeight = switch (tag.getInt("Scope")) {
            case 1 -> 0.5;
            case 2 -> 1;
            case 3 -> 1.5;
            default -> 0;
        };

        double barrelWeight = switch (tag.getInt("Barrel")) {
            case 1 -> 0.5;
            case 2 -> 1;
            default -> 0;
        };

        double magazineWeight = switch (tag.getInt("Magazine")) {
            case 1 -> 1;
            case 2 -> 2;
            default -> 0;
        };

        double stockWeight = switch (tag.getInt("Stock")) {
            case 1 -> -2;
            case 2 -> 1.5;
            default -> 0;
        };

        double gripWeight = switch (tag.getInt("Grip")) {
            case 1, 2 -> 0.25;
            case 3 -> 1;
            default -> 0;
        };

        double soundRadius = tag.getInt("Barrel") == 2 ? 0.6 : 1;

        stack.getOrCreateTag().putDouble("CustomWeight", scopeWeight + barrelWeight + magazineWeight + stockWeight + gripWeight);
        stack.getOrCreateTag().putDouble("CustomSoundRadius", soundRadius);
    }

    public boolean canApplyPerk(Perk perk) {
        return true;
    }

    private void reducePerkTagTime(ItemStack stack, String... tag) {
        if (!stack.hasTag() || stack.getTag() == null) {
            return;
        }

        var compound = stack.getOrCreateTag().getCompound("PerkData");
        for (String t : tag) {
            if (!compound.contains(t)) {
                continue;
            }

            if (compound.getInt(t) > 0) {
                compound.putInt(t, Math.max(0, compound.getInt(t) - 1));
            }
        }
        stack.addTagElement("PerkData", compound);
    }
}
