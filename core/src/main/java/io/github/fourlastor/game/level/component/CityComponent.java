package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.level.city.CityStateMachine;
import io.github.fourlastor.game.level.city.state.CityDestroyed;
import io.github.fourlastor.game.level.city.state.ShieldDown;
import io.github.fourlastor.game.level.city.state.ShieldUp;
import io.github.fourlastor.game.level.particle.ParticleActor;

public class CityComponent implements Component {
    public final CityStateMachine stateMachine;
    public final ShieldUp shieldUp;
    public final ShieldDown shieldDown;
    public final CityDestroyed destroyed;
    public final Image shieldImage;
    public final Image cityImage;
    public final Image destroyedImage;
    public final ParticleActor fireEffect;

    public CityComponent(
            CityStateMachine stateMachine,
            ShieldUp shieldUp,
            ShieldDown shieldDown,
            CityDestroyed destroyed,
            Image shieldImage,
            Image cityImage,
            Image destroyedImage,
            ParticleActor fireEffect) {
        this.stateMachine = stateMachine;
        this.shieldUp = shieldUp;
        this.shieldDown = shieldDown;
        this.destroyed = destroyed;
        this.shieldImage = shieldImage;
        this.cityImage = cityImage;
        this.destroyedImage = destroyedImage;
        this.fireEffect = fireEffect;
    }
}
