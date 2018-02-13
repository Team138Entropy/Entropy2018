package org.usfirst.frc.team138.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team138.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public final class OI {
	
	// Xbox Controller Map
	static final int xboxController = 0;
	// Xbox Buttons
	static final int xboxA = 1;
	static final int xboxB = 2;
	static final int xboxX = 3;
	static final int xboxY = 4;
	static final int xboxLeftBumper = 5;
	static final int xboxRightBumper = 6;
	static final int xboxLeftStick = 7;
	static final int xboxRightStick = 8;
	static final int xboxMenu = 9;
	static final int xboxView = 10;
	static final int xboxHome = 11;
	static final int xboxDpadUp = 12;
	static final int xboxDpadDown = 13;
	static final int xboxDpadLeft = 14;
	static final int xboxDpadRigt = 15;
	
	//Xbox axes
	static final int xboxLeftXAxis = 0;
	static final int xboxLeftYAxis = 1;
	static final int xboxLeftTriggerAxis = 2;
	static final int xboxRightTriggerAxis = 3;
	static final int xboxRightXAxis = 4;
	static final int xboxRightYAxis = 5;
	
	// Nyko Air Flow Controller Map
	static final int nykoController = 1;
	// Nyko buttons
	static final int nykoButton1 = 1;
	static final int nykoButton2 = 2;
	static final int nykoButton3 = 3;
	static final int nykoButton4 = 4;
	static final int nykoLeftBumper = 5;
	static final int nykoRightBumper = 6;
	static final int nykoLeftTrigger = 7;
	static final int nykoRightTrigger = 8;
	static final int nykoMiddle9 = 9;
	static final int nykoMiddle10 = 10;
	static final int nykoMiddle11 = 11;
	static final int nykoLeftStick = 12;
	static final int nykoRightStick = 13;
	
	// Nyko axes
	static final int nykoLeftXAxis = 0;		// X Axis on Driver Station
	static final int nykoLeftYAxis = 1;		// Y Axis on Driver Station
	static final int nykoRightYAxis = 2;	// Z Axis on Driver Station
	static final int nykoRightXAxis = 3;	// Rotate Axis on Driver Station
	
    static Joystick driverStick = new Joystick(xboxController);
    static Joystick operatorStick = new Joystick(nykoController);
    
    // Driver Stick
    static Button sampleButton 		= new JoystickButton(driverStick, xboxA);
    
    // Operator Stick
    static Button toggleGearRamButton 			= new JoystickButton(operatorStick, nykoButton1);
    static Button toggleRopeGrabberButton 		= new JoystickButton(operatorStick, nykoButton2);
    static Button chuteAcquireButton			= new JoystickButton(operatorStick, nykoButton3);
    static Button floorAcquireButton	 		= new JoystickButton(operatorStick, nykoButton4);
    static Button toggleWristButton 			= new JoystickButton(operatorStick, nykoLeftBumper);
    static Button toggleClawButton 			= new JoystickButton(operatorStick, nykoRightBumper);
    static Button shootButton 					= new JoystickButton(operatorStick, nykoLeftTrigger);
    static Button zeroTurn                     = new JoystickButton(operatorStick, nykoRightTrigger);
    static Button autoPositionShooterButton 	= new JoystickButton(operatorStick, nykoMiddle9);
    static Button autoGearPlaceButton 			= new JoystickButton(operatorStick, nykoMiddle10);
    static Button cancelAutoRoutinesButton 	= new JoystickButton(operatorStick, nykoMiddle11);
    
    static double lastX=0;
    static double LastY=0;
    
    public OI(){
    	
    }
    
    public static boolean autoRoutinesCancelled()
    {
    	System.out.println("cancelled auto routines");
    	return cancelAutoRoutinesButton.get();
    }
    
	public static double getMoveSpeed()
	{
		return driverStick.getRawAxis(xboxLeftYAxis);
	}
	
	public static double getRotateSpeed()
	{
		return driverStick.getRawAxis(xboxRightXAxis);
	}
	
	public static double getClimbSpeed()
	{
		return operatorStick.getRawAxis(nykoLeftYAxis);
	}
	
	public static boolean isReverse() {
		return driverStick.getRawButton(xboxB);
	}
	
	public static boolean isFullSpeed() {
		return driverStick.getRawAxis(xboxRightTriggerAxis) > Constants.highSpeedModeTriggerThreshold;
	}
	
	
	public static double [] getFieldCommand()
	{
		double Magnitude, Direction, x, y;
		double [] result = new double[2];
		// Coeff for cubic polynomial map btwn joystick and magnitude
		/*
		double A=.3;
		double B=.25;
		double C=-.1852;
		double D=.85734;*/
			// Linear, with offset
		double A=.1;
		double B=1;
		double C=0;
		double D=0;
		
		
		double z;

		y=-driverStick.getRawAxis(xboxLeftYAxis); // Inverted Y axis so "fwd" = +90 degrees
		x=driverStick.getRawAxis(xboxLeftXAxis);
		Magnitude=Math.sqrt(x*x+y*y);
		// Apply deadband to avoid "creep" when joystick
		// does not return "0" at center position.
		if (Magnitude<Constants.joystickDeadband)
			Magnitude=0;
		else {		
			//
			// Cubic Polynomial maps between raw Joystick and Magnitude
			z=Magnitude-Constants.joystickDeadband;		
			Magnitude=A+B*z+C*z*z+D*z*z*z;		
			// Normalize to maximum of +/-1
			if (Math.abs(Magnitude)>1)
				Magnitude = Magnitude/Math.abs(Magnitude);
		}
		
		result[0]=Magnitude;
		
		// Filter joystick coordinates using simple exponential filter
		lastX=Constants.rotateAlpha*x + (1-Constants.rotateAlpha)*lastX;
		LastY=Constants.rotateAlpha*y + (1-Constants.rotateAlpha)*LastY;
		// Direction, in degrees, in range +/- 180
		Direction=180/Math.PI*Math.atan2(LastY, lastX);
		// Joystick "0" degrees is to the "right", so that
		// angles are reported in conventional Cartesian coordinate system
		// with +X to the right and +Y is ahead (forward relative to operator).
		result[1]=Direction;
		// Note - must manage offset between gyro heading (raw heading reported as "0"
		// when robot starts and is facing in the +Y in Field (Cartesian) coordinates.
		// The offset between gyro and operator (joystick) is managed in the Sensors class.
		
		return result;
		
	}
	

	public static boolean isZeroTurn()
	{
		boolean zt;
		// Execute a zero-turn (rotate about robot center) if zero-turn button is pressed
		zt=driverStick.getRawButton(xboxX) | driverStick.getRawButton(xboxY);
		return zt;
	}
	    
} // :D)))

