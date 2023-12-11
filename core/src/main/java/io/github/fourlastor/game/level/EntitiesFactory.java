package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import io.github.fourlastor.harlequin.component.ActorComponent;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
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

    public Entity turret() {
        Entity entity = new Entity();
        Array<TextureAtlas.AtlasRegion> images = textureAtlas.findRegions("cannon/turret");
        Array<Drawable> drawables = new Array<>(images.size);
        for (TextureAtlas.AtlasRegion image : images) {
            drawables.add(new TextureRegionDrawable(image));
        }
        float frameLength = 0.5f;
        float maxLength = frameLength * images.size;
        AnimatedImage animatedImage = new AnimatedImage(new FixedFrameAnimation<>(frameLength, drawables));
        animatedImage.setPosition(31f - 14f, 11f);
        animatedImage.setProgress(maxLength / 2f);
        animatedImage.setPlaying(false);
        entity.add(new ActorComponent(animatedImage, Layer.BACKGROUND));
        InputStateMachine stateMachine = stateMachineFactory.create(entity, null);
        Aiming aiming = aimingFactory.get();
        stateMachine.changeState(aiming);
        entity.add(new Turret(stateMachine, animatedImage, aiming, maxLength));
        return entity;
    }
}
