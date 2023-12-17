package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.route.Router;
import javax.inject.Inject;

public class LevelScreen extends ScreenAdapter {

    private final SoundController soundController;
    private final Router router;
    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;
    private final InputMultiplexer inputMultiplexer;

    private final Music music;
    private final Music quVoice;

    @Inject
    public LevelScreen(
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            SoundController soundController,
            Router router,
            AssetManager assetManager,
            InputMultiplexer inputMultiplexer) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;
        this.soundController = soundController;
        this.router = router;
        this.inputMultiplexer = inputMultiplexer;
        music = assetManager.get(
                "audio/music/612631__szegvari__techno-retro-trance-sample-short-cinematic-120bpm-music-surround.ogg");
        quVoice = assetManager.get("audio/sounds/voice/qu voice.mp3");
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.M) {
                if (music.isPlaying()) {
                    music.pause();
                } else {
                    music.play();
                }
                return true;
            }
            return super.keyDown(keycode);
        }
    };

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            router.goToLevel();
        }
    }

    @Override
    public void show() {
        engine.addEntity(entitiesFactory.fade());
        for (Entity background : entitiesFactory.background()) {
            engine.addEntity(background);
        }
        for (Entity turret : entitiesFactory.turrets()) {
            engine.addEntity(turret);
        }
        for (Entity city : entitiesFactory.cities()) {
            engine.addEntity(city);
        }
        engine.addEntity(entitiesFactory.score());

        inputMultiplexer.addProcessor(processor);
        soundController.play(music, 1f, true);
    }

    @Override
    public void hide() {
        music.stop();
        quVoice.stop();
        engine.removeAllEntities();
        engine.removeAllSystems();
        inputMultiplexer.removeProcessor(processor);
    }
}
