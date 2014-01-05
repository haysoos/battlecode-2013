/**
 * 
 */
package team001.pathing;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class SimpleBugAlgorithm implements PathingAlgorithm {

	private RobotController rc;
	private boolean bugMode = false;
	private MapLocation locationAtBugStart;
	//private MapLocation locationAtBugStuck;
	private boolean rotateLeft = true;
	private Direction myInitialDirection;


	/**
	 * Default constructor
	 * @param rc
	 */
	public SimpleBugAlgorithm(RobotController rc){
		this.rc = rc;
	}


	@Override
	public void moveTowards(MapLocation goal) {

		myInitialDirection = rc.getLocation().directionTo(goal);
		try {
			moveTowards(goal, myInitialDirection);
		} catch (GameActionException e) {
			e.printStackTrace();
		}

	}

	private void moveTowards(MapLocation goal, Direction direction) throws GameActionException{

		MapLocation myLocation = rc.getLocation();
		Direction initialDirection = direction;

		if(bugMode){

			//check to see if still need to be in bug mode
			if(canMove(myLocation, direction)){
				bugMode = false;
				return;
			}

			// follow the wall
			if(rotateLeft){
				direction = direction.rotateLeft();
			} else {
				direction = direction.rotateRight();
			}

			if(isStuck(myLocation, locationAtBugStart)){
				if(stuck(myLocation, initialDirection)){
					return;
				}
			}
			
			int i = 0;
			while(!canMove(myLocation, direction)){
				if(rotateLeft){
					direction = direction.rotateLeft();
				} else {
					direction = direction.rotateRight();
				}

				if(i > 8){
					break;
				}
				
			}

			

			// We ended up where we started
			if(myLocation.add(direction).equals(locationAtBugStart)){
				rotateLeft = !rotateLeft;
				//locationAtBugStuck = myLocation;

				// Am I stuck?
				stuck(myLocation, initialDirection);
				return;
			} 


			//should be able to move now
			while(!rc.isActive()){
				rc.yield();
			}


			if(rc.canMove(direction)){
				rc.move(direction);

			}




		} else {
			while(!rc.isActive()){
				rc.yield();
			}

			if(canMove(myLocation, direction)){
				rc.move(direction);
			} else {
				bugMode = true;
				locationAtBugStart = myLocation;
			}
		}

	}

	private boolean isStuck(MapLocation location, MapLocation forbidden){

		for(Direction direction : Direction.values()){	

			if(direction.equals(Direction.OMNI) || 
					direction.equals(Direction.NONE)){
				continue;
			}

			if(direction.opposite().equals(myInitialDirection) ||
					direction.opposite().equals(myInitialDirection.rotateLeft()) ||
					direction.opposite().equals(myInitialDirection.rotateRight())){
				continue;
			}

			Team mine = rc.senseMine(location.add(direction));
			if(rc.senseMine(location.add(direction)) == null &&
					rc.canMove(direction) &&
					!location.equals(forbidden)){
				return false;
			}

		} 

		return true;

	}

	public boolean stuck(MapLocation location, Direction direction){

		int i = 0;
		Team mine = rc.senseMine(location.add(direction));
		while(mine == null || mine.equals(rc.getTeam())){

			direction = direction.rotateRight();
			i++;
			if(i > 8){
				rotateLeft = !rotateLeft;
				return false;
			}
			
			mine = rc.senseMine(location.add(direction));
		}

		try {
			while(!rc.isActive()){
				rc.yield();
			}
			rc.defuseMine(location.add(direction));
			bugMode = false;
			return true;

		} catch (GameActionException e) {
			e.printStackTrace();
		}
		
		return false;

	}

	private boolean canMove(MapLocation location, Direction direction){
		Team mine = rc.senseMine(location.add(direction));
		if(mine != null && !mine.equals(rc.getTeam())){
			return false;
		}

		return rc.canMove(direction);
	}

}
