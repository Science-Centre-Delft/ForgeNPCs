package forgenpcs;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.world.World;

public class FakePlayerEntity extends EntityLiving {
	
	/*
	 * TODO - Determine which aspects of the entity should be setable and set up custom S->C packets to sync this.
	 * Potentially interesting things to set/control:
	 *     Display name (above-head name, can be done through DataWatcher).
	 *     Held item.
	 *     Position and rotation (head and body separately).
	 *     Move from A to B, possibly through pathfinding "AI". Should have a walking animation.
	 *     Sneak state.
	 *     General arm, head, body and leg rotations (ideally with animation between them).
	 *     Ability to look at the nearest player within a given range (animating back to default rotation otherwise).
	 *         Could also move whole body when necessary to not screw the head off.
	 *     Give the player elbows and knees?
	 */
	
	public FakePlayerEntity(World world) {
		super(world);
	}
	
	public boolean isWearing(EnumPlayerModelParts modelPart) {
		// TODO - Store this in fields, eventually set from NBT.
		return false;
	}
}
