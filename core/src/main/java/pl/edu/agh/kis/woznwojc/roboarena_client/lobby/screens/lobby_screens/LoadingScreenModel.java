package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadingScreenModel {
    public Game game;
    public AssetManager assets;
    public Skin skin;
    public Stage stage;
    public String loadingProgress;
    public Label progressLabel;

    public LoadingScreenModel(AssetManager assetsModel, Skin skinModel, Game game) {
        this.assets = assetsModel;
        this.skin = skinModel;
        this.game = game;
    }
}
