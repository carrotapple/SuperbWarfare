package com.atsuishio.superbwarfare.client.model.block;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.atsuishio.superbwarfare.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class FuMO25Model<T extends Entity> extends EntityModel<T> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModUtils.loc("fumo25"), "main");

    private final ModelPart yundongjian;
    private final ModelPart hengfu;
    private final ModelPart xiefu;
    private final ModelPart moduan;
    private final ModelPart moduanL;
    private final ModelPart moduan1;
    private final ModelPart moduan2;
    private final ModelPart moduan3;
    private final ModelPart moduan4;
    private final ModelPart moduan5;
    private final ModelPart moduan6;
    private final ModelPart moduan7;
    private final ModelPart moduan8;
    private final ModelPart moduanR;
    private final ModelPart moduan9;
    private final ModelPart moduan10;
    private final ModelPart moduan11;
    private final ModelPart moduan12;
    private final ModelPart moduan13;
    private final ModelPart moduan14;
    private final ModelPart moduan15;
    private final ModelPart moduan16;
    private final ModelPart lianjie;
    private final ModelPart xialianjie;
    private final ModelPart zhonglianjie;
    private final ModelPart shanglianjie;
    private final ModelPart dinglianjie;
    private final ModelPart tajian;
    private final ModelPart tashen;
    private final ModelPart shizikuangshang;
    private final ModelPart shizikuangL;
    private final ModelPart shiziL;
    private final ModelPart tixingkuangL;
    private final ModelPart shizikuangR;
    private final ModelPart shiziR;
    private final ModelPart tixingkuangR;
    private final ModelPart guanwangR;
    private final ModelPart guanwangL;

    public FuMO25Model(ModelPart root) {
        this.yundongjian = root.getChild("yundongjian");
        ModelPart zhongxinlianjie = this.yundongjian.getChild("zhongxinlianjie");
        ModelPart leiban = zhongxinlianjie.getChild("leiban");
        ModelPart leida = this.yundongjian.getChild("leida");
        ModelPart leidawang = leida.getChild("leidawang");
        ModelPart qianheng = leidawang.getChild("qianheng");
        ModelPart qianzongwang = leidawang.getChild("qianzongwang");
        ModelPart houheng = leidawang.getChild("houheng");
        ModelPart houzongwang = leidawang.getChild("houzongwang");
        ModelPart qianfu = leidawang.getChild("qianfu");
        ModelPart zongfu = qianfu.getChild("zongfu");
        this.hengfu = qianfu.getChild("hengfu");
        this.xiefu = qianfu.getChild("xiefu");
        this.moduan = leida.getChild("moduan");
        this.moduanL = this.moduan.getChild("moduanL");
        this.moduan1 = this.moduanL.getChild("moduan1");
        this.moduan2 = this.moduanL.getChild("moduan2");
        this.moduan3 = this.moduanL.getChild("moduan3");
        this.moduan4 = this.moduanL.getChild("moduan4");
        this.moduan5 = this.moduanL.getChild("moduan5");
        this.moduan6 = this.moduanL.getChild("moduan6");
        this.moduan7 = this.moduanL.getChild("moduan7");
        this.moduan8 = this.moduanL.getChild("moduan8");
        this.moduanR = this.moduan.getChild("moduanR");
        this.moduan9 = this.moduanR.getChild("moduan9");
        this.moduan10 = this.moduanR.getChild("moduan10");
        this.moduan11 = this.moduanR.getChild("moduan11");
        this.moduan12 = this.moduanR.getChild("moduan12");
        this.moduan13 = this.moduanR.getChild("moduan13");
        this.moduan14 = this.moduanR.getChild("moduan14");
        this.moduan15 = this.moduanR.getChild("moduan15");
        this.moduan16 = this.moduanR.getChild("moduan16");
        this.lianjie = this.yundongjian.getChild("lianjie");
        this.xialianjie = this.lianjie.getChild("xialianjie");
        this.zhonglianjie = this.lianjie.getChild("zhonglianjie");
        this.shanglianjie = this.lianjie.getChild("shanglianjie");
        this.dinglianjie = this.lianjie.getChild("dinglianjie");
        this.tajian = this.yundongjian.getChild("tajian");
        this.tashen = this.tajian.getChild("tashen");
        this.shizikuangshang = this.tajian.getChild("shizikuangshang");
        this.shizikuangL = this.yundongjian.getChild("shizikuangL");
        this.shiziL = this.shizikuangL.getChild("shiziL");
        this.tixingkuangL = this.shizikuangL.getChild("tixingkuangL");
        this.shizikuangR = this.yundongjian.getChild("shizikuangR");
        this.shiziR = this.shizikuangR.getChild("shiziR");
        this.tixingkuangR = this.shizikuangR.getChild("tixingkuangR");
        this.guanwangR = this.yundongjian.getChild("guanwangR");
        this.guanwangL = this.yundongjian.getChild("guanwangL");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition yundongjian = partdefinition.addOrReplaceChild("yundongjian", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition zhongxinlianjie = yundongjian.addOrReplaceChild("zhongxinlianjie", CubeListBuilder.create().texOffs(90, 80).addBox(-15.0F, -36.0F, -15.0F, 30.0F, 2.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(0, 78).addBox(-15.0F, -1.0F, -15.0F, 30.0F, 2.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 34.0F, 0.0F));

        PartDefinition leiban = zhongxinlianjie.addOrReplaceChild("leiban", CubeListBuilder.create(), PartPose.offset(-6.2F, 8.0F, -6.2F));

        PartDefinition xiaxieban4_r1 = leiban.addOrReplaceChild("xiaxieban4_r1", CubeListBuilder.create().texOffs(0, 78).addBox(-4.5F, -4.5F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8F, -9.6F, 1.8F, -0.6155F, -0.5236F, 0.9553F));

        PartDefinition xiaxieban3_r1 = leiban.addOrReplaceChild("xiaxieban3_r1", CubeListBuilder.create().texOffs(90, 80).addBox(-4.5F, -4.5F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.6F, -9.6F, 1.8F, 0.6155F, 0.5236F, 0.9553F));

        PartDefinition xiaxieban2_r1 = leiban.addOrReplaceChild("xiaxieban2_r1", CubeListBuilder.create().texOffs(90, 94).addBox(-4.5F, -4.5F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.6F, -9.6F, 10.6F, -0.6155F, -0.5236F, 0.9553F));

        PartDefinition xiaxieban1_r1 = leiban.addOrReplaceChild("xiaxieban1_r1", CubeListBuilder.create().texOffs(0, 92).addBox(-4.5F, -4.5F, -1.0F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8F, -9.6F, 10.6F, 0.6155F, 0.5236F, 0.9553F));

        PartDefinition xialeiban1_r1 = leiban.addOrReplaceChild("xialeiban1_r1", CubeListBuilder.create().texOffs(212, 105).addBox(9.0F, -7.0F, -1.0F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition xialeiban2_r1 = leiban.addOrReplaceChild("xialeiban2_r1", CubeListBuilder.create().texOffs(206, 80).addBox(-15.0F, -7.0F, -1.0F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.4F, 0.0F, 12.4F, 0.0F, -0.7854F, 0.0F));

        PartDefinition xialeiban3_r1 = leiban.addOrReplaceChild("xialeiban3_r1", CubeListBuilder.create().texOffs(206, 93).addBox(-15.0F, -7.0F, -1.0F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 12.4F, -3.1416F, -0.7854F, 3.1416F));

        PartDefinition xialeiban4_r1 = leiban.addOrReplaceChild("xialeiban4_r1", CubeListBuilder.create().texOffs(200, 143).addBox(9.0F, -7.0F, -1.0F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.4F, 0.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));

        PartDefinition shangleiban2_r1 = leiban.addOrReplaceChild("shangleiban2_r1", CubeListBuilder.create().texOffs(127, 288).addBox(-15.0F, -32.0F, -1.0F, 30.0F, 33.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2F, -10.0F, 6.2F, 0.0F, -2.3562F, 0.0F));

        PartDefinition shangleiban1_r1 = leiban.addOrReplaceChild("shangleiban1_r1", CubeListBuilder.create().texOffs(191, 290).addBox(-15.0F, -32.0F, -1.0F, 30.0F, 33.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2F, -10.0F, 6.2F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leida = yundongjian.addOrReplaceChild("leida", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leidawang = leida.addOrReplaceChild("leidawang", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -13.5F));

        PartDefinition qianheng = leidawang.addOrReplaceChild("qianheng", CubeListBuilder.create(), PartPose.offset(0.0F, -70.0F, 0.0F));

        PartDefinition xia2_r1 = qianheng.addOrReplaceChild("xia2_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-150.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(0.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 70.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition xia1_r1 = qianheng.addOrReplaceChild("xia1_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-150.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(0.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 105.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition xia3_r1 = qianheng.addOrReplaceChild("xia3_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-150.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(0.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 35.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition xia4_r1 = qianheng.addOrReplaceChild("xia4_r1", CubeListBuilder.create().texOffs(0, 6).addBox(-150.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(0.0F, -0.5F, -0.5F, 150.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition qianzongwang = leidawang.addOrReplaceChild("qianzongwang", CubeListBuilder.create(), PartPose.offset(131.75F, -2.5F, 0.0F));

        PartDefinition qianl8_r1 = qianzongwang.addOrReplaceChild("qianl8_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl7_r1 = qianzongwang.addOrReplaceChild("qianl7_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.5F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl6_r1 = qianzongwang.addOrReplaceChild("qianl6_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl5_r1 = qianzongwang.addOrReplaceChild("qianl5_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-52.5F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl4_r1 = qianzongwang.addOrReplaceChild("qianl4_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-70.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl3_r1 = qianzongwang.addOrReplaceChild("qianl3_r1", CubeListBuilder.create().texOffs(0, 52).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-87.5F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl2_r1 = qianzongwang.addOrReplaceChild("qianl2_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-105.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianl1_r1 = qianzongwang.addOrReplaceChild("qianl1_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-122.5F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition qianr8_r1 = qianzongwang.addOrReplaceChild("qianr8_r1", CubeListBuilder.create().texOffs(0, 58).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-263.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr7_r1 = qianzongwang.addOrReplaceChild("qianr7_r1", CubeListBuilder.create().texOffs(0, 60).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-246.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr6_r1 = qianzongwang.addOrReplaceChild("qianr6_r1", CubeListBuilder.create().texOffs(0, 62).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-228.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr5_r1 = qianzongwang.addOrReplaceChild("qianr5_r1", CubeListBuilder.create().texOffs(0, 64).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-211.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr4_r1 = qianzongwang.addOrReplaceChild("qianr4_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-193.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr3_r1 = qianzongwang.addOrReplaceChild("qianr3_r1", CubeListBuilder.create().texOffs(0, 68).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-176.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr2_r1 = qianzongwang.addOrReplaceChild("qianr2_r1", CubeListBuilder.create().texOffs(0, 70).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-158.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianr1_r1 = qianzongwang.addOrReplaceChild("qianr1_r1", CubeListBuilder.create().texOffs(0, 72).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-141.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianR_r1 = qianzongwang.addOrReplaceChild("qianR_r1", CubeListBuilder.create().texOffs(0, 74).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-281.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianL_r1 = qianzongwang.addOrReplaceChild("qianL_r1", CubeListBuilder.create().texOffs(0, 76).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition houheng = leidawang.addOrReplaceChild("houheng", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 27.0F));

        PartDefinition xia3_r2 = houheng.addOrReplaceChild("xia3_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-115.0F, -25.2487F, -25.2487F, 230.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-115.0F, -0.5F, -0.5F, 230.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-115.0F, 24.2487F, 24.2487F, 230.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition houzongwang = leidawang.addOrReplaceChild("houzongwang", CubeListBuilder.create(), PartPose.offset(-65.25F, -2.5F, 27.0F));

        PartDefinition hour2_r1 = houzongwang.addOrReplaceChild("hour2_r1", CubeListBuilder.create().texOffs(90, 78).addBox(-37.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition hour1_r1 = houzongwang.addOrReplaceChild("hour1_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-37.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

        PartDefinition houl2_r1 = houzongwang.addOrReplaceChild("houl2_r1", CubeListBuilder.create().texOffs(0, 114).addBox(-32.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(130.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition houl1_r1 = houzongwang.addOrReplaceChild("houl1_r1", CubeListBuilder.create().texOffs(0, 116).addBox(-32.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(74.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition houR_r1 = houzongwang.addOrReplaceChild("houR_r1", CubeListBuilder.create().texOffs(0, 118).addBox(-32.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-49.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition houL_r1 = houzongwang.addOrReplaceChild("houL_r1", CubeListBuilder.create().texOffs(0, 120).addBox(-32.5F, -0.5F, -0.5F, 70.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(179.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 1.5708F));

        PartDefinition qianfu = leidawang.addOrReplaceChild("qianfu", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 13.5F));

        PartDefinition zongfu = qianfu.addOrReplaceChild("zongfu", CubeListBuilder.create(), PartPose.offset(-100.25F, -3.5F, -13.5F));

        PartDefinition fuyou1_r1 = zongfu.addOrReplaceChild("fuyou1_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition fuyou2_r1 = zongfu.addOrReplaceChild("fuyou2_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(60.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition fuyou3_r1 = zongfu.addOrReplaceChild("fuyou3_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-67.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(86.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition fuzuo3_r1 = zongfu.addOrReplaceChild("fuzuo3_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(114.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition fuzuo2_r1 = zongfu.addOrReplaceChild("fuzuo2_r1", CubeListBuilder.create().texOffs(0, 38).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(140.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition fuzuo1_r1 = zongfu.addOrReplaceChild("fuzuo1_r1", CubeListBuilder.create().texOffs(0, 40).addBox(-37.5F, -0.5F, -0.5F, 105.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(200.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition hengfu = qianfu.addOrReplaceChild("hengfu", CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, -46.5F, -0.5F, 132.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(0.0F, 7.5F, -0.5F, 132.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-132.0F, -46.5F, -0.5F, 132.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-132.0F, 7.5F, -0.5F, 132.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(255, 300).addBox(26.75F, -50.25F, -0.5F, 35.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(43, 132).addBox(9.0F, -52.5F, -0.5F, 18.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -13.5F));

        PartDefinition xiefu = qianfu.addOrReplaceChild("xiefu", CubeListBuilder.create(), PartPose.offset(-11.75F, -29.2574F, -12.2663F));

        PartDefinition fuxie7_r1 = xiefu.addOrReplaceChild("fuxie7_r1", CubeListBuilder.create().texOffs(0, 124).addBox(-72.5F, 23.75F, 26.0F, 68.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-56.25F, 0.0F, 0.0F, -0.829F, 0.0F, 0.5411F));

        PartDefinition fuxie6_r1 = xiefu.addOrReplaceChild("fuxie6_r1", CubeListBuilder.create().texOffs(0, 122).addBox(-72.5F, 23.75F, 26.0F, 68.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.829F, 0.0F, 0.5411F));

        PartDefinition fuxie5_r1 = xiefu.addOrReplaceChild("fuxie5_r1", CubeListBuilder.create().texOffs(297, 26).addBox(-25.5F, -0.5F, -0.5F, 43.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 13.2574F, -1.2337F, 0.0F, 0.0F, 0.9599F));

        PartDefinition fuxie4_r1 = xiefu.addOrReplaceChild("fuxie4_r1", CubeListBuilder.create().texOffs(253, 290).addBox(-23.0F, -0.5F, -0.5F, 46.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(41.1821F, 10.7621F, -1.2758F, 0.0F, 0.0F, -0.8552F));

        PartDefinition fuxie3_r1 = xiefu.addOrReplaceChild("fuxie3_r1", CubeListBuilder.create().texOffs(0, 130).addBox(-23.0F, -0.5F, -1.5F, 66.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(75.7016F, 16.0613F, -0.2758F, 0.0F, 0.0F, -0.5585F));

        PartDefinition fuxie2_r1 = xiefu.addOrReplaceChild("fuxie2_r1", CubeListBuilder.create().texOffs(0, 126).addBox(4.5F, 23.75F, 26.0F, 68.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.5F, 0.0F, 0.0F, -0.829F, 0.0F, -0.5411F));

        PartDefinition fuxie1_r1 = xiefu.addOrReplaceChild("fuxie1_r1", CubeListBuilder.create().texOffs(0, 128).addBox(4.5F, 23.75F, 26.0F, 68.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(79.75F, 0.0F, 0.0F, -0.829F, 0.0F, -0.5411F));

        PartDefinition moduan = leida.addOrReplaceChild("moduan", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition moduanL = moduan.addOrReplaceChild("moduanL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition moduan1 = moduanL.addOrReplaceChild("moduan1", CubeListBuilder.create().texOffs(213, 130).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(185, 211).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(211, 55).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(211, 43).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(131.75F, -1.0F, -24.0F));

        PartDefinition cube_r1 = moduan1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(43, 259).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = moduan1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(273, 64).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan2 = moduanL.addOrReplaceChild("moduan2", CubeListBuilder.create().texOffs(211, 31).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(172, 210).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(142, 210).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(129, 209).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(114.25F, -1.0F, -24.0F));

        PartDefinition cube_r3 = moduan2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(258, 199).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r4 = moduan2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(258, 241).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan3 = moduanL.addOrReplaceChild("moduan3", CubeListBuilder.create().texOffs(200, 131).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(56, 200).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(185, 199).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(199, 105).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(96.75F, -1.0F, -24.0F));

        PartDefinition cube_r5 = moduan3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(258, 115).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r6 = moduan3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(258, 157).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan4 = moduanL.addOrReplaceChild("moduan4", CubeListBuilder.create().texOffs(43, 199).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(13, 199).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(172, 198).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(142, 198).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(79.25F, -1.0F, -24.0F));

        PartDefinition cube_r7 = moduan4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(254, 22).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r8 = moduan4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 258).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan5 = moduanL.addOrReplaceChild("moduan5", CubeListBuilder.create().texOffs(0, 198).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(129, 197).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(99, 197).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(86, 196).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(61.75F, -1.0F, -24.0F));

        PartDefinition cube_r9 = moduan5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(172, 240).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r10 = moduan5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(215, 248).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan6 = moduanL.addOrReplaceChild("moduan6", CubeListBuilder.create().texOffs(193, 93).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(193, 81).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(56, 188).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(187, 142).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(44.25F, -1.0F, -24.0F));

        PartDefinition cube_r11 = moduan6.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(86, 238).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r12 = moduan6.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(129, 239).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan7 = moduanL.addOrReplaceChild("moduan7", CubeListBuilder.create().texOffs(187, 130).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(43, 187).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(13, 187).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 186).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(26.75F, -1.0F, -24.0F));

        PartDefinition cube_r13 = moduan7.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(43, 217).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r14 = moduan7.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(230, 73).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan8 = moduanL.addOrReplaceChild("moduan8", CubeListBuilder.create().texOffs(185, 180).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(185, 168).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(185, 156).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(180, 92).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(9.25F, -1.0F, -24.0F));

        PartDefinition cube_r15 = moduan8.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(215, 206).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r16 = moduan8.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 216).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduanR = moduan.addOrReplaceChild("moduanR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition moduan9 = moduanR.addOrReplaceChild("moduan9", CubeListBuilder.create().texOffs(180, 80).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(172, 179).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(142, 179).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(129, 178).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-131.75F, -1.0F, -24.0F));

        PartDefinition cube_r17 = moduan9.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(215, 122).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r18 = moduan9.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(215, 164).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan10 = moduanR.addOrReplaceChild("moduan10", CubeListBuilder.create().texOffs(99, 176).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(56, 176).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(86, 175).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(43, 175).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-114.25F, -1.0F, -24.0F));

        PartDefinition cube_r19 = moduan10.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(172, 198).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r20 = moduan10.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(211, 31).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan11 = moduanR.addOrReplaceChild("moduan11", CubeListBuilder.create().texOffs(13, 175).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 174).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(172, 167).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(172, 155).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-96.75F, -1.0F, -24.0F));

        PartDefinition cube_r21 = moduan11.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(86, 196).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r22 = moduan11.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(129, 197).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan12 = moduanR.addOrReplaceChild("moduan12", CubeListBuilder.create().texOffs(142, 167).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(129, 166).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(13, 160).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 159).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-79.25F, -1.0F, -24.0F));

        PartDefinition cube_r23 = moduan12.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(43, 175).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r24 = moduan12.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(187, 80).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan13 = moduanR.addOrReplaceChild("moduan13", CubeListBuilder.create().texOffs(86, 158).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(157, 139).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(157, 127).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(157, 115).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-61.75F, -1.0F, -24.0F));

        PartDefinition cube_r25 = moduan13.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(172, 156).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r26 = moduan13.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 174).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan14 = moduanR.addOrReplaceChild("moduan14", CubeListBuilder.create().texOffs(142, 155).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(129, 154).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(99, 154).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(56, 151).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-44.25F, -1.0F, -24.0F));

        PartDefinition cube_r27 = moduan14.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(86, 154).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r28 = moduan14.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(129, 155).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan15 = moduanR.addOrReplaceChild("moduan15", CubeListBuilder.create().texOffs(43, 150).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(13, 148).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 147).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(86, 146).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-26.75F, -1.0F, -24.0F));

        PartDefinition cube_r29 = moduan15.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(43, 133).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r30 = moduan15.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(144, 113).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition moduan16 = moduanR.addOrReplaceChild("moduan16", CubeListBuilder.create().texOffs(144, 138).addBox(-0.5F, -35.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(144, 126).addBox(-0.5F, -51.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(144, 114).addBox(-0.5F, 15.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(56, 139).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.25F, -1.0F, -24.0F));

        PartDefinition cube_r31 = moduan16.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(101, 112).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r32 = moduan16.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 132).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.5F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition lianjie = yundongjian.addOrReplaceChild("lianjie", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 13.5F));

        PartDefinition xialianjie = lianjie.addOrReplaceChild("xialianjie", CubeListBuilder.create(), PartPose.offset(-149.25F, 35.0F, -27.0F));

        PartDefinition xiayou1_r1 = xialianjie.addOrReplaceChild("xiayou1_r1", CubeListBuilder.create().texOffs(297, 22).addBox(0.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.6545F, 0.0F));

        PartDefinition xiayou2_r1 = xialianjie.addOrReplaceChild("xiayou2_r1", CubeListBuilder.create().texOffs(301, 11).addBox(0.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, 2.3562F, -0.9163F, 3.1416F));

        PartDefinition xiayou3_r1 = xialianjie.addOrReplaceChild("xiayou3_r1", CubeListBuilder.create().texOffs(297, 46).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -0.7854F, -0.7679F, 0.0F));

        PartDefinition xiayou4_r1 = xialianjie.addOrReplaceChild("xiayou4_r1", CubeListBuilder.create().texOffs(297, 44).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(112.0F, 0.0F, 0.0F, 2.3562F, -0.7679F, 3.1416F));

        PartDefinition xiayou5_r1 = xialianjie.addOrReplaceChild("xiayou5_r1", CubeListBuilder.create().texOffs(297, 60).addBox(0.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(113.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition xiazuo5_r1 = xialianjie.addOrReplaceChild("xiazuo5_r1", CubeListBuilder.create().texOffs(255, 298).addBox(-38.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(185.5F, 0.0F, 0.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition xiazuo4_r1 = xialianjie.addOrReplaceChild("xiazuo4_r1", CubeListBuilder.create().texOffs(297, 48).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(186.5F, 0.0F, 0.0F, -0.7854F, 2.3736F, 0.0F));

        PartDefinition xiazuo3_r1 = xialianjie.addOrReplaceChild("xiazuo3_r1", CubeListBuilder.create().texOffs(297, 50).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 0.7679F, 0.0F));

        PartDefinition xiazuo2_r1 = xialianjie.addOrReplaceChild("xiazuo2_r1", CubeListBuilder.create().texOffs(301, 13).addBox(-34.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 2.2253F, 0.0F));

        PartDefinition xiazuo1_r1 = xialianjie.addOrReplaceChild("xiazuo1_r1", CubeListBuilder.create().texOffs(297, 24).addBox(-44.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(298.5F, 0.0F, 0.0F, -0.7854F, 0.6545F, 0.0F));

        PartDefinition zhonglianjie = lianjie.addOrReplaceChild("zhonglianjie", CubeListBuilder.create(), PartPose.offset(-149.25F, 0.0F, -27.0F));

        PartDefinition zhongyou1_r1 = zhonglianjie.addOrReplaceChild("zhongyou1_r1", CubeListBuilder.create().texOffs(255, 294).addBox(0.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.6545F, 0.0F));

        PartDefinition zhongyou2_r1 = zhonglianjie.addOrReplaceChild("zhongyou2_r1", CubeListBuilder.create().texOffs(301, 7).addBox(0.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, 2.3562F, -0.9163F, 3.1416F));

        PartDefinition zhongyou3_r1 = zhonglianjie.addOrReplaceChild("zhongyou3_r1", CubeListBuilder.create().texOffs(297, 36).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -0.7854F, -0.7679F, 0.0F));

        PartDefinition zhongyou4_r1 = zhonglianjie.addOrReplaceChild("zhongyou4_r1", CubeListBuilder.create().texOffs(297, 38).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(112.0F, 0.0F, 0.0F, 2.3562F, -0.7679F, 3.1416F));

        PartDefinition zhongyou5_r1 = zhonglianjie.addOrReplaceChild("zhongyou5_r1", CubeListBuilder.create().texOffs(297, 56).addBox(0.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(113.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition zhongzuo5_r1 = zhonglianjie.addOrReplaceChild("zhongzuo5_r1", CubeListBuilder.create().texOffs(297, 58).addBox(-38.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(185.5F, 0.0F, 0.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition zhongzuo4_r1 = zhonglianjie.addOrReplaceChild("zhongzuo4_r1", CubeListBuilder.create().texOffs(297, 40).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(186.5F, 0.0F, 0.0F, -0.7854F, 2.3736F, 0.0F));

        PartDefinition zhongzuo3_r1 = zhonglianjie.addOrReplaceChild("zhongzuo3_r1", CubeListBuilder.create().texOffs(297, 42).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 0.7679F, 0.0F));

        PartDefinition zhongzuo2_r1 = zhonglianjie.addOrReplaceChild("zhongzuo2_r1", CubeListBuilder.create().texOffs(301, 9).addBox(-34.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 2.2253F, 0.0F));

        PartDefinition zhongzuo1_r1 = zhonglianjie.addOrReplaceChild("zhongzuo1_r1", CubeListBuilder.create().texOffs(255, 296).addBox(-44.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(298.5F, 0.0F, 0.0F, -0.7854F, 0.6545F, 0.0F));

        PartDefinition shanglianjie = lianjie.addOrReplaceChild("shanglianjie", CubeListBuilder.create(), PartPose.offset(-149.25F, -35.0F, -27.0F));

        PartDefinition shangyou1_r1 = shanglianjie.addOrReplaceChild("shangyou1_r1", CubeListBuilder.create().texOffs(0, 110).addBox(0.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.6545F, 0.0F));

        PartDefinition shangyou2_r1 = shanglianjie.addOrReplaceChild("shangyou2_r1", CubeListBuilder.create().texOffs(186, 286).addBox(0.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, 2.3562F, -0.9163F, 3.1416F));

        PartDefinition shangyou3_r1 = shanglianjie.addOrReplaceChild("shangyou3_r1", CubeListBuilder.create().texOffs(297, 28).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -0.7854F, -0.7679F, 0.0F));

        PartDefinition shangyou4_r1 = shanglianjie.addOrReplaceChild("shangyou4_r1", CubeListBuilder.create().texOffs(297, 30).addBox(0.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(112.0F, 0.0F, 0.0F, 2.3562F, -0.7679F, 3.1416F));

        PartDefinition shangyou5_r1 = shanglianjie.addOrReplaceChild("shangyou5_r1", CubeListBuilder.create().texOffs(297, 52).addBox(0.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(113.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition shangzuo5_r1 = shanglianjie.addOrReplaceChild("shangzuo5_r1", CubeListBuilder.create().texOffs(297, 54).addBox(-38.05F, -0.5F, -0.5F, 38.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(185.5F, 0.0F, 0.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition shangzuo4_r1 = shanglianjie.addOrReplaceChild("shangzuo4_r1", CubeListBuilder.create().texOffs(297, 32).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(186.5F, 0.0F, 0.0F, -0.7854F, 2.3736F, 0.0F));

        PartDefinition shangzuo3_r1 = shanglianjie.addOrReplaceChild("shangzuo3_r1", CubeListBuilder.create().texOffs(297, 34).addBox(-39.05F, -0.5F, -0.5F, 39.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 0.7679F, 0.0F));

        PartDefinition shangzuo2_r1 = shanglianjie.addOrReplaceChild("shangzuo2_r1", CubeListBuilder.create().texOffs(0, 301).addBox(-34.05F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -0.7854F, 2.2253F, 0.0F));

        PartDefinition shangzuo1_r1 = shanglianjie.addOrReplaceChild("shangzuo1_r1", CubeListBuilder.create().texOffs(255, 292).addBox(-44.25F, -0.5F, -0.5F, 44.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(298.5F, 0.0F, 0.0F, -0.7854F, 0.6545F, 0.0F));

        PartDefinition dinglianjie = lianjie.addOrReplaceChild("dinglianjie", CubeListBuilder.create(), PartPose.offset(-149.25F, -70.0F, -27.0F));

        PartDefinition dingyou1_r1 = dinglianjie.addOrReplaceChild("dingyou1_r1", CubeListBuilder.create().texOffs(273, 106).addBox(0.25F, -0.5F, -0.5F, 56.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.8426F, -0.4985F, 0.7844F));

        PartDefinition dingyou2_r1 = dinglianjie.addOrReplaceChild("dingyou2_r1", CubeListBuilder.create().texOffs(86, 286).addBox(0.05F, -0.5F, -0.5F, 49.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -2.7667F, -0.5876F, 2.1012F));

        PartDefinition dingyou3_r1 = dinglianjie.addOrReplaceChild("dingyou3_r1", CubeListBuilder.create().texOffs(86, 282).addBox(0.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(56.0F, 0.0F, 0.0F, -1.8885F, -0.5431F, 0.9053F));

        PartDefinition dingyou4_r1 = dinglianjie.addOrReplaceChild("dingyou4_r1", CubeListBuilder.create().texOffs(273, 112).addBox(0.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(112.0F, 0.0F, 0.0F, -2.8239F, -0.5431F, 2.2363F));

        PartDefinition dingyou5_r1 = dinglianjie.addOrReplaceChild("dingyou5_r1", CubeListBuilder.create().texOffs(273, 110).addBox(0.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(113.0F, 0.0F, 0.0F, -1.8885F, -0.5431F, 0.9053F));

        PartDefinition dingzuo1_r1 = dinglianjie.addOrReplaceChild("dingzuo1_r1", CubeListBuilder.create().texOffs(273, 108).addBox(-56.25F, -0.5F, -0.5F, 56.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(298.5F, 0.0F, 0.0F, -1.8426F, 0.4985F, -0.7844F));

        PartDefinition dingzuo2_r1 = dinglianjie.addOrReplaceChild("dingzuo2_r1", CubeListBuilder.create().texOffs(258, 287).addBox(-49.05F, -0.5F, -0.5F, 49.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -2.7667F, 0.5876F, -2.1012F));

        PartDefinition dingzuo3_r1 = dinglianjie.addOrReplaceChild("dingzuo3_r1", CubeListBuilder.create().texOffs(258, 285).addBox(-52.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(242.5F, 0.0F, 0.0F, -1.8885F, 0.5431F, -0.9053F));

        PartDefinition dingzuo4_r1 = dinglianjie.addOrReplaceChild("dingzuo4_r1", CubeListBuilder.create().texOffs(86, 284).addBox(-52.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(186.5F, 0.0F, 0.0F, -2.8239F, 0.5431F, -2.2363F));

        PartDefinition dingzuo5_r1 = dinglianjie.addOrReplaceChild("dingzuo5_r1", CubeListBuilder.create().texOffs(258, 283).addBox(-52.05F, -0.5F, -0.5F, 52.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(185.5F, 0.0F, 0.0F, -1.8885F, 0.5431F, -0.9053F));

        PartDefinition tajian = yundongjian.addOrReplaceChild("tajian", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tashen = tajian.addOrReplaceChild("tashen", CubeListBuilder.create(), PartPose.offset(-2.0F, -48.5F, -0.5F));

        PartDefinition zhewan_r1 = tashen.addOrReplaceChild("zhewan_r1", CubeListBuilder.create().texOffs(30, 132).addBox(1.5F, -2.5F, -2.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition jianduan2_r1 = tashen.addOrReplaceChild("jianduan2_r1", CubeListBuilder.create().texOffs(134, 136).addBox(-0.5F, -26.25F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(-1.0F, -14.25F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(215, 179).addBox(-2.0F, -11.25F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(215, 164).addBox(-2.5F, -2.25F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(43, 134).addBox(-3.0F, 7.75F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(-5.0F, 17.75F, -5.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 23.75F, -2.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition shizikuangshang = tajian.addOrReplaceChild("shizikuangshang", CubeListBuilder.create().texOffs(144, 143).addBox(-1.8F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, -49.0F, 1.5F));

        PartDefinition shizixie4_r1 = shizikuangshang.addOrReplaceChild("shizixie4_r1", CubeListBuilder.create().texOffs(144, 114).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, -2.3562F));

        PartDefinition shizixie3_r1 = shizikuangshang.addOrReplaceChild("shizixie3_r1", CubeListBuilder.create().texOffs(144, 119).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.6F, 0.0F, 0.0F, 0.7854F, -0.7854F));

        PartDefinition shizixie2_r1 = shizikuangshang.addOrReplaceChild("shizixie2_r1", CubeListBuilder.create().texOffs(144, 126).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6F, 0.0F, 0.0F, 0.0F, -0.7854F, 2.3562F));

        PartDefinition shizixie1_r1 = shizikuangshang.addOrReplaceChild("shizixie1_r1", CubeListBuilder.create().texOffs(144, 131).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6F, -2.6F, 0.0F, 0.0F, -0.7854F, 0.7854F));

        PartDefinition shiziheng_r1 = shizikuangshang.addOrReplaceChild("shiziheng_r1", CubeListBuilder.create().texOffs(144, 138).addBox(1.5F, -2.0F, -2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3F, 0.5F, -2.0F, -1.5708F, -1.5708F, 0.0F));

        PartDefinition shizikuangL = yundongjian.addOrReplaceChild("shizikuangL", CubeListBuilder.create(), PartPose.offset(73.0F, 15.6143F, 1.2143F));

        PartDefinition shiziL = shizikuangL.addOrReplaceChild("shiziL", CubeListBuilder.create().texOffs(138, 141).addBox(-2.8F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.3F, 1.3857F, 0.2857F));

        PartDefinition shizixie4_r2 = shiziL.addOrReplaceChild("shizixie4_r2", CubeListBuilder.create().texOffs(4, 137).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.7854F, -2.3562F));

        PartDefinition shizixie3_r2 = shiziL.addOrReplaceChild("shizixie3_r2", CubeListBuilder.create().texOffs(33, 137).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.6F, 0.0F, 0.0F, 0.7854F, -0.7854F));

        PartDefinition shizixie2_r2 = shiziL.addOrReplaceChild("shizixie2_r2", CubeListBuilder.create().texOffs(138, 122).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, 0.0F, 0.0F, 0.0F, -0.7854F, 2.3562F));

        PartDefinition shizixie1_r2 = shiziL.addOrReplaceChild("shizixie1_r2", CubeListBuilder.create().texOffs(138, 127).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, -2.6F, 0.0F, 0.0F, -0.7854F, 0.7854F));

        PartDefinition shiziheng_r2 = shiziL.addOrReplaceChild("shiziheng_r2", CubeListBuilder.create().texOffs(138, 136).addBox(1.5F, -2.0F, -2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3F, 0.5F, -2.0F, -1.5708F, -1.5708F, 0.0F));

        PartDefinition zhigan_r1 = shiziL.addOrReplaceChild("zhigan_r1", CubeListBuilder.create().texOffs(37, 147).addBox(-0.5F, -6.5F, -8.0F, 1.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3F, -1.5F, -7.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition henggan_r1 = shiziL.addOrReplaceChild("henggan_r1", CubeListBuilder.create().texOffs(73, 134).addBox(1.5F, -1.5F, -2.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3F, 0.5F, -2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tixingkuangL = shizikuangL.addOrReplaceChild("tixingkuangL", CubeListBuilder.create(), PartPose.offset(6.25F, 0.8857F, -1.7143F));

        PartDefinition tiyou3_r1 = tixingkuangL.addOrReplaceChild("tiyou3_r1", CubeListBuilder.create().texOffs(43, 163).addBox(-17.5F, -0.5F, 3.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.5F, 0.0F, 0.0F, 0.0F, -1.0472F, -1.5708F));

        PartDefinition tiyou2_r1 = tixingkuangL.addOrReplaceChild("tiyou2_r1", CubeListBuilder.create().texOffs(43, 165).addBox(-17.5F, -0.5F, -4.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.5F, 0.0F, 0.0F, 0.0F, -2.0944F, -1.5708F));

        PartDefinition tiyou1_r1 = tixingkuangL.addOrReplaceChild("tiyou1_r1", CubeListBuilder.create().texOffs(86, 170).addBox(-25.5F, -21.5F, 12.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -20.0F, -13.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition tizuo3_r1 = tixingkuangL.addOrReplaceChild("tizuo3_r1", CubeListBuilder.create().texOffs(43, 167).addBox(-17.5F, -0.5F, 3.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, -1.5708F));

        PartDefinition tizuo2_r1 = tixingkuangL.addOrReplaceChild("tizuo2_r1", CubeListBuilder.create().texOffs(43, 169).addBox(-17.5F, -0.5F, -4.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, -1.5708F));

        PartDefinition tizuo1_r1 = tixingkuangL.addOrReplaceChild("tizuo1_r1", CubeListBuilder.create().texOffs(43, 171).addBox(-25.5F, -21.5F, 12.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, -20.0F, -13.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition shizikuangR = yundongjian.addOrReplaceChild("shizikuangR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition shiziR = shizikuangR.addOrReplaceChild("shiziR", CubeListBuilder.create().texOffs(0, 137).addBox(1.8F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-74.3F, 17.0F, 1.5F));

        PartDefinition shizixie4_r3 = shiziR.addOrReplaceChild("shizixie4_r3", CubeListBuilder.create().texOffs(34, 132).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 2.3562F));

        PartDefinition shizixie3_r3 = shiziR.addOrReplaceChild("shizixie3_r3", CubeListBuilder.create().texOffs(43, 134).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.6F, 0.0F, 0.0F, -0.7854F, 0.7854F));

        PartDefinition shizixie2_r3 = shiziR.addOrReplaceChild("shizixie2_r3", CubeListBuilder.create().texOffs(61, 134).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6F, 0.0F, 0.0F, 0.0F, 0.7854F, -2.3562F));

        PartDefinition shizixie1_r3 = shiziR.addOrReplaceChild("shizixie1_r3", CubeListBuilder.create().texOffs(65, 134).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6F, -2.6F, 0.0F, 0.0F, 0.7854F, -0.7854F));

        PartDefinition shiziheng_r3 = shiziR.addOrReplaceChild("shiziheng_r3", CubeListBuilder.create().texOffs(37, 136).addBox(-2.5F, -2.0F, -2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3F, 0.5F, -2.0F, -1.5708F, 1.5708F, 0.0F));

        PartDefinition zhigan_r2 = shiziR.addOrReplaceChild("zhigan_r2", CubeListBuilder.create().texOffs(80, 133).addBox(-0.5F, -6.5F, -8.0F, 1.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3F, -1.5F, -7.5F, 0.0F, 0.0F, -1.5708F));

        PartDefinition henggan_r2 = shiziR.addOrReplaceChild("henggan_r2", CubeListBuilder.create().texOffs(69, 134).addBox(-2.5F, -1.5F, -2.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3F, 0.5F, -2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tixingkuangR = shizikuangR.addOrReplaceChild("tixingkuangR", CubeListBuilder.create(), PartPose.offset(-79.25F, 16.5F, -0.5F));

        PartDefinition tiyou3_r2 = tixingkuangR.addOrReplaceChild("tiyou3_r2", CubeListBuilder.create().texOffs(99, 146).addBox(2.5F, -0.5F, 3.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.5F, 0.0F, 0.0F, 0.0F, 1.0472F, 1.5708F));

        PartDefinition tiyou2_r2 = tixingkuangR.addOrReplaceChild("tiyou2_r2", CubeListBuilder.create().texOffs(99, 148).addBox(2.5F, -0.5F, -4.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.5F, 0.0F, 0.0F, 0.0F, 2.0944F, 1.5708F));

        PartDefinition tiyou1_r2 = tixingkuangR.addOrReplaceChild("tiyou1_r2", CubeListBuilder.create().texOffs(0, 106).addBox(14.5F, -21.5F, 12.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -20.0F, -13.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition tizuo3_r2 = tixingkuangR.addOrReplaceChild("tizuo3_r2", CubeListBuilder.create().texOffs(99, 150).addBox(2.5F, -0.5F, 3.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 1.5708F));

        PartDefinition tizuo2_r2 = tixingkuangR.addOrReplaceChild("tizuo2_r2", CubeListBuilder.create().texOffs(144, 151).addBox(2.5F, -0.5F, -4.9F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 1.5708F));

        PartDefinition tizuo1_r2 = tixingkuangR.addOrReplaceChild("tizuo1_r2", CubeListBuilder.create().texOffs(99, 166).addBox(14.5F, -21.5F, 12.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0F, -20.0F, -13.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition guanwangR = yundongjian.addOrReplaceChild("guanwangR", CubeListBuilder.create(), PartPose.offset(-90.4391F, -0.0423F, 11.3997F));

        PartDefinition guanwang_r1 = guanwangR.addOrReplaceChild("guanwang_r1", CubeListBuilder.create().texOffs(187, 128).addBox(-32.8995F, 7.4195F, -3.8728F, 32.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, -0.8029F, -2.0682F));

        PartDefinition guanwang_r2 = guanwangR.addOrReplaceChild("guanwang_r2", CubeListBuilder.create().texOffs(86, 134).addBox(-22.6995F, 7.6195F, -2.0728F, 26.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2818F, -25.8793F, 1.2789F, 0.2618F, -0.1571F, -1.6406F));

        PartDefinition guanwang_r3 = guanwangR.addOrReplaceChild("guanwang_r3", CubeListBuilder.create().texOffs(144, 112).addBox(-17.4995F, -2.0805F, -1.1728F, 17.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.3177F, -36.3865F, 4.2222F, 0.0F, -0.0698F, -0.5061F));

        PartDefinition guanwang_r4 = guanwangR.addOrReplaceChild("guanwang_r4", CubeListBuilder.create().texOffs(187, 124).addBox(-31.4495F, -0.2805F, -0.7728F, 32.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(49.1963F, -44.8127F, -4.8721F, 0.0F, 0.2793F, -0.2356F));

        PartDefinition guanwang_r5 = guanwangR.addOrReplaceChild("guanwang_r5", CubeListBuilder.create().texOffs(86, 138).addBox(-23.0F, -0.5F, -0.5F, 23.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(71.8244F, -49.6564F, -6.6433F, 0.0F, 0.0611F, -0.2182F));

        PartDefinition guanwang_r6 = guanwangR.addOrReplaceChild("guanwang_r6", CubeListBuilder.create().texOffs(86, 142).addBox(-22.5F, -0.4F, -0.2F, 23.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(89.6891F, -39.4577F, -14.4497F, 0.0F, 0.3491F, 0.5236F));

        PartDefinition guanwangL = yundongjian.addOrReplaceChild("guanwangL", CubeListBuilder.create(), PartPose.offset(90.4391F, -0.0423F, 11.3997F));

        PartDefinition guanwang_r7 = guanwangL.addOrReplaceChild("guanwang_r7", CubeListBuilder.create().texOffs(187, 126).addBox(0.8995F, 7.4195F, -3.8728F, 32.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.8029F, 2.0682F));

        PartDefinition guanwang_r8 = guanwangL.addOrReplaceChild("guanwang_r8", CubeListBuilder.create().texOffs(86, 132).addBox(-3.3005F, 7.6195F, -2.0728F, 26.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2818F, -25.8793F, 1.2789F, 0.2618F, 0.1571F, 1.6406F));

        PartDefinition guanwang_r9 = guanwangL.addOrReplaceChild("guanwang_r9", CubeListBuilder.create().texOffs(86, 144).addBox(0.4995F, -2.0805F, -1.1728F, 17.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.3177F, -36.3865F, 4.2222F, 0.0F, 0.0698F, 0.5061F));

        PartDefinition guanwang_r10 = guanwangL.addOrReplaceChild("guanwang_r10", CubeListBuilder.create().texOffs(187, 122).addBox(-0.5505F, -0.2805F, -0.7728F, 32.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-49.1963F, -44.8127F, -4.8721F, 0.0F, -0.2793F, 0.2356F));

        PartDefinition guanwang_r11 = guanwangL.addOrReplaceChild("guanwang_r11", CubeListBuilder.create().texOffs(86, 136).addBox(0.0F, -0.5F, -0.5F, 23.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-71.8244F, -49.6564F, -6.6433F, 0.0F, -0.0611F, 0.2182F));

        PartDefinition guanwang_r12 = guanwangL.addOrReplaceChild("guanwang_r12", CubeListBuilder.create().texOffs(86, 140).addBox(-0.5F, -0.4F, -0.2F, 23.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-89.6891F, -39.4577F, -14.4497F, 0.0F, -0.3491F, -0.5236F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(0.3f, 0.3f, 0.3f);
        poseStack.mulPose(Axis.XN.rotation(180.0f * Mth.DEG_TO_RAD));
        yundongjian.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, boolean roll) {
        if (roll) {
            this.yundongjian.yRot = (System.currentTimeMillis() % 36000000) / 1200f;
        } else {
            this.yundongjian.yRot = 0;
        }
        renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}