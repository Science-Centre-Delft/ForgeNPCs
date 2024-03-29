package forgenpcs.shared.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class WalkToLocationGoal extends Goal {
	
	private final MobEntity entity;
	private final double speed;
	private final Vector3d targetLocation;
	private final WalkToLocationCallback callback;
	
	private static final int MAX_INITIAL_NO_PATH_TICKS = 10;
	private static final int MAX_NO_PATH_TICKS = 10;
	private int noPathTicks;
	private boolean hasFoundInitialPath = false;
	private boolean hasReachedTarget = false;
	
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
		return !this.hasReachedTarget && this.targetLocation != null;
	}
	
	@Override
	public void startExecuting() {
		PathNavigator navigator = this.entity.getNavigator();
		navigator.setSearchDepthMultiplier(1000f);
		navigator.setPath(navigator.pathfind(
				this.targetLocation.x, this.targetLocation.y, this.targetLocation.z, 0), this.speed);
		this.hasFoundInitialPath = navigator.hasPath();
		this.noPathTicks = 0;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}
	
	@Override
	public void tick() {
		if(!this.hasReachedTarget && this.entity.getNavigator().noPath()) {
			this.entity.getNavigator().setPath(this.entity.getNavigator().pathfind(
					this.targetLocation.x, this.targetLocation.y, this.targetLocation.z, 0), this.speed);
			boolean hasFoundPath = this.entity.getNavigator().hasPath();
			
			if(this.entity.getDistanceSq(this.targetLocation) <= 1d) {
				
				// Snap to target position when close enough.
				this.teleportToTarget();
			} else {
				
				// Teleport to target position if no path has been found for longer than the allowed tick threshold.
				if(!hasFoundPath) {
					this.noPathTicks++;
					if(this.noPathTicks > (this.hasFoundInitialPath ? MAX_NO_PATH_TICKS : MAX_INITIAL_NO_PATH_TICKS)) {
						this.teleportToTarget();
					}
				} else {
					this.hasFoundInitialPath = true;
					this.noPathTicks = 0;
				}
			}
		}
	}
	
	private void teleportToTarget() {
		this.entity.getNavigator().clearPath();
		this.entity.setPosition(this.targetLocation.x, this.targetLocation.y, this.targetLocation.z);
		this.hasReachedTarget = true;
		this.callPathFinishedCallback();
	}
	
	private void callPathFinishedCallback() {
		if(this.callback != null) {
			try {
				this.callback.onPathFinished();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}
	
	public static interface WalkToLocationCallback {
		void onPathFinished();
	}
}
