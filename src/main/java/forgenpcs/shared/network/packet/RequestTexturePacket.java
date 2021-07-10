package forgenpcs.shared.network.packet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.PacketBuffer;

/**
 * Sent from client to server to request a texture. This packet is answered by the server with a {@link TexturePacket}.
 * @author P.J.S. Kools
 */
public class RequestTexturePacket {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public final String textureLocation;
	
	public RequestTexturePacket(String textureLocation) {
		this.textureLocation = textureLocation;
	}
	
	public void write(PacketBuffer buf) {
		buf.writeString(this.textureLocation);
	}
	
	public static RequestTexturePacket read(PacketBuffer buf) {
		try {
			return new RequestTexturePacket(buf.readString(Short.MAX_VALUE));
		} catch (Exception e) {
			LOGGER.warn("Exception while reading " + RequestTexturePacket.class.getSimpleName() + " packet: ", e);
			return null;
		}
	}
}
