package android.helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.helper.utils.CustomTimePickerDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

//android.helper.DateTimePickerEditText
public class DateTimePickerEditText extends android.support.v7.widget.AppCompatEditText
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        CustomTimePickerDialog.OnTimeSetListener {

    public enum Type {
        DATE_PICKER(0),
        TIME_PICKER(1),
        DELAY_PICKER(2);

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
    private static final DateFormat DEFAULT_DELAY_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private DateFormat format;
    //private String delayFormat;
    private Type type = Type.DATE_PICKER;
    private Date selectedDate;
    private long selectedDelay = 0;

    private Calendar initialDate = Calendar.getInstance();
    private long initialDelay = 0;

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
            if (ta.hasValue(R.styleable.DateTimePickerEditText_inputType)) {
                int value = ta.getInt(R.styleable.DateTimePickerEditText_inputType, 0);
                type = Type.values()[value];
                switch (type) {
                    case TIME_PICKER:
                        format = DEFAULT_TIME_FORMAT;
                        break;
                    case DELAY_PICKER:
                        format = DEFAULT_DELAY_FORMAT;
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        break;
                    case DATE_PICKER:
                    default:
                        format = DEFAULT_DATE_FORMAT;
                        break;
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
        } else if (type == Type.DELAY_PICKER) {
            CustomTimePickerDialog dialog = new CustomTimePickerDialog(getContext(), selectedDelay, this);
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

    @Override
    public void onTimeSet(android.helper.utils.TimePicker view, long delay) {
        selectedDelay = delay;
        updateText();
    }

    private void updateText() {
        if (type == Type.DELAY_PICKER) {
            if (selectedDelay == 0) {
                this.setText("");
                return;
            }
            if (format != null) {
                Date date = new Date(selectedDelay * 1000L);
                this.setText(format.format(date));
            } else {
                this.setText(formatDelay(selectedDelay));
            }
        } else {
            if (selectedDate == null) {
                this.setText("");
                return;
            }
            if (format != null)
                this.setText(format.format(selectedDate));
            else
                this.setText(selectedDate.toString());
        }
    }

    private String formatDelay(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);
    }

    public Date getDate() {
        return selectedDate;
    }

    public Date getTime() {
        return getDate();
    }

    public long getDelay() {
        return selectedDelay;
    }

    public long getDelayMillis() {
        return selectedDelay * 1000L;
    }

    public void setDate(Date date) {
        if (date != null) {
            selectedDate = date;
            setInitialDate(date);
            updateText();
        }
    }

    public void setTime(Date date) {
        setDate(date);
    }

    public void setDelayMillis(long delay) {
        selectedDelay = delay / 1000;
        updateText();
    }

    public void setDelay(long seconds) {
        selectedDelay = seconds;
        updateText();
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

    public void setInitialDelay(long delay) {
        initialDelay = delay;
        if (selectedDelay == 0) {
            selectedDelay = initialDelay;
        }
    }
}