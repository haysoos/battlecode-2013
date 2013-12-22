package team000.robots;

import team000.messaging.Intent;
import team000.messaging.Message;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Upgrade;

public class HQRobot extends BaseRobot {

	public HQRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void runProgram() {

		researchDefusion();

		while (true) {
			listenToBroadcasts();
			if (isEnemyInRange()) {
				enemyInRange(null);
			}
			spawnSoldier();
			rc.yield();
		}
	}

	private void researchDefusion() {
		while (!rc.hasUpgrade(Upgrade.DEFUSION)) {
			try {
				rc.researchUpgrade(Upgrade.DEFUSION);
			} catch (GameActionException e) {
				e.printStackTrace();
			}
		}
	}

	private void spawnSoldier() {
		if (rc.isActive()) {
			for (Direction direction : Direction.values()) {
				if (canSpawn(direction)) {
					try {
						rc.spawn(direction);
					} catch (GameActionException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	private boolean canSpawn(Direction direction) {
		return !direction.equals(Direction.OMNI) && !direction.equals(Direction.NONE) && rc.canMove(direction)
				&& !isMineLocatedAtMapLocation(rc.getLocation().add(direction));
	}

	@Override
	public void enemyInRange(RobotInfo robotInfo) {
		Message message = returnToBaseMessage();
		broadcast(message);
	}

	private Message returnToBaseMessage() {
		Message message = new Message();
		message.setChannel(1337);
		message.setIntent(Intent.RETURN_TO_BASE);
		message.setMapLocation(getLocation());
		return message;
	}


}
