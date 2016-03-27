package soccerbot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class LightLocalizer {
	private Odometer odo;
	private Navigation navigate;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	//color sensor middle
	private Port csPort;
	private SensorModes csSensor;
	private SampleProvider csValue;
	private float[] csData;
	private float color;
	
	// Class constants
	private final int FILTER_COLOR = 1;
	
	private int filterValue = 0;
	
	//display variables, delete this
	public static double x;
	public static double y;
	public static double deltaT;
	public static double thetaX;
	public static double thetaY;
	
	public LightLocalizer(Odometer odo, Navigation navigate, Port csPort, EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		this.odo = odo;
		this.navigate = navigate;
		this.csPort = csPort;
		this.csSensor = new EV3ColorSensor(csPort);
		this.csValue = csSensor.getMode("Red");
		csSensor.setCurrentMode("Red");
		this.csData = new float[csValue.sampleSize()];
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}
	
	// Initiate light localization
	public void localize() {
		
		// go straight 6 cm
		navigate.goStraight(300, 300, 6);
		
		// Turn counterclockwise 90 deg
		navigate.turnTo(-Math.PI/2);
		
		navigate.goStraight(300, 300, 10);
	
		navigate.turnTo(-2*Math.PI/3);
		
		// Initiate arc to catch all 4 grid lines
		leftMotor.backward();
		rightMotor.forward();
		
		int count = 0;
		// Indices used to store angles -> 0 = x-, 1 = y+, 2 = x+, 3 = y-
		// Array used to store angles at all 4 grid lines
		double[] angleArray = new double[4];
		
		// While loop is used to store angles at all 4 gridlines in angleArray[]. 
		while(true){
			if(getColorData() < 27 && getColorData() > 10){
				filterValue++;
				if(filterValue >= FILTER_COLOR){
					Sound.beep();
					angleArray[count] = odo.getTheta();			
					count++;
					filterValue = 0;
				}
			}
			// Break out of loop once all 4 angles are found
			if(count >= 4){
				navigate.stopMotors();
				break;
			}
		}
		
		// Calculating x, y and deltaT
		
		
		// Distance away from gridline cross section
		x = 11.45*Math.cos((angleArray[1] - angleArray[3])/2);
		y = 11.45*Math.cos((angleArray[2] - angleArray[0])/2);
		deltaT = Math.PI/2-(angleArray[3]-Math.PI)+(angleArray[1] - angleArray[3])/2;
		
		// Final sequence to position robot at cross sections at 0 heading
		
		odo.setX(-y);odo.setY(-x);odo.setTheta(odo.getTheta()+deltaT);
		
		navigate.travelTo(0,0); //navigate.turnTo(deltaT);
		//navigate.setSpeeds(150,150,false);
		//navigate.turnTo(-(Math.atan(y/x))+odo.getTheta());
		
	}
	
	// Method that returns color data
	private float getColorData() {
		csSensor.fetchSample(csData, 0);
		float color = csData[0]*100;	
		
		return color;
	}
	


}
