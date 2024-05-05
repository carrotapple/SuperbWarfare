package net.mcreator.target.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
public class Modelbocekarrow<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("target", "modelbocekarrow"), "main");
    public final ModelPart jian;

    public Modelbocekarrow(ModelPart root) {
        this.jian = root.getChild("jian");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition jian = partdefinition.addOrReplaceChild("jian", CubeListBuilder.create().texOffs(0, 0).addBox(-0.068F, -0.068F, -12.7575F, 0.1361F, 0.1361F, 25.515F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.0011F, 5.3718F, -0.0975F, -1.5708F, 0.0F, 0.0F));
        PartDefinition bone5 = jian.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -12.0507F));
        PartDefinition cube_r1 = bone5.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.2381F, -0.5783F, 0.068F, 0.2041F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.504F, 0.3134F, 2.0813F, 0.0F, 0.0F));
        PartDefinition cube_r2 = bone5.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.068F, -0.7144F, 0.068F, 0.2041F, 1.1567F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3674F, 0.0F, 0.5105F, 0.0F, 0.0F));
        PartDefinition bone6 = jian.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -12.0507F, 0.0F, 0.0F, 1.5708F));
        PartDefinition cube_r3 = bone6.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.2381F, -0.5783F, 0.068F, 0.2041F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.504F, 0.3134F, 2.0813F, 0.0F, 0.0F));
        PartDefinition cube_r4 = bone6.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.068F, -0.7144F, 0.068F, 0.2041F, 1.1567F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3674F, 0.0F, 0.5105F, 0.0F, 0.0F));
        PartDefinition bone7 = jian.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -12.0507F, 0.0F, 0.0F, -3.1416F));
        PartDefinition cube_r5 = bone7.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.2381F, -0.5783F, 0.068F, 0.2041F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.504F, 0.3134F, 2.0813F, 0.0F, 0.0F));
        PartDefinition cube_r6 = bone7.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.068F, -0.7144F, 0.068F, 0.2041F, 1.1567F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3674F, 0.0F, 0.5105F, 0.0F, 0.0F));
        PartDefinition bone8 = jian.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -12.0507F, 0.0F, 0.0F, -1.5708F));
        PartDefinition cube_r7 = bone8.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.2381F, -0.5783F, 0.068F, 0.2041F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.504F, 0.3134F, 2.0813F, 0.0F, 0.0F));
        PartDefinition cube_r8 = bone8.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.068F, -0.7144F, 0.068F, 0.2041F, 1.1567F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3674F, 0.0F, 0.5105F, 0.0F, 0.0F));
        PartDefinition bone10 = jian.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.5443F, 3.1979F, 0.068F, 0.4763F, 1.3948F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.6545F));
        PartDefinition cube_r9 = bone10.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.5443F, 0.034F, 0.068F, 0.1361F, 0.8845F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.4145F, 2.7282F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r10 = bone10.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.068F, -0.4252F, 0.068F, 0.1361F, 0.6804F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.2337F, 3.0964F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r11 = bone10.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 0.2381F, -0.2211F, 0.068F, 0.1361F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.376F, 3.0032F, 0.5803F, 0.0F, 0.0F));
        PartDefinition bone3 = jian.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.5443F, 3.1979F, 0.068F, 0.4763F, 1.3948F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.6545F, 0.0F, 0.0F, 2.138F));
        PartDefinition cube_r12 = bone3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.1531F, 2.623F, 0.068F, 0.1361F, 0.8845F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.4145F, -0.3677F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r13 = bone3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.6294F, 2.1637F, 0.068F, 0.1361F, 0.6804F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.2337F, 0.0006F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r14 = bone3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.9356F, 2.3679F, 0.068F, 0.1361F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.376F, -0.0927F, 0.5803F, 0.0F, 0.0F));
        PartDefinition bone4 = jian.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, -0.5443F, 3.1979F, 0.068F, 0.4763F, 1.3948F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.6545F, 0.0F, 0.0F, -2.0944F));
        PartDefinition cube_r15 = bone4.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.1531F, 2.623F, 0.068F, 0.1361F, 0.8845F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.4145F, -0.3677F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r16 = bone4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.6294F, 2.1637F, 0.068F, 0.1361F, 0.6804F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.2337F, 0.0006F, 0.5803F, 0.0F, 0.0F));
        PartDefinition cube_r17 = bone4.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-0.034F, 1.9356F, 2.3679F, 0.068F, 0.1361F, 0.4763F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.376F, -0.0927F, 0.5803F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        jian.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
