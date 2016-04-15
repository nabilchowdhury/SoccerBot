package testingPackage;
public class Navtest {
static int a;
static int x1;
static int y1;
static int x2;
static int y2;
static int x3;
static int y3;
static int x4;
static int y4;
static int x5;
static int y5;
static int distance;
	
	public static void main(String[] args) {
		boolean isLongEnough = false;
		while (isLongEnough==false){
		x1 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		y1 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		x2 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		y2 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		x3 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		y3 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		x4 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		y4 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		x5 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		y5 = (int) ((int)5*(Math.round((180*Math.random())/5)));
		distance = (int) Math.sqrt(x1*x1+y1*y1)+(int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))+(int)Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))+(int)Math.sqrt((x3-x4)*(x3-x4)+(y3-y4)*(y3-y4))+(int)Math.sqrt((x4-x5)*(x4-x5)+(y4-y5)*(y4-y5));
		if (distance>=540)
			isLongEnough = true;
		}
		System.out.println("("+x1+" , "+y1+")");
		System.out.println("("+x2+" , "+y2+")");
		System.out.println("("+x3+" , "+y3+")");
		System.out.println("("+x4+" , "+y4+")");
		System.out.println("("+x5+" , "+y5+")");
		System.out.println("distance is: "+distance);
	
		}

}
