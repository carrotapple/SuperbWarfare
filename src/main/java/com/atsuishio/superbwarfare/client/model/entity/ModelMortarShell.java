package com.atsuishio.superbwarfare.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.atsuishio.superbwarfare.ModUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelMortarShell<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModUtils.MODID, "modelmortar_shell_converted"), "main");
    public final ModelPart Rockets;

    public ModelMortarShell(ModelPart root) {
        this.Rockets = root.getChild("Rockets");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition Rockets = partdefinition.addOrReplaceChild("Rockets", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -24.7579F, 0.4535F, -1.5708F, 0.0F, 0.0F));
        PartDefinition octagon_r1 = Rockets.addOrReplaceChild("octagon_r1", CubeListBuilder.create().texOffs(6, 3).addBox(-1.3735F, -0.2401F, -1.5325F, 0.7465F, 0.4803F, 1.7605F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.8097F, 0.0033F, 5.8407F, 0.0F, 0.3927F, 0.0F));
        PartDefinition octagon_r2 = Rockets.addOrReplaceChild("octagon_r2", CubeListBuilder.create().texOffs(7, 0).addBox(0.627F, -0.2401F, -1.5325F, 0.7465F, 0.4803F, 1.7605F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.8097F, 0.0033F, 5.8407F, 0.0F, -0.3927F, 0.0F));
        PartDefinition octagon_r3 = Rockets.addOrReplaceChild("octagon_r3", CubeListBuilder.create().texOffs(0, 8).addBox(-0.2401F, 0.627F, -1.4075F, 0.4803F, 0.684F, 1.6355F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.0003F, 6.5823F, -0.3927F, 0.0F, 0.0F));
        PartDefinition octagon_r4 = Rockets.addOrReplaceChild("octagon_r4", CubeListBuilder.create().texOffs(4, 8).addBox(-0.2401F, -1.311F, -1.4075F, 0.4803F, 0.684F, 1.6355F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.0003F, 6.5823F, 0.3927F, 0.0F, 0.0F));
        PartDefinition octagon_r5 = Rockets.addOrReplaceChild("octagon_r5",
                CubeListBuilder.create().texOffs(2, 0).addBox(-0.57F, -0.2361F, -4.1689F, 1.14F, 0.4722F, 2.85F, new CubeDeformation(0.0F)).texOffs(0, 3).addBox(-0.2361F, -0.57F, -4.1689F, 0.4722F, 1.14F, 2.85F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(-0.741F, -0.3069F, -3.4849F, 1.482F, 0.6139F, 1.824F, new CubeDeformation(0.0F)).texOffs(19, 0).addBox(-0.3069F, -0.741F, -3.4849F, 0.6139F, 1.482F, 1.824F, new CubeDeformation(0.0F)).texOffs(13, 13)
                        .addBox(-0.912F, -0.3778F, -2.3449F, 1.824F, 0.7555F, 7.0265F, new CubeDeformation(0.0F)).texOffs(19, 0).addBox(-0.3778F, -0.912F, -2.3449F, 0.7555F, 1.824F, 7.0265F, new CubeDeformation(0.0F)).texOffs(29, 23)
                        .addBox(-0.4486F, -1.083F, -1.3189F, 0.8972F, 2.166F, 5.066F, new CubeDeformation(0.0F)).texOffs(28, 0).addBox(-1.083F, -0.4486F, -1.3189F, 2.166F, 0.8972F, 5.066F, new CubeDeformation(0.0F)).texOffs(29, 30)
                        .addBox(-1.254F, -0.5194F, -0.2929F, 2.508F, 1.0388F, 3.128F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.5194F, -1.254F, -0.2929F, 1.0388F, 2.508F, 3.128F, new CubeDeformation(0.0F)).texOffs(0, 8)
                        .addBox(-1.425F, -0.5902F, 0.7331F, 2.85F, 1.1805F, 1.19F, new CubeDeformation(0.0F)).texOffs(7, 5).addBox(-0.5902F, -1.425F, 0.7331F, 1.1805F, 2.85F, 1.19F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.0003F, -0.8848F, 0.0F, 0.0F, -0.3927F));
        PartDefinition octagon_r6 = Rockets.addOrReplaceChild("octagon_r6",
                CubeListBuilder.create().texOffs(4, 0).addBox(-0.57F, -0.2361F, -4.1689F, 1.14F, 0.4722F, 2.85F, new CubeDeformation(0.0F)).texOffs(0, 4).addBox(-0.2361F, -0.57F, -4.1689F, 0.4722F, 1.14F, 2.85F, new CubeDeformation(0.0F))
                        .texOffs(11, 17).addBox(-0.741F, -0.3069F, -3.4849F, 1.482F, 0.6139F, 1.824F, new CubeDeformation(0.0F)).texOffs(19, 3).addBox(-0.3069F, -0.741F, -3.4849F, 0.6139F, 1.482F, 1.824F, new CubeDeformation(0.0F)).texOffs(0, 15)
                        .addBox(-0.912F, -0.3778F, -2.3449F, 1.824F, 0.7555F, 7.0265F, new CubeDeformation(0.0F)).texOffs(11, 21).addBox(-0.3778F, -0.912F, -2.3449F, 0.7555F, 1.824F, 7.0265F, new CubeDeformation(0.0F)).texOffs(24, 9)
                        .addBox(-1.083F, -0.4486F, -1.3189F, 2.166F, 0.8972F, 5.066F, new CubeDeformation(0.0F)).texOffs(9, 30).addBox(-0.4486F, -1.083F, -1.3189F, 0.8972F, 2.166F, 5.066F, new CubeDeformation(0.0F)).texOffs(0, 31)
                        .addBox(-1.254F, -0.5194F, -0.2929F, 2.508F, 1.0388F, 3.128F, new CubeDeformation(0.0F)).texOffs(33, 6).addBox(-0.5194F, -1.254F, -0.2929F, 1.0388F, 2.508F, 3.128F, new CubeDeformation(0.0F)).texOffs(11, 15)
                        .addBox(-1.425F, -0.5902F, 0.7331F, 2.85F, 1.1805F, 1.19F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-0.5902F, -1.425F, 0.7331F, 1.1805F, 2.85F, 1.19F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.0003F, -0.8848F, 0.0F, 0.0F, 0.3927F));
        PartDefinition group2 = Rockets.addOrReplaceChild("group2", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0003F, 12.3571F));
        PartDefinition group3 = Rockets.addOrReplaceChild("group3", CubeListBuilder.create(), PartPose.offset(0.0F, -9.7972F, 8.7641F));
        PartDefinition octagon_r7 = group3.addOrReplaceChild("octagon_r7", CubeListBuilder.create().texOffs(20, 21).addBox(-0.7434F, -0.308F, 1.7936F, 1.4868F, 0.6158F, 5.7403F, new CubeDeformation(0.0F)).texOffs(28, 15).addBox(-0.3079F, -0.7435F,
                1.7936F, 0.6159F, 1.4868F, 5.7403F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.797F, -9.6489F, 0.0F, 0.0F, -0.3927F));
        PartDefinition octagon_r8 = group3.addOrReplaceChild("octagon_r8",
                CubeListBuilder.create().texOffs(0, 24).addBox(-0.7434F, -0.308F, 1.7936F, 1.4868F, 0.6158F, 5.7403F, new CubeDeformation(0.0F)).texOffs(21, 28).addBox(-0.308F, -0.7435F, 1.7936F, 0.6159F, 1.4868F, 5.7403F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 9.797F, -9.6489F, 0.0F, 0.0F, 0.3927F));
        PartDefinition group4 = Rockets.addOrReplaceChild("group4", CubeListBuilder.create(), PartPose.offset(0.0F, -9.7972F, 8.7641F));
        PartDefinition group = Rockets.addOrReplaceChild("group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2029F, -5.0537F));
        PartDefinition octagon_r9 = group.addOrReplaceChild("octagon_r9",
                CubeListBuilder.create().texOffs(0, 1).addBox(-0.1549F, -0.3739F, -4.905F, 0.3098F, 0.7478F, 1.1522F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.3739F, -0.1549F, -4.905F, 0.7478F, 0.3098F, 1.1522F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.2031F, 4.1689F, 0.0F, 0.0F, -0.3927F));
        PartDefinition octagon_r10 = group.addOrReplaceChild("octagon_r10",
                CubeListBuilder.create().texOffs(8, 8).addBox(-0.1549F, -0.3739F, -4.905F, 0.3098F, 0.7478F, 1.1522F, new CubeDeformation(0.0F)).texOffs(0, 1).addBox(-0.3739F, -0.1549F, -4.905F, 0.7478F, 0.3098F, 1.1522F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.2031F, 4.1689F, 0.0F, 0.0F, 0.3927F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Rockets.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
