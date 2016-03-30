package soccerbot;

import lejos.robotics.SampleProvider;

/**
 * LSPoller, similar to <code>USPoller</code>, polls the EV3 color sensor for data.
 * This data is used when detecting grid lines.
 * 
 * @author Omar Akkila
 *
 */
public class LSPoller extends Thread {

	private SampleProvider colorSensor;
	private float[] colorData;
	private double lastResult;
	private double result;
	private double difference;
	
	/**
	 * Constructor takes in a <code>SampleProvider</code> object and a flaot array for the sampled data.
	 * 
	 * @param colorSensor The <code>SampleProvider</code> that will allow this to fetch samples
	 * @param colorData Float array to store the sample data
	 */
	public LSPoller(SampleProvider colorSensor, float[] colorData) {
		this.colorSensor = colorSensor;
		this.colorData = colorData;
		
	}
	
	/**
	 * Every time this thread is ran, the last result is saved, the new results is fetch, then we store the difference between them.
	 * The reason for taking the difference is so that this robot may function at different lighting conditions.
	 */
	public void run() {
		while(true) {
			lastResult = this.result;
			this.colorSensor.fetchSample(this.colorData, 0);
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
