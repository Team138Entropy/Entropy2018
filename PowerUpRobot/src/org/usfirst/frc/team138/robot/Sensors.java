package org.usfirst.frc.team138.robot;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
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
		Robot.drivetrain.frontLeftTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		Robot.drivetrain.frontRightTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        gyro.reset();

        
	}
	
	public static double getLeftDistance() {
		// In METERS
		return -Robot.drivetrain.frontLeftTalon.getSelectedSensorPosition(0)*Constants.MetersPerPulse;
	}
	
	public static double getRightDistance() {
		// In METERS
		return -Robot.drivetrain.frontRightTalon.getSelectedSensorPosition(0)*Constants.MetersPerPulse;
	}
	
	public static void resetEncoders() {
		Robot.drivetrain.frontLeftTalon.setSelectedSensorPosition(0, 0, 0);
		Robot.drivetrain.frontRightTalon.setSelectedSensorPosition(0, 0, 0);
	}	
	
	public static void updateSmartDashboard(){
		SmartDashboard.putNumber("Left Pos(M)", getLeftDistance());
		SmartDashboard.putNumber("Right Pos(M)", getRightDistance());
		SmartDashboard.putNumber("Elev Position", Robot.elevator._elevatorMotor.getSelectedSensorPosition(0));     
		SmartDashboard.putNumber("Elev Velocity", Robot.elevator._elevatorMotor.getSelectedSensorVelocity(0));
		
		SmartDashboard.putNumber("Heading", gyro.getAngle());
		SmartDashboard.putNumber("Left Velocity",-Robot.drivetrain.frontLeftTalon.getSelectedSensorVelocity(0)*10*Constants.MetersPerPulse);
		SmartDashboard.putNumber("Right Velocity",-Robot.drivetrain.frontRightTalon.getSelectedSensorVelocity(0)*10*Constants.MetersPerPulse);

				
		
	}
}