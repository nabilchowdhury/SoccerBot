package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * NOTE: Class is still under construction - may have more methods in the future.
 * 
 * Attacker is tasked with interacting with the motors responsible for loading balls and launching them
 * and to provide those functionalities
 * 
 * @author Nabil Chowdhury
 */

public class Attacker {
	private Navigation navigate;
	
	private EV3LargeRegulatedMotor loadMotor;
	private EV3LargeRegulatedMotor launchMotor;
	
	/**
	 * This constructor requires a <code>Navigation</code> object and the two <code>EV3LargeRegulatedMotor</code> objects 
	 * representing the motors responsible for the ball-loading mechanism and the launching mechanism
	 * 
	 * @param navigate The singleton <code>Navigation</code> object
	 * @param loadMotor Motor connected to the loading mechanism
	 * @param launchMotor Motor connected to the launching mechanism
	 */
	public Attacker(Navigation navigate, EV3LargeRegulatedMotor loadMotor, EV3LargeRegulatedMotor launchMotor){
		this.navigate = navigate;
		
		this.loadMotor = loadMotor;
		this.launchMotor = launchMotor;
	}
	
	/**
	 * This method is responsible for loading balls into the loader and launching them.
	 * The loadMotor must start with the loader pointing straight upwards with the launchMotor
	 * initially resting on the actual loadMotor.
	 * <p>
	 * This will start by navigating and using the launchMotor to load balls into the loader.
	 * After balls have been loaded, the launchMotor will be used to launch them one-by-one.
	 * 
	 */
	public void attack(){
		loadMotor.setSpeed(300);
		loadMotor.rotate(180, false);
		
		loadMotor.setSpeed(50);
		loadMotor.rotate(-125, false);
		
		try {Thread.sleep(200);} catch(Exception e){}
		
		for(int i=0; i<2; i++){
			navigate.preventTwitch();
			navigate.goStraight(150, 150, -8);
			
			loadMotor.setSpeed(200);
			loadMotor.rotate(135, false);
			try{Thread.sleep(200);}catch(Exception e){}
			loadMotor.setSpeed(60);
			loadMotor.rotate(-125, false);	
		}
		
				
		
		navigate.preventTwitch();
		navigate.goStraight(200, 200, 40);
		
		launchMotor.setAcceleration(6000); launchMotor.setSpeed(150);
		launchMotor.rotate(140, false);
		
		try{
			Thread.sleep(1500);
		}catch(Exception e){}
		
		navigate.preventTwitch();
		
		navigate.setSpeeds(150,150,false, 6000);
		
		navigate.turnTo(3,7);
		
		
		launchMotor.setAcceleration(20000000); launchMotor.setSpeed(600);
		launchMotor.rotate(-110, false); launchMotor.flt();
		
	}
}
