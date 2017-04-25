package pl.edu.agh.kis.woznwojc.roboarena_server.game;

import pl.edu.agh.kis.woznwojc.logger.Log;
import pl.edu.agh.kis.woznwojc.logger.Logger;
import pl.edu.agh.kis.woznwojc.shared.game.projectiles.LaserProjectile;
import pl.edu.agh.kis.woznwojc.shared.interfaces.Projectile;
import pl.edu.agh.kis.woznwojc.shared.Resolution;
import pl.edu.agh.kis.woznwojc.shared.game.ArenaComposition;
import pl.edu.agh.kis.woznwojc.shared.game.SendingGameState;
import pl.edu.agh.kis.woznwojc.shared.game.SendingUser;
import pl.edu.agh.kis.woznwojc.shared.game.UserInput;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameWorker implements Runnable{
    /**
     * Static logger for in-class usage
     */
    private static final Logger logger = new Log(System.out, Logger.Level.TRACE);
    private GameState gameState;
    private CopyOnWriteArrayList<User> newUsers;
    private CopyOnWriteArrayList<User> disconnectedUsers;
    private ConcurrentHashMap<String, UserInput> userInput;
    private Set<String> busyPlayers;
    private ExecutorService playerWorkers;

    private long lastIterTime;
    private long thisIterTime;
    private long waitTime;
    private final int tickTime = 17;
    private long avgTaskTime = 0;
    private long maxTaskTime = 0;
    private int countedTicks = 0;
    private final int ticksPerLog = 360;

    public GameWorker(CopyOnWriteArrayList<User> newUsers) {
        this.newUsers = newUsers;
        gameState = new GameState(new ArrayList<User>(), new ArrayList<Projectile>(), ArenaComposition.obstacles);
        lastIterTime = System.currentTimeMillis();
        playerWorkers = Executors.newFixedThreadPool(2);
        disconnectedUsers = new CopyOnWriteArrayList<User>();
        userInput = new ConcurrentHashMap<String, UserInput>();
        busyPlayers = Collections.synchronizedSet(new HashSet<String>());
    }

    private void logTickTime() {
        if(countedTicks >= ticksPerLog) {
            logger.log(Logger.Level.DEBUG, "Stats for " + countedTicks + " ticks:  avgTime: " + (tickTime - avgTaskTime) + "ms maxTime: " + (tickTime - maxTaskTime) + "ms");
            countedTicks = 0;
        }
    }

    private void pauseForTick() {
        thisIterTime = System.currentTimeMillis();
        waitTime = 17 - thisIterTime + lastIterTime;

        if(countedTicks == 0) {
            avgTaskTime = waitTime;
            maxTaskTime = waitTime;
            countedTicks++;
        } else {
            if(waitTime > maxTaskTime) maxTaskTime = waitTime;
            avgTaskTime = (avgTaskTime*countedTicks + waitTime)/(++countedTicks);
        }
        logTickTime();
        try {
            if((17 - thisIterTime + lastIterTime) > 0) Thread.sleep(waitTime);
            lastIterTime = System.currentTimeMillis();
        } catch (InterruptedException e) {
            logger.log(Logger.Level.ERROR, "Thread sleep interrupted");
        }
    }

    @Override
    public void run() {
        while(true) {
            pauseForTick();

            for(User user : disconnectedUsers) {
                gameState.users.remove(user);
                disconnectedUsers.remove(user);
            }

            for(User user : newUsers) {
                gameState.users.add(user);
                userInput.put(user.session.nick, new UserInput());
                newUsers.remove(user);
            }

            for(User user : gameState.users) {
                if(userInput.get(user.session.nick).keyW) {
                    user.robot.acceleration = 0.1;
                } else if(userInput.get(user.session.nick).keyS) {
                    user.robot.acceleration = -0.1;
                } else {
                    user.robot.acceleration = 0;
                }

                if(userInput.get(user.session.nick).keyA) {
                    user.robot.rotationAcceleration = 0.001;
                } else if (userInput.get(user.session.nick).keyD) {
                    user.robot.rotationAcceleration = -0.001;
                } else {
                    user.robot.rotationAcceleration = 0;
                }

                if(userInput.get(user.session.nick).shift) {
                    if(System.currentTimeMillis() - user.lastShoot > 1000) {
                        user.lastShoot = System.currentTimeMillis();
                        LaserProjectile proj = new LaserProjectile(3000, user.robot.x, user.robot.y, user.robot.rotation, user.session.nick);
                        gameState.projectiles.add(proj);
                    }
                }

                user.robot.speed += user.robot.acceleration - (user.robot.airResistance * user.robot.speed);
                user.robot.rotationSpeed += user.robot.rotationAcceleration - (user.robot.airResistance * user.robot.rotationSpeed);

                user.robot.rotation += user.robot.rotationSpeed;

                user.robot.x += user.robot.speed*Math.cos(user.robot.rotation);
                user.robot.y += user.robot.speed*Math.sin(user.robot.rotation);
            }

            for (Iterator<Projectile> it = gameState.projectiles.iterator(); it.hasNext();){
                Projectile projectile = it.next();
                projectile.move(17);
                for(User user : gameState.users) {
                    if(projectile.collision(user.robot) && !user.session.nick.equals(projectile.getOwner())) {
                        user.robot.x = 0;
                        user.robot.y = 0;
                    }
                }
                if(!projectile.exists()){
                    it.remove();
                }
            }

            for(User user : gameState.users) {
                if(!busyPlayers.contains(user.session.nick)) {
                    busyPlayers.add(user.session.nick);
                    ArrayList<SendingUser> usersToSend = new ArrayList<SendingUser>();
                    for (User otherPlayer : gameState.users) {
                        if (Math.abs(user.robot.x - otherPlayer.robot.x) < Resolution.x && Math.abs(user.robot.y - otherPlayer.robot.y) < Resolution.y) {
                            usersToSend.add(new SendingUser(otherPlayer.robot, otherPlayer.session));
                        }
                    }
                    ArrayList<Projectile> projectilesToSend = new ArrayList<Projectile>();
                    for (Projectile projectile : gameState.projectiles) {
                        if (Math.abs(user.robot.x - projectile.getX()) < Resolution.x && Math.abs(user.robot.y - projectile.getY()) < Resolution.y) {
                            projectilesToSend.add(new LaserProjectile(0, projectile.getX(), projectile.getY(), 0, "X"));
                        }
                    }
                    SendingGameState sendingGameState = new SendingGameState(usersToSend, projectilesToSend);
                    playerWorkers.execute(new PlayerWorker(sendingGameState, user.connection, disconnectedUsers, user, userInput, busyPlayers));
                } else {
                    logger.log(Logger.Level.DEBUG, "Player \"" + user.session.nick + "\" busy.");
                }
            }
        }
    }
}
