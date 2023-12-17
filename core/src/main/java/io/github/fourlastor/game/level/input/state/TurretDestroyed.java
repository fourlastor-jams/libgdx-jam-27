package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.component.TurretComponent;
import javax.inject.Inject;

public class TurretDestroyed extends InputState {
    private final Sound explosionSound;
    private final SoundController soundController;

    @Inject
    public TurretDestroyed(Mappers mappers, AssetManager assetManager, SoundController soundController) {
        super(mappers);
        this.soundController = soundController;
        explosionSound = assetManager.get("audio/sounds/156031__iwiploppenisse__explosion.ogg");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        entity.remove(TargetComponent.class);
        TurretComponent turret = turret(entity);
        turret.animatedImage.setVisible(false);
        turret.towerImage.setVisible(false);
        turret.destroyedImage.setVisible(true);

        soundController.play(explosionSound);
    }

    @Override
    public void onHit(Entity entity) {}
}
