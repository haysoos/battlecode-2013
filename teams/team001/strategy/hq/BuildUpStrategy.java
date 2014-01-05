/**
 * 
 */
package team001.strategy.hq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.Team;
import battlecode.common.Upgrade;
import team001.common.Constants;
import team001.communication.Message;
import team001.communication.MessageType;
import team001.robots.HQRobot;
import team001.strategy.hq.HQStrategy;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class BuildUpStrategy extends HQStrategy {

	private int numberOfResourcers = 0;
	private int numberOfDefenders = 0;
	private Set<Robot> robots = new HashSet<Robot>();
	private int rotations = 0;
	private int roundSentAttack;
	private boolean attackSent = false;
	private static final int ROTATION_STUCK_COUNT = 8;
	private static final int GLOBAL_CHANNEL_RESET_DELAY = 20;
	private boolean initialized = false;
	private int defenderLimit = 15;
	private int resourceLimit = 3;
	private List<MapLocation> resourcesRequested = new ArrayList<MapLocation>();
	private static final int START_RESOURCE_GRAB_CLOCKNUM = 400;
	
	/* (non-Javadoc)
	 * @see team001.strategy.Strategy#runStrategy(team000.robots.BasePlayer)
	 */
	@Override
	public void runStrategy(HQRobot robot) throws Exception {
		
		spawnMode(robot);
		robot.researchUpgrades();

	}
	
	private void spawnMode(HQRobot hq){
		
		boolean stuck = false;
		RobotController rc = hq.getRobotController();
		Direction dir = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
		Team mine;

		if(!initialized){
			initialize(rc);
		}

		while (rc.isActive()){

			if(attackSent && Clock.getRoundNum() - roundSentAttack > GLOBAL_CHANNEL_RESET_DELAY){
				try {
					hq.clearBroadcast(1);
					attackSent = false;
				} catch (GameActionException e) {
				}
			}

			mine = rc.senseMine(rc.getLocation().add(dir));

			while(!( (rc.canMove(dir) && (mine == null)) 
					||(rc.canMove(dir) && mine.equals(rc.getTeam()))
					 )
				  ){
				dir = dir.rotateRight();
				rotations++;

				if(rotations > ROTATION_STUCK_COUNT){
					stuck = true;
					break;
				}
				
				mine = rc.senseMine(rc.getLocation().add(dir));
			}

			try {
				if(!stuck){
					//rc.setIndicatorString(Constants.INDICATOR_MODE, "Team Power: " + rc.getTeamPower() + " Byte code left: " + Clock.getBytecodesLeft());
					if(Clock.getBytecodesLeft() > 20d){
						rc.spawn(dir);
					}
					
				}
				//if we don't yield, then we can't sense the object next to the spawn-- spawn happens at the end of the round always.
				rc.yield();

				//Need to sense all nearby robots, just in case they start running quickly.
				Robot[] sensedRobots = rc.senseNearbyGameObjects(Robot.class);

				for(Robot r : sensedRobots){

					if(robots.contains(r) || rc.getTeam().opponent().equals(r.getTeam())){
						continue;
					}

					Message message = new Message();
					message.setRobotID(((Robot)r).getID());
					message.setLocation(rc.senseEnemyHQLocation());

					if(numberOfResourcers < resourceLimit){
						
						MapLocation location;
						location = hq.getClosestLocation(rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL), resourcesRequested);
						resourcesRequested.add(location);
						message.setLocation(location);
						//rc.setIndicatorString(Constants.INDICATOR_MODE, "ignore: " + Arrays.toString(resourcesRequested.toArray()) + " sensed: " + Arrays.toString(rc.senseEncampmentSquares(rc.getLocation(), 10000, Team.NEUTRAL)));
						
						message.setMessageType(MessageType.RESOURCE);
						numberOfResourcers++;
					} else if(numberOfDefenders <= defenderLimit){
						message.setMessageType(MessageType.DEFEND);
						numberOfDefenders++;
					} else {
						message.setRobotID(1);
						message.setMessageType(MessageType.ATTACK);
						roundSentAttack = Clock.getRoundNum();
						attackSent = true;
						numberOfDefenders = 0;
						resourceLimit++;
						//defenderLimit++;
					}

					hq.sendMessage(message);
					robots.add(r);

					if(Constants.VERBOSE){
						rc.setIndicatorString(Constants.INDICATOR_DEBUG, "Sending message " + message);
					}
				}

				if(stuck){

					Message message = new Message();
					message.setRobotID(1);
					message.setLocation(rc.senseEnemyHQLocation());
					message.setMessageType(MessageType.ATTACK);
					roundSentAttack = Clock.getRoundNum();
					attackSent = true;
					numberOfDefenders = 0;
					hq.sendMessage(message);
					rc.researchUpgrade(Upgrade.NUKE);	
				}
				
				if(Clock.getRoundNum() > START_RESOURCE_GRAB_CLOCKNUM){
					resourceLimit = Math.min((rc.senseAllEncampmentSquares().length / 4) * 3, 20);
				}

				stuck = false;
				rotations = 0;

			} catch (GameActionException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	private boolean outResourced(RobotController rc) {
		try {
			return rc.senseEncampmentSquares(rc.senseEnemyHQLocation(), 1000000, rc.getTeam().opponent()).length > rc.senseAlliedEncampmentSquares().length;
		} catch (GameActionException e) {
			//e.printStackTrace();
		}
		return false;
	}

	private void initialize(RobotController rc) {
		
		if(rc.getMapHeight() * rc.getMapWidth() < 1000){
			//Small Map
			defenderLimit = 3;
			resourceLimit = 1;
		} else if(rc.getMapHeight() * rc.getMapWidth() > 3000){
			//Large Map
			defenderLimit = 15;
			resourceLimit = 5;
		}
		
		initialized = true;
		
	}

}
