package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.city.CityStateMachine;
import io.github.fourlastor.game.level.city.state.CityDestroyed;
import io.github.fourlastor.game.level.city.state.ShieldDown;
import io.github.fourlastor.game.level.city.state.ShieldUp;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.game.level.component.CityComponent;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.MovementComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.component.TurretComponent;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.game.level.input.state.TurretDestroyed;
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
    private final InputStateMachine.Factory inputStateMachineFactory;
    private final CityStateMachine.Factory cityStateMachineFactory;
    private final Provider<Aiming> aimingFactory;
    private final Provider<Idle> idleFactory;
    private final Provider<TurretDestroyed> turretDestroyedFactory;
    private final Provider<ShieldUp> shieldUpFactory;
    private final Provider<ShieldDown> shieldDownFactory;
    private final Provider<CityDestroyed> destroyedFactory;
    private final TextureAtlas.AtlasRegion fireRegion;
    private final EnhancedRandom random;
    private final Vector2 ceiling;
    private final Sound shieldUpSound;
    private final SoundController soundController;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas,
            InputStateMachine.Factory inputStateMachineFactory,
            CityStateMachine.Factory cityStateMachineFactory,
            Provider<Aiming> aimingFactory,
            Provider<Idle> idleFactory,
            Provider<TurretDestroyed> turretDestroyedFactory,
            Provider<ShieldUp> shieldUpFactory,
            Provider<ShieldDown> shieldDownFactory,
            Provider<CityDestroyed> destroyedFactory,
            EnhancedRandom random,
            Stage stage,
            SoundController soundController,
            AssetManager assetManager) {
        this.textureAtlas = textureAtlas;
        this.inputStateMachineFactory = inputStateMachineFactory;
        this.cityStateMachineFactory = cityStateMachineFactory;
        this.aimingFactory = aimingFactory;
        this.idleFactory = idleFactory;
        fireRegion = textureAtlas.findRegion("cannon/fire");
        this.turretDestroyedFactory = turretDestroyedFactory;
        this.shieldUpFactory = shieldUpFactory;
        this.shieldDownFactory = shieldDownFactory;
        this.destroyedFactory = destroyedFactory;
        this.random = random;
        ceiling = new Vector2(-2000, stage.getHeight());

        this.soundController = soundController;
        shieldUpSound = assetManager.get("audio/sounds/514851__matrixxx__armor-01.wav");
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
            Image towerImage = new Image(textureAtlas.findRegion("cannon/" + setup.turretImage));
            Image destroyedImage = new Image(textureAtlas.findRegion("cannon/" + setup.destroyedImage));
            destroyedImage.setPosition(setup.destroyedOffset.x, setup.destroyedOffset.y);
            destroyedImage.setVisible(false);
            actor.addActor(towerImage);
            actor.addActor(destroyedImage);
            actor.setPosition(setup.towerPosition.x, setup.towerPosition.y);
            AnimatedImage animatedImage = new AnimatedImage(new FixedFrameAnimation<>(frameLength, drawables));
            animatedImage.setPosition(setup.turretOffset.x, setup.turretOffset.y);
            animatedImage.setProgress(maxLength / 2f);
            animatedImage.setPlaying(false);
            actor.addActor(animatedImage);
            entity.add(new ActorComponent(actor, Layer.TURRETS));
            InputStateMachine stateMachine = inputStateMachineFactory.create(entity, null);
            Aiming aiming = aimingFactory.get();
            Idle idle = idleFactory.get();
            TurretDestroyed destroyed = turretDestroyedFactory.get();
            stateMachine.changeState(idle);
            Vector2 fireOrigin =
                    new Vector2(setup.towerPosition).add(setup.turretOffset).add(4, 5);
            entity.add(new TurretComponent(
                    stateMachine,
                    animatedImage,
                    towerImage,
                    destroyedImage,
                    aiming,
                    idle,
                    destroyed,
                    maxLength,
                    fireOrigin,
                    setup.left,
                    setup.right));
            entity.add(new TargetComponent(
                    new Vector2(setup.towerPosition).add(setup.turretOffset),
                    setup.minEnemyIndex,
                    setup.maxEnemyIndex));
            entities.add(entity);
        }

        return entities;
    }

    private final Vector2 rotationVector = new Vector2(1, 1);

    public List<Entity> cities() {
        ObjectList<Entity> entities = new ObjectList<>(CitySetup.values().length);
        for (CitySetup setup : CitySetup.values()) {
            Entity entity = new Entity();
            Group group = new Group();
            Image cityImage = new Image(textureAtlas.findRegion("cities/" + setup.image));
            Image shieldImage = new Image(textureAtlas.findRegion("cities/shield"));
            Image destroyedImage = new Image(textureAtlas.findRegion("cities/" + setup.destroyedImage));
            destroyedImage.setVisible(false);
            destroyedImage.setPosition(setup.destroyedPosition.x, setup.destroyedPosition.y);
            shieldImage.setOrigin(Align.top);
            shieldImage.setScale(1f, 0f);
            shieldImage.addAction(Actions.sequence(
                    Actions.delay(random.nextFloat(0.8f)),
                    Actions.run(() -> {
                        soundController.play(shieldUpSound);
                    }),
                    Actions.scaleTo(1f, 1f, 0.6f, Interpolation.exp10)));
            shieldImage.setPosition(setup.shieldPosition.x, setup.shieldPosition.y);
            group.setPosition(setup.position.x, setup.position.y);
            group.addActor(cityImage);
            group.addActor(destroyedImage);
            group.addActor(shieldImage);
            entity.add(new ActorComponent(group, Layer.CITIES));
            CityStateMachine stateMachine = cityStateMachineFactory.create(entity, null);
            ShieldUp shieldUp = shieldUpFactory.get();
            stateMachine.changeState(shieldUp);
            ShieldDown shieldDown = shieldDownFactory.get();
            CityDestroyed destroyed = destroyedFactory.get();
            entity.add(new CityComponent(
                    stateMachine, shieldUp, shieldDown, destroyed, shieldImage, cityImage, destroyedImage));
            entity.add(new TargetComponent(setup.center, setup.minEnemyIndex, setup.maxEnemyIndex));
            entities.add(entity);
        }
        return entities;
    }

    public Entity bullet(float degrees, float x, float y) {
        Entity entity = new Entity();
        entity.add(new BulletComponent(degrees));
        Image image = new Image(fireRegion);
        image.setPosition(x, y);
        image.setRotation(degrees + 90);
        Vector2 direction = rotationToVector(degrees).scl(Config.Bullet.SPEED);
        //        image.addAction(Actions.forever(Actions.moveBy(direction.x, direction.y, 0.1f)));
        entity.add(new ActorComponent(image, Layer.BULLETS));
        entity.add(new MovementComponent(new Vector2(direction), 0.1f));
        entity.add(new PositionComponent(new Vector2(x, y)));
        return entity;
    }

    public Entity enemy(TargetComponent targetComponent) {
        Entity entity = new Entity();
        int enemyIndex = random.nextInt(targetComponent.minEnemyIndex, targetComponent.maxEnemyIndex);
        EnemySetup enemySetup = EnemySetup.values()[enemyIndex];
        Vector2 direction = rotationToVector(enemySetup.angle);
        float moveX = direction.x;
        float moveY = direction.y;
        entity.add(new MovementComponent(new Vector2(moveX, moveY), 0.2f));
        Vector2 cityPos = targetComponent.hitTarget;
        // invert direction for intersection
        direction.scl(-1);
        float distance = Intersector.intersectRayRay(cityPos, direction, ceiling, Vector2.X);
        direction.scl(distance).add(cityPos);
        Image image = new Image(textureAtlas.findRegion("enemies/" + enemySetup.image));
        boolean movingRight = moveX > 0;
        float headX = 0f;
        if (movingRight) {
            headX = image.getWidth();
            // the "head" of the enemy is on the right corner of the image
        }
        direction.add(-headX, 0);
        image.setPosition(direction.x, direction.y);
        entity.add(new ActorComponent(image, Layer.ENEMIES));
        entity.add(new PositionComponent(new Vector2(direction)));
        entity.add(new EnemyComponent(headX));
        return entity;
    }

    private Vector2 rotationToVector(float degrees) {
        return rotationVector
                .set(
                        MathUtils.cos(degrees * MathUtils.degreesToRadians),
                        MathUtils.sin(degrees * MathUtils.degreesToRadians))
                .nor();
    }

    public Entity fade() {
        Entity entity = new Entity();
        Drawable darkness = new TextureRegionDrawable(textureAtlas.findRegion("whitePixel"))
                .tint(new Color(Config.Screen.CLEAR_COLOR));
        Image darknessImage = new Image(darkness);
        darknessImage.setSize(160f, 90f);
        darknessImage.addAction(
                Actions.sequence(Actions.fadeOut(0.5f), Actions.run(() -> darknessImage.setVisible(false))));
        entity.add(new ActorComponent(darknessImage, Layer.FADE));
        return entity;
    }

    private enum CitySetup {
        FIRST(
                new Vector2(10f, 5f),
                new Vector2(3f, 3f),
                new Vector2(-2.5f, 2f),
                "city-0",
                "destroyed-0",
                new Vector2(-5f, -5f),
                0,
                EnemySetup.values().length / 2),
        SECOND(
                new Vector2(58f, 4f),
                new Vector2(50f, 2f),
                new Vector2(-2.5f, 2f),
                "city-1",
                "destroyed-1",
                new Vector2(-5f, -5f),
                0,
                EnemySetup.values().length),
        THIRD(
                new Vector2(98f, 4f),
                new Vector2(90f, 2f),
                new Vector2(-2.5f, 2f),
                "city-0",
                "destroyed-2",
                new Vector2(-5f, -5f),
                0,
                EnemySetup.values().length),
        FOURTH(
                new Vector2(134f, 2f),
                new Vector2(127f, 0f),
                new Vector2(-2.5f, 2f),
                "city-1",
                "destroyed-0",
                new Vector2(-5f, -5f),
                EnemySetup.values().length / 2,
                EnemySetup.values().length),
        ;

        public final Vector2 center;
        public final Vector2 position;
        public final Vector2 shieldPosition;
        public final String image;
        public final String destroyedImage;
        public final Vector2 destroyedPosition;
        public final int minEnemyIndex;
        public final int maxEnemyIndex;

        CitySetup(
                Vector2 center,
                Vector2 position,
                Vector2 shieldPosition,
                String image,
                String destroyedImage,
                Vector2 destroyedPosition,
                int minEnemyIndex,
                int maxEnemyIndex) {
            this.center = center;
            this.position = position;
            this.shieldPosition = shieldPosition;
            this.image = image;
            this.destroyedImage = destroyedImage;
            this.destroyedPosition = destroyedPosition;
            this.minEnemyIndex = minEnemyIndex;
            this.maxEnemyIndex = maxEnemyIndex;
        }
    }

    private enum TurretSetup {
        LEFT(
                Input.Keys.A,
                Input.Keys.S,
                new Vector2(19f, 5f),
                new Vector2(16.5f, 11f),
                "tower-0",
                "destroyed-0",
                new Vector2(15f, 3f),
                0,
                EnemySetup.values().length / 2),
        CENTER_LEFT(
                Input.Keys.D,
                Input.Keys.F,
                new Vector2(65f, 4f),
                new Vector2(16.5f, 11f),
                "tower-1",
                "destroyed-1",
                new Vector2(15f, 3f),
                0,
                EnemySetup.values().length),
        CENTER_RIGHT(
                Input.Keys.G,
                Input.Keys.H,
                new Vector2(104f, 4f),
                new Vector2(11.5f, 12f),
                "tower-2",
                "destroyed-2",
                new Vector2(10f, 6f),
                0,
                EnemySetup.values().length),
        RIGHT(
                Input.Keys.J,
                Input.Keys.K,
                new Vector2(143f, 4f),
                new Vector2(9.5f, 13.5f),
                "tower-3",
                "destroyed-0",
                new Vector2(8f, 7f),
                EnemySetup.values().length / 2,
                EnemySetup.values().length),
        ;
        public final int left;
        public final int right;
        public final Vector2 towerPosition;
        public final Vector2 turretOffset;
        public final String turretImage;
        public final String destroyedImage;
        public final Vector2 destroyedOffset;
        public final int minEnemyIndex;
        public final int maxEnemyIndex;

        TurretSetup(
                int left,
                int right,
                Vector2 towerPosition,
                Vector2 turretOffset,
                String turretImage,
                String destroyedImage,
                Vector2 destroyedOffset,
                int minEnemyIndex,
                int maxEnemyIndex) {
            this.left = left;
            this.right = right;
            this.towerPosition = towerPosition;
            this.turretOffset = turretOffset;
            this.turretImage = turretImage;
            this.destroyedImage = destroyedImage;
            this.destroyedOffset = destroyedOffset;
            this.minEnemyIndex = minEnemyIndex;
            this.maxEnemyIndex = maxEnemyIndex;
        }
    }

    private enum EnemySetup {
        //        ENEMY_200("enemy-200", 200f),
        ENEMY_210("enemy-210", 210f),
        ENEMY_225("enemy-225", 225f),
        ENEMY_240("enemy-240", 240f),
        ENEMY_250("enemy-250", 250f),
        ENEMY_255("enemy-255", 255f),
        ENEMY_270("enemy-270", 270f),
        ENEMY_285("enemy-285", 285f),
        ENEMY_290("enemy-290", 290f),
        ENEMY_300("enemy-300", 300f),
        ENEMY_315("enemy-315", 315f),
        ENEMY_330("enemy-330", 330f),
    //        ENEMY_340("enemy-340", 340f),
    ;
        public final String image;
        public final float angle;

        EnemySetup(String image, float angle) {
            this.image = image;
            this.angle = angle;
        }
    }
}
