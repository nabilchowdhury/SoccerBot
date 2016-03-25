package soccerbot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * <code>USPoller</code> polls the Ultrasonic sensor every 50 ms 
 * for new data and updates the distance accordingly
 */
public class USPoller extends Thread{
		
	private Port usPort;
	private SensorModes usSensor;
	private SampleProvider usValue;
	private float[] usData;
	private double distance;
	
	/**
	 * Takes in a <code>EV3UltrasonicSensor</code> object to being sampling
	 * 
	 * @param myUSSensor The Ultrasonic sensor being sampled
	 */
	public USPoller(Port usPort){
		this.usPort = usPort;
		this.usSensor = new EV3UltrasonicSensor(usPort);
		this.usValue = usSensor.getMode("Distance");
		this.usData = new float[usSensor.sampleSize()];
	}
	
	/**
	 * Continuously get the sampled data from the ultrasonic sensor and sleep for 50 ms
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		while(true){
			usSensor.fetchSample(usData, 0);
			this.distance = usData[0]*100;
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
	public double getDistance(){
		if(this.distance >= 255){
			this.distance = 255;
		}
		return this.distance;
	}
}
