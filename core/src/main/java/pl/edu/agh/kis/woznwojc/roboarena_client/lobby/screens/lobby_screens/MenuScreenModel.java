package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

public class MenuScreenModel {
    public Game game;
    public AssetManager assets;
    public Skin skin;
    public Stage stage;
    public Connectable protocol;
    public Session session;

    public MenuScreenModel(AssetManager assetsModel, Skin skinModel, Game game, Connectable protocol, Session session) {
        this.assets = assetsModel;
        this.skin = skinModel;
        this.game = game;
        this.protocol = protocol;
        this.session = session;
    }
}
