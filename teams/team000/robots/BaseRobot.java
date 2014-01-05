package team000.robots;

import java.util.PriorityQueue;
import java.util.Queue;

import team000.common.Constants;
import team000.messaging.Message;
import team000.pathing.PathingAlgorithm;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public abstract class BaseRobot {
	
	protected RobotController rc;
	private Queue<Message> priorityQueue;
	private PathingAlgorithm pathingAlgorithm;

	public BaseRobot(RobotController rc) {
		this.rc = rc;
		priorityQueue = new PriorityQueue<Message>();
	}

	abstract public void runProgram();

	public void broadcast(Message message) {
		try {
			rc.broadcast(Constants.BROADCAST_CHANNEL, message.encode());
			if (Constants.VERBOSE) {
				rc.setIndicatorString(Constants.INDICATOR_MESSAGE, message.toString());
			}
		} catch (GameActionException e) {
			e.printStackTrace();
		}

	}

	public void listenToBroadcasts() {
		try {
			int encodedMessage = rc.readBroadcast(Constants.BROADCAST_CHANNEL);
			Message decodedMessage = Message.decode(encodedMessage);
			priorityQueue.add(decodedMessage);
		} catch (GameActionException e) {
			printErrorMessage(e);
		}
	}

	public void moveTowardsTarget(MapLocation mapLocation) {
		if (rc.isActive()) {
			pathingAlgorithm.moveTowards(mapLocation);
		}
	}

	abstract public void enemyInSightAction(RobotInfo robotInfo);

	public boolean isEnemyInRange() {
		Robot[] robots = senseNearbyEnemyRobots();
		return robots.length > 0;
	}

	public void attackRobot(RobotInfo robotInfo) {
		try {
			if (rc.isActive()) {
				rc.attackSquare(robotInfo.location);
			}
		} catch (GameActionException e) {
			printErrorMessage(e);
		}
	}

	public RobotInfo getWeakestEnemyInRange(Robot[] robots) {
		if (robots == null || robots.length == 0) {
			return null;
		}
		RobotInfo weakest = null;
		try {
			weakest = rc.senseRobotInfo(robots[0]);
			for (Robot robot : robots) {
				RobotInfo robotInfo = rc.senseRobotInfo(robot);
				if (robotInfo.energon < weakest.energon) {
					weakest = robotInfo;
				}
			}
		} catch (GameActionException e) {
			printErrorMessage(e);
		}

		return weakest;
	}

	public Robot[] senseNearbyEnemyRobots() {
		return rc.senseNearbyGameObjects(Robot.class, 10, rc.getTeam().opponent());
	}

	public void setPathingAlgorithm(PathingAlgorithm pathingAlgorithm) {
		this.pathingAlgorithm = pathingAlgorithm;
	}

	public MapLocation getLocation() {
		return rc.getLocation();
	}

	public boolean isMineLocatedAtMapLocation(MapLocation mapLocation) {
		return rc.senseMine(mapLocation)!=null;
	}

	public RobotController getRobotController() {
		return rc;
	}
	
	public void setRobotIndicator(int index, String indicatorMessage) {
		if (Constants.VERBOSE) {
			rc.setIndicatorString(index, indicatorMessage);
		}
	}
	
	/**
	 * This canMove location takes into consideration mines.  If a mine is there it will return false.
	 * @param location
	 * @return
	 */
	public boolean canMove(Direction direction) { 
		return rc.canMove(direction) && !isMineLocatedAtMapLocation(rc.getLocation().add(direction));

	}
	
	public void printErrorMessage(GameActionException e) {
		setRobotIndicator(Constants.INDICATOR_GENERAL, e.getMessage());
		if (Constants.VERBOSE) {
			e.printStackTrace();
		}
	}
}
