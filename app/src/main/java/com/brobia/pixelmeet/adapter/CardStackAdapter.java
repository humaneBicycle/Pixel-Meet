package com.brobia.pixelmeet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brobia.pixelmeet.NotifyUserToHomeActivity;
import com.brobia.pixelmeet.R;
import com.brobia.pixelmeet.model.NearbyUser;
import com.brobia.pixelmeet.model.NotifyUserToSwipeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardStackViewHolder>  {
    Context context;
    ArrayList<NearbyUser> nearbyUsers;
    NotifyUserToHomeActivity notifyUserToHomeActivity;
    NotifyUserToSwipeFragment notifyUserToSwipeFragment;

    public CardStackAdapter(Context context, ArrayList<NearbyUser> nearbyUsers, NotifyUserToHomeActivity notifyUserToHomeActivity,NotifyUserToSwipeFragment notifyUserToSwipeFragment){
        this.context = context;
        this.nearbyUsers = nearbyUsers;
        this.notifyUserToHomeActivity = notifyUserToHomeActivity;
        this.notifyUserToSwipeFragment = notifyUserToSwipeFragment;
    }

    @NonNull
    @Override
    public CardStackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_card_item_layout,parent,false);
        CardStackViewHolder cardStackViewHolder = new CardStackViewHolder(v);
        return cardStackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackViewHolder holder, int position) {
        NearbyUser user = nearbyUsers.get(position);
        holder.genderTV.setText(user.getGender());
        holder.bodyTypeTV.setText(user.getHairstyle());//wrong type
        //Picasso.get().load(user.getActivePlate()).into(holder.plate);
        holder.professionTV.setText(user.getProfession());
        holder.smokingTV.setText(user.getSmoking());
        holder.religionTV.setText(user.getReligion());
        holder.heightTV.setText(user.getHairstyle());//wrong type

        Log.d("pwd", "onBindViewHolder: "+ user.getName());

        //get called only when the topmost stack is visible. so i can
        notifyUserToHomeActivity.getCurrentUser(user);
        notifyUserToSwipeFragment.topUserNotification(user);

    }


    @Override
    public int getItemCount() {
        return nearbyUsers.size();
    }

    public class CardStackViewHolder extends RecyclerView.ViewHolder{

        TextView bodyTypeTV, professionTV, smokingTV, religionTV, heightTV, genderTV;
        ImageView plate;

        public CardStackViewHolder(@NonNull View itemView) {
            //super();
            super(itemView);

            bodyTypeTV = itemView.findViewById(R.id.text_body_type_swipe_frag);
            professionTV = itemView.findViewById(R.id.text_profession_swipe_frag);
            smokingTV = itemView.findViewById(R.id.text_smoking_swipe_frag);
            religionTV = itemView.findViewById(R.id.text_religion_swipe_frag);
            heightTV = itemView.findViewById(R.id.text_height_swipe_frag);
            genderTV = itemView.findViewById(R.id.text_gender_swipe_frag);
            plate = itemView.findViewById(R.id.plate_background_swipe_fragment);

        }
    }
}
