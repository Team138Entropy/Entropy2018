package org.usfirst.frc.team138.robot;


import com.ctre.phoenix.motorcontrol.SensorCollection;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors {
	public static ADXRS450_Gyro gyro; 
	
    static Joystick driverStick = new Joystick(0);
	
	public static SensorCollection leftSensorCollection;
	public static SensorCollection rightSensorCollection;
	
	static UsbCamera gearCamera;
	static UsbCamera groundCamera;
	//public static Entropy2017Targeting cameraProcessor;
	
	public static double gyroBias=0;
	
	public static void initialize() {
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        gyro.reset();
        
		leftSensorCollection = Robot.drivetrain.frontLeftTalon.getSensorCollection();
		rightSensorCollection = Robot.drivetrain.frontRightTalon.getSensorCollection();
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
				
		SmartDashboard.putNumber("Left Encoder:", getLeftDistance());
		SmartDashboard.putNumber("Right Encoder:", getRightDistance());
		// User command (joystick)
		userCmd = OI.getFieldCommand();
		SmartDashboard.putNumber("Cmd Angle:", userCmd[1]);
		SmartDashboard.putNumber("Magn:", userCmd[0]);
		
	}
}
