/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team138.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

	/* talons for arcade drive */
	WPI_TalonSRX R_Motor = new WPI_TalonSRX(22); 		/* device IDs here (1 of 2) */
	WPI_TalonSRX L_Motor = new WPI_TalonSRX(21);
	
	Preferences prefs = Preferences.getInstance();
	
	int currentLimit=1;
	double rotateSpeed=.2;

//	Joystick _joy = new Joystick(0);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	R_Motor.setInverted(true);
    	L_Motor.setInverted(true);
    	
    	R_Motor.setNeutralMode(NeutralMode.Brake);
    	L_Motor.setNeutralMode(NeutralMode.Brake);
    }


    public void teleopInit() {
		currentLimit=prefs.getInt("Current Limit", 4);
    	// configure current limits on Acquire motors
    	
    	rotateSpeed=prefs.getDouble("Acquire Speed", .2);    	
    	
    	R_Motor.configPeakCurrentLimit(0,0);
    	R_Motor.configPeakCurrentDuration(0,0);
    	R_Motor.configContinuousCurrentLimit(currentLimit, 0);

    	L_Motor.configPeakCurrentLimit(0,0);
    	L_Motor.configPeakCurrentDuration(0,0);
    	L_Motor.configContinuousCurrentLimit(currentLimit, 0);
    	
    	R_Motor.enableCurrentLimit(true);
    	L_Motor.enableCurrentLimit(true);

    	
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	double Vmot, Vbat, Ibat;

    	R_Motor.set(ControlMode.PercentOutput, rotateSpeed);
    	L_Motor.set(ControlMode.PercentOutput, rotateSpeed);
    	
    	Vmot=R_Motor.getMotorOutputVoltage();
    	Vbat=R_Motor.getBusVoltage();
    	Ibat=R_Motor.getOutputCurrent();
    	SmartDashboard.putNumber("Wheel Speed:", rotateSpeed);

    	SmartDashboard.putNumber("Battery Current:", Ibat);
    	SmartDashboard.putNumber("Motor Current:", Ibat*Vbat/Vmot);
//    	SmartDashboard.putNumber("Enc Pos:", L_Motor.getSensorCollection().getQuadraturePosition());
    	

    }
}
