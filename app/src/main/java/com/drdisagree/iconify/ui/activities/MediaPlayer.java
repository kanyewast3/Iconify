package com.drdisagree.iconify.ui.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.drdisagree.iconify.R;
import com.drdisagree.iconify.config.Prefs;
import com.drdisagree.iconify.overlaymanager.MediaPlayerIconManager;
import com.drdisagree.iconify.ui.utils.ViewBindingHelpers;
import com.drdisagree.iconify.utils.AppUtil;
import com.drdisagree.iconify.utils.OverlayUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MediaPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        // Header
        ViewBindingHelpers.setHeader(this, findViewById(R.id.collapsing_toolbar), findViewById(R.id.toolbar), R.string.activity_title_media_player);

        refreshPreview();

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mp_accent = findViewById(R.id.mp_accent);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mp_system = findViewById(R.id.mp_system);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mp_pitch_black = findViewById(R.id.mp_pitch_black);

        mp_accent.setChecked(Prefs.getBoolean("IconifyComponentMPA.overlay"));

        mp_accent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mp_system.setChecked(false);
                mp_pitch_black.setChecked(false);
                OverlayUtil.disableOverlay("IconifyComponentMPS.overlay");
                OverlayUtil.disableOverlay("IconifyComponentMPB.overlay");
                OverlayUtil.enableOverlay("IconifyComponentMPA.overlay");
            } else {
                OverlayUtil.disableOverlay("IconifyComponentMPA.overlay");
            }
            refreshPreview();
        });

        mp_system.setChecked(Prefs.getBoolean("IconifyComponentMPS.overlay"));

        mp_system.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mp_accent.setChecked(false);
                mp_pitch_black.setChecked(false);
                OverlayUtil.disableOverlay("IconifyComponentMPA.overlay");
                OverlayUtil.disableOverlay("IconifyComponentMPB.overlay");
                OverlayUtil.enableOverlay("IconifyComponentMPS.overlay");
            } else {
                OverlayUtil.disableOverlay("IconifyComponentMPS.overlay");
            }
            refreshPreview();
        });

        mp_pitch_black.setChecked(Prefs.getBoolean("IconifyComponentMPB.overlay"));

        mp_pitch_black.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mp_accent.setChecked(false);
                mp_system.setChecked(false);
                OverlayUtil.disableOverlay("IconifyComponentMPA.overlay");
                OverlayUtil.disableOverlay("IconifyComponentMPS.overlay");
                OverlayUtil.enableOverlay("IconifyComponentMPB.overlay");
            } else {
                OverlayUtil.disableOverlay("IconifyComponentMPB.overlay");
            }
            refreshPreview();
        });
    }

    private void refreshPreview() {
        findViewById(R.id.preview_mp_accent).setVisibility(View.GONE);
        findViewById(R.id.preview_mp_black).setVisibility(View.GONE);
        findViewById(R.id.preview_mp_system).setVisibility(View.GONE);

        if (Prefs.getBoolean("IconifyComponentMPA.overlay"))
            findViewById(R.id.preview_mp_accent).setVisibility(View.VISIBLE);
        else if (Prefs.getBoolean("IconifyComponentMPB.overlay"))
            findViewById(R.id.preview_mp_black).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.preview_mp_system).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}