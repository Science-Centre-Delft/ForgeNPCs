package forgenpcs.client.renderer.entity.model;

import forgenpcs.shared.entity.NPCEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NPCArmorModel extends BipedModel<NPCEntity> {
	
	public NPCArmorModel(float modelSize) {
		this(modelSize, 64, 32);
	}
	
	protected NPCArmorModel(float modelSize, int textureWidthIn, int textureHeightIn) {
		super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
	}
	
	/**
	 * Sets this entity's model rotation angles.
	 */
	@Override
	public void setRotationAngles(NPCEntity entityIn,
			float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.bipedHead.rotateAngleX = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getX();
		this.bipedHead.rotateAngleY = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getY();
		this.bipedHead.rotateAngleZ = ((float) Math.PI / 180f) * entityIn.getHeadRotation().getZ();
		this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
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
	}
}
