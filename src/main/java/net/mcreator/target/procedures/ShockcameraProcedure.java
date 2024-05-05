package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModMobEffects;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ShockcameraProcedure {
    private static ViewportEvent.ComputeCameraAngles _provider = null;

    private static void setAngles(float yaw, float pitch, float roll) {
        _provider.setYaw(yaw);
        _provider.setPitch(pitch);
        _provider.setRoll(roll);
    }

    @SubscribeEvent
    public static void computeCameraangles(ViewportEvent.ComputeCameraAngles event) {
        _provider = event;
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = _provider.getCamera().getEntity();
        if (level != null && entity != null) {
            Vec3 entPos = entity.getPosition((float) _provider.getPartialTick());
            execute(_provider, entity);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(TargetModMobEffects.SHOCK.get()) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            setAngles(Minecraft.getInstance().gameRenderer.getMainCamera().getYRot(), Minecraft.getInstance().gameRenderer.getMainCamera().getXRot(), (float) Mth.nextDouble(RandomSource.create(), 8, 12));
        }
    }
}
