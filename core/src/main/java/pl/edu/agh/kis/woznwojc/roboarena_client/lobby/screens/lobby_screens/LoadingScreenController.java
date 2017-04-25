package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.AssetsUtils;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.GfxUtils;

public class LoadingScreenController implements Screen {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);

    private LoadingScreenModel loadingScreenModel;
    private LoadingScreenView loadingScreenView;

    private void loadNecessaryAssets() {
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/fancyFont.ttf", "hugeFancyFont.ttf", 240);
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/fancyFont.ttf", "bigFancyFont.ttf", 210);
        loadingScreenModel.assets.load("img/big/menu/menuBg.png", Texture.class);
        loadingScreenModel.assets.finishLoading();
    }

    private void loadNecessarySkin() {
        addPixmapToSkin();
        loadingScreenModel.skin.add("huge-fancy-font", loadingScreenModel.assets.get("hugeFancyFont.ttf"));
        loadingScreenModel.skin.add("big-fancy-font", loadingScreenModel.assets.get("bigFancyFont.ttf"));
        loadingScreenModel.skin.load(Gdx.files.internal("config/loadingSkin.json"));
    }

    private void queueAllAssets() {
        loadingScreenModel.assets.load("img/small/robot/robo.png", Texture.class);
        loadingScreenModel.assets.load("img/small/robot/gun.png", Texture.class);
        loadingScreenModel.assets.load("img/small/robot/shield.png", Texture.class);
        loadingScreenModel.assets.load("img/small/robot/engine.png", Texture.class);
        loadingScreenModel.assets.load("img/small/arena/bgPattern.png", Texture.class);
        loadingScreenModel.assets.load("img/small/arena/laser.png", Texture.class);
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/boldFont.ttf", "mediumBoldFont.ttf", 48);
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/boldFont.ttf", "bigBoldFont.ttf", 72);
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/boldFont.ttf", "hugeBoldFont.ttf", 120);
        AssetsUtils.loadTTFFont(loadingScreenModel.assets, "fonts/textFont.otf", "mediumTextFont.ttf", 48);
    }

    private void updateProgress() {
        loadingScreenModel.loadingProgress = ((int) (loadingScreenModel.assets.getProgress()*100)) + "%";
        loadingScreenModel.progressLabel.setText(loadingScreenModel.loadingProgress);
        loadingScreenModel.progressLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loadingScreenModel.assets.get("bigFancyFont.ttf"), loadingScreenModel.loadingProgress), 400);
    }

    private void addPixmapToSkin() {
        Pixmap pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        loadingScreenModel.skin.add("plain", new Texture(pixmap));
    }

    private void setupStage() {
        Gdx.input.setInputProcessor(loadingScreenModel.stage);

        loadingScreenModel.stage.addActor(new Image((Texture) loadingScreenModel.assets.get("img/big/menu/menuBg.png")));

        Label loadingLabel = new Label("LOADING", loadingScreenModel.skin, "huge-size");
        loadingLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loadingScreenModel.assets.get("hugeFancyFont.ttf"), "LOADING"), 800);
        loadingScreenModel.stage.addActor(loadingLabel);

        loadingScreenModel.progressLabel = new Label(loadingScreenModel.loadingProgress, loadingScreenModel.skin, "big-size");
        loadingScreenModel.progressLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loadingScreenModel.assets.get("bigFancyFont.ttf"), loadingScreenModel.loadingProgress), 400);
        loadingScreenModel.stage.addActor(loadingScreenModel.progressLabel);
    }

    public LoadingScreenController(AssetManager assets, Skin skin, Game game) {
        loadingScreenView = new LoadingScreenView();
        loadingScreenModel = new LoadingScreenModel(assets, skin, game);
        loadingScreenModel.stage = new Stage();
        loadingScreenModel.loadingProgress = "0%";

        loadNecessaryAssets();
        queueAllAssets();
        loadNecessarySkin();
        setupStage();
    }

    private void endLoading() {
        loadingScreenModel.skin.add("medium-bold-font", loadingScreenModel.assets.get("mediumBoldFont.ttf"));
        loadingScreenModel.skin.add("medium-text-font", loadingScreenModel.assets.get("mediumTextFont.ttf"));
        loadingScreenModel.skin.add("big-bold-font", loadingScreenModel.assets.get("bigBoldFont.ttf"));
        loadingScreenModel.skin.add("huge-bold-font", loadingScreenModel.assets.get("hugeBoldFont.ttf"));
        loadingScreenModel.skin.load(Gdx.files.internal("config/defaultSkin.json"));
    }

    @Override
    public void render(float delta) {
        updateProgress();
        loadingScreenModel.stage.act();
        loadingScreenView.renderStage(loadingScreenModel.stage);
        if(loadingScreenModel.assets.update()) {
            endLoading();
            loadingScreenModel.game.setScreen(
                    new LoginScreenController(loadingScreenModel.assets, loadingScreenModel.skin, loadingScreenModel.game)
            );
        }
    }

    @Override
    public void resize(int width, int height) {
        loadingScreenModel.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        loadingScreenModel.stage.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
