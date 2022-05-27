package mod.L0mb0ss.NuclearTomorrow.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class ModSmelting {
	
	public static void init() {
		GameRegistry.addSmelting(ModBlocks.uranium_ore, new ItemStack(ModItems.uranium_ingot), 1.5F);
	}
}
