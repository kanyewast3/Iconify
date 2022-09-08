package com.drdisagree.iconify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.topjohnwu.superuser.Shell;

import org.w3c.dom.Text;

import java.util.List;

public class MonetColor extends AppCompatActivity implements ColorPickerDialogListener {

    List<String> accent_color = Shell.cmd("settings get secure monet_engine_color_override").exec().getOut();
    int color = initialize_color();
    List<String> accent_color_check = Shell.cmd("settings get secure monet_engine_custom_color").exec().getOut();
    int acc = initialize_accent();
    List<String> chroma_fact = Shell.cmd("settings get secure monet_engine_chroma_factor").exec().getOut();
    int chroma = initialize_chroma();
    List<String> white_lum = Shell.cmd("settings get secure monet_engine_white_luminance_user").exec().getOut();
    int luminance = initialize_luminance();
    List<String> accurate_sh = Shell.cmd("settings get secure monet_engine_accurate_shades").exec().getOut();
    int shade = initialize_shade();
    List<String> linear_light = Shell.cmd("settings get secure monet_engine_linear_lightness").exec().getOut();
    int linear = initialize_linear();

    GradientDrawable drawable = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{color, color});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monet_color);

        // Header
        TextView header = findViewById(R.id.header);
        header.setText("Color Engine");

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

        drawable.setCornerRadius(120f);
        findViewById(R.id.preview_primary_accent).setBackgroundDrawable(drawable);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch experimental_color = findViewById(R.id.experimental_color);
        LinearLayout experimental_color_options = findViewById(R.id.experimental_color_options);

        if (!PrefConfig.loadPrefBool(this, "experimentalColorOptions")) {
            experimental_color.setChecked(false);
            experimental_color_options.setVisibility(View.GONE);
        } else {
            experimental_color.setChecked(true);
            experimental_color_options.setVisibility(View.VISIBLE);
        }

        experimental_color.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    experimental_color_options.setVisibility(View.VISIBLE);
                    PrefConfig.savePrefBool(MonetColor.this, "experimentalColorOptions", true);
                } else {
                    experimental_color_options.setVisibility(View.GONE);
                    PrefConfig.savePrefBool(MonetColor.this, "experimentalColorOptions", false);
                }
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch apply_monet_accent = findViewById(R.id.apply_monet_accent);

        List<String> overlays = OverlayUtils.getOverlayList();
        if (!OverlayUtils.isOverlayEnabled(overlays, "IconifyComponentAMA.overlay")) {
            apply_monet_accent.setChecked(false);
        } else {
            apply_monet_accent.setChecked(true);
        }

        apply_monet_accent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OverlayUtils.enableOverlay(OverlayUtils.getOverlayList(), "IconifyComponentAMA.overlay");
                } else {
                    OverlayUtils.disableOverlay("IconifyComponentAMA.overlay");
                }
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enable_custom_accent = findViewById(R.id.enable_custom_accent);
        LinearLayout custom_color_picker = findViewById(R.id.custom_color_picker);

        if (acc == 0) {
            enable_custom_accent.setChecked(false);
            custom_color_picker.setVisibility(View.GONE);
        } else {
            enable_custom_accent.setChecked(true);
            custom_color_picker.setVisibility(View.VISIBLE);
        }

        enable_custom_accent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Shell.cmd("settings put secure monet_engine_custom_color 1").exec();
                    custom_color_picker.setVisibility(View.VISIBLE);
                } else {
                    Shell.cmd("settings put secure monet_engine_custom_color 0").exec();
                    custom_color_picker.setVisibility(View.GONE);
                }
            }
        });

        custom_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog.newBuilder().setColor(color).setDialogType(ColorPickerDialog.TYPE_CUSTOM).setAllowCustom(false).setAllowPresets(true).setDialogId(1).setShowAlphaSlider(false).show(MonetColor.this);
            }
        });

        SeekBar white_luminance = findViewById(R.id.white_luminance);
        white_luminance.setPadding(0, 0, 0, 0);
        white_luminance.setProgressDrawable(getResources().getDrawable(R.drawable.seek_bar));
        white_luminance.setProgress(luminance / 25);
        TextView show_white_luminance = findViewById(R.id.show_white_luminance);
        if (luminance / 25 == 17)
            show_white_luminance.setText("Value: " + luminance + " (Default)");
        else
            show_white_luminance.setText("Value: " + luminance);
        white_luminance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 17)
                    show_white_luminance.setText("Value: " + i * 25 + " (Default)");
                else
                    show_white_luminance.setText("Value: " + i * 25);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Shell.cmd("settings put secure monet_engine_white_luminance_user " + (seekBar.getProgress() * 25)).exec();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enable_accurate_shades = findViewById(R.id.enable_accurate_shades);

        if (shade == 0) {
            enable_accurate_shades.setChecked(false);
        } else {
            enable_accurate_shades.setChecked(true);
        }

        enable_accurate_shades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Shell.cmd("settings put secure monet_engine_accurate_shades 1").exec();
                } else {
                    Shell.cmd("settings put secure monet_engine_accurate_shades 0").exec();
                }
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch enable_linear_lightness = findViewById(R.id.enable_linear_lightness);

        if (linear == 0) {
            enable_linear_lightness.setChecked(false);
        } else {
            enable_linear_lightness.setChecked(true);
        }

        enable_linear_lightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Shell.cmd("settings put secure monet_engine_linear_lightness 1").exec();
                } else {
                    Shell.cmd("settings put secure monet_engine_linear_lightness 0").exec();
                }
            }
        });

        SeekBar chroma_factor = findViewById(R.id.chroma_factor);
        chroma = initialize_chroma();
        chroma_factor.setPadding(0, 0, 0, 0);
        chroma_factor.setProgress(chroma / 25);
        chroma_factor.setProgressDrawable(getResources().getDrawable(R.drawable.seek_bar));
        TextView show_chroma_factor = findViewById(R.id.show_chroma_factor);
        if (chroma == 100)
            show_chroma_factor.setText("Value: " + chroma + " (Default)");
        else
            show_chroma_factor.setText("Value: " + chroma);
        chroma_factor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 4)
                    show_chroma_factor.setText("Value: " + (i * 25) + " (Default)");
                else
                    show_chroma_factor.setText("Value: " + i * 25);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Shell.cmd("settings put secure monet_engine_chroma_factor " + (seekBar.getProgress() * 25)).exec();
            }
        });
    }

    private int initialize_color() {
        int col = -65536;
        try {
            col = Integer.parseInt(accent_color.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return col;
    }
    
    private int initialize_accent() {
        int acc = 0;
        try {
            acc = Integer.parseInt(accent_color_check.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    private int initialize_luminance() {
        int lum = 425;
        try {
            lum = Integer.parseInt(white_lum.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lum;
    }

    private int initialize_chroma() {
        int chroma = 100;
        try {
            chroma = Integer.parseInt(chroma_fact.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chroma;
    }

    private int initialize_shade() {
        int shade = 1;
        try {
            chroma = Integer.parseInt(accurate_sh.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shade;
    }

    private int initialize_linear() {
        int linear = 1;
        try {
            linear = Integer.parseInt(linear_light.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linear;
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case 1:
                Shell.cmd("settings put secure monet_engine_color_override " + color).exec();
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        ;
    }

    public static String ColorToHex(int color) {
        int alpha = android.graphics.Color.alpha(color);
        int blue = android.graphics.Color.blue(color);
        int green = android.graphics.Color.green(color);
        int red = android.graphics.Color.red(color);

        String alphaHex = To00Hex(alpha);
        String blueHex = To00Hex(blue);
        String greenHex = To00Hex(green);
        String redHex = To00Hex(red);

        StringBuilder str = new StringBuilder("#");
        str.append(alphaHex);
        str.append(redHex);
        str.append(greenHex);
        str.append(blueHex);

        return str.toString();
    }

    private static String To00Hex(int value) {
        String hex = "00".concat(Integer.toHexString(value));
        return hex.substring(hex.length() - 2, hex.length());
    }
}