package com.ujwal.see;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InterestedAdapter extends RecyclerView.Adapter<InterestedAdapter.ViewHolder> {
    ArrayList<InterestedModel> interestedModelArrayList;
    Context context;

    public InterestedAdapter(ArrayList<InterestedModel> interestedModelArrayList, Context context) {
        this.interestedModelArrayList = interestedModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.report_item, parent, false);

        InterestedAdapter.ViewHolder viewHolder = new InterestedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final InterestedModel interestedModel = interestedModelArrayList.get(position);

        TextView user_name = holder.user_name;
        TextView phone_number = holder.phone_number;
        CircleImageView user_image_profile = holder.profile_image_user;

        Picasso.get().load(interestedModel.user_image).into(user_image_profile);
        user_name.setText(interestedModel.getUser_name());
        phone_number.setText(interestedModel.getMobile());

    }

    @Override
    public int getItemCount() {
        return interestedModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView user_name, phone_number;
        public CircleImageView profile_image_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.text_view_interested_name);
            phone_number = itemView.findViewById(R.id.text_view_interested_phone);
            profile_image_user = itemView.findViewById(R.id.profile_image_recycler);
        }
    }
}
