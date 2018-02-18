package org.usfirst.frc.team138.robot;
/*
 * Constant values used throughout robot code.
 * In "C" this would be a header file, but alas, this is Java
 */
public class Constants {

	// System Constants
		
		// Deadband applied to Joystick, when
		// magnitude is less than deadBand, then set Magnitude to 0
		public final static double joystickDeadband = 0.09;
		
		// Threshold beyond which high speed mode is enabled for trigger
		public final static double highSpeedModeTriggerThreshold = 0.3;
		
		// Low pass filter on joystick heading - 
		// Filter eqn:  heading(i+1) =joystickDir(i)*Alpha + (1-Alpha)*(heading(i)
		// where Alpha = Ts*2*pi*Freq
		//   Ts is sample period (20 mSec for FRC)
		//   Freq is location of filter pole in Hz
		public static double rotateAlpha = .02*6.28*1;

		//
		// These are autonomous constants useful for the autonomous commands
		//
		
		public final static double distanceSwitch = 310.0;	// 3.1 meters
		public final static double distanceScale = 600.0;	// 6 meters
		public final static double distanceBaseLine = 135.0;	// 1.35 meters
		
		// This is our encoder constant for distance (in METERS) per  encoder pulse
		// 6" Wheels, 15:45 chain drive; 256 encoder counts per drive sprocket rotation
		public static double MetersPerPulse = Math.PI*6*.0254*15/45/256;
		public static double Meters2CM = 100.0; // convert distance in Meters to Centimeters
		
		
		public static int LeftDriveEncoderPolarity = -1;
		public static int RightDriveEncoderPolarity = 1;
		
		// Elevator
		public final static double elevatorHomingSpeed = -0.2;
		public final static double elevatorJogSpeed = 0.3;
		
		//Cube Grasper
		public final static boolean grasperSolenoidActiveOpen = true;
		public final static boolean wristSolenoidActiveRaised = true;
		public final static double acquisitionWheelsPercent = 50.0;
		public final static double aquireSpeed = 0.75;
		public final static double deploySpeed = -0.7;
		public final static double holdSpeed = 0.3;

}