package net.mcreator.target.procedures;

import net.mcreator.target.TargetMod;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WelcomeProcedure {
    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        if (event != null) {
            execute(event, event.getIMCStream());
        }
    }

    public static void execute(Stream<InterModComms.IMCMessage> stream) {
        execute(null, stream);
    }

    private static void execute(@Nullable Event event, Stream<InterModComms.IMCMessage> stream) {
        if (event == null)
            return;
        Logger logger = null;
        if ((logger == null ? logger = TargetMod.LOGGER : LogManager.getLogger(TargetMod.class)) instanceof Logger) {
            {
                Logger _lgr = (Logger) logger;
                _lgr.info("This Mod is made by MCreator!");
            }
        }
    }
}
