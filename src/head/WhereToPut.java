package head;

import java.util.List;

import body.Connect4;
import interfaces.BoardWindow;

public class WhereToPut implements BoardWindow {
	public static int POSITIVE_INFINITY = Integer.MAX_VALUE;
	public static int NEGATIVE_INFINITY = Integer.MIN_VALUE;

	static int z = 0;

	public static int evaluate(int player, int[][] board, int stoneNum, int DIFFICULTY) {
		long startTime = System.currentTimeMillis();
		long endTime;
		/*
		 * @input: 누가 둘 차례인지 player를 받음
		 * 
		 * @input: board에 놓인 돌의 상태를 argument로 받고 이것을 root node로 설정한다. 그리고 minmax
		 * 알고리즘을 수행
		 * 
		 * @output: 어느 column에 넣어야 하는지를 반환 (0~COLS-1)
		 */
		// Node (Score, int[][] board, Node[7] children) 를 가지는 노드 만들자
		// 이걸로 트리를 구성하자
		// 알파베타 프루닝은 일단은 DFS로 맨 왼쪽의 노드는 다 본다! 그리고 값을 확정한 뒤 범위를 책정한다

		int result = -1;
		int depth = 0;

		int switchedPlayer = (player == 1) ? 2 : 1;
		StateNode rootNode = new StateNode(switchedPlayer, board, stoneNum);

		if (DIFFICULTY == Connect4.HIGH) {
			// 돌의 갯수에 따라 depth를 증가시켜주는 로직
			if (stoneNum >= 26) {
				depth = 14;
				System.out.println("depth" + depth + "로 증가");
			} else if (stoneNum >= 24) {
				depth = 13;
				System.out.println("depth" + depth + "로 증가");
			} else if (stoneNum >= 20) {
				depth = 12;
				System.out.println("depth" + depth + "로 증가");
			} else if (stoneNum >= 16) {
				depth = 11;
				System.out.println("depth" + depth + "로 증가");
			} else if (stoneNum >= 12) {
				depth = 10;
				System.out.println("depth" + depth + "로 증가");
			} else if (stoneNum >= 8) {
				depth = 9;
				System.out.println("depth" + depth + "로 증가");
			} else
				depth = 8;
		} else if (DIFFICULTY == Connect4.MODERATE)
			depth = 5;
		else
			depth = 2;

		// 이기는 것 검사
		StateNode testNode1 = new StateNode(switchedPlayer, board, stoneNum);
		testNode1.generateChildren();
		for (StateNode tempChild : testNode1.getChildren()) {
			tempChild.setScore(ScoreChecker.getScore(player, tempChild.getBoard()));
			if (tempChild.getScore() == ScoreChecker.WIN)
				return tempChild.getUpdatedCol();
		}
		// 등잔 밑이 어두운 단점을 보완
		StateNode testNode2 = new StateNode(player, board, stoneNum);
		testNode2.generateChildren();
		for (StateNode tempChild : testNode2.getChildren()) {
			tempChild.setScore(ScoreChecker.getScore(player, tempChild.getBoard()));
			if (tempChild.getScore() == ScoreChecker.LOSE)
				return tempChild.getUpdatedCol();
		}
		// GameTree gt = new GameTree(player, board, stoneNum, depth);
		// //DEPTH만큼의 수 앞을 내다보는 트리 생성
		// Minimax(gt);
		// ABPruningMinimax(gt);
		newABPruningMinimax(rootNode, depth);

		// 갈 방향을 정하기 위해 Root Node의 바로 한단계 밑 자식 노드들 중에서 가장 큰 추정값을 갖는 것을 선택한다.
		if (rootNode.getChildrenNum() != 0) {
			int maxScore = NEGATIVE_INFINITY;
			for (StateNode child : rootNode.getChildren()) {
				if (child.getScore() > maxScore) {
					maxScore = child.getScore();
					result = child.getUpdatedCol();
				} else if (child.getScore() == maxScore) {
					if (child.getScore() == NEGATIVE_INFINITY) {
						maxScore = child.getScore();
						result = child.getUpdatedCol();
					}
				}
			}
		}
		// debug
		System.out.print("LOG: [" + (++z) + "] ");
		for (StateNode haha : rootNode.getChildren()) {
			System.out.print(haha.getScore() + " ");
		}
		System.out.println("\n     Column " + (result + 1) + " Selected");
		endTime = System.currentTimeMillis();
		System.out.println("     elapsed time : " + (endTime - startTime) + "milliseconds");
		return result;
	}

	private static void newABPruningMinimax(StateNode root, int depth) {
		int a = NEGATIVE_INFINITY;
		int b = POSITIVE_INFINITY;
		int player = root.getPlayer();
		int switchedPlayer = (player == 1) ? 2 : 1;
		newabpruningMinimax(root, switchedPlayer, a, b, depth);
	}

	private static int newabpruningMinimax(StateNode parentNode, int player, int a, int b, int depth) {
		// a는 자식 노드들에게서 지금까지 찾아낸 최소의 값 (maximizer의 입장에서)
		// b는 자식 노드들에게서 지금까지 찾아낸 최소의 값 (minimizer의 입장에서)
		// 참고 - http://popungpopung.tistory.com/10
		int score;

		if (depth == 0) { // depth가 0이 되었다면 자기 자신의 점수를 내고 해당 점수를 위로 올림
			score = ScoreChecker.getScore(player, parentNode.getBoard());
			parentNode.setScore(score);
			return score;
		} else { // depth가 0이 아닌 경우
			parentNode.generateChildren();

			if (parentNode.getPlayer() == player) {
				/*
				 * 만약 해당 노드가 자신이 두어야 할 선택지라면, 자식 노드들의 추정값 중 가장 작은 값을 취한다. (자식
				 * 노드의 level은 상대가 두어야 할 선택지이기때문)
				 */
				for (StateNode child : parentNode.getChildren()) {
					score = newabpruningMinimax(child, player, a, b, depth - 1);
					if (b > score)
						b = score;
					if (b <= a)
						break;
					// a값보다 b값이 커야 더 위의 조상 중 maximizer가 값을 취할 수 있다.
					// 그렇지 않을 경우 버려도 상관없다.
				}
				parentNode.setScore(b);
				return b;

			} else {
				// 만약 자신이 두어야 할 선택지가 아니라면, 자식들 중 가장 큰 값을 취한다.
				for (StateNode child : parentNode.getChildren()) {
					score = newabpruningMinimax(child, player, a, b, depth - 1);
					if (a < score)
						a = score;
					if (b <= a)
						break;
					// b보다 a가 작은 값이 나와야 하는데, a가 한 번 큰 순간 절대 b보다 작아질 수 없다.
				}
				parentNode.setScore(a);
				return a;
			}
		}
	}

	private static void Minimax(GameTree gt) {
		// alpha-beta pruning이 없는 단순 Minimax
		int lastLevel = gt.getDepth();
		List<List<StateNode>> levels = gt.getLevels();
		List<StateNode> leaves = levels.get(lastLevel);
		int player = gt.getPlayer();

		while (leaves == null) {
			leaves = levels.get(--lastLevel);
		}
		// 자식 노드가 하나도 없을 경우를 대비해서 트리의 마지막 층을 구하는 단계이다.
		// 마지막 층부터 검사해서 아무 노드도 없으면 마지막 층을 한단계 위로 설정한다.

		/*
		 * if(lastLevel == 0) { //만약 마지막 레벨이 rootNode라면 더이상 둘 수 없다는 의미이므로 이미
		 * getWinner()에서 draw로 끝이날 것이므로 이 부분은 생략해도 된다. return 마지막 남은 column }
		 */

		for (StateNode child : leaves) // 맨 아래층의 자식 노드들에게 추정값을 부여한다.
			child.setScore(ScoreChecker.getScore(player, child.getBoard()));

		for (int i = lastLevel - 1; i > 0; i--) {
			List<StateNode> curLevel = levels.get(i);

			if (!curLevel.isEmpty()) {

				if (curLevel.get(0).getPlayer() == player) {
					// 만약 해당 level이 자신이 두어야 할 선택지라면,
					for (StateNode node : curLevel) {
						if (node.getChildrenNum() != 0)
							minNode(node);
						// 자식 노드들의 추정값 중 가장 작은 값을 취한다.
						// (자식 노드의 level은 상대가 두어야 할 선택지이기 때문)
						else // 자식이 하나도 없으면 자기 자신의 스코어를 계산한다.
							node.setScore(ScoreChecker.getScore(player, node.getBoard()));

					}
				} else {
					for (StateNode node : curLevel) {
						if (node.getChildrenNum() != 0)
							maxNode(node);
						else // 자식이 하나도 없으면 자기 자신의 스코어를 계산한다.
							node.setScore(ScoreChecker.getScore(player, node.getBoard()));

					}
				}
			}
		}

	}

	private static void ABPruningMinimax(GameTree gt) {
		int a = NEGATIVE_INFINITY;
		int b = POSITIVE_INFINITY;
		StateNode root = gt.getRootNode();
		abpruningMinimax(root, gt.getPlayer(), a, b);
	}

	private static int abpruningMinimax(StateNode parentNode, int player, int a, int b) {
		// a는 자식 노드들에게서 지금까지 찾아낸 최소의 값 (maximizer의 입장에서)
		// b는 자식 노드들에게서 지금까지 찾아낸 최소의 값 (minimizer의 입장에서)
		// 참고 - http://popungpopung.tistory.com/10
		int score;

		if (parentNode.getChildrenNum() == 0) {
			// 자식이 하나도 없다면 자기 자신의 점수를 내고 해당 점수를 위로 올림
			score = ScoreChecker.getScore(player, parentNode.getBoard());
			parentNode.setScore(score);
			return score;
		} else { // 자식이 있는 경우
			if (parentNode.getPlayer() == player) {
				// 만약 해당 노드가 자신이 두어야 할 선택지라면,
				// 자식 노드들의 추정값 중 가장 작은 값을 취한다.
				// (자식 노드의 level은 상대가 두어야 할 선택지이기 때문)
				for (StateNode child : parentNode.getChildren()) {
					score = abpruningMinimax(child, player, a, b);
					if (b > score)
						b = score;
					if (b <= a)
						break;
					// a값보다 b값이 커야 더 위의 조상 중 maximizer가 값을 취할 수 있다.
					// 그렇지 않을 경우 버려도 상관없다.
				}
				parentNode.setScore(b);
				return b;

			} else {
				// 만약 자신이 두어야 할 선택지가 아니라면,
				// 자식들 중 가장 큰 값을 취한다.
				for (StateNode child : parentNode.getChildren()) {
					score = abpruningMinimax(child, player, a, b);
					if (a < score)
						a = score;
					if (b <= a)
						break;
					// b보다 a가 작은 값이 나와야 하는데, a가 한 번 큰 순간 절대 b보다 작아질 수 없다.
				}
				parentNode.setScore(a);
				return a;
			}
		}
	}

	public static int[][] duplicateBoard(int[][] board) {
		int[][] newBoard = new int[ROWS][COLS];
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				newBoard[i][j] = board[i][j];
		return newBoard;
	}

	public static int[][] putInCol(int[][] board, int player, int col) {
		if (player != 1 && player != 2)
			return null;
		// col의 범위가 0~6이 아닌 경우 어차피 index outofbound exception이 발생할 것

		for (int i = 0; i < ROWS; i++) {
			if (board[i][col] == 0) {
				board[i][col] = player;
				return board;
			}
		}
		return null;
		// 만약 꽉 차있는 column에 put했을 시 null을 반환 -> alpha-beta pruning을 위해!
	}

	// 자식 노드의 추정값 중에서 가장 큰 값을 취한다.
	private static void maxNode(StateNode node) {
		int maxScore = NEGATIVE_INFINITY;

		for (StateNode child : node.getChildren()) {
			if (maxScore <= child.getScore())
				maxScore = child.getScore();
		}
		node.setScore(maxScore);
	}

	// 자식 노드의 추정값 중에서 가장 작은 값을 취한다.
	private static void minNode(StateNode node) {
		int minScore = POSITIVE_INFINITY;

		for (StateNode child : node.getChildren()) {
			if (minScore >= child.getScore())
				minScore = child.getScore();
		}
		node.setScore(minScore);
	}

}