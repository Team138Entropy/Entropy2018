package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.OI;
import org.usfirst.frc.team138.robot.Robot;

//import org.usfirst.frc.team138.robot.subsystems.Claw;

public class TeleopDrive extends Command{
	
	float clawUpPoint = 0.1f;
	
	public TeleopDrive(){
		requires(Robot.drivetrain);
	}

	protected void initialize() {
	}

	protected void execute() {
		if (Constants.useClosedLoopDrivetrain)
		{
			Robot.drivetrain.driveCloseLoopControl(OI.getMoveSpeed(), OI.getRotateSpeed());
		}
		else
		{
			Robot.drivetrain.driveWithTable(OI.getMoveSpeed(), OI.getRotateSpeed());
		}	
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}

}
