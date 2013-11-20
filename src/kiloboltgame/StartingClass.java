package kiloboltgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import kiloboltgame.Robot.STATUS;

// anything after implements is an interface
public class StartingClass extends Applet implements Runnable, KeyListener {
	private static final long serialVersionUID = 7526472295622776147L;

	private Robot robot;
	private Heliboy hb, hb2;
	private Image image, currentSprite, character, characterJumped, characterDown, background, heliboy;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	
	@Override
	public void init() {

		// initialize frame
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Q-Bot Alpha");

		// add key listener for keyboard
		addKeyListener(this);
		
		// get document base to access images
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		character = getImage(base, "data/character.png");
		characterDown = getImage(base, "data/down.png");
		characterJumped = getImage(base, "data/jumped.png");
		currentSprite = character;
		background = getImage(base, "data/background.png");
		heliboy = getImage(base, "data/heliboy.png");
		
		// Create additional image used to paint canvas for double buffering
		image = createImage(this.getWidth(), this.getHeight());
		second = image.getGraphics();
	}

	@Override
	public void start() {
		// create and start the thread (runnable interface)
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		robot = new Robot();
		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void run() {
		while (true) {
			
			//Value manipulation
			robot.update();	//changes values to bg(1|2) so it should happen before the bg1 updates
			hb.update();
			hb2.update();
			bg1.update();
			bg2.update();
			
			//Redraw frame based on previous value manipulation
			repaint();

			try {
				Thread.sleep(17);				// update every 17 ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void paint(Graphics g) {	
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		second.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		second.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		
		second.drawImage(heliboy, hb.getCenterX() - hb.getCHARWIDTH(), hb.getCenterY() - hb.getCHARWIDTH(), this);
		second.drawImage(heliboy, hb2.getCenterX() - hb2.getCHARWIDTH(), hb2.getCenterY() - hb2.getCHARWIDTH(), this);
		second.drawImage(currentSprite, robot.getCenterX() - robot.CHARWIDTH, robot.getCenterY() - robot.CHARHEIGHT, this);
		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void update(Graphics g) {

		switch (robot.getstatus()) {
		case JUMPED:
			currentSprite = characterJumped;
			break;
		case NEUTRAL:
			currentSprite = character;
			break;
		case DUCKED:
			currentSprite = characterDown;
			break;
		}
		paint(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Move up");
			break;

		case KeyEvent.VK_DOWN:
			robot.duck();
			break;

		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			break;

		case KeyEvent.VK_RIGHT:
			robot.moveRight();
			break;

		case KeyEvent.VK_SPACE:
			robot.jump();
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Stop moving up");
			break;

		case KeyEvent.VK_DOWN:
			if (robot.getstatus() == STATUS.DUCKED) {
				robot.setstatus(STATUS.NEUTRAL);
			}
			break;

		case KeyEvent.VK_LEFT:
			// only stop if actually moving left when left key released
			if(robot.getSpeedX() < 0) {
				robot.stop();
			}
			break;

		case KeyEvent.VK_RIGHT:
			// only stop if actually moving right when right key released
			if(robot.getSpeedX() > 0) {
				robot.stop();
			}
			break;

		case KeyEvent.VK_SPACE:
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	// public and static... which means can be called from robot?
    public static Background getBg1() {
        return bg1;
    }

    public static Background getBg2() {
        return bg2;
    }
}
