package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.lobby_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_client.ClientLobbyProtocol;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.GfxUtils;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.ConnectionParams;

import java.io.IOException;
import java.util.HashMap;

public class LoginScreenController implements Screen {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);

    private LoginScreenModel loginScreenModel;
    private LoginScreenView loginScreenView;

    public void setupStage() {
        Gdx.input.setInputProcessor(loginScreenModel.stage);

        loginScreenModel.stage.addActor(new Image((Texture) loginScreenModel.assets.get("img/big/menu/menuBg.png")));

        Label loadingLabel = new Label("LOGIN", loginScreenModel.skin, "huge-size");
        loadingLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loginScreenModel.assets.get("hugeFancyFont.ttf"), "LOGIN"), 800);
        loginScreenModel.stage.addActor(loadingLabel);

        Label loginText = new Label("nick", loginScreenModel.skin, "medium-description");
        loginText.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loginScreenModel.assets.get("mediumBoldFont.ttf"), "nick"), 700);
        loginScreenModel.stage.addActor(loginText);

        Label passwordText = new Label("password", loginScreenModel.skin, "medium-description");
        passwordText.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loginScreenModel.assets.get("mediumBoldFont.ttf"), "password"), 550);
        loginScreenModel.stage.addActor(passwordText);

        loginScreenModel.warnLabel = new Label("", loginScreenModel.skin, "medium-warning");
        loginScreenModel.stage.addActor(loginScreenModel.warnLabel);

        TextButton signInButton = new TextButton("Sign in", loginScreenModel.skin, "big");
        signInButton.setPosition(GfxUtils.XCenteringOffset(signInButton.getWidth()), 250);
        signInButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                try {
                    logger.log(Logger.Level.DEBUG, "Login triggered");
                    String sessionId = login(loginScreenModel.nickField.getText(), loginScreenModel.passField.getText());
                    logger.log(Logger.Level.DEBUG, "Received session id: " + sessionId);
                    if(sessionId != null) {
                        logger.log(Logger.Level.DEBUG, "Logged in: " + sessionId);
                        loginScreenModel.game.setScreen(
                                new MenuScreenController(
                                        loginScreenModel.assets,
                                        loginScreenModel.skin,
                                        loginScreenModel.game,
                                        loginScreenModel.protocol,
                                        new Session(loginScreenModel.nickField.getText(), sessionId, System.currentTimeMillis())
                                )
                        );
                    } else {
                        logger.log(Logger.Level.DEBUG, "Not correct nick/pass");
                        updateWarning("Wrong username/password");
                    }
                } catch(IOException e) {
                    logger.log(Logger.Level.ERROR, "Connection timeout");
                    updateWarning("Error while connecting to server");
                } catch (ClassNotFoundException e) {
                    logger.log(Logger.Level.ERROR, "Cast to HashMap unsuccessful");
                    updateWarning("Error while receiving server response");
                }
            }
        });
        loginScreenModel.stage.addActor(signInButton);

        TextButton createButton = new TextButton("Create account", loginScreenModel.skin, "big");
        createButton.setPosition(GfxUtils.XCenteringOffset(createButton.getWidth()), 150);
        createButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.log(Logger.Level.DEBUG, "Create account triggered");
                try {
                    if(createAccount(loginScreenModel.nickField.getText(), loginScreenModel.passField.getText())) {
                        logger.log(Logger.Level.DEBUG, "Account created with nick " + loginScreenModel.nickField.getText());
                        updateWarning("Account with nick \"" + loginScreenModel.nickField.getText() + "\" successfully created");
                    } else {
                        logger.log(Logger.Level.DEBUG, "Not correct nick/pass");
                        updateWarning("Account with nick \"" + loginScreenModel.nickField.getText() + "\" already exists. Choose other nick.");
                    }
                } catch(IOException e) {
                    logger.log(Logger.Level.ERROR, "Connection timeout");
                    updateWarning("Error while connecting to server");
                } catch (ClassNotFoundException e) {
                    logger.log(Logger.Level.ERROR, "Cast to HashMap unsuccessful");
                    updateWarning("Error while receiving server response");
                }

            }
        });
        loginScreenModel.stage.addActor(createButton);

        loginScreenModel.nickField = new TextField("", loginScreenModel.skin, "medium");
        loginScreenModel.nickField.setAlignment(1);
        loginScreenModel.nickField.setMaxLength(16);
        float width = ((BitmapFont) loginScreenModel.assets.get("mediumBoldFont.ttf")).getSpaceWidth()*32;
        float height = ((BitmapFont) loginScreenModel.assets.get("mediumBoldFont.ttf")).getLineHeight();
        loginScreenModel.nickField.setSize(width, height);
        loginScreenModel.nickField.setPosition(GfxUtils.XCenteringOffset(width), 630);
        loginScreenModel.stage.addActor(loginScreenModel.nickField);

        loginScreenModel.passField = new TextField("", loginScreenModel.skin, "medium");
        loginScreenModel.passField.setAlignment(1);
        loginScreenModel.passField.setPasswordCharacter('*');
        loginScreenModel.passField.setPasswordMode(true);
        loginScreenModel.passField.setSize(width, height);
        loginScreenModel.passField.setPosition(GfxUtils.XCenteringOffset(width), 480);
        loginScreenModel.stage.addActor(loginScreenModel.passField);
    }

    private String login(String username, String password) throws IOException, ClassNotFoundException {
        loginScreenModel.protocol.connect();
        HashMap<String, String> mapToSend = new HashMap<String, String>();
        mapToSend.put("requestType", "login");
        mapToSend.put("nick", username);
        mapToSend.put("password", password);
        loginScreenModel.protocol.send(mapToSend);
        HashMap<String, String> response = (HashMap<String, String>) loginScreenModel.protocol.receive();
        return response.get("sessionId");
    }

    private boolean createAccount(String username, String password) throws IOException, ClassNotFoundException {
        loginScreenModel.protocol.connect();
        HashMap<String, String> mapToSend = new HashMap<String, String>();
        mapToSend.put("requestType", "createAccount");
        mapToSend.put("nick", username);
        mapToSend.put("password", password);
        loginScreenModel.protocol.send(mapToSend);
        HashMap<String, String> response = (HashMap<String, String>) loginScreenModel.protocol.receive();
        return response.get("accountCreated").equals("true");
    }

    private void updateWarning(String warning) {
        loginScreenModel.warnLabel.setText(warning);
        loginScreenModel.warnLabel.setPosition(GfxUtils.textXCenteringOffset((BitmapFont) loginScreenModel.assets.get("mediumBoldFont.ttf"), warning), 395);
    }

    public LoginScreenController(AssetManager assetsModel, Skin skinModel, Game game) {
        loginScreenModel = new LoginScreenModel(assetsModel, skinModel, game);
        loginScreenView = new LoginScreenView();
        loginScreenModel.stage = new Stage();
        loginScreenModel.protocol = new ClientLobbyProtocol(ConnectionParams.timeout);
        setupStage();
    }

    @Override
    public void render(float delta) {
        loginScreenModel.stage.act();
        loginScreenView.renderStage(loginScreenModel.stage);
    }

    @Override
    public void resize(int width, int height) {
        loginScreenModel.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        loginScreenModel.stage.dispose();
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
