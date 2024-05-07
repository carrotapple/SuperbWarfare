package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;


@Mod.EventBusSubscriber
public class GunsDataProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player.level(), event.player);
        }
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        ItemStack itemstackiterator;
        itemstackiterator = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);

        if (itemstackiterator.is(ItemTags.create(new ResourceLocation("target:gun")))) {

            if (itemstackiterator.getItem() == TargetModItems.M_98B.get()) {
                itemstackiterator.getOrCreateTag().putDouble("zoomspeed", 0.85);
                itemstackiterator.getOrCreateTag().putDouble("zoom", 4);
                itemstackiterator.getOrCreateTag().putDouble("sniperguns", 1);
                itemstackiterator.getOrCreateTag().putDouble("dev", 6);
                itemstackiterator.getOrCreateTag().putDouble("recoilx", 0.007);
                itemstackiterator.getOrCreateTag().putDouble("recoily", 0.013);
                itemstackiterator.getOrCreateTag().putDouble("damage", 28);
                itemstackiterator.getOrCreateTag().putDouble("headshot", 3);
                itemstackiterator.getOrCreateTag().putDouble("velocity", 55);
                itemstackiterator.getOrCreateTag().putDouble("mag", 5);
            }
            if (itemstackiterator.getItem() == TargetModItems.RPG.get()) {
                itemstackiterator.getOrCreateTag().putDouble("zoomspeed", 0.77);
                itemstackiterator.getOrCreateTag().putDouble("zoom", 1.25);
                itemstackiterator.getOrCreateTag().putDouble("dev", 5);
                itemstackiterator.getOrCreateTag().putDouble("recoilx", 0.008);
                itemstackiterator.getOrCreateTag().putDouble("recoily", 0.018);
                itemstackiterator.getOrCreateTag().putDouble("damage", 150);
                itemstackiterator.getOrCreateTag().putDouble("velocity", 5.75);
                itemstackiterator.getOrCreateTag().putDouble("mag", 1);
            }
            if (itemstackiterator.getItem() == TargetModItems.M_4.get()) {
                itemstackiterator.getOrCreateTag().putDouble("zoomspeed", 1.15);
                itemstackiterator.getOrCreateTag().putDouble("zoom", 1.25);
                itemstackiterator.getOrCreateTag().putDouble("rifle", 1);
                itemstackiterator.getOrCreateTag().putDouble("autorifle", 1);
                itemstackiterator.getOrCreateTag().putDouble("dev", 4);
                itemstackiterator.getOrCreateTag().putDouble("recoilx", 0.0015);
                itemstackiterator.getOrCreateTag().putDouble("recoily", 0.011);
                itemstackiterator.getOrCreateTag().putDouble("damage", 7);
                itemstackiterator.getOrCreateTag().putDouble("headshot", 2);
                itemstackiterator.getOrCreateTag().putDouble("velocity", 45);
                itemstackiterator.getOrCreateTag().putDouble("mag", 30);
            }
            if (itemstackiterator.getItem() == TargetModItems.BOCEK.get()) {
                itemstackiterator.getOrCreateTag().putDouble("zoomspeed", 1);
                itemstackiterator.getOrCreateTag().putDouble("zoom", 2);
                itemstackiterator.getOrCreateTag().putDouble("autorifle", 1);
                itemstackiterator.getOrCreateTag().putDouble("dev", 4);
                itemstackiterator.getOrCreateTag().putDouble("recoilx", 0.005);
                itemstackiterator.getOrCreateTag().putDouble("recoily", 0.003);
                itemstackiterator.getOrCreateTag().putDouble("headshot", 1.5);
                itemstackiterator.getOrCreateTag().putDouble("damage", 2.4);
            }
            if (itemstackiterator.getItem() == TargetModItems.HK_416.get()) {
                itemstackiterator.getOrCreateTag().putDouble("zoomspeed", 1.3);
                itemstackiterator.getOrCreateTag().putDouble("zoom", 1.25);
                itemstackiterator.getOrCreateTag().putDouble("rifle", 1);
                itemstackiterator.getOrCreateTag().putDouble("autorifle", 1);
                itemstackiterator.getOrCreateTag().putDouble("dev", 4);
                itemstackiterator.getOrCreateTag().putDouble("recoilx", 0.0016);
                itemstackiterator.getOrCreateTag().putDouble("recoily", 0.009);
                itemstackiterator.getOrCreateTag().putDouble("damage", 7.5);
                itemstackiterator.getOrCreateTag().putDouble("headshot", 2);
                itemstackiterator.getOrCreateTag().putDouble("velocity", 45);
                itemstackiterator.getOrCreateTag().putDouble("mag", 30);
            }
        }
    }

}
