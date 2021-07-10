package forgenpcs.shared.network;

import java.util.Optional;

import forgenpcs.ForgeNPCsMod;
import forgenpcs.client.network.ClientPacketHandler;
import forgenpcs.server.network.ServerPacketHandler;
import forgenpcs.shared.network.packet.RequestTexturePacket;
import forgenpcs.shared.network.packet.TexturePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Handles custom packet communication between clients and the server.
 * Note that packets do have to be registered before they can be sent.
 * @author P.J.S. Kools
 */
public abstract class NetworkManager {
	
	private static SimpleChannel channel;
	
	private static final String CHANNEL_ID = "forgenpcs";
	private static final String NETWORK_PROTOCOL_VERSION = "1";
	
	private NetworkManager() {
	}
	
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		
		// Register network channel.
		channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(ForgeNPCsMod.MODID, CHANNEL_ID),
				() -> NETWORK_PROTOCOL_VERSION,
				(protocolVersion) -> NETWORK_PROTOCOL_VERSION.equals(protocolVersion), // Client accepted versions.
				(protocolVersion) -> NETWORK_PROTOCOL_VERSION.equals(protocolVersion)); // Server accepted versions.
		
		// Register network packets.
		int packetId = 0;
		channel.registerMessage(packetId++, TexturePacket.class,
				TexturePacket::write,
				TexturePacket::read,
				(packet, ctxSupplier) -> ClientPacketHandler.onTexturePacketReceived(packet, ctxSupplier),
				Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		
		channel.registerMessage(packetId++, RequestTexturePacket.class,
				RequestTexturePacket::write,
				RequestTexturePacket::read,
				(packet, ctxSupplier) -> ServerPacketHandler.onRequestTexturePacketReceived(packet, ctxSupplier),
				Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	/**
	 * Sends the given packet from the client to the server.
	 * @param <MSG>
	 * @param packet - The packet to send.
	 */
	@OnlyIn(Dist.CLIENT)
	public static <MSG> void sendPacketToServer(MSG packet) {
		channel.sendToServer(packet);
	}
	
	/**
	 * Sends the given packer from the server to the client.
	 * @param <MSG>
	 * @param player - The player to send the packet to.
	 * @param packet - The packet to send.
	 */
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static <MSG> void sendPacketToPlayer(ServerPlayerEntity player, MSG packet) {
		channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}
}
