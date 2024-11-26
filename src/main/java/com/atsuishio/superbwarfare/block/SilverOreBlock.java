package com.atsuishio.superbwarfare.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class SilverOreBlock extends Block {
    public SilverOreBlock() {
        super(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 5f).requiresCorrectToolForDrops());
    }
}
