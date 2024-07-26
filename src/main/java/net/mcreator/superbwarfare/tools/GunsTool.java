package net.mcreator.superbwarfare.tools;

import com.google.gson.stream.JsonReader;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.network.TargetModVariables;
import net.mcreator.superbwarfare.network.message.GunsDataMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

@Mod.EventBusSubscriber(modid = ModUtils.MODID)
public class GunsTool {

    public static HashMap<String, HashMap<String, Double>> gunsData = new HashMap<>();

    /**
     * 初始化数据，从data中读取数据json文件
     */
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
            ModUtils.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new GunsDataMessage(GunsTool.gunsData));
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        initJsonData(event.getServer().getResourceManager());
    }

    public static void reload(Entity entity, GunInfo.Type type) {
        reload(entity, type, false);
    }

    public static void reload(Entity entity, GunInfo.Type type, boolean extraOne) {
        if (!(entity instanceof LivingEntity living)) return;

        CompoundTag tag = living.getMainHandItem().getOrCreateTag();

        int mag = tag.getInt("mag");
        int ammo = tag.getInt("ammo");
        int ammoToAdd = mag - ammo + (extraOne ? 1 : 0);
        /*
         * 空仓换弹的栓动武器应该在换单后取消待上膛标记
         */
        if (ammo == 0 && tag.getDouble("bolt_action_time") > 0) {
            tag.putDouble("need_bolt_action", 0);
        }

        int playerAmmo = entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> switch (type) {
            case RIFLE -> c.rifleAmmo;
            case HANDGUN -> c.handgunAmmo;
            case SHOTGUN -> c.shotgunAmmo;
            case SNIPER -> c.sniperAmmo;
        }).orElse(0);

        entity.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            var newAmmoCount = Math.max(0, playerAmmo - ammoToAdd);
            switch (type) {
                case RIFLE -> capability.rifleAmmo = newAmmoCount;
                case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                case SNIPER -> capability.sniperAmmo = newAmmoCount;
            }

            capability.syncPlayerVariables(entity);
        });

        int needToAdd = ammo + Math.min(ammoToAdd, playerAmmo);

        tag.putInt("ammo", needToAdd);
        tag.putBoolean("is_normal_reloading", false);
        tag.putBoolean("is_empty_reloading", false);
    }
}
