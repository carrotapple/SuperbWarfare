package net.mcreator.target.tools;

import com.google.gson.stream.JsonReader;
import net.mcreator.target.network.TargetModVariables;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.InputStreamReader;
import java.util.HashMap;

public class GunsTool {

    public static final HashMap<String, HashMap<String, Double>> gunsData = new HashMap<>();

    public static void initJsonData(Level level) {
        var manager = level.getServer().getResourceManager();

        for (var entry : manager.listResources("guns", file -> file.getPath().endsWith(".json")).entrySet()) {
            var id = entry.getKey();
            var attribute = entry.getValue();
            try {
                JsonReader reader = new JsonReader(new InputStreamReader(attribute.open()));

                reader.beginObject();
                var map = new HashMap<String, Double>();
                while (reader.hasNext()) {
                    map.put(reader.nextName(), reader.nextDouble());
                }
                var path = id.getPath();
                gunsData.put(path.substring(5, path.length() - 5), map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initGun(Level level, ItemStack stack, String location) {
        if (level.getServer() == null) return;
        initJsonData(level);
        gunsData.get(location).forEach((k, v) -> stack.getOrCreateTag().putDouble(k, v));
    }

    public static void pvpModeCheck(ItemStack stack, Level level) {
        if (!TargetModVariables.MapVariables.get(level).pvpmode) {
            if (stack.getOrCreateTag().getDouble("level") >= 10) {
                stack.getOrCreateTag().putDouble("damageadd", 1 + 0.05 * (stack.getOrCreateTag().getDouble("level") - 10));
            } else {
                stack.getOrCreateTag().putDouble("damageadd", 1);
            }
        } else {
            stack.getOrCreateTag().putDouble("damageadd", 1);
        }
    }
}
