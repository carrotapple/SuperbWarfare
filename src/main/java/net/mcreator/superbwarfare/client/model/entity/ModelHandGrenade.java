package net.mcreator.superbwarfare.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.superbwarfare.ModUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("unused")
public class ModelHandGrenade<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModUtils.MODID, "model_hand_grenade"), "main");
    public final ModelPart group;

    public ModelHandGrenade(ModelPart root) {
        this.group = root.getChild("group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(6, 8).addBox(-0.4375F, 0.424F, -1.0258F, 0.875F, 0.3F, 1.075F, new CubeDeformation(0.0F))
                .texOffs(11, 13).addBox(-0.45F, -0.4283F, -1.0883F, 0.9F, 0.85F, 1.35F, new CubeDeformation(0.0F))
                .texOffs(13, 14).addBox(-0.45F, -0.4283F, 0.2617F, 0.9F, 0.85F, 0.3F, new CubeDeformation(0.0F))
                .texOffs(12, 15).addBox(-0.45F, 0.0717F, 0.5617F, 0.9F, 0.35F, 0.3F, new CubeDeformation(0.0F))
                .texOffs(11, 14).addBox(-0.45F, -0.2283F, 0.5617F, 0.9F, 0.3F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(9, 13).addBox(-0.5F, -0.6283F, -1.5883F, 1.0F, 0.2F, 2.2F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.0217F, 0.4883F));

        PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(11, 14).addBox(-1.5F, -0.2F, 1.5F, 1.0F, 1.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.207F, -0.8823F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r2 = group.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 14).addBox(-1.45F, -1.3391F, 0.4F, 0.9F, 0.9391F, 0.3508F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.9443F, -1.3048F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bone2 = group.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0295F, -26.2164F));

        PartDefinition bone178_r1 = bone2.addOrReplaceChild("bone178_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-1.2976F, -0.5375F, -0.5375F, 2.5953F, 1.075F, 1.075F, new CubeDeformation(0.0F))
                .texOffs(10, 0).addBox(-0.5375F, -1.2976F, -0.5375F, 1.075F, 2.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0078F, 25.7281F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone3 = group.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5375F, -1.3054F, 25.1906F, 1.075F, 2.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0295F, -26.2164F));

        PartDefinition bone179_r1 = bone3.addOrReplaceChild("bone179_r1", CubeListBuilder.create().texOffs(5, 1).addBox(-0.5375F, -0.5375F, -1.2976F, 1.075F, 1.075F, 2.5953F, new CubeDeformation(0.0F))
                .texOffs(7, 9).addBox(-0.5375F, -1.2976F, -0.5375F, 1.075F, 2.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0078F, 25.7281F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone175 = group.addOrReplaceChild("bone175", CubeListBuilder.create().texOffs(7, 7).addBox(-1.2976F, -0.5453F, 25.1906F, 2.5953F, 1.075F, 1.075F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-0.5375F, -0.5453F, 24.4305F, 1.075F, 1.075F, 2.5953F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0295F, -26.2164F));

        PartDefinition bone180_r1 = bone175.addOrReplaceChild("bone180_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5375F, -0.5375F, -1.2976F, 1.075F, 1.075F, 2.5953F, new CubeDeformation(0.0F))
                .texOffs(5, 5).addBox(-1.2976F, -0.5375F, -0.5375F, 2.5953F, 1.075F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0078F, 25.7281F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}