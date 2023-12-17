package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.CityComponent;
import javax.inject.Inject;

public class ShieldDown extends CityState {
    private final Sound sound;
    private final SoundController soundController;

    @Inject
    public ShieldDown(Mappers mappers, SoundController soundController, AssetManager assetManager) {
        super(mappers);
        this.soundController = soundController;
        sound = assetManager.get("audio/sounds/523745__matrixxx__armor-02.wav");
    }

    @Override
    public void enter(Entity entity) {
        Image shieldImage = city(entity).shieldImage;
        shieldImage.addAction(Actions.sequence(
                Actions.parallel(Actions.fadeOut(0.4f), Actions.color(Color.RED, 0.4f)),
                Actions.run(() -> shieldImage.setVisible(false))));

        soundController.play(sound);
    }

    @Override
    public void onHit(Entity entity) {
        CityComponent city = city(entity);
        city.stateMachine.changeState(city.destroyed);
    }
}
