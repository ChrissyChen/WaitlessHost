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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Restaurant;


public class OpenHourFragment extends Fragment {

    private static final String TAG = "Open Hour Fragment";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String NAME_CHILD = "name";
    private static final String ADDRESS_CHILD = "address";
    private static final String PHONE_CHILD = "telephone";
    private static final String CUISINE_CHILD = "cuisine";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String restaurantID;
    private TextView sundayHours, mondayHours, tuesdayHours, wednesdayHours, thursdayHours, fridayHours, saturdayHours;
    private TimePickerDialog timepickerdialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_hour, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);

        sundayHours = (TextView) view.findViewById(R.id.SundayHours);
        mondayHours = (TextView) view.findViewById(R.id.MondayHours);
        tuesdayHours = (TextView) view.findViewById(R.id.TuesdayHours);
        wednesdayHours = (TextView) view.findViewById(R.id.WednesdayHours);
        thursdayHours = (TextView) view.findViewById(R.id.ThursdayHours);
        fridayHours = (TextView) view.findViewById(R.id.FridayHours);
        saturdayHours = (TextView) view.findViewById(R.id.SaturdayHours);
        LinearLayout sundayLine = (LinearLayout) view.findViewById(R.id.SundayLine);
        sundayLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerdialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                int hour = hourOfDay % 12;
                                if (hour == 0) {
                                    hour = 12;
                                }
                                sundayHours.setText(String.format("%02d:%02d %s", hour, minute,
                                        hourOfDay < 12 ? "AM" : "PM"));
                            }
                        }, 0, 0, false);

                timepickerdialog.show();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);
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

        loadRestaurantRecordValueWithID(restaurantID);

        return view;
    }

    private void loadRestaurantRecordValueWithID(String key) {
        DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(key);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                if (restaurant != null) {
                    String name = restaurant.getName();
                    String address = restaurant.getAddress();
                    String phone = restaurant.getTelephone();
                    String cuisine = restaurant.getCuisine();
                    // TODO get image
                    Log.d("name: ", name);
                    Log.d("address: ", address);
                    Log.d("phone: ", phone);
                    Log.d("cuisine: ", cuisine);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
