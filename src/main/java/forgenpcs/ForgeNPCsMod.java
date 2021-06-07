package forgenpcs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import forgenpcs.client.ClientRegistry;
import forgenpcs.common.CommonRegistry;
import forgenpcs.common.item.ModdedSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ForgeNPCsMod.MODID)
public class ForgeNPCsMod {
	
    private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String MODID = "forgenpcs";
	
	private static final String PREFIX = TextFormatting.GOLD + "["
			+ TextFormatting.DARK_AQUA + "ForgeNPCs" + TextFormatting.GOLD + "] ";
	public static final String PREFIX_INFO = PREFIX + TextFormatting.GREEN;
	public static final String PREFIX_ERROR = PREFIX + TextFormatting.RED;
	
	public static ForgeNPCsMod instance;
	
	public ForgeNPCsMod() {
		instance = this;
		
		// Get mod event bus.
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		// Shared setup.
		new CommonRegistry(modEventBus).setup();
		
		// Client side only setup.
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new ClientRegistry(modEventBus)::setup);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	/**
	 * Inject modded spawn eggs after registration. This is a workaround for defining a custom SpawnEggItem.
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
		ModdedSpawnEggItem.initUnaddedEggs();
	}
}
