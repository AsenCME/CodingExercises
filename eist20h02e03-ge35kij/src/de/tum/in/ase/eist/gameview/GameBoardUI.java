package de.tum.in.ase.eist.gameview;


import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.audio.AudioPlayer;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.usercontrol.MouseSteering;

/**
 * This class implements the user interface for steering the player car. The
 * user interface is implemented as a Thread that is started by clicking the
 * start button on the tool bar and stops by the stop button.
 *
 */
public class GameBoardUI extends Canvas implements Runnable {
	private static final Color backgroundColor = Color.WHITE;
	private static final int SLEEP_TIME = 1000 / 25; // this gives us 25fps
	private static final Dimension2D DEFAULT_SIZE = new Dimension2D(500, 300);
	// attribute inherited by the JavaFX Canvas class
	private GraphicsContext graphicsContext = this.getGraphicsContext2D();

	// thread responsible for starting game
	private Thread theThread;

	// user interface objects
	private GameBoard gameBoard;
	private Dimension2D size;
	private Toolbar toolBar;
	
	// user control objects
	private MouseSteering mouseSteering;

	private HashMap<Car, Image> carImages;

	/**
	 * Sets up all attributes, starts the mouse steering and sets up all graphics
	 * @param toolBar used to start and stop the game
	 */
	public GameBoardUI(Toolbar toolBar) {
		this.toolBar = toolBar;
		this.size = getPreferredSize();
		gameSetup();
	}

	/**
	 * Called after starting the game thread
	 * Constantly updates the game board and renders graphics
	 * @see Runnable#run()
	 */
	@Override
	public void run() {
		while (this.gameBoard.isRunning()) {
			// updates car positions and re-renders graphics
			this.gameBoard.update();
			// when this.gameBoard.hasWon() is null, do nothing
			if (this.gameBoard.hasWon() == Boolean.FALSE) {
				showAsyncAlert("Oh.. you lost.");
				this.stopGame();
			}
			else if (this.gameBoard.hasWon() == Boolean.TRUE) {
				showAsyncAlert("Congratulations! You won!!");
				this.stopGame();
			}
			paint(this.graphicsContext);
			try {
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return current gameBoard
	 */
	public GameBoard getGameBoard() {
		return this.gameBoard;
	}
	
	/**
	 * 
	 * @return mouse steering control object
	 */
	public MouseSteering getMouseSteering() {
		return this.mouseSteering;
	}

	/**
	 * @return preferred gameBoard size
	 */
	public static Dimension2D getPreferredSize() {
		return DEFAULT_SIZE;
	}

	/**
	 * Removes all existing cars from the game board and re-adds them. Status bar is
	 * set to default value. Player car is reset to default starting position.
	 * Renders graphics.
	 */
	public void gameSetup() {
		this.gameBoard = new GameBoard(this.size);
		this.gameBoard.setAudioPlayer(new AudioPlayer());
		this.widthProperty().set(this.size.getWidth());
		this.heightProperty().set(this.size.getHeight());
		this.size = new Dimension2D(getWidth(), getHeight());
		this.carImages = new HashMap<>();
		this.mouseSteering = new MouseSteering(this, this.gameBoard.getPlayerCar());
		this.gameBoard.resetCars();
		this.gameBoard.getCars().forEach((car -> this.carImages.put(car, getImage(car.getIconLocation()))));
		this.carImages.put(this.gameBoard.getPlayerCar(), this.getImage(this.gameBoard.getPlayerCar().getIconLocation()));
		paint(this.graphicsContext);
		this.toolBar.resetToolBarButtonStatus(false);
	}

    /**
     * Sets the car's image
     *
     * @param carImageFilePath: an image file path that needs to be available in the resources folder of the project
     */
    private Image getImage(String carImageFilePath) {
        try {
            URL carImageUrl = getClass().getClassLoader().getResource(carImageFilePath);
            if(carImageUrl == null) {
                throw new RuntimeException("Please ensure that your resources folder contains the appropriate files for this exercise.");
            }
            InputStream inputStream = carImageUrl.openStream();
            return new Image(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
	 * which causes the cars to change their positions (i.e. move). Renders graphics
	 * and updates tool bar status.
	 */
	public void startGame() {
		if (!this.gameBoard.isRunning()) {
			this.gameBoard.startGame();
			this.theThread = new Thread(this);
			this.theThread.start();
			paint(this.graphicsContext);
			this.toolBar.resetToolBarButtonStatus(true);
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the cars of the
	 * game board at render each of them individually.
	 * @param graphics used to draw changes
	 */
	private void paint(GraphicsContext graphics) {
		graphics.setFill(backgroundColor);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		for (Car car : this.gameBoard.getCars()) {
			paintCar(car, graphics);
		}
		// render player car
		paintCar(this.gameBoard.getPlayerCar(), graphics);
	}

	/**
	 * Show image of a car at the current position of the car.
	 * @param car to be drawn
	 * @param graphics used to draw changes
	 */
	private void paintCar(Car car, GraphicsContext graphics) {
		Point2D carPosition = car.getPosition();
		Point2D canvasPosition = convertPosition(carPosition);

		graphics.drawImage(this.carImages.get(car), canvasPosition.getX(), canvasPosition.getY(),
				car.getSize().getWidth(), car.getSize().getHeight());
	}

	/**
	 * Converts position of car to position on the canvas
	 * @param toConvert the point to be converted
	 */
	public Point2D convertPosition(Point2D toConvert) {
		return new Point2D(toConvert.getX(), getHeight() - toConvert.getY());
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame() {
		if (this.gameBoard.isRunning()) {
			this.gameBoard.stopGame();
			this.toolBar.resetToolBarButtonStatus(false);
		}
	}

    /**
     * Method used to display alerts in moveCars() Java 8 Lambda Functions: java
     * 8 lambda function without arguments Platform.runLater Function:
     * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html
     *
     * @param message
     *            you want to display as a String
     */
    public void showAsyncAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(message);
            alert.showAndWait();
            this.gameSetup();
        });
    }
}
