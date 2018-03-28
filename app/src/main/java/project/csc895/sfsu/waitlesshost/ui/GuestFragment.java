package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;


import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Number;
import project.csc895.sfsu.waitlesshost.model.Waitlist;


public class GuestFragment extends Fragment {

    private static final String TAG = "Guest Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String WAITLIST_CHILD = "waitlists";
    private static final String COUNTER_TABLE_A_CHILD = "counterTableA";
    private static final String COUNTER_TABLE_B_CHILD = "counterTableB";
    private static final String COUNTER_TABLE_C_CHILD = "counterTableC";
    private static final String COUNTER_TABLE_D_CHILD = "counterTableD";
    private static final String NUMBER_CHILD = "numbers";
    private static final String STATUS_WAITING = "Waiting";
    private static final String STATUS_DINING = "Dining";
    private static final String STATUS_CANCELLED = "Cancelled";
    private static final String STATUS_COMPLETED = "Completed";
    public final static String EXTRA_NUMBER_ID = "Pass Number id";
    public final static String EXTRA_RESTAURANT_ID = "Pass Restaurant id";
    private String restaurantID, waitlistID;
    private TextView noGuestTextView;
    private ImageView addNumberImageView;
    private RecyclerView waitingRecyclerView, diningRecyclerView, cancelledRecyclerView,completedRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private LinearLayoutManager waitingLinearLayoutManager, diningLinearLayoutManager, cancelLinearLayoutManager, completedLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest, container, false);
        setHasOptionsMenu(true);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);
        Log.d("restaurantID", restaurantID);

        initViews(view);
        loadNumberInfo();
        getWaitlistInfo();

        return view;
    }

    private void initViews(View view) {
        noGuestTextView = (TextView) view.findViewById(R.id.noGuest);
        addNumberImageView = (ImageView) view.findViewById(R.id.addNewNumber);
        addNumberImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // restaurant will help walk-in guests get a number before seated
                Intent intent = new Intent(getActivity(), RestaurantGetNumberActivity.class);
                intent.putExtra(EXTRA_RESTAURANT_ID, restaurantID);
                startActivity(intent);
            }
        });

        waitingRecyclerView = (RecyclerView) view.findViewById(R.id.waiting_list);
        diningRecyclerView = (RecyclerView) view.findViewById(R.id.dining_list);
        cancelledRecyclerView = (RecyclerView) view.findViewById(R.id.cancelled_list);
        completedRecyclerView = (RecyclerView) view.findViewById(R.id.completed_list);

        waitingLinearLayoutManager = new LinearLayoutManager(getActivity());
        diningLinearLayoutManager = new LinearLayoutManager(getActivity());
        cancelLinearLayoutManager = new LinearLayoutManager(getActivity());
        completedLinearLayoutManager = new LinearLayoutManager(getActivity());

        waitingRecyclerView.setLayoutManager(waitingLinearLayoutManager);
        diningRecyclerView.setLayoutManager(diningLinearLayoutManager);
        cancelledRecyclerView.setLayoutManager(cancelLinearLayoutManager);
        completedRecyclerView.setLayoutManager(completedLinearLayoutManager);
    }

    private void loadNumberInfo() {
        showList(STATUS_WAITING, waitingRecyclerView);
        showList(STATUS_DINING, diningRecyclerView);
        showList(STATUS_CANCELLED, cancelledRecyclerView);
        showList(STATUS_COMPLETED, completedRecyclerView);
    }

    private void showList(final String tableStatus, RecyclerView recyclerView) {
        Query query = mDatabase.child(NUMBER_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Number, NumberViewHolder>(
                Number.class,
                R.layout.item_brief_number,
                NumberViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(NumberViewHolder viewHolder, Number number, int position) {
                String status = number.getStatus();
                if (status.equals(tableStatus)) {
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.onClick(number);
                } else {
                    viewHolder.mNumberNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mNumberName.setVisibility(View.GONE);
                    viewHolder.mSeparator.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumberName;
        private LinearLayout mNumberNameLinearLayout;
        private View mSeparator;

        public NumberViewHolder(View itemView) {
            super(itemView);
            mNumberName = (TextView) itemView.findViewById(R.id.numberName);
            mNumberNameLinearLayout = (LinearLayout)itemView.findViewById(R.id.numberNameLinearLayout);
            mSeparator = itemView.findViewById(R.id.numberSeparator);
        }

        public void setNumberName(String numberName) {
            mNumberName.setText(numberName);
        }

        public void onClick(final Number number) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, NumberDetailedActivity.class);
                    intent.putExtra(EXTRA_NUMBER_ID, number.getNumberID());// pass number id
                    intent.putExtra(EXTRA_RESTAURANT_ID, number.getRestaurantID());
                    context.startActivity(intent);
                }
            });
        }
    }

    private void getWaitlistInfo() {
        Query query = mDatabase.child(WAITLIST_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Log.d("Error", "NO WAITLIST SHOWS");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                        Waitlist waitlist = objSnapshot.getValue(Waitlist.class);
                        if (waitlist != null) {
                            waitlistID = waitlist.getWaitlistID();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNumberInfo();  // refresh if data changes
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.guest_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearCancelledListAction:
                // todo
                Toast.makeText(getActivity(), "clear cancelled button clicked!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.clearCompletedListAction:
                // todo
                Toast.makeText(getActivity(), "clear completed button clicked!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.resetWaitlistCounterAction:
                showConfirmResetCounterAlertDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmResetCounterAlertDialog() {
        String message = "Are you sure to reset the waitlist counter?";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetWaitlistCounter();
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void resetWaitlistCounter() {
        if (checkIfAllListEmpty()) {
            updateWaitlistCounter();
        }
    }

    private boolean checkIfAllListEmpty() {
        int waitingPosition = waitingLinearLayoutManager.findFirstVisibleItemPosition();
        int diningPosition = diningLinearLayoutManager.findFirstVisibleItemPosition();
        int cancelledPosition = cancelLinearLayoutManager.findFirstVisibleItemPosition();
        int completedPosition = completedLinearLayoutManager.findFirstVisibleItemPosition();
        Log.d("waiting position", String.valueOf(waitingPosition));
        Log.d("dining position", String.valueOf(diningPosition));
        Log.d("cancelled position", String.valueOf(cancelledPosition));
        Log.d("completed position", String.valueOf(completedPosition));

        if (waitingPosition == RecyclerView.NO_POSITION && diningPosition == RecyclerView.NO_POSITION &&
                cancelledPosition == RecyclerView.NO_POSITION && completedPosition == RecyclerView.NO_POSITION) {
            return true;

        } else if (waitingPosition != RecyclerView.NO_POSITION) {
            Toast.makeText(getActivity(), "Reset Counter Failed! Waiting list is not empty!", Toast.LENGTH_LONG).show();
            return false;
        } else if (diningPosition != RecyclerView.NO_POSITION) {
            Toast.makeText(getActivity(), "Reset Counter Failed! Dining list is not empty!", Toast.LENGTH_LONG).show();
            return false;
        } else if (cancelledPosition != RecyclerView.NO_POSITION) {
            Toast.makeText(getActivity(), "Reset Counter Failed! Cancelled list is not empty!", Toast.LENGTH_LONG).show();
            return false;
        } else if (completedPosition != RecyclerView.NO_POSITION) {
            Toast.makeText(getActivity(), "Reset Counter Failed! Completed list is not empty!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return false;
        }
    }

    private void updateWaitlistCounter() {
        DatabaseReference ref = mDatabase.child(WAITLIST_CHILD).child(waitlistID);
        ref.child(COUNTER_TABLE_A_CHILD).setValue(0);
        ref.child(COUNTER_TABLE_B_CHILD).setValue(0);
        ref.child(COUNTER_TABLE_C_CHILD).setValue(0);
        ref.child(COUNTER_TABLE_D_CHILD).setValue(0);
        Toast.makeText(getActivity(), "Reset Counter Succeed!", Toast.LENGTH_SHORT).show();
    }
}
