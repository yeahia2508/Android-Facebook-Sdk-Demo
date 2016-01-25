package com.gitproject.y34h1a.facebooksdkdemo.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Privacy;
import com.sromku.simple.fb.entities.Video;
import com.sromku.simple.fb.listeners.OnPublishListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SendVideo extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    VideoView ivPostVideo;
    Button btAttachVideo,btPostFeed;
    EditText etCaption;
    TextView tvLog;
    MediaController media_Controller;
    File file;

    private String mParam1;
    private String mParam2;


    public SendVideo() {
        // Required empty public constructor
    }


    public static SendVideo newInstance(String param1, String param2) {
        SendVideo fragment = new SendVideo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_send_video, container, false);
        media_Controller = new MediaController(getActivity());
        ivPostVideo = (VideoView) view.findViewById(R.id.vvPostImage);
        btAttachVideo = (Button) view.findViewById(R.id.btGetVideo);
        btPostFeed = (Button) view.findViewById(R.id.btPostFeed);
        etCaption = (EditText) view.findViewById(R.id.etVideoCaption);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);

        btAttachVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        btPostFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Privacy privacy = new Privacy.Builder().setPrivacySettings(Privacy.PrivacySettings.SELF).build();

                // create Photo instance and add some properties
                Video video = new Video.Builder()
                        .setVideo(file)
                        .setName(etCaption.getText().toString())
                        .setPrivacy(privacy)
                        .build();

                SimpleFacebook.getInstance().publish(video, new OnPublishListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        hideDialog();
                        tvLog.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        hideDialog();
                        tvLog.setText(reason);
                    }

                    @Override
                    public void onThinking() {
                        showDialog("Please Wait","Video Uploading...");
                        tvLog.setText("Uploading...");
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        tvLog.setText("Video Posted Successfully");
                    }
                });
            }
        });
        return view;
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            try
            {
                Uri selectedImageUri = data.getData();
                String[] projection = { MediaStore.MediaColumns.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,  projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                ivPostVideo.setVisibility(View.VISIBLE);
                btPostFeed.setVisibility(View.VISIBLE);
                ivPostVideo.setVideoPath(path);
                ivPostVideo.setMediaController(media_Controller);
                ivPostVideo.requestFocus();
                ivPostVideo.start();
                this.file = new File(path);
                Log.i("fb","I am here");

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
