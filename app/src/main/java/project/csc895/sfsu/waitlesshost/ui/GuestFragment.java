package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Number;


public class GuestFragment extends Fragment {

    private static final String TAG = "Guest Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String NUMBER_CHILD = "numbers";
    private static final String STATUS_WAITING = "Waiting";
    private static final String STATUS_DINING = "Dining";
    private static final String STATUS_CANCELLED = "Cancelled";
    private static final String STATUS_COMPLETED = "Completed";
    public final static String EXTRA_NUMBER_ID = "Pass Number id";
    public final static String EXTRA_RESTAURANT_ID = "Pass Restaurant id";
    private String restaurantID;
    private LinearLayout guestLinearLayout;
    private TextView noGuestTextView;
    private RecyclerView waitingRecyclerView, diningRecyclerView, cancelledRecyclerView,completedRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);
        Log.d("restaurantID", restaurantID);

        initViews(view);
        showWaitingList();
        showDiningList();
        showCancelledList();
        showCompletedList();

        return view;
    }

    private void initViews(View view) {
        guestLinearLayout = (LinearLayout) view.findViewById(R.id.guestLinearLayout);
        noGuestTextView = (TextView) view.findViewById(R.id.noGuest);

        waitingRecyclerView = (RecyclerView) view.findViewById(R.id.waiting_list);
        diningRecyclerView = (RecyclerView) view.findViewById(R.id.dining_list);
        cancelledRecyclerView = (RecyclerView) view.findViewById(R.id.cancelled_list);
        completedRecyclerView = (RecyclerView) view.findViewById(R.id.completed_list);

        waitingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        diningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cancelledRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showWaitingList() {
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
                if (status.equals(STATUS_WAITING)) {
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.onClick(number);
                } else {
                    viewHolder.mNumberNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mNumberName.setVisibility(View.GONE);
                }
            }
        };
        waitingRecyclerView.setAdapter(adapter);

//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // show no result view
//                if (!dataSnapshot.hasChildren()) {
//                    waitingRecyclerView.setVisibility(View.GONE);
//                    noGuestTextView.setVisibility(View.VISIBLE);
//                    Log.d(TAG, "NO GUEST SHOWS");
//                } else {
//                    waitingRecyclerView.setVisibility(View.VISIBLE);
//                    noGuestTextView.setVisibility(View.GONE);
//                    Log.d(TAG, "GUEST VIEW SHOWS");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void showDiningList() {
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
                if (status.equals(STATUS_DINING)) {
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.onClick(number);
                } else {
                    viewHolder.mNumberNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mNumberName.setVisibility(View.GONE);
                }
            }
        };
        diningRecyclerView.setAdapter(adapter);
    }

    private void showCancelledList() {
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
                if (status.equals(STATUS_CANCELLED)) {
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.onClick(number);
                } else {
                    viewHolder.mNumberNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mNumberName.setVisibility(View.GONE);
                }
            }
        };
        cancelledRecyclerView.setAdapter(adapter);
    }

    private void showCompletedList() {
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
                if (status.equals(STATUS_COMPLETED)) {
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.onClick(number);
                } else {
                    viewHolder.mNumberNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mNumberName.setVisibility(View.GONE);
                }
            }
        };
        completedRecyclerView.setAdapter(adapter);
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumberName;
        private LinearLayout mNumberNameLinearLayout;

        public NumberViewHolder(View itemView) {
            super(itemView);
            mNumberName = (TextView) itemView.findViewById(R.id.numberName);
            mNumberNameLinearLayout = (LinearLayout)itemView.findViewById(R.id.numberNameLinearLayout);
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
}
