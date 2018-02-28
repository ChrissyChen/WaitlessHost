package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.csc895.sfsu.waitlesshost.R;


public class OpenHourFragment extends Fragment {

    private static final String TAG = "Open Hour Fragment";
    public final static String EXTRA_SEARCH = "Pass Search Tag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_hour, container, false);

        return view;
    }
}
