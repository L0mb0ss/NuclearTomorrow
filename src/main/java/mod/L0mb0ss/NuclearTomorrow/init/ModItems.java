package mod.L0mb0ss.NuclearTomorrow.init;

import mod.L0mb0ss.NuclearTomorrow.items.ingots.UraniumIngot;
import net.minecraft.item.Item;

public class ModItems {
	
	public static Item uranium_ingot;
	
	public static void init() {
		uranium_ingot = new UraniumIngot("uranium_ingot");
	}
}
