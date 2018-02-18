package org.usfirst.frc.team138.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team138.robot.commands.TeleopDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team138.robot.RobotMap;
import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.OI;


public class Drivetrain extends Subsystem{
	private static double CONTROLLER_DEAD_ZONE = 0.09;
	private static double min_speed,max_speed;
	private static double speed_range;
	private double lastSpeed=0;
	private static double maxSpeedChange=1/20.0;

	double _speedFactor = 1;
	double _rotateFactor = 1;

	// Servo Loop Gains
	double Drive_Kf = 1.7;
	double Drive_Kp = 5;
	double Drive_Ki = 0;//0.02;
	double Drive_Kd = 30;

	// Filter state for joystick movements
	double _lastMoveSpeed = 0;

	public WPI_TalonSRX frontLeftTalon = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_CHANNEL_FRONT);
	WPI_TalonSRX backLeftTalon = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_CHANNEL_BACK);
	public WPI_TalonSRX frontRightTalon = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_CHANNEL_FRONT);
	WPI_TalonSRX backRightTalon = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_CHANNEL_BACK);
	
	//SpeedControllerGroup _right = new SpeedControllerGroup(frontRightTalon, backRightTalon);
	//SpeedControllerGroup _left = new SpeedControllerGroup(frontLeftTalon, backLeftTalon);

	//DifferentialDrive drivetrain = new DifferentialDrive(_left, _right);


	protected void initDefaultCommand() {
		SmartDashboard.putNumber("ScaleFactor", 1.0);
		setDefaultCommand(new TeleopDrive());
	}

	public void DriveTrainInit()
	{
		/* choose the sensor and sensor direction */
		frontLeftTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
		frontLeftTalon.setSensorPhase(true);
		frontLeftTalon.setInverted(true);
		backLeftTalon.setInverted(true);
		/* set the peak and nominal outputs, 12V means full */
		frontLeftTalon.configNominalOutputForward(0.,0);
		frontLeftTalon.configNominalOutputReverse(0.,0);
		frontLeftTalon.configPeakOutputForward(1,0);
		frontLeftTalon.configPeakOutputReverse(-1,0);
		frontLeftTalon.setNeutralMode(NeutralMode.Coast);
		backLeftTalon.setNeutralMode(NeutralMode.Coast);
		
		/* choose the sensor and sensor direction */

		frontRightTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
		frontRightTalon.setSensorPhase(true);
		frontRightTalon.configNominalOutputForward(0.,0);
		frontRightTalon.configNominalOutputReverse(-0.,0);
		frontRightTalon.configPeakOutputForward(1,0);
		frontRightTalon.configPeakOutputReverse(-1,0);
		frontRightTalon.setNeutralMode(NeutralMode.Coast);
		backRightTalon.setNeutralMode(NeutralMode.Coast);
		
		// Configure slave Talons to follow masters
		backLeftTalon.follow(frontLeftTalon);
		backRightTalon.follow(frontRightTalon);
	}
	
	public void drive(double moveSpeed, double rotateSpeed)
	{
		//drivetrain.arcadeDrive(moveSpeed, rotateSpeed);
	}

	public void driveTank(double leftSpeed, double rightSpeed) 
	{
		//drivetrain.tankDrive(leftSpeed, rightSpeed);
	}
	
	public void driveCloseLoopControl(double moveSpeed, double rotateSpeed)
	{
		double left  = 0;
		double right = 0;

		if (Math.abs(moveSpeed) < Constants.CloseLoopJoystickDeadband)
		{
			moveSpeed=0;
		}
		
		if (Math.abs(rotateSpeed) < Constants.CloseLoopJoystickDeadband)
		{
			rotateSpeed=0;
		}
		
		// Determine if speed factor needs to be applied
		if (OI.isFullSpeed()) {	
			_rotateFactor = 1;
			_speedFactor = 1;
		}
		else
		{
			_rotateFactor = Constants.ClosedLoopSlowRotateFactor;
			_speedFactor = Constants.ClosedLoopSlowFactor;
		}
		
		_lastMoveSpeed = limitValue(_speedFactor * moveSpeed, 
								    _lastMoveSpeed - Constants.MaxSpeedChange,
								    _lastMoveSpeed + Constants.MaxSpeedChange);	

		left = Constants.ClosedLoopCruiseVelocity * _lastMoveSpeed - _rotateFactor * 
													rotateSpeed * Constants.ClosedLoopTurnSpeed;
		right = Constants.ClosedLoopCruiseVelocity * _lastMoveSpeed + _rotateFactor * 
													rotateSpeed * Constants.ClosedLoopTurnSpeed;

		// Convert Meters / seconds to Encoder Counts per 100 milliseconds
		frontLeftTalon.set(ControlMode.Velocity, left * Constants.SecondsTo100Milliseconds / Constants.MetersPerPulse);
		frontRightTalon.set(ControlMode.Velocity, right * Constants.SecondsTo100Milliseconds / Constants.MetersPerPulse);


		SmartDashboard.putNumber("Move Speed", moveSpeed);
		
		
		SmartDashboard.putNumber("Left Speed", left);
		SmartDashboard.putNumber("Right Speed", right);
		
		SmartDashboard.putNumber("Left PWM", frontLeftTalon.getMotorOutputPercent());
		SmartDashboard.putNumber("Right PWM", frontRightTalon.getMotorOutputPercent());
		
		SmartDashboard.putNumber("Left Velocity", frontLeftTalon.getSelectedSensorVelocity(0)*10*Constants.MetersPerPulse);
		SmartDashboard.putNumber("Right Velocity", frontRightTalon.getSelectedSensorVelocity(0)*10*Constants.MetersPerPulse);
		
		SmartDashboard.putNumber("Left Position", Constants.MetersPerPulse*frontLeftTalon.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Position", Constants.MetersPerPulse*frontRightTalon.getSelectedSensorPosition(0));
	}

	public void driveWithTable(double moveSpeed, double rotateSpeed)
	{

		min_speed=Math.abs(DriveTable.Drive_Matrix_2017[14][0]);
		max_speed=1.0;
		double scale=0.5;
		speed_range=max_speed-min_speed;
		SmartDashboard.putNumber("MinSpeed:", min_speed);

		//rotateSpeed = -rotateSpeed;
		// Filter input speeds
		moveSpeed=lastSpeed+limitValue(moveSpeed-lastSpeed,-maxSpeedChange,maxSpeedChange);
		lastSpeed=moveSpeed;
		
		SmartDashboard.putNumber("lastSpeed:", lastSpeed);
	

		if (OI.isFullSpeed())
			moveSpeed = applyDeadZone(moveSpeed);
		else
			moveSpeed = .5*applyDeadZone(moveSpeed);
			
		rotateSpeed = applyDeadZone(rotateSpeed);

		// Motor Speeds on both the left and right sides
		double leftMotorSpeed  = getLeftMotorSpeed(moveSpeed, rotateSpeed);
		double rightMotorSpeed = getRightMotorSpeed(moveSpeed, rotateSpeed);
		scale=1.0;
		if (leftMotorSpeed>0)
			leftMotorSpeed=min_speed+scale*(leftMotorSpeed-min_speed);
		else
			leftMotorSpeed=-min_speed+scale*(leftMotorSpeed+min_speed);
		
		if (rightMotorSpeed>0)
			rightMotorSpeed=min_speed+scale*(rightMotorSpeed-min_speed);
		else
			rightMotorSpeed=-min_speed+scale*(rightMotorSpeed+min_speed);
			
		SmartDashboard.putNumber("LeftSpeed:", leftMotorSpeed);
		SmartDashboard.putNumber("RightSpeed:", rightMotorSpeed);
		
		//drivetrain.tankDrive(leftMotorSpeed, rightMotorSpeed);
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
