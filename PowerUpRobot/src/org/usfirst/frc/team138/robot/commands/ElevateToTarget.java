package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.Command;

public class ElevateToTarget extends Command {
	
	private ElevatorTarget elevatorTarget;
	
	public ElevateToTarget(String target){
		requires(Robot.elevator);
		elevatorTarget = Robot.elevator.ConvertToTarget(target);
		}
	
	public ElevateToTarget(ElevatorTarget target)
	{
		requires(Robot.elevator);
		elevatorTarget = target;
	}
	

	protected void initialize() {
		Robot.elevator.Elevate(elevatorTarget);
	}

	protected void execute() {
		Robot.elevator.Execute();
	}

	protected boolean isFinished() {
		return Robot.elevator.IsMoveComplete();
	}

	protected void end() {
		Robot.elevator.CancelMove();
		
	}

	protected void interrupted() {
		
		Robot.elevator.CancelMove();
	}

}