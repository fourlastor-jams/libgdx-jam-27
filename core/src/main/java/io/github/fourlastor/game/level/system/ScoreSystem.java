package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.component.ScoreComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class ScoreSystem extends EntitySystem implements Telegraph {

    private static final Family FAMILY = Family.all(ScoreComponent.class).get();
    private final MessageDispatcher messageDispatcher;
    private final ComponentMapper<ScoreComponent> scores;
    private ImmutableArray<Entity> scoreEntities;
    private int score;

    @Inject
    public ScoreSystem(MessageDispatcher messageDispatcher, ComponentMapper<ScoreComponent> scores) {
        this.messageDispatcher = messageDispatcher;
        this.scores = scores;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        score = 0;
        scoreEntities = engine.getEntitiesFor(FAMILY);
        messageDispatcher.addListener(this, Message.SCORE_INCREASE.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        messageDispatcher.removeListener(this, Message.SCORE_INCREASE.ordinal());
        scoreEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        score += 1;
        for (Entity entity : scoreEntities) {
            scores.get(entity).label.setText("Score: " + score);
        }
        return true;
    }
}
