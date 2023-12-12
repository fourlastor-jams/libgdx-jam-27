package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.ds.ObjectList;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import io.github.fourlastor.harlequin.component.ActorComponent;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private final TextureAtlas textureAtlas;
    private final InputStateMachine.Factory stateMachineFactory;
    private final Provider<Aiming> aimingFactory;
    private final Provider<Idle> idleFactory;
    private final TextureAtlas.AtlasRegion fireRegion;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas,
            InputStateMachine.Factory stateMachineFactory,
            Provider<Aiming> aimingFactory,
            Provider<Idle> idleFactory) {
        this.textureAtlas = textureAtlas;
        this.stateMachineFactory = stateMachineFactory;
        this.aimingFactory = aimingFactory;
        this.idleFactory = idleFactory;
        fireRegion = textureAtlas.findRegion("cannon/fire");
    }

    public Entity background() {
        Entity entity = new Entity();
        entity.add(new ActorComponent(new Image(textureAtlas.findRegion("background")), Layer.BACKGROUND));
        return entity;
    }

    public List<Entity> turrets() {
        Array<TextureAtlas.AtlasRegion> images = textureAtlas.findRegions("cannon/turret");
        Array<Drawable> drawables = new Array<>(images.size);
        for (TextureAtlas.AtlasRegion image : images) {
            drawables.add(new TextureRegionDrawable(image));
        }
        float frameLength = 0.5f;
        float maxLength = frameLength * images.size;

        ObjectList<Entity> entities = new ObjectList<>();
        for (TurretSetup setup : TurretSetup.values()) {
            Entity entity = new Entity();
            Group actor = new Group();
            Image towerImage = new Image(textureAtlas.findRegion("cannon/tower-" + setup.ordinal()));
            actor.addActor(towerImage);
            actor.setPosition(setup.towerPosition.x, setup.towerPosition.y);
            AnimatedImage animatedImage = new AnimatedImage(new FixedFrameAnimation<>(frameLength, drawables));
            animatedImage.setPosition(setup.turretOffset.x, setup.turretOffset.y);
            animatedImage.setProgress(maxLength / 2f);
            animatedImage.setPlaying(false);
            actor.addActor(animatedImage);
            entity.add(new ActorComponent(actor, Layer.TURRETS));
            InputStateMachine stateMachine = stateMachineFactory.create(entity, null);
            Aiming aiming = aimingFactory.get();
            Idle idle = idleFactory.get();
            stateMachine.changeState(idle);
            Vector2 fireOrigin =
                    new Vector2(setup.towerPosition).add(setup.turretOffset).add(2, 5);
            entity.add(new Turret(
                    stateMachine, animatedImage, aiming, idle, maxLength, fireOrigin, setup.left, setup.right));
            entities.add(entity);
        }

        return entities;
    }

    private final Vector2 rotationVector = new Vector2(1, 1);

    public Entity bullet(float degrees, float x, float y) {
        Entity entity = new Entity();
        entity.add(new BulletComponent(degrees));
        Image image = new Image(fireRegion);
        image.setDebug(true);
        image.setPosition(x, y, Align.bottom);
        image.setOrigin(Align.bottom | Align.center);
        image.setRotation(degrees + 90);
        rotationVector
                .set(
                        MathUtils.cos(degrees * MathUtils.degreesToRadians),
                        MathUtils.sin(degrees * MathUtils.degreesToRadians))
                .nor()
                .scl(Config.Bullet.SPEED);
        image.addAction(Actions.forever(Actions.moveBy(rotationVector.x, rotationVector.y, 0.1f)));
        entity.add(new ActorComponent(image, Layer.BULLETS));

        return entity;
    }

    private enum TurretSetup {
        LEFT(Input.Keys.A, Input.Keys.S, new Vector2(19f, 5f), new Vector2(16.5f, 11f)),
        CENTER_LEFT(Input.Keys.D, Input.Keys.F, new Vector2(65f, 4f), new Vector2(16.5f, 11f)),
        CENTER_RIGHT(Input.Keys.G, Input.Keys.H, new Vector2(104f, 4f), new Vector2(11.5f, 12f)),
        RIGHT(Input.Keys.J, Input.Keys.K, new Vector2(143f, 4f), new Vector2(9.5f, 13.5f)),
        ;
        public final int left;
        public final int right;
        public final Vector2 towerPosition;
        public final Vector2 turretOffset;

        TurretSetup(int left, int right, Vector2 towerPosition, Vector2 turretOffset) {
            this.left = left;
            this.right = right;
            this.towerPosition = towerPosition;
            this.turretOffset = turretOffset;
        }
    }
}
