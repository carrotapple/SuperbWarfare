package com.atsuishio.superbwarfare.compat.tacz;

import com.atsuishio.superbwarfare.client.RenderHelper;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.resource.GunDisplayInstance;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TACZGunEventHandler {

    public static void entityHurtByTACZGun(EntityHurtByGunEvent.Pre event) {
        if (event.getHurtEntity() instanceof VehicleEntity) {
            event.setHeadshot(false);
        }
    }

    public static boolean taczCompatRender(ItemStack stack, GuiGraphics gui, int itemIconW, float top) {
        if (stack.getItem() instanceof IGun iGun) {
            ResourceLocation gunId = iGun.getGunId(stack);
            GunData gunData = TimelessAPI.getClientGunIndex(gunId).map(ClientGunIndex::getGunData).orElse(null);
            GunDisplayInstance display = TimelessAPI.getGunDisplay(stack).orElse(null);
            if (gunData != null && display != null) {
                ResourceLocation resourceLocation = display.getHUDTexture();

                RenderHelper.preciseBlit(gui,
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

                return true;
            }
        }
        return false;
    }
}
