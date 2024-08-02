package net.mcreator.superbwarfare.event;

import net.mcreator.superbwarfare.event.modevent.ReloadEvent;
import net.mcreator.superbwarfare.init.ModEnchantments;
import net.mcreator.superbwarfare.init.ModTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ReloadEventHandler {

    @SubscribeEvent
    public static void onPreReload(ReloadEvent.Pre event) {
        Player player = event.player;
        ItemStack stack = event.stack;
        if (player == null || !stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        handleHealClipPre(stack);
        handleKillClipPre(stack);
        handleKillingTallyPre(stack);
    }

    @SubscribeEvent
    public static void onPostReload(ReloadEvent.Post event) {
        Player player = event.player;
        ItemStack stack = event.stack;
        if (player == null || !stack.is(ModTags.Items.GUN)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        handleHealClipPost(player, stack);
        handleKillClipPost(stack);
    }

    private static void handleHealClipPre(ItemStack stack) {
        int time = stack.getOrCreateTag().getInt("HealClipTime");
        if (time > 0) {
            stack.getOrCreateTag().putInt("HealClipTime", 0);
            stack.getOrCreateTag().putBoolean("HealClip", true);
        } else {
            stack.getOrCreateTag().putBoolean("HealClip", false);
        }
    }

    private static void handleHealClipPost(Player player, ItemStack stack) {
        if (!stack.getOrCreateTag().getBoolean("HealClip")) {
            return;
        }

        int healClipLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.HEAL_CLIP.get(), stack);
        if (healClipLevel == 0) {
            healClipLevel = 1;
        }

        player.heal(12.0f * (0.8f + 0.2f * healClipLevel));
        List<Player> players = player.level().getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(5))
                .stream().filter(p -> p.isAlliedTo(player)).toList();
        int finalHealClipLevel = healClipLevel;
        players.forEach(p -> p.heal(6.0f * (0.8f + 0.2f * finalHealClipLevel)));
    }

    private static void handleKillClipPre(ItemStack stack) {
        int time = stack.getOrCreateTag().getInt("KillClipReloadTime");
        if (time > 0) {
            stack.getOrCreateTag().putInt("KillClipReloadTime", 0);
            stack.getOrCreateTag().putBoolean("KillClip", true);
        } else {
            stack.getOrCreateTag().putBoolean("KillClip", false);
        }
    }

    private static void handleKillClipPost(ItemStack stack) {
        if (!stack.getOrCreateTag().getBoolean("KillClip")) {
            return;
        }

        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILL_CLIP.get(), stack);
        stack.getOrCreateTag().putInt("KillClipTime", 90 + 10 * level);
    }

    private static void handleKillingTallyPre(ItemStack stack) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.KILLING_TALLY.get(), stack);
        if (level == 0) {
            return;
        }

        if (stack.getOrCreateTag().contains("KillingTally")) {
            stack.getOrCreateTag().putInt("KillingTally", 0);
        }
    }
}
