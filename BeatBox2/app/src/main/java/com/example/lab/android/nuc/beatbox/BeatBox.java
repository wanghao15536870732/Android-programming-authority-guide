package com.example.lab.android.nuc.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐管理类
 */
public class BeatBox {
    //添加常量，第一个是为了用于日志记录
    private static final String TAG = "BeatBox";

    //第二个是用于声音资源文件目录名
    private static final String SOUNDS_FOLDER = "sample_sounds";

    ///最多同时破防5个语音
    private static final int MAX_SOUNDS = 5;

    //获取AssetManager备用
    private AssetManager mAssets;
    
    //创建数组存放音乐名字
    private List<Sound> mSounds = new ArrayList<>();

    //创建SoundPool对象，进行音频的播放
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssets = context.getAssets();

        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC,0);

        loadSounds();
    }

    void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    ///最后不要忘了释放mSoundPool
    public void release(){
        mSoundPool.release();
    }

    //取得assets中的资源清单，使用List(String)方法
    // 实现一个loadSounds()方法,调用他给出声音文件清单
    private void loadSounds(){
        String[] soundNames ;
        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG,"Found " + soundNames.length + "sounds");
        } catch (IOException e) {
            Log.e(TAG,"Gould not list assets",e);
            return;
        }
        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                //预加载音乐
                load(sound);
                mSounds.add(sound);
            }catch (IOException e){
                Log.e(TAG,"Could not load sound" + filename,e);
            }
        }
    }

    //载入音乐
    private void load(Sound sound) throws IOException{

        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        //载入全部音乐
        int soundId = mSoundPool.load(afd,1);
        //传入设置Id
        sound.setSoundId(soundId);
    }

    //创建方法返回音乐数组
    public List<Sound> getSounds(){
        return mSounds;
    }
}

