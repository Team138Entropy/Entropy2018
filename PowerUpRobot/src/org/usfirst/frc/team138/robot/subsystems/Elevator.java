package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	protected void initDefaultCommand() {
			
	}
	
	public enum ElevatorTarget{
		etAquire,
		etSwitch,
		etScale
	}
	
	public void Elevate (ElevatorTarget target) {
		switch (target) {
		case etAquire:
			break;
		case etSwitch:
			break;
		case etScale:
			break;
		default:
			//error 
			break;
			
		}
	}
}
