package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.OI;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.Sensors;

//import org.usfirst.frc.team138.robot.subsystems.Claw;

public class TeleopDrive extends Command{
	
	
	public TeleopDrive(){
		requires(Robot.drivetrain);
	}

	protected void initialize() {
		Sensors.resetEncoders();
		Sensors.gyro.reset();	
	}

	protected void execute() {
		double moveSpeed,rotateSpeed;
		moveSpeed=OI.getMoveSpeed();
		rotateSpeed= OI.getRotateSpeed();
		// Full speed or slow speed?
		if (OI.isFullSpeed()) {
			// Full speed
			moveSpeed=moveSpeed*Constants.ClosedLoopCruiseVelocity;
			rotateSpeed=rotateSpeed*Constants.ClosedLoopTurnSpeed;
		}
		else { 
			// Slow speed
			moveSpeed=moveSpeed*Constants.ClosedLoopSlowVelocity;
			rotateSpeed=rotateSpeed*Constants.ClosedLoopSlowRotateFactor;
		}
		// Limit rate of change of moveSpeed
		moveSpeed=Robot.drivetrain.limitDriveAccel(moveSpeed);
		if (!OI.isFullSpeed())
		{
			// Send to driveTrain in Meters/sec units.
			Robot.drivetrain.drive(moveSpeed,rotateSpeed);
		}
		else {
			// Full Speed - use "old fashioned" driveWithTable
			// Convert back to % throttle for backwards compatibility
			moveSpeed=moveSpeed/Constants.ClosedLoopCruiseVelocity;
			rotateSpeed=rotateSpeed/Constants.ClosedLoopTurnSpeed;
			Robot.drivetrain.driveWithTable(moveSpeed, rotateSpeed);
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
