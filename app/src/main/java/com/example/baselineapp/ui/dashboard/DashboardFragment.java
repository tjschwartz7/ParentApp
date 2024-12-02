package com.example.baselineapp.ui.dashboard;

//import android.media.MediaPlayer;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.Globals;
import com.example.baselineapp.R;
import com.example.baselineapp.databinding.FragmentDashboardBinding;

//import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
//import androidx.media3.exoplayer.*;
//import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
//import androidx.media3.ui.PlayerView;
//import androidx.media3.common.MediaItem;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.media.VideoView;

import java.net.Socket;
import java.util.ArrayList;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static boolean bool_pageUpdaterCreated;

    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    @OptIn(markerClass = UnstableApi.class)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Code starts here
        //-----------------

        /*
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
            Uri mediaUri = Uri.parse("http://192.168.90.132:5000/video_feed");
            //Otherwise, use nanny
            //Uri mediaUri = Uri.parse("https://nanny.local:8000/video");

            // Per MediaItem settings.
            MediaItem mediaItem =
                    new MediaItem.Builder()
                            .setMimeType(MimeTypes.IMAGE_JPEG)
                            .setUri(mediaUri)
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
        */

        /*while(!Globals.connectionEstablished)
        {
            //Loop until the VideoServerService establishes the connection
        }*/

        /*
        if(Globals.connectionSocket == null || Globals.connectionSocket.isClosed())
        {
            System.out.println("BOOM BOOM BOOM BOOM BLDGDGDGDG");
            try
            {
                Globals.connectionSocket = null;
                // Connect to the server
                System.out.println("Server hostname " + Globals.serverHostname);
                System.out.println("TCP: " + Globals.TCP_PORT);

                //TODO: THe code fails RIGHT HERE! The socket returns null! I'm plastic-man!
                Thread connectionThread = new Thread(() ->
                {
                    try
                    {
                        //Socket s = new Socket(Globals.serverHostname, Globals.TCP_PORT);
                        Globals.connectionSocket = new Socket(Globals.serverHostname, Globals.TCP_PORT);
                        Globals.connectionSocket.setSoTimeout(Globals.socketTimeoutMillis);  // Set a 20-second timeout
                        System.out.println("Connected to server at " + Globals.serverHostname + ":" + Globals.TCP_PORT);
                    }
                    catch (Exception e)
                    {
                        Log.e("SocketDebug", "Error connecting to Pi server", e);
                    }
                });
                connectionThread.start();
                try
                {
                    // Wait for the thread to finish
                    connectionThread.join();
                    System.out.println("Connection thread has finished.");
                }
                catch (InterruptedException ex)
                {
                    Log.e("ThreadDebug", "Thread was interrupted while waiting for connection thread to finish", ex);
                    System.out.println("Thread was interrupted while waiting for connection thread to finish: " + ex.getMessage());

                }
            }
            catch(Exception ex)
            {
                System.out.println( "Exception: " + ex.getMessage());
            }

            //Attempt to close the socket
            try {
                if (Globals.connectionSocket != null && !Globals.connectionSocket.isClosed()) {
                    Globals.connectionSocket.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
         */


        if(mediaPlayer == null)
        {
            try
            {
                AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                int result = audioManager.requestAudioFocus(focusChange -> {}, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    Log.d("Audio", "Audio focus granted");
                } else {
                    Log.e("Audio", "Audio focus request failed");
                }
                /*
                System.out.println("REACHED EXOPLAYER START");
                ExoPlayer player;
                //If you're on a hotspot it'll be nanny.local
                Uri mediaUri = Uri.parse("udp://192.168.0.234:" + Globals.UDP_PORT);
                player = new ExoPlayer.Builder(binding.getRoot().getContext()).build();

                player.setMediaItem(MediaItem.fromUri(mediaUri));
                // Prepare the player.
                player.prepare();
                binding.idVideoPlayer.setPlayer(player);
                 */
                // Initialize VLC
                ArrayList<String> options = new ArrayList<>();
                //options.add("--rtsp-tcp"); // Add options as needed
                libVLC = new LibVLC(requireContext(), options);
                mediaPlayer = new MediaPlayer(libVLC);

                // Set Media Source
                //String streamUrl = "udp://@192.168.0.234:13003"; // Update with your UDP stream
                Uri mediaUri = Uri.parse("rtsp://192.168.0.234:8554/live.stream");
                Media media = new Media(libVLC, mediaUri);
                //media.addOption("--codec=avcodec");
                //media.addOption("--aout=opensles");
                //media.addOption("--audio-time-stretch");
                //media.addOption("--audio-buffering=1000"); // 1000 ms (1 second) audio buffer
                //media.addOption("--audio-resampler=soxr"); // Use high-quality resampling
                //media.addOption("--resample=44100");      // Resample to 44100 Hz
                //media.addOption("--channels=1");          // Force stereo output
                //media.addOption("--low-latency");
                //media.addOption("-vvv");
                media.setHWDecoderEnabled(true, false);
                media.addOption(":network-caching=150");
                media.addOption(":clock-jitter=0");
                media.addOption(":clock-synchro=0");
                mediaPlayer.setMedia(media);
                // Attach VideoView to VLC
                videoView = root.findViewById(R.id.videoView);
                mediaPlayer.getVLCVout().setVideoSurface(videoView.getHolder().getSurface(), videoView.getHolder());
                mediaPlayer.getVLCVout().setWindowSize(videoView.getWidth(), videoView.getHeight());
                mediaPlayer.getVLCVout().attachViews();
                // Start playback
                mediaPlayer.play();
            }
            catch(Exception ex)
            {
                Log.e("ExoplayerError", "Error starting ExoPlayer", ex);
                System.out.println("Error starting ExoPlayer: " + ex.getMessage());
            }
        }


        /*
        surfaceView = rootView.findViewById(R.id.surfaceView);

        surfaceView.post(() ->
        {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback()
            {
                @Override
                public void surfaceCreated(SurfaceHolder holder)
                {
                    startMediaPlayer(holder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
                {
                    // Handle surface changes if needed
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    releaseMediaPlayer();
                }
            });
        });
        */

        if(!bool_pageUpdaterCreated)
        {
            updatePage();
            RepeatTask();
        }

        //-----------------

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //releaseMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (libVLC != null) {
            libVLC.release();
            libVLC = null;
        }
        binding = null;
    }

    /*
    private void startMediaPlayer(SurfaceHolder holder) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                Uri mediaUri = Uri.parse("udp://192.168.1.100:" + Globals.UDP_PORT); // Replace with your actual IP and port

                mediaPlayer.setDataSource(this.getContext(), mediaUri);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Log.e("MediaPlayerError", "Error: " + what + ", " + extra);
                    return true;
                });

                mediaPlayer.prepareAsync(); // Use async to avoid blocking the UI thread
            }
        } catch (Exception e) {
            Log.e("MediaPlayerError", "Failed to initialize MediaPlayer", e);
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    */
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