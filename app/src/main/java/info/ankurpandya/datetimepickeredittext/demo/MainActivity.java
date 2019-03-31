package info.ankurpandya.datetimepickeredittext.demo;

import android.helper.DateTimePickerEditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    DateTimePickerEditText edt_date;
    DateTimePickerEditText edt_time;
    DateTimePickerEditText edt_delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        edt_delay = findViewById(R.id.edt_delay);
    }

    public void setDateToday(View view) {
        edt_date.setDate(new Date());
    }

    public void setTimeNow(View view) {
        edt_time.setDate(new Date());
    }

    public void setDelay1Min(View view) {
        edt_delay.setDelay(TimeUnit.MINUTES.toSeconds(1));
    }

    public void submitDateAndTime(View view) {
        Date selectedDate = edt_date.getDate();
        Date selectedTime = edt_time.getDate();
        long delay = edt_delay.getDelayMillis();

        if (selectedDate == null) {
            edt_date.setError(getString(R.string.err_invalid_date));
            edt_date.requestFocus();
            return;
        }
        if (selectedTime == null) {
            edt_time.setError(getString(R.string.err_invalid_time));
            edt_time.requestFocus();
            return;
        }
        if (delay == 0) {
            edt_delay.setError(getString(R.string.err_invalid_delay));
            edt_delay.requestFocus();
            return;
        }
        showToast(
                getString(
                        R.string.msg_submit,
                        formatDate(selectedDate),
                        formatTime(selectedTime),
                        formatDelay(delay)
                )
        );
    }

    private String formatDate(Date date) {
        return edt_date.getFormat().format(date);
    }

    private String formatTime(Date time) {
        return edt_time.getFormat().format(time);
    }

    private String formatDelay(long delay) {
        return edt_delay.getFormat().format(delay);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private static boolean isValid(CharSequence text) {
        return text != null && text.toString().trim().length() > 0;
    }
}
