package com.atsuishio.superbwarfare.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class SilverBlock extends Block {
    public SilverBlock() {
        super(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops());
    }
}
