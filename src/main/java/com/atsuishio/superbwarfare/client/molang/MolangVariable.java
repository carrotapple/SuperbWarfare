package com.atsuishio.superbwarfare.client.molang;

import software.bernie.geckolib.core.molang.LazyVariable;
import software.bernie.geckolib.core.molang.MolangParser;

public class MolangVariable {

    public static void register() {
        MolangParser.INSTANCE.register(new LazyVariable("sbw.system_time", System::currentTimeMillis));
    }
}
