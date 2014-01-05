/**
 * 
 */
package team001.strategy.soldier;

import team001.communication.Message;
import team001.robots.SoldierRobot;
import team001.strategy.soldier.SoldierStrategy;

/**
 * @author Jesus Medrano
 * <a href="jesus.i.medrano@gmail.com">jesus.i.medrano@gmail.com</a>
 */
public class SoldierAwaitOrdersStrategy extends SoldierStrategy {

	private int messageCipher;
	private Message message;
	
	@Override
	public void runStrategy(SoldierRobot robotProgram) throws Exception {
		
		if(robotProgram.seekAndDestroy()){
			return;
		}
		robotProgram.readBroadcasts();
		
	}

}
