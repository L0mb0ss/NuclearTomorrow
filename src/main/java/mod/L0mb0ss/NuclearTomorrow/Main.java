package mod.L0mb0ss.NuclearTomorrow;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.L0mb0ss.NuclearTomorrow.citygen.CityAreaGen;
import mod.L0mb0ss.NuclearTomorrow.dimensions.realWorld.RealWorldCityFlattener;
import mod.L0mb0ss.NuclearTomorrow.init.ModBiomes;
import mod.L0mb0ss.NuclearTomorrow.init.ModBlocks;
import mod.L0mb0ss.NuclearTomorrow.init.ModDimensions;
import mod.L0mb0ss.NuclearTomorrow.init.ModItems;
import mod.L0mb0ss.NuclearTomorrow.init.ModSmelting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.CreateSpawnPosition;

@Mod(modid = Reference.MODID, name=Reference.MODNAME, version=Reference.VERSION, acceptedMinecraftVersions=Reference.ACCEPTED_MINECRAFT_VERSIONS)
public class Main {
	
	@Instance
	public static Main instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println(Reference.MODID + ":preInit");
		ModItems.init();
		ModBlocks.init();
		ModSmelting.init();
		ModDimensions.init();
		ModBiomes.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println(Reference.MODID + ":init");
		MinecraftForge.EVENT_BUS.register(new RealWorldCityFlattener());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println(Reference.MODID + ":postInit");
	}
}
