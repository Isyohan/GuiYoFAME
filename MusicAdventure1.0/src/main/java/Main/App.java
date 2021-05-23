package Main;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.PhysicsComponent;


import components.*;
import collisions.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import ui.*;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Map;
import java.util.ArrayList;
import java.io.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static Main.Type.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class App extends GameApplication {

    private static final int MAX_LEVEL = 13;
    private static final int STARTING_LEVEL = 0;
    private boolean dead;
    private boolean start=false;
    private double oldtime;
    private double currtime=0;
    private int activButton;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public LoadingScene newLoadingScene() {
                return new BetterLoadingScene();
            }
        });
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
        settings.setTitle("Game 1 Let's go");
        settings.setVersion("1.0");
    }

    private LazyValue<LevelEndScene> levelEndScene = new LazyValue<>(() -> new LevelEndScene());
    private Entity player;

    private boolean isMouseEvents = true;

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                isMouseEvents = true;
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.LEFT, VirtualButton.LEFT);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.RIGHT, VirtualButton.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.UP, VirtualButton.A);

        getInput().addAction(new UserAction("WhistleA") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleA();
                getGameWorld().getEntitiesByType(ENEMYA)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyAComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).left();
                        });
            }
        }, KeyCode.DIGIT1, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleB") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleB();
                getGameWorld().getEntitiesByType(ENEMYB)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyBComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).right();
                        });
            }
        }, KeyCode.DIGIT2, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleC") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleC();
                getGameWorld().getEntitiesByType(ENEMYC)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyCComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).up();
                        });
            }
        }, KeyCode.DIGIT3, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleD") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleD();
                getGameWorld().getEntitiesByType(ENEMYD)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyDComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).down();
                        });
            }
        }, KeyCode.DIGIT4, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleE") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleE();
                getGameWorld().getEntitiesByType(ENEMYE)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyEComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                /*getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).down();
                        });*/
            }
        }, KeyCode.DIGIT5, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleF") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleF();
                getGameWorld().getEntitiesByType(ENEMYF)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyFComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                /*getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).down();
                        });*/
            }
        }, KeyCode.DIGIT6, VirtualButton.X);

        getInput().addAction(new UserAction("WhistleG") {
            @Override
            protected void onActionBegin() {
                isMouseEvents = false;
                player.getComponent(PlayerComponent.class).whistleG();
                getGameWorld().getEntitiesByType(ENEMYG)
                        .stream()
                        .filter(en -> player.getComponent(PlayerComponent.class).closeEnough(en.getComponent(EnemyGComponent.class),200))
                        .forEach(en ->{en.removeFromWorld();
                        });
                /*getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                        .stream()
                        .forEach(plat ->{plat.getComponent(MovingPlatformComponent.class).down();
                        });*/
            }
        }, KeyCode.DIGIT7, VirtualButton.X);

        getInput().addAction(new UserAction("Use") {
            @Override
            protected void onActionBegin() {
                getGameWorld().getEntitiesByType(BUTTON)
                        .stream()
                        .filter(btn -> btn.hasComponent(CollidableComponent.class) && player.isColliding(btn))
                        .forEach(btn -> {
                            btn.removeComponent(CollidableComponent.class);
                            activButton=activButton-1;

                            Entity keyEntity = btn.getObject("keyEntity");
                            keyEntity.setProperty("activated", true);

                            KeyView view = (KeyView) keyEntity.getViewComponent().getChildren().get(0);
                            view.setKeyColor(Color.RED);

                            if (activButton==0){makeExitDoor();}
                        });
            }
        }, KeyCode.E, VirtualButton.B);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", STARTING_LEVEL);
        vars.put("levelTime", 0.0);
        vars.put("score", 0);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.08);
        getSettings().setGlobalSoundVolume(0.08);
        loopBGM("BGM_dash_runner.wav");


    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new Factory());

        player = null;
        nextLevel();
        activButton=3;

        // player must be spawned after call to nextLevel, otherwise player gets removed
        // before the update tick _actually_ adds the player to game world
        player = spawn("player", 50, 50);


        set("player", player);

        spawn("background");


        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);

    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 760);
        getPhysicsWorld().addCollisionHandler(new PlayerButtonHandler());

        onCollisionOneTimeOnly(PLAYER, EXIT_SIGN, (player, sign) -> {
            var texture = texture("exit_sign.png").brighter();
            texture.setTranslateX(sign.getX() + 9);
            texture.setTranslateY(sign.getY() + 13);

            var gameView = new GameView(texture, 150);

            getGameScene().addGameView(gameView);

            runOnce(() -> getGameScene().removeGameView(gameView), Duration.seconds(1.6));
        });

        onCollisionOneTimeOnly(PLAYER, EXIT_TRIGGER, (player, trigger) -> {
            makeExitDoor();
        });

        onCollisionOneTimeOnly(PLAYER, DOOR_BOT, (player, door) -> {
            levelEndScene.get().onLevelFinish();

            // the above runs in its own scene, so fade will wait until
            // the user exits that scene
            getGameScene().getViewport().fade(() -> {
                nextLevel();
            });
        });

        onCollisionOneTimeOnly(PLAYER, MESSAGE_PROMPT, (player, prompt) -> {
            prompt.setOpacity(1);

            despawnWithDelay(prompt, Duration.seconds(4.5));
        });

        onCollisionBegin(PLAYER, KEY_PROMPT, (player, prompt) -> {
            String key = prompt.getString("key");

            var entity = getGameWorld().create("keyCode", new SpawnData(prompt.getX(), prompt.getY()).put("key", key));
            spawnWithScale(entity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());

            runOnce(() -> {
                despawnWithScale(entity, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
            }, Duration.seconds(2.5));
        });
        onCollisionBegin(PLAYER,ENEMY,(player,enemy)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYA,(player,enemyA)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYB,(player,enemyB)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYC,(player,enemyC)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYD,(player,enemyD)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYE,(player,enemyE)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYF,(player,enemyF)->{dead =true;});
        onCollisionBegin(PLAYER,ENEMYG,(player,enemyG)->{dead =true;});
    }

    private void makeExitDoor() {
        var doorTop = getGameWorld().getSingleton(DOOR_TOP);
        var doorBot = getGameWorld().getSingleton(DOOR_BOT);

        doorBot.getComponent(CollidableComponent.class).setValue(true);

        doorTop.setOpacity(1);
        doorBot.setOpacity(1);
    }

    private void nextLevel() {
        if (geti("level") == MAX_LEVEL) {
            showMessage("You finished the game!");
            return;
        }
        if (geti("level") == 1) {
            //getAudioPlayer().stopAllMusic();
            //loopBGM("Venus.wav");
            //ArrayList<String> musiques = new ArrayList<String>();
            //musiques.add("USA_Anthem.mp3");
            //loopBGM((musiques.get(0)));

        }
        inc("level", +1);
        setLevel(geti("level"));


        if (geti("level") == 10) {
            getAudioPlayer().stopAllMusic();
            loopBGM("HymneFrancais.wav");
            showMessage("French level !");
        }
        if (geti("level") == 11) {
            getAudioPlayer().stopAllMusic();
            loopBGM("USA_Anthem.mp3");
            showMessage("Americain level !");
        }
        if (geti("level") == 12) {
            getAudioPlayer().stopAllMusic();
            loopBGM("BGM_dash_runner.wav");
        }
        if (geti("level") == 6) {
            spawn("background2");
        }
        if (geti("level") == 10) {
            spawn("background3");
        }
        if (geti("level") == 4) {
            showMessage("Here are the Moving Platform !" +
                    "\n See how to move them with your notes in order to finish the level");
        }
        /*
        if (geti("level") == 5 || geti("level") == 9) {
            nextLevel();
        }
        */
    }

    @Override
    protected void initUI() {
        if (isMobile()) {
            var dpadView = getInput().createVirtualDpadView();
            var buttonsView = getInput().createXboxVirtualControllerView();

            addUINode(dpadView, 0, getAppHeight() - 290);
            addUINode(buttonsView, getAppWidth() - 280, getAppHeight() - 290);

            runOnce(() -> {
                dpadView.getScene().addEventFilter(TouchEvent.ANY, event -> {
                    if (!isMouseEvents) {
                        System.out.println(event);
                    }
                });

                dpadView.getScene().addEventFilter(MouseEvent.ANY, event -> {
                    if (isMouseEvents) {
                        System.out.println(event);
                    }
                });
            }, Duration.seconds(2));
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("levelTime", tpf);
        currtime+=tpf;

        if (player.getY() > getAppHeight() || dead==true) {
            onPlayerDied();
            dead=false;
        }
        if (player.getY() >getAppHeight()-300 && start==false) {
            showMessage(" You are trapped in a Musician Dream, You have to find a way out" +
                    "\n Use arrows to move, you can make notes with number,1 to 7 for A to G " +
                    "\n You can double jump" +
                    "\n Enemies are weak against the notes they are, don't forget that..." +
                    "\n Good luck and have fun !!");
            start = true;
        }


    }

    public void onPlayerDied() {
        getGameWorld().getEntitiesByType(MOVINGPLATFORM)
                .stream()
                .forEach(plat->{plat.removeFromWorld();});
        getGameWorld().getEntitiesByType(ENEMY)
                .stream()
                .forEach(en->{en.removeFromWorld();});
        setLevel(geti("level"));
        showMessage("You are dead.\n You will respawn at the start of the level \n Go on you can do it !");
        player.getComponent(PlayerComponent.class).stop();

    }

    private void setLevel(int levelNum) {

        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
            player.setZIndex(Integer.MAX_VALUE);
        }
        activButton=3;

        set("levelTime", 0.0);

        Level level = setLevelFromMap("tmx/level" + levelNum  + ".tmx");

        var shortestTime = level.getProperties().getDouble("star1time");

        var levelTimeData = new LevelEndScene.LevelTimeData(shortestTime * 2.4, shortestTime*1.3, shortestTime);

        set("levelTimeData", levelTimeData);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
