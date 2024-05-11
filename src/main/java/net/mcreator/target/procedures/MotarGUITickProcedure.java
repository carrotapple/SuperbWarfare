package net.mcreator.target.procedures;

import net.mcreator.target.init.TargetModAttributes;
import net.mcreator.target.tools.TraceTool;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;

public class MotarGUITickProcedure {
    public static void execute(Entity entity, HashMap<String, Object> guistate) {
        if (entity == null || guistate == null) return;

        Entity looking = TraceTool.findLookingEntity(entity, 6);
        if (looking == null) return;

        if (20 <= new Object() {
            double convert(String s) {
                try {
                    return Double.parseDouble(s.trim());
                } catch (Exception ignored) {
                }
                return 0;
            }
        }.convert(guistate.containsKey("text:pitch") ? ((EditBox) guistate.get("text:pitch")).getValue() : "") && new Object() {
            double convert(String s) {
                try {
                    return Double.parseDouble(s.trim());
                } catch (Exception e) {
                }
                return 0;
            }
        }.convert(guistate.containsKey("text:pitch") ? ((EditBox) guistate.get("text:pitch")).getValue() : "") <= 90) {
            ((LivingEntity) looking).getAttribute(TargetModAttributes.MOTARPITCH.get()).setBaseValue(new Object() {
                double convert(String s) {
                    try {
                        return Double.parseDouble(s.trim());
                    } catch (Exception e) {
                    }
                    return 0;
                }
            }.convert(guistate.containsKey("text:pitch") ? ((EditBox) guistate.get("text:pitch")).getValue() : ""));
        }
    }
}
