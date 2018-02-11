package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{

	public WPI_TalonSRX _elevatorMotor = new WPI_TalonSRX(RobotMap.ELEVATOR_PORT);
	
	public DigitalInput _lowerLimitSwitch = new DigitalInput(0);
	public DigitalInput _upperLimitSwitch = new DigitalInput(1);
	
	// Servo Loop Gains
	double _liftKf = 0.2;
	double _liftKp = 0.5;
	double _liftKi = 0;
	double _liftKd = 5;
	
	private double stopDistance;
	
	double _cruiseVelocity = 40;
	double _acceleration = 80; 
	
	// Talon SRX/ Victor SPX will support multiple (cascaded) PID loops
	// For now we just want the primary one.
	public static final int kElevatorPIDLoopIndex = 0;
	
	// Elevator motion command timeout
	public static final int kElevatorTimeoutMs = 10;
	
	public enum ElevatorTarget{
		etAquire,
		etSwitch,
		etScale
	}
	private boolean _isMovingToTarget = false;
	
	private double _targetPosition = 0.0;
	private double _currentPosition = 0.0;
	
	public void ElevatorInit() {
		/* set the peak and nominal outputs, 12V means full */
		_elevatorMotor.configNominalOutputForward(0.1, kElevatorTimeoutMs);
		_elevatorMotor.configNominalOutputReverse(-0.1, kElevatorTimeoutMs);
		_elevatorMotor.configPeakOutputForward(1, kElevatorTimeoutMs);
		_elevatorMotor.configPeakOutputReverse(-1, kElevatorTimeoutMs);
		
		_elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, kElevatorPIDLoopIndex, kElevatorTimeoutMs);
		_elevatorMotor.setSensorPhase(false);
		
		/* set the allowable closed-loop error,
		 * Closed-Loop output will be neutral within this range.
		 * See Table in Section 17.2.1 for native units per rotation. 
		 */
		_elevatorMotor.configAllowableClosedloopError(0, kElevatorPIDLoopIndex, kElevatorTimeoutMs); /* always servo */

	}
	
	protected void initDefaultCommand() {
		
	}
	
	// Sets up the move
	public ElevatorTarget ConvertToTarget(String target) {
		ElevatorTarget elevatorTarget;
		
		switch (target) {
		case "Aquire":
			elevatorTarget = ElevatorTarget.etAquire;
			break;
		case "Switch":
			elevatorTarget = ElevatorTarget.etSwitch;
			break;
		case "Scale":
			elevatorTarget = ElevatorTarget.etScale;
			break;
		default:
			elevatorTarget = ElevatorTarget.etAquire;
			break;
		}
		return elevatorTarget;
	}
	
	public void Elevate (ElevatorTarget target) {
		switch (target) {
		case etAquire:
			_targetPosition = 1000; // Random values TODO: Add actual values
			break;
		case etSwitch:
			_targetPosition = 2000; // Random values TODO: Add actual values
			break;
		case etScale:
			_targetPosition = 3000; // Random values TODO: Add actual values
			break;
		default:
			// Error 
			break;
			
		}
		
		_elevatorMotor.config_kF(kElevatorPIDLoopIndex, _liftKf, kElevatorTimeoutMs);
		_elevatorMotor.config_kP(kElevatorPIDLoopIndex, _liftKp, kElevatorTimeoutMs);
		_elevatorMotor.config_kI(kElevatorPIDLoopIndex, _liftKi, kElevatorTimeoutMs);
		_elevatorMotor.config_kD(kElevatorPIDLoopIndex, _liftKd, kElevatorTimeoutMs);

		// Integral control only applies when the error is small; this avoids integral windup
		_elevatorMotor.config_IntegralZone(0, 200, kElevatorTimeoutMs);

		// Set cruise velocity and acceleration
		_elevatorMotor.configMotionCruiseVelocity((int) _cruiseVelocity, kElevatorTimeoutMs);
		_elevatorMotor.configMotionAcceleration((int) _acceleration, kElevatorTimeoutMs);
		
		//_elevatorMotor.set(ControlMode.MotionMagic, _targetPosition);
		_currentPosition = GetElevatorPosition();
		stopDistance = _cruiseVelocity * _cruiseVelocity / _acceleration;
		
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
		if (_isMovingToTarget) {
			_currentPosition = GetElevatorPosition();
			// Monitor distance to Goal
			if (Math.abs(_targetPosition - _currentPosition) < stopDistance) {
				_elevatorMotor.set(ControlMode.MotionMagic, _targetPosition);
				_isMovingToTarget = false;
			}
			// double pwm = _elevatorMotor.getMotorOutputPercent();
		}
	}
	// Interface to let command know it's done
	public boolean IsMoveComplete() {
		
		return !_isMovingToTarget;
	}
	
	public void CancelMove() {
		
		_targetPosition = _currentPosition;
	}
}
