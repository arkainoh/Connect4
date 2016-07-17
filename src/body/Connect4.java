package body;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import head.ScoreChecker;
import interfaces.BoardWindow;

public class Connect4 extends Frame implements BoardWindow
{
	private static final long serialVersionUID = -8157243558265538348L;
	
	private int[][] board;
	/*
	 * ~~~~~ (ROWS-1,COLS-1)
	 * 
	 * (0,0) ~~~~~
	 */
	private int MYPLAYING;
	private int URPLAYING;
	private int TURN;
	private int stone_num; //현재 돌의 갯수
	TurnChecker turnChecker;
	
	Label statL;
	
	public Connect4() {
		TURN = 1;
		board = new int[ROWS][COLS];
		setTitle("Four in a row");
		setSize(WINDOW_SIZE_W,WINDOW_SIZE_H);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(res.width/2-WINDOW_SIZE_W/2,res.height/2-WINDOW_SIZE_H/2);
		setResizable(false);
		
		Panel titleP = new Panel();
		titleP.setLayout(new BorderLayout());
		Font tfont = new Font("", Font.PLAIN, 35);
		Panel titleLP1 = new Panel();
		Label titleL1 = new Label("<FOUR IN A ROW> Ver 1.4");
		titleL1.setFont(tfont);
		
		Panel titleLP2 = new Panel();
		Label titleL2 = new Label("CREATED BY Kim, Inho 2012130888");
		Button refresh = new Button("Refresh");
		refresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		titleLP2.setBackground(Color.LIGHT_GRAY);
		
		Panel buttonsP = new Panel();
		buttonsP.setLayout(new GridLayout(1, COLS));
		
		Panel statP = new Panel();
		statP.setLayout(new BorderLayout());
		statL = new Label("");
		
		titleLP1.add(titleL1);
		titleLP2.add(titleL2);
		titleLP2.add(refresh);
		titleP.add("North", titleLP1);
		titleP.add("Center", titleLP2);
		titleP.add("South",buttonsP);
		for(int i=0; i<COLS; i++) {
			Button b = new Button();
			final int tmp=i;
			b.setLabel(""+(i+1));
			b.setBackground(Color.white);
			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e){
					if(TURN==URPLAYING)
						put(URPLAYING, tmp);
				}
			});

			buttonsP.add(b,i);
		}
		
		statP.add("Center", statL);
		
		add("North",titleP);
		add("South", statP);
		repaint();
		setVisible(true);
		
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	
	
	void init(int PLAYING){ //보드를 초기화한다.
		if(PLAYING != 1 && PLAYING !=2) return;
		
		board = new int[ROWS][COLS];
		this.MYPLAYING = PLAYING;
		this.URPLAYING = (PLAYING == 1)? 2:1;
		
		
		stone_num=0;
		if(MYPLAYING == 1) {
			int flag = (int)(Math.random()*2);
			if(flag==0)
				put(MYPLAYING, 0, 5);
			else
				put(MYPLAYING, 0, 1);
			
		}
		turnChecker = new TurnChecker(this);
		turnChecker.start();
	}

	public void paint(Graphics g) { //View 부분
		g.setColor(Color.BLUE);
		g.fillRect(BOARD_X, BOARD_Y, BOARD_W, BOARD_H);
		g.setColor(Color.WHITE);
		for(int i=1;i<=6;i++) //세로선 그리기
			g.drawLine(BOARD_X+i*BOARD_W_INTERVAL, BOARD_Y, BOARD_X+i*BOARD_W_INTERVAL, BOARD_Y+BOARD_H);
		for(int i=1;i<=5;i++) //가로선 그리기
			g.drawLine(BOARD_X, BOARD_Y+i*BOARD_H_INTERVAL, BOARD_X+BOARD_W, BOARD_Y+i*BOARD_H_INTERVAL);
		
		switch(TURN) { //누구의 turn인지 알려주는 부분
		case 1:
			if(MYPLAYING==TURN)
				statL.setText("Player 1(CPU)'s turn...");
			else
				statL.setText("Player 1's turn...");
			statL.setBackground(Color.red);
			break;
		case 2:
			if(MYPLAYING==TURN)
				statL.setText("Player 2(CPU)'s turn...");
			else
				statL.setText("Player 2's turn...");
			statL.setBackground(Color.yellow);
			break;
		}
		//돌의 위치를 검사해서 표현해주는 부분
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<COLS;j++) {
				switch(board[i][j]) {
				case 1:
					g.setColor(Color.red);
					g.fillOval(BOARD_X + j*BOARD_W_INTERVAL, BOARD_Y+BOARD_H - (i+1)*BOARD_H_INTERVAL, BOARD_W_INTERVAL, BOARD_H_INTERVAL);
					g.setColor(Color.black);
					g.drawOval(BOARD_X + j*BOARD_W_INTERVAL, BOARD_Y+BOARD_H - (i+1)*BOARD_H_INTERVAL, BOARD_W_INTERVAL, BOARD_H_INTERVAL);
					
					break;
				case 2:
					g.setColor(Color.yellow);
					g.fillOval(BOARD_X + j*BOARD_W_INTERVAL, BOARD_Y+BOARD_H - (i+1)*BOARD_H_INTERVAL, BOARD_W_INTERVAL, BOARD_H_INTERVAL);
					g.setColor(Color.black);
					g.drawOval(BOARD_X + j*BOARD_W_INTERVAL, BOARD_Y+BOARD_H - (i+1)*BOARD_H_INTERVAL, BOARD_W_INTERVAL, BOARD_H_INTERVAL);
					
					break;
				}
			}
		}
	}

	void print() { //테스트용 출력 함수
		System.out.println("=======");
		for(int i = ROWS-1; i>=0; i--){
			for (int j=0; j<COLS; j++)
				System.out.print(board[i][j]);
			System.out.println("");
		}
	}

	public void put(int player, int col) { // Column을 선택해서 돌을 떨어뜨려주는 메서드
		for(int i=0;i<ROWS;i++) {
			if(board[i][col]==0) {
				put(player, i, col);	
				break;
			}
		}
	}
	private void put(int player, int x, int y) { // 실제 배열의 index에 돌을 넣어주는 부분
		
		if (player != 1 && player != 2) return;
		board[x][y] = player;
		stone_num++;
		repaint();
		gameover();
		if(TURN!=-1)
			TURN = (player == 1)?2:1;
		//print();
	}
	
	
	private void gameover() { //게임이 끝났는지 체크해준다.
		int result = ScoreChecker.getWinner(board ,stone_num);
		switch(result) {
			case 1:
				if(MYPLAYING==1)
					JOptionPane.showMessageDialog(this,
						    "Player 1(CPU) WIN!");
				else
					JOptionPane.showMessageDialog(this,
						    "Player 1 WIN!");
				break;
				
			case 2:
				if(MYPLAYING==2)
					JOptionPane.showMessageDialog(this,
						    "Player 2(CPU) WIN!");
				else
					JOptionPane.showMessageDialog(this,
						    "Player 2 WIN!");
				break;
				
			case 3:
				JOptionPane.showMessageDialog(this,
					    "DRAW!");
				break;
		}
		
		if(result!=0) {
			turnChecker.stop();
			TURN=-1; //TURN을 -1로 바꾼다. 게임이 종료되었다는 의미
		}
	}
	
	public int getStoneNum() {return stone_num;}
	public int getMyplaying() {return MYPLAYING;}
	public int getTurn() {return TURN;}
	public int[][] getBoard() {return board;}
	
	public static void main(String[] args) { //테스트용 메인 함수
		Connect4 co4 = new Connect4();
		co4.init(1);
		//co4.print();
	}

}