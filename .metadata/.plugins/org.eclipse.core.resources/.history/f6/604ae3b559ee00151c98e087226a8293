package soccerbot;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * <code>USPoller</code> polls the Ultrasonic sensor every 50 ms 
 * for new data and updates the distance accordingly
 */
public class USPoller {
	private EV3UltrasonicSensor usSensor;
	private SampleProvider us;
	private float[] usData;
	private int distance;
	
	/**
	 * Takes in a <code>EV3UltrasonicSensor</code> object to being sampling
	 * 
	 * @param myUSSensor The Ultrasonic sensor being sampled
	 */
	public USPoller(EV3UltrasonicSensor myUSSensor){
		this.usSensor = myUSSensor;
		this.us = usSensor.getMode("Distance");
		this.usData = new float[usSensor.sampleSize()];
	}
	
	/**
	 * Continuously get the sampled data from the ultrasonic sensor and sleep for 50 ms
	 * 
	 * @see java.lang.Thread#run()
	 */
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
	
	/** 
	 * Get the distance in cm sampled from the ultrasonic sensor and filter 
	 * for any erroneous values over 255 cm
	 * 
	 * @return Filtered distance in cm sampled from ultrasonic sensor
	 */
	public int getDistance(){
		if(this.distance >= 255){
			this.distance = 255;
		}
		return this.distance;
	}
}
