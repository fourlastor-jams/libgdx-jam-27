package io.github.fourlastor.game.di.modules;

import com.github.tommyettinger.random.ChopRandom;
import com.github.tommyettinger.random.EnhancedRandom;
import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    @Provides
    public EnhancedRandom random() {
        return new ChopRandom();
    }
}
