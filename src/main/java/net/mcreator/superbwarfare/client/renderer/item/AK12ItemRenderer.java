package net.mcreator.superbwarfare.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.client.layer.AK12Layer;
import net.mcreator.superbwarfare.client.model.item.AK12ItemModel;
import net.mcreator.superbwarfare.item.gun.rifle.AK12Item;
import net.mcreator.superbwarfare.tools.AnimUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashSet;
import java.util.Set;

public class AK12ItemRenderer extends GeoItemRenderer<AK12Item> {
    public AK12ItemRenderer() {
        super(new AK12ItemModel());
        this.addRenderLayer(new AK12Layer(this));
    }

    @Override
    public RenderType getRenderType(AK12Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
    protected boolean renderArms = false;
    protected MultiBufferSource currentBuffer;
    protected RenderType renderType;
    public ItemDisplayContext transformType;
    protected AK12Item animatable;
    private final Set<String> hiddenBones = new HashSet<>();

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
        this.transformType = transformType;
        if (this.animatable != null)
            this.animatable.getTransformType(transformType);
        super.renderByItem(stack, transformType, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
    }

    @Override
    public void actuallyRender(PoseStack matrixStackIn, AK12Item animatable, BakedGeoModel model, RenderType type, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, boolean isRenderer, float partialTicks, int packedLightIn,
                               int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.currentBuffer = renderTypeBuffer;
        this.renderType = type;
        this.animatable = animatable;
        super.actuallyRender(matrixStackIn, animatable, model, type, renderTypeBuffer, vertexBuilder, isRenderer, partialTicks, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (this.renderArms) {
            this.renderArms = false;
        }
    }

    @Override
    public void renderRecursively(PoseStack stack, AK12Item animatable, GeoBone bone, RenderType type, MultiBufferSource buffer, VertexConsumer bufferIn, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, float red,
                                  float green, float blue, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        String name = bone.getName();
        boolean renderingArms = false;
        if (name.equals("Lefthand") || name.equals("Righthand")) {
            bone.setHidden(true);
            renderingArms = true;
        } else {
            bone.setHidden(this.hiddenBones.contains(name));
        }

        Player player_ = Minecraft.getInstance().player;
        if (player_ != null) {
            ItemStack itemStack = player_.getMainHandItem();

            if (name.equals("holo")) {

                bone.setHidden(itemStack.getOrCreateTag().getBoolean("HoloHidden")
                        || GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS
                        || itemStack.getOrCreateTag().getInt("scope_type") != 1);
            }

            if (name.equals("flare")) {
                if (itemStack.getOrCreateTag().getInt("barrel_type") == 1) {
                    bone.setPosZ(2.25f);
                }
                if (itemStack.getOrCreateTag().getDouble("flash_time") == 0 || itemStack.getOrCreateTag().getInt("barrel_type") == 2) {
                    bone.setHidden(true);
                } else {
                    bone.setHidden(false);
                    bone.setScaleX((float) (0.75 + 0.5 * (Math.random() - 0.5)));
                    bone.setScaleY((float) (0.75 + 0.5 * (Math.random() - 0.5)));
                    bone.setRotZ((float) (0.5 * (Math.random() - 0.5)));
                }
            }

            if (name.equals("Scope1")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("scope_type") != 1);
            }

            if (name.equals("Magazine0")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("magazine_type") != 0);
            }

            if (name.equals("Magazine1")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("magazine_type") != 1);
            }

            if (name.equals("Magazine2")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("magazine_type") != 2);
            }

            if (name.equals("Barrel0")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("barrel_type") != 0);
            }

            if (name.equals("Barrel1")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("barrel_type") != 1);
            }

            if (name.equals("Barrel2")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("barrel_type") != 2);
            }

            if (name.equals("Stock0")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("stock_type") != 0);
            }

            if (name.equals("Stock1")) {
                bone.setHidden(itemStack.getOrCreateTag().getInt("stock_type") != 1);
            }

        }


        if (this.transformType.firstPerson() && renderingArms) {
            AbstractClientPlayer player = mc.player;

            if (player == null) {
                return;
            }

            PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
            PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            RenderUtils.translateAwayFromPivotPoint(stack, bone);
            ResourceLocation loc = player.getSkinTextureLocation();
            VertexConsumer armBuilder = this.currentBuffer.getBuffer(RenderType.entitySolid(loc));
            VertexConsumer sleeveBuilder = this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc));
            if (name.equals("Lefthand")) {
                stack.translate(-1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
                AnimUtils.renderPartOverBone(model.leftArm, bone, stack, armBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1);
                AnimUtils.renderPartOverBone(model.leftSleeve, bone, stack, sleeveBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1);
            } else {
                stack.translate(SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
                AnimUtils.renderPartOverBone(model.rightArm, bone, stack, armBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1);
                AnimUtils.renderPartOverBone(model.rightSleeve, bone, stack, sleeveBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1);
            }

            this.currentBuffer.getBuffer(this.renderType);
            stack.popPose();
        }
        super.renderRecursively(stack, animatable, bone, type, buffer, bufferIn, isReRender, partialTick, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public ResourceLocation getTextureLocation(AK12Item instance) {
        return super.getTextureLocation(instance);
    }
}
