package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class CityComponent implements Component {
    public final Rectangle area;

    public CityComponent(Rectangle area) {
        this.area = area;
    }
}
