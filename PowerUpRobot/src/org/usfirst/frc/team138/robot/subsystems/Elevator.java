package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem{

	public WPI_TalonSRX _elevatorMotor = new WPI_TalonSRX(RobotMap.ELEVATOR_PORT);
	
	public DigitalInput _lowerLimitSwitch = new DigitalInput(0);
	public DigitalInput _upperLimitSwitch = new DigitalInput(1);
	
	// Servo Loop Gains
	double _liftKf = 0.2;
	double _liftKp = 1;
	double _liftKi = 0;
	double _liftKd = 5;
	
	private double stopDistance;
	
	double _cruiseVelocity = 75;
	double _acceleration = 40; 
	
	// Talon SRX/ Victor SPX will support multiple (cascaded) PID loops
	// For now we just want the primary one.
	public static final int kElevatorPIDLoopIndex = 0;
	
	// Elevator motion command timeout
	public static final int kElevatorTimeoutMs = 10;
	
	public enum ElevatorTarget{
		etAcquire,
		etSwitch,
		etScale
	}
	
	private boolean _isMovingToTarget = false;
	
	private double _targetPosition = 0.0;
	private double _currentPosition = 0.0;
	
	public void ElevatorInit() {
		_isMovingToTarget = false;
		/* set the peak and nominal outputs, 12V means full */
		_elevatorMotor.configNominalOutputForward(0, kElevatorTimeoutMs);
		_elevatorMotor.configNominalOutputReverse(0, kElevatorTimeoutMs);
		_elevatorMotor.configPeakOutputForward(1, kElevatorTimeoutMs);
		_elevatorMotor.configPeakOutputReverse(-1, kElevatorTimeoutMs);
		
		_elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, kElevatorPIDLoopIndex, kElevatorTimeoutMs);
		_elevatorMotor.setSensorPhase(false);
		
		/* set the allowable closed-loop error,
		 * Closed-Loop output will be neutral within this range.
		 * See Table in Section 17.2.1 for native units per rotation. 
		 */
		_elevatorMotor.configAllowableClosedloopError(0, kElevatorPIDLoopIndex, kElevatorTimeoutMs); /* always servo */

		stopDistance = 0.5 * _cruiseVelocity * _cruiseVelocity / _acceleration;
		
		_elevatorMotor.config_kF(kElevatorPIDLoopIndex, _liftKf, kElevatorTimeoutMs);
		_elevatorMotor.config_kP(kElevatorPIDLoopIndex, _liftKp, kElevatorTimeoutMs);
		_elevatorMotor.config_kI(kElevatorPIDLoopIndex, _liftKi, kElevatorTimeoutMs);
		_elevatorMotor.config_kD(kElevatorPIDLoopIndex, _liftKd, kElevatorTimeoutMs);

		// Integral control only applies when the error is small; this avoids integral windup
		_elevatorMotor.config_IntegralZone(0, 200, kElevatorTimeoutMs);

		// Set cruise velocity and acceleration
		_elevatorMotor.configMotionCruiseVelocity((int) _cruiseVelocity, kElevatorTimeoutMs);
		_elevatorMotor.configMotionAcceleration((int) _acceleration, kElevatorTimeoutMs);
	}
	
	protected void initDefaultCommand() {
		
	}
	
	// Sets up the move
	public ElevatorTarget ConvertToTarget(String target) {
		ElevatorTarget elevatorTarget;
		
		switch (target) {
		case "Aquire":
			elevatorTarget = ElevatorTarget.etAcquire;
			break;
		case "Switch":
			elevatorTarget = ElevatorTarget.etSwitch;
			break;
		case "Scale":
			elevatorTarget = ElevatorTarget.etScale;
			break;
		default:
			elevatorTarget = ElevatorTarget.etAcquire;
			break;
		}
		return elevatorTarget;
	}
	
	public void HomeElevator()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, Constants.elevatorHomingSpeed);
	}
	
	public void Elevate (ElevatorTarget target) {
		switch (target) {
		case etAcquire:
			_targetPosition = 0; // Random values TODO: Add actual values
			break;
		case etSwitch:
			_targetPosition = 500; // Random values TODO: Add actual values
			break;
		case etScale:
			_targetPosition = 1500; // Random values TODO: Add actual values
			break;
		default:
			// Error 
			break;
			
		}
		
		_currentPosition = GetElevatorPosition();
		
		if (_targetPosition > _currentPosition) {
			_elevatorMotor.set(ControlMode.PercentOutput , _cruiseVelocity / 100);
		}
		else {
			_elevatorMotor.set(ControlMode.PercentOutput , -_cruiseVelocity / 100);
		}
		
		_isMovingToTarget = true;
	}
	
	public double GetElevatorPosition() {
		 return _elevatorMotor.getSelectedSensorPosition(kElevatorPIDLoopIndex);
	}
	
	// Execute to move
	public void Execute() {
		_currentPosition = GetElevatorPosition();
		
		if (_isMovingToTarget) {
			// Monitor distance to Goal
			if (Math.abs(_targetPosition - _currentPosition) < stopDistance) {
				_elevatorMotor.set(ControlMode.MotionMagic, _targetPosition);
				_isMovingToTarget = false;
			}
		}
	}
	
	public void updateSmartDashboard()
	{
		SmartDashboard.putNumber("Current Positon", GetElevatorPosition());
		SmartDashboard.putNumber("Target Positon", _targetPosition);
		SmartDashboard.putNumber("Stop Distance", stopDistance);
		SmartDashboard.putBoolean("Is Moving to Target", _isMovingToTarget);
	}
	
	public void StopHoming()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, 0);
		_elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}
	
	// Interface to let command know it's done
	public boolean IsMoveComplete() {
		
		return !_isMovingToTarget;
	}
	
	public void CancelMove() {
		
		_targetPosition = _currentPosition;
	}
	
	public void StopMoving()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, 0);
		_isMovingToTarget = false;
	}
}
