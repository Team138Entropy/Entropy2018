package org.usfirst.frc.team138.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	protected void initDefaultCommand() {
			
	}
	
	public enum ElevatorTarget{
		etAquire,
		etSwitch,
		etScale
	}
	
	public ElevatorTarget ConvertToTarget(String target) {
		ElevatorTarget elevatorTarget;
		
		switch (target) {
		case "Aquire":
			elevatorTarget = ElevatorTarget.etAquire;
			break;
		case "Switch":
			elevatorTarget = ElevatorTarget.etSwitch;
			break;
		case "Scale":
			elevatorTarget = ElevatorTarget.etScale;
			break;
		default:
			elevatorTarget = ElevatorTarget.etAquire;
			break;
		}
		return elevatorTarget;
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
