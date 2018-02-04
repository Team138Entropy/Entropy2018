package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevateToTarget extends Command {
	
	public ElevateToTarget(String target){
//		Robot.elevator.Elevate(Robot.elevator.ConvertToTarget(target));
	}
	

	protected void initialize() {
	}

	protected void execute() {
		
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
	}

	protected void interrupted() {
	}

}