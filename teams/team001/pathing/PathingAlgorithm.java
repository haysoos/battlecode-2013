/**
 * 
 */
package team001.pathing;

import battlecode.common.Direction;
import battlecode.common.MapLocation;

/**
 * @author Jesus Medrano
 * <a href='jesus.i.medrano@gmail.com'>jesus.i.medrano@gmail.com</a>
 */
public interface PathingAlgorithm {

	/**
	 * @param goal
	 */
	public void moveTowards(MapLocation goal);
	
}
