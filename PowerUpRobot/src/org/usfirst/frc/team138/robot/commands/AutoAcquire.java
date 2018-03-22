package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.subsystems.Grasper.RollerState;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAcquire extends Command {
	
	private enum AutoAcquireStates {
		DISABLED,
		DETECT_CUBE,
		START_ACQUIRE,
		COMPLETE_ACQUIRE,
		HOLD_CUBE
		
	}
	private AutoAcquireStates _currentState = AutoAcquireStates.DISABLED;

	private int _consecutiveReadingsAboveThreshold = 0;
	
    public AutoAcquire() {
        requires(Robot.grasper);
        _currentState = AutoAcquireStates.DISABLED;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putString("AutoAcquireState", _currentState.toString());
    	switch(_currentState) {
    	case DISABLED: disabled();
    		break;
    	case DETECT_CUBE: detectCube();
    		break;
    	case START_ACQUIRE: startAcquire();
    		break;
    	case COMPLETE_ACQUIRE: completeAcquire();
    		break;
    	case HOLD_CUBE: holdCube();
    		break;
    	}
    	
    	
    }
    
    private void disabled() {
    	// do nothing 
    	
    	// Check for transition to DETECT_CUBE state
    	if (Robot.elevator.IsAtFloor() &&
    		Robot.grasper.isWristDown() &&
    		Robot.grasper.grasperIsOpen() &&
    		Robot.grasper.isRollerState(RollerState.ACQUIRE) &&
    		Robot.grasper.isCubeReleased()){
    			_currentState = AutoAcquireStates.DETECT_CUBE;
    			_consecutiveReadingsAboveThreshold = 0;
    	
    	}
    	
    }
    private void detectCube() {
    	//Check for transition to disabled
    	/*if (!Robot.elevator.IsAtFloor() ||
        		!Robot.grasper.isWristDown() ||
        		!Robot.grasper.grasperIsOpen() ||
        		!Robot.grasper.isRollerState(RollerState.ACQUIRE) ||
        		!Robot.grasper.isCubeReleased()){
        			_currentState = AutoAcquireStates.DISABLED;
        		}
    	//check for detected threshold 
    	else */
    	
    	
    	if (Robot.grasper.isCubeDetected()) {
    		_consecutiveReadingsAboveThreshold++;
    	}
    	else
    	{
    		_consecutiveReadingsAboveThreshold = 0;
    	}
    	
		SmartDashboard.putNumber("Threshold Count", _consecutiveReadingsAboveThreshold);
    	
    	if (_consecutiveReadingsAboveThreshold >= Constants.cubeDetectThresholdCount) {
    		_currentState = AutoAcquireStates.START_ACQUIRE;
    		_consecutiveReadingsAboveThreshold = 0;
    	}
    }
    private void startAcquire() {
    	Robot.grasper.StartAcquire();
    	//Check for acquired threshold
    	if (Robot.grasper.isCubeAcquired()) {
    		_consecutiveReadingsAboveThreshold++;
    	}
    	else {
    		_consecutiveReadingsAboveThreshold = 0;
    	}
   
		SmartDashboard.putNumber("Threshold Count", _consecutiveReadingsAboveThreshold);
		
    	if ((_consecutiveReadingsAboveThreshold >= Constants.cubeAcquireThresholdCount))
    	{
    		_currentState = AutoAcquireStates.COMPLETE_ACQUIRE;
    		_consecutiveReadingsAboveThreshold = 0;
    	}
    }
    private void completeAcquire() {
    	Robot.grasper.CompleteAcquire();
    	
    	_currentState = AutoAcquireStates.HOLD_CUBE;
    }
    private void holdCube() {
    	if (Robot.grasper.isCubeReleased()) {
    		_currentState = AutoAcquireStates.DISABLED;
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
