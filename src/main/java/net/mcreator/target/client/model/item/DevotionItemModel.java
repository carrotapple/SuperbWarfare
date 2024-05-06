package net.mcreator.target.client.model.item;

import net.mcreator.target.item.gun.DevotionItem;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class DevotionItemModel extends GeoModel<DevotionItem> {
    @Override
    public ResourceLocation getAnimationResource(DevotionItem animatable) {
        return new ResourceLocation("target", "animations/devotion.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DevotionItem animatable) {
        return new ResourceLocation("target", "geo/devotion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DevotionItem animatable) {
        return new ResourceLocation("target", "textures/item/devotion.png");
    }


    @Override
    public void setCustomAnimations(DevotionItem animatable, long instanceId, AnimationState animationState) {
        CoreGeoBone gun = getAnimationProcessor().getBone("bone");
        CoreGeoBone l = getAnimationProcessor().getBone("l");
        CoreGeoBone r = getAnimationProcessor().getBone("r");
        CoreGeoBone bolt = getAnimationProcessor().getBone("bolt2");

        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();

        double p = 0;
        p = player.getPersistentData().getDouble("zoompos");

        double zp = 0;
        zp = player.getPersistentData().getDouble("zoomposz");

        gun.setPosX(2.19f * (float) p);

        gun.setPosY(0.17f * (float) p - (float) (0.5f * zp));

        gun.setPosZ(8.8f * (float) p + (float) (0.6f * zp));

        gun.setRotZ((float) (0.05f * zp));

        gun.setScaleZ(1f - (0.7f * (float) p));

        CoreGeoBone shen = getAnimationProcessor().getBone("shen");
        CoreGeoBone number = getAnimationProcessor().getBone("number");

        double fp = 0;
        fp = player.getPersistentData().getDouble("firepos");

        double bp = 0;
        bp = player.getPersistentData().getDouble("boltpos");

        bolt.setPosZ(-2f * (float) bp);

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setPosZ(0.4f * (float) fp);
        } else {
            shen.setPosZ(0.75f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).zooming) {
            shen.setRotX(0.002f * (float) fp);
        } else {
            shen.setRotX(0.02f * (float) fp);
        }

        if ((player.getCapability(TargetModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new TargetModVariables.PlayerVariables())).recoilhorizon == 1) {
            shen.setRotZ(0.002f * (float) fp);
        } else {
            shen.setRotZ(-0.002f * (float) fp);
        }

        CoreGeoBone holo = getAnimationProcessor().getBone("holo");
        CoreGeoBone flare = getAnimationProcessor().getBone("flare");
        if (gun.getPosX() > 1.2) {
            holo.setScaleX(1);
            holo.setScaleY(1);
            number.setScaleX(1);
            number.setScaleY(1);
        } else {
            holo.setScaleX(0);
            holo.setScaleY(0);
            number.setScaleX(0);
            number.setScaleY(0);
        }
        if (stack.getOrCreateTag().getDouble("fireanim") > 0) {
            flare.setScaleX((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setScaleY((float) (1.0 + 0.5 * (Math.random() - 0.5)));
            flare.setRotZ((float) (0.5 * (Math.random() - 0.5)));
        } else {
            flare.setScaleX(0);
            flare.setScaleY(0);
            flare.setRotZ(0);
        }


        CoreGeoBone n0 = getAnimationProcessor().getBone("00");
        CoreGeoBone n1 = getAnimationProcessor().getBone("1");
        CoreGeoBone n2 = getAnimationProcessor().getBone("2");
        CoreGeoBone n3 = getAnimationProcessor().getBone("3");
        CoreGeoBone n4 = getAnimationProcessor().getBone("4");
        CoreGeoBone n5 = getAnimationProcessor().getBone("5");
        CoreGeoBone n6 = getAnimationProcessor().getBone("6");
        CoreGeoBone n7 = getAnimationProcessor().getBone("7");
        CoreGeoBone n8 = getAnimationProcessor().getBone("8");
        CoreGeoBone n9 = getAnimationProcessor().getBone("9");
        CoreGeoBone n10 = getAnimationProcessor().getBone("10");
        CoreGeoBone n11 = getAnimationProcessor().getBone("11");
        CoreGeoBone n12 = getAnimationProcessor().getBone("12");
        CoreGeoBone n13 = getAnimationProcessor().getBone("13");
        CoreGeoBone n14 = getAnimationProcessor().getBone("14");
        CoreGeoBone n15 = getAnimationProcessor().getBone("15");
        CoreGeoBone n16 = getAnimationProcessor().getBone("16");
        CoreGeoBone n17 = getAnimationProcessor().getBone("17");
        CoreGeoBone n18 = getAnimationProcessor().getBone("18");
        CoreGeoBone n19 = getAnimationProcessor().getBone("19");
        CoreGeoBone n20 = getAnimationProcessor().getBone("20");
        CoreGeoBone n21 = getAnimationProcessor().getBone("21");
        CoreGeoBone n22 = getAnimationProcessor().getBone("22");
        CoreGeoBone n23 = getAnimationProcessor().getBone("23");
        CoreGeoBone n24 = getAnimationProcessor().getBone("24");
        CoreGeoBone n25 = getAnimationProcessor().getBone("25");
        CoreGeoBone n26 = getAnimationProcessor().getBone("26");
        CoreGeoBone n27 = getAnimationProcessor().getBone("27");
        CoreGeoBone n28 = getAnimationProcessor().getBone("28");
        CoreGeoBone n29 = getAnimationProcessor().getBone("29");
        CoreGeoBone n30 = getAnimationProcessor().getBone("30");
        CoreGeoBone n31 = getAnimationProcessor().getBone("31");
        CoreGeoBone n32 = getAnimationProcessor().getBone("32");
        CoreGeoBone n33 = getAnimationProcessor().getBone("33");
        CoreGeoBone n34 = getAnimationProcessor().getBone("34");
        CoreGeoBone n35 = getAnimationProcessor().getBone("35");
        CoreGeoBone n36 = getAnimationProcessor().getBone("36");
        CoreGeoBone n37 = getAnimationProcessor().getBone("37");
        CoreGeoBone n38 = getAnimationProcessor().getBone("38");
        CoreGeoBone n39 = getAnimationProcessor().getBone("39");
        CoreGeoBone n40 = getAnimationProcessor().getBone("40");
        CoreGeoBone n41 = getAnimationProcessor().getBone("41");
        CoreGeoBone n42 = getAnimationProcessor().getBone("42");
        CoreGeoBone n43 = getAnimationProcessor().getBone("43");
        CoreGeoBone n44 = getAnimationProcessor().getBone("44");
        CoreGeoBone n45 = getAnimationProcessor().getBone("45");
        CoreGeoBone n46 = getAnimationProcessor().getBone("46");
        CoreGeoBone n47 = getAnimationProcessor().getBone("47");
        CoreGeoBone n48 = getAnimationProcessor().getBone("48");
        CoreGeoBone n49 = getAnimationProcessor().getBone("49");
        CoreGeoBone n50 = getAnimationProcessor().getBone("50");
        CoreGeoBone n51 = getAnimationProcessor().getBone("51");
        CoreGeoBone n52 = getAnimationProcessor().getBone("52");
        CoreGeoBone n53 = getAnimationProcessor().getBone("53");
        CoreGeoBone n54 = getAnimationProcessor().getBone("54");
        CoreGeoBone n55 = getAnimationProcessor().getBone("55");
        CoreGeoBone n56 = getAnimationProcessor().getBone("56");

        if (stack.getOrCreateTag().getDouble("ammo") == 0) {
            n0.setScaleX(1);
            n0.setScaleY(1);
        } else {
            n0.setScaleX(0);
            n0.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 1) {
            n1.setScaleX(1);
            n1.setScaleY(1);
        } else {
            n1.setScaleX(0);
            n1.setScaleY(0);
        }


        if (stack.getOrCreateTag().getDouble("ammo") == 2) {
            n2.setScaleX(1);
            n2.setScaleY(1);
        } else {
            n2.setScaleX(0);
            n2.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 3) {
            n3.setScaleX(1);
            n3.setScaleY(1);
        } else {
            n3.setScaleX(0);
            n3.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 4) {
            n4.setScaleX(1);
            n4.setScaleY(1);
        } else {
            n4.setScaleX(0);
            n4.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 5) {
            n5.setScaleX(1);
            n5.setScaleY(1);
        } else {
            n5.setScaleX(0);
            n5.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 6) {
            n6.setScaleX(1);
            n6.setScaleY(1);
        } else {
            n6.setScaleX(0);
            n6.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 7) {
            n7.setScaleX(1);
            n7.setScaleY(1);
        } else {
            n7.setScaleX(0);
            n7.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 8) {
            n8.setScaleX(1);
            n8.setScaleY(1);
        } else {
            n8.setScaleX(0);
            n8.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 9) {
            n9.setScaleX(1);
            n9.setScaleY(1);
        } else {
            n9.setScaleX(0);
            n9.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 10) {
            n10.setScaleX(1);
            n10.setScaleY(1);
        } else {
            n10.setScaleX(0);
            n10.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 11) {
            n11.setScaleX(1);
            n11.setScaleY(1);
        } else {
            n11.setScaleX(0);
            n11.setScaleY(0);
        }


        if (stack.getOrCreateTag().getDouble("ammo") == 12) {
            n12.setScaleX(1);
            n12.setScaleY(1);
        } else {
            n12.setScaleX(0);
            n12.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 13) {
            n13.setScaleX(1);
            n13.setScaleY(1);
        } else {
            n13.setScaleX(0);
            n13.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 14) {
            n14.setScaleX(1);
            n14.setScaleY(1);
        } else {
            n14.setScaleX(0);
            n14.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 15) {
            n15.setScaleX(1);
            n15.setScaleY(1);
        } else {
            n15.setScaleX(0);
            n15.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 16) {
            n16.setScaleX(1);
            n16.setScaleY(1);
        } else {
            n16.setScaleX(0);
            n16.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 17) {
            n17.setScaleX(1);
            n17.setScaleY(1);
        } else {
            n17.setScaleX(0);
            n17.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 18) {
            n18.setScaleX(1);
            n18.setScaleY(1);
        } else {
            n18.setScaleX(0);
            n18.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 19) {
            n19.setScaleX(1);
            n19.setScaleY(1);
        } else {
            n19.setScaleX(0);
            n19.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 20) {
            n20.setScaleX(1);
            n20.setScaleY(1);
        } else {
            n20.setScaleX(0);
            n20.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 21) {
            n21.setScaleX(1);
            n21.setScaleY(1);
        } else {
            n21.setScaleX(0);
            n21.setScaleY(0);
        }


        if (stack.getOrCreateTag().getDouble("ammo") == 22) {
            n22.setScaleX(1);
            n22.setScaleY(1);
        } else {
            n22.setScaleX(0);
            n22.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 23) {
            n23.setScaleX(1);
            n23.setScaleY(1);
        } else {
            n23.setScaleX(0);
            n23.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 24) {
            n24.setScaleX(1);
            n24.setScaleY(1);
        } else {
            n24.setScaleX(0);
            n24.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 25) {
            n25.setScaleX(1);
            n25.setScaleY(1);
        } else {
            n25.setScaleX(0);
            n25.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 26) {
            n26.setScaleX(1);
            n26.setScaleY(1);
        } else {
            n26.setScaleX(0);
            n26.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 27) {
            n27.setScaleX(1);
            n27.setScaleY(1);
        } else {
            n27.setScaleX(0);
            n27.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 28) {
            n28.setScaleX(1);
            n28.setScaleY(1);
        } else {
            n28.setScaleX(0);
            n28.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 29) {
            n29.setScaleX(1);
            n29.setScaleY(1);
        } else {
            n29.setScaleX(0);
            n29.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 30) {
            n30.setScaleX(1);
            n30.setScaleY(1);
        } else {
            n30.setScaleX(0);
            n30.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 31) {
            n31.setScaleX(1);
            n31.setScaleY(1);
        } else {
            n31.setScaleX(0);
            n31.setScaleY(0);
        }


        if (stack.getOrCreateTag().getDouble("ammo") == 32) {
            n32.setScaleX(1);
            n32.setScaleY(1);
        } else {
            n32.setScaleX(0);
            n32.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 33) {
            n33.setScaleX(1);
            n33.setScaleY(1);
        } else {
            n33.setScaleX(0);
            n33.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 34) {
            n34.setScaleX(1);
            n34.setScaleY(1);
        } else {
            n34.setScaleX(0);
            n34.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 35) {
            n35.setScaleX(1);
            n35.setScaleY(1);
        } else {
            n35.setScaleX(0);
            n35.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 36) {
            n36.setScaleX(1);
            n36.setScaleY(1);
        } else {
            n36.setScaleX(0);
            n36.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 37) {
            n37.setScaleX(1);
            n37.setScaleY(1);
        } else {
            n37.setScaleX(0);
            n37.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 38) {
            n38.setScaleX(1);
            n38.setScaleY(1);
        } else {
            n38.setScaleX(0);
            n38.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 39) {
            n39.setScaleX(1);
            n39.setScaleY(1);
        } else {
            n39.setScaleX(0);
            n39.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 40) {
            n40.setScaleX(1);
            n40.setScaleY(1);
        } else {
            n40.setScaleX(0);
            n40.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 41) {
            n41.setScaleX(1);
            n41.setScaleY(1);
        } else {
            n41.setScaleX(0);
            n41.setScaleY(0);
        }


        if (stack.getOrCreateTag().getDouble("ammo") == 42) {
            n42.setScaleX(1);
            n42.setScaleY(1);
        } else {
            n42.setScaleX(0);
            n42.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 43) {
            n43.setScaleX(1);
            n43.setScaleY(1);
        } else {
            n43.setScaleX(0);
            n43.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 44) {
            n44.setScaleX(1);
            n44.setScaleY(1);
        } else {
            n44.setScaleX(0);
            n44.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 45) {
            n45.setScaleX(1);
            n45.setScaleY(1);
        } else {
            n45.setScaleX(0);
            n45.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 46) {
            n46.setScaleX(1);
            n46.setScaleY(1);
        } else {
            n46.setScaleX(0);
            n46.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 47) {
            n47.setScaleX(1);
            n47.setScaleY(1);
        } else {
            n47.setScaleX(0);
            n47.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 48) {
            n48.setScaleX(1);
            n48.setScaleY(1);
        } else {
            n48.setScaleX(0);
            n48.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 49) {
            n49.setScaleX(1);
            n49.setScaleY(1);
        } else {
            n49.setScaleX(0);
            n49.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 50) {
            n50.setScaleX(1);
            n50.setScaleY(1);
        } else {
            n50.setScaleX(0);
            n50.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 51) {
            n51.setScaleX(1);
            n51.setScaleY(1);
        } else {
            n51.setScaleX(0);
            n51.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 52) {
            n52.setScaleX(1);
            n52.setScaleY(1);
        } else {
            n52.setScaleX(0);
            n52.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 53) {
            n53.setScaleX(1);
            n53.setScaleY(1);
        } else {
            n53.setScaleX(0);
            n53.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 54) {
            n54.setScaleX(1);
            n54.setScaleY(1);
        } else {
            n54.setScaleX(0);
            n54.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 55) {
            n55.setScaleX(1);
            n55.setScaleY(1);
        } else {
            n55.setScaleX(0);
            n55.setScaleY(0);
        }

        if (stack.getOrCreateTag().getDouble("ammo") == 56) {
            n56.setScaleX(1);
            n56.setScaleY(1);
        } else {
            n56.setScaleX(0);
            n56.setScaleY(0);
        }

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        double y = 0;
        double x = 0;
        y = player.getPersistentData().getDouble("y");
        x = player.getPersistentData().getDouble("x");

        root.setPosY((float) y);
        root.setRotX((float) x);

        CoreGeoBone move = getAnimationProcessor().getBone("move");

        double m = 0;
        m = player.getPersistentData().getDouble("move");

        double yaw = 0;
        yaw = player.getPersistentData().getDouble("yaw");

        double pit = 0;
        pit = player.getPersistentData().getDouble("gunpitch");

        double vy = 0;
        vy = player.getPersistentData().getDouble("vy");

        move.setPosY(-1 * (float) vy);

        move.setPosX(9.3f * (float) m);

        move.setRotX(2.0f * (float) pit);

        move.setRotZ(3.7f * (float) yaw + 2.7f * (float) m);

        move.setRotY(1.9f * (float) yaw - 1.7f * (float) m);

        if (player.getPersistentData().getDouble("prone") > 0) {
            l.setRotX(1.5f);
            r.setRotX(1.5f);
        }
    }
}
