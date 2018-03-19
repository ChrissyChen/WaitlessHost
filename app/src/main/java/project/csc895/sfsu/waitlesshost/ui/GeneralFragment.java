package project.csc895.sfsu.waitlesshost.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Restaurant;

public class GeneralFragment extends Fragment {

    private static final String TAG = "General Info Fragment";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String NAME_CHILD = "name";
    private static final String ADDRESS_CHILD = "address";
    private static final String PHONE_CHILD = "telephone";
    private static final String CUISINE_CHILD = "cuisine";
    private static final String IMAGE_URL_CHILD = "imageUrl";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final int REQUEST_PICK_IMAGE = 1;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String restaurantID;
    private EditText inputName, inputAddress, inputPhone, inputCuisine;
    private ImageView logo;
    private static FirebaseStorage sStorage = FirebaseStorage.getInstance();
    private FirebaseUser mUser;
    private Uri filePath;
    private StorageReference storageReference = sStorage.getReference();

    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);
        Log.d("restaurantID", restaurantID);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        initViews(view);
        loadRestaurantRecordValue();

        return view;
    }

    private void initViews(View view) {
        inputName = (EditText) view.findViewById(R.id.name);
        inputAddress = (EditText) view.findViewById(R.id.address);
        inputPhone = (EditText) view.findViewById(R.id.phone);
        inputPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        inputCuisine = (EditText) view.findViewById(R.id.cuisine);
        logo = (ImageView) view.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRestaurantInfo();
            }
        });
    }

    private void loadRestaurantRecordValue() {
        DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                if (restaurant != null) {
                    String name = restaurant.getName();
                    String address = restaurant.getAddress();
                    String phone = restaurant.getTelephone();
                    String cuisine = restaurant.getCuisine();
                    String imageUrl = restaurant.getImageUrl();

                    inputName.setText(name);
                    inputAddress.setText(address);
                    inputPhone.setText(phone);
                    inputCuisine.setText(cuisine);

                    // get image
                    if (imageUrl != null) {  // if has value in imageUrl, then load the uri into logo ImageView
                        final StorageReference gsReference = sStorage.getReferenceFromUrl(imageUrl);
                        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("image uri", uri.toString());
                                Log.d(TAG,GeneralFragment.this.toString());
                                //Log.d("activity",getActivity().toString());
                                Glide.with(getActivity())
                                        .load(uri)
                                        .into(logo);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e(TAG, "Could not load image for message", exception);
                            }
                        });
                    } // if imageUrl is null, display the default image of logo ImageView

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                filePath = data.getData();   // filePath is where the image is stored on device photo
                Log.d("uri filepath", filePath.toString()); //uri filepath: content://com.android.providers.media.documents/document/image%3A5191

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    Log.d(TAG, getActivity().toString());
                    logo.setImageBitmap(bitmap);    // display picked image
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void updateRestaurantInfo() {
        DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);

        String name = inputName.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String cuisine = inputCuisine.getText().toString().trim();

        ref.child(NAME_CHILD).setValue(name);
        ref.child(ADDRESS_CHILD).setValue(address);
        ref.child(PHONE_CHILD).setValue(phone);
        ref.child(CUISINE_CHILD).setValue(cuisine);

        uploadImage();

        Toast.makeText(getActivity(), "General Information Saved! ", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading Logo Image...");
            progressDialog.show();

            final StorageReference imageRef = storageReference.child(mUser.getUid() + "/logo");
            imageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Logo Uploaded", Toast.LENGTH_SHORT).show();

                            updateRestaurantImageUrl(imageRef.toString());
                            Log.d("update image url db", "success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void updateRestaurantImageUrl(String imageRef) {
        DatabaseReference ref = mDatabase.child(RESTAURANT_CHILD).child(restaurantID);
        ref.child(IMAGE_URL_CHILD).setValue(imageRef);
    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mActivity = activity;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mActivity = null;
//    }

}