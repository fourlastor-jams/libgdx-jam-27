package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.fourlastor.game.level.Config;
import javax.inject.Inject;

public class ClearScreenSystem extends EntitySystem {

    @Inject
    public ClearScreenSystem() {}

    @Override
    public void update(float deltaTime) {
        ScreenUtils.clear(Config.Screen.CLEAR_COLOR, true);
    }
}
