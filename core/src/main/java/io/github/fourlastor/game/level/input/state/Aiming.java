package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import io.github.fourlastor.game.level.Config;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.game.level.event.Message;
import io.github.fourlastor.game.level.event.SpawnBullet;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class Aiming extends InputState {

    private static final float AIM_SPEED = 30f;

    private final MessageDispatcher messageDispatcher;

    private final Pool<SpawnBullet> spawnBulletPool;

    private float fireTimer;

    @Inject
    public Aiming(Mappers mappers, MessageDispatcher messageDispatcher, Pool<SpawnBullet> spawnBulletPool) {
        super(mappers);
        this.messageDispatcher = messageDispatcher;
        this.spawnBulletPool = spawnBulletPool;
    }

    @Override
    public void enter(Entity entity) {
        fireTimer = 0f;
    }

    @Override
    public void update(Entity entity) {
        int direction = 0;
        Turret turret = turret(entity);
        if (Gdx.input.isKeyPressed(turret.left)) {
            direction -= 1;
        }
        if (Gdx.input.isKeyPressed(turret.right)) {
            direction += 1;
        }

        AnimatedImage animatedImage = turret.animatedImage;

        float delta = delta();
        fireTimer += delta;
        if (fireTimer >= Config.Turret.SHOOT_INTERVAL) {
            SpawnBullet spawnBullet = spawnBulletPool.obtain();
            spawnBullet.set(turret.fireOrigin.x, turret.fireOrigin.y, turret.angle);
            messageDispatcher.dispatchMessage(Message.SPAWN_BULLET.ordinal(), spawnBullet);
            fireTimer = 0f;
        }

        float progressDelta = delta * direction;
        float progress = MathUtils.clamp(animatedImage.playTime + progressDelta, 0f, turret.maxLength);
        animatedImage.setProgress(progress);
        turret.angle = MathUtils.clamp(turret.angle - progressDelta * Config.Turret.AIM_SPEED, 90 - 45f, 90 + 45f);
    }
}
