/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 Ivan Kovac  navratnanos@gmail.com
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.helper.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class CustomTimePickerDialog extends AlertDialog implements OnClickListener,
        TimePicker.OnTimeChangedListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view  The view associated with this listener.
         * @param delay The delay in seconds that was set.
         */
        void onTimeSet(TimePicker view, long delay);
    }

    private static final String DELAY = "delay";

    private final TimePicker timePicker;
    private final OnTimeSetListener mCallback;

    /**
     * @param context  Parent.
     * @param delay    The initial delay in seconds.
     * @param callBack How parent is notified.
     */
    public CustomTimePickerDialog(Context context,
                                  long delay,
                                  OnTimeSetListener callBack) {

        this(context, 0, delay, callBack);
    }

    /**
     * @param context  Parent.
     * @param theme    the theme to apply to this dialog
     * @param delay    The initial delay in seconds.
     * @param callBack How parent is notified.
     */
    public CustomTimePickerDialog(Context context,
                                  int theme,
                                  long delay,
                                  OnTimeSetListener callBack) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;

        setButton(context.getText(R.string.dtpe_time_set), this);
        setButton2(context.getText(R.string.dtpe_cancel), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        timePicker.setCurrentDelay(delay);
        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            timePicker.clearFocus();
            mCallback.onTimeSet(timePicker, timePicker.getCurrentDelay());
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, long seconds) {
        //updateTitle(hourOfDay, minute, seconds);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putLong(DELAY, timePicker.getCurrentDelay());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long delay = savedInstanceState.getLong(DELAY);
        timePicker.setCurrentDelay(delay);
        timePicker.setOnTimeChangedListener(this);
        //updateTitle(hour, minute, seconds);
    }


}
