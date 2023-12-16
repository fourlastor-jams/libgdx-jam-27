package io.github.fourlastor.game.level.bullet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import javax.inject.Inject;

public class BulletRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BulletComponent.class, PositionComponent.class).get();
    private final ComponentMapper<PositionComponent> positions;
    private final Rectangle stageArea;

    @Inject
    public BulletRemovalSystem(ComponentMapper<PositionComponent> positions, Stage stageArea) {
        super(FAMILY);
        this.positions = positions;
        this.stageArea = new Rectangle(0, 0, stageArea.getWidth(), stageArea.getHeight());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 position = positions.get(entity).position;
        float x = position.x;
        float y = position.y;
        if (!stageArea.contains(x, y)) {
            getEngine().removeEntity(entity);
        }
    }
}
