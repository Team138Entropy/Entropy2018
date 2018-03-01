package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ElevateToAlternateTarget extends Command {
	
	public ElevateToAlternateTarget()
	{
		requires(Robot.elevator);
	}
	
	protected void initialize() {
		Robot.elevator.ElevateToAlternateTarget();
	}

	protected void execute() {
		Robot.elevator.Execute();
	}

	protected boolean isFinished() {
		return Robot.elevator.IsMoveComplete();
	}

	protected void end() {
		Robot.elevator.StopMoving();
		
	}

	protected void interrupted() {
		
		Robot.elevator.CancelMove();
	}

}