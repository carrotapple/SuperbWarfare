package net.mcreator.target.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber
public class PlayerRespawnRewardProcedure {
    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        execute(event, event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        {
            AtomicReference<IItemHandler> _iitemhandlerref = new AtomicReference<>();
            entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(_iitemhandlerref::set);
            if (_iitemhandlerref.get() != null) {
                for (int _idx = 0; _idx < _iitemhandlerref.get().getSlots(); _idx++) {
                    ItemStack itemstackiterator = _iitemhandlerref.get().getStackInSlot(_idx);
                    if (itemstackiterator.is(ItemTags.create(new ResourceLocation("target:gun")))) {
                        itemstackiterator.getOrCreateTag().putDouble("ammo", (itemstackiterator.getOrCreateTag().getDouble("mag")));
                    }
                }
            }
        }
    }
}
