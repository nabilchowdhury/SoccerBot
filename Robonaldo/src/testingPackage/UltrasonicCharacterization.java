package testingPackage;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Timer;
import lejos.utility.TimerListener;


// A simple demonstration of data acquisition with the EV3
// using the remote console utility to collect data.

public class UltrasonicCharacterization implements TimerListener{
	
// Class Constants
	
	public static final int SINTERVAL=50;		// Sampling interval (mS)
	public static final int NSAMPLES=100;		// Number of samples to acquire
	public static final int TIMEOUT=40000;		// Fail if no comms by this time (mS)
	public static final int FWDSPEED=80;		// Forward motion speed (deg/sec)
	public static final int SLEEPINT=500;		// Main thread sleeps for 500 (mS)
	

	
	public static int numSamples;
	public static int currentSample;
		static TextLCD t = LocalEV3.get().getTextLCD();
		private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
		private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

		static Port portUS = LocalEV3.get().getPort("S1");
		
		
		SensorModes usSensor = new EV3UltrasonicSensor(portUS);
		SampleProvider usValue = usSensor.getMode("Distance");			// colorValue provides samples from this instance
		float[] usData = new float[usValue.sampleSize()];		
		UltrasonicCharacterizationOdometer odo = new UltrasonicCharacterizationOdometer(leftMotor, rightMotor,20, false); 

		
		public static void main(String[] args) throws InterruptedException {
			
			boolean rolling = true;			// cart moves while true
			int status;
			numSamples=0;
			t.clear();
			t.drawString("Data Acquisition Demo",0,0,false);
			t.drawString("Remote stream...",0,2,false);
			t.drawString("# Samples ",0,4,false);
			t.drawString("Last Val. ",0,5,false);
			

			Timer myTimer = new Timer(SINTERVAL,new UltrasonicCharacterization());
			
		
			myTimer.start();
			leftMotor.setSpeed(FWDSPEED);
			rightMotor.setSpeed(FWDSPEED);
			leftMotor.rotate(129,false);
			rightMotor.rotate(-129,false);

			while(rolling) {
				status=Button.readButtons();
				if ((status==Button.ID_ENTER)||(numSamples>=NSAMPLES)) {
					System.exit(0);
				}
				t.drawInt(numSamples,4,11,4);		// show current count
				t.drawInt(currentSample,4,11,5);	// and current value read
				Thread.sleep(SLEEPINT);				// sleep 'till next cycle
			}
			

		}



	@Override
	public void timedOut() {



		usSensor.fetchSample(usData, 0);
		float distance = usData[0]*100;
		numSamples++;
		System.out.println(odo.getAng() + ", " + distance);		
	}

}
