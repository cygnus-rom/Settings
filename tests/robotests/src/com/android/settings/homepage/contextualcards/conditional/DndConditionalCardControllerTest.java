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

package com.android.settings.homepage.contextualcards.conditional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class DndConditionalCardControllerTest {

    @Mock
    private ConditionManager mConditionManager;
    private Context mContext;
    private DndConditionCardController mController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = spy(RuntimeEnvironment.application);
        mController = new DndConditionCardController(mContext, mConditionManager);
    }

    @Test
    public void cycleMonitoring_shouldRegisterAndUnregisterReceiver() {
        mController.startMonitoringStateChange();
        mController.stopMonitoringStateChange();

        verify(mContext).registerReceiver(any(DndConditionCardController.Receiver.class),
                eq(DndConditionCardController.DND_FILTER));
        verify(mContext).unregisterReceiver(any(DndConditionCardController.Receiver.class));
    }
}
