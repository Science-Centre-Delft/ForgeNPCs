package forgenpcs;

import forgenpcs.proxy.CommonProxy;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ForgeNPCsMod.MODID, version = ForgeNPCsMod.VERSION)
public class ForgeNPCsMod {
	
	// You also need to update the modid and version in two other places as well:
	//  build.gradle file (the version, group, and archivesBaseName parameters).
	//  resources/mcmod.info (the name, description, and version parameters).
	public static final String MODID = "ForgeNPCs";
	public static final String VERSION = "MC1.8.9-V0.0.1";
	
	private static final String PREFIX = EnumChatFormatting.GOLD + "["
			+ EnumChatFormatting.DARK_AQUA + "ForgeNPCs" + EnumChatFormatting.GOLD + "] ";
	public static final String PREFIX_INFO = PREFIX + EnumChatFormatting.GREEN;
	public static final String PREFIX_ERROR = PREFIX + EnumChatFormatting.RED;
	
	public ForgeNPCsMod() {
	}
	
	// The instance of this mod as created by Forge.
	@Mod.Instance(ForgeNPCsMod.MODID)
	public static ForgeNPCsMod instance;
	
	@SidedProxy(clientSide = "forgenpcs.proxy.ClientProxy",
			serverSide = "forgenpcs.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
		
		// Register commands.
//		event.registerServerCommand(new BlaCommand(this));
	}
	
//	/**
//	 * Logs an error message with a throwable as SEVERE to console, prefixed with the mod's error prefix.
//	 * @param message - The message to log.
//	 * @param throwable - The throwable to log.
//	 */
//	public static void logException(String message, Throwable throwable) {
//		MinecraftServer.getServer().logSevere(
//				PREFIX_ERROR + message + "\nHere's the stacktrace:\n" + Utils.getStacktrace(throwable));
//	}
}
