package org.usfirst.frc.team138.robot;
// import java.math.*;
/*
 * Constant values used throughout robot code.
 * In "C" this would be a header file, but alas, this is Java
 */
public class Constants {

	// System Constants
		
		// Deadband applied to Joystick, when
		// magnitude is less than deadBand, then set Magnitude to 0
		public final static double joystickDeadband = 0.09;
		
		// Low pass filter on joystick heading - 
		// Filter eqn:  heading(i+1) =joystickDir(i)*Alpha + (1-Alpha)*(heading(i)
		// where Alpha = Ts*2*pi*Freq
		//   Ts is sample period (20 mSec for FRC)
		//   Freq is location of filter pole in Hz
		public static double rotateAlpha = .02*6.28*1;
		
		//
		// This is our encoder constant for distance (in METERS) per  encoder pulse
		// 6" Wheels, 15:45 chain drive; 256 encoder counts per drive sprocket rotation
		public static double MetersPerPulse = Math.PI*6*.0254*15/45/256;
		
		public static int LeftDriveEncoderPolarity = -1;
		public static int RightDriveEncoderPolarity = 1;
		

}
