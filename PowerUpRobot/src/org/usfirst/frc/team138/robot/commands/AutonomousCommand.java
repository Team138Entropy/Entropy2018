package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;

import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	public AutonomousCommand(String team, String startPos, String autoMode, String gameData){
		final String ourSwitch = String.valueOf(0);
		final String scale = String.valueOf(1);
		final String theirSwitch = String.valueOf(2);
		
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
				
				if (gameData == "LLL") {
					// Scale
					addSequential(new AutoDrive(0.8, Constants.distanceLeftScale));
					addSequential(new AutoDrive(Constants.rightAngleTwenty));
					addSequential(new ElevateToTarget(scale));
					
				}
				
				if (gameData == "RRR") {
					// "Off" position
					addSequential(new AutoDrive(135));
				}
				
				if (gameData == "RLR") {
					// Scale
					
				}
				
				if (gameData == "LRL") {
					// Switch
					
				}
			}
			
			if (startPos == "middle") {
				
				if (gameData == "LLL") {
					// Switch
					
				}
				
				if (gameData == "RRR") {
					// Switch
					
				}
				
				if (gameData == "RLR") {
					// Switch
					
				}
				
				if (gameData == "LRL") {
					// Switch
					
				}
			}
			
			if (startPos == "right") {
				
				if (gameData == "LLL") {
					// "Off" position
					addSequential(new AutoDrive(135));
				}
				
				if (gameData == "RRR") {
					// Scale
					
				}
				
				if (gameData == "RLR") {
					// Scale
					
				}
				
				if (gameData == "LRL") {
					// Switch
					
				}
			}
		}
	}
}