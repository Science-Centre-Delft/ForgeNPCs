package forgenpcs;

import com.google.gson.JsonParseException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NPCEntity extends LivingEntity {
	
	/*
	 * TODO - Determine which aspects of the entity should be setable.
	 * Potentially unimplemented interesting things to set/control:
	 *     Body part positions.
	 *     Move from A to B, possibly through pathfinding "AI". Should have a walking animation.
	 *     Animations (ability to define target rotations and positions at given timestamps, lerping between them).
	 *     Sneak state.
	 *     Ability to look at the nearest player within a given range (animating back to default rotation otherwise).
	 *         Could also move whole body when necessary to not screw the head off.
	 */
	
	public static final String NPC_DEFAULT_TEXTURE_LOCATION = ForgeNPCsMod.MODID + ":textures/entity/npc/steve.png";
	public static final ResourceLocation DEFAULT_NPC_TEXTURE = new ResourceLocation(NPC_DEFAULT_TEXTURE_LOCATION);
	
	private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
	private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
	private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
	private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
	
	public static final DataParameter<Rotations> HEAD_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> BODY_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> LEFT_ARM_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> RIGHT_ARM_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> LEFT_LEG_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> RIGHT_LEG_ROTATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.ROTATIONS);
	
	public static final DataParameter<String> TEXTURE_LOCATION = EntityDataManager.createKey(NPCEntity.class, DataSerializers.STRING);
	
	public static final ITextComponent DEFAULT_DISPLAY_NAME = new StringTextComponent(ForgeNPCsMod.MODID + ":npc");
	public static final DataParameter<ITextComponent> DISPLAY_NAME = EntityDataManager.createKey(NPCEntity.class, DataSerializers.TEXT_COMPONENT);
	
	private Rotations headRotation = DEFAULT_HEAD_ROTATION;
	private Rotations bodyRotation = DEFAULT_BODY_ROTATION;
	private Rotations leftArmRotation = DEFAULT_LEFTARM_ROTATION;
	private Rotations rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
	private Rotations leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
	private Rotations rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
	
	private AttributeModifierManager attributes = null;
	private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
	private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
	
	private ResourceLocation npcTexture = DEFAULT_NPC_TEXTURE;
	private ITextComponent displayName = DEFAULT_DISPLAY_NAME;
	
	public NPCEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return LivingEntity.registerAttributes();
//				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0d)
//				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.1f)
//				.createMutableAttribute(Attributes.ATTACK_SPEED).createMutableAttribute(Attributes.LUCK)
//				.createMutableAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get());
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
		this.dataManager.register(BODY_ROTATION, DEFAULT_BODY_ROTATION);
		this.dataManager.register(LEFT_ARM_ROTATION, DEFAULT_LEFTARM_ROTATION);
		this.dataManager.register(RIGHT_ARM_ROTATION, DEFAULT_RIGHTARM_ROTATION);
		this.dataManager.register(LEFT_LEG_ROTATION, DEFAULT_LEFTLEG_ROTATION);
		this.dataManager.register(RIGHT_LEG_ROTATION, DEFAULT_RIGHTLEG_ROTATION);
		this.dataManager.register(DISPLAY_NAME, DEFAULT_DISPLAY_NAME);
		this.dataManager.register(TEXTURE_LOCATION, NPC_DEFAULT_TEXTURE_LOCATION);
	}
	
	@Override
	public AttributeModifierManager getAttributeManager() {
		
		// Initialize the attributes on the first call.
		// This cannot be done directly in class construction due to the super constructor calling this method earlier.
		if(this.attributes == null) {
			this.attributes = new AttributeModifierManager(LivingEntity.registerAttributes().create());
		}
		return this.attributes;
	}
	
	@Override
	public Iterable<ItemStack> getHeldEquipment() {
		return this.handItems;
	}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return this.armorItems;
	}
	
	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType equipmentSlotType) {
		switch(equipmentSlotType.getSlotType()) {
			case HAND: {
				return this.handItems.get(equipmentSlotType.getIndex());
			}
			case ARMOR: {
				return this.armorItems.get(equipmentSlotType.getIndex());
			}
			default: {
				return ItemStack.EMPTY;
			}
		}
	}
	
	@Override
	public void setItemStackToSlot(EquipmentSlotType equipmentSlotType, ItemStack stack) {
		switch(equipmentSlotType.getSlotType()) {
			case HAND: {
				this.handItems.set(equipmentSlotType.getIndex(), stack);
				break;
			}
			case ARMOR: {
				this.armorItems.set(equipmentSlotType.getIndex(), stack);
				break;
			}
		}
	}
	
	@Override
	public HandSide getPrimaryHand() {
		return HandSide.RIGHT;
	}
	
	public void setDisplayName(ITextComponent name) {
		this.displayName = name;
		this.dataManager.set(DISPLAY_NAME, name);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return this.displayName;
	}
	
	public void setEntityTexture(ResourceLocation textureLocation) {
		this.npcTexture = textureLocation;
		this.dataManager.set(TEXTURE_LOCATION, textureLocation.toString());
	}
	
	public ResourceLocation getEntityTexture() {
		return this.npcTexture;
	}
	
	public boolean isWearing(PlayerModelPart playerModelPart) {
		return false;
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		
		// Write armor items.
		ListNBT armorItemsNBT = new ListNBT();
		for(ItemStack itemstack : this.armorItems) {
			CompoundNBT itemNBT = new CompoundNBT();
			if(!itemstack.isEmpty()) {
				itemstack.write(itemNBT);
			}
			armorItemsNBT.add(itemNBT);
		}
		compound.put("ArmorItems", armorItemsNBT);
		
		// Write hand items.
		ListNBT handItemsNBT = new ListNBT();
		for(ItemStack itemstack : this.handItems) {
			CompoundNBT itemNBT = new CompoundNBT();
			if(!itemstack.isEmpty()) {
				itemstack.write(itemNBT);
			}
			handItemsNBT.add(itemNBT);
		}
		compound.put("HandItems", handItemsNBT);
		
		// Write pose.
		CompoundNBT poseNBT = new CompoundNBT();
		if(!DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
			poseNBT.put("Head", this.headRotation.writeToNBT());
		}
		if(!DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
			poseNBT.put("Body", this.bodyRotation.writeToNBT());
		}
		if(!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
			poseNBT.put("LeftArm", this.leftArmRotation.writeToNBT());
		}
		if(!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
			poseNBT.put("RightArm", this.rightArmRotation.writeToNBT());
		}
		if(!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
			poseNBT.put("LeftLeg", this.leftLegRotation.writeToNBT());
		}
		if(!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
			poseNBT.put("RightLeg", this.rightLegRotation.writeToNBT());
		}
		compound.put("Pose", poseNBT);
		
		// Write display name.
		if(this.displayName != null) {
			compound.putString("DisplayName", ITextComponent.Serializer.toJson(this.displayName));
		}
		
		// Write texture location.
		if(!this.npcTexture.equals(DEFAULT_NPC_TEXTURE)) {
			compound.putString("Texture", this.npcTexture.toString());
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		
		// Read armor items.
		if(compound.contains("ArmorItems", 9)) {
			ListNBT armorItemsNBT = compound.getList("ArmorItems", 10);
			for(int i = 0; i < this.armorItems.size(); i++) {
				this.armorItems.set(i, ItemStack.read(armorItemsNBT.getCompound(i)));
			}
		}
		
		// Read hand items.
		if(compound.contains("HandItems", 9)) {
			ListNBT handItemsNBT = compound.getList("HandItems", 10);
			for(int i = 0; i < this.handItems.size(); i++) {
				this.handItems.set(i, ItemStack.read(handItemsNBT.getCompound(i)));
			}
		}
		
		// Read pose.
		CompoundNBT poseNBT = compound.getCompound("Pose");
		ListNBT poseHeadNBT = poseNBT.getList("Head", 5);
		this.setHeadRotation(poseHeadNBT.isEmpty() ? DEFAULT_HEAD_ROTATION : new Rotations(poseHeadNBT));
		ListNBT poseBodyNBT = poseNBT.getList("Body", 5);
		this.setBodyRotation(poseBodyNBT.isEmpty() ? DEFAULT_BODY_ROTATION : new Rotations(poseBodyNBT));
		ListNBT poseLeftArmNBT = poseNBT.getList("LeftArm", 5);
		this.setLeftArmRotation(poseLeftArmNBT.isEmpty() ? DEFAULT_LEFTARM_ROTATION : new Rotations(poseLeftArmNBT));
		ListNBT poseRightArmNBT = poseNBT.getList("RightArm", 5);
		this.setRightArmRotation(poseRightArmNBT.isEmpty() ? DEFAULT_RIGHTARM_ROTATION : new Rotations(poseRightArmNBT));
		ListNBT poseLeftLegNBT = poseNBT.getList("LeftLeg", 5);
		this.setLeftLegRotation(poseLeftLegNBT.isEmpty() ? DEFAULT_LEFTLEG_ROTATION : new Rotations(poseLeftLegNBT));
		ListNBT poseRightLegNBT = poseNBT.getList("RightLeg", 5);
		this.setRightLegRotation(poseRightLegNBT.isEmpty() ? DEFAULT_RIGHTLEG_ROTATION : new Rotations(poseRightLegNBT));
		
		// Read display name.
		if(compound.contains("DisplayName", 8)) {
			String displayNameJson = compound.getString("DisplayName");
			try {
				this.setDisplayName(ITextComponent.Serializer.getComponentFromJson(displayNameJson));
			} catch (JsonParseException e) {
				this.setDisplayName(new StringTextComponent(displayNameJson));
			}
		}
		
		// Read texture location.
		if(compound.contains("Texture", 8)) {
			try {
				this.setEntityTexture(new ResourceLocation(compound.getString("Texture")));
			} catch (ResourceLocationException e) {
				this.setEntityTexture(DEFAULT_NPC_TEXTURE);
			}
		}
	}
	
	public void setHeadRotation(Rotations vec) {
		this.headRotation = vec;
		this.dataManager.set(HEAD_ROTATION, vec);
	}
	
	public void setBodyRotation(Rotations vec) {
		this.bodyRotation = vec;
		this.dataManager.set(BODY_ROTATION, vec);
	}
	
	public void setLeftArmRotation(Rotations vec) {
		this.leftArmRotation = vec;
		this.dataManager.set(LEFT_ARM_ROTATION, vec);
	}
	
	public void setRightArmRotation(Rotations vec) {
		this.rightArmRotation = vec;
		this.dataManager.set(RIGHT_ARM_ROTATION, vec);
	}
	
	public void setLeftLegRotation(Rotations vec) {
		this.leftLegRotation = vec;
		this.dataManager.set(LEFT_LEG_ROTATION, vec);
	}
	
	public void setRightLegRotation(Rotations vec) {
		this.rightLegRotation = vec;
		this.dataManager.set(RIGHT_LEG_ROTATION, vec);
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getHeadRotation() {
		return this.headRotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getBodyRotation() {
		return this.bodyRotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getLeftArmRotation() {
		return this.leftArmRotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getRightArmRotation() {
		return this.rightArmRotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getLeftLegRotation() {
		return this.leftLegRotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public Rotations getRightLegRotation() {
		return this.rightLegRotation;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// Update data manager parameters that should always immediately change client-sided when changed server-sided.
		Rotations headRotation = this.dataManager.get(HEAD_ROTATION);
		if(!this.headRotation.equals(headRotation)) {
			this.setHeadRotation(headRotation);
		}
		Rotations bodyRotation = this.dataManager.get(BODY_ROTATION);
		if(!this.bodyRotation.equals(bodyRotation)) {
			this.setBodyRotation(bodyRotation);
		}
		Rotations leftArmRotation = this.dataManager.get(LEFT_ARM_ROTATION);
		if(!this.leftArmRotation.equals(leftArmRotation)) {
			this.setLeftArmRotation(leftArmRotation);
		}
		Rotations rightArmRotation = this.dataManager.get(RIGHT_ARM_ROTATION);
		if(!this.rightArmRotation.equals(rightArmRotation)) {
			this.setRightArmRotation(rightArmRotation);
		}
		Rotations leftLegRotation = this.dataManager.get(LEFT_LEG_ROTATION);
		if(!this.leftLegRotation.equals(leftLegRotation)) {
			this.setLeftLegRotation(leftLegRotation);
		}
		Rotations rightLegRotation = this.dataManager.get(RIGHT_LEG_ROTATION);
		if(!this.rightLegRotation.equals(rightLegRotation)) {
			this.setRightLegRotation(rightLegRotation);
		}
		ITextComponent displayName = this.dataManager.get(DISPLAY_NAME);
		if(!this.displayName.equals(displayName)) {
			this.setDisplayName(displayName);
		}
		
		ResourceLocation textureLocation;
		try {
			textureLocation = new ResourceLocation(this.dataManager.get(TEXTURE_LOCATION));
		} catch (ResourceLocationException e) {
			textureLocation = DEFAULT_NPC_TEXTURE;
		}
		if(!this.npcTexture.equals(textureLocation)) {
			this.setEntityTexture(textureLocation);
		}
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		
		// Disable collision unless this entity can be pushed.
		if(this.canBePushed()) {
			super.collideWithEntity(entityIn);
		}
	}
}
