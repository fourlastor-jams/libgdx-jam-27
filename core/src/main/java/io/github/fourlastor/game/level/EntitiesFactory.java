package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.ds.ObjectList;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
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

    private static final float SCALE_XY = 1f / 32f;
    private final TextureAtlas textureAtlas;
    private final InputStateMachine.Factory stateMachineFactory;
    private final Provider<Aiming> aimingFactory;

    @Inject
    public EntitiesFactory(
            TextureAtlas textureAtlas, InputStateMachine.Factory stateMachineFactory, Provider<Aiming> aimingFactory) {
        this.textureAtlas = textureAtlas;
        this.stateMachineFactory = stateMachineFactory;
        this.aimingFactory = aimingFactory;
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
            AnimatedImage animatedImage = new AnimatedImage(new FixedFrameAnimation<>(frameLength, drawables));
            animatedImage.setPosition(setup.position.x, setup.position.y);
            animatedImage.setProgress(maxLength / 2f);
            animatedImage.setPlaying(false);
            entity.add(new ActorComponent(animatedImage, Layer.BACKGROUND));
            InputStateMachine stateMachine = stateMachineFactory.create(entity, null);
            Aiming aiming = aimingFactory.get();
            stateMachine.changeState(aiming);
            entity.add(new Turret(stateMachine, animatedImage, aiming, maxLength, setup.left, setup.right));
            entities.add(entity);
        }

        return entities;
    }

    private static final float TURRET_PADDING = 14f;

    private enum TurretSetup {
        LEFT(Input.Keys.A, Input.Keys.S, new Vector2(30f - TURRET_PADDING, 10f)),
        CENTER_LEFT(Input.Keys.D, Input.Keys.F, new Vector2(72f - TURRET_PADDING, 13f)),
        CENTER_RIGHT(Input.Keys.G, Input.Keys.H, new Vector2(116f - TURRET_PADDING, 10f)),
        RIGHT(Input.Keys.J, Input.Keys.K, new Vector2(152f - TURRET_PADDING, 16f)),
        ;
        public final int left;
        public final int right;
        public final Vector2 position;

        TurretSetup(int left, int right, Vector2 position) {
            this.left = left;
            this.right = right;
            this.position = position;
        }
    }
}
