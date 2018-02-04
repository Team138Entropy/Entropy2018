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
        
	}
	
	public static double getLeftDistance() {
		return 0; // leftSensorCollection.getQuadraturePosition();
	}
	
	public static double getRightDistance() {
		return 0; //rightSensorCollection.getQuadraturePosition();
	}
	
	public static void resetEncoders() {
	}	
	
	public static void updateSmartDashboard(){
				
		
	}
}
