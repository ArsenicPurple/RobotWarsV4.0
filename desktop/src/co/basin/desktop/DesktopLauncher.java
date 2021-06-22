package co.basin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import co.basin.RobotWars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Robot Wars";
		config.width = 900;
		config.height = 900;
		config.foregroundFPS = 60;
		config.forceExit = false;

		new LwjglApplication(new RobotWars(), config);
	}
}
