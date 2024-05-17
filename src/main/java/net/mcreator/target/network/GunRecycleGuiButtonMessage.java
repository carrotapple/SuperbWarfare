package net.mcreator.target.network;

import net.mcreator.target.init.TargetModItems;
import net.mcreator.target.init.TargetModTags;
import net.mcreator.target.world.inventory.GunRecycleGuiMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GunRecycleGuiButtonMessage {
    private final int buttonID, x, y, z;

    public GunRecycleGuiButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    public GunRecycleGuiButtonMessage(int buttonID, int x, int y, int z) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void buffer(GunRecycleGuiButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static void handler(GunRecycleGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            int buttonID = message.buttonID;
            int x = message.x;
            int y = message.y;
            int z = message.z;
            if (player == null) return;
            handleButtonAction(player, buttonID, x, y, z);
        });
        context.setPacketHandled(true);
    }

    public static void handleButtonAction(Player player, int buttonID, int x, int y, int z) {
        // security measure to prevent arbitrary chunk generation
        if (!player.level().hasChunkAt(new BlockPos(x, y, z))) return;

        if (buttonID == 0) {
            dismantleGun(player);
        }
    }

    private static void dismantleGun(Player player) {
        var menu = ((GunRecycleGuiMenu) player.containerMenu).get();
        var slot0 = menu.get(0);
        var gun = slot0.getItem();

        if (gun.is(TargetModTags.Items.GUN)) {
            // 普通稀有度
            var material = switch (gun.getRarity()) {
                case COMMON -> Items.IRON_INGOT;
                case RARE -> TargetModItems.INGOT_STEEL.get();
                case EPIC -> TargetModItems.CEMENTED_CARBIDE_INGOT.get();
                default -> null;
            };
            if (material != null) ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(material));

            // 特殊稀有度
            if (gun.is(TargetModTags.Items.LEGENDARY_GUN)) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.NETHERITE_INGOT));
            }
            if (gun.is(TargetModTags.Items.SPECIAL_GUN)) {
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TargetModItems.SOUL_STEEL_INGOT.get()));
            }

            // 高等级额外奖励
            int level = gun.getOrCreateTag().getInt("level");
            if (level >= 10) {
                var soulSteelNuggetCount = 0;

                if (Math.random() < 0.005 * level) soulSteelNuggetCount += 3;
                if (Math.random() < 0.01 * level) soulSteelNuggetCount += 2;
                if (Math.random() < 0.03 * level) soulSteelNuggetCount++;
                if (Math.random() < 0.06 * level) soulSteelNuggetCount++;

                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TargetModItems.SOUL_STEEL_NUGGET.get(), soulSteelNuggetCount));
            }

            slot0.set(ItemStack.EMPTY);
            player.containerMenu.broadcastChanges();
        }
    }
}
