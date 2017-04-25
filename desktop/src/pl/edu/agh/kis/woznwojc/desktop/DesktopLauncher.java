package pl.edu.agh.kis.woznwojc.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.ClientController;
import pl.edu.agh.kis.woznwojc.shared.GameName;
import pl.edu.agh.kis.woznwojc.shared.Resolution;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = GameName.name;
        config.width = Resolution.x;
        config.height = Resolution.y;
        config.fullscreen = false;
        new LwjglApplication(new ClientController(), config);
        //new LwjglApplication(new RoboArenaClient(), config);

		/*LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new roboarena_client(), config);*/
        //TexturePacker.process("img/small", "img/small", "assetAtlas");
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.title = "Mashiin";
        /*config.width = 1920;
        config.height = 1080;*/
        //config.width = 1920;//LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        //config.height = 1080;//LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        //config.allowSoftwareMode = true;
        /*config.fullscreen = false;
		*/
	}
}