package soccerbot;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class USPoller {
	private EV3UltrasonicSensor usSensor;
	private SampleProvider us;
	private float[] usData;
	private int distance;
	
	// Constructor, takes in a US sensor as input
	public USPoller(EV3UltrasonicSensor myUSSensor){
		this.usSensor = myUSSensor;
		this.us = usSensor.getMode("Distance");
		this.usData = new float[usSensor.sampleSize()];
	}
	
	// Samples distance
	public void run(){
		while(true){
			us.fetchSample(usData, 0);
			this.distance = (int)(usData[0]*100);
			
			// Sleeps for 50 ms
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
		}
	}
	
	// Returns distance in cm and filters erroneous values over 255 cm
	public int getDistance(){
		if(this.distance >= 255){
			this.distance = 255;
		}
		return this.distance;
	}
}
