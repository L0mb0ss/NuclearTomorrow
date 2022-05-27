package mod.L0mb0ss.NuclearTomorrow.init;

import javax.annotation.Nullable;
import net.minecraftforge.common.DimensionManager;
import mod.L0mb0ss.NuclearTomorrow.dimensions.realWorld.WorldProviderRealWorld;
public class ModDimensions {
	
	public static final int realWorldId = findFreeDimensionID();
	public static final String realWorldName = "realworld";
	
	
	public static void init() {
		registerDimension();
	}
	
	public static void registerDimension() {
		DimensionManager.registerProviderType(realWorldId, WorldProviderRealWorld.class, false);
		DimensionManager.registerDimension(realWorldId, realWorldId);
	}
	
	@Nullable
    private static Integer findFreeDimensionID() {
        for (int i=2; i<Integer.MAX_VALUE; i++) {
            if (!DimensionManager.isDimensionRegistered(i)) {
                System.out.println("Found free dimension ID = "+i);
                return i;
            }
        }
        System.out.println("ERROR: Could not find free dimension ID");
        return null;
    }
}
