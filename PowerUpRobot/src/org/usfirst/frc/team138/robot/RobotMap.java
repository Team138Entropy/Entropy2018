package org.usfirst.frc.team138.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	// CAN Bus Assignments
	public final static int LEFT_MOTOR_CHANNEL_FRONT    = 2;
	public final static int LEFT_MOTOR_CHANNEL_BACK     = 3;
	public final static int RIGHT_MOTOR_CHANNEL_FRONT   = 4;
	public final static int RIGHT_MOTOR_CHANNEL_BACK    = 5;
	public final static int LEFT_CUBE_CAN_GRASPER_PORT  = 6; //TODO: Configure correct CAN Port
	public final static int RIGHT_CUBE_CAN_GRASPER_PORT = 7; //TODO: Configure correct CAN Port
  
	public final static int ELEVATOR_PORT = 2;
	
//	public final static int LEFT_ENCODER_PORT = LEFT_MOTOR_CHANNEL_FRONT;
//	public final static int RIGHT_ENCODER_PORT = RIGHT_MOTOR_CHANNEL_FRONT;
	
	// Analog Input
	
	// Pneumatic Control Module
	public final static int SOLENOID_GRASPER_PORT  = 0;
	public final static int SOLENOID_WRIST_PORT    = 2;


	
	// Relay
}
