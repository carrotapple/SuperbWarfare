
package net.mcreator.target.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.*;
import net.mcreator.target.headshot.BoundingBoxManager;
import net.mcreator.target.headshot.IHeadshotBox;
import net.mcreator.target.init.TargetModEntities;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import net.mcreator.target.network.TargetModVariables;

import java.util.Optional;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class BocekarrowEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Items.ARROW);

	public BocekarrowEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(TargetModEntities.BOCEKARROW.get(), world);
	}

	public BocekarrowEntity(EntityType<? extends BocekarrowEntity> type, Level world) {
		super(type, world);
	}

	public BocekarrowEntity(EntityType<? extends BocekarrowEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public BocekarrowEntity(EntityType<? extends BocekarrowEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getPickupItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		final Vec3 position = this.position();
		Entity entity = result.getEntity();
							if(this.getOwner() instanceof LivingEntity living){
					double _setval = 25;
					living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.hitind = _setval;
					capability.syncPlayerVariables(living);
							});
					Entity _ent = living;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:indication voice @a ~ ~ ~ 1 1");
							}
						}
		if (entity instanceof LivingEntity livingEntity) {
        	entity.invulnerableTime = 0;
        }
		AABB boundingBox = entity.getBoundingBox();
		Vec3 startVec = this.position();
		Vec3 endVec = startVec.add(this.getDeltaMovement());
		Vec3 hitPos = boundingBox.clip(startVec, endVec).orElse(null);
		/* Check for headshot */
		boolean headshot = false;
		if(entity instanceof LivingEntity)
		{
			IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
			if(headshotBox != null)
			{
				AABB box = headshotBox.getHeadshotBox((LivingEntity) entity);
				if(box != null)
				{
					box = box.move(boundingBox.getCenter().x, boundingBox.minY, boundingBox.getCenter().z);
					Optional<Vec3> headshotHitPos = box.clip(startVec, endVec);
					if(!headshotHitPos.isPresent())
					{
						box = box.inflate( 0.2, 0.2, 0.2);
						headshotHitPos = box.clip(startVec, endVec);
					}
					if(headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.55))
					{
						hitPos = headshotHitPos.get();
						headshot = true;
					}
					if(headshot){
					if(this.getOwner() instanceof LivingEntity living){
					setBaseDamage(getBaseDamage() * 2);
					double _setval = 25;
					living.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.headind = _setval;
					capability.syncPlayerVariables(living);
							});
					Entity _ent = living;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound target:headshot voice @a ~ ~ ~ 1 1");
							}
						}
					}
				}
			}
		}
				super.onHitEntity(result);
				this.discard();
	}

	@Override
	public void tick() {
		super.tick();
		if(this.tickCount>200){
		this.discard();
		}
	}

	public static BocekarrowEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 0);
	}

	public static BocekarrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		BocekarrowEntity entityarrow = new BocekarrowEntity(TargetModEntities.BOCEKARROW.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static BocekarrowEntity shoot(LivingEntity entity, LivingEntity target) {
		BocekarrowEntity entityarrow = new BocekarrowEntity(TargetModEntities.BOCEKARROW.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
