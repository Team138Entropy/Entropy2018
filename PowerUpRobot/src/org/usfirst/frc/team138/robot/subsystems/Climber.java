package org.usfirst.frc.team138.robot.subsystems;

import org.usfirst.frc.team138.robot.RobotMap;
import org.usfirst.frc.team138.robot.commands.Climb;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Climber extends Subsystem {

	public WPI_TalonSRX _winchMotor = new WPI_TalonSRX(RobotMap.WINCH_PORT);
	public WPI_TalonSRX _hookMotor = new WPI_TalonSRX(RobotMap.HOOK_PORT);
	
	private static double _winchSpeed = 0.0;
	private static double _hookSpeed = 0.0;
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Climb());
    }
    
    // Takes values from -1.0 to 1.0
    public void climb(double winchSpeed)
    {
    	_winchSpeed = winchSpeed;
    	if (winchSpeed >= -1.0 && winchSpeed <= 1.0) {
    		_winchMotor.set(ControlMode.PercentOutput, winchSpeed);
    	}
    }
    
    // Takes values from -1.0 to 1.0
    public void rotateHook(double hookSpeed)
    {
    	_hookSpeed = hookSpeed;
    	if (hookSpeed >= -1.0 && hookSpeed <= 1.0) {
    		_hookMotor.set(ControlMode.PercentOutput, hookSpeed);
    	}
    }
    
    public void updateSmartDashboard()
	{
		SmartDashboard.putNumber("Winch Speed", _winchMotor.getMotorOutputPercent());
		SmartDashboard.putNumber("Hook Speed", _hookMotor.getMotorOutputPercent());
	}
    
}