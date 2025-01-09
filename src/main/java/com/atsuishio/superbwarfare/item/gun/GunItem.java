package com.atsuishio.superbwarfare.item.gun;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent;
import com.atsuishio.superbwarfare.init.ModPerks;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkHelper;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.ItemNBTTool;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity) {
            if (!stack.is(ModTags.Items.GUN)) {
                return;
            }

            if (!(stack.getItem() instanceof GunItem gunItem)) return;

            if (!ItemNBTTool.getBoolean(stack, "init", false)) {
                GunsTool.initGun(level, stack, this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf('.') + 1));
                GunsTool.generateAndSetUUID(stack);
                ItemNBTTool.setBoolean(stack, "init", true);
            }

            if (stack.getOrCreateTag().getBoolean("draw")) {
                stack.getOrCreateTag().putBoolean("draw", false);
            }

            handleGunPerks(stack);
            handleGunAttachment(stack);

            if ((gunItem.bulletInBarrel(stack) && GunsTool.getGunIntTag(stack, "Ammo", 0) >
                    GunsTool.getGunIntTag(stack, "Magazine", 0) + GunsTool.getGunIntTag(stack, "CustomMagazine", 0) + 1)
                    || (!gunItem.bulletInBarrel(stack) && GunsTool.getGunIntTag(stack, "Ammo", 0) >
                    GunsTool.getGunIntTag(stack, "Magazine", 0) + GunsTool.getGunIntTag(stack, "CustomMagazine", 0))
            ) {
                int count = GunsTool.getGunIntTag(stack, "Ammo", 0) - GunsTool.getGunIntTag(stack, "Magazine", 0)
                        + GunsTool.getGunIntTag(stack, "CustomMagazine", 0) - (gunItem.bulletInBarrel(stack) ? 1 : 0);
                entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

                    if (stack.is(ModTags.Items.USE_SHOTGUN_AMMO)) {
                        capability.shotgunAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).shotgunAmmo + count;
                    } else if (stack.is(ModTags.Items.USE_SNIPER_AMMO)) {
                        capability.sniperAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).sniperAmmo + count;
                    } else if (stack.is(ModTags.Items.USE_HANDGUN_AMMO)) {
                        capability.handgunAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).handgunAmmo + count;
                    } else if (stack.is(ModTags.Items.USE_RIFLE_AMMO)) {
                        capability.rifleAmmo = entity.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ModVariables.PlayerVariables()).rifleAmmo + count;
                    }
                    capability.syncPlayerVariables(entity);
                });

                GunsTool.setGunIntTag(stack, "Ammo", GunsTool.getGunIntTag(stack, "Magazine", 0)
                        + GunsTool.getGunIntTag(stack, "CustomMagazine", 0) + (gunItem.bulletInBarrel(stack) ? 1 : 0));
            }
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
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
                            -0.01f - 0.005f * (GunsTool.getGunDoubleTag(stack, "Weight") + GunsTool.getGunDoubleTag(stack, "CustomWeight")),
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

                int mag = GunsTool.getGunIntTag(stack, "Magazine", 0) + GunsTool.getGunIntTag(stack, "CustomMagazine", 0);
                GunsTool.setGunIntTag(stack, "Ammo", Math.min(mag, GunsTool.getGunIntTag(stack, "Ammo", 0) + 2));
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

        GunsTool.setGunDoubleTag(stack, "CustomWeight", scopeWeight + barrelWeight + magazineWeight + stockWeight + gripWeight);
        GunsTool.setGunDoubleTag(stack, "CustomSoundRadius", soundRadius);
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

    public boolean isMagazineReload(ItemStack stack) {
        return false;
    }

    public boolean isClipReload(ItemStack stack) {
        return false;
    }

    public boolean isIterativeReload(ItemStack stack) {
        return false;
    }

    public boolean isOpenBolt(ItemStack stack) {
        return false;
    }

    public boolean bulletInBarrel(ItemStack stack) {
        return false;
    }

    public boolean autoWeapon(ItemStack stack) {
        return false;
    }

    public boolean useBackpackAmmo(ItemStack stack) {
        return false;
    }

    public boolean canCustom(ItemStack stack) {
        return false;
    }

    public boolean canCustomBarrel(ItemStack stack) {
        return false;
    }

    public boolean canCustomGrip(ItemStack stack) {
        return false;
    }

    public boolean canCustomMagazine(ItemStack stack) {
        return false;
    }

    public boolean canCustomScope(ItemStack stack) {
        return false;
    }

    public boolean canCustomStock(ItemStack stack) {
        return false;
    }

    public boolean ejectShell(ItemStack stack) {
        return false;
    }

    public int getFireMode() {
        return 0;
    }

    public enum FireMode {
        SEMI(1),
        BURST(2),
        AUTO(4);

        public final int flag;

        FireMode(int i) {
            this.flag = i;
        }
    }
}
