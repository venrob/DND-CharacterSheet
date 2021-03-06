package GUI;

import DND.CharGen;
import DND.Main_Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main_Gui extends Application
{
	private static Stage stage;
	public static Scene menu, charsheet, inventory, gen_start, gen_stats_rand, gen_stats_array, gen_stats_pointbuy, gen_char_setup, gen_stats_manual, edit_race;
	protected static GenStats_Array_Controller statsarr_controller;
	protected static GenStats_PointBuy_Controller statspoint_controller;
	protected static GenStats_Random_Controller statsrand_controller;
	protected static GenStats_Manual_Controller statsmanual_controller;
	public static GenStartController genstart_controller;
	public static MainMenuController main_controller;
	protected static GenCharSetupController charsetup_controller;
	protected static EditRaceController race_controller;
	public static CharGen gen;
	
	
	private static final int MINWID = 200, MINHEI = 200, WID = 800, HEI = 600, MAXWID = 0, MAXHEI = 0, STAGE_HEI_OFFS = 30;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		Main_Gui.stage = stage;
		try
		{
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/MainMenu.fxml");
				loader.setLocation(u);
				menu = new Scene(loader.load());
				main_controller = loader.getController();
				main_controller.setScene(menu);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/Gen_Start.fxml");
				loader.setLocation(u);
				gen_start = new Scene(loader.load());
				genstart_controller = loader.getController();
				genstart_controller.setScene(gen_start);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/Gen_Stats_Array.fxml");
				loader.setLocation(u);
				gen_stats_array = new Scene(loader.load());
				statsarr_controller = loader.getController();
				statsarr_controller.setScene(gen_stats_array);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/Gen_Stats_PointBuy.fxml");
				loader.setLocation(u);
				gen_stats_pointbuy = new Scene(loader.load());
				statspoint_controller = loader.getController();
				statspoint_controller.setScene(gen_stats_pointbuy);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/Gen_Stats_Random.fxml");
				loader.setLocation(u);
				gen_stats_rand = new Scene(loader.load());
				statsrand_controller = loader.getController();
				statsrand_controller.setScene(gen_stats_rand);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/GenCharSetup.fxml");
				loader.setLocation(u);
				gen_char_setup = new Scene(loader.load());
				charsetup_controller = loader.getController();
				charsetup_controller.setScene(gen_char_setup);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/Gen_Stats_Manual.fxml");
				loader.setLocation(u);
				gen_stats_manual = new Scene(loader.load());
				statsmanual_controller = loader.getController();
				statsmanual_controller.setScene(gen_stats_manual);
			}
			
			{
				FXMLLoader loader = new FXMLLoader();
				URL u = getClass().getResource("/GUI/EditRace.fxml");
				loader.setLocation(u);
				edit_race = new Scene(loader.load());
				race_controller = loader.getController();
				race_controller.setScene(edit_race);
			}
			
			stage.setTitle("D&D Digital DND.Character Sheet");
			double minwid = calcMinWid();
			double minhei = calcMinHei();
			stage.setMinWidth(minwid);
			stage.setWidth(WID);
			if(MAXWID > 0) stage.setMaxWidth(MAXWID);
			stage.setMinHeight(minhei);
			stage.setHeight(HEI);
			if(MAXHEI > 0) stage.setMaxHeight(MAXHEI);
			stage.setResizable(true);
			setScene(menu);
			stage.setOnCloseRequest(windowEvent ->
			                        {
				                        System.out.println("Closing!");
				                        System.exit(0);
			                        });
			Main_Game.init(); //Initialize the DND environment
			for(SceneController sc : SceneController.controllers)
				sc.init();
			stage.show();
		}
		catch (IOException e)
		{
			System.err.println("Error in main Start method!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static void setScene(Scene scene)
	{
		SceneController sc = SceneController.getController(scene);
		if(sc != null)
		{
			sc.switchTo();
		}
		stage.setScene(scene);
		scene.getRoot().resize(stage.getWidth(), stage.getHeight()-STAGE_HEI_OFFS); //Fix centering
	}
	private static double calcMinWid()
	{
		double ret = MINWID;
		for(SceneController sc : SceneController.controllers)
		{
			Scene s = sc.getScene();
			Parent root = s.getRoot();
			if(root instanceof Pane)
			{
				double w = ((Pane) root).getMinWidth();
				if(ret < w) ret = w;
			}
		}
		return ret;
	}
	private static double calcMinHei()
	{
		double ret = MINHEI;
		for(SceneController sc : SceneController.controllers)
		{
			Scene s = sc.getScene();
			Parent root = s.getRoot();
			if(root instanceof Pane)
			{
				double h = ((Pane) root).getMinHeight();
				if(ret < h) ret = h;
			}
		}
		return ret + STAGE_HEI_OFFS;
	}
}