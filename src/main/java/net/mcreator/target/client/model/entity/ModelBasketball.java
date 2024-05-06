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

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelBasketball<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("target", "modelbasketball"), "main");
    public final ModelPart bone;

    public ModelBasketball(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
        PartDefinition bone2 = bone.addOrReplaceChild("bone2",
                CubeListBuilder.create().texOffs(48, 50).addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 50).addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 50)
                        .addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 50).addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 49)
                        .addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 49).addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 48)
                        .addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 49).addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 49)
                        .addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 48).addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 46)
                        .addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 44).addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 42)
                        .addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 49).addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 49)
                        .addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 49).addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 49)
                        .addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 50).addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 50)
                        .addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 50).addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 50)
                        .addBox(5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(51, 1).addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 51)
                        .addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 50).addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 44)
                        .addBox(1.5F, -1.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 26).addBox(-2.5F, -1.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 44)
                        .addBox(1.5F, -1.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 44).addBox(-2.5F, -1.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 1)
                        .addBox(-6.5F, -1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 45).addBox(-6.5F, -1.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 3)
                        .addBox(5.5F, -1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 5).addBox(5.5F, -1.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 7)
                        .addBox(-3.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 45).addBox(-3.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 9)
                        .addBox(2.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 11).addBox(2.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 13)
                        .addBox(4.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 45).addBox(-5.5F, -1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 15)
                        .addBox(4.5F, -1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 17).addBox(-5.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 19)
                        .addBox(4.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 45).addBox(4.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 21)
                        .addBox(-5.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 23).addBox(-4.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 25)
                        .addBox(-4.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 45).addBox(3.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 27)
                        .addBox(3.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 29).addBox(-5.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 24)
                        .addBox(1.5F, 0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 26).addBox(-2.5F, 0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 28)
                        .addBox(1.5F, 0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 42).addBox(-2.5F, 0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 30)
                        .addBox(-6.5F, 0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 32).addBox(-6.5F, 0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 34)
                        .addBox(5.5F, 0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 42).addBox(5.5F, 0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 36)
                        .addBox(-3.5F, 0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 38).addBox(-3.5F, 0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 40)
                        .addBox(2.5F, 0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 42).addBox(2.5F, 0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 43)
                        .addBox(4.5F, 0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 43).addBox(-5.5F, 0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 43)
                        .addBox(4.5F, 0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 43).addBox(-5.5F, 0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 43)
                        .addBox(4.5F, 0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 43).addBox(4.5F, 0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 43)
                        .addBox(-5.5F, 0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 44).addBox(-4.5F, 0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 44)
                        .addBox(-4.5F, 0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 44).addBox(3.5F, 0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 44)
                        .addBox(3.5F, 0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 44).addBox(-5.5F, 0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(36, 8).addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 11).addBox(0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 33)
                        .addBox(-1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 6).addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 36)
                        .addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 4).addBox(0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 0)
                        .addBox(-1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 35)
                        .addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 29).addBox(5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 29)
                        .addBox(-6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 6).addBox(5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 30)
                        .addBox(-6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 16).addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 8)
                        .addBox(-6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 35).addBox(5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 35)
                        .addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 35).addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 35)
                        .addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 35).addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 34)
                        .addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 34).addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 30)
                        .addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 34).addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 34)
                        .addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 34).addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 34)
                        .addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 33).addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 31)
                        .addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 29).addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 27)
                        .addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 33).addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 25)
                        .addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 23).addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(21, 29).addBox(5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 29).addBox(-6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 30)
                        .addBox(5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 2).addBox(-6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 14)
                        .addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 30).addBox(-6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 20)
                        .addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 48).addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 47)
                        .addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 47).addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 47)
                        .addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 47).addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 47)
                        .addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 47).addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 47)
                        .addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 47).addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 46)
                        .addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 46).addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 46)
                        .addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 46).addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 46)
                        .addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 46).addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 46)
                        .addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 46).addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 43)
                        .addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 41).addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 45)
                        .addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 35).addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 33)
                        .addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 17).addBox(-0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 45)
                        .addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition cube_r3 = bone2.addOrReplaceChild("cube_r3",
                CubeListBuilder.create().texOffs(15, 29).addBox(5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 4)
                        .addBox(5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 0).addBox(-6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 12)
                        .addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 10).addBox(-6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 18)
                        .addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 38).addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 33)
                        .addBox(0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 19).addBox(-1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 38)
                        .addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 38).addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 38)
                        .addBox(0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 38).addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 38)
                        .addBox(-1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 38).addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 37)
                        .addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 37).addBox(5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 37)
                        .addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 37).addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 37)
                        .addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 37).addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 36)
                        .addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 34).addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 32)
                        .addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 30).addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 36)
                        .addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 28).addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 26)
                        .addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 24).addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 36)
                        .addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 22).addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 20)
                        .addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 18).addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 36)
                        .addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition cube_r4 = bone2.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(33, 9).addBox(-0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 21).addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 15)
                        .addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 31).addBox(-5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 30)
                        .addBox(6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 32).addBox(-5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 32)
                        .addBox(6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 3).addBox(-5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 1)
                        .addBox(6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 40).addBox(-4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 38)
                        .addBox(4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 36).addBox(4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 48)
                        .addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 34).addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 32)
                        .addBox(-4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 30).addBox(5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 48)
                        .addBox(5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 28).addBox(-4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 26)
                        .addBox(5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 24).addBox(-4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 48)
                        .addBox(5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 22).addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 20)
                        .addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 18).addBox(-2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 48)
                        .addBox(-2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 16).addBox(6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 14)
                        .addBox(6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 12).addBox(-5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 48)
                        .addBox(-5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 10).addBox(-1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 4)
                        .addBox(2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 2).addBox(-1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 0)
                        .addBox(2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r5 = bone2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(21, 33).addBox(-0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -1.5708F, 1.5708F));
        PartDefinition cube_r6 = bone2.addOrReplaceChild("cube_r6",
                CubeListBuilder.create().texOffs(18, 40).addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 40).addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 40)
                        .addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 39).addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 35)
                        .addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 31).addBox(-6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 31)
                        .addBox(5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 32).addBox(-6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 32)
                        .addBox(5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 7).addBox(-6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 33)
                        .addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 7).addBox(1.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 5)
                        .addBox(1.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 3).addBox(2.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 27)
                        .addBox(2.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 1).addBox(2.5F, -3.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 26)
                        .addBox(1.5F, -3.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 26).addBox(3.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 26)
                        .addBox(3.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 26).addBox(4.5F, -1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 26)
                        .addBox(3.5F, -2.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 25).addBox(3.5F, -2.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 25)
                        .addBox(3.5F, -1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 25).addBox(3.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 25)
                        .addBox(2.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 24).addBox(1.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 22)
                        .addBox(3.5F, -3.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 20).addBox(2.5F, -3.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 18)
                        .addBox(1.5F, -4.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 9).addBox(-3.5F, -3.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 11)
                        .addBox(-3.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 13).addBox(-4.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 27)
                        .addBox(-4.5F, -2.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 15).addBox(-4.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 17)
                        .addBox(-3.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 19).addBox(-3.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 27)
                        .addBox(-4.5F, -1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 27).addBox(-4.5F, -1.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 21)
                        .addBox(-2.5F, -3.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 23).addBox(-2.5F, -3.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 25)
                        .addBox(-2.5F, -2.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 27).addBox(-2.5F, -1.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 28)
                        .addBox(-2.5F, -4.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 28).addBox(-3.5F, -3.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 28)
                        .addBox(-4.5F, -3.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 28).addBox(-4.5F, -2.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 28)
                        .addBox(-5.5F, -1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 30).addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 33)
                        .addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 39).addBox(5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 31)
                        .addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 29).addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 27)
                        .addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 39).addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 25)
                        .addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 23).addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 21)
                        .addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 39).addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 19)
                        .addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 17).addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 15)
                        .addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 39).addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 13)
                        .addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 11).addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 9)
                        .addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 39).addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 7)
                        .addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r7 = bone2.addOrReplaceChild("cube_r7",
                CubeListBuilder.create().texOffs(15, 31).addBox(-6.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 28).addBox(5.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 32)
                        .addBox(-6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 31).addBox(5.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 5)
                        .addBox(-6.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 32).addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 22)
                        .addBox(1.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 13).addBox(-0.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 20)
                        .addBox(-2.5F, -0.5F, 5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 18).addBox(1.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 16)
                        .addBox(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 12).addBox(-2.5F, -0.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 24)
                        .addBox(-6.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 22).addBox(-6.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 42)
                        .addBox(5.5F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 10).addBox(5.5F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 8)
                        .addBox(-3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 6).addBox(-3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 42)
                        .addBox(2.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 4).addBox(2.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 2)
                        .addBox(4.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 0).addBox(-5.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 42)
                        .addBox(4.5F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(39, 41).addBox(-5.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(33, 41)
                        .addBox(4.5F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 41).addBox(4.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 41)
                        .addBox(-5.5F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 41).addBox(-4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 41)
                        .addBox(-4.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 41).addBox(3.5F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 40)
                        .addBox(3.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 40).addBox(-5.5F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r8 = bone2.addOrReplaceChild("cube_r8",
                CubeListBuilder.create().texOffs(9, 7).addBox(1.0F, -2.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 5).addBox(1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3)
                        .addBox(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 9).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 1)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 8).addBox(1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 8)
                        .addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 7).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 6)
                        .addBox(-2.0F, -2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 4).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 2)
                        .addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 0).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 5).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 4)
                        .addBox(1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 3).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 1)
                        .addBox(0.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 2).addBox(1.0F, 1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 14)
                        .addBox(5.0F, -2.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 14).addBox(5.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 13)
                        .addBox(6.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 13).addBox(6.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 12)
                        .addBox(6.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 10).addBox(5.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 8)
                        .addBox(7.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 6).addBox(7.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 12)
                        .addBox(8.0F, -2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 4).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 2)
                        .addBox(7.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 0).addBox(7.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 12)
                        .addBox(7.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 11).addBox(6.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 11)
                        .addBox(5.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 10).addBox(7.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 10)
                        .addBox(6.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 9).addBox(5.0F, 1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.5F, 3.5F, -3.5F, -1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r9 = bone2.addOrReplaceChild("cube_r9",
                CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 17).addBox(-2.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 17)
                        .addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 17).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 16)
                        .addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 16).addBox(-2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 16)
                        .addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 15).addBox(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 13)
                        .addBox(1.0F, -2.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 11).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 9)
                        .addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 15).addBox(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 7)
                        .addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 5).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 3)
                        .addBox(-2.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 15).addBox(0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 1)
                        .addBox(-1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 14).addBox(-2.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 20)
                        .addBox(-6.0F, -2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 20).addBox(-6.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 20)
                        .addBox(-7.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 19).addBox(-7.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 19)
                        .addBox(-7.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 19).addBox(-6.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 18)
                        .addBox(-8.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 16).addBox(-8.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 14)
                        .addBox(-9.0F, -2.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 12).addBox(-8.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 18)
                        .addBox(-8.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 10).addBox(-8.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 8)
                        .addBox(-8.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 6).addBox(-7.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 18)
                        .addBox(-6.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 4).addBox(-8.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 2)
                        .addBox(-7.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 0).addBox(-6.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.5F, 3.5F, 3.5F, 1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10",
                CubeListBuilder.create().texOffs(12, 22).addBox(-2.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 22).addBox(-2.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 22)
                        .addBox(-3.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 21).addBox(-3.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 19)
                        .addBox(-3.5F, -3.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 17).addBox(-2.5F, -3.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 15)
                        .addBox(-4.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 21).addBox(-4.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 13)
                        .addBox(-5.5F, -1.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 11).addBox(-4.5F, -2.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 9)
                        .addBox(-4.5F, -2.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 21).addBox(-4.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 7)
                        .addBox(-4.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 5).addBox(-3.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 3)
                        .addBox(-2.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 21).addBox(-4.5F, -3.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 1)
                        .addBox(-3.5F, -3.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 20).addBox(-2.5F, -4.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 24)
                        .addBox(1.5F, -1.5F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 16).addBox(1.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 14)
                        .addBox(2.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 12).addBox(2.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 24)
                        .addBox(2.5F, -3.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 10).addBox(1.5F, -3.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 8)
                        .addBox(3.5F, -2.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 6).addBox(3.5F, -1.5F, -4.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 24)
                        .addBox(4.5F, -1.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 4).addBox(3.5F, -2.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 2)
                        .addBox(3.5F, -2.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 0).addBox(3.5F, -1.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24)
                        .addBox(3.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 23).addBox(2.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 23)
                        .addBox(1.5F, -3.5F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 23).addBox(3.5F, -3.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 23)
                        .addBox(2.5F, -3.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 22).addBox(1.5F, -4.5F, -2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(45, 31).addBox(5.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
