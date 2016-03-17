/*
 * Odometer.java
 */
package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer extends Thread {
	// class constants: robot dimensions
	private static final double W_BASE = 15.92;	
	private static final double W_RADIUS = 2.096;
	
	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;
	
	// robot position
	private double x, y, theta;	// theta is in radians

	// lock object for mutual exclusion
	private Object lock;
	
	// class variables used to calculate x, y and theta
	private int lastTachoL;			// Tacho L at last sample
	private int lastTachoR;			// Tacho R at last sample 
	private int nowTachoL;			// Current tacho L
	private int nowTachoR;			// Current tacho R

	
	// motors
	private EV3LargeRegulatedMotor leftMotor, rightMotor;

	// constructor
	public Odometer(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		lock = new Object();
		this.leftMotor = leftMotor;
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;
		double distL, distR, deltaD, deltaT, dX, dY;
		
		// reset tacho count at the start of each execution
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		
		// set initial values of lastTacho
		lastTachoL = leftMotor.getTachoCount();
		lastTachoR = rightMotor.getTachoCount();
		
		
		while (true) {
			updateStart = System.currentTimeMillis();

			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!
				
				// get current tacho counts for each wheel
				nowTachoL = leftMotor.getTachoCount();
				nowTachoR = rightMotor.getTachoCount();
				
				// calculate left and right wheel displacements
				distL = Math.PI*W_RADIUS*(nowTachoL - lastTachoL)/180;
				distR = Math.PI*W_RADIUS*(nowTachoR - lastTachoR)/180;
				
				// save current tacho counts for next iteration
				lastTachoL = nowTachoL;
				lastTachoR = nowTachoR;
				
				// compute vehicle displacement
				deltaD = 0.5*(distL + distR);
				
				// compute change in heading
				deltaT = (distL - distR)/W_BASE;
				
				// update heading
				theta += deltaT;
				
				// compute x and y components of displacement
				dX = deltaD * Math.sin(theta);						
				dY = deltaD * Math.cos(theta);		
				
				// update x and y
				x += dX;											
				y += dY;
				
				// bound theta
				if(Math.abs(theta) >= 2*Math.PI){
					theta = (double)Math.round(theta%(2*Math.PI)*100d)/100d;
				}
				
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}
