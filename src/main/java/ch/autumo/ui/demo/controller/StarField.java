/**
 * Copyright (c) 2026 autumo Ltd. Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.autumo.ui.demo.controller;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


/**
 * The star field.
 */
public class StarField {

	/** Star-field depth. */
	public static final int DEPTH = 1000;
	/** Star-field width. */
	public static final int WIDTH = 1080;
	/** Star-field height. */
	public static final int HEIGHT = 566;
	/** Initial star brightness. */
	public static final double INITIAL_BRIGHTNESS = 0.3;
	/** Star size end radius. */
	private static final double STAR_SIZE = 2;
	/** Amount of stars. */
	private static final int AMOUNT_OF_STARS = 750;
	/** Star colors. */
	private static final Color STAR_COLORS[] = new Color[] {
			Color.ALICEBLUE, 
			Color.BLUEVIOLET, 
			Color.CADETBLUE, 
			Color.CORNSILK, 
			Color.CYAN, 
			Color.DARKCYAN,
			Color.DARKGRAY,
			Color.DARKSLATEBLUE,
			Color.DARKSLATEGREY,
			Color.LIGHTBLUE,
			Color.DARKTURQUOISE,
			Color.DARKSLATEBLUE
		};
	
	/** Star-field initial speed */
	public static double speed = 1;

	private Pane starPane = null;
	
	/**
	 * Star field.
	 * 
	 * @param mainPane the main pain holding the star pane 
	 * @param starPane the star pane
	 * @param toolBarShift a toolBarShift occurs if the menu is detached 
	 * 			from the window (macOS, if detach is not disabled)
	 */
	public StarField(Pane mainPane, Pane starPane, double toolBarShift) {
	
		this.starPane = starPane;
		
		// It is necessary to clip the star-pane, otherwise stars would "fly"
		// outside the borders and this deactivates mouse listener events
		// outside the star-pane!
		final Rectangle clipPane = new Rectangle();
		this.starPane.setClip(clipPane);
		this.starPane.layoutBoundsProperty().addListener((_, _, newValue) -> {
	        clipPane.setWidth(newValue.getWidth());
	        clipPane.setHeight(newValue.getHeight() + toolBarShift); 
	    });
		
        this.createStars();
        this.animateStars();
        
        mainPane.setOnScroll(e -> {
        	final double delta = e.getDeltaY();
        	if ( (delta < 0 && speed >= -1 ) || (delta > 0 && speed <= 20 ) )
        		speed += delta * 0.1;
        });
	}

    private void createStars() {
    	for (int i = 0; i < AMOUNT_OF_STARS; i++) {
            final Star star = new Star(STAR_COLORS[(int) ((Math.random() * (STAR_COLORS.length - 1)))]); // adds itself to list
            starPane.getChildren().add(star.getBody());
        }
    }
	
	private void animateStars() {
		final Timeline update = new Timeline(new KeyFrame(Duration.millis(16), _ -> { // ca. 60 fps
            for (Star star : Star.stars) {
                star.update();
            }
        }));
        update.setCycleCount(Timeline.INDEFINITE);
        update.setRate(1);
        update.setAutoReverse(false);
        update.play();
    }

    /**
     * The Star.
     * Uses code from: https://github.com/GokselKUCUKSAHIN/JavaFx-Starfield
     */
    public static class Star {
    	
    	/** All stars. */
        public static final ArrayList<Star> stars = new ArrayList<>();

        private Circle body;
        private Color color;
        
        private double x;
        private double y;
        private double z;

        /**
         * Star.
         * @param color color of the star
         */
        public Star(Color color) {
        	
        	this.color = color;
        	
            x = Star.getRandom(-WIDTH / 2, WIDTH / 2);
            y = Star.getRandom(-HEIGHT / 2, HEIGHT / 2);
            z = Star.getRandom(DEPTH);
            double sx = map(x / z, 0, 1, 0, WIDTH);
            double sy = map(y / z, 0, 1, 0, HEIGHT);
            
            double r = Star.map(z, 0, DEPTH, STAR_SIZE, 0);
            
            double cz = z;
            if (cz < 1 || cz > DEPTH)
            	cz = DEPTH;
            double f = Star.map(cz, 0, DEPTH, 1.0, 0.3);
            double rc = f * color.getRed();
            double gc = f * color.getGreen();
            double bc = f * color.getBlue();
            Color calcColor = new Color(rc, gc, bc, 1.0);
            
            body = new Circle(sx + WIDTH / 2, sy + HEIGHT / 2, r, calcColor);
            
            stars.add(this);
        }

        /**
         * Update the star's location.
         */
        public void update() {
            z = z - speed;
            if (z < 1) {
                z = DEPTH;
                x = Star.getRandom(-WIDTH / 2, WIDTH / 2);
                y = Star.getRandom(-HEIGHT / 2, HEIGHT / 2);
            }

            double sx = Star.map(x / z, 0, 1, 0, WIDTH);
            double sy = Star.map(y / z, 0, 1, 0, HEIGHT);
            body.setCenterX(sx + WIDTH / 2);
            body.setCenterY(sy + HEIGHT / 2);
            double r = Star.map(z, 0, DEPTH, STAR_SIZE, 0);
            body.setRadius(r);

            double cz = z;
            if (cz < 1 || cz > DEPTH)
            	cz = DEPTH;
            double f = Star.map(cz, 0, DEPTH, 1, INITIAL_BRIGHTNESS);
            double rc = f * color.getRed();
            double gc = f * color.getGreen();
            double bc = f * color.getBlue();
            Color calcColor = new Color(rc, gc, bc, 1.0);
            
            body.setFill(calcColor);
        }

        /**
         * Get the star's body (the FX circle).
         * @return star body
         */
        public Node getBody() {
            return this.body;
        }

        private static double getRandom(double limit) {
            return map(Math.random(), 0, 1, 0, limit);
        }
        private static double getRandom(double min, double max) {
            return map(Math.random(), 0, 1, min, max);
        }
        private static double map(double x, double in_min, double in_max, double out_min, double out_max) {
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min; //double map
        }        
    }
    
}
