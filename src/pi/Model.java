package pi;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * The Model class contains the logic of the Pi-Simulation. It is an Observable
 * class being observed by the View.
 * 
 * @author leu
 */
public class Model extends Observable implements Runnable {

	private boolean running;
	private List<Point> pointsWithin = new LinkedList<>();
	private List<Point> pointsOutside = new LinkedList<>();
	private static final long MAX_WAIT_TIME = 101;
	// The STARTING_SPEED_INDEX allows to synchronize the starting speed with the
	// slider start position defined in the view. It can be set to a value between 0
	// and 100
	protected static final long STARTING_SPEED_INDEX = 50;
	private long waitTime = STARTING_SPEED_INDEX;

	/**
	 * This method does the calculations to estimate pi. It is based on the ratio of
	 * the randomly generated points that are found within the circle to the total
	 * points (--> see also the class Point).
	 * 
	 * @return double value of the actual estimation of pi based on the randomly
	 *         generated points.
	 */
	public double estimatePi() {
		double numberOfPointsWithin = pointsWithin.size();
		double totalPoints = numberOfPointsWithin + pointsOutside.size();
		// estimation of pi: pi = chance of any random point being inside the circle * 4
		return (numberOfPointsWithin / totalPoints) * 4d;
	}

	public int getTotalPoints() {
		return pointsWithin.size() + pointsOutside.size();
	}

	public void setSpeed(Number speed) {
		waitTime = MAX_WAIT_TIME - speed.longValue();
	}

	/**
	 * If there is already a thread generating and attributing random points to one
	 * of two LinkedLists, this method will stop the thread. If the thread is
	 * stopped, it will, in contrary, start a new thread.
	 */
	public void startStop() {
		if (!running) {
			running = true;
			new Thread(this).start();
		} else {
			running = false;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}

	/**
	 * This method creates new lists for the points in order to be able to restart
	 * with empty lists
	 */
	public void clear() {
		pointsWithin = new LinkedList<>();
		pointsOutside = new LinkedList<>();
	}

	@Override
	public void run() {
		while (running) {
			Point p = Point.randomPoint();
			addPoint(p);
			notifyObservers(p);
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The appPoint-Methods adds a point either to the list "pointsWithin" or to the
	 * list "pointsOutside" depending if they are within the quarter circle or
	 * outside of it.
	 * 
	 * @param p
	 *            of the Type Point
	 */
	private void addPoint(Point p) {
		if (p.isWithinCircle()) {
			pointsWithin.add(p);
		} else {
			pointsOutside.add(p);
		}
		setChanged();
	}

}
