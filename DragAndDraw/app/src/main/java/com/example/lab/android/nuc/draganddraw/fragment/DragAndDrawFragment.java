package com.example.lab.android.nuc.draganddraw.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab.android.nuc.draganddraw.R;

public class DragAndDrawFragment extends Fragment {
    public static DragAndDrawFragment newInstance(){
        return new DragAndDrawFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_drag_and_draw,container,false );

        return view;
    }
}
