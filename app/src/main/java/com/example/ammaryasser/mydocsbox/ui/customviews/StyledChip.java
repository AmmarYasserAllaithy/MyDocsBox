/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.ui.customviews;

import android.content.Context;
import android.widget.CompoundButton;

import com.example.ammaryasser.mydocsbox.R;
import com.google.android.material.chip.Chip;


public class StyledChip extends Chip implements Chip.OnCheckedChangeListener {

//    public static List<CustomChip> selectedChips = new ArrayList<>();


    private StyledChip(Context context) {
        super(context);
    }

    public StyledChip(Context context, int id, String title, boolean isChecked) {
        this(context);

        setId(id);
        setText(title);
        setClickable(true);
        setCheckable(true);
        setRippleColorResource(R.color.colorPrimary);
        setCheckedIconVisible(false);
        reset();

        setOnCheckedChangeListener(this);

        if (isChecked) check();
    }


    public void check() {
        setChecked(true);
        setChipBackgroundColorResource(R.color.colorPrimary);
        setTextColor(getResources().getColor((R.color.white)));
    }

    public void uncheck() {
        setChecked(false);
        reset();
    }

    public StyledChip radius(int radius) {
        setChipCornerRadius(radius);
        return this;
    }

    public StyledChip disabled() {
        setOnCheckedChangeListener(null);
        return this;
    }


    private void reset() {
        setChipBackgroundColorResource(R.color.white);
        setTextColor(getResources().getColor(R.color.colorPrimary));
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        StyledChip styledChip = (StyledChip) buttonView;

        if (isChecked) styledChip.check();
        else styledChip.uncheck();
    }

}
