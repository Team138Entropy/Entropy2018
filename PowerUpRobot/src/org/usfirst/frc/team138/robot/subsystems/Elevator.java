package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.Constants;
import org.usfirst.frc.team138.robot.RobotMap;
import org.usfirst.frc.team138.robot.commands.JogElevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
	
	double _cruiseVelocity = 75;
	double _acceleration = 40; 
	
	// Talon SRX/ Victor SPX will support multiple (cascaded) PID loops
	// For now we just want the primary one.
	public static final int kElevatorPIDLoopIndex = 0;
	
	// Elevator motion command timeout
	public static final int kElevatorTimeoutMs = 10;
	
	public enum ElevatorTarget{
		NONE,
		JOG,			// Jog Mode
		HOME,			// Home Mode
		ACQUIRE,		// Acquire Cube Level 1 (Floor)
		EXCHANGE,		// Deposit Exchange
		CUBE_LEVEL_2,	// Acquire Cube Level 2
		SWITCH,			// Deposit Switch / Acquire Cube Level 3 
		SCALE,			// Deposit Lower Scale
		UPPER_SCALE		// Deposit Upper Scale
	}
	
	private static int _count = 0;

	private int _direction = 0;		// 0: not moving to target, -1 or 1 moving to target in that direction
	
	private double _targetPosition = 0.0;
	private double _currentPosition = 0.0;
	private ElevatorTarget _alternateElevatorTarget = ElevatorTarget.NONE;
	
	private int _currentJogDirection = 0;
	
	public void ElevatorInit() {
		// initial direction is 0, since elevator is not moving
		_direction = 0;
		
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
		
		_elevatorMotor.config_kF(kElevatorPIDLoopIndex, _liftKf, kElevatorTimeoutMs);
		_elevatorMotor.config_kP(kElevatorPIDLoopIndex, _liftKp, kElevatorTimeoutMs);
		_elevatorMotor.config_kI(kElevatorPIDLoopIndex, _liftKi, kElevatorTimeoutMs);
		_elevatorMotor.config_kD(kElevatorPIDLoopIndex, _liftKd, kElevatorTimeoutMs);

		// Set current limit on elevator motor Talon
		// current limit applies to current drawn from battery
		// limit of 20 amps implies 12*20 = 240 Watts max power drawn from
		// battery.  Actual motor current at stall is sqrt(Watts/R)
		// 775 Motor resistance ~0.15 Ohms.  So 20 Amps input equates to 40 Amps in motor
		// at Stall.
		_elevatorMotor.configContinuousCurrentLimit(20, 5000);
		_elevatorMotor.configPeakCurrentLimit(30, 2000);
		_elevatorMotor.enableCurrentLimit(true);
		
		// Set brake mode to hold at position
		_elevatorMotor.setNeutralMode(NeutralMode.Brake);		
		
		// Integral control only applies when the error is small; this avoids integral windup
		_elevatorMotor.config_IntegralZone(0, 200, kElevatorTimeoutMs);

		// Set cruise velocity and acceleration
		_elevatorMotor.configMotionCruiseVelocity((int) _cruiseVelocity, kElevatorTimeoutMs);
		_elevatorMotor.configMotionAcceleration((int) _acceleration, kElevatorTimeoutMs);
	}
	
	protected void initDefaultCommand() {
		setDefaultCommand(new JogElevator());
	}
	
	// Convert the target string to an ElevatorTarget
	public ElevatorTarget ConvertToTarget(String target) {
		ElevatorTarget elevatorTarget;
		
		switch (target) {
		case "Aquire":
			elevatorTarget = ElevatorTarget.ACQUIRE;
			break;
		case "Exchange": 
			elevatorTarget = ElevatorTarget.EXCHANGE;
			break;
		case "CubeLevel2": 
			elevatorTarget = ElevatorTarget.CUBE_LEVEL_2;
			break;
		case "Switch":
			elevatorTarget = ElevatorTarget.SWITCH;
			break;
		case "Scale":
			elevatorTarget = ElevatorTarget.SCALE;
			break;
		case "UpperScale":
			elevatorTarget = ElevatorTarget.UPPER_SCALE;
				break;
		default:
			elevatorTarget = ElevatorTarget.ACQUIRE;
			break;
		}
		return elevatorTarget;
	}
	
	// Convert the Elevator Target to its string representation
	public String ConvertToString(ElevatorTarget target)
	{
		String elevatorTarget;
		
		switch (target) {
		case NONE: 
			elevatorTarget = "None";
			break;
		case JOG: 
			elevatorTarget = "Jog";
			break;
		case HOME: 
			elevatorTarget = "Home";
			break;
		case ACQUIRE:
			elevatorTarget = "Acquire";
			break;
		case EXCHANGE:
			elevatorTarget = "Exchange";
			break;
		case CUBE_LEVEL_2:
			elevatorTarget = "Cube Level 2";
			break;
		case SWITCH:
			elevatorTarget = "Switch";
			break;
		case SCALE:
			elevatorTarget = "Scale";
			break;
		case UPPER_SCALE:
			elevatorTarget = "Upper Scale";
			break;
		default:
			elevatorTarget = "Invalid";
			break;
		}
		return elevatorTarget;
		
	}
	
	// Start jogging the elevator
	public void JogElevator(int jogDirection, double jogSpeed)
	{
		_currentJogDirection = jogDirection;
		_elevatorMotor.set(ControlMode.PercentOutput, jogSpeed * jogDirection);
	}
	
	// Start homing the elevator
	public void HomeElevator()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, Constants.elevatorHomingSpeed);
	}
	
	// Elevate to a specific target position
	public void Elevate (ElevatorTarget target) {
		_count++;
		if (target == ElevatorTarget.NONE)
		{
			StopMoving();
		}
		else
		{		
		if (Constants.practiceBot) { // These numbers need updating
			switch (target) {
			case ACQUIRE:
				_alternateElevatorTarget = ElevatorTarget.EXCHANGE;
				_targetPosition = 0;	// Acquire Height is Cube Level 1
				break;
			case EXCHANGE:
				_targetPosition = 500;	// Alternate Acquire position is Exchange
										//TODO: determine real position
				break;
			case CUBE_LEVEL_2:
				_targetPosition = 900;	// Alternate Switch position is Cube Level 2
				break;
										// TODO: determine real position
			case SWITCH:
				_alternateElevatorTarget = ElevatorTarget.CUBE_LEVEL_2;
				_targetPosition = 1200; // Switch height is also Cube Level 3
				break;
			case SCALE:
				_alternateElevatorTarget = ElevatorTarget.UPPER_SCALE;
				_targetPosition = 2500;	// Default scale position is lower scale
				break;
			case UPPER_SCALE:
				_targetPosition = 2700;	// Alternate scale position is upper scale
			default:
				// Error 
				break;
			}
		}
		else
		{
			switch (target) {
			case ACQUIRE:
				_alternateElevatorTarget = ElevatorTarget.EXCHANGE;
				_targetPosition = 0;	// Acquire Height is Cube Level 1
				break;
			case EXCHANGE:
				_targetPosition = 200;	// Alternate Acquire position is Exchange
										//TODO: determine real position
				break;
			case CUBE_LEVEL_2:
				_targetPosition = 300;	// Alternate Switch position is Cube Level 2
				break;
										// TODO: determine real position
			case SWITCH:
				_alternateElevatorTarget = ElevatorTarget.CUBE_LEVEL_2;
				_targetPosition = 1000; // Switch height is also Cube Level 3
				break;
			case SCALE:
				_alternateElevatorTarget = ElevatorTarget.UPPER_SCALE;
				_targetPosition = 2000;	// Default scale position is lower scale
				break;
			case UPPER_SCALE:
				_targetPosition = 2600;	// Alternate scale position is upper scale
			default:
				// Error 
				break;
			}
		}
			_currentPosition = GetElevatorPosition();
			
			if (_targetPosition > _currentPosition) {
				_direction = 1;
			}
			else {
				_direction = -1;
			}
			
			_elevatorMotor.set(ControlMode.PercentOutput , _direction *_cruiseVelocity / 100);
		}
	}
	
	public void ElevateToAlternateTarget()
	{
		Elevate(_alternateElevatorTarget);
	}
	
	// Return the elevator position in encoder counts
	public double GetElevatorPosition() {
		 return _elevatorMotor.getSelectedSensorPosition(kElevatorPIDLoopIndex);
	}
	
	// Execute to move
	public void Execute() {
		_currentPosition = GetElevatorPosition();
		
		if (_direction != 0) {
			// Monitor distance to Goal
			if ( (_direction > 0 && (_currentPosition > _targetPosition) ||
			     (_direction < 0 && (_currentPosition < _targetPosition) )))
			{
				_elevatorMotor.set(ControlMode.MotionMagic, _targetPosition);
				_direction = 0;
			}
		}
	}
	
	public void updateSmartDashboard()
	{
		SmartDashboard.putNumber("Current Position", GetElevatorPosition());
		SmartDashboard.putNumber("Target Position", _targetPosition);
		SmartDashboard.putNumber("Direction", _direction);
		SmartDashboard.putString("Alternate Target", ConvertToString(_alternateElevatorTarget));
		SmartDashboard.putNumber("Jog Direction", _currentJogDirection);
		SmartDashboard.putNumber("Elevate Output:",_elevatorMotor.getMotorOutputPercent());
		SmartDashboard.putNumber("Count", _count);
	}
	
	public int getCount()
	{
		return _count;
	}
	
	// Stop the homing move, reset the encoder position 
	public void StopHoming()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, 0);
		_elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}
	
	// Interface to let command know it's done
	public boolean IsMoveComplete() {
		return (_direction == 0);
	}
	
	// Cancel the current elevator move, but don't stop the motion immediately
	// This occurs when another move command is about to start with a new target position
	public void CancelMove() {
		_targetPosition = _currentPosition;
	}
	
	// Stop the current elevator move immediately
	public void StopMoving()
	{
		_elevatorMotor.set(ControlMode.PercentOutput, 0);
		_direction = 0;
	}
}
