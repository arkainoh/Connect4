package head;

import java.util.ArrayList;
import java.util.List;

import interfaces.BoardWindow;

public class GameTree implements BoardWindow {

	private int DEPTH;
	private int player;
	private StateNode rootNode;
	private List<List<StateNode>> levels;

	// player는 현재 누구 차례인지를 의미
	public GameTree(int player, int[][] board, int stoneNum, int DEPTH) {
		// (주의) Node의 경우 player는 누구 차례인지가 아니라, 누가 돌을 두어서 그 노드의 상태가 된 것인지를 의미
		// 따라서 player를 바꿔줌
		this.player = player;
		int switchedPlayer = (this.player == 1) ? 2 : 1;

		this.DEPTH = DEPTH;
		this.rootNode = new StateNode(switchedPlayer, board, stoneNum);
		levels = new ArrayList<List<StateNode>>();
		for (int i = 0; i <= DEPTH; i++) { // 0~DEPTH까지의 level들을 생성
			levels.add(new ArrayList<StateNode>());
		}

		buildTree(this.DEPTH);
	}

	public StateNode getRootNode() {
		return rootNode;
	}

	public void setDepth(int x) {
		DEPTH = x;
	}

	public int getDepth() {
		return DEPTH;
	}

	private void buildTree(int depth) {
		levels.get(0).add(rootNode);

		for (int i = 0; i < depth; i++) {
			List<StateNode> curLevel = levels.get(i);
			List<StateNode> nextLevel = levels.get(i + 1);

			for (StateNode node : curLevel) {
				nextLevel.addAll(node.generateChildren());
			}
		}
	}

	public int getPlayer() {
		return player;
	}

	public String toString() {
		String result = "<GameTree>\n";

		for (List<StateNode> level : levels) {
			result += "=> " + level.size() + " Nodes\n";
		}

		return result;
	}

	public List<List<StateNode>> getLevels() {
		return levels;
	}

}