package de.tum.in.ase.eist.gameview;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

import de.tum.in.ase.eist.BumpersApplication;

/**
 *
 * This class visualizes the tool bar with start, stop and exit buttons above
 * the game board.
 *
 */
public class Toolbar extends ToolBar {
    private BumpersApplication gameWindow;
    private Button start;
    private Button stop;

    public Toolbar(BumpersApplication gameWindow) {
        this.start = new Button("Start");
        this.stop = new Button("Stop");
        initActions();
        this.getItems().addAll(start, new Separator(), stop);
        this.setGameWindow(gameWindow);
    }

    /**
     * Initialises the actions
     */
    private void initActions() {
        this.start.setOnAction(event -> getGameWindow().gameBoardUI.startGame());

        this.stop.setOnAction(event -> {
            Toolbar.this.getGameWindow().gameBoardUI.stopGame();

            ButtonType YES = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType NO = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game ?", YES, NO);
            alert.setTitle("Stop Game Confirmation");
            alert.setHeaderText("");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == YES) {
                getGameWindow().gameBoardUI.gameSetup();
            } else {
                getGameWindow().gameBoardUI.startGame();
            }
        });
    }

    /**
     * Resets the toolbar button status
     * @param running Used to disable/enable buttons
     */
    public void resetToolBarButtonStatus(boolean running) {
        this.start.setDisable(running);
        this.stop.setDisable(!running);
    }

    /**
     * @return current gameWindow
     */
    public BumpersApplication getGameWindow() {
        return this.gameWindow;
    }

    /**
     * @param gameWindow New gameWindow to be set
     */
    public void setGameWindow(BumpersApplication gameWindow) {
        this.gameWindow = gameWindow;
    }
}