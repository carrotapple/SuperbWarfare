package com.atsuishio.superbwarfare.network;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class PlayerVariable {

    public Map<Ammo, Integer> ammo = new HashMap<>();
    public boolean tacticalSprint = false;
    public boolean edit = false;

    public void sync(Entity entity) {
        if (entity instanceof ServerPlayer player)
            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new PlayerVariablesSyncMessage(this, entity.getId()));
    }

    public Tag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (var type : Ammo.values()) {
            type.set(nbt, type.get(this));
        }

        nbt.putBoolean("TacticalSprint", tacticalSprint);
        nbt.putBoolean("EditMode", edit);

        return nbt;
    }

    public void readNBT(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;
        for (var type : Ammo.values()) {
            type.set(this, type.get(nbt));
        }

        tacticalSprint = nbt.getBoolean("TacticalSprint");
        edit = nbt.getBoolean("EditMode");
    }
}
