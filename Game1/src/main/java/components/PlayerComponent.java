package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk;

    private int jumps = 2;

    public PlayerComponent() {

        Image image = image("player.png");

        animIdle = new AnimationChannel(image, 4, 32, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel(image, 4, 32, 42, Duration.seconds(0.66), 0, 3);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                play("land.wav");
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (physics.isMovingX()) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    public void left() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void stop() {
        physics.setVelocityX(0);
    }

    public void jump() {
        if (jumps == 0)
            return;

        play("jump.wav");
        physics.setVelocityY(-300);

        jumps--;
    }
    public void whistleA() {

        play("notes/A(La).wav");

    }
    public void whistleB() {

        play("notes/B(Si).wav");

    }
    public void whistleC() {

        play("notes/C(Do).wav");

    }
    public void whistleD() {

        play("notes/D(Ré).wav");

    }
    public void whistleE() {

        play("notes/E(Mi).wav");

    }
    public void whistleF() {

        play("notes/F(Fa).wav");

    }
    public void whistleG() {

        play("notes/G(Sol).wav");

    }
    public double dEnemy(EnemyComponent enemy){
        double d=0;


        return d;
    }

    public Boolean closeEnough(EnemyComponent en,double dMax){
        Point2D pe=en.getEntity().getPosition();
        Point2D pp=this.getEntity().getPosition();
        double dX=Math.abs(pp.getX()-pe.getX());
        double dY=Math.abs(pp.getY()-pe.getY());
        double d=Math.sqrt(dY*dY+dX*dX);
        if (d<dMax){
            return true;
        }
        return false;
    }
}
