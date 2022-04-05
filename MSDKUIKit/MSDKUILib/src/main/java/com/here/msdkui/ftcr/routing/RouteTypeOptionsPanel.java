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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A view that shows an options panel to select the available elements of
 * {@link FTCRRouteOptions.Type}.
 *
 * @see FTCRRouteOptions
 */
public class RouteTypeOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {

    private Map<FTCRRouteOptions.Type, String> mResourceKey;
    private OptionItem mOptionItem;
    private FTCRRouteOptions mRouteOptions;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public RouteTypeOptionsPanel(final Context context) {
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
    public RouteTypeOptionsPanel(final Context context, final AttributeSet attrs) {
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
    public RouteTypeOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public RouteTypeOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourceKey = new ConcurrentHashMap<>();
        mResourceKey.put(FTCRRouteOptions.Type.FASTEST, getString(R.string.msdkui_fastest));
        mResourceKey.put(FTCRRouteOptions.Type.SHORTEST, getString(R.string.msdkui_shortest));
    }

    private void createPanel() {
        mOptionItem = new OptionItemBuilders.SingleChoiceOptionItemBuilder(getContext()).setLabels(getString(R.string
                .msdkui_route_type_title), new ArrayList<>(mResourceKey.values()))
                .build();
        mOptionItem.setListener(this);
        setOptionItems(Collections.singletonList(mOptionItem));
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
        select(mRouteOptions.getRouteType());
    }

    /**
     * Populates the panel using the provided route options. Does nothing if no route options
     * have been set.
     */
    public void populateRouteOptions() {
        if (mRouteOptions == null) {
            return;
        }
        final String label = ((SingleChoiceOptionItem) mOptionItem).getSelectedItemLabel();
        for (final Map.Entry<FTCRRouteOptions.Type, String> entry : mResourceKey.entrySet()) {
            if (entry.getValue().equals(label)) {
                mRouteOptions.setRouteType(entry.getKey());
            }
        }
    }

    private void select(final FTCRRouteOptions.Type type) {
        ((SingleChoiceOptionItem) mOptionItem).selectLabel(mResourceKey.get(type));
    }

    @Override
    public void onChanged(OptionItem item) {
        populateRouteOptions();
        notifyOnOptionChanged(item);
    }
}
