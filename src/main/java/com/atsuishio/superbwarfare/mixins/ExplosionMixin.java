package com.atsuishio.superbwarfare.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @ModifyVariable(method = "explode()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 14)
    private double modifyD11(double d) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return d;


        return d;
    }

}
