package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousCommand extends CommandGroup {

	public AutonomousCommand(String team, String startPos, String autoMode, String gameData) {
		
		
		// Test Modes
		if (autoMode == "test" && Constants.AutoEnable)
		{
			int Dir=1;
			// Only allow Autonomous to execute once
			Constants.AutoEnable=false;
			String Target2="Switch";
			// Near Scale
			//addParallel(new ElevateToTarget(ElevatorTarget.EXCHANGE));
			addSequential(new AutoDrive(Dir*3.3));				
			addSequential(new AutoDrive(Constants.autoSpeed, 663));
			addParallel(new AutoDrive(Dir*35));
		//	addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			// Use vision to drive to scale?
//			addSequential(new StartRelease());
			addSequential(new Wait(5));
//			addSequential(new CompleteRelease());
			// 
			// Grab 2nd cube at end of near switch
			// drop elevator to acquire position
			//addParallel(new ElevateToTarget(ElevatorTarget.ACQUIRE));
			addSequential(new AutoDrive(Dir*148.7));
			addSequential(new AutoDrive(Constants.autoSpeed, 158));
			addSequential(new Wait(1));			
			addSequential(new AutoDrive(Constants.autoSpeed, 14));
			// Turn to deposit on Switch
			addSequential(new AutoDrive(Dir*175));
/*
 * //			addSequential(new StartAcquire());
//			addSequential(new CompleteAcquire());
			if (Target2=="Switch") {
//				addSequential(new ElevateToTarget(ElevatorTarget.SWITCH));
				// Drive a little closer
				addSequential(new AutoDrive(Constants.autoSpeed, 12));
				// Deposit on Switch
			}
			else
			{ // Deposit on Scale
//				addParallel(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
				addSequential(new AutoDrive(Dir*-5));
				addSequential(new AutoDrive(Constants.autoSpeed, 119));
				addSequential(new AutoDrive(Dir*0));
			}
//			addSequential(new StartRelease());
			addSequential(new Wait(1));
//			addSequential(new CompleteRelease());
//			addSequential(new CloseGrasper());
*/		}

		
		// This auto mode does the "proper action" depending on the starting position and gameData
		if (autoMode == "auto" && Constants.AutoEnable)
		{
			Constants.AutoEnable=false;
			SmartDashboard.putString("Auto", "auto");
			if (startPos.equals("left")) {
				
				if (gameData.equals("LLL")) { 
					// Cubes on near scale and switch
					// "Left" angles are inverted
					depositCubeNearScale(-1, "Switch");
				}		
				
				if (gameData.equals("RLR")) {
					// Put 2 cubes on near scale
					depositCubeNearScale(-1, "Scale");
				}
				
				if (gameData.equals("RRR") ) {
					// Cubes on Far scale and Switch
					depositCubeFarScale(-1, "Switch");
				}
				
				if (gameData.equals("LRL") ) {
					// 2 cubes on Far scale
					depositCubeFarScale(-1, "Scale");
				}
				
			}
			
			if (startPos.equals( "middle") ) {
				
				if (gameData.equals("LLL") || gameData.equals("LRL") ) 					
					depositCubeLeftSwitch(); // Left Switch
								
				if (gameData.equals("RRR") || gameData.equals( "RLR") ) 					
					depositCubeRightSwitch(); // Right Switch
			
			}
			
			if (startPos.equals("right") ) {
				if (gameData.equals("LLL") ) {
					depositCubeFarScale(1, "Switch");
				}
				
				if (gameData.equals("RRR")) {
					depositCubeNearScale(1, "Switch");
				}
				
				if (gameData.equals("LRL") ) {
					depositCubeNearScale(1, "Scale");
				}
				
				if (gameData.equals( "RLR") ) {
					depositCubeFarScale(1, "Scale");
				}
			}
		}
	}

/*	private void crossAutoLine() {
		// "Off" position
		addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceBaseLine));
	}
*/
	private void depositCubeNearScale(double Dir, String Target2)
	{
		// Near Scale
		addParallel(new ElevateToTarget(ElevatorTarget.EXCHANGE));
		addSequential(new AutoDrive(Dir*3.3));				
		addSequential(new AutoDrive(Constants.autoSpeed, 663));
		addParallel(new AutoDrive(Dir*35));
		addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
		// Use vision to drive to scale?
		addSequential(new StartRelease());
		addSequential(new Wait(Constants.releaseDelay));
		addSequential(new CompleteRelease());
		// 
		// Grab 2nd cube at end of near switch
		// drop elevator to acquire position
		addParallel(new ElevateToTarget(ElevatorTarget.ACQUIRE));
		addSequential(new AutoDrive(Dir*148.7));
		addSequential(new AutoDrive(Constants.autoSpeed, 158));
		addSequential(new StartAcquire());
		addSequential(new CompleteAcquire());
		if (Target2=="Switch") {
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			// Drive a little closer
			addSequential(new AutoDrive(Constants.autoSpeed, 14));
			// Rotate and Deposit on Switch
			addSequential(new AutoDrive(Dir*175));
		}
		else
		{ // Deposit on Scale
			addParallel(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			addSequential(new AutoDrive(Dir*-5));
			addSequential(new AutoDrive(Constants.autoSpeed, 119));
			addSequential(new AutoDrive(Dir*0));
		}
		addSequential(new StartRelease());
		addSequential(new Wait(Constants.releaseDelay));
		addSequential(new CompleteRelease());
		addSequential(new CloseGrasper());
	}

	
	private void depositCubeFarScale(double Dir, String Target2)
	{
			// Far Scale
			addParallel(new ElevateToTarget(ElevatorTarget.EXCHANGE));
			addSequential(new AutoDrive(Dir*3));				
			addSequential(new AutoDrive(Constants.autoSpeed, 567));
			// Turn towards far side
			addSequential(new AutoDrive(Dir*80));
			// Drive across field
			addSequential(new AutoDrive(Constants.autoSpeed, 449));
			// Turn towards Scale
			addParallel(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			addSequential(new AutoDrive(Dir*0));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			// 
			// Grab 2nd cube at end of far switch
			// drop elevator to acquire position
			addParallel(new ElevateToTarget(ElevatorTarget.ACQUIRE));
			addSequential(new AutoDrive(Dir*-179));
			addSequential(new AutoDrive(Constants.autoSpeed, 119));	
			addSequential(new StartAcquire());
			addSequential(new CompleteAcquire());
			if (Target2=="Switch") {
				addSequential(new ElevateToTarget(ElevatorTarget.SWITCH));
				// Drive a little closer
				addSequential(new AutoDrive(Constants.autoSpeed, 6));
				// Deposit on Switch
			}
			else
			{ // Deposit on Scale
				addParallel(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
				addSequential(new AutoDrive(Dir*1));
				addSequential(new AutoDrive(Constants.autoSpeed, 119));
			}
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
	
	
	private void depositCubeLeftSwitch()
	{
			// Center start
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, 55));
			addSequential(new AutoDrive(90));
			addSequential(new AutoDrive(Constants.autoSpeed, 197));
			addSequential(new AutoDrive(0));
			addSequential(new AutoDrive(Constants.autoSpeed, 195));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
	}
	
	private void depositCubeRightSwitch()
	{
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, 55));
			addSequential(new AutoDrive(-27));
			addSequential(new AutoDrive(Constants.autoSpeed, 168));
			addSequential(new AutoDrive(0));
			addSequential(new AutoDrive(Constants.autoSpeed, 45));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
	}
	

}