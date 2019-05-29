DateTimePickerEditText
===========

A simple material EditText which allows to you pick Date and Time using Android's default DatePicker & TimePicker.

Preview
---------------
<p>
    <img src="https://i.imgur.com/IiJmpZ2.gif" width="45%" />
    <img src="https://i.imgur.com/LyWFZuW.gif" width="45%" />
</p>

Getting started
---------------

[ ![latestVersion](https://api.bintray.com/packages/ankurgecr/dateTimePickerEditText/datetimepickeredittext/images/download.svg) ](https://api.bintray.com/packages/ankurgecr/dateTimePickerEditText/datetimepickeredittext/)

To get started with DateTimePickerEditText, you'll need to get
add the dependency to your project's build.gradle file:
```
dependencies {
    //other dependencies
    implementation "android.helper:datetimepickeredittext:$latestVersion"
}
```
Then to sync up your project and you are all set to use DateTimePickerEditText.

To add Date UI componenet in your Layout XML file
```xml
<android.helper.DateTimePickerEditText
    android:id="@+id/edt_date"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:inputType="date" />

<android.helper.DateTimePickerEditText
    android:id="@+id/edt_time"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:inputType="time" />
```

default value for `android:inputType` is `date`

Get and Set contact numbers
--------

```
import android.helper.DateTimePickerEditText;

DateTimePickerEditText edt_date = findViewById(R.id.edt_date);
DateTimePickerEditText edt_time = findViewById(R.id.edt_time);

edt_phone.setDate(new Date());
edt_time.setDate(new Date());

Date selectedDate = edt_date.getDate();
Date selectedTime = edt_time.getDate();
```

You can use all properties of `EditText` for `DateTimePickerEditText`

To specify the date or time format, use
```
<android.helper.DateTimePickerEditText
    android:id="@+id/edt_time"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:format="dd/MM/yyyy" />
```

or

```
edt_time.setFormat("dd/MM/yyyy");
```

Questions?
--------
Feel free to register github issues with a 'question' label

Demo
--------
Check the demo app for more details.

License
--------
Licensed under the MIT license. See [LICENSE](LICENSE.md).