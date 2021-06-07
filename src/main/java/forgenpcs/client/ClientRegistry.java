package forgenpcs.client;

import forgenpcs.ForgeNPCsMod;
import forgenpcs.common.CommonRegistry;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = ForgeNPCsMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
	
	private final IEventBus eventBus;
	
	public ClientRegistry(IEventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void setup() {
		this.eventBus.register(ClientRegistry.class);
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		
		// Register NPC renderer.
		RenderingRegistry.registerEntityRenderingHandler(CommonRegistry.ModEntities.NPC.get(),
				(EntityRendererManager entityRenderManager) -> new RenderNPC(entityRenderManager));
	}
}
