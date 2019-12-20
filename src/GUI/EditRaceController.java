package GUI;

import DND.Character;
import DND.Race;
import GUI.Helpers.GenericGuiHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.Arrays;

public class EditRaceController extends SceneController
{
	@FXML
	Label nameReq, descriptionReq, trait_ageReq;
	@FXML
	TextField name;
	@FXML
	TextArea description, trait_age;
	@FXML
	Label mode;
	@FXML
	Button confirm, cancel, editAll, editCopy, delete, newRace;
	@FXML
	ChoiceBox<Race> raceChoice;
	private Scene returnScene;
	private Race loadedRace;
	private int loadedRaceIndex = -1;
	private Race cancelToRace;
	private boolean editing = false;
	private Node[] disable_fields;
	public Character currChar;
	
	public boolean init()
	{
		if (super.init()) return true;
		disable_fields = new Node[]{name, description, trait_age};
		raceChoice.setOnAction(foo -> loadRace(raceChoice.getValue()));
		GenericGuiHelper.filterTextField(name, GenericGuiHelper.ALPHANUMERIC_SPACE, 25, "[^\n\t]+", nameReq);
		GenericGuiHelper.filterTextArea(description, "[^\n]*", 0, "[^\n]+", descriptionReq);
		GenericGuiHelper.filterTextArea(trait_age, "[^\n]*", 0, "[^\n]+", trait_ageReq);
		editAll.setTooltip(GenericGuiHelper.instaTT("Edits this Race, affecting ALL loaded characters using this race."));
		editCopy.setTooltip(GenericGuiHelper.instaTT("Edits a copy of this Race, which can be assigned to characters."));
		delete.setTooltip(GenericGuiHelper.instaTT("Deletes this race. Only works if no loaded characters use this Race."));
		return false;
	}
	
	public void setup(Scene returnto, Race race, Character c)
	{
		returnScene = returnto;
		currChar = c;
		resetRaces(race);
	}
	
	public void resetRaces(Race race)
	{
		raceChoice.getItems().clear();
		raceChoice.getItems().addAll(Race.packRaces);
		raceChoice.getItems().addAll(Race.customRaces);
		loadRace(race);
		raceChoice.setValue(loadedRace);
		updateText();
	}
	
	public void onEditAll(ActionEvent e)
	{
		setEditing(true);
	}
	public void onEditCopy(ActionEvent e)
	{
		cancelToRace = loadedRace;
		loadedRace = loadedRace.copy();
		loadedRaceIndex = -1;
		setEditing(true);
	}
	public void onDelete(ActionEvent e)
	{
		Race.customRaces.remove(loadedRace);
		if(currChar!=null && currChar.race == loadedRace) currChar.race = null;
		resetRaces(Race.getRace(loadedRaceIndex-1));
	}
	public void onNew(ActionEvent e)
	{
		cancelToRace = loadedRace;
		loadedRace = new Race();
		loadedRaceIndex = -1;
		name.setText("");
		description.setText("");
		trait_age.setText("");
		setEditing(true);
	}
	
	public void loadRace(Race race)
	{
		if (race == null)
		{
			loadedRaceIndex = -1;
			loadedRace = null;
		}
		else
		{
			loadedRaceIndex = Race.getIndex(race);
			if (loadedRaceIndex == -1)
			{
				Race.customRaces.add(race);
				loadedRaceIndex = Race.getIndex(race);
				loadedRace = Race.getRace(loadedRaceIndex);
			}
			else
			{
				loadedRace = race;
			}
		}
		updateText();
	}
	
	@FXML
	public void updateText(Event e)
	{
		if(editing)
		{
			disable(delete, true);
			disable(editAll, true);
			disable(editCopy, true);
			disable(newRace, true);
			disable(confirm, isInvalid());
			disable(cancel, false);
		}
		else
		{
			disable(cancel, currChar!=null && currChar.race == null);
			disable(newRace, false);
			if (loadedRace == null)
			{
				name.setText("");
				description.setText("");
				trait_age.setText("");
				disable(delete, true);
				disable(editAll, true);
				disable(editCopy, true);
			}
			else
			{
				name.setText(loadedRace.name);
				description.setText(loadedRace.description);
				trait_age.setText(loadedRace.trait_age);
				disable(editCopy, false);
				boolean doDisable = false;
				if(Arrays.asList(Race.packRaces).contains(loadedRace))
					doDisable = true;
				disable(editAll, doDisable); //Don't allow editing packed races directly!
				if(!doDisable)
				{
					for (Character c : Character.loaded)
					{
						if (c.race == loadedRace)
						{
							doDisable = true;
							break;
						}
					}
				}
				disable(delete, doDisable);
			}
		}
	}
	
	@Override
	public void switchTo() //ensure editing is refreshed upon entry
	{
		editing = true;
		setEditing(false);
	}
	
	public void setEditing(boolean edit)
	{
		if (editing == edit)
		{
			updateText();
			return;
		}
		editing = edit;
		if (edit)
		{
			mode.setText("Edit Mode");
			confirm.setText("Save");
			for (Node n : disable_fields)
			{
				disable(n, false);
			}
			disable(raceChoice, true);
		}
		else
		{
			mode.setText("View Mode");
			if (returnScene == Main_Gui.gen_char_setup)
			{
				confirm.setText("Select");
			}
			else
			{
				confirm.setText("Exit");
			}
			for (Node n : disable_fields)
			{
				disable(n, true);
			}
			disable(raceChoice, false);
		}
		updateText();
	}
	
	private void disable(Node n, boolean state)
	{
		if(n instanceof TextArea)
		{
			((TextArea) n).setEditable(!state);
			n.setFocusTraversable(!state);
		}
		else if(n instanceof TextField)
		{
			((TextField) n).setEditable(!state);
			n.setFocusTraversable(!state);
		}
		else
		{
			n.setDisable(state);
			n.setFocusTraversable(!state);
		}
	}
	
	@FXML
	@Override
	public void onClose(ActionEvent e)
	{
		if (editing)
		{
			if(cancelToRace!=null)
			{
				loadedRaceIndex = Race.getIndex(cancelToRace);
			}
			loadRace(Race.getRace(loadedRaceIndex));
			setEditing(false);
		}
		else
		{
			Main_Gui.setScene(returnScene);
			returnScene = null;
		}
	}
	
	@SuppressWarnings({"RedundantIfStatement"})
	private boolean isInvalid()
	{
		if(name.getText() == null || name.getText().equals("")) return true;
		if(description.getText() == null || description.getText().equals("")) return true;
		if(trait_age.getText() == null || trait_age.getText().equals("")) return true;
		
		return false;
	}
	
	private void setLoadedRace()
	{
		if(isInvalid()) return;
		loadedRace.name = name.getText();
		loadedRace.description = description.getText();
		loadedRace.trait_age = trait_age.getText();
	}
	
	@FXML
	@Override
	public void onNext(ActionEvent e)
	{
		if (editing)
		{
			cancelToRace = null;
			setLoadedRace();
			if (loadedRaceIndex == -1)
			{
				Race.customRaces.add(loadedRace);
			}
			else
			{
				Race.setIndex(loadedRaceIndex, loadedRace);
			}
			resetRaces(loadedRace);
			setEditing(false);
		}
		else
		{
			try
			{
				if (currChar != null)
				{
					currChar.race = loadedRace;
				}
				Main_Gui.setScene(returnScene);
			}
			catch (Exception ignored)
			{
			}
		}
	}
}