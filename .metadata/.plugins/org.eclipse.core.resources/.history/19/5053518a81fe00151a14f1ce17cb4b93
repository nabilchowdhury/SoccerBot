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
	private double filteredDistance;
	private int FILTER_OUT = 5;
	private int filterControl;
	
	/**
	 * Takes in a <code>Port</code> object representing the port which the ultrasonic sensor to be sampled
	 * is connected to.
	 * 
	 * @param usPort The <code>Port</code> object the ultrasonic sensor is connected to
	 * 
	 */
	public USPoller(Port usPort){
		this.usPort = usPort;
		this.usSensor = new EV3UltrasonicSensor(usPort);
		this.usValue = usSensor.getMode("Distance");
		this.usData = new float[usSensor.sampleSize()];
	}
	
	/**
	 * Continuously get the sampled data from the ultrasonic sensor and sleep for 50 ms.
	 * Thsi sampled data represents the distance reported and is filtered and bounded for any
	 * erroneous values. The bound is set at 60 cm. 
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		while(true){
			usSensor.fetchSample(usData, 0);
			this.distance = usData[0]*100;
			
			if(this.distance >= 60){
				filteredDistance = 60;
			}
			//filter
			if (this.distance >= 60 && filterControl < FILTER_OUT) {
				// bad value, do not set the distance var, however do increment the filter value
				filterControl++;
			} else if (this.distance >= 60){
				// true max distance
				filteredDistance = 60;
			} else {
				// distance went below 255, therefore reset everything.
				filterControl = 0;
				filteredDistance = this.distance;
			}
			
			// Sleeps for 50 ms
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
		}
	}
	
	
	/** 
	 * Get the distance in cm sampled and filtered from the ultrasonic sensor
	 * 
	 * @return Filtered distance in cm sampled from ultrasonic sensor
	 */
	public double getDistance(){
		return this.filteredDistance;
	}
}
