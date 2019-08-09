package com.sisipho.ngamlana.travelmantics;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealAdapterViewHolder> {

    ArrayList<TravelDeal> travelDealArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    public DealAdapter(MainActivity activity) {
        FireBaseUtil.openFireBaseReference("traveldeals", activity);
        firebaseDatabase = FireBaseUtil.firebaseDatabase;
        databaseReference = FireBaseUtil.databaseReference;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                travelDeal.setId(dataSnapshot.getKey());
                travelDealArrayList.add(travelDeal);
                notifyItemInserted(travelDealArrayList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
        this.travelDealArrayList = FireBaseUtil.travelDealList;

    }

    @NonNull
    @Override
    public DealAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = inflator.inflate(R.layout.item_layout, parent, false);
        return new DealAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapterViewHolder holder, int position) {
        TravelDeal travelDeal = travelDealArrayList.get(position);
        holder.textViewTitle.setText(travelDeal.getTitle());
        holder.textViewDescription.setText(travelDeal.getDescription());
        holder.textViewPrice.setText(travelDeal.getPrice());
        holder.showImage(travelDeal.getImageUrl());

    }

    @Override
    public int getItemCount() {
        return travelDealArrayList.size();
    }

    public class DealAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPrice;
        ImageView imageDealImage;

        public DealAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_item_title);
            textViewDescription = itemView.findViewById(R.id.textView_item_description);
            imageDealImage = itemView.findViewById(R.id.imageView_list_image);
            textViewPrice = itemView.findViewById(R.id.textView_item_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            TravelDeal selectedDeal = travelDealArrayList.get(position);
            Intent intent = new Intent(itemView.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }


        private void showImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(imageDealImage.getContext())
                        .load(imageUrl)
                        .resize(240, 240)
                        .centerCrop()
                        .into(imageDealImage);
            }
        }
    }
}
