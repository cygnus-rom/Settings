/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.fuelgauge.batterysaver;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settings.R;
import com.android.settings.widget.RadioButtonPreference;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.widget.CandidateInfo;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Fragment that allows users to customize their automatic battery saver mode settings.
 *
 * Location: Settings > Battery > Battery Saver > Set a Schedule
 * See {@link BatterySaverSchedulePreferenceController} for the controller that manages navigation
 * to this screen from "Settings > Battery > Battery Saver" and the summary.
 * See {@link BatterySaverScheduleRadioButtonsController} &
 * {@link BatterySaverScheduleSeekBarController} for the controller that manages user
 * interactions in this screen.
 */
public class BatterySaverScheduleSettings extends RadioButtonPickerFragment {

    public BatterySaverScheduleRadioButtonsController mRadioButtonController;
    private BatterySaverScheduleSeekBarController mSeekBarController;

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.battery_saver_schedule_settings;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSeekBarController = new BatterySaverScheduleSeekBarController(context);
        mRadioButtonController = new BatterySaverScheduleRadioButtonsController(
                context, mSeekBarController);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<? extends CandidateInfo> getCandidates() {
        Context context = getContext();
        List<CandidateInfo> candidates = Lists.newArrayList();
        String routineProviderApp = getContext().getResources()
                .getString(com.android.internal.R.string.config_batterySaverScheduleProvider);
        candidates.add(new BatterySaverScheduleCandidateInfo(
                context.getText(R.string.battery_saver_auto_no_schedule),
                /* summary */ null,
                BatterySaverScheduleRadioButtonsController.KEY_NO_SCHEDULE,
                /* enabled */ true));
        // only add routine option if an app has been specified
        if (!TextUtils.isEmpty(routineProviderApp)) {
            candidates.add(new BatterySaverScheduleCandidateInfo(
                    context.getText(R.string.battery_saver_auto_routine),
                    context.getText(R.string.battery_saver_auto_routine_summary),
                    BatterySaverScheduleRadioButtonsController.KEY_ROUTINE,
                    /* enabled */ true));
        } else {
            // Make sure routine is not selected if no provider app is configured
            BatterySaverUtils.revertScheduleToNoneIfNeeded(context);
        }
        candidates.add(new BatterySaverScheduleCandidateInfo(
                context.getText(R.string.battery_saver_auto_percentage),
                /* summary */ null,
                BatterySaverScheduleRadioButtonsController.KEY_PERCENTAGE,
                /* enabled */ true));

        return candidates;
    }

    @Override
    public void bindPreferenceExtra(RadioButtonPreference pref, String key, CandidateInfo info,
            String defaultKey, String systemDefaultKey) {
        final BatterySaverScheduleCandidateInfo candidateInfo =
                (BatterySaverScheduleCandidateInfo) info;
        final CharSequence summary = candidateInfo.getSummary();
        if (summary != null) {
            pref.setSummary(summary);
            pref.setAppendixVisibility(View.GONE);
        }
    }

    @Override
    protected void addStaticPreferences(PreferenceScreen screen) {
        mSeekBarController.updateSeekBar();
        mSeekBarController.addToScreen(screen);
    }

    @Override
    protected String getDefaultKey() {
        return mRadioButtonController.getDefaultKey();
    }

    @Override
    protected boolean setDefaultKey(String key) {
        return mRadioButtonController.setDefaultKey(key);
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    static class BatterySaverScheduleCandidateInfo extends CandidateInfo {

        private final CharSequence mLabel;
        private final CharSequence mSummary;
        private final String mKey;

        BatterySaverScheduleCandidateInfo(CharSequence label, CharSequence summary, String key,
                boolean enabled) {
            super(enabled);
            mLabel = label;
            mKey = key;
            mSummary = summary;
        }

        @Override
        public CharSequence loadLabel() {
            return mLabel;
        }

        @Override
        public Drawable loadIcon() {
            return null;
        }

        @Override
        public String getKey() {
            return mKey;
        }

        public CharSequence getSummary() {
            return mSummary;
        }
    }
}