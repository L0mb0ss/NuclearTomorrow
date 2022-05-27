package mod.L0mb0ss.NuclearTomorrow.blocks.ores;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.L0mb0ss.NuclearTomorrow.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class UraniumOre extends Block {
	
	public UraniumOre(String name) {
		super(Material.rock);
		this.setHardness(3);
		this.setResistance(3);
		this.setHarvestLevel("pickaxe", 2);
		GameRegistry.registerBlock(this, name).setBlockName(name).setBlockTextureName(Reference.MODID + ":" + name).setCreativeTab(CreativeTabs.tabBlock);
	}
}
