package com.atsuishio.superbwarfare.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ModRenderTypes extends RenderType {

    public ModRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static final Function<ResourceLocation, RenderType> LASER = Util.memoize((location) -> {
        TextureStateShard shard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState state = RenderType.CompositeState.builder().setTextureState(shard)
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setCullState(NO_CULL).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
        return RenderType.create("laser", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
    });

    public static final Function<ResourceLocation, RenderType> ILLUMINATED = Util.memoize((location) -> {
        TextureStateShard shard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState state = RenderType.CompositeState.builder().setTextureState(shard)
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setCullState(NO_CULL).setOverlayState(NO_OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
        return RenderType.create("illuminated", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
    });

    //DickSheep的恩情还不完

    public static final RenderType MUZZLE_FLASH_TYPE =
            RenderType.create(
                    "muzzle_flash",
                    DefaultVertexFormat.POSITION_COLOR_TEX, // 注意：格式名改为 POSITION_COLOR_TEX（不是 TEX）
                    VertexFormat.Mode.QUADS,
                    256,
                    false,
                    true,
                    // 使用新的 CompositeState 构造方式
                    RenderType.CompositeState.builder()
                            // 关键修复：使用内置的 POSITION_COLOR_TEX_SHADER（直接调用 ShaderStateShard）
                            .setShaderState(RENDERTYPE_EYES_SHADER)
                            // 启用半透明（确保正确排序）
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            // 绑定贴图（替换为你的路径）
                            .setTextureState(new RenderStateShard.TextureStateShard(
                                    new ResourceLocation("superbwarfare", "textures/particle/flare.png"),
                                    false, // 不模糊
                                    false  // 不生成Mipmap
                            ))
                            // 禁用光照和覆盖颜色
                            .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                            .setOverlayState(RenderStateShard.NO_OVERLAY)
                            // 深度测试和写入设置
//                            .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
                            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                            .createCompositeState(false)
            );


}
