package com.atsuishio.superbwarfare.tools;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

public class InventoryTool {

    /**
     * 计算物品列表内指定物品的数量
     *
     * @param item 物品类型
     * @return 物品数量
     */
    public static int countItem(NonNullList<ItemStack> itemList, @NotNull Item item) {
        return itemList.stream()
                .filter(stack -> stack.is(item))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    /**
     * 消耗物品列表内指定物品
     *
     * @param item  物品类型
     * @param count 要消耗的数量
     * @return 成功消耗的物品数量
     */
    public static int consumeItem(NonNullList<ItemStack> itemList, Item item, int count) {
        int initialCount = count;
        var items = itemList.stream().filter(stack -> stack.is(item)).toList();
        for (var stack : items) {
            var countToShrink = Math.min(stack.getCount(), count);
            stack.shrink(countToShrink);
            count -= countToShrink;
            if (count <= 0) break;
        }
        return initialCount - count;
    }

    /**
     * 尝试插入指定物品指定数量
     *
     * @param item  物品类型
     * @param count 要插入的数量
     * @return 未能成功插入的物品数量
     */
    public static int insertItem(NonNullList<ItemStack> itemList, Item item, int count) {
        var defaultStack = new ItemStack(item);
        var maxStackSize = item.getMaxStackSize(defaultStack);

        for (int i = 0; i < itemList.size(); i++) {
            var stack = itemList.get(i);

            if (stack.is(item) && stack.getCount() < maxStackSize) {
                var countToAdd = Math.min(maxStackSize - stack.getCount(), count);
                stack.grow(countToAdd);
                count -= countToAdd;
            } else if (stack.isEmpty()) {
                var countToAdd = Math.min(maxStackSize, count);
                itemList.set(i, new ItemStack(item, countToAdd));
                count -= countToAdd;
            }

            if (count <= 0) break;
        }

        return count;
    }
}
