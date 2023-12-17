package io.github.fourlastor.game.level.particle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleActor extends Actor {

    private final ParticleEffect effect;
    private boolean active = false;

    public ParticleActor(ParticleEffect effect) {
        super();
        System.out.println("ACTOR!!!!");
        this.effect = effect;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            effect.reset(false);
        } else {
            effect.start();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (active) {
            float x = getX() * getScaleX();
            float y = getY() * getScaleY();
            effect.setPosition(x, y);
            effect.update(delta);
            if (effect.isComplete()) {
                effect.reset(false);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (active) {
            effect.draw(batch);
        }
    }
}
