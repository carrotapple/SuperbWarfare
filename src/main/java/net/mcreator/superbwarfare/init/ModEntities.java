package net.mcreator.superbwarfare.init;

import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModUtils.MODID);

    public static final RegistryObject<EntityType<TargetEntity>> TARGET = register("target",
            EntityType.Builder.<TargetEntity>of(TargetEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(3).setCustomClientFactory(TargetEntity::new).fireImmune().sized(0.875f, 2f));
    public static final RegistryObject<EntityType<MortarEntity>> MORTAR = register("mortar",
            EntityType.Builder.<MortarEntity>of(MortarEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MortarEntity::new).fireImmune().sized(0.8f, 1.4f));
    public static final RegistryObject<EntityType<SenpaiEntity>> SENPAI = register("senpai",
            EntityType.Builder.<SenpaiEntity>of(SenpaiEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(1024).setUpdateInterval(3).setCustomClientFactory(SenpaiEntity::new)
                    .sized(0.6f, 2f));
    public static final RegistryObject<EntityType<ClaymoreEntity>> CLAYMORE = register("claymore",
            EntityType.Builder.<ClaymoreEntity>of(ClaymoreEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<Mk42Entity>> MK_42 = register("mk_42",
            EntityType.Builder.<Mk42Entity>of(Mk42Entity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(5124).setUpdateInterval(3).setCustomClientFactory(Mk42Entity::new).fireImmune().sized(3.4f, 3.5f));
    public static final RegistryObject<EntityType<Mle1934Entity>> MLE_1934 = register("mle_1934",
            EntityType.Builder.<Mle1934Entity>of(Mle1934Entity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(3).setCustomClientFactory(Mle1934Entity::new).fireImmune().sized(4.5f, 2.8f));

    public static final RegistryObject<EntityType<DroneEntity>> DRONE = register("drone",
            EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(3).setCustomClientFactory(DroneEntity::new).sized(0.7f, 0.175f));

    public static final RegistryObject<EntityType<TaserBulletProjectileEntity>> TASER_BULLET_PROJECTILE = register("projectile_taser_bullet_projectile",
            EntityType.Builder.<TaserBulletProjectileEntity>of(TaserBulletProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(TaserBulletProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
                    .setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<GunGrenadeEntity>> GUN_GRENADE = register("projectile_gun_grenade",
            EntityType.Builder.<GunGrenadeEntity>of(GunGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(GunGrenadeEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<RpgRocketEntity>> RPG_ROCKET = register("projectile_rpg_rocket",
            EntityType.Builder.<RpgRocketEntity>of(RpgRocketEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(RpgRocketEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<MortarShellEntity>> MORTAR_SHELL = register("projectile_mortar_shell",
            EntityType.Builder.<MortarShellEntity>of(MortarShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(MortarShellEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<BocekArrowEntity>> BOCEK_ARROW = register("projectile_bocekarrow",
            EntityType.Builder.<BocekArrowEntity>of(BocekArrowEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setCustomClientFactory(BocekArrowEntity::new).setTrackingRange(512).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile",
            EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(ProjectileEntity::new).setTrackingRange(512).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<CannonShellEntity>> CANNON_SHELL = register("projectile_cannon_shell",
            EntityType.Builder.<CannonShellEntity>of(CannonShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(CannonShellEntity::new).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<HandGrenadeEntity>> HAND_GRENADE_ENTITY = register("projectile_hand_grenade_entity",
            EntityType.Builder.<HandGrenadeEntity>of(HandGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(HandGrenadeEntity::new).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<RgoGrenadeEntity>> RGO_GRENADE = register("projectile_rgo_grenade",
            EntityType.Builder.<RgoGrenadeEntity>of(RgoGrenadeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(RgoGrenadeEntity::new).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<JavelinMissileEntity>> JAVELIN_MISSILE = register("projectile_javelin_missile",
            EntityType.Builder.<JavelinMissileEntity>of(JavelinMissileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(512).setUpdateInterval(1).setCustomClientFactory(JavelinMissileEntity::new).sized(0.5f, 0.5f));


    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(SenpaiEntity::init);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TARGET.get(), TargetEntity.createAttributes().build());
        event.put(MORTAR.get(), MortarEntity.createAttributes().build());
        event.put(SENPAI.get(), SenpaiEntity.createAttributes().build());
        event.put(MK_42.get(), Mk42Entity.createAttributes().build());
        event.put(DRONE.get(), DroneEntity.createAttributes().build());
        event.put(MLE_1934.get(), Mle1934Entity.createAttributes().build());
        event.put(CLAYMORE.get(), ClaymoreEntity.createAttributes().build());
    }
}
