package net.mcreator.target.init;

import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.*;
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
public class TargetModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TargetMod.MODID);

    public static final RegistryObject<EntityType<Target1Entity>> TARGET_1 = register("target_1",
            EntityType.Builder.<Target1Entity>of(Target1Entity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(256).setUpdateInterval(3).setCustomClientFactory(Target1Entity::new).fireImmune().sized(0.875f, 2f));
    public static final RegistryObject<EntityType<MortarEntity>> MORTAR = register("mortar",
            EntityType.Builder.<MortarEntity>of(MortarEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MortarEntity::new).fireImmune().sized(0.8f, 1.4f));
    public static final RegistryObject<EntityType<SenpaiEntity>> SENPAI = register("senpai",
            EntityType.Builder.<SenpaiEntity>of(SenpaiEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(1024).setUpdateInterval(3).setCustomClientFactory(SenpaiEntity::new)
                    .sized(0.6f, 2f));
    public static final RegistryObject<EntityType<ClaymoreEntity>> CLAYMORE = register("claymore",
            EntityType.Builder.<ClaymoreEntity>of(ClaymoreEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ClaymoreEntity::new).fireImmune().sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<TaserBulletProjectileEntity>> TASER_BULLET_PROJECTILE = register("projectile_taser_bullet_projectile",
            EntityType.Builder.<TaserBulletProjectileEntity>of(TaserBulletProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(TaserBulletProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
                    .setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<GunGrenadeEntity>> GUN_GRENADE = register("projectile_gun_grenade",
            EntityType.Builder.<GunGrenadeEntity>of(GunGrenadeEntity::new, MobCategory.MISC).setCustomClientFactory(GunGrenadeEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<TargetEntity>> TARGET = register("target",
            EntityType.Builder.<TargetEntity>of(TargetEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TargetEntity::new).fireImmune().sized(0.875f, 2f));
    public static final RegistryObject<EntityType<RpgRocketEntity>> RPG_ROCKET = register("projectile_rpg_rocket",
            EntityType.Builder.<RpgRocketEntity>of(RpgRocketEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<MortarShellEntity>> MORTAR_SHELL = register("projectile_mortar_shell",
            EntityType.Builder.<MortarShellEntity>of(MortarShellEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<BocekArrowEntity>> BOCEK_ARROW = register("projectile_bocekarrow",
            EntityType.Builder.<BocekArrowEntity>of(BocekArrowEntity::new, MobCategory.MISC).setCustomClientFactory(BocekArrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile",
            EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC).setTrackingRange(512).sized(0.5f, 0.5f));


    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(SenpaiEntity::init);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TARGET_1.get(), Target1Entity.createAttributes().build());
        event.put(MORTAR.get(), MortarEntity.createAttributes().build());
        event.put(SENPAI.get(), SenpaiEntity.createAttributes().build());
        event.put(CLAYMORE.get(), ClaymoreEntity.createAttributes().build());
        event.put(TARGET.get(), TargetEntity.createAttributes().build());
    }
}
