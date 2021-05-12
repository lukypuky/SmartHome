package com.example.smarthome.scenarios;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.smarthome.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
{
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int min = calendar.get(Calendar.MINUTE);

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(), hour, min, true);
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Potvrdiť", timePickerDialog);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Späť", timePickerDialog);
        return timePickerDialog;
    }
}
