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
public class ModelRgoGrenade<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ModUtils.MODID, "model_rgo_grenade"), "main");
	public final ModelPart group;

	public ModelRgoGrenade(ModelPart root) {
		this.group = root.getChild("group");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create(), PartPose.offset(0.0F, -0.0217F, 0.0F));

		PartDefinition bone = group.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

		PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(24, 25).addBox(-0.5375F, -13.3055F, 17.1856F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0295F, -17.7281F));

		PartDefinition bone180_r1 = bone5.addOrReplaceChild("bone180_r1", CubeListBuilder.create().texOffs(25, 25).addBox(-1.2976F, -0.5375F, -0.5375F, 0.5953F, 1.075F, 1.075F, new CubeDeformation(0.0F))
				.texOffs(24, 25).addBox(-0.5375F, 0.7024F, -0.5375F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0078F, 17.7231F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone179_r1 = bone5.addOrReplaceChild("bone179_r1", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5375F, -0.5375F, -1.2976F, 1.075F, 1.075F, 0.5953F, new CubeDeformation(0.0F))
				.texOffs(24, 25).addBox(-0.5375F, 0.7024F, -0.5375F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0078F, 17.7231F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone6 = bone.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5375F, -15.5554F, 17.1856F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F))
				.texOffs(1, 24).addBox(-0.5375F, -15.5554F, 17.1856F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-0.387F, -17.2646F, 17.3361F, 0.774F, 0.4286F, 0.774F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0295F, -17.7281F));

		PartDefinition bone181_r1 = bone6.addOrReplaceChild("bone181_r1", CubeListBuilder.create().texOffs(25, 25).addBox(-1.2976F, -0.5375F, -0.5375F, 0.5953F, 1.075F, 1.075F, new CubeDeformation(0.0F))
				.texOffs(24, 25).addBox(-0.5375F, -1.2976F, -0.5375F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.2578F, 17.7231F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bone180_r2 = bone6.addOrReplaceChild("bone180_r2", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5375F, -0.5375F, -1.2976F, 1.075F, 1.075F, 0.5953F, new CubeDeformation(0.0F))
				.texOffs(24, 25).addBox(-0.5375F, -1.2976F, -0.5375F, 1.075F, 0.5953F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.2578F, 17.7231F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone7 = bone.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(24, 28).addBox(-1.2976F, -14.7953F, 17.1856F, 2.5953F, 1.325F, 1.075F, new CubeDeformation(0.0F))
				.texOffs(19, 28).addBox(-0.5375F, -14.7953F, 16.4255F, 1.075F, 1.325F, 2.5953F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0295F, -17.7281F));

		PartDefinition bone180_r3 = bone7.addOrReplaceChild("bone180_r3", CubeListBuilder.create().texOffs(19, 24).addBox(-0.5375F, -0.7875F, -1.2976F, 1.075F, 1.325F, 2.5953F, new CubeDeformation(0.0F))
				.texOffs(24, 24).addBox(-1.2976F, -0.7875F, -0.5375F, 2.5953F, 1.325F, 1.075F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0078F, 17.7231F, 0.0F, 0.7854F, 0.0F));

		PartDefinition group3 = bone.addOrReplaceChild("group3", CubeListBuilder.create().texOffs(0, 30).addBox(-1.337F, -17.1111F, -7.7855F, 0.674F, 1.104F, 0.5686F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-1.337F, -16.0911F, -7.263F, 0.674F, 0.804F, 0.4686F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-1.587F, -15.2871F, -7.463F, 1.174F, 0.5F, 0.6686F, new CubeDeformation(0.0F))
				.texOffs(12, 29).addBox(-1.487F, -15.1871F, -6.7944F, 0.974F, 0.3F, 0.0117F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 15.3733F, 8.1462F));

		PartDefinition bone183_r1 = group3.addOrReplaceChild("bone183_r1", CubeListBuilder.create().texOffs(2, 27).addBox(-0.337F, -0.3093F, -0.427F, 0.674F, 0.3186F, 0.254F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -16.4252F, -8.0809F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bone182_r1 = group3.addOrReplaceChild("bone182_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-0.337F, -0.427F, -0.3093F, 0.674F, 0.554F, 0.6186F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -16.5982F, -7.9079F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone183_r2 = group3.addOrReplaceChild("bone183_r2", CubeListBuilder.create().texOffs(13, 28).addBox(-0.362F, -0.552F, 0.1843F, 0.724F, 0.1F, 0.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -15.5821F, -6.7683F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone182_r2 = group3.addOrReplaceChild("bone182_r2", CubeListBuilder.create().texOffs(0, 30).addBox(-0.337F, -0.617F, 0.3657F, 0.674F, 1.104F, 0.5686F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -16.1835F, -7.844F, 0.3927F, 0.0F, 0.0F));

		PartDefinition group4 = bone.addOrReplaceChild("group4", CubeListBuilder.create().texOffs(1, 29).addBox(-0.9343F, -13.817F, -8.392F, 1.8686F, 1.554F, 0.774F, new CubeDeformation(0.0F))
				.texOffs(0, 27).addBox(-0.387F, -13.817F, -8.9393F, 0.774F, 1.554F, 1.8686F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.1292F, 8.0F));

		PartDefinition bone181_r2 = group4.addOrReplaceChild("bone181_r2", CubeListBuilder.create().texOffs(2, 27).addBox(-0.387F, -0.567F, -0.9343F, 0.774F, 1.554F, 1.8686F, new CubeDeformation(0.0F))
				.texOffs(0, 29).addBox(-0.9343F, -0.567F, -0.387F, 1.8686F, 1.554F, 0.774F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.25F, -8.005F, 0.0F, 0.7854F, 0.0F));

		PartDefinition group5 = bone.addOrReplaceChild("group5", CubeListBuilder.create().texOffs(0, 24).addBox(-0.4218F, -12.5194F, -9.0234F, 0.8437F, 0.2589F, 2.0368F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-1.0184F, -12.5194F, -8.4268F, 2.0368F, 0.2589F, 0.8437F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.1292F, 8.0F));

		PartDefinition bone184_r1 = group5.addOrReplaceChild("bone184_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0184F, 0.817F, -0.4218F, 2.0368F, 0.2589F, 0.8437F, new CubeDeformation(0.0F))
				.texOffs(0, 29).addBox(-0.4218F, 0.817F, -1.0184F, 0.8437F, 0.2589F, 2.0368F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.3364F, -8.005F, 0.0F, 0.7854F, 0.0F));

		PartDefinition group6 = bone.addOrReplaceChild("group6", CubeListBuilder.create(), PartPose.offset(0.0F, 12.9492F, 8.0F));

		PartDefinition bone179_r2 = group6.addOrReplaceChild("bone179_r2", CubeListBuilder.create().texOffs(0, 30).addBox(-0.387F, -0.9343F, -0.387F, 0.774F, 0.4286F, 0.774F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-0.387F, -0.387F, -0.9343F, 0.774F, 0.774F, 0.4286F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.25F, -8.005F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone180_r4 = group6.addOrReplaceChild("bone180_r4", CubeListBuilder.create().texOffs(0, 30).addBox(-0.387F, -0.9343F, -0.387F, 0.774F, 0.4286F, 0.774F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-0.9343F, -0.387F, -0.387F, 0.4286F, 0.774F, 0.774F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.25F, -8.005F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}