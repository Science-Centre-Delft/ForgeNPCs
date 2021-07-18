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
	private final WalkToLocationCallback callback;
	
	public WalkToLocationGoal(MobEntity entity, Vector3d targetLocation, double speed) {
		this(entity, targetLocation, speed, null);
	}
	
	public WalkToLocationGoal(
			MobEntity entity, Vector3d targetLocation, double speed, WalkToLocationCallback callback) {
		this.entity = entity;
		this.targetLocation = targetLocation;
		this.speed = speed;
		this.callback = callback;
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
		return this.targetLocation != null && this.entity.getDistanceSq(this.targetLocation) >= 0.01d;
	}
	
	@Override
	public void startExecuting() {
		PathNavigator navigator = this.entity.getNavigator();
		navigator.setSearchDepthMultiplier(1000f);
		navigator.setPath(this.entity.getNavigator().pathfind(
				this.targetLocation.x, this.targetLocation.y, this.targetLocation.z, 0), this.speed);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		boolean shouldContinueExecuting = !this.entity.getNavigator().noPath();
		if(!shouldContinueExecuting) {
			this.entity.setPosition(this.targetLocation.x, this.targetLocation.y, this.targetLocation.z);
			if(this.callback != null) {
				try {
					this.callback.onPathFinished();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		return shouldContinueExecuting;
	}
	
	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}
	
	public static interface WalkToLocationCallback {
		void onPathFinished();
	}
}
