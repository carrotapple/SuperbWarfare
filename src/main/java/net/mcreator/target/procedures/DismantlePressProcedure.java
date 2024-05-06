package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Map;
import java.util.function.Supplier;

public class DismantlePressProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        ItemStack stack;
        stack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY);
        if (stack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
            if (stack.getRarity() == Rarity.COMMON) {
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(Items.IRON_INGOT);
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (stack.getRarity() == Rarity.RARE) {
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(TargetModItems.INGOT_STEEL.get());
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (stack.getRarity() == Rarity.EPIC) {
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(TargetModItems.CEMENTED_CARBIDE_INGOT.get());
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (stack.is(ItemTags.create(new ResourceLocation("target:legendary_gun")))) {
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(Items.NETHERITE_INGOT);
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (stack.is(ItemTags.create(new ResourceLocation("target:special_gun")))) {
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_INGOT.get());
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (stack.getOrCreateTag().getDouble("level") >= 10) {
                if (Math.random() < 0.005 * stack.getOrCreateTag().getDouble("level")) {
                    if (entity instanceof Player _player) {
                        ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get());
                        _setstack.setCount(3);
                        ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                    }
                }
                if (Math.random() < 0.01 * stack.getOrCreateTag().getDouble("level")) {
                    if (entity instanceof Player _player) {
                        ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get());
                        _setstack.setCount(2);
                        ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                    }
                }
                if (Math.random() < 0.03 * stack.getOrCreateTag().getDouble("level")) {
                    if (entity instanceof Player _player) {
                        ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get());
                        _setstack.setCount(1);
                        ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                    }
                }
                if (Math.random() < 0.06 * stack.getOrCreateTag().getDouble("level")) {
                    if (entity instanceof Player _player) {
                        ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get());
                        _setstack.setCount(1);
                        ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                    }
                }
                if (entity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get());
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
            }
            if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
                ((Slot) _slots.get(0)).set(ItemStack.EMPTY);
                _player.containerMenu.broadcastChanges();
            }
        }
    }
}
