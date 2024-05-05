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
public class Modelrpg7_rocket_Converted<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("target", "modelrpg_7_rocket_converted"), "main");
    public final ModelPart Rockets;

    public Modelrpg7_rocket_Converted(ModelPart root) {
        this.Rockets = root.getChild("Rockets");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition Rockets = partdefinition.addOrReplaceChild("Rockets", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -24.7579F, 0.4535F, -1.5708F, 0.0F, 0.0F));
        PartDefinition Rockets2 = Rockets.addOrReplaceChild("Rockets2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.4579F, -0.0535F));
        PartDefinition bone23 = Rockets2.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -10.1988F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -1.0385F));
        PartDefinition octagon_r1 = bone23.addOrReplaceChild("octagon_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r2 = bone23.addOrReplaceChild("octagon_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r3 = bone23.addOrReplaceChild("octagon_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r4 = bone23.addOrReplaceChild("octagon_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r5 = bone23.addOrReplaceChild("octagon_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r6 = bone23.addOrReplaceChild("octagon_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r7 = bone23.addOrReplaceChild("octagon_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4007F, -0.9673F, -16.1742F, 0.8014F, 1.0084F, 10.3003F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.9754F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone24 = Rockets2.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -0.2217F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -10.1912F));
        PartDefinition octagon_r8 = bone24.addOrReplaceChild("octagon_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r9 = bone24.addOrReplaceChild("octagon_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r10 = bone24.addOrReplaceChild("octagon_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r11 = bone24.addOrReplaceChild("octagon_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r12 = bone24.addOrReplaceChild("octagon_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r13 = bone24.addOrReplaceChild("octagon_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r14 = bone24.addOrReplaceChild("octagon_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5521F, -1.333F, -21.1812F, 1.1043F, 1.3896F, 1.2137F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 20.9595F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone34 = Rockets2.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, 0.3534F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -10.312F));
        PartDefinition octagon_r15 = bone34.addOrReplaceChild("octagon_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r16 = bone34.addOrReplaceChild("octagon_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r17 = bone34.addOrReplaceChild("octagon_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r18 = bone34.addOrReplaceChild("octagon_r18", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r19 = bone34.addOrReplaceChild("octagon_r19", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r20 = bone34.addOrReplaceChild("octagon_r20", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r21 = bone34.addOrReplaceChild("octagon_r21", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6331F, -1.5284F, -23.6787F, 1.2662F, 1.5933F, 0.784F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 24.0321F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone26 = Rockets2.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -1.5592F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -9.2513F));
        PartDefinition octagon_r22 = bone26.addOrReplaceChild("octagon_r22", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r23 = bone26.addOrReplaceChild("octagon_r23", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r24 = bone26.addOrReplaceChild("octagon_r24", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r25 = bone26.addOrReplaceChild("octagon_r25", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r26 = bone26.addOrReplaceChild("octagon_r26", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r27 = bone26.addOrReplaceChild("octagon_r27", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r28 = bone26.addOrReplaceChild("octagon_r28", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4638F, -1.1197F, -19.1652F, 0.9276F, 1.1673F, 0.9359F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.606F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone19 = Rockets2.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, 0.9259F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -6.0195F));
        PartDefinition octagon_r29 = bone19.addOrReplaceChild("octagon_r29", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r30 = bone19.addOrReplaceChild("octagon_r30", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r31 = bone19.addOrReplaceChild("octagon_r31", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r32 = bone19.addOrReplaceChild("octagon_r32", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r33 = bone19.addOrReplaceChild("octagon_r33", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r34 = bone19.addOrReplaceChild("octagon_r34", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r35 = bone19.addOrReplaceChild("octagon_r35", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6472F, -1.5624F, -13.8248F, 1.2943F, 1.6287F, 3.5402F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 14.7507F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone33 = bone19.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -2.4449F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0285F));
        PartDefinition octagon_r36 = bone33.addOrReplaceChild("octagon_r36", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r37 = bone33.addOrReplaceChild("octagon_r37", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r38 = bone33.addOrReplaceChild("octagon_r38", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r39 = bone33.addOrReplaceChild("octagon_r39", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r40 = bone33.addOrReplaceChild("octagon_r40", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r41 = bone33.addOrReplaceChild("octagon_r41", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, -1.5708F));
        PartDefinition octagon_r42 = bone33.addOrReplaceChild("octagon_r42", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6913F, -1.6689F, -19.8031F, 1.3826F, 1.7398F, 4.5899F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 17.3581F, 0.0F, 0.0F, 0.7854F));
        PartDefinition bone20 = Rockets2.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.2F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, 2.8992F));
        PartDefinition octagon_r43 = bone20.addOrReplaceChild("octagon_r43", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r44 = bone20.addOrReplaceChild("octagon_r44", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r45 = bone20.addOrReplaceChild("octagon_r45", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r46 = bone20.addOrReplaceChild("octagon_r46", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r47 = bone20.addOrReplaceChild("octagon_r47", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r48 = bone20.addOrReplaceChild("octagon_r48", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r49 = bone20.addOrReplaceChild("octagon_r49", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3182F, -0.7683F, -1.313F, 0.6365F, 0.801F, 1.2F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.113F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone22 = Rockets2.addOrReplaceChild("bone22", CubeListBuilder.create(), PartPose.offset(0.0268F, 0.0407F, 3.383F));
        PartDefinition octagon_r50 = bone22.addOrReplaceChild("octagon_r50", CubeListBuilder.create().texOffs(0, 0).addBox(0.3224F, -0.7931F, -0.5685F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, -0.7854F, 0.0F, -0.7854F));
        PartDefinition octagon_r51 = bone22.addOrReplaceChild("octagon_r51", CubeListBuilder.create().texOffs(0, 0).addBox(0.3224F, -0.7982F, -0.4228F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, 0.4494F, 0.0F, -0.7854F));
        PartDefinition octagon_r52 = bone22.addOrReplaceChild("octagon_r52", CubeListBuilder.create().texOffs(0, 0).addBox(0.0278F, -0.5846F, -0.5258F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, 0.4494F, 0.0F, -1.5708F));
        PartDefinition octagon_r53 = bone22.addOrReplaceChild("octagon_r53", CubeListBuilder.create().texOffs(0, 0).addBox(0.0278F, -0.6255F, -0.4008F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, -0.7854F, 0.0F, -1.5708F));
        PartDefinition octagon_r54 = bone22.addOrReplaceChild("octagon_r54", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3482F, -0.6542F, -0.4296F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, -0.7854F, 0.0F, -2.3562F));
        PartDefinition octagon_r55 = bone22.addOrReplaceChild("octagon_r55", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3482F, -0.6213F, -0.5081F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, 0.4494F, 0.0F, -2.3562F));
        PartDefinition octagon_r56 = bone22.addOrReplaceChild("octagon_r56", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5852F, -0.8866F, -0.3801F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, 0.4494F, 0.0F, -3.1416F));
        PartDefinition octagon_r57 = bone22.addOrReplaceChild("octagon_r57", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5852F, -0.8626F, -0.6379F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4742F, 0.1389F, -0.9306F, -0.7854F, 0.0F, 3.1416F));
        PartDefinition octagon_r58 = bone22.addOrReplaceChild("octagon_r58", CubeListBuilder.create().texOffs(0, 0).addBox(0.1884F, -0.4137F, -0.1891F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.7589F, 0.3353F, -0.9306F, -0.7854F, 0.0F, 2.3562F));
        PartDefinition octagon_r59 = bone22.addOrReplaceChild("octagon_r59", CubeListBuilder.create().texOffs(0, 0).addBox(0.1884F, -0.3149F, -0.6559F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.7589F, 0.3353F, -0.9306F, 0.4494F, 0.0F, 2.3562F));
        PartDefinition octagon_r60 = bone22.addOrReplaceChild("octagon_r60", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4464F, -0.3282F, -0.6495F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.7589F, 0.3353F, -0.9306F, 0.4494F, 0.0F, 1.5708F));
        PartDefinition octagon_r61 = bone22.addOrReplaceChild("octagon_r61", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4464F, -0.4242F, -0.1995F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.7589F, 0.3353F, -0.9306F, -0.7854F, 0.0F, 1.5708F));
        PartDefinition octagon_r62 = bone22.addOrReplaceChild("octagon_r62", CubeListBuilder.create().texOffs(0, 0).addBox(0.1884F, -0.4137F, -0.1891F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.3353F, -0.7589F, -0.9306F, -0.7854F, 0.0F, 0.7854F));
        PartDefinition octagon_r63 = bone22.addOrReplaceChild("octagon_r63", CubeListBuilder.create().texOffs(0, 0).addBox(0.1884F, -0.3149F, -0.6559F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.3353F, -0.7589F, -0.9306F, 0.4494F, 0.0F, 0.7854F));
        PartDefinition octagon_r64 = bone22.addOrReplaceChild("octagon_r64", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4464F, -0.3282F, -0.6495F, 0.2221F, 0.4016F, 0.9842F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.3353F, -0.7589F, -0.9306F, 0.4494F, 0.0F, 0.0F));
        PartDefinition octagon_r65 = bone22.addOrReplaceChild("octagon_r65", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4464F, -0.4242F, -0.1995F, 0.2221F, 0.3016F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.3353F, -0.7589F, -0.9306F, -0.7854F, 0.0F, 0.0F));
        PartDefinition bone21 = Rockets2.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, 2.6961F));
        PartDefinition octagon_r66 = bone21.addOrReplaceChild("octagon_r66", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r67 = bone21.addOrReplaceChild("octagon_r67", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r68 = bone21.addOrReplaceChild("octagon_r68", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r69 = bone21.addOrReplaceChild("octagon_r69", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r70 = bone21.addOrReplaceChild("octagon_r70", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r71 = bone21.addOrReplaceChild("octagon_r71", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r72 = bone21.addOrReplaceChild("octagon_r72", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1111F, -0.91F, -0.205F, 0.2221F, 0.1484F, 0.4099F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone18 = Rockets2.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2378F, -0.6508F, -9.1073F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)), PartPose.offset(-0.0021F, 0.0474F, 7.8333F));
        PartDefinition octagon_r73 = bone18.addOrReplaceChild("octagon_r73", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r74 = bone18.addOrReplaceChild("octagon_r74", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r75 = bone18.addOrReplaceChild("octagon_r75", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r76 = bone18.addOrReplaceChild("octagon_r76", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r77 = bone18.addOrReplaceChild("octagon_r77", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r78 = bone18.addOrReplaceChild("octagon_r78", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r79 = bone18.addOrReplaceChild("octagon_r79", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2668F, -0.6441F, -2.0088F, 0.5336F, 0.6715F, 14.8775F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.029F, -0.0067F, -7.0986F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone25 = Rockets2.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 0.2985F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -12.411F));
        PartDefinition octagon_r80 = bone25.addOrReplaceChild("octagon_r80", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r81 = bone25.addOrReplaceChild("octagon_r81", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r82 = bone25.addOrReplaceChild("octagon_r82", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r83 = bone25.addOrReplaceChild("octagon_r83", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r84 = bone25.addOrReplaceChild("octagon_r84", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r85 = bone25.addOrReplaceChild("octagon_r85", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r86 = bone25.addOrReplaceChild("octagon_r86", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3281F, -0.792F, 13.5543F, 0.6561F, 0.8257F, 1.2092F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -13.2557F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone30 = Rockets2.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, -3.9858F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -18.5853F));
        PartDefinition octagon_r87 = bone30.addOrReplaceChild("octagon_r87", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r88 = bone30.addOrReplaceChild("octagon_r88", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r89 = bone30.addOrReplaceChild("octagon_r89", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r90 = bone30.addOrReplaceChild("octagon_r90", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r91 = bone30.addOrReplaceChild("octagon_r91", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r92 = bone30.addOrReplaceChild("octagon_r92", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r93 = bone30.addOrReplaceChild("octagon_r93", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2953F, -0.7128F, 7.8443F, 0.5905F, 0.7431F, 5.4076F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.8302F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone31 = Rockets2.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 1.3934F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -25.0853F));
        PartDefinition octagon_r94 = bone31.addOrReplaceChild("octagon_r94", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r95 = bone31.addOrReplaceChild("octagon_r95", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r96 = bone31.addOrReplaceChild("octagon_r96", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r97 = bone31.addOrReplaceChild("octagon_r97", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r98 = bone31.addOrReplaceChild("octagon_r98", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r99 = bone31.addOrReplaceChild("octagon_r99", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r100 = bone31.addOrReplaceChild("octagon_r100", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 12.0737F, 0.5551F, 0.6985F, 0.3832F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.6803F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone32 = Rockets2.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, -0.4736F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -24.5343F));
        PartDefinition octagon_r101 = bone32.addOrReplaceChild("octagon_r101", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r102 = bone32.addOrReplaceChild("octagon_r102", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r103 = bone32.addOrReplaceChild("octagon_r103", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r104 = bone32.addOrReplaceChild("octagon_r104", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r105 = bone32.addOrReplaceChild("octagon_r105", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r106 = bone32.addOrReplaceChild("octagon_r106", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r107 = bone32.addOrReplaceChild("octagon_r107", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2775F, -0.6701F, 11.5097F, 0.5551F, 0.6985F, 0.9472F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -11.9832F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone29 = Rockets2.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, -0.5789F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -12.2801F));
        PartDefinition octagon_r108 = bone29.addOrReplaceChild("octagon_r108", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r109 = bone29.addOrReplaceChild("octagon_r109", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r110 = bone29.addOrReplaceChild("octagon_r110", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r111 = bone29.addOrReplaceChild("octagon_r111", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r112 = bone29.addOrReplaceChild("octagon_r112", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r113 = bone29.addOrReplaceChild("octagon_r113", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r114 = bone29.addOrReplaceChild("octagon_r114", CubeListBuilder.create().texOffs(0, 0).addBox(-0.2552F, -0.6161F, 9.59F, 0.5104F, 0.6423F, 1.104F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -10.169F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone28 = Rockets2.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, -3.0583F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)), PartPose.offset(0.0268F, 0.0407F, -19.1553F));
        PartDefinition octagon_r115 = bone28.addOrReplaceChild("octagon_r115", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r116 = bone28.addOrReplaceChild("octagon_r116", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r117 = bone28.addOrReplaceChild("octagon_r117", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r118 = bone28.addOrReplaceChild("octagon_r118", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r119 = bone28.addOrReplaceChild("octagon_r119", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r120 = bone28.addOrReplaceChild("octagon_r120", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r121 = bone28.addOrReplaceChild("octagon_r121", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3543F, -0.8554F, 11.3379F, 0.7086F, 0.8917F, 4.6066F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -14.3962F, 0.0F, 0.0F, -1.5708F));
        PartDefinition bone27 = Rockets2.addOrReplaceChild("bone27",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2342F, -0.5653F, -4.8664F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2014F, -0.4862F, -11.3624F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0268F, 0.0407F, -13.0786F));
        PartDefinition octagon_r122 = bone27.addOrReplaceChild("octagon_r122",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, 0.7854F));
        PartDefinition octagon_r123 = bone27.addOrReplaceChild("octagon_r123",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, 2.3562F));
        PartDefinition octagon_r124 = bone27.addOrReplaceChild("octagon_r124",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, 1.5708F));
        PartDefinition octagon_r125 = bone27.addOrReplaceChild("octagon_r125",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, 3.1416F));
        PartDefinition octagon_r126 = bone27.addOrReplaceChild("octagon_r126",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, -2.3562F));
        PartDefinition octagon_r127 = bone27.addOrReplaceChild("octagon_r127",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, -0.7854F));
        PartDefinition octagon_r128 = bone27.addOrReplaceChild("octagon_r128",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.2014F, -0.4862F, -1.9F, 0.4028F, 0.5069F, 1.9F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.2342F, -0.5653F, 4.596F, 0.4684F, 0.5894F, 6.404F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.4624F, 0.0F, 0.0F, -1.5708F));
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
