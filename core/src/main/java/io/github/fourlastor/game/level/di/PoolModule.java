package io.github.fourlastor.game.level.di;

import com.badlogic.gdx.utils.Pool;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.event.SpawnBullet;

@Module
public class PoolModule {

    @Provides
    @ScreenScoped
    public Pool<SpawnBullet> spawnBulletPool() {
        return new Pool<SpawnBullet>() {
            @Override
            protected SpawnBullet newObject() {
                return new SpawnBullet();
            }
        };
    }
}
