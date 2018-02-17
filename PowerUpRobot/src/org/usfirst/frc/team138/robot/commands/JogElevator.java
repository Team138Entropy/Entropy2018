package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class JogElevator extends Command {
	
	private int _jogDirection = 0;
	
	public JogElevator(int direction)
	{
		requires(Robot.elevator);
		_jogDirection = direction;
	}
	

	protected void initialize() {
		Robot.elevator.JogElevator(_jogDirection, Constants.elevatorJogSpeed);
	}

	protected void execute() {
		Robot.elevator.Execute();
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.elevator.StopMoving();
		
	}

	protected void interrupted() {
		
		Robot.elevator.StopMoving();
	}

}