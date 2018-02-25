package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StartRelease extends Command {
	int timer;

    public StartRelease() {
        requires(Robot.grasper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.grasper.StartRelease();
    	timer=0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (timer++>2)
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
