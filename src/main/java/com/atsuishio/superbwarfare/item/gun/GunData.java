package com.atsuishio.superbwarfare.item.gun;

import com.atsuishio.superbwarfare.tools.GunsTool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.WeakHashMap;

public class GunData {
    private final ItemStack stack;
    private final GunItem item;
    private final CompoundTag tag;
    private final CompoundTag data;
    private final String id;

    private static final WeakHashMap<ItemStack, GunData> dataCache = new WeakHashMap<>();

    private GunData(ItemStack stack) {
        if (!(stack.getItem() instanceof GunItem gunItem)) {
            throw new IllegalArgumentException("stack is not GunItem!");
        }
        this.item = gunItem;

        this.stack = stack;
        this.tag = stack.getOrCreateTag();

        if (!tag.contains("GunData")) {
            data = new CompoundTag();
            tag.put("GunData", data);
        } else {
            data = tag.getCompound("GunData");
        }

        var id = stack.getDescriptionId();
        this.id = id.substring(id.lastIndexOf(".") + 1);
    }

    public static GunData from(ItemStack stack) {
        var value = dataCache.get(stack);
        if (value == null) {
            value = new GunData(stack);
            dataCache.put(stack, value);
        }
        return value;
    }

    public GunItem getItem() {
        return item;
    }

    public CompoundTag getTag() {
        return tag;
    }

    public CompoundTag getData() {
        return data;
    }

    private double getGunData(String key) {
        return getGunData(key, 0);
    }

    private double getGunData(String key, double defaultValue) {
        return GunsTool.gunsData.getOrDefault(id, new HashMap<>()).getOrDefault(key, defaultValue);
    }

    public double damage() {
        return getGunData("Damage") + item.getCustomDamage(stack);
    }

    public double explosionDamage() {
        return getGunData("ExplosionDamage");
    }

    public double explosionRadius() {
        return getGunData("ExplosionRadius");
    }

    public double velocity() {
        return getGunData("Velocity") + item.getCustomVelocity(stack);
    }

    public double spread() {
        return getGunData("Spread");
    }

    public int magazine() {
        return (int) (getGunData("Magazine") + item.getCustomMagazine(stack));
    }

    public int projectileAmount() {
        return (int) getGunData("ProjectileAmount", 1);
    }

    public double headshot() {
        return getGunData("Headshot", 1.5) + item.getCustomHeadshot(stack);
    }

    public int normalReloadTime() {
        return (int) getGunData("NormalReloadTime");
    }

    public int emptyReloadTime() {
        return (int) getGunData("EmptyReloadTime");
    }

    public int iterativeTime() {
        return (int) getGunData("IterativeTime");
    }

    public int prepareTime() {
        return (int) getGunData("PrepareTime");
    }

    public int prepareLoadTime() {
        return (int) getGunData("PrepareLoadTime");
    }

    public int prepareEmptyTime() {
        return (int) getGunData("PrepareEmptyTime");
    }

    public int boltActionTime() {
        return (int) getGunData("BoltActionTime") + item.getCustomBoltActionTime(stack);
    }

    public int finishTime() {
        return (int) getGunData("FinishTime");
    }

    public int reloadTime() {
        var normalReload = normalReloadTime();
        var emptyReload = emptyReloadTime();

        if (normalReload == 0) return emptyReload;
        if (emptyReload == 0) return normalReload;

        return getAmmo() < magazine() ? normalReload : emptyReload;
    }

    public double soundRadius() {
        return getGunData("SoundRadius", 15) + item.getCustomSoundRadius(stack);
    }

    public double bypassArmor() {
        return getGunData("BypassesArmor") + item.getCustomBypassArmor(stack);
    }

    public double recoilX() {
        return getGunData("RecoilX");
    }

    public double recoilY() {
        return getGunData("RecoilY");
    }

    public double weight() {
        return getGunData("Weight") + customWeight();
    }

    public double customWeight() {
        return item.getCustomWeight(stack);
    }

    public int getAmmo() {
        return data.getInt("Ammo");
    }

    public void setAmmo(int ammo) {
        data.putInt("Ammo", ammo);
    }

    public boolean isReloading() {
        return data.getBoolean("Reloading");
    }

    public void setReloading(boolean reloading) {
        data.putBoolean("Reloading", reloading);
    }

    public double defaultZoom() {
        return getGunData("DefaultZoom", 1.25);
    }

    public double minZoom() {
        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);
        return scopeType == 3 ? getGunData("MinZoom", 1.25) : 1.25;
    }

    public double maxZoom() {
        int scopeType = GunsTool.getAttachmentType(stack, GunsTool.AttachmentType.SCOPE);
        return scopeType == 3 ? getGunData("MaxZoom", 1) : 114514;
    }

    public double zoom() {
        if (minZoom() == maxZoom()) return defaultZoom();

        return Mth.clamp(defaultZoom() + item.getCustomZoom(stack), minZoom(), maxZoom());
    }

    public int rpm() {
        return (int) (getGunData("RPM") + item.getCustomRPM(stack));
    }

    public int burstAmount() {
        return (int) getGunData("BurstAmount");
    }

    public int getFireMode() {
        if (data.contains("FireMode")) {
            return data.getInt("FireMode");
        }
        return (int) getGunData("FireMode");
    }

    public void setFireMode(int fireMode) {
        data.putInt("FireMode", fireMode);
    }

    public int getLevel() {
        return data.getInt("Level");
    }

    public void setLevel(int level) {
        data.putInt("Level", level);
    }

    public double getExp() {
        return data.getDouble("Exp");
    }

    public void setExp(double exp) {
        data.putDouble("Exp", exp);
    }

    public double getUpgradePoint() {
        return data.getDouble("UpgradePoint");
    }

    public void setUpgradePoint(double upgradePoint) {
        data.putDouble("UpgradePoint", upgradePoint);
    }

    public boolean canAdjustZoom() {
        return item.canAdjustZoom(stack);
    }

    public boolean canSwitchScope() {
        return item.canSwitchScope(stack);
    }
}
