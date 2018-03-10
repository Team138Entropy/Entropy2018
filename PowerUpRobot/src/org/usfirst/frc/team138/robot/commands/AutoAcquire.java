package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAcquire extends Command {

	private boolean _isAcquiring = false;
	private final double _acquireTimeSeconds = 1;
	private double _currentAcquireTime = 0;
	
    public AutoAcquire() {
        requires(Robot.grasper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.grasper.isCubeDetected() && !_isAcquiring) {
    		_currentAcquireTime = 0;
    		_isAcquiring = true;
    		Robot.grasper.StartAcquire();
    	}
    	if(_isAcquiring) {
    		_currentAcquireTime += Constants.commandLoopIterationSeconds;
    		
    		if (_currentAcquireTime > _acquireTimeSeconds) {
    			Robot.grasper.CompleteAcquire();
    			_isAcquiring = false;
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
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
