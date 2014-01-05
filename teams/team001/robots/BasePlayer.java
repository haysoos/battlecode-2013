/**
 * 
 */
package team001.robots;

import java.util.ArrayList;
import java.util.List;

import team001.common.Constants;
import team001.communication.Message;
import team001.pathing.PathingAlgorithm;
import team001.pathing.SimpleBugAlgorithm;
import team001.strategy.Strategies;
import team001.strategy.Strategy;
import team001.strategy.soldier.SoldierAwaitOrdersStrategy;
import team001.strategy.soldier.SoldierDefenseStrategy;
import team001.strategy.soldier.SoldierSmarterStrategy;
import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public abstract class BasePlayer {

	private RobotController rc;
	private Strategy strategy;
	private PathingAlgorithm pathingAlgorithm;
	private Message message;
	private MapLocation target;

	/**
	 * Constructor
	 * @param rc 
	 */
	public BasePlayer(RobotController rc){
		this.rc = rc;
		pathingAlgorithm = new SimpleBugAlgorithm(rc);
	}

	protected void startProgram(){

		while(true){

			try {
				rc.setIndicatorString(Constants.INDICATOR_STRATEGY, "Strategy is " + strategy.getClass().getSimpleName());
				strategy.runStrategy(this);
			} catch (Exception e) {
				e.printStackTrace();
			}

			rc.yield();

		}

	}

	public void readBroadcasts() throws GameActionException{

		//Read personal messages
		int messageCipher = rc.readBroadcast(rc.getRobot().getID());
		int channel = 0;

		if(messageCipher > 0){
			Message message = Message.decodeMessage(messageCipher);
			clearBroadcast(rc.getRobot().getID());
			setStrategyBasedOnMessage(message);
			rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Found unique message at " + Clock.getRoundNum() + " " + message);
			
			return;
		}

		rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Couldn't find unique message switching to channels");
		//Read Messages based on my strategy
		if(strategy instanceof SoldierDefenseStrategy){
			channel = 1;
		} else if(strategy instanceof SoldierSmarterStrategy){
			channel = 2;
		} else if(strategy instanceof SoldierAwaitOrdersStrategy){
			channel = 1;
		}
			
		if (channel > 0){
			rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Will be listening on channel " + channel + " at " + Clock.getRoundNum());
			messageCipher = rc.readBroadcast(channel);

			if(messageCipher > 0){
				Message message = Message.decodeMessage(messageCipher);
				rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Found unique message at " + Clock.getRoundNum() + " " + message);
				setStrategyBasedOnMessage(message);
				
			}
		}


	}

	public void clearBroadcast(int channel) throws GameActionException {
		rc.broadcast(channel, 0);
	}

	/**
	 * Sets the strategy
	 * @param strategy
	 */
	public void setStrategy(Strategy strategy){
		this.strategy = strategy;
	}

	/**
	 * @return Gets the robot controller
	 */
	public RobotController getRobotController(){
		return rc;
	}


	public void moveTowards(MapLocation goal){		
		pathingAlgorithm.moveTowards(goal);
	}

	public RobotInfo getClosestRobot(Robot[] robots) throws GameActionException{

		if(robots.length > 0){

			RobotInfo closest = rc.senseRobotInfo(robots[0]); //There should be at least one.
			for(Robot r : robots){
				if(rc.getLocation().distanceSquaredTo(closest.location) >
				rc.getLocation().distanceSquaredTo(rc.senseRobotInfo(r).location)){
					closest = rc.senseRobotInfo(r);
				}
			}

			return closest;

		} else {
			return null;
		}

	}

	public void sendMessage(Message message) throws GameActionException{
		rc.broadcast(message.getRobotID(), message.toCode());
	}

	/**
	 * @param senseAllEncampmentSquares
	 * @return
	 */
	public MapLocation getClosestLocation(MapLocation[] locations) {

		return getClosestLocation(locations, null);
	}

	/**
	 * @return
	 */
	public boolean seekAndDestroy() {

		Robot[] robots = rc.senseNearbyGameObjects(Robot.class, 1000, rc.getTeam().opponent());
		RobotInfo enemyRobot;
		if(robots.length > 0){
			try {
				enemyRobot = this.getClosestRobot(robots);
				if(rc.getLocation().distanceSquaredTo(enemyRobot.location) > 1){
					return enemyInSight(enemyRobot.location);
				}

			} catch (GameActionException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public abstract boolean enemyInSight(MapLocation enemyLocation);

	public MapLocation getClosestLocation(MapLocation[] locations,
			List<MapLocation> ignore) {

		if(locations.length > 0){

			MapLocation closest = locations[0]; //There should be at least one.
			if(ignore != null && ignore.contains(closest)){
				if(locations.length > 1){
					closest = locations[1];
				} else {
					return null;
				}
			}
			for(MapLocation location : locations){
				if(rc.getLocation().distanceSquaredTo(closest) >
				rc.getLocation().distanceSquaredTo(location)){
					if(ignore != null){
						if(!ignore.contains(location)){
							closest = location;
						}
					} else {
						closest = location;
					}

				}
			}

			return closest;

		} else {
			return null;
		}
	}

	public void setStrategyBasedOnMessage(Message message) {
		switch(message.getMessageType()){
		
		case RESOURCE:
			this.setTarget(message.getLocation());
			setStrategy(Strategies.soldierResourceStrategy);
			break;
		case ATTACK:
			setStrategy(Strategies.soldierOffensiveStrategy);
			break;
			
		case DEFEND:
			setStrategy(Strategies.soldierDefenseStrategy);
			break;
		default:
			
		}
		
	}

	private void setTarget(MapLocation target) {
		this.target = target;
		
	}

	public MapLocation getTarget() {
		return target;
	}

}
