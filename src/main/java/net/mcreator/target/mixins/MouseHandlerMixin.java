package net.mcreator.target.mixins;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @ModifyVariable(method = "turnPlayer()V", at = @At(value = "STORE", opcode = Opcodes.DSTORE), ordinal = 2)
    private double sensitivity(double original) {
        float additionalAdsSensitivity = 1.0F;
        Minecraft mc = Minecraft.getInstance();
        boolean flag = false;
        float sens = 0.13f;
        float fov = 0;
        
        if (mc.player != null && !mc.player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON) {

        	Player player = Minecraft.getInstance().player;
            ItemStack stack = mc.player.getMainHandItem();
            
            fov = ((float)player.getPersistentData().getDouble("fov"));
            
                if (stack.is(ItemTags.create(new ResourceLocation("target:gun")))) {
                    float modifier = 1.5f * fov / 90;
                    additionalAdsSensitivity = Mth.clamp(1.0F - (1.0F / modifier) / 10F, 0.0F, 1.0F);
                    flag = true;
        		}
        }
        return original * additionalAdsSensitivity * (1.0 - sens * (flag ? 1 : 0));
    }
}
