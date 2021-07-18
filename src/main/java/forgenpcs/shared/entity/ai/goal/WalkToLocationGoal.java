package forgenpcs.shared.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.vector.Vector3d;

public class WalkToLocationGoal extends Goal {
	
	private final MobEntity entity;
	private final double speed;
	private final Vector3d targetLocation;
	
	public WalkToLocationGoal(MobEntity entity, Vector3d targetLocation, double speed) {
		this.entity = entity;
		this.targetLocation = targetLocation;
		this.speed = speed;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
	}
	
	public Vector3d getTargetLocation() {
		return this.targetLocation;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	@Override
	public boolean shouldExecute() {
		return this.targetLocation != null && (this.entity.getPosX() != this.targetLocation.x
				|| this.entity.getPosY() != this.targetLocation.y || this.entity.getPosZ() != this.targetLocation.z);
	}
	
	@Override
	public void startExecuting() {
		PathNavigator navigator = this.entity.getNavigator();
		navigator.setSearchDepthMultiplier(1000f);
		navigator.setPath(navigator.pathfind(
				this.targetLocation.x, this.targetLocation.y, this.targetLocation.z, 0), this.speed);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.entity.getNavigator().noPath();
	}
	
	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}
}
