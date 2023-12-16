package io.github.fourlastor.game.level.bullet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import javax.inject.Inject;

public class BulletRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BulletComponent.class, PositionComponent.class).get();
    private static final Family FAMILY_ENEMIES =
            Family.all(EnemyComponent.class, PositionComponent.class).get();
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<EnemyComponent> enemies;
    private final Rectangle stageArea;
    private ImmutableArray<Entity> enemyEntities;

    @Inject
    public BulletRemovalSystem(
            ComponentMapper<PositionComponent> positions, ComponentMapper<EnemyComponent> enemies, Stage stageArea) {
        super(FAMILY);
        this.positions = positions;
        this.enemies = enemies;
        this.stageArea = new Rectangle(0, 0, stageArea.getWidth(), stageArea.getHeight());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        enemyEntities = engine.getEntitiesFor(FAMILY_ENEMIES);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        enemyEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 position = positions.get(entity).position;
        float x = position.x;
        float y = position.y;
        if (!stageArea.contains(x, y)) {
            getEngine().removeEntity(entity);
            return;
        }

        for (Entity enemy : enemyEntities) {
            Vector2 enemyPosition = positions.get(enemy).position;
            float headX = enemies.get(enemy).headX;
            float dst = position.dst(enemyPosition.x + headX, enemyPosition.y);
            if (dst < 8f) {
                getEngine().removeEntity(entity);
                getEngine().removeEntity(enemy);
            }
        }
    }
}
