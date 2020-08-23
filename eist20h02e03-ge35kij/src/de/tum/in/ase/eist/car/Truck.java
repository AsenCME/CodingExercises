package de.tum.in.ase.eist.car;

import java.util.Random;

import de.tum.in.ase.eist.Point2D;

public class Truck extends Car {

	public static String DEFAULT_TRUCK_IMAGE_FILE = "truck.png";
	private long timer = 5;
	private int lives = 3;
	private int maxX, maxY;

	/**
	 * Constructor for a Truck
	 * 
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 */
	public Truck(int maxX, int maxY) {
		super(maxX, maxY);
		this.maxX = maxX;
		this.maxY = maxY;
		this.MIN_SPEED = 2;
		this.MAX_SPEED = 5;
		this.setRandomSpeed();
		this.setIcon(DEFAULT_TRUCK_IMAGE_FILE);
	}

	public void getHit() {
		this.lives--;
		teleport();
	}

	public int getLives() {
		return lives;
	};

	void teleport() {
		Random rnd = new Random();
		this.setDirection(rnd.nextInt(360));
		this.position = new Point2D(rnd.nextInt(this.maxX), rnd.nextInt(this.maxY));
		this.timer = rnd.nextInt(9) + 1;
	}

	@Override
	public void updatePosition(int maxX, int maxY) {
		if ((System.currentTimeMillis() / 1000) % timer == 0)
			this.teleport();
		else
			super.updatePosition(maxX, maxY);
	}
}
