package team001.pathing;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class BugAlgorithm implements PathingAlgorithm{

	private RobotController rc;
	private double distanceAtObstacle;
	private boolean bugMode = false;
	private Double slope;
	private double intercept;
	private Direction myDirection;


	public BugAlgorithm(RobotController rc){
		this.rc = rc;
	}

	private void computeMLine(MapLocation start, MapLocation end){
		
		if(end.x == start.x){
			slope = Double.NaN;
		} else {
			slope = (double) ((end.y - start.y) / (end.x - start.x));
		}
		

		intercept = start.y - slope * start.x;

	}

	@Override
	public void moveTowards(MapLocation goal) {

		Direction direction = rc.getLocation().directionTo(goal);
		try {
			moveTowards(goal, direction);
		} catch (GameActionException e) {
			e.printStackTrace();
		}

	}

	private boolean backOnMLine(MapLocation location){
		if(slope.isNaN()){
			return (Math.abs(intercept - location.y) < 2);
		} else {
			double temp = slope*location.x + intercept;
			return (Math.abs(temp - location.y) < 3);
		}
		
	}

	private void moveTowards(MapLocation goal, Direction direction) throws GameActionException{

		MapLocation myLocation = rc.getLocation();
		if(bugMode){

			if(backOnMLine(myLocation)){
				bugMode = false;
				return;
			}
			//hug the wall/obstacle

			if(myDirection != null && canMove(myLocation, myDirection)){
				
				if(myDirection.equals(direction) && canMove(myLocation, myDirection)){
					bugMode = false;
				}
				else if(canMove(myLocation, myDirection.rotateRight())){
					myDirection = myDirection.rotateRight();
				}
				rc.move(myDirection);

			} else {

				while(!canMove(myLocation, direction)){
					direction = direction.rotateLeft();
					myDirection = direction;
				}
			}



		} else {
			while(!rc.isActive()){
				rc.yield();
			}

			if(canMove(myLocation, direction)){
				rc.move(direction);
			} else {
				bugMode = true;
				computeMLine(myLocation, goal);
				//moveTowards(goal, direction.rotateLeft());

			}
		}

	}

	private boolean canMove(MapLocation location, Direction direction){
		if(rc.senseMine(location.add(direction)) != null){
			return false;
		}

		return rc.canMove(direction);
	}


}
