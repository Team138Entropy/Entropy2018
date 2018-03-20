package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;
import org.usfirst.frc.team138.robot.commands.AutoAcquire;

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
	
	public enum RollerState {
		OFF,
		HOLD,
		DEPLOY,
		ACQUIRE
	}
	private static RollerState _acquisitionState = RollerState.OFF;
	
	private WPI_TalonSRX _leftRollerTalon = new WPI_TalonSRX(RobotMap.LEFT_CUBE_CAN_GRASPER_PORT);
	private WPI_TalonSRX _rightRollerTalon = new WPI_TalonSRX(RobotMap.RIGHT_CUBE_CAN_GRASPER_PORT);
	
	private static boolean _isCubeDetected = false;
	private static boolean _isCubeReleased = false;
	
	// Master
	 SpeedControllerGroup _rollerSpeedController = new SpeedControllerGroup(_leftRollerTalon, _rightRollerTalon);
	
	protected void initDefaultCommand() {
		setDefaultCommand(new AutoAcquire());
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
		// return _isGrasperOpen;
	}
    
    // Wrist Functions
    
    public void toggleWrist() {
    	if (isWristUp()) {
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
	
	public boolean isWristUp() {
		return (_wristSolenoid.get() == Constants.wristSolenoidActiveRaised);
		
		// For simulation
		// return _isWristRaised;
	}
	public boolean isWristDown() {
		return (!isWristUp());
	}
	// Acquisition Roller Functions
	
	public boolean isCubeDetected() {
		return (_leftRollerTalon.getOutputCurrent() > 5 || _rightRollerTalon.getOutputCurrent() > 5);
	}
	public boolean isCubeAcquired() {
		return (_leftRollerTalon.getOutputCurrent() > 15 || _rightRollerTalon.getOutputCurrent() > 15);
	}
	
	public void toggleCube() {
		if (_isCubeDetected) {
			_isCubeDetected = false;
		}
		else {
			_isCubeDetected = true;
		}
	}
	
	public void acquireRollers() {
		_rollerSpeedController.set(Constants.aquireSpeed);
		_acquisitionState =  RollerState.ACQUIRE;
	}
	
	private void deployRollers() {
		_rollerSpeedController.set(Constants.deploySpeed);
		_acquisitionState =  RollerState.DEPLOY;
	}
	
	private void holdRollers() {
		_rollerSpeedController.set(Constants.holdSpeed);
		_acquisitionState =  RollerState.HOLD;
	}
	
	private void stopRollers() {
		_rollerSpeedController.set(0);
		_acquisitionState =  RollerState.OFF;
	}
	
	public boolean isRollerState(RollerState rollerState) {
		return (_acquisitionState == rollerState);
	}
	
	private String convertAcquisitionStateString(RollerState rollerState) {
		String acquisitionState;
		switch (rollerState)
		{
		case OFF: acquisitionState = "OFF";
			break;
		case HOLD: acquisitionState = "HOLD";
			break;
		case DEPLOY: acquisitionState = "DEPLOY";
			break;
		case ACQUIRE: acquisitionState = "ACQUIRE";
			break;
		default: acquisitionState = "INVALID";
			break;
		}
		return acquisitionState;
	}
	
	// Command Functions
	
	public void StartAcquire() {
		SmartDashboard.putString("Acquire Release","Start Acquire");
		closeGrasper();
		acquireRollers(); 
		_isCubeReleased = false;
	}
	
	public void CompleteAcquire() {
		SmartDashboard.putString("Acquire Release", "Complete Acquire");
		holdRollers();
		_isCubeReleased = false;
	}

	public void StartRelease() {
		SmartDashboard.putString("Acquire Release","Start Release");
		lowerWrist();
		deployRollers();
		_isCubeReleased = false;
	}
	
	public void CompleteRelease() {
		SmartDashboard.putString("Acquire Release","Complete Release");
		stopRollers();
		openGrasper();
		_isCubeReleased = true;
	}
	
	public boolean isCubeReleased() {
		return _isCubeReleased;
	}
	
	public void updateSmartDashboard()
	{
		if (grasperIsOpen()) {
			SmartDashboard.putString("Grasper", "open");
		}
		else {
			SmartDashboard.putString("Grasper", "closed");
		}
		
		if (isWristUp()) {
			SmartDashboard.putString("Wrist", "raised");
		}
		else {
			SmartDashboard.putString("Wrist", "lowered");
		}
		
		SmartDashboard.putBoolean("Cube", _isCubeDetected);
		SmartDashboard.putString("Acquisition", convertAcquisitionStateString(_acquisitionState));
		SmartDashboard.putNumber("L Acquisition Current", _leftRollerTalon.getOutputCurrent());
		SmartDashboard.putNumber("R Acquisition Current", _rightRollerTalon.getOutputCurrent());
	}
}
