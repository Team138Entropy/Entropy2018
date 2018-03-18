package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.Sensors;
import org.usfirst.frc.team138.robot.Utility;
import org.usfirst.frc.team138.robot.Constants;


public class AutoDrive extends Command {
	
	boolean isDone = false;
	double rotateToAngleRate = 0.0;
	double lastLeftDistance = 0.0;
	double lastRightDistance = 0.0;
	int stallCounter = 0;
	boolean areMotorsStalled = false;
	boolean rotateInPlace;
	double driveSpeed = 0.0;
	double driveDistance = 0.0;
	boolean arcTurn = false;
	double timer=0;
	double MinDistance=.25*.025*Constants.AutoDriveSpeed*Constants.Meters2CM; // centimeter (limit to detect stall)
	

	//*******************************************
	
	//Degree Tolerance
	//within how many degrees will you be capable of turning
	static double ToleranceDegrees = 2.0;
	double IntegralError=0;
	/**
	 * Drives straight for the specified distance
	 * @param speedArg The speed, from 0 to full in Meters/sec, the robot will drive. 
	 * Negative distance drives backwards
	 * @param distanceArg The distance to drive, in CM (centimeters). 
	 */
	public AutoDrive(double speedArg, double distanceArg){
		requires(Robot.drivetrain);
		rotateInPlace = false;
		driveSpeed = Math.abs(speedArg)*Constants.AutoDriveSpeed;
		driveDistance = distanceArg;
		IntegralError=0;
		timer=0;
		stallCounter=0;
		isDone=false;
		SmartDashboard.putString("Auto Drive", "Straight");
	}	
	/**
	 * Rotates to an angle
	 * @param angle Angle, in degrees, to turn to. Negative angles turn right, positive angles turn left
	 * ie:  right hand rule convention
	 */
	public AutoDrive(double angle){
		requires(Robot.drivetrain);
		rotateInPlace = true;
		IntegralError=0;
		Robot.accumulatedHeading = Robot.accumulatedHeading+angle;
		SmartDashboard.putNumber("Auto Angle 1",Robot.accumulatedHeading );
		if (angle>0)
			angle=angle-Constants.AutoDriveRotateOvershoot;
		else
			angle=angle+Constants.AutoDriveRotateOvershoot;

		stallCounter=0;
		isDone=false;
		SmartDashboard.putString("Auto Drive", "Turn");
	}
	
	
	public static double leftDistance() {
		// Return leftDistance from left encoder in centimeters
		// motors and encoders run opposite to robot convention
		// Invert encoder readings here so that distance increases
		// when robot moving forward.
		return Constants.Meters2CM*Sensors.getLeftDistance();
	}
	
	public static double rightDistance() {
		// Return rightDistance from right encoder in centimeters
		return Constants.Meters2CM*Sensors.getRightDistance();
	}

	public void initialize() {
		Sensors.resetEncoders();
		timer=0;
		stallCounter=0;
	}

	public void execute() {
		boolean moveComplete=false; // true when move complete
		double rate; // rate of rotation			
		double avgDistance=.5*(leftDistance()+rightDistance());
		
		// Stalled?
		double distanceRemaining=Math.abs(driveDistance)-Math.abs(avgDistance);
//		SmartDashboard.putNumber("Auto Angle",Robot.accumulatedHeading );
		/*
		if (Math.abs(lastLeftDistance-leftDistance())<MinDistance || 
				Math.abs(lastRightDistance-rightDistance())<MinDistance ) 
		{
			if (stallCounter == 50) 
			{
				Robot.drivetrain.drive(0.0, 0.0);
				areMotorsStalled = true;
				isDone = true;
			}
			stallCounter++;
			SmartDashboard.putString("Stall:","Stalled");
		}
		else
		*/
		
		{
			stallCounter = 0;
			
			// Angular difference between target and current heading
			double diffAngle=Robot.accumulatedHeading-Sensors.gyro.getAngle();
			SmartDashboard.putNumber("Diff Angle", diffAngle);
			
			if (rotateInPlace)
			{
				if (Robot.accumulatedHeading > 0)
					moveComplete =(diffAngle<=ToleranceDegrees);
				else
					moveComplete =(diffAngle>=-ToleranceDegrees);
				driveSpeed=0;
			}
			else
			{
				// Check distance-to-go
				// Are we there yet?
				moveComplete = (Math.abs(driveDistance) - Math.abs(avgDistance) < Constants.AutoDriveStopTolerance);
			}
			if (moveComplete)
			{
				Robot.drivetrain.drive(0.0, 0.0);
				isDone = true;
			}
			else {
				double speed;
				IntegralError+=diffAngle*.025;
				if (driveSpeed == 0) {
					rate=Constants.kPRotate*diffAngle+IntegralError*Constants.kIRotate;
				} else {
					rate=Constants.kPDrive*diffAngle;
				}
				// Scale to Meters/second
				rotateToAngleRate=Utility.limitValue(rate,-1,1)*Constants.AutoDriveRotateRate;	
				// Control deceleration to stop based on AutoDriveAccel limit
				// Approaching stop: Speed= sqrt(2*Accel*distance2Go), otherwise: driveSpeed
				distanceRemaining=Math.max(0,Math.abs(distanceRemaining))/Constants.Meters2CM; // Meters
				speed=Math.min(Math.sqrt(2*Constants.AutoDriveAccel*Math.abs(driveSpeed*distanceRemaining)),driveSpeed);
				// speed has sign of driveDistance
				speed=Robot.drivetrain.limitDriveAccel(Math.signum(driveDistance)*speed);
				rotateToAngleRate=Robot.drivetrain.limitRotateAccel(rotateToAngleRate);
				
				SmartDashboard.putNumber("Auto Speed", speed);
				SmartDashboard.putNumber("Auto AngleRate", rotateToAngleRate);
				Robot.drivetrain.drive(speed, rotateToAngleRate);
			}
		}
		// update distance moved (used to detect stalled motors)
		lastLeftDistance = leftDistance();
		lastRightDistance = rightDistance();
	}

	public boolean isFinished() {
		// Insert delay after motion complete before command is considered completer
		// allows time for motion to settle before resetting sensors at start of next
		// command
		SmartDashboard.putNumber("Auto Angle",Robot.accumulatedHeading );
		if (isDone) {
			Robot.drivetrain.Relax();
			SmartDashboard.putString("Auto Drive", "Done");
			return true;
		}
		else
			return false;
	}

	public void end() {
	}

	protected void interrupted() {
	}
	
	
}
