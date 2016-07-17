package head;

import java.util.ArrayList;
import java.util.List;

import interfaces.BoardWindow;

public class StateNode implements BoardWindow{
	private int player; //누가 이 노드의 소유인지, 즉 이전 단계에서 누가 돌을 놓았는지를 뜻함
	private int updatedCol; //부모에서 어떤 Column에 돌을 넣었는지를 저장
	private int score;
	private int stoneNum;
	private int[][] board;
	private ArrayList<StateNode> children;
	
	public StateNode(int player, int[][] board, int stoneNum) {
		this.player = player;
		this.updatedCol=-1;
		this.score=0;
		this.board=board;
		this.stoneNum=stoneNum;
		this.children = new ArrayList<StateNode>();
	}
	
	public StateNode(int player, int updatedCol, int[][] board, int stoneNum) {
		this.player = player;
		this.updatedCol=updatedCol;
		this.score=0;
		this.board=board;
		this.stoneNum=stoneNum;
		this.children = new ArrayList<StateNode>();
	}
	
	public int getPlayer() {
		return player;
	}
	public int getUpdatedCol() {
		return updatedCol;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int[][] getBoard() {
		return board;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}
	public Iterable<StateNode> getChildren() {
		return children;
	}
	public int getChildrenNum() {
		return children.size();
	}
	
	public void switchPlayer() {
		this.player = (this.player==1)?2:1;
	}
	
	
	public List<StateNode> generateChildren() {
		for(int i=0;i<COLS;i++){
			int[][] duplicatedBoard = WhereToPut.duplicateBoard(board);
			int switchedPlayer = (player==1)?2:1;
			int[][] temp = WhereToPut.putInCol(duplicatedBoard, switchedPlayer, i);
			if(temp!=null) {
				StateNode newChild = new StateNode(switchedPlayer,i,temp, stoneNum+1); //왠지 노드 생성할때 배열의 값들 복사하게 만들어야할 것 같다...
				//newChild.switchPlayer();
				if(ScoreChecker.getWinner(board, stoneNum)==0)
					children.add(newChild);
			}
		}
		return children; //생성된 child의 갯수를 반환
	}
	
	public String toString() {
		//테스트용 출력 함수
		return "Node: [owner: " + player + "/ updated column: " + updatedCol + "]";
	}
		
}