package forgenpcs.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import forgenpcs.NPCEntity;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NPCModel<T extends NPCEntity> extends BipedModel<T> {
	
	private List<ModelRenderer> modelRenderers = Lists.newArrayList();
	public final ModelRenderer bipedLeftArmwear;
	public final ModelRenderer bipedRightArmwear;
	public final ModelRenderer bipedLeftLegwear;
	public final ModelRenderer bipedRightLegwear;
	public final ModelRenderer bipedBodyWear;
	private final ModelRenderer bipedCape;
	
	public NPCModel(float modelSize) {
		super(RenderType::getEntityTranslucent, modelSize, 0.0f, 64, 64);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0f, 0.0f, -1.0f, 10.0f, 16.0f, 1.0f, modelSize);
		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize + 0.25f);
		this.bipedLeftArmwear.setRotationPoint(5.0f, 2.0f, 0.0f);
		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize + 0.25f);
		this.bipedRightArmwear.setRotationPoint(-5.0f, 2.0f, 10.0f);
		
		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize);
		this.bipedLeftLeg.setRotationPoint(1.9f, 12.0f, 0.0f);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize + 0.25f);
		this.bipedLeftLegwear.setRotationPoint(1.9f, 12.0f, 0.0f);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, modelSize + 0.25f);
		this.bipedRightLegwear.setRotationPoint(-1.9f, 12.0f, 0.0f);
		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, modelSize + 0.25f);
		this.bipedBodyWear.setRotationPoint(0.0f, 0.0f, 0.0f);
	}
	
	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.bipedLeftLegwear, this.bipedRightLegwear, this.bipedLeftArmwear, this.bipedRightArmwear, this.bipedBodyWear));
	}
	
	public void renderCape(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
		this.bipedCape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
	
	/**
	 * Sets this entity's model rotation angles.
	 */
	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		
		// Set body part rotations.
		this.bipedHead.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getX();
		this.bipedHead.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getY();
		this.bipedHead.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getZ();
		this.bipedHead.setRotationPoint(0.0f, 1.0f, 0.0f);
		this.bipedBody.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getBodyRotation().getX();
		this.bipedBody.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getBodyRotation().getY();
		this.bipedBody.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getBodyRotation().getZ();
		this.bipedLeftArm.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getLeftArmRotation().getX();
		this.bipedLeftArm.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getLeftArmRotation().getY();
		this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getLeftArmRotation().getZ();
		this.bipedRightArm.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getRightArmRotation().getX();
		this.bipedRightArm.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getRightArmRotation().getY();
		this.bipedRightArm.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getRightArmRotation().getZ();
		this.bipedLeftLeg.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getLeftLegRotation().getX();
		this.bipedLeftLeg.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getLeftLegRotation().getY();
		this.bipedLeftLeg.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getLeftLegRotation().getZ();
		this.bipedLeftLeg.setRotationPoint(1.9f, 11.0f, 0.0f);
		this.bipedRightLeg.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getRightLegRotation().getX();
		this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getRightLegRotation().getY();
		this.bipedRightLeg.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getRightLegRotation().getZ();
		this.bipedRightLeg.setRotationPoint(-1.9f, 11.0f, 0.0f);
		this.bipedHeadwear.copyModelAngles(this.bipedHead);
		
		// Set worn armor rotations.
		this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
		this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
		this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
		this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
		this.bipedBodyWear.copyModelAngles(this.bipedBody);
		if(entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
			if(entityIn.isCrouching()) {
				this.bipedCape.rotationPointZ = 1.4f;
				this.bipedCape.rotationPointY = 1.85f;
			} else {
				this.bipedCape.rotationPointZ = 0.0f;
				this.bipedCape.rotationPointY = 0.0f;
			}
		} else if(entityIn.isCrouching()) {
			this.bipedCape.rotationPointZ = 0.3f;
			this.bipedCape.rotationPointY = 0.8f;
		} else {
			this.bipedCape.rotationPointZ = -1.1f;
			this.bipedCape.rotationPointY = -0.85f;
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedLeftArmwear.showModel = visible;
		this.bipedRightArmwear.showModel = visible;
		this.bipedLeftLegwear.showModel = visible;
		this.bipedRightLegwear.showModel = visible;
		this.bipedBodyWear.showModel = visible;
		this.bipedCape.showModel = visible;
	}
	
	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
		ModelRenderer modelrenderer = this.getArmForSide(sideIn);
		modelrenderer.translateRotate(matrixStackIn);
	}
	
	public ModelRenderer getRandomModelRenderer(Random randomIn) {
		return this.modelRenderers.get(randomIn.nextInt(this.modelRenderers.size()));
	}
	
	@Override
	public void accept(ModelRenderer modelRenderer) {
		if(this.modelRenderers == null) {
			this.modelRenderers = Lists.newArrayList();
		}
		this.modelRenderers.add(modelRenderer);
	}
}
