package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.GfxUtils;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.game_screens.GamePlayScreenController;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.GameName;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;

import java.io.IOException;
import java.util.HashMap;

public class MenuScreenController implements Screen{
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private MenuScreenModel menuScreenModel;
    private MenuScreenView menuScreenView;

    MenuScreenController(AssetManager assetsModel, Skin skinModel, Game game, Connectable protocol, Session session) {
        menuScreenModel = new MenuScreenModel(assetsModel, skinModel, game, protocol, session);
        menuScreenView = new MenuScreenView();
        menuScreenModel.stage = new Stage();

        setupStage();
    }

    private void setupStage() {
        Gdx.input.setInputProcessor(menuScreenModel.stage);

        menuScreenModel.stage.addActor(new Image((Texture) menuScreenModel.assets.get("img/big/menu/menuBg.png")));

        Label loadingLabel = new Label(GameName.name, menuScreenModel.skin, "huge-size");
        loadingLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) menuScreenModel.assets.get("hugeFancyFont.ttf"), GameName.name), 800);
        menuScreenModel.stage.addActor(loadingLabel);

        Label loggedAsLabel = new Label("Logged as " + menuScreenModel.session.nick, menuScreenModel.skin, "medium-warning");
        loggedAsLabel.setPosition(50, 50);
        menuScreenModel.stage.addActor(loggedAsLabel);

        TextButton signInButton = new TextButton("PLAY", menuScreenModel.skin, "huge");
        signInButton.setPosition(GfxUtils.XCenteringOffset(signInButton.getWidth()), 500);
        signInButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                try {
                    joinGame(menuScreenModel.session.nick, menuScreenModel.session.sessionId, new Robot());
                } catch (ClassNotFoundException e) {
                    logger.log(Logger.Level.ERROR, "Class not found during casting");
                } catch (IOException e) {
                    logger.log(Logger.Level.ERROR, "Connection interrupted while joining game");
                }

                menuScreenModel.game.setScreen(new GamePlayScreenController(
                        menuScreenModel.assets,
                        menuScreenModel.skin,
                        menuScreenModel.game,
                        menuScreenModel.protocol,
                        menuScreenModel.session));
            }
        });
        menuScreenModel.stage.addActor(signInButton);

        TextButton exitButton = new TextButton("EXIT", menuScreenModel.skin, "huge");
        exitButton.setPosition(GfxUtils.XCenteringOffset(exitButton.getWidth()), 350);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                logger.log(Logger.Level.INFO, "Exiting by user choice");
                System.exit(0);
            }
        });
        menuScreenModel.stage.addActor(exitButton);
    }

    private void joinGame(String username, String sessionId, Robot robot) throws IOException, ClassNotFoundException {
        menuScreenModel.protocol.connect();
        HashMap<String, String> mapToSend = new HashMap<String, String>();
        mapToSend.put("requestType", "joinGame");
        mapToSend.put("nick", username);
        mapToSend.put("sessionId", sessionId);
        menuScreenModel.protocol.send(mapToSend);
        menuScreenModel.protocol.send(robot);
        logger.log(Logger.Level.INFO, "Joining game");
    }

    @Override
    public void render(float delta) {
        menuScreenModel.stage.act();
        menuScreenView.renderStage(menuScreenModel.stage);
    }

    @Override
    public void resize(int width, int height) {
        menuScreenModel.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        menuScreenModel.stage.dispose();
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
