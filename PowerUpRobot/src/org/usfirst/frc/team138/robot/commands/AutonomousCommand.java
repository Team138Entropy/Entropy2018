package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	
	public AutonomousCommand(String team, String startPos, String autoMode, String gameData) {
		
		String sameSide;
		String oppositeSide;
		
		// Test Mode
		if (autoMode == "test")
		{
			addSequential(new AutoDrive(5));
			addSequential(new AutoDrive(5));
		}
		
		// This auto mode does the "proper action" depending on the starting position and gameData
		if (autoMode == "auto")
		{
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
		addSequential(new AutoDrive(0.8, Constants.distanceBaseLine));
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
			addSequential(new AutoDrive(0.8, Constants.distanceScale));
			addSequential(new AutoDrive(90.0));
			// Use vision to drive to scale?
			addSequential(new ElevateToTarget(ElevatorTarget.etScale));
			addSequential(new StartRelease());
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
			addSequential(new AutoDrive(0.8, Constants.distanceScale));
			addSequential(new AutoDrive(-90.0));
			addSequential(new ElevateToTarget(ElevatorTarget.etScale));
			addSequential(new StartRelease());
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
			addSequential(new AutoDrive(0.8, Constants.distanceSwitch));
			addSequential(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new StartRelease());
		}
		else if (startingPosition == "right")
		{
			// Nothing here, this is a very very bad idea
		}
		else
		{
			// Center start
			addSequential(new AutoDrive(0.8, 92));
			addSequential(new AutoDrive(-50.0));
			addSequential(new AutoDrive(0.8, 427.0));
			addSequential(new AutoDrive(140.0));
			addSequential(new AutoDrive(0.8, 183.88));
			addSequential(new StartRelease());
		}
	}
	
	private void depositCubeRightSwitch(String startingPosition)
	{
		if (startingPosition == "right") {
			// Switch on right
			addSequential(new AutoDrive(0.8, Constants.distanceScale));
			addSequential(new AutoDrive(-90.0));
			addSequential(new ElevateToTarget(ElevatorTarget.etSwitch));
			addSequential(new StartRelease());
		}
		else if (startingPosition == "left")
		{
			// Nothing here, this is a very very bad idea
		}
		else
		{
			// Center start
			addSequential(new AutoDrive(0.8, 92));
			addSequential(new AutoDrive(55.0));
			addSequential(new AutoDrive(0.8, 427.0));
			addSequential(new AutoDrive(-145.0));
			addSequential(new AutoDrive(0.8, 137));
			addSequential(new StartRelease());
		}
	}
}