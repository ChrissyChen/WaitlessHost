package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Restaurant;



public class GeneralFragment extends Fragment {

    private static final String TAG = "General Info Fragment";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String NAME_CHILD = "name";
    private static final String ADDRESS_CHILD = "address";
    private static final String PHONE_CHILD = "telephone";
    private static final String CUISINE_CHILD = "cuisine";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String restaurantID;
    private EditText inputName, inputAddress, inputPhone, inputCuisine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);

        inputName = (EditText) view.findViewById(R.id.name);
        inputAddress = (EditText) view.findViewById(R.id.address);
        inputPhone = (EditText) view.findViewById(R.id.phone);
        inputCuisine = (EditText) view.findViewById(R.id.cuisine);
        ImageView imageUpload = (ImageView) view.findViewById(R.id.imageUpload);

        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);
                String name = inputName.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String cuisine = inputCuisine.getText().toString().trim();
                ref.child(NAME_CHILD).setValue(name);
                ref.child(ADDRESS_CHILD).setValue(address);
                ref.child(PHONE_CHILD).setValue(phone);
                ref.child(CUISINE_CHILD).setValue(cuisine);
                // TODO update image
                Toast.makeText(getActivity(), "General Information Saved! ", Toast.LENGTH_SHORT).show();
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
                    inputName.setText(name);
                    inputAddress.setText(address);
                    inputPhone.setText(phone);
                    inputCuisine.setText(cuisine);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
