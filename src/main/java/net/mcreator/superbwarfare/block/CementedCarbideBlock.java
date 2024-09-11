package net.mcreator.superbwarfare.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class CementedCarbideBlock extends Block {
    public CementedCarbideBlock() {
        super(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops());
    }
}
