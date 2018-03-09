package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousCommand extends CommandGroup {

	public AutonomousCommand(String team, String startPos, String autoMode, String gameData) {
		
		String sameSide;
		String oppositeSide;
		
		
		// Test Modes
		if (autoMode == "test")
		{
			
//			depositCubeRightSwitch("right");
		}
		
		// This auto mode does the "proper action" depending on the starting position and gameData
		if (autoMode == "auto")
		{
			if (startPos.equals("left")) {
				
				sameSide = "left";
				oppositeSide = "right";
				
				if (gameData.equals("LLL") || gameData.equals("RLR") ) 
					depositCubeScale(startPos, sameSide);
				
				if (gameData.equals("RRR") ) 
					depositCubeScale(startPos, oppositeSide);
								
				if (gameData.equals("LRL") )
					depositCubeSwitch(startPos, sameSide);
				
			}
			
			if (startPos.equals( "middle") ) {
				
				if (gameData.equals("LLL") || gameData.equals("LRL") ) 					
					depositCubeSwitch(startPos, "left"); // Left Switch
								
				if (gameData.equals("RRR") || gameData.equals( "RLR") ) 					
					depositCubeSwitch(startPos, "right"); // Right Switch
			
			}
			
			if (startPos.equals("right") ) {
				
				sameSide = "right";
				oppositeSide = "left";
				
				if (gameData.equals("LLL") ) {
					depositCubeScale(startPos, oppositeSide);
				}
				
				if (gameData.equals("RRR") || gameData.equals("LRL") ) {
					depositCubeScale(startPos, sameSide);
				}
				
				if (gameData.equals( "RLR") ) {
					depositCubeSwitch(startPos, sameSide);
				}
			}
		}
	}

	private void crossAutoLine() {
		// "Off" position
		addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceBaseLine));
	}

	private void depositCubeScale (String startingPosition, String side)
	{
		if (side == "left") 
		{
			depositCubeLeftScale(startingPosition);
		}
		else
		{
			depositCubeRightScale(startingPosition);
		}
	}
	
	private void depositCubeLeftScale(String startingPosition)
	{
		if (startingPosition == "left") {
			// Scale on left
			addParallel(new ElevateToTarget(ElevatorTarget.EXCHANGE));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceScale));
			addParallel(new AutoDrive(Constants.right90Degrees));
			addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			// Use vision to drive to scale?
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
		else if (startingPosition == "right")
		{
			// This crosses the field to the opposite scale
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceToCrossPoint));
			addSequential(new AutoDrive(Constants.left90Degrees));
			addParallel(new AutoDrive(Constants.autoSpeed, Constants.distanceCross));
			addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			addSequential(new AutoDrive(Constants.right90Degrees));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceFinalMoveAfterCross));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.softReleaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
	}
	
	private void depositCubeRightScale(String startingPosition)
	{
		if (startingPosition == "right") {
			// Scale on right
			addParallel(new ElevateToTarget(ElevatorTarget.EXCHANGE));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceScale));
			addParallel(new AutoDrive(Constants.left90Degrees));
			addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
		else if (startingPosition == "left")
		{
			// This crosses the field to the opposite scale
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceToCrossPoint));
			addSequential(new AutoDrive(Constants.right90Degrees));
			addParallel(new AutoDrive(Constants.autoSpeed, Constants.distanceCross));
			addSequential(new ElevateToTarget(ElevatorTarget.UPPER_SCALE));
			addSequential(new AutoDrive(Constants.left90Degrees));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceFinalMoveAfterCross));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.softReleaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
	}
	
	private void depositCubeSwitch (String startingPosition, String side)
	{
		if (side == "left") 
		{
			depositCubeLeftSwitch(startingPosition);
		}
		else
		{
			depositCubeRightSwitch(startingPosition);
		}
	}
	
	private void depositCubeLeftSwitch(String startingPosition)
	{
		if (startingPosition == "left") {
			// Switch on left
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceSwitch));
			addSequential(new AutoDrive(Constants.right90Degrees));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());	
			addSequential(new CloseGrasper());
		}
		
		else if (startingPosition == "right")
		{
			// Nothing here, this is a very very bad idea
		}
		else
		{
			// Center start
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.startingBoxDistance));
			addSequential(new AutoDrive(Constants.navigateLeftSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.centerTransitionDistance));
			addSequential(new AutoDrive(Constants.alignRightSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.leftFarToLeftScale));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
	}
	
	private void depositCubeRightSwitch(String startingPosition)
	{
		if (startingPosition == "right") {
			// Switch on right
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceSwitch));
			addSequential(new AutoDrive(Constants.left90Degrees));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
		else if (startingPosition == "left")
		{
			// Nothing here, this is a very very bad idea
		}
		else
		{
			addParallel(new ElevateToTarget(ElevatorTarget.SWITCH));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.startingBoxDistance));
			addSequential(new AutoDrive(Constants.navigateRightSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.centerTransitionDistance));
			addSequential(new AutoDrive(Constants.alignLeftSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.rightFarToRightScale));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
	}
	
}