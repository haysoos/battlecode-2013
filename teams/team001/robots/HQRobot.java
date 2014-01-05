/**
 * 
 */
package team001.robots;

import java.util.ArrayList;

import team001.common.Constants;
import team001.strategy.Strategies;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.Upgrade;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class HQRobot extends BasePlayer {

	private RobotController rc;
	private ArrayList<Robot> dronelist;
	/**
	 * Constructor
	 * @param rc 
	 */
	public HQRobot(RobotController rc){
		super(rc);
		this.rc = rc;
		setStrategy(Strategies.initialHQStrategy);
		dronelist = new ArrayList<Robot>(10);
		super.startProgram();
	}
	
	public boolean addDrone(Robot d) {
		dronelist.add(d);
		double eng =0;
		for (Robot r : dronelist)  {
			// TODO remove this-- very wasteful, just proof of concept
			try {
				eng += rc.senseRobotInfo(r).energon;
			} catch (GameActionException e) {
				//e.printStackTrace();
				dronelist.remove(r);
			}
			
		}
		
		if(Constants.VERBOSE){
			rc.setIndicatorString(1, "Drone Count" + dronelist.size() + "   " + eng);
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see team001.robots.BasePlayer#attackEnemy(battlecode.common.MapLocation)
	 */
	@Override
	public boolean enemyInSight(MapLocation enemyLocation) {
		try {
			if(super.getRobotController().getLocation().distanceSquaredTo(enemyLocation) <
					Constants.MINIMUM_ENEMY_VISIBLE_RANGE){
				rc.broadcast(1234, 1234);
				return true;
			} 
			
		} catch (GameActionException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Research upgrades
	 */
	public void researchUpgrades(){
		try{
			if(!rc.hasUpgrade(Upgrade.DEFUSION)){
				while(!rc.isActive()){
					rc.yield();
				}
				rc.researchUpgrade(Upgrade.DEFUSION);
			} else if(!rc.hasUpgrade(Upgrade.PICKAXE)){
				while(!rc.isActive()){
					rc.yield();
				}
				rc.researchUpgrade(Upgrade.PICKAXE);

			} else if(!rc.hasUpgrade(Upgrade.FUSION)){
				while(!rc.isActive()){
					rc.yield();
				}
				rc.researchUpgrade(Upgrade.FUSION);

			} 
			
			/*else if(!rc.hasUpgrade(Upgrade.NUKE)){

				boolean stuck = true;
				for(Direction d : Direction.values()){
					if(rc.canMove(d)){
						stuck = false;
						break;
					}
				}
				if(stuck){
					while(!rc.isActive()){
						rc.yield();
					}
					rc.researchUpgrade(Upgrade.NUKE);	
				}

			}*/
		} catch (GameActionException e){
			e.printStackTrace();
		}

	}

}
