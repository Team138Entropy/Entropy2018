package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team138.robot.OI;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.Sensors;

//import org.usfirst.frc.team138.robot.subsystems.Claw;

public class TeleopDrive extends Command{
	
	float clawUpPoint = 0.1f;
	
	public TeleopDrive(){
		requires(Robot.drivetrain);
	}

	protected void initialize() {
		Sensors.resetEncoders();
		Sensors.gyro.reset();	
	}

	protected void execute() {
		Robot.drivetrain.drive(OI.getMoveSpeed(), OI.getRotateSpeed());
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}

}
