package org.usfirst.frc.team138.robot;

public class AutoLocations {
	
    private static final int[][] scaleNear = new int[][] {{0, 1}, {1, 2}, {2, 3}, {2, 5}};
	private static final int[][] scaleFar  = new int[][] {{1, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 7}, {8, 9}};
	private static final int[][] centerLSw = new int[][] {{10, 13}, {13, 14}, {14, 11}};
	private static final int[][] centerRSw = new int[][] {{10, 13}, {13, 15}, {15, 12}};
	
	public double angleModifier = 1.0;
	
	public int curentStep = 0;
	
	private static final double[][] locations =
	                       //X cm   Y cm   Angle degrees
			new double[][] {{45.0,  119.0, 0.0},    // Start Position - Right
							{707.0, 157.0, 35.0},   // Place Near Scale
							{572.0, 239.0, 175.0},  // Pickup End Cube Near Switch
							{560.0, 246.3, 175.0},  // Deposit Second Cube Near Switch
							{690.0, 228.0, 0.0},    // Deposit Second Cube Near Scale
							{611.0, 150.0, 90.0},   // 1st Waypoint Crossfield Traverse
							{611.0, 590.0, 0.0},    // 2nd Waypoint Crossfield Traverse
							{690.0, 590.0, 0.0},    // Deposit Scale Opp Side
							{568.0, 590.0, -170.0}, // Acquire 2nd Cube at Opp Side
							{549.0, 587.0, -170.0}, // Deposit 2nd Cube Far Switch
							{45.0,  350.0, 0.0},    // Center Start
							{295.0, 547.0, 0.0},    // Deposit 1st Cube Left Switch
							{295.0, 275.0, 0.0},    // Deposit 1st Cube Right Switch
							{100.0, 350.0, 0.0},    // Step Away From Center Start Position
							{175.0, 547.0, 0.0},    // Waypoint to Left Switch from Center
							{250.0, 275.0, 0.0}};   // Waypoint to Right Switch from Center
	
	public AutoLocations(String startPosition, String autoMode, String gameData){
		
		// Since our default scalar is 1.0 we only need 
		// to change our modifier to -1.0
		switch (startPosition) {
			case "left" :
				this.angleModifier = -1.0;
				break;
		    default :
		    	break;
		}
		
		
		
	}
							
	public double getDistanceByStep(int autoStep, String routine) {
		
		switch (routine) {
		
		
		
		
		}
		
		return getDistanceByLocations()
		
	}
	
	public double getHeadingByStep(int autoStep, String routine) {
		
	}
	
	public double getDistanceByLocations(int start, int end) {
		
		double dx = locations[start][0] - locations[end][0];
		double dy = locations[start][1] - locations[end][1];

		return Math.sqrt( dx * dx + dy * dy );
		
	}
	
	public double getHeadingByLocations(int start, int end) {
		
		double dx = locations[start][0] - locations[end][0];
		double dy = locations[start][1] - locations[end][1];
		
		return Math.atan2(dy, dx);
		
	}
	

}
