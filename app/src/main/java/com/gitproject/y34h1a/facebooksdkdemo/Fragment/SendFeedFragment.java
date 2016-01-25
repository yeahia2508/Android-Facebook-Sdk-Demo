package com.gitproject.y34h1a.facebooksdkdemo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Privacy;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class SendFeedFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView tvLog;
    EditText etMessage,etLinkName,etLinkCaption,etLinkDisription,etLinkUrl;



    public SendFeedFragment() {
        // Required empty public constructor
    }


    public static SendFeedFragment newInstance(String param1, String param2) {
        SendFeedFragment fragment = new SendFeedFragment();
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

        View view  = inflater.inflate(R.layout.fragment_send_feed, container, false);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);
        Button btSendFeed = (Button) view.findViewById(R.id.btSendFeed);

        etMessage = (EditText) view.findViewById(R.id.etMessage);
        etLinkName = (EditText) view.findViewById(R.id.etLinkName);
        etLinkCaption = (EditText) view.findViewById(R.id.etLinkCaption);
        etLinkDisription = (EditText) view.findViewById(R.id.etLinkDiscription);
        etLinkUrl = (EditText) view.findViewById(R.id.etLinkUrl);


        btSendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set privacy
                Privacy privacy = new Privacy.Builder()
                        .setPrivacySettings(Privacy.PrivacySettings.SELF)
                        .build();

                final Feed feed = new Feed.Builder()
                        .setMessage(etMessage.getText().toString())
                        .setName(etLinkName.getText().toString())
                        .setCaption(etLinkCaption.getText().toString())
                        .setDescription(etLinkDisription.getText().toString())
                        .setPicture("http://www.w3.org/html/logo/downloads/HTML5_Badge_256.png")
                        .setLink(etLinkUrl.getText().toString())
                        .setPrivacy(privacy)
                        .build();

                SimpleFacebook.getInstance().publish(feed, new OnPublishListener() {

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
                        showDialog("Please Wait","Feed Uploading...");
                        tvLog.setText("Posting...");
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        tvLog.setText("Posted Successfully");
                    }
                });
            }
        });

        return view;
    }

}
