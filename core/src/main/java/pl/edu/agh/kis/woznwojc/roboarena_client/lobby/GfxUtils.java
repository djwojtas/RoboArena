package pl.edu.agh.kis.woznwojc.roboarena_client.lobby;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import pl.edu.agh.kis.woznwojc.shared.Resolution;

public class GfxUtils {
    private static GlyphLayout layout = new GlyphLayout();

    public static float textXCenteringOffset(BitmapFont font, String text) {
        layout.setText(font, text);
        return Resolution.x/2 - layout.width/2;
    }

    public static float XCenteringOffset(float elementWidth) {
        return Resolution.x/2 - elementWidth/2;
    }

    public static float getTextLength(BitmapFont font, String text) {
        layout.setText(font, text);
        return layout.width;
    }
}
