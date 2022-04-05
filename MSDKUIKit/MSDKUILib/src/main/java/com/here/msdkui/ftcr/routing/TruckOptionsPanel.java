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
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.here.android.mpa.ftcr.FTCRRouteOptions;
import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A view that shows an options panel to select the available truck options provided by
 * {@link FTCRRouteOptions}.
 */
@SuppressWarnings({"PMD.ModifiedCyclomaticComplexity", "PMD.StdCyclomaticComplexity"}) // Those rules are deprecated.
public class TruckOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {

    private FTCRRouteOptions mRouteOptions;

    private Integer[] mResourceKey;
    private List<OptionItem> mSubItem;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TruckOptionsPanel(final Context context) {
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
    public TruckOptionsPanel(final Context context, final AttributeSet attrs) {
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
    public TruckOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
     * Requires Lollipop (API Level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TruckOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourceKey = new Integer[]{
                R.string.msdkui_height,
                R.string.msdkui_length,
                R.string.msdkui_width,
                R.string.msdkui_limited_weight,
                R.string.msdkui_weight_per_axle,
                R.string.msdkui_number_of_trailers
        };
    }

    private void createPanel() {
        mSubItem = new ArrayList<>();
        for (final Integer id : mResourceKey) {
            mSubItem.add(getItem(id));
        }
        mSubItem.add(getRestrictionItem());
        setOptionItems(mSubItem);
    }


    private OptionItem getRestrictionItem() {
        final OptionItem item = new OptionItemBuilders.BooleanOptionItemBuilder(getContext()).setLabel(getString(R.string
                .msdkui_violate_truck_options)).setItemId(R.string.msdkui_violate_truck_options).build();
        item.setListener(this);
        return item;
    }

    private OptionItem getItem(Integer id) {
        final OptionItem item = new OptionItemBuilders.NumericOptionItemBuilder(getContext()).setLabel(getString(id))
                .setItemId(id).build();
        item.setListener(this);
        return item;
    }

    /**
     * Gets the underlying {@link FTCRRouteOptions}.
     *
     * @return the {@link FTCRRouteOptions} that was set for this panel or null if no options have been set.
     */
    public FTCRRouteOptions getRouteOptions() {
        return mRouteOptions;
    }

    /**
     * Sets the {@link FTCRRouteOptions} and populates this panel based on the provided options.
     *
     * @param routeOptions the {@link FTCRRouteOptions} to set for this panel.
     * @throws IllegalArgumentException if routeOptions is null.
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity"}) // This function is not complicated.
    public void setRouteOptions(final FTCRRouteOptions routeOptions) {
        if (routeOptions == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_route_options_null));
        }
        mRouteOptions = routeOptions;
        for (final OptionItem item : mSubItem) {
            if (item.getItemId() == R.string.msdkui_height) {
                ((NumericOptionItem) item).setValue(mRouteOptions.getVehicleHeight());
            } else if (item.getItemId() == R.string.msdkui_length) {
                ((NumericOptionItem) item).setValue(mRouteOptions.getVehicleLength());
            } else if (item.getItemId() == R.string.msdkui_width) {
                ((NumericOptionItem) item).setValue(mRouteOptions.getVehicleWidth());
            } else if (item.getItemId() == R.string.msdkui_limited_weight) {
                ((NumericOptionItem) item).setValue(mRouteOptions.getVehicleLimitedWeight());
            } else if (item.getItemId() == R.string.msdkui_weight_per_axle) {
                ((NumericOptionItem) item).setValue(mRouteOptions.getVehicleWeightPerAxle());
            }
            // TODO no such API in FTCR
            //else if (item.getItemId() == R.string.msdkui_number_of_trailers) {
            //    ((NumericOptionItem) item).setInputType(InputType.TYPE_CLASS_NUMBER)
            //            .setValue(mRouteOptions.trailegetTruckTrailersCount());
            //}
            //else if (item.getItemId() == R.string.msdkui_truck_type) {
            //    ((SingleChoiceOptionItem) item).selectLabel(getTruckTypeLabel(mRouteOptions.getTruckType()));
            //}
            //else if (item.getItemId() == R.string.msdkui_violate_truck_options) {
            //    setRestriction(item);
            //}
        }
    }

//    private void setRestriction(OptionItem item) {
//        final BooleanOptionItem booleanOptionItem = (BooleanOptionItem) item;
//        if (mRouteOptions.getTruckRestrictionsMode() == RouteOptions.TruckRestrictionsMode.NO_VIOLATIONS) {
//            booleanOptionItem.setChecked(false);
//        } else {
//            booleanOptionItem.setChecked(true);
//        }
//    }

    // Here, we catch generic exception to revert changes if anything goes wrong.
    // This function is not complicated and not too long.
    @SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.NcssCount", "PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    @Override
    public void onChanged(OptionItem item) {

        if (mRouteOptions == null) {
            return;
        }

        // TODO no such API in FTCR
        //  if (item.getItemId() == R.string.msdkui_violate_truck_options) {
        //      if (((BooleanOptionItem) item).isChecked()) {
        //          mRouteOptions.setTruckRestrictionsMode(RouteOptions.TruckRestrictionsMode.PENALIZE_VIOLATIONS);
        //      } else {
        //          mRouteOptions.setTruckRestrictionsMode(RouteOptions.TruckRestrictionsMode.NO_VIOLATIONS);
        //      }
        //  }

        try {

            if (item instanceof NumericOptionItem) {
                final Number value = ((NumericOptionItem) item).getValue();
                if (item.getItemId() == R.string.msdkui_height) {
                    mRouteOptions.setVehicleHeight(value == null ? Float.NaN : value.floatValue());
                } else if (item.getItemId() == R.string.msdkui_length) {
                    mRouteOptions.setVehicleLength(value == null ? Float.NaN : value.floatValue());
                } else if (item.getItemId() == R.string.msdkui_width) {
                    mRouteOptions.setVehicleWidth(value == null ? Float.NaN : value.floatValue());
                } else if (item.getItemId() == R.string.msdkui_limited_weight) {
                    mRouteOptions.setVehicleLimitedWeight(value == null ? Float.NaN : value.floatValue());
                } else if (item.getItemId() == R.string.msdkui_weight_per_axle) {
                    mRouteOptions.setVehicleWeightPerAxle(value == null ? Float.NaN : value.floatValue());
                }
                // TODO no such API in FTCR
                //else if (item.getItemId() == R.string.msdkui_number_of_trailers) {
                //    mRouteOptions.setTruckTrailersCount(value == null ? 0 : value.intValue());
                //}
            }

            // TODO no such API in FTCR
            //if (item instanceof SingleChoiceOptionItem && item.getItemId() == R.string.msdkui_truck_type) {
            //    final String label = ((SingleChoiceOptionItem) item).getSelectedItemLabel();
            //    setTruckType(label);
            //}

            notifyOnOptionChanged(item);

        } catch (Exception e) {
            // revert changes
            setRouteOptions(mRouteOptions);
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }

    // TODO no such API in FTCR
//    private void setTruckType(String label) {
//        for (final Map.Entry<RouteOptions.TruckType, String> entry : mTruckTypeResource.entrySet()) {
//            if (entry.getValue().equals(label)) {
//                mRouteOptions.setTruckType(entry.getKey());
//                break;
//            }
//        }
//    }
//
//    private String getTruckTypeLabel(final RouteOptions.TruckType truckType) {
//        for (final Map.Entry<RouteOptions.TruckType, String> entry : mTruckTypeResource.entrySet()) {
//            if (truckType == entry.getKey()) {
//                return entry.getValue();
//            }
//        }
//        return null;
//    }
}
