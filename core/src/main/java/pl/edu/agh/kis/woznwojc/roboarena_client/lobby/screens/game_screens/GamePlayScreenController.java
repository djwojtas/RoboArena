package pl.edu.agh.kis.woznwojc.roboarena_client.lobby.screens.game_screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.roboarena_client.lobby.BackgroundPattern;
import pl.edu.agh.kis.woznwojc.roboarena_server.Session;
import pl.edu.agh.kis.woznwojc.shared.Resolution;
import pl.edu.agh.kis.woznwojc.shared.RobotSize;
import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.SendingGameState;
import pl.edu.agh.kis.woznwojc.shared.game.SendingUser;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Connectable;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GamePlayScreenController implements Screen {
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private GamePlayScreenModel gamePlayScreenModel;
    private GamePlayScreenView gamePlayScreenView;


    public GamePlayScreenController(AssetManager assetsModel, Skin skinModel, Game game, Connectable protocol, Session session) {
        gamePlayScreenModel = new GamePlayScreenModel(assetsModel, skinModel, game, protocol, session);
        gamePlayScreenModel.stage = new Stage();
        gamePlayScreenView = new GamePlayScreenView();
        gamePlayScreenModel.pattern = new BackgroundPattern((Texture)gamePlayScreenModel.assets.get("img/small/arena/bgPattern.png"), 294, 294);
        gamePlayScreenModel.roboTexture = gamePlayScreenModel.assets.get("img/small/robot/robo.png");
        gamePlayScreenModel.laser = gamePlayScreenModel.assets.get("img/small/arena/laser.png");
    }

    private void updateRobot() {
        try {
            if(gamePlayScreenModel.sendingGameState == null) gamePlayScreenModel.sendingGameState = new SendingGameState();
            gamePlayScreenModel.sendingGameState.buildFromString(gamePlayScreenModel.protocol.receiveString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.exit(2);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void getUserOffset() {
        for(SendingUser user : gamePlayScreenModel.sendingGameState.users) {
            if(user.nick.equals(gamePlayScreenModel.session.nick)) {
                gamePlayScreenModel.offsetX = user.robot.x;
                gamePlayScreenModel.offsetY = user.robot.y;
            }
        }
    }

    private void drawBackground() {
        gamePlayScreenModel.pattern.draw(gamePlayScreenModel.stage.getBatch(), (float)gamePlayScreenModel.offsetX, (float)gamePlayScreenModel.offsetY);
    }

    private void drawRobots() {
        gamePlayScreenModel.stage.getBatch().begin();
        for(SendingUser user : gamePlayScreenModel.sendingGameState.users) {
            gamePlayScreenModel.stage.getBatch().draw(gamePlayScreenModel.roboTexture,
                    (float)(user.robot.x - gamePlayScreenModel.offsetX + Resolution.x/2 - RobotSize.radius),
                    (float)(user.robot.y - gamePlayScreenModel.offsetY  + Resolution.y/2 - RobotSize.radius),
                    100.5f, 100.5f,
                    202, 202,
                    1.0f, 1.0f,
                    (float)(user.robot.rotation*(180/Math.PI)),
                    0, 0,
                    202, 202,
                    false, false);
        }
        for(Projectile projectile : gamePlayScreenModel.sendingGameState.projectiles) {
            gamePlayScreenModel.stage.getBatch().draw(gamePlayScreenModel.laser,
                    (float)(projectile.getX() - gamePlayScreenModel.offsetX + Resolution.x/2 - 25),
                    (float)(projectile.getY() - gamePlayScreenModel.offsetY  + Resolution.y/2 - 25),
                    24.5f, 24.5f,
                    49, 49,
                    1.0f, 1.0f,
                    (float)(projectile.getRotation()*(180/Math.PI)),
                    0, 0,
                    49, 49,
                    false, false);
        }
    }

    private void getUserInput(UserInput userInput) {
        userInput.keyW = Gdx.input.isKeyPressed(Input.Keys.W);
        userInput.keyA = Gdx.input.isKeyPressed(Input.Keys.A);
        userInput.keyS = Gdx.input.isKeyPressed(Input.Keys.S);
        userInput.keyD = Gdx.input.isKeyPressed(Input.Keys.D);
        userInput.shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    private void sendUserInput(UserInput userInput) {
        try {
            gamePlayScreenModel.protocol.sendString(userInput.getStringRepresentation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        updateRobot();
        getUserOffset();
        drawBackground();
        drawRobots();
        gamePlayScreenModel.stage.getBatch().end();
        gamePlayScreenModel.stage.act();
        gamePlayScreenView.renderStage(gamePlayScreenModel.stage);
        getUserInput(gamePlayScreenModel.userInput);
        sendUserInput(gamePlayScreenModel.userInput);
    }

    @Override
    public void resize(int width, int height) {
        gamePlayScreenModel.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {

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
