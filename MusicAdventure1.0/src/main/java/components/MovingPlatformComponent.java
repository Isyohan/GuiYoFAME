package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class MovingPlatformComponent extends Component {

    private PhysicsComponent physics;

    private Texture texture;

    private AnimationChannel animIdle, animWalk;

    private double curtime=0;
    private double oldtime;
    private boolean moving=false;
    private String positionX="init";
    private String positionY="init";


    public MovingPlatformComponent() {

        Image image = image("dirt_32x32.png");

        //texture = new AnimatedTexture(animIdle);
        texture = new Texture(image);

    }
    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

    }
    @Override
    public void onUpdate(double tpf) {
        curtime+=tpf;
        //System.out.println(curtime+"    "+oldtime);
        if (curtime>=oldtime+0.1 && moving){
            stop();
        }
    }
    public void left() {
        if (!moving && positionX!="left") {
            oldtime = curtime;
            physics.setVelocityX(-1700);
            moving = true;
            if (positionX=="right"){
                positionX="init";
            }else{
                positionX="left";
            }
        }
    }
    public void right() {
        if (!moving && positionX !="right"){
            oldtime=curtime;
            physics.setVelocityX(1700);
            moving=true;
            if (positionX=="left"){
                positionX="init";
            }else{
                positionX="right";
            }
        }
    }
    public void up(){
        if (!moving && positionY !="up") {
            oldtime=curtime;
            physics.setVelocityY(-1700);
            moving = true;
            if (positionY=="down"){
                positionY="init";
            }else{
                positionY="up";
            }
        }
    }
    public void down(){
        if(!moving && positionY !="down") {
            oldtime=curtime;
            physics.setVelocityY(1700);
            moving = true;
            if (positionY=="up"){
                positionY="init";
            }else{
                positionY="down";
            }
        }
    }
    public void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
        moving=false;
    }
}
