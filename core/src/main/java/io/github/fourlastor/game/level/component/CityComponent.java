package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.level.city.CityStateMachine;
import io.github.fourlastor.game.level.city.state.Destroyed;
import io.github.fourlastor.game.level.city.state.ShieldDown;
import io.github.fourlastor.game.level.city.state.ShieldUp;

public class CityComponent implements Component {
    public final Rectangle area;
    public final CityStateMachine stateMachine;
    public final ShieldUp shieldUp;
    public final ShieldDown shieldDown;
    public final Destroyed destroyed;
    public final Image shieldImage;
    public final Image cityImage;
    public final Image destroyedImage;

    public CityComponent(
            Rectangle area,
            CityStateMachine stateMachine,
            ShieldUp shieldUp,
            ShieldDown shieldDown,
            Destroyed destroyed,
            Image shieldImage,
            Image cityImage,
            Image destroyedImage) {
        this.area = area;
        this.stateMachine = stateMachine;
        this.shieldUp = shieldUp;
        this.shieldDown = shieldDown;
        this.destroyed = destroyed;
        this.shieldImage = shieldImage;
        this.cityImage = cityImage;
        this.destroyedImage = destroyedImage;
    }
}
