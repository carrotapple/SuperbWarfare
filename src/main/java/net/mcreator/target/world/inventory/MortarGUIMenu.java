package net.mcreator.target.world.inventory;

import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.init.TargetModMenus;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class MortarGUIMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public final static HashMap<String, Object> GUI_STATE = new HashMap<>();
    public final Level world;
    public final Player entity;
    public int x, y, z;
    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public MortarGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(TargetModMenus.MORTAR_GUI.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();
        IItemHandler internal = new ItemStackHandler(0);
        if (extraData != null) {
            BlockPos pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            ContainerLevelAccess access = ContainerLevelAccess.create(world, pos);
        }
        Entity looking = TraceTool.findLookingEntity(entity, 6);
        if (looking == null) return;

        if (GUI_STATE.get("text:pitch") instanceof EditBox box)
            box.setValue((new java.text.DecimalFormat("##").format(-looking.getXRot())));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    public Map<Integer, Slot> get() {
        return customSlots;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player entity = event.player;
        if (event.phase == TickEvent.Phase.END && entity.containerMenu instanceof MortarGUIMenu) {
            Entity looking = TraceTool.findLookingEntity(entity, 6);
            if (looking == null) return;

            String s = GUI_STATE.containsKey("text:pitch") ? ((EditBox) GUI_STATE.get("text:pitch")).getValue() : "0";
            double converted = 0;
            try {
                converted = Double.parseDouble(s);
            } catch (Exception ignored) {
            }

            if (20 <= converted && converted <= 90) {
                ((LivingEntity) looking).getAttribute(TargetModAttributes.MORTAR_PITCH.get()).setBaseValue(converted);
            }
        }
    }
}
