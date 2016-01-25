package com.gitproject.y34h1a.facebooksdkdemo.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.BaseFragment;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Photo;
import com.sromku.simple.fb.entities.Privacy;
import com.sromku.simple.fb.listeners.OnPublishListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SendPhoto extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CAMERA = 1000;
    private static final int SELECT_FILE = 3000;


    private String mParam1;
    private String mParam2;

    ImageView ivPostImage;
    Button btAttachImage,btPostFeed;
    EditText etCaption;
    TextView tvLog;
    Bitmap bitmap;

    public SendPhoto() {
        // Required empty public constructor
    }

    public static SendPhoto newInstance(String param1, String param2) {
        SendPhoto fragment = new SendPhoto();
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
        View view =  inflater.inflate(R.layout.fragment_send_photo, container, false);
        ivPostImage = (ImageView) view.findViewById(R.id.ivPostImage);
        btAttachImage = (Button) view.findViewById(R.id.btGetPhoto);
        btPostFeed = (Button) view.findViewById(R.id.btPostFeed);
        etCaption = (EditText) view.findViewById(R.id.etPhotoCaption);
        tvLog = (TextView) view.findViewById(R.id.tvPostLog);

        btAttachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btPostFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set privacy
                Privacy privacy = new Privacy.Builder()
                        .setPrivacySettings(Privacy.PrivacySettings.SELF)
                        .build();

                // create Photo instance and add some properties
                Photo photo = new Photo.Builder()
                        .setImage(bitmap)
                        .setName(etCaption.getText().toString())
                        .setPlace("101889586519301")
                        .setPrivacy(privacy)
                        .build();

                SimpleFacebook.getInstance().publish(photo, false, new OnPublishListener() {

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
                        showDialog("Please Wait","Image Uploading...");
                        tvLog.setText("Posting...");
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        tvLog.setText("Posted Succesfully");
                    }
                });
            }
        });
        return view;
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                bitmap = thumbnail;
                ivPostImage.setVisibility(View.VISIBLE);
                ivPostImage.setImageBitmap(thumbnail);
                btPostFeed.setVisibility(View.VISIBLE);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = { MediaStore.MediaColumns.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,  projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 150;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                bitmap = bm;
                ivPostImage.setVisibility(View.VISIBLE);
                ivPostImage.setImageBitmap(bm);
                btPostFeed.setVisibility(View.VISIBLE);


            }
        }
    }


}
