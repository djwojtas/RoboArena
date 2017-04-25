package pl.edu.agh.kis.woznwojc.roboarena_client.lobby;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import pl.edu.agh.kis.woznwojc.shared.Resolution;

public class BackgroundPattern {
    Texture pattern;
    int width;
    int height;
    int xPatterns;
    int yPatterns;

    public BackgroundPattern(Texture pattern, int width, int height) {
        this.pattern = pattern;
        this.width = width;
        this.height = height;
        xPatterns = (int)(Resolution.x / (float) width) + 3;
        yPatterns = (int)(Resolution.y / (float) height) + 3;
    }

    public void draw(Batch batch, float x, float y) {
        x = (-x) % this.width;
        y = (-y) % this.height;

        batch.begin();
        for(int i=0; i<xPatterns; ++i) {
            for(int j=0; j<yPatterns; ++j) {
                batch.draw(pattern, width*(i-1) + x, height*(j-1) + y);
            }
        }
        batch.end();
    }
}
