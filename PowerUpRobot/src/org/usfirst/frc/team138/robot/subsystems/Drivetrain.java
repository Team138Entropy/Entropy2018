package org.usfirst.frc.team138.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team138.robot.commands.TeleopDrive;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;

public class Drivetrain extends Subsystem{
	private static double CONTROLLER_DEAD_ZONE = 0.09;

	public WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_CHANNEL_FRONT);
	WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_CHANNEL_BACK);
	public WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_CHANNEL_FRONT);
	WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_CHANNEL_BACK);
	
	SpeedControllerGroup right = new SpeedControllerGroup(frontRightTalon, backRightTalon);
	SpeedControllerGroup left = new SpeedControllerGroup(frontLeftTalon, backLeftTalon);

	DifferentialDrive drivetrain = new DifferentialDrive(left, right);


	protected void initDefaultCommand() {
		frontLeftTalon.setInverted(true);
		backLeftTalon.setInverted(true);
		frontRightTalon.setInverted(true);
		backRightTalon.setInverted(true);

		setDefaultCommand(new TeleopDrive());
	}

	public void drive(double moveSpeed, double rotateSpeed)
	{
		drivetrain.arcadeDrive(moveSpeed, rotateSpeed*Constants.rotateDriveDirection);
	}

	public void driveTank(double leftSpeed, double rightSpeed) 
	{
		drivetrain.tankDrive(leftSpeed, rightSpeed);
	}

	public void driveWithTable(double moveSpeed, double rotateSpeed)
	{

		//rotateSpeed = -rotateSpeed;
		// Filter input speeds
		moveSpeed = applyDeadZone(moveSpeed);
		rotateSpeed = applyDeadZone(rotateSpeed)*Constants.rotateDriveDirection;

		// Motor Speeds on both the left and right sides
		double leftMotorSpeed  = getLeftMotorSpeed(moveSpeed, rotateSpeed);
		double rightMotorSpeed = getRightMotorSpeed(moveSpeed, rotateSpeed);

		SmartDashboard.putNumber("LeftSpeed:", leftMotorSpeed);
		SmartDashboard.putNumber("RightSpeed:", rightMotorSpeed);


		drivetrain.tankDrive(leftMotorSpeed, rightMotorSpeed);
	}

	double getLeftMotorSpeed(double moveSpeed, double rotateSpeed)
	{
		int[] indices = {16, 16};

		indices = getIndex(moveSpeed, rotateSpeed);

		return DriveTable.Drive_Matrix_2017[indices[1]][indices[0]];
	}

	double getRightMotorSpeed(double moveSpeed, double rotateSpeed)
	{
		int[] indices = {16, 16};

		indices = getIndex(moveSpeed, rotateSpeed);
		indices[0] = 32 - indices[0];

		return DriveTable.Drive_Matrix_2017[indices[1]][indices[0]];
	}

	int[] getIndex(double moveSpeed, double rotateSpeed)
	{		
		double diff1 = 0;
		double diff2 = 0;
		// [0] is x, [1] is y
		int[] returnIndex = {0, 0};

		double[] arrayPtr = DriveTable.Drive_Lookup_X;
		int arrayLength = DriveTable.Drive_Lookup_X.length;

		double rotateValue = limitValue(rotateSpeed, arrayPtr[0], arrayPtr[arrayLength-1]);

		for(int i = 0; i < arrayLength; i++) 
		{
			if(i+1 >= arrayLength || inRange(rotateValue, arrayPtr[i], arrayPtr[i+1]))
			{
				//Assume match found
				if((i + 1) >= arrayLength)
				{
					returnIndex[0] = i;	
				}
				else
				{
					diff1 = Math.abs(rotateValue - arrayPtr[i]);
					diff2 = Math.abs(rotateValue - arrayPtr[i+1]);

					if(diff1 < diff2)
					{
						returnIndex[0] = i;
					}
					else
					{
						returnIndex[0] = i + 1;
					}
				}
				break;
			}
		}

		arrayPtr = DriveTable.Drive_Lookup_Y;
		arrayLength = DriveTable.Drive_Lookup_Y.length;
		double moveValue = limitValue(moveSpeed, arrayPtr[0], arrayPtr[arrayLength - 1]);

		for( int i = 0; i < arrayLength; i++) 
		{
			if(i+1 >= arrayLength || inRange(moveValue, arrayPtr[i], arrayPtr[i+1]))
			{
				//Assume match found
				if((i + 1) >= arrayLength)
				{
					returnIndex[1] = i;	
				}
				else
				{
					diff1 = Math.abs(moveValue - arrayPtr[i]);
					diff2 = Math.abs(moveValue - arrayPtr[i+1]);

					if(diff1 < diff2)
					{
						returnIndex[1] = i;
					}
					else
					{
						returnIndex[1] = i + 1;
					}
				}
				break;
			}
		}

		return returnIndex;
	}

	boolean inRange(double testValue, double bound1, double bound2) 
	{  
		return (((bound1 <= testValue) && (testValue <= bound2)) ||
				((bound1 >= testValue) && (testValue >= bound2)));
	}

	double limitValue(double testValue, double lowerBound, double upperBound)
	{
		if(testValue > upperBound)
		{
			return upperBound;
		}
		else if(testValue < lowerBound)
		{
			return lowerBound;
		}
		else
		{
			return testValue;
		}
	}

	double applyDeadZone(double speed)
	{
		double finalSpeed;

		if ( Math.abs(speed) < CONTROLLER_DEAD_ZONE) {
			finalSpeed = 0;
		}
		else {
			finalSpeed = speed;
		}
		return finalSpeed;
	}





}
