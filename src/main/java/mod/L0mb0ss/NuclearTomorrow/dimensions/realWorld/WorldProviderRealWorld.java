package mod.L0mb0ss.NuclearTomorrow.dimensions.realWorld;

import mod.L0mb0ss.NuclearTomorrow.init.ModDimensions;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.layer.GenLayerBiome;

public class WorldProviderRealWorld extends WorldProvider {
	
	@Override
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new RealWorldChunkManager(worldObj);
		this.dimensionId = ModDimensions.realWorldId;
		this.hasNoSky = false;
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new RealWorldChunkProvider(this.worldObj, this.worldChunkMgr);
    }

	@Override
	public boolean isSurfaceWorld() {
		return true;
	}

	@Override
	public String getDimensionName() {
		return ModDimensions.realWorldName;
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int y) {
		if (x > 600 & x < 2000 & y > 600 & y < 2000) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	@Override
	public boolean canRespawnHere() {
        return true;
    }


}
