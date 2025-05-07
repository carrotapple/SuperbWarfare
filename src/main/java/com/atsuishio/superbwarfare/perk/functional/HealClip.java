package com.atsuishio.superbwarfare.perk.functional;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.item.gun.data.GunData;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.perk.PerkInstance;
import com.atsuishio.superbwarfare.tools.DamageTypeTool;
import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HealClip extends Perk {

    public HealClip() {
        super("heal_clip", Perk.Type.FUNCTIONAL);
    }

    @Override
    public void onKill(GunData data, PerkInstance instance, @Nullable Player player, LivingEntity target, DamageSource source) {
        super.onKill(data, instance, player, target, source);

        if (DamageTypeTool.isGunDamage(source) || source.is(ModDamageTypes.PROJECTILE_BOOM)) {
            ItemStack stack = data.stack;
            int healClipLevel = instance.level();
            if (healClipLevel != 0) {
                GunsTool.setPerkIntTag(stack, "HealClipTime", 80 + healClipLevel * 20);
            }
        }
    }

    @Override
    public void preReload(GunData data, PerkInstance instance, @Nullable Player player) {
        super.preReload(data, instance, player);

        ItemStack stack = data.stack;
        int time = GunsTool.getPerkIntTag(stack, "HealClipTime");
        if (time > 0) {
            GunsTool.setPerkIntTag(stack, "HealClipTime", 0);
            GunsTool.setPerkBooleanTag(stack, "HealClip", true);
        } else {
            GunsTool.setPerkBooleanTag(stack, "HealClip", false);
        }
    }

    @Override
    public void postReload(GunData data, PerkInstance instance, @Nullable Player player) {
        super.postReload(data, instance, player);

        if (player == null) return;

        ItemStack stack = data.stack;

        if (!GunsTool.getPerkBooleanTag(stack, "HealClip")) {
            return;
        }

        int healClipLevel = instance.level();
        if (healClipLevel == 0) {
            healClipLevel = 1;
        }

        player.heal(12.0f * (0.8f + 0.2f * healClipLevel));
        List<Player> players = player.level().getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(5))
                .stream().filter(p -> p.isAlliedTo(player)).toList();
        int finalHealClipLevel = healClipLevel;
        players.forEach(p -> p.heal(6.0f * (0.8f + 0.2f * finalHealClipLevel)));
    }
}
