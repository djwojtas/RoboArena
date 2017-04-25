package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.game_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.BackgroundPattern;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.SendingGameState;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

public class GamePlayScreenModel {
    public Game game;
    public AssetManager assets;
    public Skin skin;
    public Stage stage;
    public Connectable protocol;
    public Session session;
    public BackgroundPattern pattern;
    public SendingGameState sendingGameState;
    public double offsetX;
    public double offsetY;
    public Texture roboTexture;
    public Texture laser;
    public UserInput userInput;

    public GamePlayScreenModel(AssetManager assetsModel, Skin skinModel, Game game, Connectable protocol, Session session) {
        this.assets = assetsModel;
        this.skin = skinModel;
        this.game = game;
        this.protocol = protocol;
        this.session = session;
        userInput = new UserInput();
    }
}
