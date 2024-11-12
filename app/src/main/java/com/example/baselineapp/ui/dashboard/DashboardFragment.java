package com.example.baselineapp.ui.dashboard;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.Globals;
import com.example.baselineapp.R;
import com.example.baselineapp.databinding.FragmentDashboardBinding;

import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.*;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.MediaItem;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static boolean bool_pageUpdaterCreated;

    private ExoPlayer player;


    @OptIn(markerClass = UnstableApi.class)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        //Code starts here
        //-----------------


        if(binding.idVideoPlayer.getPlayer() == null)
        {
            player = new ExoPlayer.Builder(binding.getRoot().getContext()).build();
            // Global settings.
            ExoPlayer player =
                    new ExoPlayer.Builder(binding.getRoot().getContext())
                            .setMediaSourceFactory(
                                    new DefaultMediaSourceFactory(binding.getRoot().getContext())
                                            .setLiveTargetOffsetMs(5000))
                            .build();


            //If you're on a hotspot it'll be nanny.local
            //Uri mediaUri = Uri.parse("http://192.168.90.132:5000/video_feed");
            //Above address seems outdated
            Uri mediaUri = Uri.parse("http://192.168.0.234:5000/video_feed");
            //Otherwise, use nanny
            //Uri mediaUri = Uri.parse("https://nanny.local:8000/video");

            // Per MediaItem settings.
            MediaItem mediaItem =
                    new MediaItem.Builder()
                            .setUri(mediaUri)
                            .setMimeType(MimeTypes.VIDEO_MJPEG)
                            .setLiveConfiguration(
                                    new MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build())
                            .build();
            player.setMediaItem(mediaItem);

            binding.idVideoPlayer.setPlayer(player);
        }

        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.play();

        /*
        if(binding.idVideoPlayer.getPlayer() == null)
        {

            //If you're on a hotspot it'll be nanny.local
            Uri mediaUri = Uri.parse("http://192.168.90.132:5000/video_feed");
            player = new ExoPlayer.Builder(binding.getRoot().getContext()).build();
            // Set the media item to be played with the desired duration.
            player.setMediaItem(
                    new MediaItem.Builder().setUri(mediaUri).setImageDurationMs(1000).build());
            // Prepare the player.
            player.prepare();
            binding.idVideoPlayer.setPlayer(player);
        }
        */




        if(!bool_pageUpdaterCreated)
        {
            updatePage();
            RepeatTask();
        }

        //-----------------

        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        WebView myWebView = (WebView) getView().findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        myWebView.loadUrl("http://192.168.0.234:5000/video_feed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updatePage()
    {
        double dbl_bloodOxValue = Globals.getBloodOxVal();
        double dbl_tempValue = Globals.getTempVal();
        double dbl_pulseValue = Globals.getPulseVal();

        //Get all of our data from the Globals class where its maintained
        double dbl_bloodOxLowWarningThreshold = Globals.getBloodOxLowWarningThreshold();
        double dbl_bloodOxLowCautionThreshold = Globals.getBloodOxLowCautionThreshold();

        double dbl_pulseLowWarningThreshold = Globals.getPulseLowWarningThreshold();
        double dbl_pulseLowCautionThreshold = Globals.getPulseLowCautionThreshold();
        double dbl_pulseHighWarningThreshold = Globals.getPulseHighWarningThreshold();
        double dbl_pulseHighCautionThreshold = Globals.getPulseHighCautionThreshold();

        double dbl_tempLowWarningThreshold = Globals.getTempLowWarningThreshold();
        double dbl_tempLowCautionThreshold = Globals.getTempLowCautionThreshold();
        double dbl_tempHighWarningThreshold = Globals.getTempHighWarningThreshold();
        double dbl_tempHighCautionThreshold = Globals.getTempHighCautionThreshold();

        int color_healthyGreen = getResources().getColor(R.color.healthy_green, getActivity().getTheme());
        int color_cautionYellow = getResources().getColor(R.color.caution_yellow, getActivity().getTheme());
        int color_warningRed = getResources().getColor(R.color.warning_red, getActivity().getTheme());
        int color_black = getResources().getColor(R.color.black, requireContext().getTheme());
        int color_white = getResources().getColor(R.color.white, requireContext().getTheme());

        String str_bloodOxTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_bloodOxValue), "%");
        String str_tempTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_tempValue), "F");
        String str_pulseTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_pulseValue), "bpm");

        if(binding == null) return;

        if(dbl_bloodOxValue <= dbl_bloodOxLowWarningThreshold)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_warningRed);
            binding.idBloodOxTextBox.setTextColor(color_white);
        }
        else if(dbl_bloodOxValue <= dbl_bloodOxLowCautionThreshold)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_cautionYellow);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_healthyGreen);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }

        if(dbl_pulseValue <= dbl_pulseLowWarningThreshold || dbl_pulseValue >= dbl_pulseHighWarningThreshold)
        {
            binding.idPulseTextBox.setBackgroundColor(color_warningRed);
            binding.idPulseTextBox.setTextColor(color_white);
        }
        else if(dbl_pulseValue <= dbl_pulseLowCautionThreshold || dbl_pulseValue >= dbl_pulseHighCautionThreshold)
        {
            binding.idPulseTextBox.setBackgroundColor(color_cautionYellow);
            binding.idPulseTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idPulseTextBox.setBackgroundColor(color_healthyGreen);
            binding.idPulseTextBox.setTextColor(color_black);
        }

        if(dbl_tempValue <= dbl_tempLowWarningThreshold || dbl_tempValue >= dbl_tempHighWarningThreshold)
        {
            binding.idTempTextBox.setBackgroundColor(color_warningRed);
            binding.idTempTextBox.setTextColor(color_white);
        }
        else if(dbl_tempValue <= dbl_tempLowCautionThreshold || dbl_tempValue >= dbl_tempHighCautionThreshold)
        {
            binding.idTempTextBox.setBackgroundColor(color_cautionYellow);
            binding.idTempTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idTempTextBox.setBackgroundColor(color_healthyGreen);
            binding.idTempTextBox.setTextColor(color_black);
        }

        binding.idBloodOxTextBox.setText(str_bloodOxTextBoxValue);
        binding.idPulseTextBox.setText(str_pulseTextBoxValue);
        binding.idTempTextBox.setText(str_tempTextBoxValue);
    }

    private void RepeatTask()
    {
        bool_pageUpdaterCreated = true;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (Globals.userLoggedIn())
                {
                    try {
                        // Update TextView in runOnUiThread
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                updatePage();
                            }
                        });
                    }catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }


                    try
                    {
                        // Sleep for 3 seconds
                        Thread.sleep(3*1000);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        bool_pageUpdaterCreated = false;
    }


}