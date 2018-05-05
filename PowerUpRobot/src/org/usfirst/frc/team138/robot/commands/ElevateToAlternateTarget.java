package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevateToAlternateTarget extends Command {
	
	private final double commandTimeoutSeconds = 7;
	private double _currentCommandTime = 0;
	
	public ElevateToAlternateTarget()
	{
		requires(Robot.elevator);
	}
	
	protected void initialize() {
		Robot.elevator.ElevateToAlternateTarget();
		_currentCommandTime = 0;
	}

	protected void execute() {
		Robot.elevator.Execute();
		_currentCommandTime += Constants.commandLoopIterationSeconds;
		SmartDashboard.putNumber("Timer", _currentCommandTime);
	}

	protected boolean isFinished() {
		if (_currentCommandTime >= commandTimeoutSeconds)
		{
			return true;
		}
		else
		{
			return Robot.elevator.IsMoveComplete();
		}
	}

	protected void end() {
		Robot.elevator.StopMoving();
	}

	protected void interrupted() {
		
		Robot.elevator.CancelMove();
	}

}