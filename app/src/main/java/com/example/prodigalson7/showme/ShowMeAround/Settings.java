package com.example.prodigalson7.showme.ShowMeAround;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.prodigalson7.showme.Adapters.TargetsRecyclerAdapter;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ProdigaLsON7 on 11/03/2018.
 *
 */

public class Settings extends Dialog {

    Context context;                                                                      //The Activity
    private Settings mSettings = this;                                        //pointer to the Settings current object
    private TargetsRecyclerAdapter adapter;                         //recyclerview adapter

    //views references
    @BindView(R.id.coffeeCB)
    CheckBox coffeeCB;

    @BindView(R.id.barsCB)
    CheckBox barsCB;

    @BindView(R.id.restaurantCB)
    CheckBox restaurantCB;

    @BindView(R.id.dancebarsCB)
    CheckBox dancebarsCB;

    @BindView(R.id.gymCB)
    CheckBox gymCB;

    @BindView(R.id.spaCB)
    CheckBox spaCB;

    @BindView(R.id.atmCB)
    CheckBox atmCB;

    @BindView(R.id.cancelActionBtn)
    Button cancelActionBtn;

    @BindView(R.id.applyBtn)
    Button applyBtn;

    public Settings(@NonNull Context context, @NonNull TargetsRecyclerAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1. initialize view
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_layout);

        //2. Bind to butterknife
        ButterKnife.bind(this);                 //binding butterknife

        //3. assign the checkbox status
        loadSubjectStatus();
    }
    @OnClick({R.id.cancelActionBtn, R.id.applyBtn})
     public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.cancelActionBtn):
                mSettings.dismiss();
                break;
            default:
                saveChangesToSharedPref();
                mSettings.dismiss();
                ((MapsActivity) context).onResume();
                ((MapsActivity) context).getPresenter().fillMap();
                adapter.notifyDataSetChanged();
        }
    }

    //-------------------------------------------------------------------------TOOLS-----------------------------------------------------------------------
    private void loadSubjectStatus(){
        coffeeCB.setChecked(Util.getInstance().getSearchOptions().isCoffee());
        barsCB.setChecked(Util.getInstance().getSearchOptions().isBar());
        restaurantCB.setChecked(Util.getInstance().getSearchOptions().isRestaurant());
        dancebarsCB.setChecked(Util.getInstance().getSearchOptions().isDancebar());
        gymCB.setChecked(Util.getInstance().getSearchOptions().isPool());
        spaCB.setChecked(Util.getInstance().getSearchOptions().isSpa());
        atmCB.setChecked(Util.getInstance().getSearchOptions().isAtm());
    }

    private void saveChangesToSharedPref() {
        Context context = this.context.getApplicationContext();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    "mySharedPreferences", Context.MODE_PRIVATE);
            //read from sharedpreferences

            int spa = spaCB.isChecked() ? 1 : 0;
            int bar = barsCB.isChecked() ? 1 : 0;
            int gym = gymCB.isChecked() ? 1 : 0;
            int dancebar = dancebarsCB.isChecked() ? 1 : 0;
            int restaurant = restaurantCB.isChecked() ? 1 : 0;
            int coffee = coffeeCB.isChecked() ? 1 : 0;
            int atm = atmCB.isChecked() ? 1 : 0;
            //set Util
            Util.getInstance().getSearchOptions().setSpa(spaCB.isChecked());
            Util.getInstance().getSearchOptions().setBar(barsCB.isChecked());
            Util.getInstance().getSearchOptions().setPool(gymCB.isChecked());
            Util.getInstance().getSearchOptions().setDancebar(dancebarsCB.isChecked());
            Util.getInstance().getSearchOptions().setRestaurant(restaurantCB.isChecked());
            Util.getInstance().getSearchOptions().setCoffee(coffeeCB.isChecked());
            Util.getInstance().getSearchOptions().setAtm(atmCB.isChecked());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("spa", spa);
            editor.putInt("bar", bar);
            editor.putInt("gym", gym);
            editor.putInt("dancebar", dancebar);
            editor.putInt("restaurant", restaurant);
            editor.putInt("coffee", coffee);
            editor.putInt("atm", atm);
            editor.apply();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
        }
    }
    }
