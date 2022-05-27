package mod.L0mb0ss.NuclearTomorrow.init;

import mod.L0mb0ss.NuclearTomorrow.biome.CityBiome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class ModBiomes {
	
	public static BiomeGenBase biomeCity;
	
	public static void init() {
		initialiseBiomes();
		registerBiomes();
	}
	
	public static void initialiseBiomes() {
		biomeCity = new CityBiome(127).setBiomeName("City");
		biomeCity.heightVariation = 0.0F;
		biomeCity.temperature = 1.0F;
	}
	
	public static void registerBiomes() {
		BiomeDictionary.registerBiomeType(biomeCity, Type.PLAINS);
	}
}
