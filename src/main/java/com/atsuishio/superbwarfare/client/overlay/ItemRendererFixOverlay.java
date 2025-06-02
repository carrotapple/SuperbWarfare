package com.atsuishio.superbwarfare.client.overlay;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class ItemRendererFixOverlay implements IGuiOverlay {

    public static final String ID = Mod.MODID + "_item_renderer_fix";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player entity = gui.getMinecraft().player;

        if (entity != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(-1145.0D, 0.0D, 0.0D);
            gui.getMinecraft().gameRenderer.itemInHandRenderer.renderItem(entity, entity.getMainHandItem(),
                    ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, guiGraphics.pose(), guiGraphics.bufferSource(), 0);
            guiGraphics.pose().popPose();
        }
    }
}
