package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Hour;
import project.csc895.sfsu.waitlesshost.model.Restaurant;


public class OpenHourFragment extends Fragment {

    private static final String TAG = "Open Hour Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String HOUR_CHILD = "hours";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String SUNDAY_CHILD = "Sunday";
    private static final String MONDAY_CHILD = "Monday";
    private static final String TUESDAY_CHILD = "Tuesday";
    private static final String WEDNESDAY_CHILD = "Wednesday";
    private static final String THURSDAY_CHILD = "Thursday";
    private static final String FRIDAY_CHILD = "Friday";
    private static final String SATURDAY_CHILD = "Saturday";

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String restaurantID, hourID;
    private TextView sundayStartHour, sundayEndHour, mondayStartHour, mondayEndHour,
            tuesdayStartHour, tuesdayEndHour, wednesdayStartHour, wednesdayEndHour,
            thursdayStartHour, thursdayEndHour, fridayStartHour, fridayEndHour,
            saturdayStartHour, saturdayEndHour;
    private TimePickerDialog timepickerdialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_hour, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);

        initTextViews(view);
        loadHoursWithRestaurantID();   // retrieve data from database


        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 //               DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);
//                String name = inputName.getText().toString().trim();
//                String address = inputAddress.getText().toString().trim();
//                String phone = inputPhone.getText().toString().trim();
//                String cuisine = inputCuisine.getText().toString().trim();
//                ref.child(NAME_CHILD).setValue(name);
//                ref.child(ADDRESS_CHILD).setValue(address);
//                ref.child(PHONE_CHILD).setValue(phone);
//                ref.child(CUISINE_CHILD).setValue(cuisine);

                Toast.makeText(getActivity(), "Open Hours Saved! ", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void loadHoursWithRestaurantID() {
        Query query = mDatabase.child(HOUR_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NO HOUR FOUND!");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                         //hourID = objSnapshot.getKey();  // another way to get hourID

                        Hour hour = objSnapshot.getValue(Hour.class);
                        if (hour != null) {
                            hourID = hour.getHourID();
                            sundayStartHour.setText(hour.getSunday().get(0));
                            sundayEndHour.setText(hour.getSunday().get(1));
                            mondayStartHour.setText(hour.getMonday().get(0));
                            mondayEndHour.setText(hour.getMonday().get(1));
                            tuesdayStartHour.setText(hour.getTuesday().get(0));
                            tuesdayEndHour.setText(hour.getTuesday().get(1));
                            wednesdayStartHour.setText(hour.getWednesday().get(0));
                            wednesdayEndHour.setText(hour.getWednesday().get(1));
                            thursdayStartHour.setText(hour.getThursday().get(0));
                            thursdayEndHour.setText(hour.getThursday().get(1));
                            fridayStartHour.setText(hour.getFriday().get(0));
                            fridayEndHour.setText(hour.getFriday().get(1));
                            saturdayStartHour.setText(hour.getSaturday().get(0));
                            saturdayEndHour.setText(hour.getSaturday().get(1));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initTextViews(View view) {
        sundayStartHour = (TextView) view.findViewById(R.id.SundayStartHour);
        sundayEndHour = (TextView) view.findViewById(R.id.SundayEndHour);
        mondayStartHour = (TextView) view.findViewById(R.id.MondayStartHour);
        mondayEndHour = (TextView) view.findViewById(R.id.MondayEndHour);
        tuesdayStartHour = (TextView) view.findViewById(R.id.TuesdayStartHour);
        tuesdayEndHour = (TextView) view.findViewById(R.id.TuesdayEndHour);
        wednesdayStartHour = (TextView) view.findViewById(R.id.WednesdayStartHour);
        wednesdayEndHour = (TextView) view.findViewById(R.id.WednesdayEndHour);
        thursdayStartHour = (TextView) view.findViewById(R.id.ThursdayStartHour);
        thursdayEndHour = (TextView) view.findViewById(R.id.ThursdayEndHour);
        fridayStartHour = (TextView) view.findViewById(R.id.FridayStartHour);
        fridayEndHour = (TextView) view.findViewById(R.id.FridayEndHour);
        saturdayStartHour = (TextView) view.findViewById(R.id.SaturdayStartHour);
        saturdayEndHour = (TextView) view.findViewById(R.id.SaturdayEndHour);

        sundayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(sundayStartHour);
            }
        });
        sundayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(sundayEndHour);
            }
        });
        mondayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(mondayStartHour);
            }
        });
        mondayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(mondayEndHour);
            }
        });
        tuesdayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(tuesdayStartHour);
            }
        });
        tuesdayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(tuesdayEndHour);
            }
        });
        wednesdayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(wednesdayStartHour);
            }
        });
        wednesdayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(wednesdayEndHour);
            }
        });
        thursdayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(thursdayStartHour);
            }
        });
        thursdayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(thursdayEndHour);
            }
        });
        fridayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(fridayStartHour);
            }
        });
        fridayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(fridayEndHour);
            }
        });
        saturdayStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(saturdayStartHour);
            }
        });
        saturdayEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(saturdayEndHour);
            }
        });
    }

    private void setTimePicker(final TextView textView) {
        timepickerdialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay % 12;
                        if (hour == 0) {
                            hour = 12;
                        }
                        textView.setText(String.format("%02d:%02d %s", hour, minute,
                                hourOfDay < 12 ? "AM" : "PM"));
                    }
                }, 0, 0, false);

        timepickerdialog.show();
    }
}
