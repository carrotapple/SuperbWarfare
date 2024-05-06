package net.mcreator.target.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelClaymore<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("target", "modelclaymore"), "main");
    public final ModelPart claymore;

    public ModelClaymore(ModelPart root) {
        this.claymore = root.getChild("claymore");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition claymore = partdefinition.addOrReplaceChild("claymore", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bone = claymore.addOrReplaceChild("bone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.75F, -0.5F, 7.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 14).addBox(-2.0F, -8.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 7)
                        .addBox(2.0F, -8.75F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 7).addBox(-3.0F, -8.75F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 7).addBox(-1.5F, -6.0F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.3118F, -1.75F, -0.4598F, 0.0F, 0.3927F, 0.0F));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 7).addBox(0.5F, -6.0F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.6184F, -1.75F, 0.0814F, 0.0F, -0.3927F, 0.0F));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3",
                CubeListBuilder.create().texOffs(12, 13).addBox(0.0F, -0.75F, -0.5F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(6.0F, -0.75F, -0.5F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, -2.6F, 0.3F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(10, 13).addBox(0.0F, -0.5F, -0.25F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 13).addBox(6.0F, -0.5F, -0.25F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, -2.6F, 0.3F, 0.7854F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        claymore.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
