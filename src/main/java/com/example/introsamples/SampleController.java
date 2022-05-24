package com.example.introsamples;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class SampleController {
    @FXML
    private Canvas gameCanvas;
    @FXML
    private AnchorPane gamePane;

    private int numberPressed = 0;

    private boolean moveUP, moveDOWN, moveLEFT, moveRIGHT = false;

    private double mousePosX;
    private double mousePosY;

    private final String FILEPATH="C:\\Users\\trunt\\IdeaProjects\\IntroSamples\\src\\main\\resources\\com\\example\\introsamples\\";

    /* TODO:
        [X] Demonstrate drawing on the canvas
        [X] Demonstrate adding UI elements
        [X] Demonstrate creating an action event handler
        [X] Demonstrate updating a UI Element
        [X] Demonstrate adding a mouse event handler
        [X] Demonstrate grid selection code
        [X] Demonstrate adding on keyboard event handler
        [X] Demonstrate moving an object around the scene
     */
    
    public void setup() {
        System.out.println("Startin'");

        //********************"DRAWING" ON THE CANVAS*************************

        // GraphicContext is an Object that allows us to "draw" on the canvas
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        // Change the color of the drawing
        gc.setFill(Color.LIGHTBLUE);
        // Draw a rectangle (there are also .strokeLine, .fillOval, .strokeRect, etc etc.)
        gc.fillRect(450 - 50, 300 - 50, 100, 100);

        // GC can also draw images on the Canvas (useful for backgrounds, and other static visual elements)
        gc.drawImage(new Image(FILEPATH + "Continuo_tile.png"),400, 100, 100, 100);

        // All the methods and other information about GraphicalContext can be found at:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html


        //********************ADDING NODES TO THE SCENE*************************

        // There are numerous NODES that come prepackaged with JavaFX
        // Some of the most basic but convenient nodes is LABEL, which could be used to
        // display information on the screen
        Label sampleLabel = new Label("Hi!");

        // Using .setLayoutX or .setLayoutY on any node (Label, Button, Canvas, etc etc) allows you to change
        // its position on the screen
        sampleLabel.setLayoutX(600);
        sampleLabel.setLayoutY(375);

        // In order to actually see the created node, it needs to be added to the "container" of the scene,
        // which is a Pane called gamePane in this case
        gamePane.getChildren().add(sampleLabel);

        // Further Information about the Label object:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Label.html


        // Another versatile node is a BUTTON, which can be made to call methods called EventHandlers on a press
        Button buttn = new Button();

        // Note: All nodes have access to the .setLayout methods
        buttn.setLayoutX(600);
        buttn.setLayoutY(275);

        buttn.setText("Button!");

        // The button by itself is useless, however, using EVENT HANDLERS, we can make it execute code when it is pressed
        // The code below is how you create an event handler
        EventHandler<ActionEvent> buttonPressed = new EventHandler<ActionEvent>() {

            // The HANDLE method is what is called when the button is pressed
            @Override
            public void handle(ActionEvent actionEvent) {
                // All the code that needs to be executed upon a button press goes here //
                sampleLabel.setText("Button Pressed!\t Times Pressed: " + numberPressed);
                numberPressed++;
            }
        };

        // After the Event Handler is created it needs to be added to a button
        // NOTE: if there were multiple buttons, they could have the same EventHandler if you want to reuse it
        buttn.setOnAction(buttonPressed);

        // After the button is set up, it should be added to the scene, same as the Label
        gamePane.getChildren().add(buttn);

        // Further Information about the Button object:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Button.html


        // Another useful Node is IMAGEVIEW, which can be used to create images that can be moved around the screen
        // Unlike the canvas which is primarily used for static images, the ImageView is best used for dynamic images

        // First an image needs to be loaded in. This is done by creating a new Image object, with the
        // string filepath to the image as the parameter
        Image playerIcon = new Image(FILEPATH + "baseline_keyboard_capslock_black_24dp.png");
        // Then the ImageView object is created using that image
        ImageView playerView = new ImageView(playerIcon);
        playerView.setLayoutX(400);
        playerView.setLayoutY(500);
        playerView.setFitWidth(playerIcon.getWidth());
        playerView.setFitHeight(playerIcon.getHeight());

        gamePane.getChildren().add(playerView);

        // Further Information about the ImageVIew object:
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html


        //*******GRID SELECTION*******//

        // 2D array that represents the grid/gameboard
        // changing the dimensions of the array, allows you to
        // change the # of rows and columns in the grid
        char [][] gridArr = new char[3][3];

        // fill the array with empty spaces
        for(int i = 0; i<gridArr.length; i++) {
            for(int j = 0; j<gridArr[0].length; j++) {
                gridArr[i][j] =' ';
            }
        }

        int colCount = gridArr[0].length;
        int rowCount = gridArr.length;

        // the offset variables control the position of the grid on the canvas
        double xOffset = 100;
        double yOffset = 200;
        // the size variables control the size of the grid
        double xSize = 200;
        double ySize = 200;

        // calculates the sizes of individual cells in the grid
        double widthScale = xSize / colCount;
        double heightScale = ySize / rowCount;

        // draws the actual grid
        drawGrid(rowCount, colCount, xOffset, yOffset, widthScale, heightScale);

        EventHandler<MouseEvent> mouseGridSelection = new EventHandler<MouseEvent>() {
            private boolean isCrossTurn = true;

            @Override
            public void handle(MouseEvent mouse) {
                // determines if the user clicked within the grid
                // mouse.getX() -> returns the x position of the mouse
                // mouse.getY() -> returns the y position of the mouse
                if ((mouse.getX() < xOffset + xSize && mouse.getX() > xOffset) && (mouse.getY() < yOffset + ySize && mouse.getY() > yOffset)) {
                    // determines what cell the mouse is clicking on
                    double mouseCellX = ((mouse.getX() - xOffset) / widthScale - ((mouse.getX()-xOffset) / widthScale) % 1);
                    double mouseCellY = ((mouse.getY() - yOffset) / heightScale - ((mouse.getY() - yOffset) / heightScale) % 1);

                    // NOTE:
                    // row = y
                    // col = x
                    System.out.println("Row: " + mouseCellY + "Col: " + mouseCellX);

                    if (isCrossTurn ) {
                        gc.drawImage(new Image(FILEPATH + "red-x-png-4.png"), mouseCellX * widthScale + xOffset, mouseCellY * heightScale + yOffset, widthScale, heightScale);
                        isCrossTurn = false;
                        gridArr[(int) mouseCellY][(int) mouseCellX] = 'X';
                    } else  {
                        gc.drawImage(new Image(FILEPATH + "Letter-O-PNG-High-Quality-Image.png"), mouseCellX * widthScale + xOffset, mouseCellY * heightScale + yOffset, widthScale, heightScale);
                        isCrossTurn = true;
                        gridArr[(int) mouseCellY][(int) mouseCellX] = 'O';
                    }

                    printArray(gridArr);
                }
            }
        };

        gamePane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseGridSelection);


        //**********ADVANCED TECHNIQUES**********//

        // Mouse Movement
/*
        // Debug Mouse Coordinates
        Label mouseCords = new Label("");
        gamePane.getChildren().add(mouseCords);

        DecimalFormat cordFormat = new DecimalFormat("0.00");

        // Debug Lines
        Line diagonalLine = new Line();
        Line horizontalLine = new Line();
        Line verticalLine = new Line();

        gamePane.getChildren().add(diagonalLine);
        gamePane.getChildren().add(horizontalLine);
        gamePane.getChildren().add(verticalLine);


        final double[] theta = {0};
        gamePane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                mousePosX = mouseEvent.getX();
                mousePosY = mouseEvent.getY();

                //System.out.println("Mouse Moved");

                mouseCords.setText(cordFormat.format(mouseEvent.getX()) + "\t" + cordFormat.format(mouseEvent.getY()));
                mouseCords.setLayoutX(mouseEvent.getX()-20);
                mouseCords.setLayoutY(mouseEvent.getY()-20);

                System.out.println();
                // angle testing

                updateAngleLinesDEBUG(diagonalLine, verticalLine, horizontalLine, playerView);


                //playerView.setLayoutX(mouseEvent.getX()+20);
                //playerView.setLayoutY(mouseEvent.getY()+20);
                double playerCenterX = playerView.getLayoutX() + playerView.getFitWidth()/2.0;
                double playerCenterY = playerView.getLayoutY() + playerView.getFitHeight()/2.0;

                double horizontalComponent = mouseEvent.getX() - playerCenterX;
                double verticalComponent = playerCenterY - mouseEvent.getY();

                theta[0] = Math.toDegrees(Math.atan(verticalComponent / horizontalComponent));
                if(horizontalComponent < 0) {
                    playerView.setRotate(270 - theta[0]);

                }
                else
                    playerView.setRotate(90 - theta[0]);
                System.out.println(theta[0]);
            }
        });

        final boolean[] mouseDown = {false};

        gamePane.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseDown[0] = true;
            }
        });
        gamePane.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseDown[0] = false;
            }
        });

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(mouseDown[0]) {
                    playerView.setLayoutX(playerView.getLayoutX() + (mousePosX - (playerView.getLayoutX()+playerView.getFitWidth()/2)) / 30);
                    playerView.setLayoutY(playerView.getLayoutY() + (mousePosY - (playerView.getLayoutY()+playerView.getFitHeight()/2)) / 30);
                    updateAngleLinesDEBUG(diagonalLine, verticalLine, horizontalLine, playerView);

                    //playerView.setLayoutX(playerView.getLayoutX());
                }
            }
        };

        gameLoop.start();
*/


        // setting up keyboard character controls
        // Continous movement
        /*
        EventHandler<KeyEvent> movementKeyDown = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyPressed) {
                System.out.println("pressin'" + keyPressed.getCharacter());

                // has to use keyPressed.getCharacter() instead of .getCode() because it is a typed event for some reason
                String pressedCharacter = keyPressed.getCharacter();

                switch (keyPressed.getCharacter()) {
                    case "w" -> {
                        moveUP = true;
                    }
                    case "a" -> {
                        moveLEFT = true;
                    }
                    case "s" -> {
                        moveDOWN = true;
                    }
                    case "d" -> {
                        moveRIGHT = true;
                    }
                }
            }
        };
        gamePane.addEventHandler(KeyEvent.KEY_TYPED, movementKeyDown);

        EventHandler<KeyEvent> movementKeyReleased = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyPressed) {
                System.out.println("releasin'" + keyPressed.getCode());


                switch (keyPressed.getCode()) {
                    case W -> {
                        moveUP = false;
                    }
                    case A -> {
                        moveLEFT = false;
                    }
                    case S -> {
                        moveDOWN = false;
                    }
                    case D -> {
                        moveRIGHT = false;
                    }
                }
            }
        };
        gamePane.addEventHandler(KeyEvent.KEY_RELEASED, movementKeyReleased);

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                double horizontalComponent = 0;
                double verticalComponent = 0;

                if(moveUP)
                    verticalComponent-=2;
                if(moveDOWN)
                    verticalComponent+=2;
                if(moveLEFT)
                    horizontalComponent-=2;
                if(moveRIGHT)
                    horizontalComponent+=2;

                playerView.setLayoutX(playerView.getLayoutX() + horizontalComponent);
                playerView.setLayoutY(playerView.getLayoutY() + verticalComponent);
            }
        };
        gameLoop.start();
*/

        // Non Continous Movement
        /*
        EventHandler<KeyEvent> movementKeyDown = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyPressed) {
                System.out.println("pressin'" + keyPressed.getCharacter());

                // has to use keyPressed.getCharacter() instead of .getCode() because it is a typed event for some reason
                String pressedCharacter = keyPressed.getCharacter();

                switch (keyPressed.getCharacter()) {
                    case "w" -> {
                        playerView.setLayoutY(playerView.getLayoutY() - 50);
                    }
                    case "a" -> {
                        playerView.setLayoutX(playerView.getLayoutX() - 50);
                    }
                    case "s" -> {
                        playerView.setLayoutY(playerView.getLayoutY() + 50);
                    }
                    case "d" -> {
                        playerView.setLayoutX(playerView.getLayoutX() + 50);
                    }
                }
            }
        };
        gamePane.addEventHandler(KeyEvent.KEY_TYPED, movementKeyDown);

        drawLevel(new int[][]{}, 400, 350, 200,400);
        drawGrid(4,8,400,350,50,50);
        */
    }

    private void drawGrid (int rows, int cols, double xOffset, double yOffset, double widthScale, double heightScale) {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        for(int i = 0; i < rows + 1; i++) {
            for (int j = 0; j < cols + 1; j++) {
                gc.strokeLine(widthScale * j + xOffset, yOffset, widthScale * j + xOffset, heightScale*rows + yOffset);
                gc.strokeLine(xOffset, heightScale * i + yOffset, widthScale * cols + xOffset, heightScale * i + yOffset);

            }
        }
    }

    private void printArray(char[][] grid) {
        for(char[] row : grid) {
            for(char tile : row) {
                System.out.print(tile+" ");
            }
            System.out.println();
        }
    }

    private void drawLevel(int [][] levelGrid, double xOffset, double yOffset, double width, double height) {
        int [][] levelTest = new int[][]{
                {1,1,2,1,2,1,1,2},
                {2,1,1,2,1,1,1,2},
                {1,2,2,1,2,1,1,1},
                {1,1,2,1,2,1,1,2}
        };

        int rowCount = levelTest.length;
        int colCount = levelTest[0].length;

        double widthScale = width / rowCount;
        double heightScale = height / colCount;

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        for (int i = 0; i < levelTest.length; i++) {
            for (int j = 0; j < levelTest[0].length; j++) {
                if(levelTest[i][j] == 1)
                    gc.drawImage(new Image("C:\\Users\\trunt\\IdeaProjects\\IntroSamples\\src\\main\\resources\\com\\example\\introsamples\\floor_1.png"),widthScale * j + xOffset, heightScale*i + yOffset, widthScale, heightScale);
                if(levelTest[i][j] == 2)
                    gc.drawImage(new Image("C:\\Users\\trunt\\IdeaProjects\\IntroSamples\\src\\main\\resources\\com\\example\\introsamples\\floor_2.png"),widthScale * j + xOffset, heightScale*i + yOffset, widthScale, heightScale);

            }
        }
    }

    private void updateAngleLinesDEBUG(Line diagonalLine, Line verticalLine, Line horizontalLine, ImageView playerView) {
        diagonalLine.setStartX(playerView.getLayoutX() + playerView.getFitWidth()/2);
        diagonalLine.setStartY(playerView.getLayoutY() + playerView.getFitHeight()/2);
        diagonalLine.setEndX(mousePosX);
        diagonalLine.setEndY(mousePosY);

        horizontalLine.setStartX(playerView.getLayoutX() + playerView.getFitWidth()/2);
        horizontalLine.setStartY(playerView.getLayoutY() + playerView.getFitHeight()/2);
        horizontalLine.setEndY(playerView.getLayoutY() + playerView.getFitHeight()/2);
        horizontalLine.setEndX(mousePosX);

        verticalLine.setStartX(horizontalLine.getEndX());
        verticalLine.setStartY(horizontalLine.getEndY());
        verticalLine.setEndY(diagonalLine.getEndY());
        verticalLine.setEndX(diagonalLine.getEndX());
    }
}