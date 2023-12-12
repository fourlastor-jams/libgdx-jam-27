package io.github.fourlastor.game.level.bullet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.harlequin.component.ActorComponent;
import javax.inject.Inject;

public class BulletRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BulletComponent.class, ActorComponent.class).get();
    private final ComponentMapper<ActorComponent> actors;
    private final Rectangle stageArea;

    @Inject
    public BulletRemovalSystem(ComponentMapper<ActorComponent> actors, Stage stageArea) {
        super(FAMILY);
        this.actors = actors;
        this.stageArea = new Rectangle(0, 0, stageArea.getWidth(), stageArea.getHeight());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Actor actor = actors.get(entity).actor;
        float x = actor.getX();
        float y = actor.getY();
        if (!stageArea.contains(x, y)) {
            getEngine().removeEntity(entity);
        }
    }
}
