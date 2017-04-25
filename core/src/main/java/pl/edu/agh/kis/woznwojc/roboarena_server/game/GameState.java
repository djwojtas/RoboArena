package pl.edu.agh.kis.woznwojc.roboarena_server.game;

import pl.edu.agh.kis.woznwojc.shared.interfaces.Obstacle;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;

import java.util.ArrayList;

public class GameState {
    ArrayList<User> users;
    ArrayList<Projectile> projectiles;
    Obstacle[] obstacles;

    public GameState(ArrayList<User> users, ArrayList<Projectile> projectiles, Obstacle[] obstacles) {
        this.users = users;
        this.projectiles = projectiles;
        this.obstacles = obstacles;
    }
}