package mod.L0mb0ss.NuclearTomorrow.dimensions.realWorld;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class RealWorldCityFlattener {

	@SubscribeEvent
	public void onPopulate(PopulateChunkEvent.Post event) {
		int chunkX = event.chunkX;
		int chunkZ = event.chunkZ;
		World world = event.world;
		int blockX = (chunkX * 16) + 8;
		int blockZ = (chunkZ * 16) + 8;
		if (!world.isRemote) {
			for(int x = blockX; x <= blockX + 16; x++) {
				for(int z = blockZ; z <= blockZ + 16; z++) {
					world.setBlock(x, 100, z, Blocks.dirt);
				}
			}
		}
	}
}
