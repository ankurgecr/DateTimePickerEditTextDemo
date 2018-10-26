package android.helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//android.helper.DateTimePickerEditText
public class DateTimePickerEditText extends android.support.v7.widget.AppCompatEditText
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public enum Type {
        DATE_PICKER(0),
        TIME_PICKER(1);

        int value;

        Type(int value) {
            this.value = value;
        }

        int getValue() {
            return this.value;
        }
    }

    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("hh:mm a");

    private DateFormat format;
    private Type type = Type.DATE_PICKER;
    private Date selectedDate;
    private Calendar initialDate = Calendar.getInstance();

    public DateTimePickerEditText(Context context) {
        super(context);
        setup();
    }

    public DateTimePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
        parseAttribute(attrs);
    }

    public DateTimePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
        parseAttribute(attrs);
    }

    private void parseAttribute(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DateTimePickerEditText, 0, 0);
        try {

            if (ta.hasValue(R.styleable.DateTimePickerEditText_android_inputType)) {
                int valueTime = (InputType.TYPE_DATETIME_VARIATION_TIME | InputType.TYPE_CLASS_DATETIME);
                int valueDate = (InputType.TYPE_DATETIME_VARIATION_DATE | InputType.TYPE_CLASS_DATETIME);
                //int valueNormal = (InputType.TYPE_DATETIME_VARIATION_NORMAL |  InputType.TYPE_CLASS_DATETIME);
                int value = ta.getInt(R.styleable.DateTimePickerEditText_android_inputType, valueDate);
                if (value == valueTime) {
                    type = Type.TIME_PICKER;
                    format = DEFAULT_TIME_FORMAT;
                } else {
                    type = Type.DATE_PICKER;
                    format = DEFAULT_DATE_FORMAT;
                }
            } else {
                type = Type.DATE_PICKER;
                format = DEFAULT_DATE_FORMAT;
            }

            String str_format = ta.getString(R.styleable.DateTimePickerEditText_android_format);
            if (str_format != null && str_format.trim().length() > 0) {
                setFormat(str_format);
            }
        } finally {
            ta.recycle();
        }
    }

    public void setup() {
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker();
            }
        });
    }

    public DateFormat getFormat() {
        return format;
    }

    public void setFormat(String formatString) {
        setFormat(new SimpleDateFormat(formatString));
    }

    public void setFormat(DateFormat format) {
        this.format = format;
        updateText();
    }

    public void showPicker() {
        if (type == Type.TIME_PICKER) {
            int hourOfDay = initialDate.get(Calendar.HOUR_OF_DAY);
            int minutes = initialDate.get(Calendar.MINUTE);
            TimePickerDialog dialog = new TimePickerDialog(getContext(), this, hourOfDay, minutes, false);
            dialog.show();
        } else {
            int year;
            int month;
            int day;
            year = initialDate.get(Calendar.YEAR);
            month = initialDate.get(Calendar.MONTH);
            day = initialDate.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, year, month, day);
            dialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
        Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
        selectedDate = calendar.getTime();
        updateText();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        selectedDate = calendar.getTime();
        updateText();
    }

    private void updateText() {
        if (selectedDate == null) {
            this.setText("");
            return;
        }
        if (format != null)
            this.setText(format.format(selectedDate));
        else
            this.setText(selectedDate.toString());
    }

    public Date getDate() {
        return selectedDate;
    }

    public void setDate(Date date) {
        if (date != null) {
            selectedDate = date;
            setInitialDate(date);
            updateText();
        }
    }

    private void setDate(Date date, boolean shouldSetInitialDate) {
        if (date != null) {
            selectedDate = date;
            if (shouldSetInitialDate) {
                setInitialDate(date);
            }
            updateText();
        }
    }

    public void setInitialDate(Date date) {
        initialDate.setTime(date);
    }
}