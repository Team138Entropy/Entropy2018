package org.usfirst.frc.team138.robot;


import com.ctre.phoenix.motorcontrol.SensorCollection;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
//import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors {
	public static ADXRS450_Gyro gyro; 
	
    static Joystick driverStick = new Joystick(0);
	
//	static Encoder leftEncoder;
//	static Encoder rightEncoder;
	
	public static SensorCollection leftSensorCollection;
	public static SensorCollection rightSensorCollection;
	
	static UsbCamera gearCamera;
	static Relay cameraLight = new Relay(RobotMap.GEAR_CAMERA_LIGHT_PORT);
	static UsbCamera groundCamera;
	//public static Entropy2017Targeting cameraProcessor;
	
	public static double gyroBias=0;
	
	public static void initialize() {
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        gyro.reset();
        
//        leftEncoder = new Encoder(RobotMap.LEFT_ENCODER_PORT_A, RobotMap.LEFT_ENCODER_PORT_B);
//		rightEncoder = new Encoder(RobotMap.RIGHT_ENCODER_PORT_A, RobotMap.RIGHT_ENCODER_PORT_B);
		leftSensorCollection = Robot.drivetrain.frontLeftTalon.getSensorCollection();
		rightSensorCollection = Robot.drivetrain.frontRightTalon.getSensorCollection();
//    	leftEncoder.setDistancePerPulse(0.124);
//    	rightEncoder.setDistancePerPulse(0.124);
    	resetEncoders();
	}
	
	public static double getLeftDistance() {
		return leftSensorCollection.getQuadraturePosition();
	}
	
	public static double getRightDistance() {
		return rightSensorCollection.getQuadraturePosition();
	}
	
	public static void resetEncoders() {
		leftSensorCollection.setQuadraturePosition(0, 0);
		rightSensorCollection.setQuadraturePosition(0, 0);
	}	
	
	public static void updateSmartDashboard(){
		double [] userCmd;
		
		if (Robot.claw.clawIsOpen())
		{
			SmartDashboard.putString("Claw State:", "Open");
		}
		else
		{
			SmartDashboard.putString("Claw State:", "Closed");
		}
		
		if (Robot.claw.wristIsUp())
		{
			SmartDashboard.putString("Wrist Position:", "Up");
		}
		else
		{
			SmartDashboard.putString("Wrist Position:", "Down");
		}
		
		if (Robot.claw.guardIsUp())
		{
			SmartDashboard.putString("Guard Position:", "Up");
		}
		else
		{
			SmartDashboard.putString("Guard Position:", "Down");
		}
		
		if (Robot.claw.ramExtended())
		{
			SmartDashboard.putString("Ram Position:", "Extended");
		}
		else
		{
			SmartDashboard.putString("Ram Position:", "Retracted");
		}
		SmartDashboard.putNumber("Left Encoder:", getLeftDistance());
		SmartDashboard.putNumber("Right Encoder:", getRightDistance());
		// User command (joystick)
		userCmd = OI.getFieldCommand();
		SmartDashboard.putNumber("Cmd Angle:", userCmd[1]);
		SmartDashboard.putNumber("Magn:", userCmd[0]);
		
	}
}
