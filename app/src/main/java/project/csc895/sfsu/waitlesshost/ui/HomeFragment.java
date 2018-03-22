package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import project.csc895.sfsu.waitlesshost.model.Table;


public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String MANAGER_ID_CHILD = "managerID";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String NUMBER_CHILD = "numbers";
    private static final String TABLE_CHILD = "tables";
    private static final String STATUS_OPEN = "Open";
    private static final String STATUS_SEATED = "Seated";
    private static final String STATUS_DIRTY = "Dirty";
    public final static String EXTRA_NUMBER_ID = "Pass Number id";
    public final static String EXTRA_RESTAURANT_ID = "Pass Restaurant id";
    private String restaurantID;
    private RecyclerView openRecyclerView, seatedRecyclerView, dirtyRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        // Retrieve data from main activity // !!! problem: sometime can't load restaurantID because of poor internet!!!
//        Bundle args = getArguments();
//        restaurantID = args.getString(ARGS_RESTAURANT_ID);
//        Log.d("restaurantID", restaurantID);

        Bundle args = getArguments();
        if (args != null) {
            restaurantID = args.getString(ARGS_RESTAURANT_ID);
        } else {
            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            loadRestaurantID(mFirebaseUser.getUid());
        }

        initViews(view);
        showList(STATUS_OPEN, openRecyclerView);
        showList(STATUS_SEATED, seatedRecyclerView);
        showList(STATUS_DIRTY, dirtyRecyclerView);

        return view;
    }

    private void loadRestaurantID(String managerID) {
        Query query = mDatabase.child(RESTAURANT_CHILD)
                .orderByChild(MANAGER_ID_CHILD)
                .equalTo(managerID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // show no result view
                if (!dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NO RESULT FOUND!");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                        //restaurantID = objSnapshot.getKey();

                        Restaurant restaurant = objSnapshot.getValue(Restaurant.class);
                        if (restaurant != null) {
                            restaurantID = restaurant.getRestaurantID();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViews(View view) {
        openRecyclerView = (RecyclerView) view.findViewById(R.id.open_list);
        seatedRecyclerView = (RecyclerView) view.findViewById(R.id.seated_list);
        dirtyRecyclerView = (RecyclerView) view.findViewById(R.id.dirty_list);

        openRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        seatedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dirtyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void showList(final String tableStatus, RecyclerView recyclerView) {
        Query query = mDatabase.child(TABLE_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Table, TableViewHolder>(
                Table.class,
                R.layout.item_brief_table,
                TableViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(TableViewHolder viewHolder, Table table, int position) {
                String status = table.getStatus();
                if (status.equals(tableStatus)) {
                    viewHolder.setTableName(table.getTableName());
                    viewHolder.onClick(table);
                } else {
                    viewHolder.mTableNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mTableName.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        private TextView mTableName;
        private LinearLayout mTableNameLinearLayout;

        public TableViewHolder(View itemView) {
            super(itemView);
            mTableName = (TextView) itemView.findViewById(R.id.tableName);
            mTableNameLinearLayout = (LinearLayout)itemView.findViewById(R.id.tableNameLinearLayout);
        }

        public void setTableName(String tableName) {
            mTableName.setText(tableName);
        }

        public void onClick(final Table table) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, NumberDetailedActivity.class);
//                    intent.putExtra(EXTRA_NUMBER_ID, number.getNumberID());// pass number id
//                    intent.putExtra(EXTRA_RESTAURANT_ID, number.getRestaurantID());
//                    context.startActivity(intent);

                    // Alert Dialog
                }
            });
        }
    }
}
