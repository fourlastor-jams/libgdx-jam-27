package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.harlequin.component.ActorComponent;
import javax.inject.Inject;

/**
 * Factory to create various entities: player, buildings, enemies..
 */
@ScreenScoped
public class EntitiesFactory {

    private static final float SCALE_XY = 1f / 32f;
    private final TextureAtlas textureAtlas;

    @Inject
    public EntitiesFactory(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public Entity background() {
        Entity entity = new Entity();
        entity.add(new ActorComponent(new Image(textureAtlas.findRegion("background")), Layer.BACKGROUND));
        return entity;
    }
}
