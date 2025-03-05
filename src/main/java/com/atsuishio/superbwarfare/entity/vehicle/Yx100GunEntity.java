package com.atsuishio.superbwarfare.entity.vehicle;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.entity.vehicle.base.CannonEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.AmmoType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Yx100GunEntity extends VehicleEntity implements GeoEntity, CannonEntity {
    public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(Yx100GunEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Yx100GunEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.YX_100_GUN.get(), world);
    }

    public Yx100GunEntity(EntityType<Yx100GunEntity> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AMMO, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        return super.interact(player, hand);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 1;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void baseTick() {
//        if (!(this.getVehicle() instanceof Yx100Entity yx100)) {
//            this.discard();
//        }
        if (this.level() instanceof ServerLevel) {
            this.handleAmmo();
        }
        super.baseTick();
    }

    private void handleAmmo() {
        if (!(this.getFirstPassenger() instanceof Player player)) return;

        if (this.getVehicle() instanceof Yx100Entity yx100) {
            int ammoCount = yx100.getItemStacks().stream().filter(stack -> {
                if (stack.is(ModItems.AMMO_BOX.get())) {
                    return AmmoType.HEAVY.get(stack) > 0;
                }
                return false;
            }).mapToInt(AmmoType.HEAVY::get).sum() + yx100.countItem(ModItems.HEAVY_AMMO.get());


            this.entityData.set(AMMO, ammoCount);
        }
    }

    @Override
    public void vehicleShoot(Player player, int type) {
    }


    @Override
    public void travel() {
        Entity passenger = this.getFirstPassenger();
        float diffY = 0;
        float diffX = 0;

        if (passenger != null) {
            diffY = Mth.wrapDegrees(passenger.getYHeadRot() - this.getYRot());
            diffX = Mth.wrapDegrees(passenger.getXRot() - this.getXRot());
        }

        this.setYRot(this.getYRot() + Mth.clamp(0.5f * diffY, -25f, 25f));
        this.setXRot(Mth.clamp(this.getXRot() + Mth.clamp(0.5f * diffX, -25f, 25f), -65, 7f));
    }

    protected void clampRotation(Entity entity) {
        float f = Mth.wrapDegrees(entity.getXRot());
        float f1 = Mth.clamp(f, -85.0F, 16.3F);
        entity.xRotO += f1 - f;
        entity.setXRot(entity.getXRot() + f1 - f);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getMaxHealth() {
        if (this.getVehicle() instanceof VehicleEntity vehicle) {
            return vehicle.getMaxHealth();
        }
        return 0;
    }

    @Override
    public int mainGunRpm(Player player) {
        return 600;
    }

    @Override
    public boolean canShoot(Player player) {
        return true;
    }

    @Override
    public int getAmmoCount(Player player) {
        return this.entityData.get(AMMO);
    }

    @Override
    public boolean banHand(Player player) {
        return true;
    }

    @Override
    public boolean hidePassenger() {
        return true;
    }

    @Override
    public int zoomFov() {
        return 3;
    }

    @Override
    public ResourceLocation getVehicleIcon() {
        return ModUtils.loc("textures/vehicle_icon/sherman_icon.png");
    }
}
