package forgenpcs.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import forgenpcs.NPCEntity;
import forgenpcs.client.renderer.entity.model.NPCArmorModel;
import forgenpcs.client.renderer.entity.model.NPCModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

public class RenderNPC extends LivingRenderer<NPCEntity, NPCModel<NPCEntity>> {
	
	public RenderNPC(EntityRendererManager renderManager) {
		super(renderManager, new NPCModel<>(0.0f), 0.5f);
		this.addLayer(new BipedArmorLayer<>(this, new NPCArmorModel(0.5f), new NPCArmorModel(1.0f)));
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new HeadLayer<>(this));
		this.addLayer(new ElytraLayer<>(this));
	}
	
	@Override
	public void render(NPCEntity npc, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		this.setModelVisibilities(npc);
		super.render(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}
	
	@Override
	public Vector3d getRenderOffset(NPCEntity npc, float partialTicks) {
		return npc.isCrouching() ? new Vector3d(0.0D, -0.125D, 0.0D) : super.getRenderOffset(npc, partialTicks);
	}
	
	private void setModelVisibilities(NPCEntity npc) {
		NPCModel<NPCEntity> model = this.getEntityModel();
		model.setVisible(true);
		model.bipedHeadwear.showModel = true;
		model.bipedBodyWear.showModel = true;
		model.bipedLeftLegwear.showModel = true;
		model.bipedRightLegwear.showModel = true;
		model.bipedLeftArmwear.showModel = true;
		model.bipedRightArmwear.showModel = true;
		model.isSneak = npc.isSneaking();
		BipedModel.ArmPose bipedmodel$armpose = getArmPose(npc, Hand.MAIN_HAND);
		BipedModel.ArmPose bipedmodel$armpose1 = getArmPose(npc, Hand.OFF_HAND);
		if(bipedmodel$armpose.func_241657_a_()) {
			bipedmodel$armpose1 = npc.getHeldItemOffhand().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
		}
		
		if(npc.getPrimaryHand() == HandSide.RIGHT) {
			model.rightArmPose = bipedmodel$armpose;
			model.leftArmPose = bipedmodel$armpose1;
		} else {
			model.rightArmPose = bipedmodel$armpose1;
			model.leftArmPose = bipedmodel$armpose;
		}
	}
	
	private static BipedModel.ArmPose getArmPose(NPCEntity npc, Hand hand) {
		ItemStack itemstack = npc.getHeldItem(hand);
		if(itemstack.isEmpty()) {
			return BipedModel.ArmPose.EMPTY;
		} else {
			if(npc.getActiveHand() == hand && npc.getItemInUseCount() > 0) {
				UseAction useAction = itemstack.getUseAction();
				if(useAction == UseAction.BLOCK) {
					return BipedModel.ArmPose.BLOCK;
				}
				if(useAction == UseAction.BOW) {
					return BipedModel.ArmPose.BOW_AND_ARROW;
				}
				if(useAction == UseAction.SPEAR) {
					return BipedModel.ArmPose.THROW_SPEAR;
				}
				if(useAction == UseAction.CROSSBOW && hand == npc.getActiveHand()) {
					return BipedModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else if(!npc.isSwingInProgress
					&& itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
				return BipedModel.ArmPose.CROSSBOW_HOLD;
			}
			return BipedModel.ArmPose.ITEM;
		}
	}
	
	/**
	 * Returns the location of this entity's texture.
	 * @param npc - The entity to get the texture from.
	 */
	@Override
	public ResourceLocation getEntityTexture(NPCEntity npc) {
		return npc.getEntityTexture();
	}
	
	@Override
	protected void preRenderCallback(NPCEntity npc, MatrixStack matrixStack, float partialTickTime) {
		float scale = 0.9375F;
		matrixStack.scale(scale, scale, scale);
	}
	
	@Override
	protected void renderName(NPCEntity npc, ITextComponent displayName,
			MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
//		double d0 = this.renderManager.squareDistanceTo(npc);
		matrixStack.push();
//		if(d0 < 100.0D) {
//			Scoreboard scoreboard = npc.getWorldScoreboard();
//			ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
//			if(scoreobjective != null) {
//				Score score = scoreboard.getOrCreateScore(npc.getScoreboardName(), scoreobjective);
//				super.renderName(npc, new StringTextComponent(Integer.toString(score.getScorePoints()))
//						.appendString(" ").appendSibling(scoreobjective.getDisplayName()), matrixStack, buffer, packedLight);
//				matrixStack.translate(0.0D, (double) (9.0F * 1.15F * 0.025F), 0.0D);
//			}
//		}
		
		super.renderName(npc, displayName, matrixStack, buffer, packedLight);
		matrixStack.pop();
	}
	
	@Override
	protected void applyRotations(NPCEntity npc, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
		float f = npc.getSwimAnimation(partialTicks);
		if(npc.isElytraFlying()) {
			super.applyRotations(npc, matrixStack, ageInTicks, rotationYaw, partialTicks);
			float f1 = (float)npc.getTicksElytraFlying() + partialTicks;
			float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if(!npc.isSpinAttacking()) {
				matrixStack.rotate(Vector3f.XP.rotationDegrees(f2 * (-90.0F - npc.rotationPitch)));
			}
			
			Vector3d vector3d = npc.getLook(partialTicks);
			Vector3d vector3d1 = npc.getMotion();
			double d0 = Entity.horizontalMag(vector3d1);
			double d1 = Entity.horizontalMag(vector3d);
			if(d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
				double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
				matrixStack.rotate(Vector3f.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
			}
		} else if(f > 0.0F) {
			super.applyRotations(npc, matrixStack, ageInTicks, rotationYaw, partialTicks);
			float f3 = npc.isInWater() ? -90.0F - npc.rotationPitch : -90.0F;
			float f4 = MathHelper.lerp(f, 0.0F, f3);
			matrixStack.rotate(Vector3f.XP.rotationDegrees(f4));
			if (npc.isActualySwimming()) {
				matrixStack.translate(0.0D, -1.0D, (double)0.3F);
			}
		} else {
			super.applyRotations(npc, matrixStack, ageInTicks, rotationYaw, partialTicks);
		}
	}
}
