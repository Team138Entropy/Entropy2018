package org.usfirst.frc.team138.robot.commands;

import org.usfirst.frc.team138.robot.Robot;
//import org.usfirst.frc.team138.robot.Sensors;

import edu.wpi.first.wpilibj.command.Command;

public class SetWristPosition extends Command {

	boolean isUp;
	boolean toggleMode = false;
	
	public SetWristPosition(boolean flipUp){
		requires(Robot.grasper);
		isUp = flipUp;
	}
	
	public SetWristPosition(){
		requires(Robot.grasper);
		toggleMode = true;
	}

	protected void initialize() {		
		if (toggleMode)
		{
			isUp = !Robot.grasper.wristIsUp();
		}
		if (!isUp == Robot.grasper.wristIsUp()) {
			if (Robot.grasper.grasperIsOpen())
			{
				Robot.grasper.closegrasper();
			}
//			if (!Robot.grasper.guardIsUp()){
	//			Robot.grasper.guardUp();
		//	}
			if (isUp) {
				Robot.grasper.wristUp();
				setTimeout(0.3);
			} else {
				Robot.grasper.wristDown();
				setTimeout(0.0);
			}
		}
	}

	protected void execute() {
		
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
	}

	protected void interrupted() {
	}

}