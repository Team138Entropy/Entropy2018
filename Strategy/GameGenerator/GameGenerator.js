// This is a random game/robot generator for team 138

function GameRobot () {
	robotName = "";
	driveType = "";
	canSwitch = false;
	canScale = false;
	canClimb = false;
	position = 0;
}

var positions = [];

var finalPrint = "";

function buildRobot (name, drive, cSwitch, cScale, cClimb, pos) {
    var newRobot = new GameRobot();
    newRobot.robotName = name;
    newRobot.driveType = drive;
    newRobot.canScale = cSwitch;
    newRobot.canScale = cScale;
    newRobot.canClimb = cClimb;
    newRobot.position = pos;
    return newRobot;
}

function randomizeRobot (robotName, inputPosition) {
	var setName = robotName;
	var setDrive = "";
	var setSwitch = false;
	var setScale = false;
	var setClimb = false;
	var setPos = 0;
	
	// Sets drive
	if (Math.floor(Math.random() * 2) === 0) {
		setDrive = "3/6";
	} else {
		setDrive = "2/4";
	}
	
	// Sets switch
	if (Math.floor(Math.random() * 2) === 0) {
		setSwitch = true;
	} else {
		setSwitch = false;
	}
	
	// Sets scale
	if (Math.floor(Math.random() * 2) === 0) {
		setScale = true;
	} else {
		setScale = false;
	}
	
	// Sets climb
	if (Math.floor(Math.random() * 2) === 0) {
		setClimb = true;
	} else {
		setClimb = false;
	}
	
	setPos = inputPosition;
	
	var randomizedRobot = buildRobot(setName, setDrive, setSwitch, setScale, setClimb, setPos);
	return randomizedRobot;
}

function printRobot (robot) {
	var stringToPrint = robot.robotName + " is the " + robot.position.toString() + " position robot. It can drive at " + robot.driveType + " speeds and ";
	
	if (!robot.canSwitch) { // Had to invert this for some reason
		stringToPrint = stringToPrint + "can place on the switch";
	} else {
		stringToPrint = stringToPrint + "can't place on the switch";
	}
	
	if (robot.canScale) {
		stringToPrint = stringToPrint + " and can place on the scale";
	} else {
		stringToPrint = stringToPrint + " and can't place on the scale";
	}
	
	if (robot.canClimb) {
		stringToPrint = stringToPrint + " and can climb.";
	} else {
		stringToPrint = stringToPrint + " and can't climb.";
	}
	
	finalPrint = finalPrint +  stringToPrint + "\n\n"
}

function setPositions () {
  positions.push(1);
  
  if (Math.floor(Math.random() * 2) === 0) {
      positions.push(2);
  } else {
      positions.unshift(2);
  }
  
  if (Math.floor(Math.random() * 2) === 0) {
      positions.push(3);
  } else {
      positions.unshift(3);
  }
  
  if (Math.floor(Math.random() * 2) === 0) {
      positions.push(4);
  } else {
      positions.unshift(4);
  }
  
  if (Math.floor(Math.random() * 2) === 0) {
      positions.push(5);
  } else {
      positions.unshift(5);
  }
  
  if (Math.floor(Math.random() * 2) === 0) {
      positions.push(6);
  } else {
      positions.unshift(6);
  }
}

function generateRobots() {
	// Randomly assigns the positions
	setPositions();

	// Creates blue team
	var teamRobot = buildRobot("2018EntropyRobot", "3/6", true, true, true, positions[0]);
	var blue1Bot = randomizeRobot("Alpha", positions[1]);
	var blue2Bot = randomizeRobot("Beta", positions[2]);

	// Creates red team
	var red1Bot = randomizeRobot("Gamma", positions[3]);
	var red2Bot = randomizeRobot("Delta", positions[4]);
	var red3Bot = randomizeRobot("Epsilon", positions[5]);

	// Print our random robots
	printRobot(teamRobot);
	printRobot(blue1Bot);
	printRobot(blue2Bot);
	printRobot(red1Bot);
	printRobot(red2Bot);
	printRobot(red3Bot);

	var gameInt = Math.floor(Math.random() * 4);
	var gameString = "";

	if (gameInt === 0) {
		gameString = "RRR";
	} else if (gameInt == 1) {
		gameString = "LLL";
	} else if (gameInt == 2) {
		gameString = "RLR";
	} else if (gameInt == 3) {
		gameString = "LRL";
	}

	var stringString = "The game state is " + gameString;
	finalPrint = finalPrint + stringString;

	document.getElementById("output").value = finalPrint;
	finalPrint = "";
}