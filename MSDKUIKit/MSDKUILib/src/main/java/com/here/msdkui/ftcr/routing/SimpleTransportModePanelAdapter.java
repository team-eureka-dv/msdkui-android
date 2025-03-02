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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A pre-defined adapter class for {@link TransportModePanel} to show a {@code TabLayout} structure displaying
 * all possible transport modes with icons.
 */
public class SimpleTransportModePanelAdapter extends TransportModePanelAdapter {

    private final Context mContext;

    /**
     * Constructs a new instance using all supported
     * {@link FTCRRouteOptions.TransportMode} elements.
     *
     * @param context
     *         the required {@link Context}.
     */
    public SimpleTransportModePanelAdapter(final Context context) {
        super(null);
        final List<FTCRRouteOptions.TransportMode> mTransportModes = new ArrayList<>(Arrays.asList(
                FTCRRouteOptions.TransportMode.CAR,
                FTCRRouteOptions.TransportMode.TRUCK,
                FTCRRouteOptions.TransportMode.PEDESTRIAN,
                FTCRRouteOptions.TransportMode.BICYCLE,
                FTCRRouteOptions.TransportMode.SCOOTER));
        setTransportModes(mTransportModes);
        mContext = context;
    }

    /**
     * Constructs a new instance using the given list of
     * {@link FTCRRouteOptions.TransportMode} elements.
     *
     * @param context
     *         the required {@link Context}.
     * @param transportModes
     *         a list of {@link FTCRRouteOptions.TransportMode} elements.
     */
    public SimpleTransportModePanelAdapter(final Context context, List<FTCRRouteOptions.TransportMode> transportModes) {
        super(null);
        setTransportModes(transportModes);
        mContext = context;
    }

    @Override
    public Fragment getContent(final FTCRRouteOptions.TransportMode mode) {
        return null;
    }


    private TabView getTabViewForTransportMode(final FTCRRouteOptions.TransportMode mode) {
        // Set label for tab if needed.
        final TabView tab = new TabView(mContext);
        String description = "";
        switch (mode) {
            case CAR:
                tab.setIcon(getDrawable(R.drawable.ic_drive));
                description = mContext.getString(R.string.msdkui_car);
                break;
            case TRUCK:
                tab.setIcon(getDrawable(R.drawable.ic_truck_24));
                description = mContext.getString(R.string.msdkui_truck);
                break;
            case PEDESTRIAN:
                tab.setIcon(getDrawable(R.drawable.ic_walk_24));
                description = mContext.getString(R.string.msdkui_pedestrian);
                break;
            case BICYCLE:
                tab.setIcon(getDrawable(R.drawable.ic_bike_24));
                description = mContext.getString(R.string.msdkui_bike);
                break;
            case SCOOTER:
                tab.setIcon(getDrawable(R.drawable.ic_scooter));
                description = mContext.getString(R.string.msdkui_scooter);
                break;
            default:
                throw new IllegalArgumentException(mContext.getString(R.string.msdkui_exception_custom_view_not_available,
                        mode.toString()));
        }
        tab.setContentDescription(mContext.
                getString(R.string.msdkui_transport_mode, description));
        return tab;
    }

    /**
     * Gets a custom {@link TabView} with drawable icons which can be used to add labels
     * by calling {@link TabView#setLabel(String)} in the respective switch statements.
     * @deprecated Please use
     * {@link #getCustomTabView(FTCRRouteOptions.TransportMode)} instead.
     */
    @Deprecated
    @Override
    public TabView getTabCustomView(final FTCRRouteOptions.TransportMode mode) {
        return getTabViewForTransportMode(mode);
    }

    /**
     * Gets a custom {@link TabView} with drawable icons which can be used to add labels
     * by calling {@link TabView#setLabel(String)} in the respective switch statements.
     */
    @Override
    public TabView getCustomTabView(final FTCRRouteOptions.TransportMode mode) {
        return getTabViewForTransportMode(mode);
    }

    private Drawable getDrawable(int drawableId) {
        final Drawable drawable = ContextCompat.getDrawable(mContext, drawableId).mutate();
        drawable.setColorFilter(ThemeUtil.getColor(mContext, R.attr.colorForegroundSecondaryLight), PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }

    @Override
    public boolean isContentVisible() {
        return false;
    }
}
