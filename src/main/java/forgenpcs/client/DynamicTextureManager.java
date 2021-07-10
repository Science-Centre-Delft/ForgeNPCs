package forgenpcs.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import forgenpcs.ForgeNPCsMod;
import forgenpcs.shared.network.NetworkManager;
import forgenpcs.shared.network.packet.RequestTexturePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public final class DynamicTextureManager {
	
    private static final Logger LOGGER = LogManager.getLogger();
	
	private static final long TEXTURE_REQUEST_TIMEOUT_MS = 60000;
	private static final Map<ResourceLocation, Long> PENDING_TEXTURE_REQUEST_TIMES = new HashMap<>();
	
	private DynamicTextureManager() {
	}
	
	public static ResourceLocation getTextureResourceLocation(
			ResourceLocation desiredTextureLocation, ResourceLocation defaultTextureLocation) {
		
		// Return the desired texture location if it is loaded or available in client resources.
		if(Minecraft.getInstance().getTextureManager().getTexture(desiredTextureLocation) != null
				|| Minecraft.getInstance().getResourceManager().hasResource(desiredTextureLocation)) {
			return desiredTextureLocation;
		}
		
		// Return the default texture if this is not a ForgeNPCs texture.
		if(!desiredTextureLocation.getNamespace().equals(ForgeNPCsMod.MODID)) {
			return defaultTextureLocation;
		}
		
		// Send a request for the desired texture location if a request hasn't already been made within the timeout.
		long currentTime = System.currentTimeMillis();
		Long time = PENDING_TEXTURE_REQUEST_TIMES.get(desiredTextureLocation);
		if(time == null || currentTime - time > TEXTURE_REQUEST_TIMEOUT_MS) { 
			LOGGER.info("Requesting texture from server: " + desiredTextureLocation);
			NetworkManager.sendPacketToServer(new RequestTexturePacket(desiredTextureLocation.getPath()));
			PENDING_TEXTURE_REQUEST_TIMES.put(desiredTextureLocation, currentTime);
		}
		
		// Return the default texture location.
		return defaultTextureLocation;
	}
}
