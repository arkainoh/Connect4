package body;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class InitDialog{
	public static final int WIDTH = 200;
	public static final int HEIGHT = 100;
	private int turn=0;
	
	public InitDialog(Frame f) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		
		Dialog info = new Dialog(f, "Connect4", true);
		info.setSize(WIDTH,HEIGHT);
		info.setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);
		info.setLayout(new FlowLayout());;
		
		Label msg = new Label("Who's gonna be the first one?", Label.CENTER);
		Button b1 = new Button("Player");
		Button b2 = new Button("AI");
		
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
		info.add(msg);
		info.add(b1);
		info.add(b2);
		info.setVisible(true);
	}
	public void setTurn(int num) { turn = num; }
	public int getTurn() { return turn; }
}