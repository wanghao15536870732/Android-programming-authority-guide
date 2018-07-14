package com.example.lab.android.nuc.beatbox;

import android.support.v4.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.StructPollfd;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.List;


public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //fragment的retainInstance属性默认的是faluse,这表民更不会被保留
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beat_box,container,false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_beat_box_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        return view;
    }

    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater,ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound,container,false));
            mButton = (Button) itemView.findViewById(R.id.list_ite_sound_button);
            mButton.setOnClickListener(this);
        }

        //绑定Sound
        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(View v) {
            mBeatBox.play(mSound);
        }

    }


    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }
}
