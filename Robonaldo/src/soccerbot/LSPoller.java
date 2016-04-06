package soccerbot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

/**
 * LSPoller, similar to <code>USPoller</code>, polls the EV3 color sensor for data.
 * This data is used when detecting grid lines.
 * 
 * @author Omar Akkila
 *
 */
public class LSPoller extends Thread {

	private Port csPort;
	private SensorModes csSensor;
	private SampleProvider csValue;
	private float[] colorData;
	
	private double lastResult;
	private double result;
	private double difference;
	
	
	
	/**
	 * Constructor takes in a <code>SampleProvider</code> object and a float array for the sampled data.
	 * 
	 * @param csValue The <code>SampleProvider</code> that will allow this to fetch samples
	 * @param colorData Float array to store the sample data
	 */
	public LSPoller(Port csPort) {
		this.csPort = csPort;
		this.csSensor = new EV3ColorSensor(csPort);
		this.csValue = csSensor.getMode("Red");
		csSensor.setCurrentMode("Red");
		this.colorData = new float[csValue.sampleSize()];
	}
	
	/**
	 * Every time this thread is ran, the last result is saved, the new results is fetch, then we store the difference between them.
	 * The reason for taking the difference is so that this robot may function at different lighting conditions.
	 */
	public void run() {
		while(true) {
			lastResult = this.result;
			this.csValue.fetchSample(this.colorData, 0);
			this.result = this.colorData[0];
			this.difference = this.result - this.lastResult;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// Nothing
			}	
		}
	}
	
	/**
	 * Get the difference between the current result and the last result. If robot is traveling on monochromatic surface
	 * result should always be zero (ideally) or close to zero. 
	 * @return
	 */
	public synchronized double getDifferentialData() { 
		return this.difference;
	}
	
	
}
