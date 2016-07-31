package body;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class InitDialog{
	public static final int WIDTH = 220;
	public static final int HEIGHT = 160;
	private int turn=0;
	Dialog info;
	Label msg1;
	CheckboxGroup chGroup;
	Checkbox chLow;
	Checkbox chModerate;
	Checkbox chHigh;
	Label msg2;
	Button b1;
	Button b2;
	
	public InitDialog(Frame f) {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		
		info = new Dialog(f, "Connect4", true);
		info.setSize(WIDTH,HEIGHT);
		info.setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);
		info.setLayout(new FlowLayout());;
		
		msg1 = new Label("Select the difficulty level", Label.CENTER);
		chGroup = new CheckboxGroup();
		chLow = new Checkbox("Low", chGroup, true);
		chModerate = new Checkbox("Moderate", chGroup, false);
		chHigh = new Checkbox("High", chGroup, false);
		msg1.setFont(new Font("Dialog",Font.BOLD,13));
		info.add(msg1);
		info.add(chLow);
		info.add(chModerate);
		info.add(chHigh);
		
		msg2 = new Label("Who's gonna be the first one?", Label.CENTER);
		b1 = new Button("Player");
		b2 = new Button("AI");
		
		info.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				setTurn(2);
				info.dispose();
			}
		});
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				setTurn(1);
				info.dispose();
			}
		});
		msg2.setFont(new Font("Dialog",Font.BOLD,13));
		info.add(msg2);
		info.add(b1);
		info.add(b2);
		info.setVisible(true);
	}
	public void setTurn(int num) { turn = num; }
	public int getTurn() { return turn; }
	public int getDifficulty() {
		if(chLow.getState()) return Connect4.LOW;
		else if(chModerate.getState()) return Connect4.MODERATE;
		else return Connect4.HIGH;
	}
}