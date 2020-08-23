package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.ase.eist.audio.AudioPlayerInterface;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.car.FastCar;
import de.tum.in.ase.eist.car.SlowCar;
import de.tum.in.ase.eist.car.Truck;
import de.tum.in.ase.eist.collision.Collision;
import de.tum.in.ase.eist.collision.TruckCollision;

/**
 * Creates all car objects, detects collisions, updates car positions, notifies
 * player about victory or defeat
 *
 */
public class GameBoard {

	// list of all active cars, does not contain player car
	private List<Car> cars = new ArrayList<>();

	// the player object with player car object
	private Player player;

	private AudioPlayerInterface audioPlayer;

	private Dimension2D size;

	// list of all loser cars (needed for testing, DO NOT DELETE THIS)
	private List<Car> loserCars = new ArrayList<>();

	// true if game is running, false if game is stopped
	private boolean isRunning;

	private Boolean gameWon;

	// constants
	public static int NUMBER_OF_SLOW_CARS = 2;
	public static int NUMBER_OF_TESLA_CARS = 2;
	public static int NUMBER_OF_TRUCKS = 1;

	/**
	 * Constructor, creates the gameboard based on size
	 * 
	 * @param size of the gameboard
	 */
	public GameBoard(Dimension2D size) {
		Car playerCar = new FastCar(250, 30);
		this.player = new Player(playerCar);
		this.size = size;
		this.addCars();
	}

	/**
	 * Adds specified number of cars to the cars list, creates new object for each
	 * car
	 */
	public void addCars() {
		for (int i = 0; i < NUMBER_OF_SLOW_CARS; i++) {
			this.cars.add(new SlowCar(this.size.getWidth(), this.size.getHeight()));
		}
		for (int i = 0; i < NUMBER_OF_TESLA_CARS; i++) {
			this.cars.add(new FastCar(this.size.getWidth(), this.size.getHeight()));
		}
		for (int i = 0; i < NUMBER_OF_TRUCKS; i++) {
			this.cars.add(new Truck(this.size.getWidth(), this.size.getHeight()));
		}
	}

	/**
	 * Removes all existing cars from the car list, resets the position of the
	 * player car Invokes the creation of new car objects by calling addCars()
	 */
	public void resetCars() {
		this.player.getCar().reset(this.size.getHeight());
		this.cars.clear();
		addCars();
	}

	/**
	 * Checks if game is currently running by checking if the thread is running
	 * 
	 * @return boolean isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Used for testing only
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * 
	 * @return null if the game is running; true if the player has won; false if the
	 *         player has lost
	 */
	public Boolean hasWon() {
		return this.gameWon;
	}

	/**
	 * @return list of cars
	 */
	public List<Car> getCars() {
		return this.cars;
	}

	/**
	 * @return the player's car
	 */
	public Car getPlayerCar() {
		return this.player.getCar();
	}

	/**
	 * @return the gameboard's instance of AudioPlayer
	 */
	public AudioPlayerInterface getAudioPlayer() {
		return this.audioPlayer;
	}

	public void setAudioPlayer(AudioPlayerInterface audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * Updates the position of each car
	 */
	public void update() {
		moveCars();
	}

	/**
	 * Starts the game. Cars start to move and background music starts to play.
	 */
	public void startGame() {
		playMusic();
		this.isRunning = true;
	}

	/**
	 * Stops the game. Cars stop moving and background music stops playing.
	 */
	public void stopGame() {
		stopMusic();
		this.isRunning = false;
	}

	/**
	 * Starts the background music
	 */
	public void playMusic() {
		this.audioPlayer.playBackgroundMusic();
	}

	/**
	 * Stops the background music
	 */
	public void stopMusic() {
		this.audioPlayer.stopBackgroundMusic();
	}

	/**
	 * @return list of loser cars
	 */
	public List<Car> getLoserCars() {
		return this.loserCars;
	}

	/**
	 * Iterate through list of cars (without the player car) and update each car's
	 * position Update player car afterwards separately
	 */
	public void moveCars() {

		List<Car> cars = getCars();

		// maximum x and y values a car can have depending on the size of the game board
		int maxX = (int) size.getWidth();
		int maxY = (int) size.getHeight();

		// update the positions of the player car and the autonomous cars
		for (Car car : cars) {
			car.updatePosition(maxX, maxY);
		}

		player.getCar().updatePosition(maxX, maxY);

		// iterate through all cars (except player car) and check if it is crunched
		for (Car car : cars) {
			if (car.isCrunched()) {
				continue; // because there is no need to check for a collision
			}

			Collision collision = new TruckCollision(player.getCar(), car);

			if (collision.isCollision) {
				Car winner = collision.evaluate();
				Car loser = collision.evaluateLoser();
				System.out.println(winner);
				System.out.println(loser);
				audioPlayer.playCrashSound();

				if (loser != null) {
					loserCars.add(loser);
					loser.MIN_SPEED = 0;
					loser.MAX_SPEED = 0;
					loser.setCrunched();
				}

				if (loser != null && loser.equals(player.getCar())) {
					stopGame();
				}

				if (isWinner()) {
					gameWon = true;
					stopGame();
				}

			}
		}
	}

	/**
	 * If all cars are crunched, the player wins.
	 * 
	 * @return true if game is won
	 * @return false if game is not (yet) won
	 */
	private boolean isWinner() {
		for (Car car : getCars()) {
			if (!car.isCrunched()) {
				return false;
			}
		}
		return true;
	}
}
