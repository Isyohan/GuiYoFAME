package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.app.scene.Viewport;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.canvas.GraphicsContext;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PlayerComponent extends Component {

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk;
    private Canvas cv;
    private GraphicsContext gc ;

    private boolean attack=false;
    private boolean attackAnim=false;

    private int jumps = 2;
    private double currtime=0;
    private double oldtime;
    private double animX=0;
    private double animY=0;

    public PlayerComponent() {

        Image image = image("player.png");

        animIdle = new AnimationChannel(image, 4, 32, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel(image, 4, 32, 42, Duration.seconds(0.66), 0, 3);

        texture = new AnimatedTexture(animIdle);
        texture.loop();

        cv = new Canvas();
        getGameScene().getContentRoot().getChildren().add(cv);
        cv.setHeight(getAppHeight());
        cv.setWidth(getAppWidth());
        gc = cv.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                //play("land.wav");
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        currtime+=tpf;
        if (physics.isMovingX()) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }
        Point2D p=this.getEntity().getPosition();
        if (p.getY()<=0){
            physics.setVelocityY(50);
        }
        if (attack){
            oldtime=currtime;
            attack=false;
            attackAnim=true;
            Point2D pp = this.entity.getPosition();
            animY=pp.getY();
            animX=getAppWidth()/2;
        }
        if (attackAnim){
            double dt=currtime-oldtime;
            Point2D pp = this.entity.getPosition();
            gc.clearRect(0,0,getAppWidth(),getAppHeight());
            gc.strokeOval(animX+20 - dt *200, animY+20 - dt *200, dt *400, dt *400);

            if (dt>1){
                attackAnim=false;
                gc.clearRect(0,0,getAppWidth(),getAppHeight());
            }
        }

    }
    public boolean isAttack(){
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
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
        getSettings().setGlobalSoundVolume(1);
        play("Kick.wav");
        getSettings().setGlobalSoundVolume(0.08);
        physics.setVelocityY(-300);

        jumps--;
    }
    public void whistleA() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/A(La).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleB() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/B(Si).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleC() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/C(Do).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleD() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/D(Ré).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleE() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/E(Mi).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleF() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/F(Fa).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
    }
    public void whistleG() {
        getSettings().setGlobalSoundVolume(1);
        play("notes/G(Sol).wav");
        getSettings().setGlobalSoundVolume(0.08);
        attack=true;
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
    public Boolean closeEnough(EnemyAComponent en,double dMax){
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
    public Boolean closeEnough(EnemyBComponent en,double dMax){
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
    public Boolean closeEnough(EnemyCComponent en,double dMax){
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
    public Boolean closeEnough(EnemyDComponent en,double dMax){
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
    public Boolean closeEnough(EnemyEComponent en,double dMax){
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
    public Boolean closeEnough(EnemyFComponent en,double dMax){
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
    public Boolean closeEnough(EnemyGComponent en,double dMax){
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
