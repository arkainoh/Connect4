package interfaces;

public interface BoardWindow {

	public static final int COLS = 7;
	public static final int ROWS = 6;
	public static final int WINDOW_SIZE_W = 480;
	public static final int WINDOW_SIZE_H = 640;

	public static final int BOARD_X = 5;
	public static final int BOARD_Y = 185; //
	public static final int BOARD_W = WINDOW_SIZE_W - BOARD_X * 2;
	public static final int BOARD_H = BOARD_W / 7 * 6; // 나누어 떨어지게 만들어주자
	public static final int BOARD_W_INTERVAL = BOARD_W / COLS;
	public static final int BOARD_H_INTERVAL = BOARD_H / ROWS;

}
