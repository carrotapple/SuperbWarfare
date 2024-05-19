package net.mcreator.target.tools;

import com.google.gson.stream.JsonReader;
import net.mcreator.target.TargetMod;
import net.mcreator.target.entity.ProjectileEntity;
import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.network.TargetModVariables;
import net.mcreator.target.network.message.GunsDataMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TargetMod.MODID)
public class GunsTool {

    public static HashMap<String, HashMap<String, Double>> gunsData = new HashMap<>();

    public static void initJsonData(ResourceManager manager) {
        for (var entry : manager.listResources("guns", file -> file.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var attribute = entry.getValue();
            try {
                JsonReader reader = new JsonReader(new InputStreamReader(attribute.open()));

                reader.beginObject();
                var map = new HashMap<String, Double>();
                while (reader.hasNext()) {
                    map.put(reader.nextName(), reader.nextDouble());
                }
                var path = id.getPath();
                gunsData.put(path.substring(5, path.length() - 5), map);

                reader.endObject();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initGun(Level level, ItemStack stack, String location) {
        if (level.getServer() == null) return;
        gunsData.get(location).forEach((k, v) -> stack.getOrCreateTag().putDouble(k, v));
    }

    public static void initCreativeGun(ItemStack stack, String location) {
        if (gunsData != null && gunsData.get(location) != null) {
            gunsData.get(location).forEach((k, v) -> stack.getOrCreateTag().putDouble(k, v));
            stack.getOrCreateTag().putInt("ammo", stack.getOrCreateTag().getInt("mag"));
        }
    }

    public static void pvpModeCheck(ItemStack stack, Level level) {
        if (!TargetModVariables.MapVariables.get(level).pvpMode) {
            if (stack.getOrCreateTag().getInt("level") >= 10) {
                stack.getOrCreateTag().putDouble("damageadd", 1 + 0.05 * (stack.getOrCreateTag().getInt("level") - 10));
            } else {
                stack.getOrCreateTag().putDouble("damageadd", 1);
            }
        } else {
            stack.getOrCreateTag().putDouble("damageadd", 1);
        }
    }

    public static void genUUID(ItemStack stack) {
        UUID uuid = UUID.randomUUID();
        stack.getOrCreateTag().putUUID("gun_uuid", uuid);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TargetMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new GunsDataMessage(GunsTool.gunsData));
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        initJsonData(event.getServer().getResourceManager());
    }

    public static void spawnBullet(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.recoilHorizon = Math.random() < 0.5 ? -1 : 1;
            capability.recoil = 0.1;
            capability.firing = 1;
            capability.syncPlayerVariables(player);
        });

        if (player.level().isClientSide()) return;

        CompoundTag tag = heldItem.getOrCreateTag();
        double damage;
        float headshot = (float) tag.getDouble("headshot");
        float velocity = 4 * (float) tag.getDouble("speed");

        var projectile = new ProjectileEntity(player.level())
                .shooter(player)
                .headShot(headshot);
        if (tag.getBoolean("beast")) {
            projectile.beast();
        }
        projectile.setPos(player.getX() - 0.1 * player.getLookAngle().x, player.getEyeY() - 0.1 - 0.1 * player.getLookAngle().y, player.getZ() + -0.1 * player.getLookAngle().z);

        if (heldItem.getItem() == TargetModItems.BOCEK.get()) {
            damage = 0.008333333 * tag.getDouble("damage") * tag.getDouble("speed") * tag.getDouble("damageadd");
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 2.5f);
        } else {
            damage = tag.getDouble("damage") + tag.getDouble("add_damage") * tag.getDouble("damageadd");

            projectile.shoot(player.getLookAngle().x,
                    player.getLookAngle().y,
                    player.getLookAngle().z,
                    (float) tag.getDouble("velocity"),
                    (float) player.getAttribute(TargetModAttributes.SPREAD.get()).getBaseValue());
        }

        projectile.damage((float) damage);
        player.level().addFreshEntity(projectile);
    }
}
