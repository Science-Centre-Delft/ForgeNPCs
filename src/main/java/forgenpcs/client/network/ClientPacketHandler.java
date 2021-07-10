package forgenpcs.client.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import forgenpcs.ForgeNPCsMod;
import forgenpcs.shared.network.packet.TexturePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet handler for custom packets that are sent from the server to clients.
 * @author P.J.S. Kools
 */
public class ClientPacketHandler {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Handles the Server -> Client {@link TexturePacket} packet.
	 * Loads the received texture in the texture manager.
	 * @param packet
	 * @param ctxSupplier
	 */
	public static void onTexturePacketReceived(
			TexturePacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		
		// Ignore packets that were received on the wrong side.
		if(ctx.getDirection().getReceptionSide() != LogicalSide.CLIENT) {
			LOGGER.warn(packet.getClass().getSimpleName()
					+ " packet received on the wrong side: " + ctx.getDirection().getReceptionSide());
			return;
		}
		
		// Mark packet as handled.
		ctx.setPacketHandled(true);
		
		// Read the texture image.
		NativeImage img;
		try {
			img = NativeImage.read(new ByteArrayInputStream(packet.textureBytes));
		} catch (IOException e) {
			LOGGER.warn("Failed to load texture from received bytes.", e);
			return;
		}
		
		// Load the texture.
		ResourceLocation textureLocation = new ResourceLocation(ForgeNPCsMod.MODID, packet.textureLocation);
		Minecraft.getInstance().getTextureManager().loadTexture(textureLocation, new DynamicTexture(img));
		LOGGER.info("Received texture from server: " + textureLocation);
	}
}
