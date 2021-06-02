package forgenpcs;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderFakePlayer extends RendererLivingEntity<FakePlayerEntity> {
	
	// TODO - Make this adjustable through NBT data.
	public static final ResourceLocation FAKE_PLAYER_TEXTURE =
			new ResourceLocation(ForgeNPCsMod.MODID + ":textures/entity/fakeplayer/steve.png");
	
	public RenderFakePlayer(RenderManager renderManager) {
		super(renderManager, new ModelPlayer(0.0F, false), 0.5F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
//		this.addLayer(new LayerDeadmau5Head(this)); // TODO - Create custom Layer for this if necessary.
//		this.addLayer(new LayerCape(this)); // TODO - Create custom Layer for this if necessary.
		this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	}
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(FakePlayerEntity entity) {
		return FAKE_PLAYER_TEXTURE;
	}
	
	public ModelPlayer getMainModel() {
		return (ModelPlayer) super.getMainModel();
	}
	
	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(FakePlayerEntity fakePlayer, double x, double y, double z, float entityYaw, float partialTicks) {
		double d0 = (fakePlayer.isSneaking() ? y - 0.125D : y);
		this.setModelVisibilities(fakePlayer);
		super.doRender(fakePlayer, x, d0, z, entityYaw, partialTicks);
	}
	
	private void setModelVisibilities(FakePlayerEntity fakePlayer) {
		ModelPlayer modelplayer = this.getMainModel();
		
		ItemStack heldItemStack = null; // TODO - Can set current held item stack here if desired.
		modelplayer.setInvisible(true);
		modelplayer.bipedHeadwear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.HAT);
		modelplayer.bipedBodyWear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.JACKET);
		modelplayer.bipedLeftLegwear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
		modelplayer.bipedRightLegwear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
		modelplayer.bipedLeftArmwear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
		modelplayer.bipedRightArmwear.showModel = fakePlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
		modelplayer.heldItemLeft = 0;
		modelplayer.aimedBow = false;
		modelplayer.isSneak = fakePlayer.isSneaking();
		
		if(heldItemStack == null) {
			modelplayer.heldItemRight = 0;
		} else {
			modelplayer.heldItemRight = 1;
			
			// TODO - Mimic item in use count if necessary. Decrements each tick, used for actions.
//			if(fakePlayer.getItemInUseCount() > 0) {
//				EnumAction enumaction = heldItemStack.getItemUseAction();
//				
//				if(enumaction == EnumAction.BLOCK) {
//					modelplayer.heldItemRight = 3;
//				} else if(enumaction == EnumAction.BOW) {
//					modelplayer.aimedBow = true;
//				}
//			}
		}
	}
	
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}
	
	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(FakePlayerEntity entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;
		GlStateManager.scale(f, f, f);
	}
	
	protected void renderOffsetLivingLabel(FakePlayerEntity entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
		// TODO - This code can be useful to render text below the fake player name. Decide whether we want to use this or not.
//		if(p_177069_10_ < 100.0D) {
//			Scoreboard scoreboard = entityIn.getWorldScoreboard();
//			ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
//			
//			if(scoreobjective != null) {
//				Score score = scoreboard.getValueFromObjective(entityIn.getName(), scoreobjective);
//				this.renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
//				y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * p_177069_9_);
//			}
//		}
		
		super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
	}
	
	public void renderRightArm(FakePlayerEntity fakePlayer) {
		float f = 1.0F;
		GlStateManager.color(f, f, f);
		ModelPlayer modelplayer = this.getMainModel();
		this.setModelVisibilities(fakePlayer);
		modelplayer.swingProgress = 0.0F;
		modelplayer.isSneak = false;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, fakePlayer);
		modelplayer.renderRightArm();
	}
	
	public void renderLeftArm(FakePlayerEntity fakePlayer) {
		float f = 1.0F;
		GlStateManager.color(f, f, f);
		ModelPlayer modelplayer = this.getMainModel();
		this.setModelVisibilities(fakePlayer);
		modelplayer.isSneak = false;
		modelplayer.swingProgress = 0.0F;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, fakePlayer);
		modelplayer.renderLeftArm();
	}
}
