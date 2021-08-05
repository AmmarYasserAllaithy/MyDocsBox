/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 */

package com.example.ammaryasser.mydocsbox.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.databinding.SettingLayoutBinding;


public class SettingLayout extends ConstraintLayout {

    private static final String TAG = "SettingLayout";

    private SettingLayoutBinding binding;
    private int iconResId;
    private String key, value;

    private SettingClickListener listener;


    public SettingLayout(@NonNull Context context) {
        super(context);

        init(null);
    }

    public SettingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public SettingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }


    //region: Getter & Setter
    public int getIconResId() {
        return iconResId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
        binding.iconIv.setImageResource(iconResId);
    }

    public void setKey(String key) {
        this.key = key;
        binding.keyTv.setText(key);
    }

    public void setValue(String value) {
        this.value = value;
        binding.valueTv.setText(value);
    }
    //endregion


    private void init(AttributeSet attrs) {
        binding = SettingLayoutBinding.inflate(LayoutInflater.from(getContext()), this);

        setClickable(true);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SettingLayout);

        setIconResId(ta.getResourceId(R.styleable.SettingLayout_icon, -1));
        setKey(ta.getString(R.styleable.SettingLayout_key));
        setValue(ta.getString(R.styleable.SettingLayout_value));

        ta.recycle();
    }


    @Override
    public boolean performClick() {
        if (!super.performClick()) listener.onSettingClicked();

        return true;
    }

    public void setOnSettingClicked(SettingClickListener listener) {
        this.listener = listener;
    }


    public interface SettingClickListener {
        void onSettingClicked();
    }

}
