package com.atsuishio.superbwarfare.client.molang;

import software.bernie.geckolib.core.molang.LazyVariable;
import software.bernie.geckolib.core.molang.MolangParser;

import java.util.function.DoubleSupplier;

public class MolangVariable {
    public static void register() {
        register("sbw.system_time", System::currentTimeMillis);
    }

    private static void register(String name, DoubleSupplier supplier) {
        MolangParser.INSTANCE.register(new LazyVariable(name, supplier));
    }
}
