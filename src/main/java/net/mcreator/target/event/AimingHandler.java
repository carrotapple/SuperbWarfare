package net.mcreator.target.event;

import net.mcreator.target.network.TargetModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AimingHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            ItemStack itemstack = event.player.getMainHandItem();

            if (itemstack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
                itemstack.getOrCreateTag().putBoolean("aiming", event.player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables()).zooming);
            }
        }
    }
}
