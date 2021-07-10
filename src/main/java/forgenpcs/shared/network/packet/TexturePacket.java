package forgenpcs.shared.network.packet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.PacketBuffer;

/**
 * Sent from server to client as response to a {@link RequestTexturePacket}.
 * @author P.J.S. Kools
 */
public class TexturePacket {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public final String textureLocation;
	public final byte[] textureBytes;
	
	public TexturePacket(String textureLocation, byte[] textureBytes) {
		this.textureLocation = textureLocation;
		this.textureBytes = (textureBytes == null ? new byte[0] : textureBytes);
	}
	
	public void write(PacketBuffer buf) {
		buf.writeString(this.textureLocation);
		buf.writeByteArray(this.textureBytes);
	}
	
	public static TexturePacket read(PacketBuffer buf) {
		try {
			String textureLocation = buf.readString();
			byte[] textureBytes = buf.readByteArray(Integer.MAX_VALUE);
			return new TexturePacket(textureLocation, textureBytes);
		} catch (Exception e) {
			LOGGER.warn("Exception while reading " + TexturePacket.class.getSimpleName() + " packet: ", e);
			return null;
		}
	}
}
