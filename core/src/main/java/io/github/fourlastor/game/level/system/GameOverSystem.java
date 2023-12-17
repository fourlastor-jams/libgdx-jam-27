package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class GameOverSystem extends EntitySystem implements Telegraph {

    private static final Family FAMILY = Family.all(TargetComponent.class).get();
    private final MessageDispatcher messageDispatcher;
    private final EntitiesFactory entitiesFactory;
    private ImmutableArray<Entity> targetEntities;
    private boolean checkForEntities = false;

    @Inject
    public GameOverSystem(MessageDispatcher messageDispatcher, EntitiesFactory entitiesFactory) {
        this.messageDispatcher = messageDispatcher;
        this.entitiesFactory = entitiesFactory;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (checkForEntities) {
            if (targetEntities.size() == 0) {
                getEngine().addEntity(entitiesFactory.gameOver());
            }
            checkForEntities = false;
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        targetEntities = engine.getEntitiesFor(FAMILY);
        messageDispatcher.addListener(this, Message.TARGET_HIT.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        messageDispatcher.removeListener(this, Message.TARGET_HIT.ordinal());
        targetEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        checkForEntities = true;
        return true;
    }
}
