package pl.edu.agh.kis.woznwojc.roboarena_client.lobby;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens.LoadingScreenController;

public class ClientController extends Game{
    private ClientModel clientModel;

    @Override
    public void create() {
        clientModel = new ClientModel(new Skin(), new AssetManager());
        AssetsUtils.setTTFResolver(clientModel.assets);
        this.setScreen(new LoadingScreenController(clientModel.assets, clientModel.skin, this));
    }
}
