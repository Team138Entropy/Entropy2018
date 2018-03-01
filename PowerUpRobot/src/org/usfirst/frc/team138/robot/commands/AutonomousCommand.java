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
			
			depositCubeRightSwitch("right");
			
		}
		
		// This auto mode does the "proper action" depending on the starting position and gameData
		if (autoMode == "auto")
		{
			SmartDashboard.putString("Game Data",gameData);
			if (startPos == "left") {
				
				sameSide = "left";
				oppositeSide = "right";
				
				if (gameData == "LLL" || gameData == "RLR") {
					depositCubeScale(startPos, sameSide);
				}
				
				if (gameData == "RRR") {
					crossAutoLine();
				}
				
				if (gameData == "LRL") {
					depositCubeSwitch(startPos, sameSide);
				}
			}
			
			if (startPos == "middle") {
				
				if (gameData == "LLL" || gameData == "LRL") {
					// Left Switch
					depositCubeSwitch(startPos, "left");
				}
				
				if (gameData == "RRR" || gameData == "RLR") {
					// Right Switch
					depositCubeSwitch(startPos, "right");
				}
				
			}
			
			if (startPos == "right") {
				
				sameSide = "right";
				oppositeSide = "left";
				
				if (gameData == "LLL") {
					crossAutoLine();
				}
				
				if (gameData == "RRR" || gameData == "LRL") {
					depositCubeScale(startPos, sameSide);
				}
				
				if (gameData == "RLR") {
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
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceScale));
			addParallel(new AutoDrive(Constants.rotateToScore));
			addSequential(new ElevateToTarget(ElevatorTarget.etScale));
			// Use vision to drive to scale?
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
		else if (startingPosition == "right")
		{
			// Not yet
		}
	}
	
	private void depositCubeRightScale(String startingPosition)
	{
		if (startingPosition == "right") {
			// Scale on right
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceScale));
			addParallel(new AutoDrive(-1*Constants.rotateToScore));
			addSequential(new ElevateToTarget(ElevatorTarget.etScale));
			addSequential(new StartRelease());
			addSequential(new Wait(Constants.releaseDelay));
			addSequential(new CompleteRelease());
			addSequential(new CloseGrasper());
		}
		else if (startingPosition == "left")
		{
			// Not yet
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
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceSwitch));
			addSequential(new AutoDrive(-Constants.rotateToScore));
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
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
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
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new AutoDrive(Constants.autoSpeed, Constants.distanceSwitch));
			addSequential(new AutoDrive(Constants.rotateToScore));
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
			// Center start
			addParallel(new ElevateToTarget(ElevatorTarget.etSwitch));
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