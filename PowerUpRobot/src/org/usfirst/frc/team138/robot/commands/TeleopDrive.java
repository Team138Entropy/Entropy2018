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
		if (!OI.isFullSpeed()) {
			moveSpeed=moveSpeed*Constants.ClosedLoopSlowFactor;
			rotateSpeed=rotateSpeed*Constants.ClosedLoopSlowRotateFactor;
		}
		// Limit rate of change of moveSpeed
		moveSpeed=Robot.drivetrain.limitDriveAccel(moveSpeed);
		// Convert to Meters/Sec
		moveSpeed=moveSpeed*Constants.ClosedLoopCruiseVelocity;
		rotateSpeed=rotateSpeed*Constants.ClosedLoopTurnSpeed;
		// Send to driveTrain in Meters/sec units.
		Robot.drivetrain.drive(moveSpeed,rotateSpeed);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}

}
