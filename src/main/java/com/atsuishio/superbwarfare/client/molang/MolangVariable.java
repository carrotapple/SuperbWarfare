package com.atsuishio.superbwarfare.client.molang;

import software.bernie.geckolib.core.molang.LazyVariable;
import software.bernie.geckolib.core.molang.MolangParser;

import java.util.function.DoubleSupplier;

public class MolangVariable {

    public static final String SBW_SYSTEM_TIME = "sbw.system_time";

    public static void register() {
        MolangParser.INSTANCE.setMemoizedValue(SBW_SYSTEM_TIME, () -> 0);
    }

    private static void register(String name, DoubleSupplier supplier) {
        MolangParser.INSTANCE.register(new LazyVariable(name, supplier));
    }
}
