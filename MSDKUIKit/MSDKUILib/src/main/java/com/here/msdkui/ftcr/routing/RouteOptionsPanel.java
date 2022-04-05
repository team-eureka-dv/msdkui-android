/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.msdkui.ftcr.routing;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A view that shows an options panel to select the available {@link FTCRRouteOptions}.
 */
@SuppressWarnings({"PMD.ModifiedCyclomaticComplexity", "PMD.StdCyclomaticComplexity"}) // Those rules are deprecated.
public class RouteOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {

    private int[] mResourcesKey;
    private FTCRRouteOptions mRouteOptions;
    private OptionItem mOptionItem;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public RouteOptionsPanel(final Context context) {
        this(context, null, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     */
    public RouteOptionsPanel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     */
    public RouteOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populateResources();
        createPanel();
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     *
     * @param defStyleRes
     *         a default style resource.
     *
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RouteOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourcesKey = new int[]{
                R.string.msdkui_allow_toll_roads,
                R.string.msdkui_allow_tunnels,
                R.string.msdkui_allow_highways,
                R.string.msdkui_allow_car_shuttle,
                R.string.msdkui_allow_ferries,
                R.string.msdkui_allow_car_pool,
                R.string.msdkui_allow_dirt_roads,
                R.string.msdkui_allow_parks
        };
    }

    private void createPanel() {
        mOptionItem = new OptionItemSpec.MultipleChoiceOptionItemBuilder(getContext()).setLabels(getLabels())
                .build();
        mOptionItem.setListener(this);
        setOptionsSpecs(Collections.singletonList(mOptionItem));
    }

    private List<String> getLabels() {
        final List<String> labels = new ArrayList<>(mResourcesKey.length);
        for (final int id : mResourcesKey) {
            labels.add(getString(id));
        }
        return labels;
    }

    private void select(final List<String> checkedOptions) {
        ((MultipleChoiceOptionItem) mOptionItem).selectLabels(checkedOptions);
    }

    /**
     * Gets the underlying {@link FTCRRouteOptions}.
     * @return the route options or null if no route options have been set.
     */
    public FTCRRouteOptions getRouteOptions() {
        if (mRouteOptions == null) {
            return null;
        }
        populateRouteOptions();
        return mRouteOptions;
    }

    /**
     * Sets the {@link FTCRRouteOptions} to be used for this panel.
     * @param routeOptions the route options to use for this panel.
     */
    public void setRouteOptions(final FTCRRouteOptions routeOptions) {
        mRouteOptions = routeOptions;
        select(getSelectedOptions());
    }

    /**
     * Populates the panel using the provided route options. Does nothing if no route options
     * have been set.
     */
    // This function is not complicated and not too long.
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NcssCount"})
    public void populateRouteOptions() {
        if (mRouteOptions == null) {
            return;
        }
        final MultipleChoiceOptionItem multipleChoiceOptionItem = (MultipleChoiceOptionItem) mOptionItem;
        final List<String> selectedLabels = multipleChoiceOptionItem.getSelectedLabels();
        for (final int key : mResourcesKey) {
            // TODO no such API in FTCR
            final boolean status = selectedLabels.contains(getString(key));
            if (key == R.string.msdkui_allow_car_shuttle) {
                //mRouteOptions.setCarShuttleTrainsAllowed(status);
            } else if (key == R.string.msdkui_allow_car_pool) {
                //mRouteOptions.setCarpoolAllowed(status);
            } else if (key == R.string.msdkui_allow_dirt_roads) {
                //mRouteOptions.setDirtRoadsAllowed(status);
            } else if (key == R.string.msdkui_allow_ferries) {
                mRouteOptions.setBoatFerriesAvoidance(!status
                        ? FTCRRouteOptions.RouteAvoidance.AVOID
                        : FTCRRouteOptions.RouteAvoidance.DEFAULT);
            } else if (key == R.string.msdkui_allow_highways) {
                mRouteOptions.setMotorwaysAvoidance(!status
                        ? FTCRRouteOptions.RouteAvoidance.AVOID
                        : FTCRRouteOptions.RouteAvoidance.DEFAULT);
            } else if (key == R.string.msdkui_allow_toll_roads) {
                mRouteOptions.setTollRoadsAvoidance(!status
                        ? FTCRRouteOptions.RouteAvoidance.AVOID
                        : FTCRRouteOptions.RouteAvoidance.DEFAULT);
            } else if (key == R.string.msdkui_allow_parks) {
                //mRouteOptions.setParksAllowed(status);
            } else if (key == R.string.msdkui_allow_tunnels) {
                mRouteOptions.setTunnelsAvoidance(!status
                        ? FTCRRouteOptions.RouteAvoidance.AVOID
                        : FTCRRouteOptions.RouteAvoidance.DEFAULT);
            }
        }
    }

    // This function is not complicated.
    @SuppressWarnings("PMD.NPathComplexity")
    private List<String> getSelectedOptions() {
        // TODO no such API in FTCR
        final List<String> checkedOptions = new ArrayList<>();
        if (false) { //mRouteOptions.areCarShuttleTrainsAllowed()
            checkedOptions.add(getString(R.string.msdkui_allow_car_shuttle));
        }
        if (false) { // mRouteOptions.isCarpoolAllowed()
            checkedOptions.add(getString(R.string.msdkui_allow_car_pool));
        }
        if (false) { // mRouteOptions.areDirtRoadsAllowed()
            checkedOptions.add(getString(R.string.msdkui_allow_dirt_roads));
        }
        if (mRouteOptions.getBoatFerriesAvoidance() == FTCRRouteOptions.RouteAvoidance.DEFAULT) {
            checkedOptions.add(getString(R.string.msdkui_allow_ferries));
        }
        if (mRouteOptions.getMotorwaysAvoidance() == FTCRRouteOptions.RouteAvoidance.DEFAULT) {
            checkedOptions.add(getString(R.string.msdkui_allow_highways));
        }
        if (mRouteOptions.getTollRoadsAvoidance() == FTCRRouteOptions.RouteAvoidance.DEFAULT) {
            checkedOptions.add(getString(R.string.msdkui_allow_toll_roads));
        }
        if (false) { //mRouteOptions.areParksAllowed()
            checkedOptions.add(getString(R.string.msdkui_allow_parks));
        }
        if (mRouteOptions.getTunnelsAvoidance() == FTCRRouteOptions.RouteAvoidance.DEFAULT) {
            checkedOptions.add(getString(R.string.msdkui_allow_tunnels));
        }
        return checkedOptions;
    }

    @Override
    public void onChanged(OptionItem item) {
        populateRouteOptions();
        notifyOnOptionChanged(item);
    }
}
