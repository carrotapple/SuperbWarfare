package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.config.server.SpawnConfig;
import com.atsuishio.superbwarfare.entity.*;
import com.atsuishio.superbwarfare.entity.projectile.*;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModUtils.MODID);

    public static final RegistryObject<EntityType<TargetEntity>> TARGET = register("target",
            EntityType.Builder.<TargetEntity>of(TargetEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TargetEntity::new).fireImmune().sized(0.875f, 2f));
    public static final RegistryObject<EntityType<MortarEntity>> MORTAR = register("mortar",
            EntityType.Builder.<MortarEntity>of(MortarEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MortarEntity::new).fireImmune().sized(0.8f, 1.4f));
    public static final RegistryObject<EntityType<SenpaiEntity>> SENPAI = register("senpai",
            EntityType.Builder.<SenpaiEntity>of(SenpaiEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SenpaiEntity::new)
                    .sized(0.6f, 2f));
    public static final RegistryObject<EntityType<ClaymoreEntity>> CLAYMORE = register("claymore",
            EntityType.Builder.<ClaymoreEntity>of(ClaymoreEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<C4Entity>> C_4 = register("c4",
            EntityType.Builder.<C4Entity>of(C4Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<Mk42Entity>> MK_42 = register("mk_42",
            EntityType.Builder.<Mk42Entity>of(Mk42Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Mk42Entity::new).fireImmune().sized(3.4f, 3.5f));
    public static final RegistryObject<EntityType<Mle1934Entity>> MLE_1934 = register("mle_1934",
            EntityType.Builder.<Mle1934Entity>of(Mle1934Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Mle1934Entity::new).fireImmune().sized(4.5f, 2.8f));
    public static final RegistryObject<EntityType<AnnihilatorEntity>> ANNIHILATOR = register("annihilator",
            EntityType.Builder.<AnnihilatorEntity>of(AnnihilatorEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(AnnihilatorEntity::new).fireImmune().sized(13f, 4.2f));

    public static final RegistryObject<EntityType<DroneEntity>> DRONE = register("drone",
            EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(DroneEntity::new).sized(0.6f, 0.2f));

    public static final RegistryObject<EntityType<TaserBulletProjectileEntity>> TASER_BULLET_PROJECTILE = register("projectile_taser_bullet_projectile",
            EntityType.Builder.<TaserBulletProjectileEntity>of(TaserBulletProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(TaserBulletProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
                    .setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<GunGrenadeEntity>> GUN_GRENADE = register("projectile_gun_grenade",
            EntityType.Builder.<GunGrenadeEntity>of(GunGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(GunGrenadeEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<SmallCannonShellEntity>> SMALL_CANNON_SHELL = register("projectile_small_cannon_shell",
            EntityType.Builder.<SmallCannonShellEntity>of(SmallCannonShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(SmallCannonShellEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<RpgRocketEntity>> RPG_ROCKET = register("projectile_rpg_rocket",
            EntityType.Builder.<RpgRocketEntity>of(RpgRocketEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(RpgRocketEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<MortarShellEntity>> MORTAR_SHELL = register("projectile_mortar_shell",
            EntityType.Builder.<MortarShellEntity>of(MortarShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(MortarShellEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile",
            EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(ProjectileEntity::new).setTrackingRange(64).noSave().noSummon().sized(0.25f, 0.25f));
    public static final RegistryObject<EntityType<CannonShellEntity>> CANNON_SHELL = register("projectile_cannon_shell",
            EntityType.Builder.<CannonShellEntity>of(CannonShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(CannonShellEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<HandGrenadeEntity>> HAND_GRENADE_ENTITY = register("projectile_hand_grenade_entity",
            EntityType.Builder.<HandGrenadeEntity>of(HandGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(HandGrenadeEntity::new).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<RgoGrenadeEntity>> RGO_GRENADE = register("projectile_rgo_grenade",
            EntityType.Builder.<RgoGrenadeEntity>of(RgoGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(RgoGrenadeEntity::new).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<JavelinMissileEntity>> JAVELIN_MISSILE = register("projectile_javelin_missile",
            EntityType.Builder.<JavelinMissileEntity>of(JavelinMissileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(JavelinMissileEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<LaserEntity>> LASER = register("laser",
            EntityType.Builder.<LaserEntity>of(LaserEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).fireImmune().setUpdateInterval(1));
    public static final RegistryObject<EntityType<SpeedboatEntity>> SPEEDBOAT = register("speedboat",
            EntityType.Builder.<SpeedboatEntity>of(SpeedboatEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SpeedboatEntity::new).fireImmune().sized(3.0f, 2.0f));
    public static final RegistryObject<EntityType<WheelChairEntity>> WHEEL_CHAIR = register("wheel_chair",
            EntityType.Builder.<WheelChairEntity>of(WheelChairEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(WheelChairEntity::new).fireImmune().sized(1.0f, 1.0f));
    public static final RegistryObject<EntityType<Ah6Entity>> AH_6 = register("ah_6",
            EntityType.Builder.<Ah6Entity>of(Ah6Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Ah6Entity::new).fireImmune().sized(2.8f, 2.9f));
    public static final RegistryObject<EntityType<HeliRocketEntity>> HELI_ROCKET = register("projectile_heli_rocket",
            EntityType.Builder.<HeliRocketEntity>of(HeliRocketEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(HeliRocketEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<FlareDecoyEntity>> FLARE_DECOY = register("flare_decoy_entity",
            EntityType.Builder.<FlareDecoyEntity>of(FlareDecoyEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(FlareDecoyEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<Lav150Entity>> LAV_150 = register("lav_150",
            EntityType.Builder.<Lav150Entity>of(Lav150Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Lav150Entity::new).fireImmune().sized(2.8f, 3.1f));
    public static final RegistryObject<EntityType<Tom6Entity>> TOM_6 = register("tom_6",
            EntityType.Builder.<Tom6Entity>of(Tom6Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Tom6Entity::new).fireImmune().sized(1.05f, 1.0f));
    public static final RegistryObject<EntityType<MelonBombEntity>> MELON_BOMB = register("melon_bomb",
            EntityType.Builder.<MelonBombEntity>of(MelonBombEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(MelonBombEntity::new).sized(1f, 1f));
    public static final RegistryObject<EntityType<Bmp2Entity>> BMP_2 = register("bmp_2",
            EntityType.Builder.<Bmp2Entity>of(Bmp2Entity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Bmp2Entity::new).fireImmune().sized(4f, 3f));
    public static final RegistryObject<EntityType<WgMissileEntity>> WG_MISSILE = register("wg_missile",
            EntityType.Builder.<WgMissileEntity>of(WgMissileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(WgMissileEntity::new).fireImmune().sized(0.5f, 0.5f));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.SENPAI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && SpawnConfig.SPAWN_SENPAI.get()
                        && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
                SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TARGET.get(), TargetEntity.createAttributes().build());
        event.put(SENPAI.get(), SenpaiEntity.createAttributes().build());
    }
}
