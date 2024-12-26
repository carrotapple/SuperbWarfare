package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.client.renderer.block.ContainerBlockItemRenderer;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModEntities;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ContainerBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ContainerBlockItem() {
        super(ModBlocks.CONTAINER.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult place(BlockPlaceContext pContext) {
        ItemStack stack = pContext.getItemInHand();
        Player player = pContext.getPlayer();
        if (player != null) {
            var tag = BlockItem.getBlockEntityData(stack);
            if (tag != null && tag.get("Entity") != null) {
                player.getInventory().removeItem(stack);
            }
        }
        return super.place(pContext);
    }

    private PlayState predicate(AnimationState<ContainerBlockItem> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new ContainerBlockItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static ItemStack createInstance(Entity entity) {
        ItemStack stack = new ItemStack(ModBlocks.CONTAINER.get());
        CompoundTag tag = new CompoundTag();
        tag.put("Entity", entity.serializeNBT());
        tag.putString("EntityType", EntityType.getKey(entity.getType()).toString());
        BlockItem.setBlockEntityData(stack, ModBlockEntities.CONTAINER.get(), tag);
        return stack;
    }

    public static ItemStack createInstance(EntityType<?> entityType) {
        ItemStack stack = new ItemStack(ModBlocks.CONTAINER.get());
        CompoundTag tag = new CompoundTag();
        tag.putString("EntityType", EntityType.getKey(entityType).toString());
        BlockItem.setBlockEntityData(stack, ModBlockEntities.CONTAINER.get(), tag);
        return stack;
    }

    public static ItemStack createMk42Instance() {
        return createInstance(ModEntities.MK_42.get());
    }

    public static ItemStack createMle1934Instance() {
        return createInstance(ModEntities.MLE_1934.get());
    }

    public static ItemStack createAnnihilatorInstance() {
        return createInstance(ModEntities.ANNIHILATOR.get());
    }
    public static ItemStack createSpeedboatInstance() {
        return createInstance(ModEntities.SPEEDBOAT.get());
    }
    public static ItemStack createWheelChairInstance() {
        return createInstance(ModEntities.WHEEL_CHAIR.get());
    }
}
