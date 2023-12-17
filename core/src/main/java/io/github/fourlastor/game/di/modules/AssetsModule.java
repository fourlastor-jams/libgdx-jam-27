package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.harlequin.animation.AnimationNode;
import io.github.fourlastor.harlequin.loader.dragonbones.DragonBonesLoader;
import io.github.fourlastor.ldtk.LdtkLoader;
import io.github.fourlastor.ldtk.model.LdtkMapData;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class AssetsModule {

    private static final String PATH_TEXTURE_ATLAS = "images/packed/images.pack.atlas";
    public static final String WHITE_PIXEL = "white-pixel";
    public static final String EFFECTS_CASINGS = "effects/casings.pfx";
    public static final String EFFECTS_GROUND_FIRE_NARROW = "effects/ground_fire_narrow.pfx";
    public static final String EFFECTS_GROUND_FIRE_WIDE = "effects/ground_fire_wide.pfx";

    @Provides
    public DragonBonesLoader dragonBonesLoader(JsonReader json) {
        return new DragonBonesLoader();
    }

    @Provides
    @Singleton
    public AssetManager assetManager(LdtkLoader ldtkLoader, DragonBonesLoader dragonBonesLoader) {
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(LdtkMapData.class, ldtkLoader);
        assetManager.setLoader(AnimationNode.Group.class, dragonBonesLoader);
        assetManager.load(PATH_TEXTURE_ATLAS, TextureAtlas.class);
        ParticleEffectLoader.ParticleEffectParameter particleOptions =
                new ParticleEffectLoader.ParticleEffectParameter();
        particleOptions.atlasFile = PATH_TEXTURE_ATLAS;
        assetManager.load(EFFECTS_CASINGS, ParticleEffect.class, particleOptions);
        assetManager.load(EFFECTS_GROUND_FIRE_NARROW, ParticleEffect.class, particleOptions);
        assetManager.load(EFFECTS_GROUND_FIRE_WIDE, ParticleEffect.class, particleOptions);

        BitmapFontLoader.BitmapFontParameter fontParameter = new BitmapFontLoader.BitmapFontParameter();
        fontParameter.atlasName = PATH_TEXTURE_ATLAS;
        assetManager.load("fonts/play-16.fnt", BitmapFont.class, fontParameter);
        assetManager.load("fonts/quan-pixel-8.fnt", BitmapFont.class, fontParameter);

        assetManager.load("audio/music/241618__zagi2__dark-pulsing-intro.ogg", Music.class);

        assetManager.load("audio/sounds/156031__iwiploppenisse__explosion.ogg", Sound.class);
        assetManager.load("audio/sounds/514851__matrixxx__armor-01.wav", Sound.class);
        assetManager.load("audio/sounds/523745__matrixxx__armor-02.wav", Sound.class);
        assetManager.load("audio/sounds/voice/intro voice over new.mp3", Music.class);
        assetManager.load("audio/sounds/voice/qu voice.mp3", Music.class);
        assetManager.load("audio/sounds/128299__xenonn__layered-gunshot-5.ogg", Sound.class);
        assetManager.load("audio/sounds/399303__deleted_user_5405837__explosion_012.ogg", Sound.class);
        assetManager.load(
                "audio/music/612631__szegvari__techno-retro-trance-sample-short-cinematic-120bpm-music-surround.ogg",
                Music.class);

        assetManager.finishLoading();
        return assetManager;
    }

    @Provides
    @Singleton
    public TextureAtlas textureAtlas(AssetManager assetManager) {
        return assetManager.get(PATH_TEXTURE_ATLAS, TextureAtlas.class);
    }

    @Provides
    @Singleton
    @Named(WHITE_PIXEL)
    public TextureRegion whitePixel(TextureAtlas atlas) {
        return atlas.findRegion("whitePixel");
    }
}
