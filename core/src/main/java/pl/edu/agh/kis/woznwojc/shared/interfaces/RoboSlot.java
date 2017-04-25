package pl.edu.agh.kis.woznwojc.shared.interfaces;

import pl.edu.agh.kis.woznwojc.shared.game.Robot;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;

public interface RoboSlot {
    void trigger(UserInput input, Robot robot);
    double getXOffset();
    double getYOffset();
}
