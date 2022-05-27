package mod.L0mb0ss.NuclearTomorrow.items.ingots;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.L0mb0ss.NuclearTomorrow.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class UraniumIngot extends Item {
	
	public UraniumIngot (String name) {
        super();
        this.setMaxDamage(0);
        this.setUnlocalizedName("uranium_ingot").setTextureName(Reference.MODID + ":uranium_ingot").setCreativeTab(CreativeTabs.tabMaterials);
        GameRegistry.registerItem(this, this.getUnlocalizedName());
    }

}
