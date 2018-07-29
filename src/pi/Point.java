package pi;

import java.util.Random;

/**
 * The class Point represents the randomly generated points and manages the
 * decision based on its coordinates, if the point is within a quarter circle
 * with radius = 1 or outside of it.
 * 
 * @author leu
 */
public class Point {

	final double x;
	final double y;
	static Random random = new Random();

	/**
	 * This method generates point with random coordinates between 0 and 1.
	 * 
	 * @return the randomly generated point
	 */
	public static Point randomPoint() {
		double x = random.nextDouble();
		double y = random.nextDouble();
		return new Point(x, y);
	};

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * The method isWithinCircle calculates based on the coordinates of the point,
	 * if this point is within a quarter circle with r=1 or outside of it
	 * 
	 * @return true if the point is within the quarter circle
	 */
	public boolean isWithinCircle() {
		// the point is within the circle when x^2 + y2 < R^2 with R=1
		return (Math.pow(x, 2) + Math.pow(y, 2) < 1);
	}

	@Override
	public String toString() {
		return "Point: x=" + x + " / y=" + y;
	}

}
