package com.android.voicerecorder;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import voicelist.CollectRecordedVoice;
import voicelist.VoiceListAdapter;
import voicelist.VoiceRecordedModel;

public class MainActivity extends BaseActivity implements View.OnClickListener {
 //   private static TextView mPlayingVoiceTextview;
    private Button mRecordButton;
    private Button mStopRecordButton;
    // private Button mPlayButton;
  //  private Button mStopPlayButton;

    private final Context mContext = MainActivity.this;
    private MediaRecorder mediaRecorder;
    private RecyclerView mVoiceListRecyclerView;
    private VoiceListAdapter mVoiceAdapter;
    List<VoiceRecordedModel> mVoiceRecordedModels = new ArrayList<>();
    private static MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        if (getRecordedVoiceList() != null) {
            initAdapter(mVoiceRecordedModels);
            initRecyclerView(mVoiceAdapter);
        }

        mRecordButton.setOnClickListener(this);
        mStopRecordButton.setOnClickListener(this);

        mStopRecordButton.setEnabled(false);
        //  setButtonDisable(mStopRecordButton);
        //  setButtonDisable(mPlayButton);
      //  mStopPlayButton.setEnabled(false);
        //  setButtonDisable(mStopPlayButton);

        CollectRecordedVoice mVoice = new CollectRecordedVoice();
        mVoice.CollectAllRecordedVoice(mContext);
    }

    private List<VoiceRecordedModel> getRecordedVoiceList() {
        CollectRecordedVoice voice = new CollectRecordedVoice();
        mVoiceRecordedModels = voice.CollectAllRecordedVoice(mContext);
        Toast.makeText(mContext, "voice record: " + mVoiceRecordedModels + "", Toast.LENGTH_SHORT).show();
//        if (!mVoiceRecordedModels.isEmpty()) {
//            initAdapter(mVoiceRecordedModels);
//        }
        return mVoiceRecordedModels;
    }

    private void initAdapter(List<VoiceRecordedModel> mVoiceRecordedModels) {
        // Toast.makeText(mContext, "init:" + mVoiceAdapter, Toast.LENGTH_SHORT).show();
        if (mVoiceAdapter != null) {
            mVoiceAdapter.addNewItem(getRecordedVoiceList());
        } else {
            mVoiceAdapter = new VoiceListAdapter(mContext, mVoiceRecordedModels);
        }
    }

    private void initRecyclerView(VoiceListAdapter mVoiceAdapter) {
        mVoiceListRecyclerView.setAdapter(mVoiceAdapter);
        setLayoutManager();
    }

    private void setLayoutManager() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mVoiceListRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void setButtonEnable(Button mEnableBtn) {
        mEnableBtn.setEnabled(true);
    }

    private void setButtonDisable(Button mDisableBtn) {
        mDisableBtn.setEnabled(false);
    }

    private void findViews() {
        mRecordButton = findViewById(R.id.record_button);
        mStopRecordButton = findViewById(R.id.stopRecord_button);
      //  mStopPlayButton = findViewById(R.id.stopPlay_button);
        mVoiceListRecyclerView = findViewById(R.id.voiceList_recyclerview);
      //  mPlayingVoiceTextview = findViewById(R.id.playingVoice_textview);
    }

    @Override
    public void onClick(View clickedButton) {
        switch (clickedButton.getId()) {
            case R.id.record_button:
             //   new Thread(new Runnable() {
              //      @Override
              //      public void run() {
                        startRecording();
             //      }
             //   }).start();
                break;

            case R.id.stopRecord_button: {
                initAdapter(mVoiceRecordedModels);
                stopRecording();
                break;

            }
        }
    }

    private void startRecording() {
        if (checkPermission(mContext)) {
            makeDirectoryForVoiceRecord();
            MediaRecorderReady();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecordButton.setEnabled(false);
           // setButtonDisable(mRecordButton);
          //  setButtonEnable(mStopRecordButton);
            mStopRecordButton.setEnabled(true);
        } else {
            requestPermission((Activity) mContext);
        }
    }

    private void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(createRecordFileName());
    }

    private void stopRecording() {
        mediaRecorder.stop();
        setButtonDisable(mStopRecordButton);
        // setButtonEnable(mPlayButton);
        setButtonEnable(mRecordButton);
     //   setButtonDisable(mStopPlayButton);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

//    public void playClickedVoice(String filePath) {
//
//        mStopRecordButton.setEnabled(false);
//        mRecordButton.setEnabled(false);
//            //    mStopPlayButton.setEnabled(true);
//
//
//
//
//        mMediaPlayer = new MediaPlayer();
//        try {
//            mMediaPlayer.setDataSource(filePath);
//            mMediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(LOG_TAG, LOG_MESSAGE_PLAY_ERROR);
//        }
//        mMediaPlayer.start();
////        isPlayingVoice(voiceName);
//    }

 //   public static void isPlayingVoice(String voiceName) {
      //  mPlayingVoiceTextview.setText(voiceName);
 //  }


//    public boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
//                WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
//                RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED &&
//                result1 == PackageManager.PERMISSION_GRANTED;
//    }

//        mPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) throws IllegalArgumentException,
//                    SecurityException, IllegalStateException {
//                boolean FALSE=false;
//                mStopRecordButton.setEnabled(FALSE);
//                mRecordButton.setEnabled(false);
//                mStopPlayButton.setEnabled(true);
//
//                mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(AUDIO_FILE_PATH);
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                mediaPlayer.start();
//                Toast.makeText(MainActivity.this, "Recording Playing",
//                        Toast.LENGTH_LONG).show();
//            }
//        });

//        mStopPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mStopRecordButton.setEnabled(false);
//                mRecordButton.setEnabled(true);
//                mStopPlayButton.setEnabled(false);
//                mPlayButton.setEnabled(true);
//
//                if (mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    MediaRecorderReady();
//                }
//            }
//        });

    //    private int writeFileNameIndex() {
//        SharedPreferences sharedpreferences =
//                getSharedPreferences(PREFERENCES_FILE_NAME_INDEX, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        int index = readFileNameIndex();
//        editor.putInt(PREFERENCES_KEY, index);
//        editor.commit();
//        return index;
//    }

//    private int readFileNameIndex() {
//        int index = PreferenceManager.getDefaultSharedPreferences(mContext)
//                .getInt(PREFERENCES_KEY, 0);
//        index += 1;
//        return index;
//    }

//    private String createFileName() {
//        fileIndex = writeFileNameIndex();
//        return fileNamePrefix + fileIndex + fileNamePostfix;
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}