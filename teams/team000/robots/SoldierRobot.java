package team000.robots;

import team000.pathing.BugPathingAlgorithm;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class SoldierRobot extends BaseRobot {

	private MapLocation target;

	public SoldierRobot(RobotController rc) {
		super(rc);
		target = rc.senseEnemyHQLocation();
		this.setPathingAlgorithm(new BugPathingAlgorithm(this));
	}

	@Override
	public void runProgram() {
		while (true) {
			listenToBroadcasts();
			if (isEnemyInRange()) {
				RobotInfo weakestEnemy = getWeakestEnemyInRange(senseNearbyEnemyRobots());
				enemyInRange(weakestEnemy);
			}
			moveTowardsTarget(target);
			rc.yield();
		}

	}

	@Override
	public void enemyInRange(RobotInfo robotInfo) {
		if (rc.isActive()) {
			attackRobot(robotInfo);
		}
	}

	private boolean shouldAttackEnemy(Robot[] robots) {
		if (robots.length > 2) {
			return false;
		} else {
			return true;
		}
	}

}
