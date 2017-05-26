package body;

import interfaces.BoardWindow;

public class WinnerChecker implements BoardWindow {
	Connect4 obj;
	int[][] board;
	int stone_num;

	public WinnerChecker(Connect4 obj) {
		this.obj = obj;
		this.board = obj.getBoard();
		this.stone_num = obj.getStoneNum();
	}

	public int getWinner() {
		board = obj.getBoard(); // board의 상태를 업데이트해준다.

		int winner = 0; // 초기값은 0으로 설정. 0은 아직 Winner가 결정되지 않았다는 의미
		winner = getWinner_Row();

		if (winner == 0)
			winner = getWinner_Col();

		if (winner == 0)
			winner = getWinner_Diag_LR();

		if (winner == 0)
			winner = getWinner_Diag_RL();

		if (winner == 0 && isFull())
			return 3; // 무승부를 뜻함

		return winner;
	}

	private boolean isFull() { // board가 꽉 차 있는지를 알려줌
		stone_num = obj.getStoneNum();
		return (stone_num >= 42);
	}

	private int getWinner_Row() { // 가로로 이겼는지 검색
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == 1) {
					count1++;
					count2 = 0;
				} else if (board[i][j] == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
			}
		}
		return 0;
	}

	private int getWinner_Col() { // 세로로 이겼는지 검색
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (board[j][i] == 1) {
					count1++;
					count2 = 0;
				} else if (board[j][i] == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
			}
		}
		return 0;
	}

	private int getWinner_Diag_LR() { // 좌측 상단 -> 우측 하단 대각선
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < 3; i++) { // 아래서 위로
			// board[i][COLS-1]; //시작점
			count1 = 0;
			count2 = 0;
			int increment = 0; // 증가분
			while (i + increment < ROWS && COLS - 1 - increment > 0) {
				int curpoint = board[i + increment][COLS - 1 - increment];
				if (curpoint == 1) {
					count1++;
					count2 = 0;
				} else if (curpoint == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
				increment++;
			}
		}

		for (int i = 3; i < COLS - 1; i++) { // 좌에서 우로
			// board[0][i]; //시작점
			count1 = 0;
			count2 = 0;
			int increment = 0; // 증가분
			while (increment < ROWS && i - increment > 0) {
				int curpoint = board[increment][i - increment];
				if (curpoint == 1) {
					count1++;
					count2 = 0;
				} else if (curpoint == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
				increment++;
			}
		}
		return 0;
	}

	private int getWinner_Diag_RL() { // 우측 상단 -> 좌측 하단 대각선
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < 3; i++) { // 아래서 위로
			// board[i][0]; //start point
			count1 = 0;
			count2 = 0;
			int increment = 0; // 증가분
			while (i + increment < ROWS && increment < COLS) {
				int curpoint = board[i + increment][increment]; // 현재 선택된 점
				if (curpoint == 1) {
					count1++;
					count2 = 0;
				} else if (curpoint == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
				increment++;
			}
		}

		for (int i = 1; i <= 3; i++) { // 좌에서 우로
			// board[0][i]; //start point
			count1 = 0;
			count2 = 0;
			int increment = 0; // 증가분
			while (increment < ROWS && increment + i < COLS) {
				int curpoint = board[increment][increment + i]; // 현재 선택된 점
				if (curpoint == 1) {
					count1++;
					count2 = 0;
				} else if (curpoint == 2) {
					count2++;
					count1 = 0;
				} else {
					count1 = 0;
					count2 = 0;
				}
				if (count1 >= 4)
					return 1;
				if (count2 >= 4)
					return 2;
				increment++;
			}
		}

		return 0;
	}
}
