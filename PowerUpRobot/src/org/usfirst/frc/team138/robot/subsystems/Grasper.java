package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grasper extends Subsystem{
	
	// These are some class variables
	
	private Solenoid _grasperSolenoid = new Solenoid(RobotMap.SOLENOID_GRASPER_PORT);
	private Solenoid _wristSolenoid = new Solenoid(RobotMap.SOLENOID_WRIST_PORT);
	
	private WPI_TalonSRX _leftRollerTalon = new WPI_TalonSRX(RobotMap.LEFT_CUBE_CAN_GRASPER_PORT);
	private WPI_TalonSRX _rightRollerTalon = new WPI_TalonSRX(RobotMap.RIGHT_CUBE_CAN_GRASPER_PORT);
	
	// Master
	 SpeedControllerGroup _rollerSpeedController = new SpeedControllerGroup(_leftRollerTalon, _rightRollerTalon);
	
	protected void initDefaultCommand() {
	
	}
	
	public void Initialize() {
    	_rightRollerTalon.setInverted(true);
    }
	
	private void openGrasper() {
    	_grasperSolenoid.set(Constants.grasperSolenoidActiveOpen);
    }
    
    private void closeGrasper() {
    	_grasperSolenoid.set(!Constants.grasperSolenoidActiveOpen);
    }
    
    public boolean grasperIsOpen() {
		return (_grasperSolenoid.get() == Constants.grasperSolenoidActiveOpen);
	}
    
    private void raiseWrist() {
    	_wristSolenoid.set(Constants.wristSolenoidActiveRaised);
    }
    
    private void lowerWrist() {
    	_wristSolenoid.set(!Constants.wristSolenoidActiveRaised);
    	
    }
	
	public boolean wristIsUp() {
		return (_wristSolenoid.get() == Constants.wristSolenoidActiveRaised);
	}
	
	private void acquireRollers() {
		_rollerSpeedController.set(Constants.aquireSpeed);

	}
	
	private void deployRollers() {
		_rollerSpeedController.set(Constants.deploySpeed);
	}
	
	private void stopRollers() {
		_rollerSpeedController.set(0);
	}
	public void StartAcquire() {
		closeGrasper();
		acquireRollers();
		
	}
	
	public void CompleteAcquire() {
		stopRollers();
		raiseWrist();
	}

	public void StartRelease() {
		lowerWrist();
		deployRollers();
	}
	
	public void CompleteRelease() {
		stopRollers();
		openGrasper();
	}
}
