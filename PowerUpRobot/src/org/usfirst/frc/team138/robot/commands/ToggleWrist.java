package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToggleWrist extends Command {

    public ToggleWrist() {
        requires(Robot.grasper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.grasper.toggleWrist();
    	if(!Robot.grasper.wristIsUp()) {
    		Robot.grasper.acquireRollers(true);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
