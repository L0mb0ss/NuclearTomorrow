package mod.L0mb0ss.NuclearTomorrow.init;

import mod.L0mb0ss.NuclearTomorrow.blocks.ores.UraniumOre;
import net.minecraft.block.Block;

public class ModBlocks {
	
	public static Block uranium_ore;
	
	public static void init() {
		uranium_ore = new UraniumOre("uranium_ore");
	}
}
