package org.usfirst.frc.team138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team138.robot.Robot;
import org.usfirst.frc.team138.robot.Sensors;
import org.usfirst.frc.team138.robot.Utility;
import org.usfirst.frc.team138.robot.Constants;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;


public class AutoDrive extends Command implements PIDOutput{
	
	boolean isDone = false;
	PIDController turnController;
	double rotateToAngleRate = 0.0;
	double lastRightDistance = 0.0;
	double lastLeftDistance = 0.0;
	int stallCounter = 0;
	boolean areMotorsStalled = false;
	boolean rotateInPlace;
	double driveSpeed = 0.0;
	double driveDistance = 0.0;
	double targetAngle = 0.0;
	boolean arcTurn = false;
	double timer=0;
	

	//*******************************************
	
	//Degree Tolerance
	//within how many degrees will you be capable of turning
	static double ToleranceDegrees = 1.0;
	double IntegralError=0;

	
	/**
	 * Drives straight for the specified distance
	 * @param speedArg The speed, from -1.0 to 1.0, the robot will drive. Negative speeds go backwards
	 * @param distanceArg The distance to drive, in inches. Uses the absolute value of encoders, so use positive distances
	 */
	public AutoDrive(double speedArg, double distanceArg){
		requires(Robot.drivetrain);
		rotateInPlace = false;
		driveSpeed = 2*speedArg;
		if (distanceArg>0)
			driveDistance=distanceArg-Constants.AutoDriveOvershoot;
		else
		{
			driveDistance=distanceArg+Constants.AutoDriveOvershoot;
			driveSpeed=-driveSpeed;
		}
			
		IntegralError=0;
		timer=0;
	}
	
	/**
	 * Rotates to an angle
	 * @param angle Angle, in degrees, to turn to. Negative angles turn left, positive angles turn right
	 */
	public AutoDrive(double angle){
		requires(Robot.drivetrain);
		rotateInPlace = true;
		targetAngle = angle;
		IntegralError=0;
		
	}
	
	
	private double leftDistance() {
		// Return leftDistance from left encoder in centimeters
		return -1*Constants.Meters2CM*Sensors.getLeftDistance();
	}
	private double rightDistance() {
		// Return rightDistance from right encoder in centimeters
		return -1*Constants.Meters2CM*Sensors.getRightDistance();
	}

	
	public void initialize() {
		Sensors.resetEncoders();
		Sensors.gyro.reset();	
		timer=0;		
	}

	public void execute() {
		if (areMotorsStalled) 
		{
			Robot.drivetrain.drive(0.0, 0.0);
			System.out.println("Stalled");
		}
		else
		{
			boolean result;
			double rate;
			if (rotateInPlace)
			{
				double diffAngle=targetAngle-Sensors.gyro.getAngle();
				if (targetAngle > 0)
				{
					result =(diffAngle<=0);
				}
				else
				{
					result =(diffAngle>=0);
				}
				IntegralError+=diffAngle*.025;
				rate=Constants.kPRotate*diffAngle+IntegralError*Constants.kIRotate;
				// rotate Talons opposite direction
				rotateToAngleRate=Utility.limitValue(rate,-1,1);
			}
			else
			{
				rotateToAngleRate=0;
				lastRightDistance = rightDistance();
				lastLeftDistance = leftDistance();
				double avgDistance=.5*(lastRightDistance+lastLeftDistance);
				result = (Math.abs(avgDistance) >= Math.abs(driveDistance));
			}
			if (result)
			{
				Robot.drivetrain.drive(0.0, 0.0);
				isDone = true;
			}		
			else
			{
				Robot.drivetrain.drive(driveSpeed, rotateToAngleRate);
				
				
			}
		}
	}

	public boolean isFinished() {
		return (isDone && (timer++>Constants.AutoDrivePause));
	}

	public void end() {
	}

	protected void interrupted() {
	}
	
	public void pidWrite(double output) {
		output = -output;
		if (rotateInPlace)
		{
			double minSpeed = 1;
			if (output > minSpeed || output < -minSpeed)
			{
				rotateToAngleRate = output;
			}
			else if (targetAngle > 0)
			{
				rotateToAngleRate = -minSpeed;
			}
			else
			{
				rotateToAngleRate = minSpeed;
			}
		}
		else
		{
			rotateToAngleRate =  0;
		}		
	}
	
}
