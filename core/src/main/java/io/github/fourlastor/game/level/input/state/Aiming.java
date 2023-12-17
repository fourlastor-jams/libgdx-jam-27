package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.Config;
import io.github.fourlastor.game.level.component.TurretComponent;
import io.github.fourlastor.game.level.event.Message;
import io.github.fourlastor.game.level.event.SpawnBullet;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class Aiming extends InputState {

    private static final float MIN_ANGLE = 90 - 45f;
    private static final float MAX_ANGLE = 90 + 45f;
    private final MessageDispatcher messageDispatcher;

    private final Pool<SpawnBullet> spawnBulletPool;

    private final Sound fireSound;
    private final SoundController soundController;

    private float fireTimer;
    private long soundId = -1;

    @Inject
    public Aiming(
            Mappers mappers,
            MessageDispatcher messageDispatcher,
            Pool<SpawnBullet> spawnBulletPool,
            AssetManager assetManager,
            SoundController soundController) {
        super(mappers);
        this.messageDispatcher = messageDispatcher;
        this.spawnBulletPool = spawnBulletPool;
        this.soundController = soundController;

        fireSound = assetManager.get("audio/sounds/128299__xenonn__layered-gunshot-5.ogg");
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        fireTimer = 0f;
        soundId = soundController.loop(fireSound, 1f, MathUtils.random(.9f, 1.1f));
        turret(entity).casings.setActive(true);
    }

    @Override
    public void exit(Entity entity) {
        turret(entity).casings.setActive(false);
        if (soundId > 0) {
            soundController.stop(fireSound, soundId);
            soundId = -1;
        }
        super.exit(entity);
    }

    @Override
    public void update(Entity entity) {
        int direction = 0;
        TurretComponent turret = turret(entity);
        if (Gdx.input.isKeyPressed(turret.left)) {
            direction -= 1;
        }
        if (Gdx.input.isKeyPressed(turret.right)) {
            direction += 1;
        }

        AnimatedImage animatedImage = turret.animatedImage;

        if (direction == 0) {
            turret.stateMachine.changeState(turret.idle);
            return;
        }
        float delta = delta();
        fireTimer += delta;
        if (fireTimer >= Config.Turret.SHOOT_INTERVAL) {
            SpawnBullet spawnBullet = spawnBulletPool.obtain();
            spawnBullet.set(turret.fireOrigin.x, turret.fireOrigin.y, turret.angle);
            messageDispatcher.dispatchMessage(Message.SPAWN_BULLET.ordinal(), spawnBullet);
            fireTimer = 0f;
        }

        float progressDelta = delta * direction;
        turret.angle = MathUtils.clamp(turret.angle - progressDelta * Config.Turret.AIM_SPEED, MIN_ANGLE, MAX_ANGLE);
        float percent = 1f - (turret.angle - MIN_ANGLE) / MAX_ANGLE;
        float progress = MathUtils.clamp(turret.maxLength * percent, 0f, turret.maxLength);
        animatedImage.setProgress(progress);
    }
}
