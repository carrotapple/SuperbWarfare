package net.mcreator.superbwarfare.event.modevent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ReloadEvent extends Event {

    public final Player player;
    public final ItemStack stack;

    public ReloadEvent(Player player, ItemStack stack) {
        this.player = player;
        this.stack = stack;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }
}
