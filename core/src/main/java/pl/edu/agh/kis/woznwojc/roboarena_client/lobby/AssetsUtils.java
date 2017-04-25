package pl.edu.agh.kis.woznwojc.roboarena_client.lobby;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class AssetsUtils {
    public static void setTTFResolver(AssetManager assets) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
    }

    public static void loadTTFFont(AssetManager assets, String fontPath, String fontName, int fontSize) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontToLoadParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontToLoadParams.fontFileName = fontPath;
        fontToLoadParams.fontParameters.size = fontSize;
        assets.load(fontName, BitmapFont.class, fontToLoadParams);
    }
}
