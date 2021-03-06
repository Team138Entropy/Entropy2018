package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team138.robot.Robot;

import java.io.Console;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.Constants.CoordinateSystem;
import org.usfirst.frc.team138.robot.OI;
//import org.usfirst.frc.team138.robot.subsystems.Claw;

public class TeleopDrive extends Command{
	
	float clawUpPoint = 0.1f;
	
	public TeleopDrive(){
		requires(Robot.drivetrain);
	}

	protected void initialize() {
	}

	protected void execute() {
		//
		// the first Robot.drivetrain.driveWithTable may not be necessary
		// 10/7/17 - pre RiverRage
		// TODO: remove
		//
//		if (Robot.oi.getMoveSpeed() < clawUpPoint && Robot.claw.wristIsUp() == false) {
//			Robot.claw.wristUp();
//		    Robot.drivetrain.driveWithTable(Robot.oi.getMoveSpeed(), Robot.oi.getRotateSpeed());
//		} else {
//			Robot.drivetrain.driveWithTable(Robot.oi.getMoveSpeed(), Robot.oi.getRotateSpeed());
//		}
		if (Constants.coordinateSystem == CoordinateSystem.RobotCoordinates)
		{
			Robot.drivetrain.driveWithTable(OI.getMoveSpeed(), OI.getRotateSpeed());
		}
		else if (Constants.coordinateSystem == CoordinateSystem.FieldCoordinates)
		{
			Robot.drivetrain.driveWithFieldCoord();
		}
		else
		{
			// Invalid Coordinate System - print error?
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
