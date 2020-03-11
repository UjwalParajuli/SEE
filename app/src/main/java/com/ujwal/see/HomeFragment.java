package com.ujwal.see;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    TextView textView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textView = (TextView) view.findViewById(R.id.textView7);

        sharedPreferences = getContext().getSharedPreferences("LoginForm", MODE_PRIVATE);

        editorPreferences = sharedPreferences.edit();
        set();
        return view;

    }

    private void set(){
        String name = sharedPreferences.getString("full_name", null);
        textView.setText(name);
    }


}
