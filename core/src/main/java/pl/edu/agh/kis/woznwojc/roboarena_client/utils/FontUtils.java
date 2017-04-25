package pl.edu.agh.kis.woznwojc.roboarena_client.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class FontUtils {
    public static void load(AssetManager assets, String fontPath, String fontName, int fontSize) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontToLoadParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontToLoadParams.fontFileName = fontPath;
        fontToLoadParams.fontParameters.size = fontSize;
        assets.load(fontName, BitmapFont.class, fontToLoadParams);
    }
}