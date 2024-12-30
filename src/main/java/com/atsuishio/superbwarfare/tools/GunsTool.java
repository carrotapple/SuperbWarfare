package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.ModUtils;
import com.atsuishio.superbwarfare.init.ModTags;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.network.ModVariables;
import com.atsuishio.superbwarfare.network.message.GunsDataMessage;
import com.google.gson.stream.JsonReader;
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

import javax.annotation.Nullable;
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
                ModUtils.LOGGER.error(e.getMessage());
            }
        }
    }

    public static void initGun(Level level, ItemStack stack, String location) {
        if (level.getServer() == null) return;
        gunsData.get(location).forEach((k, v) -> {
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag data = tag.getCompound("GunData");
            data.putDouble(k, v);
            stack.addTagElement("GunData", data);
        });
    }

    public static void initCreativeGun(ItemStack stack, String location) {
        if (gunsData != null && gunsData.get(location) != null) {
            gunsData.get(location).forEach((k, v) -> {
                CompoundTag tag = stack.getOrCreateTag();
                CompoundTag data = tag.getCompound("GunData");
                data.putDouble(k, v);
                stack.addTagElement("GunData", data);
            });
            GunsTool.setGunIntTag(stack, "Ammo", GunsTool.getGunIntTag(stack, "Magazine", 0)
                    + stack.getOrCreateTag().getInt("customMag"));
        }
    }

    public static void generateAndSetUUID(ItemStack stack) {
        UUID uuid = UUID.randomUUID();
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        data.putUUID("UUID", uuid);
        stack.addTagElement("GunData", data);
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

    public static void reload(Player player, ItemStack stack, GunInfo.Type type) {
        reload(player, stack, type, false);
    }

    public static void reload(Player player, ItemStack stack, GunInfo.Type type, boolean extraOne) {
        CompoundTag tag = stack.getOrCreateTag();
        GunItem gunItem = null;
        if (stack.getItem() instanceof GunItem gunItem1) {
            gunItem = gunItem1;
        }
        if (gunItem == null) return;

        int mag = GunsTool.getGunIntTag(stack, "Magazine", 0) + tag.getInt("customMag");
        int ammo = gunItem.getAmmoCount(stack);
        int ammoToAdd = mag - ammo + (extraOne ? 1 : 0);

        // 空仓换弹的栓动武器应该在换弹后取消待上膛标记
        if (ammo == 0 && GunsTool.getGunIntTag(stack, "BoltActionTime", 0) > 0 && !stack.is(ModTags.Items.REVOLVER)) {
            GunsTool.setGunBooleanTag(stack, "NeedBoltAction", false);
        }

        int playerAmmo = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(c -> switch (type) {
            case RIFLE -> c.rifleAmmo;
            case HANDGUN -> c.handgunAmmo;
            case SHOTGUN -> c.shotgunAmmo;
            case SNIPER -> c.sniperAmmo;
        }).orElse(0);

        player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            var newAmmoCount = Math.max(0, playerAmmo - ammoToAdd);
            switch (type) {
                case RIFLE -> capability.rifleAmmo = newAmmoCount;
                case HANDGUN -> capability.handgunAmmo = newAmmoCount;
                case SHOTGUN -> capability.shotgunAmmo = newAmmoCount;
                case SNIPER -> capability.sniperAmmo = newAmmoCount;
            }

            capability.syncPlayerVariables(player);
        });

        int needToAdd = ammo + Math.min(ammoToAdd, playerAmmo);

        GunsTool.setGunIntTag(stack, "Ammo", needToAdd);
        tag.putBoolean("is_normal_reloading", false);
        tag.putBoolean("is_empty_reloading", false);
    }

    /* PerkData */
    public static void setPerkIntTag(ItemStack stack, String name, int num) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        tag.putInt(name, num);
        stack.addTagElement("PerkData", tag);
    }

    public static int getPerkIntTag(ItemStack stack, String name) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        return tag.getInt(name);
    }

    public static void setPerkDoubleTag(ItemStack stack, String name, double num) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        tag.putDouble(name, num);
        stack.addTagElement("PerkData", tag);
    }

    public static double getPerkDoubleTag(ItemStack stack, String name) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        return tag.getDouble(name);
    }

    public static void setPerkBooleanTag(ItemStack stack, String name, boolean flag) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        tag.putBoolean(name, flag);
        stack.addTagElement("PerkData", tag);
    }

    public static boolean getPerkBooleanTag(ItemStack stack, String name) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("PerkData");
        return tag.getBoolean(name);
    }

    /* Attachments */
    public static int getAttachmentType(ItemStack stack, AttachmentType type) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("Attachments");
        return tag.getInt(type.getName());
    }

    public enum AttachmentType {
        SCOPE("Scope"),
        MAGAZINE("Magazine"),
        BARREL("Barrel"),
        STOCK("Stock"),
        GRIP("Grip");

        private final String name;

        AttachmentType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /* GunData */
    public static CompoundTag getGunData(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getCompound("GunData");
    }

    public static void setGunIntTag(ItemStack stack, String name, int num) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        data.putInt(name, num);
        stack.addTagElement("GunData", data);
    }

    public static int getGunIntTag(ItemStack stack, String name) {
        return getGunIntTag(stack, name, 0);
    }

    public static int getGunIntTag(ItemStack stack, String name, int defaultValue) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getInt(name);
    }

    public static void setGunDoubleTag(ItemStack stack, String name, double num) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        data.putDouble(name, num);
        stack.addTagElement("GunData", data);
    }

    public static double getGunDoubleTag(ItemStack stack, String name) {
        return getGunDoubleTag(stack, name, 0);
    }

    public static double getGunDoubleTag(ItemStack stack, String name, double defaultValue) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getDouble(name);
    }

    public static void setGunBooleanTag(ItemStack stack, String name, boolean flag) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        data.putBoolean(name, flag);
        stack.addTagElement("GunData", data);
    }

    public static boolean getGunBooleanTag(ItemStack stack, String name) {
        return getGunBooleanTag(stack, name, false);
    }

    public static boolean getGunBooleanTag(ItemStack stack, String name, boolean defaultValue) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getBoolean(name);
    }

    @Nullable
    public static UUID getGunUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("GunData")) return null;
        CompoundTag data = tag.getCompound("GunData");
        if (!data.hasUUID("UUID")) return null;
        return data.getUUID("UUID");
    }
}
