package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grasper extends Subsystem{
	
	// These are some class variables
	
	private Solenoid _grasperSolenoid = new Solenoid(RobotMap.SOLENOID_GRASPER_PORT);
	private Solenoid _wristSolenoid = new Solenoid(RobotMap.SOLENOID_WRIST_PORT);
	
	// Used for Simulation
	private static boolean _isGrasperOpen = true;
	private static boolean _isWristRaised = true;
	private static String _acquisitionState = "off";
	
	private WPI_TalonSRX _leftRollerTalon = new WPI_TalonSRX(RobotMap.LEFT_CUBE_CAN_GRASPER_PORT);
	private WPI_TalonSRX _rightRollerTalon = new WPI_TalonSRX(RobotMap.RIGHT_CUBE_CAN_GRASPER_PORT);
	
	
	// Master
	 SpeedControllerGroup _rollerSpeedController = new SpeedControllerGroup(_leftRollerTalon, _rightRollerTalon);
	
	protected void initDefaultCommand() {
	
	}
	
	public void initialize() {
    	_leftRollerTalon.setInverted(true);
    }
	
	// Grasper Functions
	
	public void toggleGrasper() {
		if (grasperIsOpen()){
			closeGrasper();
		}
		else {
			openGrasper();
		}
	}
	
	public void openGrasper() {
    	_grasperSolenoid.set(Constants.grasperSolenoidActiveOpen);
		_isGrasperOpen = true;
    }
    
    public void closeGrasper() {
    	_grasperSolenoid.set(!Constants.grasperSolenoidActiveOpen);
    	_isGrasperOpen = false;
    }
    
    public boolean grasperIsOpen() {
		return (_grasperSolenoid.get() == Constants.grasperSolenoidActiveOpen);

		// For Simulation
		//return _isGrasperOpen;
	}
    
    // Wrist Functions
    
    public void toggleWrist() {
    	if (wristIsUp()) {
    		lowerWrist();
    	}
    	else {
    		raiseWrist();
    	}
    }
    
    public void raiseWrist() {
    	_wristSolenoid.set(Constants.wristSolenoidActiveRaised);
    	_isWristRaised = true;
    }
    
    public void lowerWrist() {
    	_wristSolenoid.set(!Constants.wristSolenoidActiveRaised);
    	_isWristRaised = false;
    }
	
	public boolean wristIsUp() {
		return (_wristSolenoid.get() == Constants.wristSolenoidActiveRaised);
		
		// For simulation
		// return _isWristRaised;
	}
	
	// Acquisition Roller Functions
	
	public void acquireRollers() {
		_rollerSpeedController.set(Constants.aquireSpeed);
		_acquisitionState = "Acquire";
		
	}
	
	private void deployRollers() {
		_rollerSpeedController.set(Constants.deploySpeed);
		_acquisitionState = "Deploy";
	}
	
	private void holdRollers() {
		_rollerSpeedController.set(Constants.holdSpeed);
		_acquisitionState = "Hold";
	}
	
	private void stopRollers() {
		_rollerSpeedController.set(0);
		_acquisitionState = "off";
	}
	
	// Command Functions
	
	public void StartAcquire() {
		SmartDashboard.putString("Acquire Release","Start Acquire");
		closeGrasper();
		acquireRollers(); 
	}
	
	public void CompleteAcquire() {
		SmartDashboard.putString("Acquire Release", "Complete Acquire");
		holdRollers();
	}

	public void StartRelease() {
		SmartDashboard.putString("Acquire Release","Start Release");
		lowerWrist();
		deployRollers();
	}
	
	public void CompleteRelease() {
		SmartDashboard.putString("Acquire Release","Complete Release");
		stopRollers();
		openGrasper();
	}
	public void updateSmartDashboard()
	{
		if (grasperIsOpen()) {
			SmartDashboard.putString("Grasper", "open");
		}
		else {
			SmartDashboard.putString("Grasper", "closed");
		}
		
		if (wristIsUp()) {
			SmartDashboard.putString("Wrist", "raised");
		}
		else {
			SmartDashboard.putString("Wrist", "lowered");
		}
		
		SmartDashboard.putString("Acquisition", _acquisitionState );
	}
}
