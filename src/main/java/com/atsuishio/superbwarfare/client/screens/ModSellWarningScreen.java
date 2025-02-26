package com.atsuishio.superbwarfare.client.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ModSellWarningScreen extends WarningScreen {

    private static final String ENVIRONMENT_CHECKSUM = generateEnvironmentHash();

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "error";
        }
    }

    private static String generateEnvironmentHash() {
        var environmentInfo = List.of(
                System.getProperty("os.name"),          // 操作系统名称
                System.getProperty("os.arch"),          // 操作系统架构
                System.getProperty("java.vm.version"),  // JVM详细版本号
                System.getProperty("java.home"),        // JVM路径
                System.getProperty("user.name"),        // 系统用户名称
                getHostName(),                          // 主机名称
                "stupidNoPayWarningChecksum"            // 神秘的盐
        );

        //
//        System.out.println("env info:");
//        environmentInfo.forEach(System.out::println);
//        System.out.println("env info end");
        //

        return sha256(String.join("|", environmentInfo));
    }

    private static String sha256(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    // TODO 正确实现提示文本
    //    private static final Component TITLE = Component.translatable("multiplayerWarning.header").withStyle(ChatFormatting.BOLD);
    private static final Component TITLE = Component.literal("test").withStyle(ChatFormatting.BOLD);
    private static final Component CONTENT = Component.literal("这里应该写个多人游戏警告");
    private static final Component CHECK = Component.literal("这里应该是确认勾选框");
    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
    private final Screen lastScreen;

    public ModSellWarningScreen(Screen lastScreen) {
        super(TITLE, CONTENT, CHECK, NARRATION);
        this.lastScreen = lastScreen;
    }

    // TODO 正确实现校验和配置文件
    private static String checksumTest = "";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onGuiOpen(ScreenEvent.Opening event) {
        if (!(event.getNewScreen() instanceof JoinMultiplayerScreen && !(event.getCurrentScreen() instanceof ModSellWarningScreen)))
            return;

        System.out.println("multiplayer screen open");

        if (checksumTest.equals(ENVIRONMENT_CHECKSUM)) return;
//        if (ModSellWarningConfig.ENVIRONMENT_CHECKSUM.get().equals(ENVIRONMENT_CHECKSUM)) return;

        // 拦截多人游戏界面加载
        event.setCanceled(true);
        Minecraft.getInstance().setScreen(new ModSellWarningScreen(event.getCurrentScreen()));
        System.out.println(ENVIRONMENT_CHECKSUM);
    }

    @Override
    protected void initButtons(int pYOffset) {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_PROCEED, button -> {
            if (this.stopShowing.selected()) {
                checksumTest = ENVIRONMENT_CHECKSUM;
//                ModSellWarningConfig.ENVIRONMENT_CHECKSUM.set(ENVIRONMENT_CHECKSUM);
//                ModSellWarningConfig.ENVIRONMENT_CHECKSUM.save();
            }
            Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(this.lastScreen));
        }).bounds(this.width / 2 - 155, 100 + pYOffset, 150, 20).build());

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> Minecraft.getInstance().setScreen(this.lastScreen))
                        .bounds(this.width / 2 - 155 + 160, 100 + pYOffset, 150, 20)
                        .build()
        );
    }
}
