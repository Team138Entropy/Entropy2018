package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
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
	double targetAngle = 0.0;
	boolean arcTurn = false;
	double timer=0;
	double MinDistance=10; // centimeter (limit to detect stall)
	

	//*******************************************
	
	//Degree Tolerance
	//within how many degrees will you be capable of turning
	static double ToleranceDegrees = 1.0;
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
		driveSpeed = speedArg;
		// Adjust drive distance to allow for distance required to decelerate to stop
		if (distanceArg>0)
			driveDistance=distanceArg-Constants.AutoDriveOvershoot;
		else
		{
			driveDistance=distanceArg+Constants.AutoDriveOvershoot;
			// If moving backwards, invert drivespeed
			driveSpeed=-driveSpeed;
		}
		IntegralError=0;
		timer=0;
	}
	
	/**
	 * Rotates to an angle
	 * @param angle Angle, in degrees, to turn to. Negative angles turn right, positive angles turn left
	 * ie:  right hand rule convention
	 */
	public AutoDrive(double angle){
		requires(Robot.drivetrain);
		rotateInPlace = true;
		targetAngle = angle;
		IntegralError=0;		
	}
	
	
	private double leftDistance() {
		// Return leftDistance from left encoder in centimeters
		// motors and encoders run oposite to robot convention
		// Invert encoder readings here so that distance increases
		// when robot moving forward.
		return -1*Constants.Meters2CM*Sensors.getLeftDistance();
	}
	private double rightDistance() {
		// Return rightDistance from right encoder in centimeters
		return -1*Constants.Meters2CM*Sensors.getRightDistance();
	}

	public void initialize() {
		// reset gyro and encoders are start of every autonomous move
		// ie: sequential moves are relative to previous position.
		Sensors.resetEncoders();
		Sensors.gyro.reset();	
		timer=0;		
	}

	public void execute() {
		boolean moveComplete; // true when move complete
		double rate; // rate of rotation			
		double avgDistance=.5*(leftDistance()+rightDistance());
		// Stalled?
		if (Math.abs(lastLeftDistance-leftDistance())<MinDistance || 
				Math.abs(lastRightDistance-rightDistance())<MinDistance ) 
		{
			if (stallCounter == 25) 
			{
				Robot.drivetrain.drive(0.0, 0.0);
				areMotorsStalled = true;
			}
			if(stallCounter == 50)
				isDone = true;
			stallCounter++;
		}
		else
		{
			stallCounter = 0;
			if (rotateInPlace)
			{
				// Angular difference between target and current heading
				double diffAngle=targetAngle-Sensors.gyro.getAngle();
				if (targetAngle > 0)
					moveComplete =(diffAngle<=0);
				else
					moveComplete =(diffAngle>=0);
				IntegralError+=diffAngle*.025;
				rate=Constants.kPRotate*diffAngle+IntegralError*Constants.kIRotate;
				// Scale to Meters/second
				rotateToAngleRate=Utility.limitValue(rate,-1,1)*Constants.AutoDriveRotateRate;	
				driveSpeed=0;
			}
			else
			{
				// move straight
				rotateToAngleRate=0;
				// Check distance-to-go
				// Are we there yet?
				moveComplete = (Math.abs(avgDistance) >= Math.abs(driveDistance));
			}
			if (moveComplete)
			{
				Robot.drivetrain.drive(0.0, 0.0);
				isDone = true;
			}
			else {
				double distanceRemaining=Math.abs(avgDistance)-Math.abs(driveDistance);
				double speed;
				// Control deceleration to stop based on AutoDriveAccel limit
				// Approaching stop: Speed= sqrt(2*Accel*distance2Go), otherwise: driveSpeed
				speed=Math.min(Math.sqrt(2*Constants.AutoDriveAccel*Math.abs(driveSpeed)*distanceRemaining),Math.abs(driveSpeed));
				Robot.drivetrain.drive(speed*Math.signum(driveSpeed), rotateToAngleRate);
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
		return (isDone && (timer++>Constants.AutoDrivePause));
	}

	public void end() {
	}

	protected void interrupted() {
	}
	
	
}
