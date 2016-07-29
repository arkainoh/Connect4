package body;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;


public class InitDialog extends Frame{
	public InitDialog() {
		setSize(300,200);
		
		Dialog info = new Dialog(this, "Information", true);
		info.setSize(140,90);
		info.setLocation(50, 50);
		info.setLayout(new FlowLayout());;
		
		Label msg = new Label("Message content", Label.CENTER);
		Button b1 = new Button("b1");
		Button b2 = new Button("b2");
		info.add(msg);
		info.add(b1);
		info.add(b2);
		info.setVisible(true);
		
	}
	public static void main(String args[]) {
		InitDialog id = new InitDialog();
	}
	
}