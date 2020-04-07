package com.ujwal.see;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    CircleImageView profile_image;
    TextView text_user_name, text_user_type, text_total_attended, text_total_organized, text_about_us, text_help, text_logout;
    Button button_edit_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Your Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = (CircleImageView)view.findViewById(R.id.profile_image);
        text_user_name = (TextView)view.findViewById(R.id.text_view_user_name);
        text_user_type = (TextView)view.findViewById(R.id.text_view_user_type);
        text_total_attended = (TextView)view.findViewById(R.id.text_view_count_attended);
        text_total_organized = (TextView)view.findViewById(R.id.text_view_organized);
        text_about_us = (TextView)view.findViewById(R.id.text_view_about_us);
        text_help = (TextView)view.findViewById(R.id.text_view_help);
        text_logout = (TextView)view.findViewById(R.id.text_view_logout);
        button_edit_profile = (Button)view.findViewById(R.id.button_edit_profile);



        return view;
    }
}
