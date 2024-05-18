package net.mcreator.target.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerKillRecord {
    public Player attacker;
    public Entity target;
    public ItemStack stack;
    public boolean headshot;
    public int tick;

    public PlayerKillRecord(Player attacker, Entity target, ItemStack stack, boolean headshot) {
        this.attacker = attacker;
        this.target = target;
        this.stack = stack;
        this.headshot = headshot;
        this.tick = 0;
    }
}
