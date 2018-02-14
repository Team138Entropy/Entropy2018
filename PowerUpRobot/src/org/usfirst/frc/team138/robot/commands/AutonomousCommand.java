package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	public AutonomousCommand(String team, String startPos, String autoMode, String gameData) {
		final String ourSwitch = String.valueOf(0);
		final String scale = String.valueOf(1);
		
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
					// Scale on left
					addSequential(new AutoDrive(0.8, Constants.distanceLeftSwitch));
					addSequential(new AutoDrive(0.8, 1000)); // TODO: Add real distance
					addSequential(new AutoDrive(Constants.rightAngleThirty));
					addSequential(new ElevateToTarget(scale));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
				}
				
				if (gameData == "RRR") {
					// "Off" position
					addSequential(new AutoDrive(0.8, Constants.distanceBaseLine));
				}
				
				if (gameData == "RLR") {
					// Scale on left
					addSequential(new AutoDrive(0.8, Constants.distanceRightSwitch));
					addSequential(new AutoDrive(0.8, 1000)); // TODO: Add real distance
					addSequential(new AutoDrive(Constants.leftAngleThirty));
					addSequential(new ElevateToTarget(scale));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
				}
				
				if (gameData == "LRL") {
					// Switch on left
					addSequential(new AutoDrive(0.8, Constants.distanceRightSwitch));
					addSequential(new AutoDrive(Constants.leftAngleThirty));
					addSequential(new ElevateToTarget(ourSwitch));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
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
					addSequential(new AutoDrive(0.8, Constants.distanceBaseLine));
				}
				
				if (gameData == "RRR") {
					// Scale on right
					addSequential(new AutoDrive(0.8, Constants.distanceRightSwitch));
					addSequential(new AutoDrive(Constants.leftAngleThirty));
					addSequential(new AutoDrive(0.8, 1000)); // TODO: Add real distance
					addSequential(new ElevateToTarget(scale));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
				}
				
				if (gameData == "RLR") {
					// Scale on left
					addSequential(new AutoDrive(0.8, Constants.distanceRightSwitch));
					addSequential(new AutoDrive(Constants.leftAngleThirty));
					addSequential(new AutoDrive(0.8, 1000)); // TODO: Add real distance
					addSequential(new ElevateToTarget(scale));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
				}
				
				if (gameData == "LRL") {
					// Switch on left
					addSequential(new AutoDrive(0.8, Constants.distanceRightSwitch));
					addSequential(new AutoDrive(Constants.leftAngleThirty));
					addSequential(new ElevateToTarget(ourSwitch));
					addSequential(new Wait(Constants.threeSeconds));
					// "Release cube" addSequential(new ());
				}
			}
		}
	}
}