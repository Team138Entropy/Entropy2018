package org.usfirst.frc.team138.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	// CAN Bus Assignments
	public final static int LEFT_MOTOR_CHANNEL_FRONT = 2;
	public final static int LEFT_MOTOR_CHANNEL_BACK = 3;
	public final static int RIGHT_MOTOR_CHANNEL_FRONT = 4;
	public final static int RIGHT_MOTOR_CHANNEL_BACK = 5;
	public final static int CUBE_PWM_GRASPER_PORT = 6;

	
	// PWM
	public final static int ELEVATOR_PORT = 0;
	
	
	// GPIO
	public final static int LEFT_ENCODER_PORT_A = 0;
	public final static int LEFT_ENCODER_PORT_B = 1;
	public final static int RIGHT_ENCODER_PORT_A = 3;
	public final static int RIGHT_ENCODER_PORT_B = 2;
	
	// Analog Input
	
	// Pneumatic Control Module
	public final static int CUBE_SOLENOID_GRASPER_PORT = 0;
	public final static int CUBE_SOLENOID_WRIST_PORT = 2;
	
	// Relay
}
