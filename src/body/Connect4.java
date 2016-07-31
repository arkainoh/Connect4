package body;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

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
	private int DIFFICULTY;
	
	public static final int LOW = 0; 
	public static final int MODERATE = 1;
	public static final int HIGH = 2;
	
	TurnChecker turnChecker;
	private MenuBar menuBar; // 메뉴 막대
	private Menu menuNewGame;
	private Menu menuDebug;
	private Menu menuDifficulty;
	
	private MenuItem menuPlayerFirst;
	private MenuItem menuAIFirst;
	private CheckboxMenuItem menuDifficultyLow; // 저장
	private CheckboxMenuItem menuDifficultyModerate; // 다른 이름으로 저장
	private CheckboxMenuItem menuDifficultyHigh; // 인쇄
	private MenuItem menuRefresh;
	private Toolkit tk;
	private Image ArkainohLogo;
	private int logo_W;
	private int logo_H;
	Label statL;
	
	public Connect4() {
		
		setTitle("Connect4");
		setSize(WINDOW_SIZE_W,WINDOW_SIZE_H);
		tk = Toolkit.getDefaultToolkit();
		Dimension res = tk.getScreenSize();
		setLocation(res.width/2-WINDOW_SIZE_W/2,res.height/2-WINDOW_SIZE_H/2);
		setResizable(false);
		
		//Menus
		menuBar = new MenuBar();
		setMenuBar(menuBar);
		
		menuNewGame = new Menu("New Game");
		menuBar.add(menuNewGame);
		//sub menu로 누가 먼저 할지 정하기
		menuPlayerFirst = new MenuItem("Player First");
		menuAIFirst = new MenuItem("AI First");
		menuNewGame.add(menuPlayerFirst);
		menuNewGame.add(menuAIFirst);
		menuPlayerFirst.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				System.out.println("New Game : Player First");
				restart(2);
			}
		});
		menuAIFirst.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				System.out.println("New Game : AI First");
				restart(1);
			}
		});
		
		menuDifficulty = new Menu("Difficulty");
		menuBar.add(menuDifficulty);
		//sub menu로 상, 중, 하 - 체크 그룹으로 만들기!!!
		
		menuDifficultyLow = new CheckboxMenuItem("Low", false);
		menuDifficultyModerate = new CheckboxMenuItem("Moderate", false);
		menuDifficultyHigh = new CheckboxMenuItem("High", true);
		menuDifficulty.add(menuDifficultyLow);
		menuDifficulty.add(menuDifficultyModerate);
		menuDifficulty.add(menuDifficultyHigh);

		menuDifficultyLow.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) {
				if(menuDifficultyLow.getState()) {
					setDifficulty(LOW);
				} else {
					menuDifficultyLow.setState(true);
				}
			}
		});
		menuDifficultyModerate.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) {
				if(menuDifficultyModerate.getState()) {
					setDifficulty(MODERATE);
				} else {
					menuDifficultyModerate.setState(true);
				}
			}
		});
		menuDifficultyHigh.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) {
				if(menuDifficultyHigh.getState()) {
					setDifficulty(HIGH);
				} else {
					menuDifficultyHigh.setState(true);
				}
			}
		});
		
		menuDebug = new Menu("Debug");
		menuBar.add(menuDebug);
		menuRefresh = new MenuItem("Refresh");
		
		menuRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				System.out.println("Refresh");
				repaint();
			}
		});
		menuDebug.add(menuRefresh);
		URL url = getClass().getResource("/Logo with letters_Whitened.png");
		ArkainohLogo = tk.getImage(url);
		
		Panel titleP = new Panel();
		titleP.setLayout(new BorderLayout());
		Font tfont = new Font("", Font.PLAIN, 35);
		Panel titleLP1 = new Panel();
		Label titleL1 = new Label("FOUR IN A ROW");
		titleL1.setFont(tfont);
		
		Panel titleLP2 = new Panel();
		Label titleL2 = new Label("CREATED BY Arkainoh");
		titleLP2.setBackground(Color.LIGHT_GRAY);
		
		Panel buttonsP = new Panel();
		buttonsP.setLayout(new GridLayout(1, COLS));
		
		Panel statP = new Panel();
		statP.setLayout(new BorderLayout());
		statL = new Label("");
		
		titleLP1.add(titleL1);
		titleLP2.add(titleL2);
		//titleP.add("North", titleLP1);
		//titleP.add("Center", titleLP2);
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
		statP.add("North", buttonsP);
		statP.add("Center", statL);

		//add("North",titleP);
		add("South", statP);
		
		//add("Center", )
		repaint();
		//setVisible(true);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	
	void start() { //Game Start
		//show window to choose
		InitDialog initDialog = new InitDialog(this);
		setVisible(true); //Show the game window
		init(initDialog.getTurn(), initDialog.getDifficulty());
	}
	
	void restart(int PLAYING) { //Restart
		TURN = -1;
		if(turnChecker != null){
			turnChecker.stop();
			turnChecker = null;
		}
		
		init(PLAYING, getDifficulty());
		repaint();
	}
	
	void init(int PLAYING, int difficulty){ //보드를 초기화한다.
		if(PLAYING != 1 && PLAYING !=2) return;
		TURN = 1;
		stone_num=0;
		

		setDifficulty(difficulty);
		board = null;
		board = new int[ROWS][COLS];
		this.MYPLAYING = PLAYING;
		this.URPLAYING = (PLAYING == 1)? 2:1;
		
		if(MYPLAYING == 1) {
			int startingPoint = (int)(Math.random()*5) + 1;
			put(MYPLAYING, 0, startingPoint);
		}

		turnChecker = new TurnChecker(this);
		turnChecker.start();
	}

	public void paint(Graphics g) { //View 부분
		if(ArkainohLogo != null){
			logo_W = ArkainohLogo.getWidth(this);
			logo_H = ArkainohLogo.getHeight(this);
			g.drawImage(ArkainohLogo,(WINDOW_SIZE_W-logo_W)/2,(190-logo_H)-27,this);

		}
		
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
				statL.setText("Player 1(AI)'s turn...");
			else
				statL.setText("Player 1's turn...");
			statL.setBackground(Color.red);
			break;
		case 2:
			if(MYPLAYING==TURN)
				statL.setText("Player 2(AI)'s turn...");
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
	
	public void setDifficulty(int dif) {
		switch(dif) {
		case LOW: //low
			System.out.println("Set Difficulty : LOW");
			if(menuDifficultyLow.getState() == false) menuDifficultyLow.setState(true);
			menuDifficultyModerate.setState(false);
			menuDifficultyHigh.setState(false);
			DIFFICULTY = LOW;
			break;
		case MODERATE: //moderate
			System.out.println("Set Difficulty : MODERATE");
			if(menuDifficultyModerate.getState() == false) menuDifficultyModerate.setState(true);
			menuDifficultyHigh.setState(false);
			menuDifficultyLow.setState(false);
			DIFFICULTY = MODERATE;
			break;
		case HIGH: //high
			System.out.println("Set Difficulty : High");
			if(menuDifficultyHigh.getState() == false) menuDifficultyHigh.setState(true);
			menuDifficultyModerate.setState(false);
			menuDifficultyLow.setState(false);
			DIFFICULTY = HIGH;
			break;
		}
	}
	public int getDifficulty() {return DIFFICULTY;}
	public int getStoneNum() {return stone_num;}
	public int getMyplaying() {return MYPLAYING;}
	public int getTurn() {return TURN;}
	public int[][] getBoard() {return board;}
	
	public static void main(String[] args) { //테스트용 메인 함수
		Connect4 co4 = new Connect4();
		co4.start();
	}

}