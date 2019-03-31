/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 Ivan Kovac navratnanos@gmail.com
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

package android.helper.utils;

import android.content.Context;
import android.helper.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import java.util.concurrent.TimeUnit;

public class TimePicker extends FrameLayout {

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
     */
    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        public void onTimeChanged(TimePicker view, long delay) {
        }
    };

    public static final Formatter TWO_DIGIT_FORMATTER =
            new Formatter() {
                @Override
                public String format(int value) {
                    return String.format("%02d", value);
                }
            };

    // state
    private long currentDelay = 0;
    private int currentHour = 0;
    private int currentMinute = 0;
    private int currentSeconds = 0;

    // ui components
    private final NumberPicker picker_hour;
    private final NumberPicker picker_minute;
    private final NumberPicker picker_second;

    // callbacks
    private OnTimeChangedListener mOnTimeChangedListener;

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {

        /**
         * @param view  The view associated with this listener.
         * @param delay Picked delay in seconds
         */
        void onTimeChanged(TimePicker view, long delay);
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_picker_widget, this, true);

        // hour
        picker_hour = (NumberPicker) findViewById(R.id.hour);
        picker_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentHour = newVal;
                onTimeChanged();
            }
        });

        // digits of minute
        picker_minute = (NumberPicker) findViewById(R.id.minute);
        picker_minute.setMinValue(0);
        picker_minute.setMaxValue(59);
        picker_minute.setFormatter(TWO_DIGIT_FORMATTER);
        picker_minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                currentMinute = newVal;
                onTimeChanged();
            }
        });

        // digits of seconds
        picker_second = (NumberPicker) findViewById(R.id.seconds);
        picker_second.setMinValue(0);
        picker_second.setMaxValue(59);
        picker_second.setFormatter(TWO_DIGIT_FORMATTER);
        picker_second.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentSeconds = newVal;
                onTimeChanged();

            }
        });

        // now that the hour/minute picker objects have been initialized, set
        // the hour range properly based on the 12/24 hour display mode.
        configurePickerRanges();

        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);

        setCurrentDelay(0);

        if (!isEnabled()) {
            setEnabled(false);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        picker_minute.setEnabled(enabled);
        picker_hour.setEnabled(enabled);
    }

    /**
     * Used to save / restore state of time picker
     */
    private static class SavedState extends BaseSavedState {

        private final long delay;

        private SavedState(Parcelable superState, long delay) {
            super(superState);
            this.delay = delay;
        }

        private SavedState(Parcel in) {
            super(in);
            delay = in.readLong();
        }

        public long getDelay() {
            return this.delay;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.delay);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, currentDelay);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentDelay(ss.getDelay());
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     *
     * @param onTimeChangedListener the callback, should not be null.
     */
    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        mOnTimeChangedListener = onTimeChangedListener;
    }

    /**
     * @return The current hour (0-23).
     */
    public long getCurrentDelay() {
        return currentDelay;
    }

    /**
     * Divides and sets the current delay seconds to Hour, Minute and Second
     */
    public void setCurrentDelay(long delay) {
        currentSeconds = (int) (delay % 60);
        currentMinute = (int) ((delay / 60) % 60);
        currentHour = (int) ((delay / (60 * 60)) % 24);

        picker_hour.setValue(currentHour);
        picker_minute.setValue(currentMinute);
        picker_second.setValue(currentSeconds);

        onTimeChanged();
    }

    @Override
    public int getBaseline() {
        return picker_hour.getBaseline();
    }

    private void configurePickerRanges() {
        picker_hour.setMinValue(0);
        picker_hour.setMaxValue(8784);
        picker_hour.setFormatter(TWO_DIGIT_FORMATTER);
    }

    private void onTimeChanged() {
        long seconds = 0;
        seconds += TimeUnit.HOURS.toSeconds(currentHour);
        seconds += TimeUnit.MINUTES.toSeconds(currentMinute);
        seconds += currentSeconds;
        currentDelay = seconds;
        mOnTimeChangedListener.onTimeChanged(this, currentDelay);
    }
}

