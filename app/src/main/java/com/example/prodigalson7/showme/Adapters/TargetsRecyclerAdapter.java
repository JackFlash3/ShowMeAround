package com.example.prodigalson7.showme.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ProdigaLsON7 on 07/03/2018.
 */

public class TargetsRecyclerAdapter extends RecyclerView.Adapter{
    private LayoutInflater inflater;
    List<Target> data = null;
    ClickListener mClickListener;


    public TargetsRecyclerAdapter(Context context, List<Target> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.target_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //***view.setOnClickListener(mOnClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Target current = data.get(position);
        MyViewHolder mHolder = (MyViewHolder) holder;
        mHolder.setValues(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the Listener of the item clicks events
    public void setListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyLocation mLocation;
        ImageView subjectIV;
        TextView nameTV;
        TextView addressTV;
        RatingBar ratingRB;
        View itemView;
        ImageView photoIV;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            subjectIV = (ImageView) itemView.findViewById(R.id.subjectIV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            addressTV = (TextView) itemView.findViewById(R.id.addressTV);
            ratingRB = (RatingBar) itemView.findViewById(R.id.ratingRB);
            photoIV =  (ImageView) itemView.findViewById(R.id.photoIV);
            itemView.setOnClickListener(this);
        }

        //set the values inside the ViewHolder object
        public void setValues(Target target) {
            mLocation = target.getmLocation();
            nameTV.setText(target.getName());
            addressTV.setText(target.getAddress());
            ratingRB.setRating((float)target.getRating());
            setBusinessSubjectImage(target.getSubject());
            itemView.setTag(target.getPlaceID());
            photoIV.setImageBitmap(target.getPhoto());
        }

        //set the subject image
        private void setBusinessSubjectImage(Util.Subject subject) {

            //Bar, Gym, Restaurant, Dancebar, Spa
            switch (subject) {
                case Bar:
                    subjectIV.setImageResource(R.drawable.bar);
                    break;
                case Gym:
                    subjectIV.setImageResource(R.drawable.pool);
                    break;
                case Restaurant:
                    subjectIV.setImageResource(R.drawable.restaurant);
                    break;
                case Dancebar:
                    subjectIV.setImageResource(R.drawable.nightclub);
                    break;
                case Coffee:
                    subjectIV.setImageResource(R.drawable.coffee);
                    break;
                case Spa:
                    subjectIV.setImageResource(R.drawable.spa);
                    break;
                default:
                    subjectIV.setImageResource(R.drawable.atm);
            }
        }

        @Override
        public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.itemClicked(view, mLocation);
                }
        }
    }

    public interface ClickListener {
        public void itemClicked(View view, MyLocation location);
    }

    /*
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyLocation mLocation = null;
            if (mClickListener != null) {
                mClickListener.itemClicked(view, mLocation);
            }
        }
    };
    */
}
