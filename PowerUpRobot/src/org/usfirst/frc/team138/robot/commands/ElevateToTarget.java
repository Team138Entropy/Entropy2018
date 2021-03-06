package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevateToTarget extends Command {
	
	private ElevatorTarget elevatorTarget;
	private final double commandTimeoutSeconds = 7;
	private double _currentCommandTime = 0;
	
	public ElevateToTarget(String target){
		requires(Robot.elevator);
		requires(Robot.grasper);
		elevatorTarget = Robot.elevator.ConvertToTarget(target);
		}
	
	public ElevateToTarget(ElevatorTarget target)
	{
		requires(Robot.elevator);
		elevatorTarget = target;
	}
	

	protected void initialize() {
		
		// Supports elevate to scale with hook interference
		if (elevatorTarget == ElevatorTarget.LOWER_SCALE || elevatorTarget == ElevatorTarget.UPPER_SCALE) {
			Robot.grasper.lowerWrist();
		}
		
		Robot.elevator.Elevate(elevatorTarget);
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
		
		// Supports release from scale
		if (elevatorTarget == ElevatorTarget.LOWER_SCALE || elevatorTarget == ElevatorTarget.UPPER_SCALE) {
			Robot.grasper.raiseWrist();
		}		
	}

	protected void interrupted() {
		
		Robot.elevator.CancelMove();
	}

}