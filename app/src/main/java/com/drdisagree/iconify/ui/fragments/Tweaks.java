package com.drdisagree.iconify.ui.fragments;

import static com.drdisagree.iconify.common.Const.FRAGMENT_TRANSITION_DELAY;
import static com.drdisagree.iconify.common.References.FRAGMENT_COLORENGINE;
import static com.drdisagree.iconify.common.References.FRAGMENT_QSPANEL;
import static com.drdisagree.iconify.common.References.FRAGMENT_STYLES;
import static com.drdisagree.iconify.common.References.FRAGMENT_XPOSEDMENU;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drdisagree.iconify.Iconify;
import com.drdisagree.iconify.R;
import com.drdisagree.iconify.ui.activities.MediaPlayer;
import com.drdisagree.iconify.ui.activities.Miscellaneous;
import com.drdisagree.iconify.ui.activities.NavigationBar;
import com.drdisagree.iconify.ui.activities.Statusbar;
import com.drdisagree.iconify.ui.activities.UiRoundness;
import com.drdisagree.iconify.ui.activities.VolumePanel;
import com.drdisagree.iconify.utils.AppUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

public class Tweaks extends Fragment {

    private ViewGroup listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweaks, container, false);

        // Header
        CollapsingToolbarLayout collapsing_toolbar = view.findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle(getResources().getString(R.string.navbar_tweaks));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        listView = view.findViewById(R.id.tweaks_list);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out);

        ArrayList<Object[]> tweaks_list = new ArrayList<>();

        tweaks_list.add(new Object[]{(View.OnClickListener) v -> new Handler().postDelayed(() -> {
            fragmentTransaction.replace(R.id.main_fragment, new ColorEngine(), FRAGMENT_COLORENGINE);
            fragmentManager.popBackStack(FRAGMENT_STYLES, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(FRAGMENT_COLORENGINE);
            fragmentTransaction.commit();
        }, FRAGMENT_TRANSITION_DELAY), getResources().getString(R.string.activity_title_color_engine), getResources().getString(R.string.activity_desc_color_engine), R.drawable.ic_tweaks_color});
        tweaks_list.add(new Object[]{UiRoundness.class, getResources().getString(R.string.activity_title_ui_roundness), getResources().getString(R.string.activity_desc_ui_roundness), R.drawable.ic_tweaks_roundness});
        tweaks_list.add(new Object[]{(View.OnClickListener) v -> new Handler().postDelayed(() -> {
            fragmentTransaction.replace(R.id.main_fragment, new QsPanel(), FRAGMENT_QSPANEL);
            fragmentManager.popBackStack(FRAGMENT_STYLES, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(FRAGMENT_QSPANEL);
            fragmentTransaction.commit();
        }, FRAGMENT_TRANSITION_DELAY), getResources().getString(R.string.activity_title_qs_panel), getResources().getString(R.string.activity_desc_qs_panel), R.drawable.ic_tweaks_qs_panel});
        tweaks_list.add(new Object[]{Statusbar.class, getResources().getString(R.string.activity_title_statusbar), getResources().getString(R.string.activity_desc_statusbar), R.drawable.ic_tweaks_statusbar});
        tweaks_list.add(new Object[]{NavigationBar.class, getResources().getString(R.string.activity_title_navigation_bar), getResources().getString(R.string.activity_desc_navigation_bar), R.drawable.ic_tweaks_navbar});
        tweaks_list.add(new Object[]{MediaPlayer.class, getResources().getString(R.string.activity_title_media_player), getResources().getString(R.string.activity_desc_media_player), R.drawable.ic_tweaks_media});
        tweaks_list.add(new Object[]{VolumePanel.class, getResources().getString(R.string.activity_title_volume_panel), getResources().getString(R.string.activity_desc_volume_panel), R.drawable.ic_tweaks_volume});
        tweaks_list.add(new Object[]{(View.OnClickListener) v -> new Handler().postDelayed(() -> {
            // Check if LSPosed is installed or not
            if (!AppUtil.isLsposedInstalled()) {
                Toast.makeText(Iconify.getAppContext(), getResources().getString(R.string.toast_lsposed_not_found), Toast.LENGTH_SHORT).show();
                return;
            }
            fragmentTransaction.replace(R.id.main_fragment, new XposedMenu(), FRAGMENT_XPOSEDMENU);
            fragmentManager.popBackStack(FRAGMENT_STYLES, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.addToBackStack(FRAGMENT_XPOSEDMENU);
            fragmentTransaction.commit();
        }, FRAGMENT_TRANSITION_DELAY), getResources().getString(R.string.activity_title_xposed_menu), getResources().getString(R.string.activity_desc_xposed_menu), R.drawable.ic_tweaks_xposed_menu});
        tweaks_list.add(new Object[]{Miscellaneous.class, getResources().getString(R.string.activity_title_miscellaneous), getResources().getString(R.string.activity_desc_miscellaneous), R.drawable.ic_tweaks_miscellaneous});

        addItem(tweaks_list);

        return view;
    }

    // Function to add new item in list
    private void addItem(ArrayList<Object[]> pack) {
        for (int i = 0; i < pack.size(); i++) {
            View list = LayoutInflater.from(requireActivity()).inflate(R.layout.view_list_menu, listView, false);

            TextView title = list.findViewById(R.id.list_title);
            title.setText((String) pack.get(i)[1]);

            TextView desc = list.findViewById(R.id.list_desc);
            desc.setText((String) pack.get(i)[2]);

            ImageView preview = list.findViewById(R.id.list_preview);
            preview.setImageResource((int) pack.get(i)[3]);

            if (pack.get(i)[0] instanceof Class<?>) {
                int finalI = i;
                list.setOnClickListener(v -> {
                    Intent intent = new Intent(requireActivity(), (Class<?>) pack.get(finalI)[0]);
                    startActivity(intent);
                });
            } else if (pack.get(i)[0] instanceof View.OnClickListener) {
                list.setOnClickListener((View.OnClickListener) pack.get(i)[0]);
            }

            listView.addView(list);
        }
    }
}