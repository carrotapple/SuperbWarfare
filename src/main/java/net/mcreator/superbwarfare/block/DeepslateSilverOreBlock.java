package net.mcreator.superbwarfare.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class DeepslateSilverOreBlock extends Block {
    public DeepslateSilverOreBlock() {
        super(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 8f).requiresCorrectToolForDrops());
    }
}
