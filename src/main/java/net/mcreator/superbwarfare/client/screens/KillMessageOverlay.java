package net.mcreator.superbwarfare.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.event.KillMessageHandler;
import net.mcreator.superbwarfare.init.ModDamageTypes;
import net.mcreator.superbwarfare.init.ModItems;
import net.mcreator.superbwarfare.item.gun.GunItem;
import net.mcreator.superbwarfare.tools.DamageTypeTool;
import net.mcreator.superbwarfare.tools.PlayerKillRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicReference;

import static net.mcreator.superbwarfare.tools.RenderTool.preciseBlit;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KillMessageOverlay {
    private static final ResourceLocation HEADSHOT = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/headshot.png");

    private static final ResourceLocation KNIFE = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/knife.png");
    private static final ResourceLocation EXPLOSION = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/explosion.png");
    private static final ResourceLocation CLAYMORE = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/claymore.png");
    private static final ResourceLocation GENERIC = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/generic.png");
    private static final ResourceLocation BEAST = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/beast.png");
    private static final ResourceLocation BLEEDING = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/bleeding.png");
    private static final ResourceLocation SHOCK = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/shock.png");
    private static final ResourceLocation BLOOD_CRYSTAL = new ResourceLocation(ModUtils.MODID, "textures/screens/damage_types/blood_crystal.png");

    private static final ResourceLocation WORLD_PEACE_STAFF = new ResourceLocation(ModUtils.MODID, "textures/gun_icon/compat/world_peace_staff.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (KillMessageHandler.QUEUE.isEmpty()) {
            return;
        }

        float totalTop = 5;

        var arr = KillMessageHandler.QUEUE.toArray(new PlayerKillRecord[0]);
        var record = arr[0];

        if (record.freeze) {
            for (var playerKillRecord : arr) {
                playerKillRecord.freeze = false;
            }
        }

        if (record.tick >= 80) {
            if (arr.length > 1 && record.tick - arr[1].tick < (record.fastRemove ? 2 : 20)) {
                arr[1].fastRemove = true;
                record.fastRemove = true;
                for (int j = 1; j < arr.length; j++) {
                    arr[j].freeze = true;
                }
            }
        }

        for (PlayerKillRecord r : KillMessageHandler.QUEUE) {
            totalTop = renderKillMessages(r, event, totalTop);
        }
    }

    private static float renderKillMessages(PlayerKillRecord record, RenderGuiEvent.Pre event, float baseTop) {
        int w = event.getWindow().getGuiScaledWidth();
        float top = baseTop;

        Font font = Minecraft.getInstance().font;

        AtomicReference<String> targetName = new AtomicReference<>(record.target.getDisplayName().getString());
        if (record.target instanceof Player targetPlayer) {
            CuriosApi.getCuriosInventory(targetPlayer).ifPresent(
                    c -> c.findFirstCurio(ModItems.DOG_TAG.get()).ifPresent(
                            s -> {
                                if (s.stack().hasCustomHoverName()) {
                                    targetName.set(s.stack().getHoverName().getString());
                                }
                            }
                    )
            );
        }

        int targetNameWidth = font.width(targetName.get());

        var gui = event.getGuiGraphics();
        gui.pose().pushPose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE
        );

        // 入场效果
        if (record.tick < 3) {
            gui.pose().translate((3 - record.tick - event.getPartialTick()) * 33, 0, 0);
        }

        // 4s后开始消失
        if (record.tick >= 80) {
            int animationTickCount = record.fastRemove ? 2 : 20;
            float rate = (float) Math.pow((record.tick + event.getPartialTick() - 80) / animationTickCount, 5);
            gui.pose().translate(rate * 100, 0, 0);
            gui.setColor(1, 1, 1, 1 - rate);
            baseTop += 10 * (1 - rate);
        } else {
            baseTop += 10;
        }

        // 击杀提示是右对齐的，这里从右向左渲染

        // 渲染被击杀者名称
        gui.drawString(
                Minecraft.getInstance().font,
                targetName.get(),
                w - targetNameWidth - 10f,
                top,
                record.target.getTeamColor(),
                false
        );

        // 第一个图标：爆头/爆炸/近战等图标
        int damageTypeIconW = w - targetNameWidth - 28;

        ResourceLocation damageTypeIcon = getDamageTypeIcon(record);

        if (damageTypeIcon != null) {
            preciseBlit(gui,
                    damageTypeIcon,
                    damageTypeIconW,
                    top - 2,
                    0,
                    0,
                    12,
                    12,
                    12,
                    12
            );
        }

        boolean renderItem = false;
        int itemIconW = damageTypeIcon != null ? w - targetNameWidth - 64 : w - targetNameWidth - 46;
        // 如果是枪械击杀，则渲染枪械图标
        if (record.stack.getItem() instanceof GunItem gunItem) {
            renderItem = true;

            ResourceLocation resourceLocation = gunItem.getGunIcon();

            preciseBlit(gui,
                    resourceLocation,
                    itemIconW,
                    top,
                    0,
                    0,
                    32,
                    8,
                    -32,
                    8
            );
        }

        // TODO 如果是特殊武器击杀，则渲染对应图标
        if (record.stack.getItem().getDescriptionId().equals("item.dreamaticvoyage.world_peace_staff")) {
            renderItem = true;

            preciseBlit(gui,
                    WORLD_PEACE_STAFF,
                    itemIconW,
                    top,
                    0,
                    0,
                    32,
                    8,
                    32,
                    8
            );
        }

        // 渲染击杀者名称
        AtomicReference<String> attackerName = new AtomicReference<>(record.attacker.getDisplayName().getString());
        CuriosApi.getCuriosInventory(record.attacker).ifPresent(
                c -> c.findFirstCurio(ModItems.DOG_TAG.get()).ifPresent(
                        s -> {
                            if (s.stack().hasCustomHoverName()) {
                                attackerName.set(s.stack().getHoverName().getString());
                            }
                        }
                )
        );

        int attackerNameWidth = font.width(attackerName.get());
        int nameW = w - targetNameWidth - 16 - attackerNameWidth;
        if (renderItem) {
            nameW -= 32;
        }
        if (damageTypeIcon != null) {
            nameW -= 18;
        }

        gui.drawString(
                Minecraft.getInstance().font,
                attackerName.get(),
                nameW,
                top,
                record.attacker.getTeamColor(),
                false
        );

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        gui.setColor(1, 1, 1, 1);
        gui.pose().popPose();

        return baseTop;
    }

    @Nullable
    private static ResourceLocation getDamageTypeIcon(PlayerKillRecord record) {
        ResourceLocation icon;
        // 渲染爆头图标
        if (record.headshot) {
            icon = HEADSHOT;
        } else {
            if (DamageTypeTool.isArrowDamage(record.damageType) || DamageTypeTool.isGunDamage(record.damageType)) {
                icon = null;
            } else {
                // 如果是其他伤害，则渲染对应图标
                if (record.damageType == DamageTypes.EXPLOSION || record.damageType == DamageTypes.PLAYER_EXPLOSION || record.damageType == ModDamageTypes.PROJECTILE_BOOM) {
                    icon = EXPLOSION;
                } else if (record.damageType == DamageTypes.PLAYER_ATTACK) {
                    icon = KNIFE;
                } else if (record.damageType == ModDamageTypes.BEAST) {
                    icon = BEAST;
                } else if (record.damageType == ModDamageTypes.MINE) {
                    icon = CLAYMORE;
                } else if (record.damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("dreamaticvoyage", "bleeding"))) {
                    icon = BLEEDING;
                } else if (record.damageType == ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("dreamaticvoyage", "blood_crystal"))) {
                    icon = BLOOD_CRYSTAL;
                } else if (record.damageType == ModDamageTypes.SHOCK) {
                    icon = SHOCK;
                } else {
                    icon = GENERIC;
                }
            }
        }
        return icon;
    }
}
