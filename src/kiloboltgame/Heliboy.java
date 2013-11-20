package kiloboltgame;

public class Heliboy extends Enemy {

	private int CHARWIDTH = 48;
	
	public Heliboy(int centerX, int centerY) {
		setCenterX(centerX);
		setCenterY(centerY);
	}

	public int getCHARWIDTH() {
		return CHARWIDTH;
	}

	public void setCHARWIDTH(int width) {
		CHARWIDTH = width;
	}

}
