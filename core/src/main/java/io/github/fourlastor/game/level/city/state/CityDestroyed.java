package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.TargetComponent;
import javax.inject.Inject;

public class CityDestroyed extends CityState {
    private final Sound explosionSound;
    private final SoundController soundController;

    @Inject
    public CityDestroyed(Mappers mappers, SoundController soundController, AssetManager assetManager) {
        super(mappers);
        this.soundController = soundController;
        explosionSound = assetManager.get("audio/sounds/156031__iwiploppenisse__explosion.ogg");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        city(entity).cityImage.setVisible(false);
        city(entity).destroyedImage.setVisible(true);
        entity.remove(TargetComponent.class);

        soundController.play(explosionSound);
    }

    @Override
    public void onHit(Entity entity) {}
}