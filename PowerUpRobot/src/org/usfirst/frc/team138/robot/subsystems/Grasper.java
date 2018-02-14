package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grasper extends Subsystem{
	
	// These are some class variables
	
	Compressor compressor = new Compressor();
	
	Solenoid leftGrasperSolenoid = new Solenoid(RobotMap.CUBE_SOLENOID_GRASPER_PORT);
	Solenoid rightGrasperSolenoid = new Solenoid(RobotMap.CUBE_SOLENOID_GRASPER_PORT);
	Solenoid primaryWristSolenoid = new Solenoid(RobotMap.CUBE_SOLENOID_WRIST_PORT);
	Solenoid secondaryWristSolenoid = new Solenoid(RobotMap.CUBE_SOLENOID_WRIST_PORT);
	
	public WPI_TalonSRX leftRollerTalon = new WPI_TalonSRX(RobotMap.LEFT_INTAKE_PORT);
	public WPI_TalonSRX rightRollerTalon = new WPI_TalonSRX(RobotMap.RIGHT_INTAKE_PORT);
	
	// Master
	SpeedControllerGroup leftSpeedController = new SpeedControllerGroup(leftRollerTalon);
	// Slave
	SpeedControllerGroup rightSpeedController = new SpeedControllerGroup(rightRollerTalon);
	
	boolean grasperIsOpen = false;
	boolean wristIsUp = true;
	
	private static final double aquireSpeed = 0.9;
	private static final double deploySpeed = -0.7;
	
	protected void initDefaultCommand() {
		rightSpeedController.setInverted(!leftSpeedController.getInverted());
	}
	
	public void stopCompressor()
	{
		compressor.stop();
	}
	
	public void startCompressor()
	{
		compressor.start();
	}
	
	public void wristUp(){
		primaryWristSolenoid.set(false);
		secondaryWristSolenoid.set(false);
		wristIsUp = true;
	}
	
	public void wristDown(){
		primaryWristSolenoid.set(true);
		secondaryWristSolenoid.set(true);
		wristIsUp = false;
	}
	
	public boolean wristIsUp() {
		return wristIsUp;
	}
	
	public void acquireRollers() {
		leftSpeedController.set(aquireSpeed);
		rightSpeedController.set(aquireSpeed);		
	}
	
	public void deployRollers() {
		leftSpeedController.set(deploySpeed);
		rightSpeedController.set(deploySpeed);
	}
	
	
	public void closegrasper(){
		leftGrasperSolenoid.set(false);
		rightGrasperSolenoid.set(false);
		grasperIsOpen = false;
	}
	
	public void opengrasper(){
		leftGrasperSolenoid.set(true);
		rightGrasperSolenoid.set(true);
		grasperIsOpen = true;
	}
	
	public boolean grasperIsOpen() {
		return grasperIsOpen;
	}

}
