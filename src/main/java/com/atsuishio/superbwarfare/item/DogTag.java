package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.menu.DogTagEditorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.concurrent.atomic.AtomicBoolean;

public class DogTag extends Item implements ICurioItem {

    private static final Component CONTAINER_TITLE = Component.translatable("container.superbwarfare.dog_tag_editor");

    public DogTag() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        AtomicBoolean flag = new AtomicBoolean(true);
        CuriosApi.getCuriosInventory(livingEntity).ifPresent(c -> c.findFirstCurio(this).ifPresent(s -> flag.set(false)));

        return flag.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.success(stack);
        } else {
            pPlayer.openMenu(new SimpleMenuProvider((i, inventory, player) ->
                    new DogTagEditorMenu(i, ContainerLevelAccess.create(pLevel, pPlayer.getOnPos()), stack), CONTAINER_TITLE));
            return InteractionResultHolder.consume(stack);
        }
    }
}
