package mod.L0mb0ss.NuclearTomorrow.citygen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class CityAreaGen {
	//public CityAreaGen(int xCoord, int zCoord, long seed) {
		//int[] cityCentre = {xCoord, zCoord};
	//}
	
	@SubscribeEvent
	public void CityAreaGen(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (event.player.dimension != 2) {
			player.travelToDimension(2);
			player.setPosition(0, 1000, 0);
		}
	}
}
