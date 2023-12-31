package io.github.fourlastor.game.level.bullet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.BulletComponent;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class BulletRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(BulletComponent.class, PositionComponent.class).get();
    private static final Family FAMILY_ENEMIES =
            Family.all(EnemyComponent.class, PositionComponent.class).get();

    private final MessageDispatcher messageDispatcher;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<EnemyComponent> enemies;
    private final Rectangle stageArea;
    private ImmutableArray<Entity> enemyEntities;

    private final Sound explosionSound;
    private final SoundController soundController;

    @Inject
    public BulletRemovalSystem(
            MessageDispatcher messageDispatcher,
            ComponentMapper<PositionComponent> positions,
            ComponentMapper<EnemyComponent> enemies,
            Stage stage,
            SoundController soundController,
            AssetManager assetManager) {
        super(FAMILY);
        this.messageDispatcher = messageDispatcher;
        this.positions = positions;
        this.enemies = enemies;
        this.stageArea = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());
        this.soundController = soundController;

        explosionSound = assetManager.get("audio/sounds/399303__deleted_user_5405837__explosion_012.ogg");
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
            if (dst < 2f) {
                soundController.play(explosionSound, .2f, MathUtils.random(.8f, 1.2f));
                getEngine().removeEntity(entity);
                getEngine().removeEntity(enemy);
                messageDispatcher.dispatchMessage(Message.SCORE_INCREASE.ordinal());
            }
        }
    }
}
