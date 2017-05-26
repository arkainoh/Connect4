package body;

import head.WhereToPut;

public class TurnChecker implements Runnable {
	Thread th;
	int MYPLAYING;
	int TURN;
	Connect4 obj;

	public TurnChecker(Connect4 obj) {
		this.th = new Thread(this);
		this.obj = obj;
		this.MYPLAYING = obj.getMyplaying();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted()) {
			TURN = obj.getTurn(); // TURN을 최신 상태로 업데이트한다.

			if (TURN == MYPLAYING) { // 만약 방금 누군가가 돌을 두고 나서 컴퓨터가 둘 차례가 되었을 때
				// System.out.println("minmax computing...");
				int col = WhereToPut.evaluate(MYPLAYING, obj.getBoard(), obj.getStoneNum(), obj.getDifficulty());
				// 컴퓨터가 둘 차례일 경우 minmax알고리즘을 수행한다.
				obj.put(MYPLAYING, col);
			}
			System.out.print("");
		}
	}

	public void start() {
		th.start();
	}

	public void stop() {
		th.interrupt();
	}
}
