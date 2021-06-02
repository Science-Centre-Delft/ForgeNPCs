package forgenpcs.proxy;

import forgenpcs.FakePlayerEntity;
import forgenpcs.ForgeNPCsMod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent preEvent) {
		
		// Register fake player entity and spawn egg.
		int trackingRange = 64; // Block range around players to keep entities loaded.
		int updateFreq = 1; // Frequency to tick the entity at for updates. Cannot be 0.
		EntityRegistry.registerModEntity(FakePlayerEntity.class, "fakeplayer",
				10000, ForgeNPCsMod.instance, trackingRange, updateFreq, false, 0x0000ffff, 0x00ff0000);
	}
	
	public void init(FMLInitializationEvent event) {
	}
	
	public void postInit(FMLPostInitializationEvent postEvent) {
	}
	
	public void serverStarting(FMLServerStartingEvent event) {
	}
}
