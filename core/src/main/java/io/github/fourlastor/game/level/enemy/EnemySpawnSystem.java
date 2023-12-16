package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.TargetComponent;
import javax.inject.Inject;

public class EnemySpawnSystem extends IntervalSystem {
    private static final Family FAMILY_TARGETS =
            Family.all(TargetComponent.class).get();
    private static final Family FAMILY_ENEMIES =
            Family.all(EnemyComponent.class).get();
    private static final float INTERVAL = 2f;
    private static final int MAX_ENEMIES_TICK = 3;

    private final EntitiesFactory entitiesFactory;
    private final EnhancedRandom random;
    private final ComponentMapper<TargetComponent> targets;
    private ImmutableArray<Entity> targetEntities;
    private ImmutableArray<Entity> enemyEntities;
    private float accumulator = 0f;

    @Inject
    public EnemySpawnSystem(
            EntitiesFactory entitiesFactory, EnhancedRandom random, ComponentMapper<TargetComponent> targets) {
        super(INTERVAL);
        this.entitiesFactory = entitiesFactory;
        this.random = random;
        this.targets = targets;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        targetEntities = engine.getEntitiesFor(FAMILY_TARGETS);
        enemyEntities = engine.getEntitiesFor(FAMILY_ENEMIES);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        accumulator += deltaTime;
    }

    @Override
    protected void updateInterval() {
        int maxEnemies;
        if (accumulator > 90) {
            maxEnemies = 25;
        } else if (accumulator > 60) {
            maxEnemies = 20;
        } else if (accumulator > 30) {
            maxEnemies = 15;
        } else if (accumulator > 10) {
            maxEnemies = 10;
        } else {
            maxEnemies = 5;
        }
        int enemyCount = enemyEntities.size();
        for (int i = enemyCount, tick = 0; i < maxEnemies && tick <= MAX_ENEMIES_TICK; i++, tick++) {
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        int targetCount = targetEntities.size();
        if (targetCount > 0) {
            int cityIndex = random.nextInt(targetCount);
            TargetComponent target = targets.get(targetEntities.get(cityIndex));
            getEngine().addEntity(entitiesFactory.enemy(target));
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        enemyEntities = null;
        targetEntities = null;
        super.removedFromEngine(engine);
    }
}
