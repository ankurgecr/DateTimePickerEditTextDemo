package info.ankurpandya.datetimepickeredittext.demo;

import android.helper.DateTimePickerEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DateTimePickerEditText edt_date;
    DateTimePickerEditText edt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
    }

    public void setTimeNow(View view) {
        edt_time.setDate(new Date());
    }

    public void setDateToday(View view) {
        edt_date.setDate(new Date());
    }

    public void submitDateAndTime(View view) {
        Date selectedDate = edt_date.getDate();
        Date selectedTime = edt_time.getDate();
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
        showToast(
                getString(
                        R.string.msg_submit,
                        formatDate(selectedDate),
                        formatTime(selectedTime)
                )
        );
    }

    private String formatDate(Date date) {
        return edt_date.getFormat().format(date);
    }

    private String formatTime(Date time) {
        return edt_time.getFormat().format(time);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private static boolean isValid(CharSequence text) {
        return text != null && text.toString().trim().length() > 0;
    }
}
