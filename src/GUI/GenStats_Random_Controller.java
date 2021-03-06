package GUI;

import DND.Character;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GenStats_Random_Controller extends SceneController
{
	@FXML
	Text strength, dexterity, constitution, intelligence, wisdom, charisma;
	public void updateText(Event e)
	{
		Character c = Main_Gui.gen.c;
		strength.setText("" + c.str);
		dexterity.setText("" + c.dex);
		constitution.setText("" + c.con);
		intelligence.setText("" + c.intel);
		wisdom.setText("" + c.wis);
		charisma.setText("" + c.cha);
	}
	@FXML
	public void onReset(ActionEvent e)
	{
		Main_Gui.gen.initStats();
		updateText();
	}
	@FXML
	public void onClose(ActionEvent e)
	{
		if(Main_Gui.gen.back())
			Main_Gui.setScene(Main_Gui.gen_start);
	}
	
	@FXML
	public void onNext(ActionEvent e)
	{
		if (Main_Gui.gen.confirm())
		{
			Main_Gui.charsetup_controller.init();
			Main_Gui.charsetup_controller.reset();
			Main_Gui.setScene(Main_Gui.gen_char_setup);
		}
	}
}
