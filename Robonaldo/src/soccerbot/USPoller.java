package soccerbot;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * <code>USPoller</code> polls the Ultrasonic sensor every 50 ms 
 * for new data and updates the distance accordingly
 */
public class USPoller extends Thread{
	
	private final int FILTER_OUT = 3;
	
	private EV3UltrasonicSensor usSensor;
	private SampleProvider us;
	private float[] usData;
	private int distance;
	private int filterControl;
	
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
			filterDistance();
			// Sleeps for 50 ms
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
		}
	}
	
	
	private void filterDistance(){
		if (this.distance >= 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (this.distance >= 255){
			// true 255, therefore set distance to 255
			this.distance = 255;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
		}
	}
	
	
	/** 
	 * Get the distance in cm sampled from the ultrasonic sensor and filter 
	 * for any erroneous values over 255 cm
	 * 
	 * @return Filtered distance in cm sampled from ultrasonic sensor
	 */
	public int getDistance(){
		return this.distance;
	}
}
