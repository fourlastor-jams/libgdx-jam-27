package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class Aiming extends InputState {
    @Inject
    public Aiming(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void update(Entity entity) {
        int direction = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction += 1;
        }

        Turret turret = turret(entity);
        AnimatedImage animatedImage = turret.animatedImage;

        System.out.println(animatedImage.playTime);

        if (direction == 0) return;

        float delta = delta() * direction;
        System.out.println(delta);
        float progress = MathUtils.clamp(animatedImage.playTime + delta, 0f, turret.maxLength);
        System.out.println(
                "From " + animatedImage.playTime + "to " + progress + " - between 0 and " + turret.maxLength);
        System.out.println(progress);
        animatedImage.setProgress(progress);
    }
}
