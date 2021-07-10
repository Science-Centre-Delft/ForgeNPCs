package forgenpcs.server.network;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import forgenpcs.ForgeNPCsMod;
import forgenpcs.shared.network.NetworkManager;
import forgenpcs.shared.network.packet.RequestTexturePacket;
import forgenpcs.shared.network.packet.TexturePacket;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet handler for custom packets that are sent from clients to the server.
 * @author P.J.S. Kools
 */
public class ServerPacketHandler {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Handles the Client -> Server {@link RequestTexturePacket} packet.
	 * Fetches the texture and sends it to the client.
	 * @param packet
	 * @param ctxSupplier
	 */
	public static void onRequestTexturePacketReceived(
			RequestTexturePacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		
		// Ignore packets that were received on the wrong side.
		if(ctx.getDirection().getReceptionSide() != LogicalSide.SERVER) {
			LOGGER.warn(packet.getClass().getSimpleName()
					+ " packet received on the wrong side: " + ctx.getDirection().getReceptionSide());
			return;
		}
		
		// Mark packet as handled.
		ctx.setPacketHandled(true);
		
		// Get texture file.
		String textureLocation = packet.textureLocation;
		File textureFile;
		if(!textureLocation.matches("[a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*")) {
			LOGGER.warn("Player " + ctxSupplier.get().getSender().getName() + " has sent "
					+ packet.getClass().getSimpleName() + " packet with invalid texture location: " + textureLocation);
			return;
		}
		String textureFileLocation = textureLocation.replace('.', File.separatorChar) + ".png";
		textureFile = new File(ForgeNPCsMod.TEXTURE_STORAGE_DIR.getAbsolutePath(), textureFileLocation);
		if(!textureFile.isFile()) {
			LOGGER.warn("Player " + ctxSupplier.get().getSender().getName()
					+ " has requested unexisting texture: " + textureFileLocation);
			return;
		}
		
		// Read texture file.
		byte[] textureBytes;
		try {
			textureBytes = FileUtils.readFileToByteArray(textureFile);
		} catch (IOException e) {
			LOGGER.warn("Exception while reading texture file for player "
					+ ctxSupplier.get().getSender().getName() + ": " + textureFile.getAbsolutePath(), e);
			return;
		}
		
		// Send texture file to client.
		NetworkManager.sendPacketToPlayer(
				ctxSupplier.get().getSender(), new TexturePacket(textureLocation, textureBytes));
	}
}
