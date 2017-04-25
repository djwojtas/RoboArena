package pl.edu.agh.kis.woznwojc.roboarena_client.lobby;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClientModel {
    Skin skin;
    AssetManager assets;

    ClientModel(Skin skin, AssetManager assets) {
        this.skin = skin;
        this.assets = assets;
    }
}
