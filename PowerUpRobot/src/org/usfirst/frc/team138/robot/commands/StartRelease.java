package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.subsystems.Elevator.ElevatorTarget;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StartRelease extends Command {
	private double _currentCommandTime = 0;

    public StartRelease() {
        requires(Robot.grasper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		// Supports release from scale
		if (Robot.elevator.getAlternateTarget() == ElevatorTarget.LOWER_SCALE || Robot.elevator.getAlternateTarget() == ElevatorTarget.UPPER_SCALE) {
			Robot.grasper.raiseWrist();
		}	
		_currentCommandTime = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	_currentCommandTime += Constants.commandLoopIterationSeconds;
    	
    	if (_currentCommandTime > Constants.StartReleaseDelay)
    	{
    		Robot.grasper.StartRelease();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (_currentCommandTime > (Constants.StartReleaseDelay + Constants.commandLoopIterationSeconds))
    		return true;
    	else
    		return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
