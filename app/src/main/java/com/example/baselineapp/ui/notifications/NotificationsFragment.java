package com.example.baselineapp.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.Globals;
import com.example.baselineapp.MainActivity;
import com.example.baselineapp.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int N = Globals.getNumNotifications(); // total number of textviews to add

        for (int i = 0; i < N; i++) {
            // create a new textview
            final TextView rowTextView = new TextView(getActivity());

            // set some properties of rowTextView or something
            rowTextView.setText(Globals.getNotificationString(i));
            rowTextView.setTextSize(24);
            // add the textview to the linearlayout
            binding.idContainer.addView(rowTextView);
        }
        


        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}