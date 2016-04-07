package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * TODO
 */

//NOTE: Class is still under construction, but it will have an attack(int x, int y) method. May have more methods in the future
public class Attacker {
	private Navigation navigate;
	
	private EV3LargeRegulatedMotor loadMotor;
	private EV3LargeRegulatedMotor launchMotor;
	
	public Attacker(Navigation navigate, EV3LargeRegulatedMotor loadMotor, EV3LargeRegulatedMotor launchMotor){
		this.navigate = navigate;
		
		this.loadMotor = loadMotor;
		this.launchMotor = launchMotor;
	}
	
	public void attack(){
		loadMotor.setSpeed(300);
		loadMotor.rotate(180, false);
		
		loadMotor.setSpeed(50);
		loadMotor.rotate(-125, false);
		
		try{Thread.sleep(200);}catch(Exception e){}
		
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
