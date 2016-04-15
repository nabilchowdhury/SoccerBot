package soccerbot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Attacker is tasked with interacting with the motors responsible for loading balls and launching them
 * and to provide those functionalities
 * 
 * @author Nabil Chowdhury
 */

public class Attacker {
	private Navigation navigate;
	
	private EV3LargeRegulatedMotor loadMotor;
	private EV3LargeRegulatedMotor launchMotor;
	
	private double[] coordinates = new double[4];
	
	/**
	 * This constructor requires a <code>Navigation</code> object and the two <code>EV3LargeRegulatedMotor</code> objects 
	 * representing the motors responsible for the ball-loading mechanism and the launching mechanism. <code>x1, y1, x1, y2</code> 
	 * represent the coordinates of the tiles of the left and right side of the goal, respectively.
	 * 
	 * @param navigate The singleton <code>Navigation</code> object
	 * @param loadMotor Motor connected to the loading mechanism
	 * @param launchMotor Motor connected to the launching mechanism
	 * @param x1 X-position of tile on the left side of the goal
	 * @param y1 Y-position of tile on the left side of the goal
	 * @param x2 X-position of tile on the right side of the goal
	 * @param y2 Y-position of tile on the right side of the goal
	 */
	public Attacker(Navigation navigate, EV3LargeRegulatedMotor loadMotor, EV3LargeRegulatedMotor launchMotor, double x1, double y1, double x2, double y2){
		this.navigate = navigate;
		
		this.loadMotor = loadMotor;
		this.launchMotor = launchMotor;
		
		coordinates[0] = x1; 
		coordinates[1] = y1; 
		coordinates[2] = x2; 
		coordinates[3] = y2; 
	}
	
	/**
	 * This method is responsible for loading balls into the loader and launching them.
	 * The loadMotor must start with the loader pointing straight upwards.
	 * <p>
	 * This will start after this robot has navigated to the ball platform. Three balls will be retrieved where by afterwards this robot will navigate
	 * to the center of the neutral zone and then proceed with the launching algorithm.
	 * <p>
	 * The launching algorithm starts of by aiming towards the left innermost tile of the goal and alternates between that and the right innermost tile until all balls are fired.
	 * 
	 */
	public void attack(){
		// rotate motor
		loadMotor.setSpeed(100);
		loadMotor.rotate(150, false); 
		for(int i=0; i<3; i++){
			// land softly on first ball
			loadMotor.flt();
			try{Thread.sleep(1200);}catch(Exception e){}
			// obtain first ball
			loadMotor.setAcceleration(15000);
			loadMotor.setSpeed(500);
			loadMotor.rotate(30, false);
			try{Thread.sleep(100);}catch(Exception e){}
			loadMotor.setAcceleration(6000);
			loadMotor.resetTachoCount();
			// move cage back by 80 deg and lock it in place
			loadMotor.setSpeed(100);
			loadMotor.rotate(-80, false); loadMotor.setSpeed(0); loadMotor.forward();
			
			// move robot 10 cm forward for next ball
			if(i != 2) {
				navigate.goStraight(150, 150, -7.7);
			}
		}
		
		loadMotor.flt();
		
		navigate.goStraight(150, 150, 40);
		
		navigate.travelTo(5, 5, true, false);
		navigate.setSpeeds(200, 200, false, 6000);
		navigate.turnTo(5*30.5, 12*30.5);
		navigate.odometryCorrection(5*30.5, 5*30.5);
		
		boolean left = true;
		for(int i=0; i<4; i++){
			
			navigate.setSpeeds(150, 150, false, 6000);
			if(left){
				navigate.turnTo(coordinates[0]*30.5, coordinates[1]*30.5);
				left = !left;
			}else{
				navigate.turnTo(coordinates[2]*30.5, coordinates[3]*30.5);
				left = !left;
			}
			launchMotor.setAcceleration(6000); launchMotor.setSpeed(150);
			launchMotor.rotate(140, false);
			try{
				Thread.sleep(1500);
			}catch(Exception e){}
			
			loadMotor.setAcceleration(500);
			loadMotor.setSpeed(153);
			loadMotor.rotate(-110, false); loadMotor.setSpeed(0); loadMotor.forward();
			
			try{
				Thread.sleep(1500);
			}catch(Exception e){}
			
			launchMotor.setAcceleration(20000000); launchMotor.setSpeed(700);
			launchMotor.rotate(-110, false); launchMotor.flt(); launchMotor.resetTachoCount();
			
			loadMotor.setSpeed(60); loadMotor.rotate(110, false); loadMotor.flt(); loadMotor.resetTachoCount();
		}
		
		
		
		
		
		/*
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
		*/
	}
}
