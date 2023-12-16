package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.Config;
import io.github.fourlastor.game.route.Router;
import javax.inject.Inject;

public class IntroScreen extends ScreenAdapter {

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;
    private final AssetManager assetManager;
    private final Router router;

    private Array<Image> ground;
    private Array<Image> cityShields;
    private Array<Image> cities;
    private Array<Image> turrets;
    private Image stars;
    private Image planets;

    private TypingLabel label;

    private Music introMusic;
    private Sound voiceOver;

    private SoundController soundController;
    private Image darknessImage;

    @Inject
    public IntroScreen(
            InputMultiplexer inputMultiplexer,
            AssetManager assetManager,
            TextureAtlas textureAtlas,
            SoundController soundController,
            Router router) {
        this.inputMultiplexer = inputMultiplexer;
        this.textureAtlas = textureAtlas;
        this.assetManager = assetManager;
        this.router = router;
        this.soundController = soundController;

        viewport = new FitViewport(160f, 90f);
        stage = new Stage(viewport);

        introMusic = assetManager.get("audio/music/241618__zagi2__dark-pulsing-intro.ogg", Music.class);
        soundController.play(introMusic, .75f, false);

        voiceOver = assetManager.get("audio/sounds/voice/intro voice over new.mp3", Sound.class);
        soundController.play(voiceOver, 1f);
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
            //            transitionToLevelScreen();
            if (keycode == Input.Keys.Q) Gdx.app.exit();
            return super.keyDown(keycode);
        }
    };

    @Override
    public void show() {
        setup();
        startAnimation();

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
        inputMultiplexer.removeProcessor(processor);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Config.Screen.CLEAR_COLOR, true);

        stage.act(delta);
        stage.draw();
    }

    private void transitionToLevelScreen() {
        darknessImage.addAction(Actions.sequence(
                Actions.run(() -> darknessImage.setVisible(true)), Actions.fadeIn(0.5f), Actions.run(() -> {
                    introMusic.stop();
                    voiceOver.stop();
                    router.goToLevel();
                })));
    }

    private void setup() { // TODO
        stars = new Image(textureAtlas.findRegion("background/stars"));
        stars.setY(-20f);
        stage.addActor(stars);

        float offset = -30f;
        planets = new Image(textureAtlas.findRegion("background/planets"));
        planets.setY(-31f + offset);
        stage.addActor(planets);

        BitmapFont font = assetManager.get("fonts/play-16.fnt");
        label = new TypingLabel(
                "{FADE}{SLOWER}From the cosmic abyss\n{WAIT=0.75}emerged an ancient {COLOR=#944e87}{SHAKE}terror",
                new Font(font));
        label.getFont().scale(.5f, .5f);
        label.setAlignment(Align.center);
        Table table = new Table();
        table.add(label).padBottom(Gdx.graphics.getHeight() * .025f);
        table.setFillParent(true);
        stage.addActor(table);

        ground = new Array<>();
        ground.add(new Image(textureAtlas.findRegion("ground/layer5")));
        ground.add(new Image(textureAtlas.findRegion("ground/layer4")));
        ground.add(new Image(textureAtlas.findRegion("ground/layer3")));
        ground.add(new Image(textureAtlas.findRegion("intro/layer2")));
        ground.add(new Image(textureAtlas.findRegion("intro/layer1")));

        ground.get(4).setY(-30f + offset); // foreground
        ground.get(3).setY(-17f + offset);
        ground.get(2).setY(-8f + offset);
        ground.get(1).setY(0f + offset);
        ground.get(0).setY(10f + offset); // background

        for (Image layer : ground) layer.moveBy(0f, -66f);

        for (Image image : ground) stage.addActor(image);
        Drawable darkness = new TextureRegionDrawable(textureAtlas.findRegion("whitePixel"))
                .tint(new Color(Config.Screen.CLEAR_COLOR));
        darknessImage = new Image(darkness);
        darknessImage.setSize(stage.getWidth(), stage.getHeight());
        darknessImage.getColor().a = 0f;
        darknessImage.setVisible(false);
        stage.addActor(darknessImage);
    }

    private void startAnimation() {
        float durationOffset = 5f;
        float offset = 27f;
        float delay = 5f;
        ground.get(0)
                .addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.moveBy(0f, 31f + offset, 5f + durationOffset, Interpolation.fastSlow)));

        ground.get(1)
                .addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.moveBy(0f, 41f + offset, 7f + durationOffset, Interpolation.fastSlow)));
        ground.get(2)
                .addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.moveBy(0f, 49f + offset, 9f + durationOffset, Interpolation.fastSlow)));
        ground.get(3)
                .addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.moveBy(0f, 58f + offset, 11f + durationOffset, Interpolation.fastSlow)));
        ground.get(4)
                .addAction(Actions.sequence(
                        Actions.delay(delay),
                        Actions.moveBy(0f, 70f + offset, 13f + durationOffset, Interpolation.fastSlow)));

        planets.addAction(Actions.moveBy(0f, 5f + offset, 13f + durationOffset, Interpolation.fastSlow));
        stars.addAction(Actions.moveBy(0f, 2f + offset, 13f + durationOffset, Interpolation.fastSlow));

        stars.addAction(Actions.sequence(Actions.delay(23f), Actions.run(() -> {
            soundController.play(introMusic, 1f, false);
        })));

        label.addAction(Actions.sequence(Actions.delay(8f), Actions.run(() -> {
            label.setText("{FADE}{SLOWER}Our world was forcibly reshaped\n in {COLOR=#ebe5ab}their image{CLEARCOLOR}. "
                    + "{WAIT=0.75}Abolishing sapient life.");
            label.restart();
        })));
        label.addAction(Actions.sequence(Actions.delay(17f), Actions.run(() -> {
            label.setText("{FADE}{SLOWER}{SHAKE}we're all that's left{WAIT=0.75}\n"
                    + "and this{WAIT=0.75} is our {ENDSHAKE}{COLOR=#a1234d}last {WAIT=0.75}stand.");
            label.restart();
        })));
        ground.first().addAction(Actions.sequence(Actions.delay(30f), Actions.run(() -> {
            transitionToLevelScreen();
        })));
    }
}
