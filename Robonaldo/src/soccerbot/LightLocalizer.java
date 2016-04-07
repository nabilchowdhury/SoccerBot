package soccerbot;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class LightLocalizer{
	private Odometer odo;
	private Navigation navigate;
	
	private LSPoller leftCS;
	private LSPoller rightCS;
	
	private int startingCorner;
	
	
	public LightLocalizer(Odometer odo, Navigation navigate, LSPoller leftCS, LSPoller rightCS, int startingCorner) {
		this.odo = odo;
		this.navigate = navigate;
		this.leftCS = leftCS;
		this.rightCS = rightCS;
		this.startingCorner = startingCorner;
		
	}
	
	// Initiate light localization
	public void localize() {
		navigate.goStraight(300,300, -23);
		
		preventTwitch();
		
		correctAngle();
		
		/*
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}*/
		
		
		
		preventTwitch();
		
		navigate.goStraight(200, 200, 4.5);
		
		preventTwitch();
		
		navigate.setSpeeds(200, 200, false, 2000);
		navigate.turnTo(Math.PI/2);
		
		preventTwitch();
		
		navigate.goStraight(200, 200, -8);
		
		preventTwitch();
		
		correctAngle();
		
		/*
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}*/
		
		preventTwitch();
		
		navigate.goStraight(220, 220, 11.3);
		
		preventTwitch();
		
		navigate.setSpeeds(-180, 180, true, 6000);
		
		int count = 0;
		
		while(true){
			if(count == 2){
				navigate.stopMotors();
				break;
			}
			if(leftCS.getDifferentialData() > 0.13){
				count++;
				Sound.beep();
			}
		}
		
		
		navigate.setSpeeds(150, 150, false, 6000);
		
		navigate.turnTo(Math.toRadians(-14.4));
		
		preventTwitch();
		
		navigate.goStraight(150, 150, 7.95);	
		
		preventTwitch();
		

		// fix for actual competition
		if(this.startingCorner == 1){
			double[] position = {0.0, 0.0, 0.0};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 2){
			double[] position = {6*30.45, 0.0, 3*Math.PI/2};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 3){
			double[] position = {6*30.45, 6*30.45, Math.PI};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}else if(this.startingCorner == 4){
			double[] position = {0.0, 6*30.45, Math.PI/2};
			boolean[] update = {true, true, true};
			odo.setPosition(position, update);
		}
		
		Sound.beep();
		
	}
	
	// NEW METHOD
	// uses both light sensors to correct angle with grid lines. Extremely useful
	public void correctAngle(){
		navigate.setSpeeds(200, 200, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				Sound.beep();
				break;
			}
		}
		
		navigate.setSpeeds(-100, -100, true, 6000);
		while(true){	
			if(leftCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(0, -navigate.getRMotor().getSpeed(), true, 3000);
			}
			if(rightCS.getDifferentialData() > 0.13){
				navigate.setSpeeds(-navigate.getLMotor().getSpeed(), 0, true, 3000);
			}
			if(navigate.getRMotor().getSpeed() == 0 && navigate.getLMotor().getSpeed() == 0){
				navigate.stopMotors();
				break;
			}
		}
	}
	
	public void preventTwitch(){
		navigate.stopMotors();
		try{
			Thread.sleep(50);
		}catch(Exception e){}
	}
	
	
}
