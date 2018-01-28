package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;

// BedfordBase branch started 2017-03-25 - jmcg
// 1. Increase "advance to neutral zone" distance to 10 feet"
// 2. Mirror "advance to neutral zone" for left starting position
// 3. Mark which moves have been tested with competition robot on practice field
// 4. Mirror dialed-in values from "Red" alliance positions to corresponding "Blue" positions
// note that values from red-left end up in blue-right while red-right end up in blue-left
// except for negating the turn angles

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	public AutonomousCommand(String team, String startPos, String autoMode, String gameData){
		final String ourSwitch = gameData.valueOf(0);
		final String scale = gameData.valueOf(1);
		final String theirSwitch = gameData.valueOf(2);
		
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
				}
				
				if (gameData == "RRR") {
					// "Off" position
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