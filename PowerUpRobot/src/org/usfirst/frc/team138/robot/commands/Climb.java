package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.OI;
import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {

    public Climb() {
        requires(Robot.climber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (OI.isWinchEnabled()) {
    		Robot.climber.climb(OI.getClimbSpeed());
    	} else {
    		Robot.climber.climb(0.0);
    	}
    	
    	if (OI.isHookRotationEnabled()) {
    		Robot.climber.rotateHook(OI.getHookRotation());
    	} else {
    		Robot.climber.rotateHook(0.0);
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
