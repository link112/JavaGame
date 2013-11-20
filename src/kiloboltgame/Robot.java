package kiloboltgame;

public class Robot {
	/*Macros*/
	final int JUMPSPEED = -15;
	final int MOVESPEED = 5;
	final int GROUND = 382;
	final int RIGHTBORDER = 200;
	final int CHARWIDTH = 60;
	final int CHARHEIGHT = 62;
	
	public enum STATUS {
		JUMPED, NEUTRAL, DUCKED
	}

	/*Private variables*/
	private int centerX = 100;			//starting character position
	private int centerY = 382;
	private int speedX = 0;				//starting speeds
	private int speedY = 0;
	private STATUS status = STATUS.NEUTRAL;

	/*Background values (same instance of the one in Starting Class)*/
	private static Background bg1 = StartingClass.getBg1();
	private static Background bg2 = StartingClass.getBg2();

	/*Getters and Setters*/
	public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
    public STATUS getstatus() {
    	return status;    
    }
    
    public void setstatus(STATUS status) {
    	this.status = status;
    }

    /*updating the robot*/
	public void update() {

		// moving left or not moving
		if (speedX <= 0) { 
			// don't move background
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);
			// if at left border of screen and moving left, set character at edge
			if( centerX + speedX < CHARWIDTH) {
				centerX = CHARWIDTH + 1;
			} 
			// not at border so move left
			else {
				centerX += speedX;
			}
		} 
		// moving right
		else { 	
			// move robot if left of border
			if(centerX <= RIGHTBORDER) {
				centerX += speedX;
			} 
			// move background if at border
			else { 
	            bg1.setSpeedX(-MOVESPEED);
	            bg2.setSpeedX(-MOVESPEED);
			}
		}

		// Handles Jumping
		if (status == STATUS.JUMPED) {
			
			speedY += 1; 						// while jumping (in air) decrease y speed
			centerY += speedY;					// update position depending on speed
			
			// once robot hits ground, stop traveling
			if (centerY + speedY >= GROUND) {
				centerY = GROUND;
				speedY = 0;
				status = STATUS.NEUTRAL;
			}
		}
	}

	public void moveRight() {
		if (status != STATUS.DUCKED) {
			speedX = MOVESPEED;
		}
	}

	public void moveLeft() {
		if (status != STATUS.DUCKED) {
			speedX = -MOVESPEED;
		}
	}

	public void stop() {
		speedX = 0;
	}

	public void jump() {
		// need to check to make sure doesn't jump while jumping
		if (status != STATUS.JUMPED) {
			speedY = JUMPSPEED;
			setstatus(STATUS.JUMPED);
		}
	}
	
	public void duck() {
        if (status != STATUS.JUMPED){
            setstatus(STATUS.DUCKED);
            setSpeedX(0);
        }
	}
}