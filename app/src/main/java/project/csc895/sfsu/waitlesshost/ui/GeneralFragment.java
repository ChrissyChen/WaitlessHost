package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.csc895.sfsu.waitlesshost.R;


public class GeneralFragment extends Fragment {

    private static final String TAG = "General Info Fragment";
    public final static String EXTRA_SEARCH = "Pass Search Tag";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        EditText inputRestaurantName = (EditText) view.findViewById(R.id.restaurant_name);
        EditText inputAddress = (EditText) view.findViewById(R.id.address);
        EditText inputPhone = (EditText) view.findViewById(R.id.phone);
        EditText inputCuisine = (EditText) view.findViewById(R.id.cuisine);
        ImageView imageUpload = (ImageView) view.findViewById(R.id.imageUpload);
        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        return view;
    }
}
