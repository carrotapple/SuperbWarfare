package com.atsuishio.superbwarfare.client.screens;

import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.menu.FuMO25Menu;
import com.atsuishio.superbwarfare.tools.SeekTool;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FuMO25ScreenHelper {

    public static BlockPos pos = null;
    public static List<Entity> entities = new ArrayList<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side != LogicalSide.CLIENT) return;
        if (event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        var menu = player.containerMenu;
        if (!(menu instanceof FuMO25Menu fuMO25Menu)) return;
        if (pos == null) return;

        if (fuMO25Menu.getEnergy() <= 0) {
            if (entities != null) {
                entities.clear();
            }
            return;
        }

        var funcType = fuMO25Menu.getFuncType();
        entities = SeekTool.getEntitiesWithinRange(pos, player.level(), funcType == 1 ? FuMO25BlockEntity.MAX_RANGE : FuMO25BlockEntity.DEFAULT_RANGE);
    }

    public static void resetEntities() {
        if (entities != null) {
            entities.clear();
        }
    }
}
