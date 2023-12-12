package io.github.fourlastor.game.level.bullet;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.event.Message;
import io.github.fourlastor.game.level.event.SpawnBullet;
import javax.inject.Inject;

public class BulletSpawnSystem extends EntitySystem implements Telegraph {

    private final MessageDispatcher dispatcher;
    private final EntitiesFactory entitiesFactory;
    private final Pool<SpawnBullet> spawnBulletPool;

    @Inject
    public BulletSpawnSystem(
            MessageDispatcher dispatcher, EntitiesFactory entitiesFactory, Pool<SpawnBullet> spawnBulletPool) {
        this.dispatcher = dispatcher;
        this.entitiesFactory = entitiesFactory;
        this.spawnBulletPool = spawnBulletPool;
    }

    @Override
    public void addedToEngine(Engine engine) {
        dispatcher.addListener(this, Message.SPAWN_BULLET.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        dispatcher.removeListener(this, Message.SPAWN_BULLET.ordinal());
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.SPAWN_BULLET.ordinal()) {
            SpawnBullet spawnBullet = (SpawnBullet) msg.extraInfo;
            getEngine()
                    .addEntity(
                            entitiesFactory.bullet(spawnBullet.angle, spawnBullet.location.x, spawnBullet.location.y));
            spawnBulletPool.free(spawnBullet);
            return true;
        }
        return false;
    }
}
