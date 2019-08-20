package com.mlz.shuiguang.fragment.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mlz.shuiguang.ActivitySwitch;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.fragment.BaseFragment;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.transmission.TransmissionUtil;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.dialog.AlertDialog;
import com.mlz.shuiguang.util.dialog.ButtonActionHandler;
import com.mlz.shuiguang.util.dialog.ChangePasswordDialog;
import com.mlz.shuiguang.util.dialog.DatePickerDialog;
import com.mlz.shuiguang.util.dialog.EmailDialog;
import com.mlz.shuiguang.util.dialog.GenderDialog;
import com.mlz.shuiguang.util.dialog.PhoneDialog;
import com.mlz.shuiguang.util.dialog.ChangeThemeDialog;
import com.mlz.shuiguang.util.dialog.SkinTypeDialog;
import com.mlz.shuiguang.util.widget.button.NJButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by dehualai on 12/24/16.
 */

public class UserFragment extends BaseFragment {

    String TAG = "UserFragment";

    View view;


    LinearLayout bgLayout;
    LinearLayout headerLayout;
    LinearLayout emailLayout;
    LinearLayout phoneLayout;
    LinearLayout passwordLayout;
    LinearLayout dobLayout;
    LinearLayout genderLayout;
    LinearLayout skinLayout;



    Button logoutButton;
    Button changeProfileImageButton;
    ImageView profileImage;
    TextView username;
    TextView emailText;
    Button emailButton;
    TextView phoneText;
    Button phoneButton;

    TextView birthDateText;
    Button birthDateButton;

    TextView genderText;
    Button genderButton;

    TextView skinTypeText;
    Button skinTypeButton;



    NJButton changeLanguageButton;


    private final int STATUS_EMPTY = 0 ;
    private final int STATUS_FILLED = 1 ;


    // Code for our image picker select action.
    private static final int IMAGE_PICKER_SELECT = 999;



    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);

    }


    @Override
    public void onResume(){
        super.onResume();
        // load profile image
        if (profileImage!=null) {
            File avatarImage = new File(Global.AVATAR_PATH);
            if (avatarImage.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(Global.AVATAR_PATH);
                if (bitmap!=null) profileImage.setImageBitmap(bitmap);
            }
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_user, container, false);
        Bundle args = getArguments();
        assignViews();
        addActionToViews();
        queryDatabase();
        return view;
    }




    private void assignViews(){

        bgLayout = (LinearLayout)view.findViewById(R.id.bg_layout);
        headerLayout = (LinearLayout)view.findViewById(R.id.header_layout);

        emailLayout = (LinearLayout)view.findViewById(R.id.email_layout);
        phoneLayout = (LinearLayout)view.findViewById(R.id.phone_layout);
        passwordLayout = (LinearLayout)view.findViewById(R.id.password_layout);
        dobLayout = (LinearLayout)view.findViewById(R.id.dob_layout);
        genderLayout = (LinearLayout)view.findViewById(R.id.gender_layout);
        skinLayout = (LinearLayout)view.findViewById(R.id.skin_type_layout);

        logoutButton = (Button)view.findViewById(R.id.logout_button);
        profileImage = (ImageView)view.findViewById(R.id.profile_image);
        changeProfileImageButton = (Button)view.findViewById(R.id.change_profile_image_button);
        username = (TextView)view.findViewById(R.id.username);

        emailText = (TextView)view.findViewById(R.id.emailText);
        emailButton = (Button)view.findViewById(R.id.emailButton);

        phoneText = (TextView)view.findViewById(R.id.phoneText);
        phoneButton = (Button)view.findViewById(R.id.editPhoneButton);

        birthDateText = (TextView)view.findViewById(R.id.birthdayText);
        birthDateButton = (Button)view.findViewById(R.id.editDateOfBirthButton);

        genderText = (TextView)view.findViewById(R.id.genderText);
        genderButton = (Button)view.findViewById(R.id.editGenderButton);

        skinTypeText = (TextView)view.findViewById(R.id.skinTypeText);
        skinTypeButton = (Button)view.findViewById(R.id.editSkinButton);


        changeLanguageButton = (NJButton)view.findViewById(R.id.changeLanguageButton);

    }

    private void addActionToViews(){
        // on logout button pressed
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog(mContext);
                alertDialog.setTitleView(null);
                alertDialog.setMessageView(mContext.getResources().getString(R.string.are_you_sure_logout));
                alertDialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok), new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                new TBUser().clean(DBAdapter.getDatabase(mContext));
                                new TBDetection().clean(DBAdapter.getDatabase(mContext));
                                Intent targetIntent = new Intent(mContext, ActivitySwitch.class);
                                if(targetIntent!=null){
                                    targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mContext.startActivity(targetIntent);
                                }
                            }
                        }
                );
                alertDialog.showDialog();
            }
        });

        // on change profile image
        changeProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);
            }
        });

        // on change theme color
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeThemeDialog dialog = new ChangeThemeDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.pick_theme));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                    @Override
                    public void button1Pressed() {

                    }

                    @Override
                    public void button2Pressed() {

                    }
                });
                dialog.showDialog();
            }
        });


        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailDialog dialog = new EmailDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.please_enter_your_email_address));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                    @Override
                    public void button1Pressed() {

                    }

                    @Override
                    public void button2Pressed() {
                        // re query information from database
                        queryDatabase();
                    }
                });
                dialog.showDialog();

            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneDialog dialog = new PhoneDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.please_enter_your_phone_number));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                // re query information from database
                                queryDatabase();
                            }
                        });
                dialog.showDialog();
            }
        });

       

        birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.select_you_date_of_birth));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                // re query information from database
                                queryDatabase();
                            }
                        });
                dialog.showDialog();
            }
        });


        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderDialog genderDialog = new GenderDialog(mContext);
                genderDialog.setTitleView(null);
                genderDialog.setMessageView(mContext.getResources().getString(R.string.select_your_gender));
                genderDialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                // re query information from database
                                queryDatabase();
                            }
                        });
                genderDialog.showDialog();
            }
        });


        skinTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkinTypeDialog dialog = new SkinTypeDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.select_your_skin_type));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                // re query information from database
                                queryDatabase();
                            }
                        });
                dialog.showDialog();
            }
        });

    }





    private UserData queryUserDataFromDB(){
        TBUser tbUser = new TBUser();
        ArrayList<UserData> userList = tbUser.getUserList(DBAdapter.getDatabase(mContext));
        if (userList.size() > 0 ){
            UserData userData = userList.get(0);
            return userData;
        }
        return null;
    }

    private void queryDatabase(){

        UserData userData = queryUserDataFromDB();
        if (userData!=null){
            if (userData.getUsername() != null) {username.setText(userData.getUsername());}
            if (userData.getEmail() != null && !userData.getEmail().equals("")){
                emailText.setText(userData.getEmail());
                emailButton.setText(mContext.getResources().getString(R.string.edit));
            } else {
                emailButton.setText(mContext.getResources().getString(R.string.add));
            }

            if (userData.getPhone() != null && !userData.getPhone().equals("")){
                phoneText.setText(userData.getPhone());
                phoneButton.setText(mContext.getResources().getString(R.string.edit));
            } else {
                phoneButton.setText(mContext.getResources().getString(R.string.add));
            }

            if (userData.getDate_of_birth() != null && !userData.getDate_of_birth().equals("")){
                birthDateText.setText(userData.getDate_of_birth());
                birthDateButton.setVisibility(View.GONE);
            } else {
                birthDateButton.setVisibility(View.VISIBLE);
            }
            if (userData.getGender() != null && !userData.getGender().equals("")){
                genderText.setText(userData.getGender());
                genderButton.setVisibility(View.GONE);
            } else {
                genderButton.setVisibility(View.VISIBLE);
            }

            if (userData.getSkin_type() !=null && !userData.getSkin_type().equals("")){
                skinTypeText.setText(userData.getSkin_type());
                skinTypeButton.setVisibility(View.GONE);
            }else{
                skinTypeButton.setVisibility(View.VISIBLE);
            }

        }else {

        }


        TransmissionUtil.syncUserInfo(mContext);

    }




    private void setEmailStatus(int status){

    }

    private void setPhoneStatus(int status){

    }

    private void setBirthDateStatus(int status){

    }

    private void setGenderStatus(int status){

    }

    private void setSkinTypeStatus(int status){

    }





    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {
            // step 1: copy image to app folder
            String imageFile = getImagePathFromGallary(data, mContext);
            boolean success = copyFile(imageFile, Global.AVATAR_PATH);
            if (!success){
                AlertDialog alertDialog = new AlertDialog(mContext);
                alertDialog.setTitleView(mContext.getResources().getString(R.string.permission_denied));
                alertDialog.setMessageView(mContext.getResources().getString(R.string.no_permission_for_storage));
                alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                alertDialog.showDialog();
            }

            // step 2: update database
            UserData userData = queryUserDataFromDB();
            userData.setAvatar(Global.AVATAR_PATH);
            TBUser tbUser = new TBUser();
            tbUser.smartInsert(DBAdapter.getDatabase(mContext), userData);
        }
    }

    private String getImagePathFromGallary(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }






    /**
     * save avatar image to app folder
     * */


    private boolean copyFile(String sourceFile, String destFile)  {
        File file = new File(destFile);
        if (file.exists())
            file.delete();
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(sourceFile);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            Log.e("show", "Exception: " + e.getMessage());
            return false;
        }
    }





}
