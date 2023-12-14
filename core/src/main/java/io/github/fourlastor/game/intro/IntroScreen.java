package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.inject.Inject;

public class IntroScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(0x333333ff);

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;

    private Array<Image> ground;
    private Array<Image> cityShields;
    private Array<Image> cities;
    private Array<Image> turrets;
    private Image space;
    private Image stars;
    private Image planets;

    @Inject
    public IntroScreen(
            InputMultiplexer inputMultiplexer,
            AssetManager assetManager,
            TextureAtlas textureAtlas
    ) {
        this.inputMultiplexer = inputMultiplexer;
        this.textureAtlas = textureAtlas;

        viewport = new FitViewport(160f, 90f);
        stage = new Stage(viewport);
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transitionToLevelScreen();
            return true;
        }

        @Override
        public boolean keyDown(int keycode) {
            transitionToLevelScreen();
            return super.keyDown(keycode);
        }
    };

    @Override
    public void show() {
        Image image = new Image();
        stage.addActor(image);
        image.addAction(Actions.sequence(Actions.delay(1f), Actions.run(() -> {
            imageSetup();
        })));
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(CLEAR_COLOR, true);
        stage.act(delta);
        stage.draw();
    }


    private void transitionToLevelScreen() {
        // TODO
    }

    private void imageSetup() { // TODO
        ground = new Array<>();
        ground.add(new Image(textureAtlas.findRegion("ground/layer_1")));

        for (Image image : ground)
            stage.addActor(image);

        /*
                cityShields
        cities
                turrets
        space
                stars
        planets*/
    }
}
