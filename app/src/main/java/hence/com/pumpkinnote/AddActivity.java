package hence.com.pumpkinnote;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hence.com.pumpkinnote.Adapter.ImagenoteAdapter;
import hence.com.pumpkinnote.Fragment.SublimePickerFragment;
import hence.com.pumpkinnote.database.imageDatabase;
import hence.com.pumpkinnote.database.note;
import hence.com.pumpkinnote.database.noteDatabase;
import hence.com.pumpkinnote.database.voiceDatabase;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Hence on 2016/8/8.
 */

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    Toolbar toolbar;
    private BottomBar bottomBar;
    private ImageView type;
    private EditText et1;
    private note n1;
    private int noteid;
    private TextView notedtial;
    private String function;
    private String notedetail;
    private String notedate;
    private String notetype;
    public static final String LOG_TAG = "Log";
    private noteDatabase notedb;

    private Context context;
    private LinearLayout mainview;

    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;

    // date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private FancyButton mbutton;
    private FancyButton voiceplay;
    private FancyButton voicepause;
    private FancyButton voicedelete;




    private static int REQUEST_IMAGE_CAPTURE = 1;
    private static int REQUEST_GET_IMAGE = 2;
    //recorder
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private static String mFilePath = null;
    private static String mFileName = null;
    private static String FILE_FOLDER = "PumpkinNote";
    private static String mImageFile = null;
    private static String IMG_FOLDER = "IMAGE";
    private static String VIC_FOLDER = "VOICE";
    private LinearLayout mVoiceLayout;
    private LinearLayout mTypeselectLayout;
    private voiceDatabase vdb;
    private imageDatabase idb;
    private ArrayList<String> imagefilePathlist;

    private static String VOICE_PLAY = "PLAY";
    private static String VOICE_STOP = "STOP";
    private static String VOICE_DELE = "DELE";

    private FancyButton fbutton1;
    private FancyButton fbutton2;
    private FancyButton fbutton3;
    private FancyButton fbutton4;
    private FancyButton fbutton5;


    private RecyclerView imageRecyclerview;
    private ImagenoteAdapter imagenoteAdapter;
    private ArrayList<String> imgfilepath;

    private String imgFlag;
    private String vicFlag;

    private LinearLayout alarmpick;


    private TextView timepicker;
    private TextView datepicker;
    private TextView mondaypicker;
    private TextView tuesdaypicker;
    private TextView wendsdaypicker;
    private TextView tursdaypicker;
    private TextView fridaypicker;
    private TextView saturdaypicker;
    private TextView sundaypicker;
    private SelectedDate mSelectedDate;
    private Switch pickswitch;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilePath = Environment.getExternalStorageDirectory().getPath();
        setContentView(R.layout.add_layout);
        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        mVoiceLayout = (LinearLayout) findViewById(R.id.voice_layout);
        mTypeselectLayout = (LinearLayout) findViewById(R.id.typeselect_layout);
        //   mainview = (LinearLayout) findViewById(R.id.addlayout);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        pickswitch = (Switch) findViewById(R.id.pickswitch);
        mbutton = (FancyButton) findViewById(R.id.button);
        voiceplay = (FancyButton) findViewById(R.id.voiceplay);
        voiceplay.setOnClickListener(this);
        voiceplay.setTag(VOICE_PLAY);
        voicepause = (FancyButton) findViewById(R.id.voicestop);
        voicepause.setOnClickListener(this);
        voicepause.setTag(VOICE_STOP);
        voicedelete = (FancyButton) findViewById(R.id.voicedelete);
        voicedelete.setOnClickListener(this);
        voicedelete.setTag(VOICE_DELE);
        imgfilepath = new ArrayList<String>();
        imageRecyclerview = (RecyclerView) findViewById(R.id.image_recycler);
        imageRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        alarmpick = (LinearLayout) findViewById(R.id.alarmpick);


        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator("BallClipRotateMultipleIndicator");
        avi.setIndicatorColor(R.color.colorAccent);


        timepicker = (TextView) findViewById(R.id.timepick);
        timepicker.setOnClickListener(this);
        timepicker.setTag("TIMEPICK");
        datepicker = (TextView) findViewById(R.id.datepick);
        datepicker.setOnClickListener(this);
        datepicker.setTag("DATEPICK");
        mondaypicker = (TextView) findViewById(R.id.mondaypick);
        mondaypicker.setOnClickListener(this);
        mondaypicker.setTag("MONPICK");
        tuesdaypicker = (TextView) findViewById(R.id.tuesdaypick);
        tuesdaypicker.setOnClickListener(this);
        tuesdaypicker.setTag("TUEPICK");
        wendsdaypicker = (TextView) findViewById(R.id.wednsdaypick);
        wendsdaypicker.setOnClickListener(this);
        wendsdaypicker.setTag("WEDPICK");
        tursdaypicker = (TextView) findViewById(R.id.thursdaydaypick);
        tursdaypicker.setOnClickListener(this);
        tursdaypicker.setTag("THUPICK");
        fridaypicker = (TextView) findViewById(R.id.fridaypick);
        fridaypicker.setOnClickListener(this);
        fridaypicker.setTag("FRIPICK");
        saturdaypicker = (TextView) findViewById(R.id.saturdaypick);
        saturdaypicker.setOnClickListener(this);
        saturdaypicker.setTag("SATEPICK");
        sundaypicker = (TextView) findViewById(R.id.sundaypick);
        sundaypicker.setOnClickListener(this);
        sundaypicker.setTag("SUNPICK");


        mbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(LOG_TAG, "action down");
                        avi.show();
                        avi.setVisibility(View.VISIBLE);
                        startRecording();

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(LOG_TAG, "action up");
                        avi.hide();
                        stopRecording();

                        break;
                }
                return false;
            }
        });


        context = this;


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setDefaultTabPosition(0);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                int currpos = bottomBar.getCurrentTabId();
                Log.d(LOG_TAG, "tab selected " + tabId + currpos);
                //       Snackbar.make(bottomBar.getRootView(), "点击"+tabId, Snackbar.LENGTH_SHORT).show();
                if (mVoiceLayout.getVisibility() == View.VISIBLE) {
                    mTypeselectLayout.setAlpha(1.0f);
                    mTypeselectLayout.animate().translationX(0)
                            .alpha(0.0f);
                    mVoiceLayout.setVisibility(View.GONE);
                }
                if (mTypeselectLayout.getVisibility() == View.VISIBLE) {
                    mTypeselectLayout.setAlpha(1.0f);
                    mTypeselectLayout.animate().translationY(0)
                            .alpha(0.0f);
                    mTypeselectLayout.setVisibility(View.GONE);
                }
                if (alarmpick.getVisibility() == View.VISIBLE) {
                    alarmpick.setVisibility(View.GONE);
                }
                if (tabId == R.id.btm1) {
                    Log.d(LOG_TAG, "btm item one ");


                    mTypeselectLayout.setVisibility(View.VISIBLE);
                    mTypeselectLayout.setAlpha(0.0f);
                    mTypeselectLayout.animate().translationY(0)
                            .alpha(1.0f);
                }
                if (tabId == R.id.btm2) {
                    Log.d(LOG_TAG, "btm item one ");


                    getPhoto();
                }
                if (tabId == R.id.btm3) {
                    Log.d(LOG_TAG, "btm item one ");

                    takePhoto();

                }
                if (tabId == R.id.btm4) {
                    Log.d(LOG_TAG, "btm item two ");
                    mVoiceLayout.setVisibility(View.VISIBLE);
                    mVoiceLayout.setAlpha(0.0f);
                    mVoiceLayout.animate().translationY(0)
                            .alpha(1.0f);
                }
                //         if (tabId == R.id.btm5) {
                //             Log.d(LOG_TAG, "btm item three ");
                //             updateInfoView();
                //             alarmpick.setVisibility(View.VISIBLE);

                //               Intent intent = new Intent(context, SetAlarmActivity.class);
                //               startActivity(intent);
                //                 showDialog(DATE_DIALOG_ID);

                //            }
            }


        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                Log.d(LOG_TAG, "tab reselected " + tabId);
                mVoiceLayout.setVisibility(View.GONE);
                mTypeselectLayout.setVisibility(View.GONE);
                if (tabId == R.id.btm1) {
                    Log.d(LOG_TAG, "btm item one ");


                    mTypeselectLayout.setVisibility(View.VISIBLE);
                }
                if (tabId == R.id.btm2) {
                    Log.d(LOG_TAG, "btm item one ");


                    getPhoto();
                }
                if (tabId == R.id.btm3) {
                    Log.d(LOG_TAG, "btm item one ");

                    takePhoto();

                }
                if (tabId == R.id.btm4) {
                    Log.d(LOG_TAG, "btm item two ");
                    mVoiceLayout.setVisibility(View.VISIBLE);
                }
                //        if (tabId == R.id.btm5) {
                //             Log.d(LOG_TAG, "btm item three ");

                //             Intent intent = new Intent(context, SetAlarmActivity.class);
                //             startActivity(intent);
                //                 showDialog(DATE_DIALOG_ID);

                //        }

            }
        });


        et1 = (EditText) findViewById(R.id.input);
        n1 = new note();

        function = intent.getStringExtra("NOTE_FUNC");
        Log.d(LOG_TAG, "function " + function);
        notedb = new noteDatabase(this);

        if (function.contentEquals("NEW")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            noteid = intent.getIntExtra("NOTE_SIZE", 0) + 1;
            n1.setNoteid(String.valueOf(noteid));
            n1.setNotedate(df.format(Calendar.getInstance().getTime()));
            n1.setNotetype("1");
            n1.setNoteimgflag("N");
            n1.setNotevicflag("N");
            notetype = "1";
            imgFlag = "N";
            vicFlag = "N";
            Log.d(LOG_TAG, "new detail " + noteid);

        } else if (function.equals("UPDATE")) {

            notedetail = intent.getStringExtra("NOTE_DTIL");
            notedate = intent.getStringExtra("NOTE_DATE");
            Log.d(LOG_TAG, "update detail " + notedetail);
            noteid = Integer.valueOf(intent.getStringExtra("NOTE_ID"));
            notetype = intent.getStringExtra("NOTE_TYPE");

            n1 = notedb.getNote(intent.getStringExtra("NOTE_ID"));
            et1.setText(notedetail);
            et1.setSelection(notedetail.length());

        }
        vdb = new voiceDatabase(this);

        idb = new imageDatabase(this);
        imgfilepath = idb.getAllImageData(String.valueOf(noteid));
        if (imgfilepath.size() == 0) {
            imageRecyclerview.setVisibility(View.GONE);
        } else {
            imageRecyclerview.setVisibility(View.VISIBLE);
        }
        imagenoteAdapter = new ImagenoteAdapter(this, imgfilepath, String.valueOf(noteid));
        imageRecyclerview.setAdapter(imagenoteAdapter);

        fbutton1 = (FancyButton) findViewById(R.id.fancybtn1);
        fbutton1.setOnClickListener(this);
        fbutton1.setTag("fbtn1");


        fbutton2 = (FancyButton) findViewById(R.id.fancybtn2);
        fbutton2.setOnClickListener(this);
        fbutton2.setTag("fbtn2");


        fbutton3 = (FancyButton) findViewById(R.id.fancybtn3);
        fbutton3.setOnClickListener(this);
        fbutton3.setTag("fbtn3");

        fbutton4 = (FancyButton) findViewById(R.id.fancybtn4);
        fbutton4.setOnClickListener(this);
        fbutton4.setTag("fbtn4");

        fbutton5 = (FancyButton) findViewById(R.id.fancybtn5);
        fbutton5.setOnClickListener(this);
        fbutton5.setTag("fbtn5");
        if (notetype == null) {
            notetype = "1";
        }
        switch (notetype) {
            case "2":
                fbutton2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case "3":
                fbutton3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case "4":
                fbutton4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case "5":
                fbutton5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            default:
                fbutton1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
        Log.d(LOG_TAG, "voice id" + noteid);
        mFileName = vdb.getData(String.valueOf(noteid));
        //       Log.d(LOG_TAG,"add activity noteid"+noteid);

    }

    private void startRecording() {
        String foldername = FILE_FOLDER + "/" + VIC_FOLDER;
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        File audioVoice = new File(mFilePath, foldername);

        if (!audioVoice.exists()) {
            Log.d(LOG_TAG, "file not exist" + audioVoice.getAbsolutePath());
            audioVoice.mkdirs();
        }
        mFileName = audioVoice.getAbsolutePath() + File.separator + noteid + notedate + ".mp4";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Log.d(LOG_TAG, "start to record" + mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.d(LOG_TAG, "prepare() failed" + e);
        }

        mRecorder.start();
        vicFlag = "Y";
        n1.setNotevicflag("Y");
    }

    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            vdb = new voiceDatabase(this);
            if (!vdb.insertData(String.valueOf(noteid), mFileName)) {
                vdb.updateData(String.valueOf(noteid), mFileName);
            }
            ;
        }
    }


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.confirm) {

            if (pickswitch.isChecked()) {
                Calendar sevendayalarm = Calendar.getInstance();
                sevendayalarm.add(Calendar.HOUR_OF_DAY, 11);
                Intent intent = new Intent(this, Receiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 001, intent, 0);

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, sevendayalarm.getTimeInMillis(), pendingIntent);
            }


            if (function.equals("NEW")) {
                Log.d(LOG_TAG, "note add" + noteid);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                n1.setNotedate(df.format(Calendar.getInstance().getTime()));
                n1.setNotedetail(et1.getText().toString());
                n1.setNoteid(String.valueOf(noteid));
                n1.addnote(this);
                notedb.updateType(String.valueOf(noteid), n1.getNotetype());

            } else if (function.equals("UPDATE")) {
                Log.d(LOG_TAG, "note update when add" + noteid);


                n1.setNoteid(String.valueOf(noteid));
                n1.setNotedetail(et1.getText().toString());
                n1.setNotetype(notetype);

                notedb.updateData(String.valueOf(noteid), et1.getText().toString());
                Log.d(LOG_TAG, "note update for type" + n1.getNotetype());
                notedb.updateType(String.valueOf(noteid), n1.getNotetype());

            }


            notedb.updateVicflag(String.valueOf(noteid), n1.getNotevicflag());

            onBackPressed();

            return true;

        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    @Override
    public void onClick(View view) {

        Log.d(LOG_TAG, "voice click" + view.getTag());
        Log.d(LOG_TAG, "voice path" + mFileName);
        SublimeOptions options = new SublimeOptions();
        if (view.getTag().equals(VOICE_PLAY)) {
            if (mFileName == null) {
                Snackbar.make(mVoiceLayout, "还没有创建语音记录@_@", Snackbar.LENGTH_SHORT).show();

            }
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
            } catch (Exception e) {
                Snackbar.make(mVoiceLayout, "还没有创建语音记录@_@", Snackbar.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "voice play failer" + e);
            }

        }
        if (view.getTag().equals(VOICE_STOP)) {
            if (mFileName == null) {
                Snackbar.make(mVoiceLayout, "还没有创建语音记录@_@", Snackbar.LENGTH_SHORT).show();

            }
            if (mPlayer == null) {
                Log.d(LOG_TAG, "voice play null");
                Snackbar.make(mVoiceLayout, "咦，没有在播放语音呀@_@", Snackbar.LENGTH_SHORT).show();

            } else {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }

        }
        if (view.getTag().equals(VOICE_DELE)) {


            if (vdb.deleteVoice(String.valueOf(noteid))) {
                Snackbar.make(mVoiceLayout, "成功帮你清理了语音记录>_<", Snackbar.LENGTH_SHORT).show();
                vicFlag = "N";
                n1.setNotevicflag("N");
            } else {
                Snackbar.make(mVoiceLayout, "好像没有语音记录咧@_@", Snackbar.LENGTH_SHORT).show();
            }


        }
        if (view.getTag().equals("fbtn1")) {
            setfancybtn(fbutton1);

            notetype = "1";

        }
        if (view.getTag().equals("fbtn2")) {
            setfancybtn(fbutton2);
            notetype = "2";
        }
        if (view.getTag().equals("fbtn3")) {
            setfancybtn(fbutton3);
            notetype = "3";
        }
        if (view.getTag().equals("fbtn4")) {
            setfancybtn(fbutton4);
            notetype = "4";
        }
        if (view.getTag().equals("fbtn5")) {
            setfancybtn(fbutton5);
            notetype = "5";
        }
        if (view.getTag().equals("TIMEPICK")) {
            Log.d(LOG_TAG, "TIMEPICK");
            SublimePickerFragment pickerFrag = new SublimePickerFragment();
            Bundle bundle = new Bundle();
            options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
            bundle.putParcelable("SUBLIME_OPTIONS", options);
            pickerFrag.setCallback(mFragmentCallback);
            pickerFrag.setArguments(bundle);
            pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
        }
        if (view.getTag().equals("DATEPICK")) {
            Log.d(LOG_TAG, "datep");
            SublimePickerFragment pickerFrag = new SublimePickerFragment();
            Bundle bundle = new Bundle();
            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
            bundle.putParcelable("SUBLIME_OPTIONS", options);
            pickerFrag.setCallback(mFragmentCallback);
            pickerFrag.setArguments(bundle);
            pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
        }
    }

    public void setfancybtn(FancyButton fb1) {
        fbutton1.setBackgroundColor(getResources().getColor(R.color.fancybtmdefault));
        fbutton2.setBackgroundColor(getResources().getColor(R.color.fancybtmdefault));
        fbutton3.setBackgroundColor(getResources().getColor(R.color.fancybtmdefault));
        fbutton4.setBackgroundColor(getResources().getColor(R.color.fancybtmdefault));
        fbutton5.setBackgroundColor(getResources().getColor(R.color.fancybtmdefault));
        fb1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void takePhoto() {

        String foldername = FILE_FOLDER + "/" + IMG_FOLDER;
        mImageFile = Environment.getExternalStorageDirectory().getPath();
        File imagefile = new File(mImageFile, foldername);

        if (!imagefile.exists()) {
            Log.d(LOG_TAG, "file not exist" + imagefile.getAbsolutePath());
            imagefile.mkdirs();
        }

        Log.d(LOG_TAG, "photo take 1" + mImageFile);
        mImageFile = imagefile.getAbsolutePath() + File.separator + noteid + notedate + ".jpg";
        Log.d(LOG_TAG, "photo take 1" + mImageFile);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImageFile)));
        takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }

    public void getPhoto() {

        String foldername = FILE_FOLDER + "/" + IMG_FOLDER;
        mImageFile = Environment.getExternalStorageDirectory().getPath();
        File imagefile = new File(mImageFile, foldername);


        if (!imagefile.exists()) {
            Log.d(LOG_TAG, "file not exist" + imagefile.getAbsolutePath());
            imagefile.mkdirs();
        }

        Log.d(LOG_TAG, "photo get 1" + mImageFile);
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(date.getTime());
        mImageFile = imagefile.getAbsolutePath() + File.separator + noteid + timeStamp + ".jpg";
        Log.d(LOG_TAG, "photo get 1" + mImageFile);

        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, REQUEST_GET_IMAGE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "photo get " + requestCode);
        if (data != null) {
            if (requestCode == REQUEST_GET_IMAGE) {
                Uri uri = data.getData();
                Log.d(LOG_TAG, "photo get image");

                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    saveImage(bitmap, mImageFile);
                } catch (Exception e) {

                    Log.d(LOG_TAG, "photo get exception" + e);
                    return;
                }


            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Uri uri = data.getData();
                if (uri == null) {
                    Log.d(LOG_TAG, "photo get camera" + mFileName);
                    //use bundle to get data
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Log.d(LOG_TAG, "photo get camera save");
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        //spath :生成图片取个名字和路径包含类型
                        saveImage(photo, mFileName);
                    } else {
                        Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    //to do find the path of pic by uri
                }
            }
            Log.d(LOG_TAG, "photo recycler refresh");
            imagenoteAdapter.addData(mImageFile);
            imageRecyclerview.setVisibility(View.VISIBLE);
            idb.insertData(String.valueOf(noteid), mImageFile);
            Log.d(LOG_TAG, "photo recycler refresh");
            notedb.updateImgflag(n1.getNoteid(), "Y");
        }
    }

    public void addImagedb(String imageId, String imagePath) {


        idb.insertData(imageId, imagePath);

    }

    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            Log.d(LOG_TAG, "save image failed" + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {


        @Override
        public void onCancelled() {

        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            mSelectedDate = selectedDate;
            mMonth = mSelectedDate.getFirstDate().get(Calendar.MONTH) + 1;
            mDay = mSelectedDate.getFirstDate().get(Calendar.DAY_OF_MONTH);
            mHour = hourOfDay;
            mMinute = minute;
            Log.d(LOG_TAG, "Call back date" + String.valueOf(mMonth) + String.valueOf(mDay));
            Log.d(LOG_TAG, "Call back time" + mHour + mMinute);
            updateInfoView();


        }
    };

    public void updateInfoView() {
        datepicker.setText(mMonth + "月" + mDay + "日");
        timepicker.setText(mHour + ":" + mMinute);
    }
}


