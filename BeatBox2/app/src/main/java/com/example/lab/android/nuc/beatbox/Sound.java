package com.example.lab.android.nuc.beatbox;

/**
 * Sound管理类
 * Created by 王浩 on 2018/3/30.
 */


public class Sound {

    private String mAssetPath;

    private String mName;

    //SoundPool加载音频文件时，都有自己的Integer的类型Id
    private Integer mSoundId;

    public Sound(String assetPath){
        mAssetPath = assetPath;
        //路径用"/"隔开，即分理出文件名字
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        //将文件名的后缀去掉
        mName = filename.replace(".wav","");
    }

    public String getAssetPath(){
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
