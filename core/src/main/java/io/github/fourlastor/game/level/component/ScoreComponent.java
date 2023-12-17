package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.github.tommyettinger.textra.TextraLabel;

public class ScoreComponent implements Component {

    public final TextraLabel label;

    public ScoreComponent(TextraLabel label) {
        this.label = label;
    }
}
