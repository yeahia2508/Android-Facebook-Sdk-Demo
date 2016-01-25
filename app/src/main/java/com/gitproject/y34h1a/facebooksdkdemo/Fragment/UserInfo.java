package com.gitproject.y34h1a.facebooksdkdemo.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.login.widget.ProfilePictureView;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

public class UserInfo extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProfilePictureView profilePicture;
    TextView profileName, userInfo, tvLog;

    private String mParam1;
    private String mParam2;

    public UserInfo() {

    }


    public static UserInfo newInstance(String param1, String param2) {
        UserInfo fragment = new UserInfo();
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
        View view =  inflater.inflate(R.layout.fragment_user_info, container, false);
        profilePicture = (ProfilePictureView) view.findViewById(R.id.profilePic);
        profileName = (TextView) view.findViewById(R.id.tvProfileName);
        userInfo = (TextView) view.findViewById(R.id.tvUserInfo);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);

        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(500);
        pictureAttributes.setWidth(500);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.ID)
                .add(Profile.Properties.PICTURE, pictureAttributes)
                .add(Profile.Properties.FIRST_NAME)
                .add(Profile.Properties.LAST_NAME)
                .add(Profile.Properties.NAME)
                .add(Profile.Properties.LOCATION)
                .add(Profile.Properties.EDUCATION)
                .add(Profile.Properties.EMAIL)
                .add(Profile.Properties.RELIGION)
                .add(Profile.Properties.RELATIONSHIP_STATUS)
                .build();

        // SimpleFacebook.getInstance().getProfile(new OnProfileListener() {
        SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

            @Override
            public void onThinking() {
                showDialog("Please Wait","Facthing Data...");
            }

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
            public void onComplete(Profile response) {
                hideDialog();
                tvLog.setText("Successfully Data Fatched...");
                profileName.setText(response.getName());
                String userResult = "";
                userResult =    "Email: " + response.getEmail() + "<br><br>" +
                        "University: " + response.getEducation().get(3).getSchool()+"<br>"
                ;
                userInfo.setText(Html.fromHtml(userResult));
               profilePicture.setProfileId(response.getId());
            }
        });

        return view;
    }
}
