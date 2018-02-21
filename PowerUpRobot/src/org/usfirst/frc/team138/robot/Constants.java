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
		
		// Angles
		public final static double rightAngleTwenty = 20.0;
		public final static double leftAngleTwenty = -20.0;
		
		// Distances
		public final static double distanceFiveCentimeters = 5.0;
		public final static double distanceThreeMeters = 300.0;
		
		public final static double distanceLeftScale = distanceThreeMeters + distanceFiveCentimeters;

		// Durations
		public final static double oneSecond    = 1.0;
		public final static double twoSeconds   = 2.0;
		public final static double threeSeconds = 3.0;
		public final static double fourSeconds  = 4.0;
		public final static double fiveSeconds  = 5.0;
		public final static double sixSeconds   = 6.0;
		public final static double sevenSeconds = 7.0;  
		public final static double eightSeconds = 8.0;
		public final static double nineSeconds  = 9.0;
		public final static double tenSeconds   = 10.0;
		
		
		// Drivetrain
		public final static boolean useClosedLoopDrivetrain = true;

		public final static double ClosedLoopCruiseVelocity = 1.75; // meters / second
		public final static double ClosedLoopSlowFactor = 0.4; // slowFactor * CV
		public final static double ClosedLoopSlowRotateFactor = 0.5;
		
		// Wheel spacing ~0.5 Meters;  For zero Turn, each wheel travels
		// on a circle of circumference of pi*0.5 or 1.57 Meters.
		// For 180 Degree turn in 1 second (180 Degrees/sec), each
		// wheel travels 1/2 Circumference of .785 Meters in 1 second
		public final static double ClosedLoopTurnSpeed = 0.5; // Meters/sec
		
		public final static double MaxSpeedChange = 1 * 0.025; // full-speed/sec * .025 seconds
		public final static double MaxSlowSpeedChange = 1 * 0.025;
		public final static double CloseLoopJoystickDeadband = 0.1;
		
		// This is our encoder constant for distance (in METERS) per  encoder pulse
		// 6" Wheels, 15:45 chain drive; 256 encoder counts per drive sprocket rotation
		public final static double MetersPerPulse = Math.PI*6*.0254*15/45/256;
		public final static double SecondsTo100Milliseconds = 0.1;
		
		public final static int LeftDriveEncoderPolarity = -1;
		public final static int RightDriveEncoderPolarity = 1;
		
		// Elevator
		public final static double elevatorHomingSpeed = -0.2;
		public final static double elevatorJogSpeed = 0.5;
		
		//Cube Grasper
		public final static boolean grasperSolenoidActiveOpen = true;
		public final static boolean wristSolenoidActiveRaised = false;
		public final static double acquisitionWheelsPercent = 50.0;
		public final static double aquireSpeed = 0.75;
		public final static double deploySpeed = -0.7;
		public final static double holdSpeed = 0.3;

}