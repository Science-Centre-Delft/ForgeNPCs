package forgenpcs.shared;


import forgenpcs.shared.entity.NPCEntity;
import forgenpcs.shared.item.ModdedSpawnEggItem;
import forgenpcs.ForgeNPCsMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = ForgeNPCsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonRegistry {
	
	private final IEventBus eventBus;
	
	public CommonRegistry(IEventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void setup() {
		ModEntities.ENTITIES.register(this.eventBus);
		ModItems.ITEMS.register(this.eventBus);
		this.eventBus.register(CommonRegistry.class);
	}
	
	public static final class ModEntities {
		
		static final DeferredRegister<EntityType<?>> ENTITIES =
				DeferredRegister.create(ForgeRegistries.ENTITIES, ForgeNPCsMod.MODID);
		
		public static final RegistryObject<EntityType<NPCEntity>> NPC = ENTITIES.register("npc",
				() -> EntityType.Builder.create(NPCEntity::new, EntityClassification.MISC).build("npc"));
	}
	
	public static final class ModItems {
		
		static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeNPCsMod.MODID);
		
		public static final RegistryObject<SpawnEggItem> NPC_SPAWN_EGG = ITEMS.register("npc_spawn_egg",
				() -> new ModdedSpawnEggItem(ModEntities.NPC, 0x0000ffff, 0x00ff0000,
						new Item.Properties().group(ItemGroup.MISC)));
	}
}
