package team000.robots;

import team000.common.Constants;
import team000.messaging.Message;
import team000.messaging.MessageIntent;
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
				enemyInSightAction(null);
			}
			spawnSoldier();
			rc.yield();
		}
	}

	private void researchDefusion() {
		while (!rc.hasUpgrade(Upgrade.DEFUSION)) {
			try {
				if (rc.isActive() && !rc.hasUpgrade(Upgrade.DEFUSION)) {
					rc.researchUpgrade(Upgrade.DEFUSION);
				} 				
			} catch (GameActionException e) {
				printErrorMessage(e);
			}
		}
	}

	private void spawnSoldier() {
		if (rc.isActive()) {
			Direction direction = calculateBestDirectionToSpawn();
			setRobotIndicator(Constants.INDICATOR_GENERAL, "Spawning Solider at " + direction.toString());
			spawnSoliderRobot(direction);
		}
	}

	private Direction calculateBestDirectionToSpawn() {
		Direction direction = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
		while (!canMove(direction)) {
			direction.rotateRight();
		}
		return direction;
	}

	private void spawnSoliderRobot(Direction direction) {
		try {
			rc.spawn(direction);
		} catch (GameActionException e) {
			printErrorMessage(e);
		}
	}

	@Override
	public void enemyInSightAction(RobotInfo robotInfo) {
		Message message = returnToBaseMessage();
		broadcast(message);
	}

	private Message returnToBaseMessage() {
		Message message = new Message();
		message.setMessageIntent(MessageIntent.RETURN_TO_BASE);
		message.setMapLocation(getLocation());
		return message;
	}


}
