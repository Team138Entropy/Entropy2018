package org.usfirst.frc.team138.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team138.robot.subsystems.*;
import org.usfirst.frc.team138.robot.commands.*;

//import edu.wpi.first.wpilibj.Preferences;

/**
 * This is Master branch.
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory .
 */
public class Robot extends IterativeRobot {
	// Interface with players
    SendableChooser<String> teamChooser;
    SendableChooser<String> startPosChooser;
    SendableChooser<String> autoModeChooser;
        
    // Subsystems
    public static final Compressor compressor = new Compressor();
    public static final Drivetrain drivetrain = new Drivetrain();
    public static final Grasper grasper = new Grasper();
    public static final Elevator elevator = new Elevator();
    // public static final Climber climber = new Climber();

    public static final OI oi = new OI();
	
    Preferences prefs = Preferences.getInstance();
	
    // Commands
    AutonomousCommand autonomousCommand;
    
    // Global constants
    public static String mode; // "auto" or "teleop"
    public static String gameData;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	drivetrain.DriveTrainInit();
    	compressor.start();
		Sensors.initialize();
		grasper.initialize();
		elevator.ElevatorInit();

		// Smart Dashboard Initialization
		Sensors.updateSmartDashboard();
		SmartDashboard.putData(Scheduler.getInstance());
		
		teamChooser = new SendableChooser<String>();
		teamChooser.addDefault("Red Alliance", "red");
		teamChooser.addObject("Blue Alliance", "blue");
		SmartDashboard.putData("Team:", teamChooser);
		
		startPosChooser = new SendableChooser<String>();
		startPosChooser.addObject("Left", "left");
		startPosChooser.addDefault("Middle", "middle");
		startPosChooser.addObject("Right", "right");
		SmartDashboard.putData("Starting Position:", startPosChooser);
		
		autoModeChooser = new SendableChooser<String>();
		autoModeChooser.addObject("Nothing", "DNE");
		autoModeChooser.addDefault("Automatic", "auto");
		autoModeChooser.addObject("Manual", "manual");
		autoModeChooser.addObject("Test" , "test");
		SmartDashboard.putData("Auto Mode:", autoModeChooser);
					
		SmartDashboard.putBoolean("practiceBot", isPracticeRobot());		

    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
    	mode = "auto";
		SmartDashboard.putData("Team:", teamChooser);
		SmartDashboard.putData("Starting Position:", startPosChooser);		
		SmartDashboard.putData("Auto Mode:", autoModeChooser);
    	
		/*
    	Constants.kPRotate=prefs.getDouble("Rotate KP", .02);
    	Constants.kDRotate=prefs.getDouble("Rotate KD", .0);
    	Constants.kIRotate=prefs.getDouble("Rotate KI", .001);
    	Constants.AutoDriveRotateOvershoot=prefs.getDouble("AutoDrive Overshoot", 4); // Degrees
    	*/


    	
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
        autonomousCommand = new AutonomousCommand(teamChooser.getSelected(), 
        		startPosChooser.getSelected(),
        		autoModeChooser.getSelected(),
        		gameData);
        isPracticeRobot();
        autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Sensors.updateSmartDashboard();
    }

    public void teleopInit() {
    	mode = "teleop";
        if (autonomousCommand != null) {
        	autonomousCommand.cancel();        	
        }        
        Constants.practiceBot = isPracticeRobot();
    	Sensors.resetEncoders();
    	elevator.StopMoving();
    	
    }
    
    public boolean isPracticeRobot() {
    	return (! Sensors.practiceRobotJumperPin.get());
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
//		LiveWindow.run();
        
		
        Sensors.updateSmartDashboard();
        elevator.updateSmartDashboard();
        // climber.updateSmartDashboard();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
  //      LiveWindow.run();
    }
}
