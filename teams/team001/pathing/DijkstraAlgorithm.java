package team001.pathing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class DijkstraAlgorithm implements PathingAlgorithm {

	private Set<MapLocation> settledNodes;
	private Set<MapLocation> unSettledNodes;
	private Map<MapLocation, MapLocation> predecessors;
	private Map<MapLocation, Double> distance;
	private RobotController rc;
	private static final double MAX_DISTANCE = GameConstants.MAP_MAX_HEIGHT * GameConstants.MAP_MAX_WIDTH + 1;


	public DijkstraAlgorithm(RobotController rc){
		this.rc = rc;
	}
	
	public void execute(MapLocation source) {
		settledNodes = new HashSet<MapLocation>();
		unSettledNodes = new HashSet<MapLocation>();
		distance = new HashMap<MapLocation, Double>();
		predecessors = new HashMap<MapLocation, MapLocation>();
		distance.put(source, 0d);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			MapLocation node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}


	private void findMinimalDistances(MapLocation node) {

		for (MapLocation target : getNeighbors(node)) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node)
						+ getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private double getDistance(MapLocation node, MapLocation target) {
		
		double distance = 0;
		GameObject go = null;
		try {
			go = rc.senseObjectAtLocation(target);
		} catch (GameActionException e) {
			distance = node.distanceSquaredTo(target);
		}
		
		if(go != null){
			distance = MAX_DISTANCE;
		} else if(rc.senseMine(target) != null){
			distance = MAX_DISTANCE;
		} else {
			distance = node.distanceSquaredTo(target);
		}
		
		return distance;
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


	 private MapLocation getMinimum(Set<MapLocation> locations) {
		 MapLocation minimum = null;
		 for (MapLocation location : locations) {
			 if (minimum == null) {
				 minimum = location;
			 } else {
				 if (getShortestDistance(location) < getShortestDistance(minimum)) {
					 minimum = location;
				 }
			 }
		 }
		 return minimum;
	 }

	 private double getShortestDistance(MapLocation destination) {
		 Double d = distance.get(destination);
		 if (d == null) {
			 return Integer.MAX_VALUE;
		 } else {
			 return d;
		 }
	 }

	 /*
	  * This method returns the path from the source to the selected target and
	  * NULL if no path exists
	  */
	 public List<MapLocation> getPath(MapLocation target) {
		 List<MapLocation> path = new LinkedList<MapLocation>();
		 MapLocation step = target;
		 // Check if a path exists
		 if (predecessors.get(step) == null) {
			 return null;
		 }
		 path.add(step);
		 while (predecessors.get(step) != null) {
			 step = predecessors.get(step);
			 path.add(step);
		 }
		 // Put it into the correct order
		 Collections.reverse(path);
		 return path;
	 }


	@Override
	public void moveTowards(MapLocation goal) {
		
		MapLocation currentLocation = null;
		System.out.println("Executing move towards " + rc.getLocation());
		execute(rc.getLocation());
		System.out.println("Execution finished ");
		List<MapLocation> path = getPath(goal);
		Direction direction = null;
		
		System.out.println("Found path of size " + path.size());
		
		for(MapLocation location : path){
			
			direction = rc.getLocation().directionTo(location);
			while(!rc.isActive()){
				rc.yield();
			}
			
			if(rc.canMove(direction)){
				try {
					rc.move(direction);
				} catch (GameActionException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
}
