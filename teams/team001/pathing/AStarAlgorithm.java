/**
 * 
 */
package team001.pathing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public class AStarAlgorithm implements PathingAlgorithm {

	private Set<MapLocation> closedSet;
	private Set<MapLocation> openSet;
	private Map<MapLocation, MapLocation> cameFrom;
	private Map<MapLocation, Double> gScore;
	private Map<MapLocation, Double> fScore;
	private RobotController rc;
	private List<MapLocation> bestPath;
	private Set<MapLocation> blackList;
	//private static final int MAX_VALUE = 100*100 + 100*100 +1;


	public AStarAlgorithm(RobotController rc){
		this.rc = rc;
		MapLocation[] mines = rc.senseMineLocations(rc.getLocation(), 1000000, Team.NEUTRAL);
		blackList = new HashSet<MapLocation>(Arrays.asList(mines));


	}	

	private void init(){
		closedSet = new HashSet<MapLocation>();
		openSet = new HashSet<MapLocation>();
		fScore = new HashMap<MapLocation, Double>();
		gScore = new HashMap<MapLocation, Double>();
		bestPath = new LinkedList<MapLocation>();
	}


	public void findBestPath(MapLocation start, MapLocation goal) throws Exception {

		init();

		//Closed set is empty.  Created in constructor.
		rc.breakpoint();
		openSet.add(start);
		//The map of navigated nodes
		cameFrom = new HashMap<MapLocation, MapLocation>();

		gScore.put(start, 0d); // Cost from start along best known path.
		// Estimated total cost from start to goal through y.
		fScore.put(start, gScore.get(start) + heuristicCostEstimate(start, goal));
		double tentativeGScore;

		while(!openSet.isEmpty()){
			MapLocation current = getLowestFScore();
			//System.out.println("Lowest f score " + current);

			if(current.equals(goal)){
				//System.out.println("Found the goal");
				reconstructPath(goal);
				for(MapLocation location : bestPath){
					System.out.println(location);
				}
				//System.out.println("Finished best path");
				return;
			}

			//Remove current from open set
			openSet.remove(current);
			//Add current to closed set
			closedSet.add(current);

			for(MapLocation neighbor : getNeighbors(current)){

				if(closedSet.contains(neighbor)){
					continue;
				}

				//if(blackList.contains(neighbor)){
				//	tentativeGScore = 10000000;
				//} else {
				tentativeGScore = gScore.get(current) + current.distanceSquaredTo(neighbor);
				//}

				//tentativeGScore = gScore.get(current) + current.distanceSquaredTo(neighbor);

				if(!openSet.contains(neighbor) || (tentativeGScore < gScore.get(neighbor))){
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, tentativeGScore);
					fScore.put(neighbor, gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal));
					if(!openSet.contains(neighbor)){
						openSet.add(neighbor);
					}
				}

				if(neighbor.equals(goal)){
					//System.out.println("Found the goal");

					//					for(MapLocation key : cameFrom.keySet()){
					//						System.out.println("Key: " + key + " Value: " + cameFrom.get(key));
					//					}

					reconstructPath(goal);
					//System.out.println("Best path is size " + bestPath.size());
					//					for(MapLocation location : bestPath){
					//						System.out.println(location);
					//					}
					//System.out.println("Finished printing path");
					return;
				}

			}

		}

		throw new Exception("Could not find a path from " + start + " to " + goal);

	}

	private boolean canMoveOver(MapLocation location){

		return rc.senseTerrainTile(location).isTraversableAtHeight(rc.getRobot().getRobotLevel()) &&
				(rc.senseMine(location)==null);
	}

	/**
	 * @param start
	 * @param goal
	 * @return
	 */
	private double heuristicCostEstimate(MapLocation start, MapLocation goal) {
		return start.distanceSquaredTo(goal);
	}

	/**
	 * @param current
	 * @return
	 */
	private Iterable<MapLocation> getNeighbors(MapLocation current) {

		List<MapLocation> neighbors = new ArrayList<MapLocation>();

		neighbors.add(current.add(Direction.NORTH));
		neighbors.add(current.add(Direction.NORTH_EAST));
		neighbors.add(current.add(Direction.EAST));
		neighbors.add(current.add(Direction.SOUTH_EAST));
		neighbors.add(current.add(Direction.SOUTH));
		neighbors.add(current.add(Direction.SOUTH_WEST));
		neighbors.add(current.add(Direction.WEST));
		neighbors.add(current.add(Direction.NORTH_WEST));

		return neighbors;
	}

	/**
	 * @param cameFrom
	 * @param goal
	 */
	private void reconstructPath(MapLocation current) {

		if(cameFrom.containsKey(current)){
			reconstructPath(cameFrom.get(current));
			bestPath.add(current);
		}
		//		} else {
		//			bestPath.add(current);
		//		}


	}

	private MapLocation getLowestFScore(){

		Entry<MapLocation, Double> min = null;
		for (Entry<MapLocation, Double> entry : fScore.entrySet()) {
			if (min == null || min.getValue() > entry.getValue()) {
				min = entry;
			}
		}

		return min.getKey();

	}

	/* (non-Javadoc)
	 * @see team001.pathing.PathingAlgorithm#moveTowards(battlecode.common.MapLocation)
	 */
	@Override
	public void moveTowards(MapLocation goal) {

		MapLocation locationError = null;

		try {
			//System.out.println("Entering find best path");
			findBestPath(rc.getLocation(), goal);
			rc.breakpoint();
			Direction direction = null;

			for(MapLocation location : bestPath){

				if(location == null){
					System.out.println("Path not possible");
					return;
				}
				
				direction = rc.getLocation().directionTo(location);
				if(direction.equals(Direction.OMNI)||
						direction.equals(Direction.NONE)){
					continue;
				}

				while(!rc.isActive()){
					rc.yield();
				}

				if (rc.senseMine(location)!= null) {
					rc.defuseMine(location);
					return;
				}

				if(!rc.canMove(direction)){
					blackListLocation(location);
					moveTowards(goal);
				}

				//				if(!canMoveOver(location)){
				//					blackListLocation(location);
				//					moveTowards(goal);
				//				}



				locationError = location;
				while(!rc.isActive()){
					rc.yield();
				}
				
				if(direction.equals(Direction.OMNI) ||
						direction.equals(Direction.NONE)){
					continue;
				}
				
				if(rc.canMove(direction)){
					rc.move(direction);
				}
			}


			//System.out.println("Exiting find best path");
		} catch (Exception e) {
			System.out.println("Trying to move from " + rc.getLocation() + " to " + locationError);
			e.printStackTrace();
		}

	}

	private void blackListLocation(MapLocation location){
		blackList.add(location);
	}

}
