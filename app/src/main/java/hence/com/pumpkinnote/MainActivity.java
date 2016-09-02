package hence.com.pumpkinnote;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.daimajia.swipe.SwipeLayout;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import hence.com.pumpkinnote.Adapter.notelistAdapter;
import hence.com.pumpkinnote.Fragment.MyMenuFragment;
import hence.com.pumpkinnote.Network.VolleySingleton;
import hence.com.pumpkinnote.database.note;
import hence.com.pumpkinnote.database.noteDatabase;

import static android.R.attr.version;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyMenuFragment.OnNavigationItemSelectedListener {

    private static final String TAG_ADD_NOTE = "ADD_NOTE";
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<note> notelist = new ArrayList<>();
    private noteDatabase noteDb;
    public static final String LOG_TAG = "Log";
    private LeftDrawerLayout mLeftDrawerLayout;
    private MyMenuFragment mMenuFragment;
    private int[] cnt = new int[6];

    private String newUrl;
    private TextView totalcnt;
    private TextView homecnt;
    private TextView workcnt;
    private TextView catcnt;
    private TextView qustcnt;
    private TextView imptcnt;

    private Toolbar mtoobar;

    private String version;
    private String newVersion;

    private VolleySingleton volleySignleton;
    private RequestQueue requestQueue;
    private NavigationView mNav;

    private Callbacks callbacks;

//    private notelistAdapter notelistadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Network
        volleySignleton = VolleySingleton.getInstance();
        requestQueue = volleySignleton.getmRequestQueue();

        noteDb = new noteDatabase(this);
        ImageView fabicon = new ImageView(this); // Create an icon
        fabicon.setImageResource(R.drawable.plus);
        Log.d(LOG_TAG, "load note");
        loadnotelist(notelist);
        context = this;


        if (checkNetwork()) {
            checkLatestUpdate();
        }
        mNav = (NavigationView) findViewById(R.id.vNavigation);
        recyclerView = (RecyclerView) findViewById(R.id.notelistrecyclerview);
        notelistAdapter notelistadapter = new notelistAdapter(this, this, notelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notelistadapter);


        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (MyMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        FlowingView mFlowingView = (FlowingView) findViewById(R.id.sv);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new MyMenuFragment()).commit();

        }
        mLeftDrawerLayout.setFluidView(mFlowingView);
        mLeftDrawerLayout.setMenuFragment(mMenuFragment);
        cnt = noteDb.getCnt();
        mMenuFragment.setCnt(cnt[0], cnt[1], cnt[2], cnt[3], cnt[4], cnt[5]);


        //    setCnt(cnt[0],cnt[1],cnt[2],cnt[3],cnt[4],cnt[5]);

        fabicon.setTag(TAG_ADD_NOTE);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(fabicon)
                .build();


        fabicon.setOnClickListener(this);


    }

    public boolean checkNetwork() {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networks = cm.getActiveNetworkInfo();

        if (networks==null){
            return false;
        }

        if (networks.getTypeName().equalsIgnoreCase("WIFI")) {
            if (networks.isConnected()) {
                haveConnectedWifi = true;
                Log.d(LOG_TAG, "WIFI AVAILABLE");
            } else {
                Log.d(LOG_TAG, "WIFI NOT AVAILABLE");
            }
        }


        return haveConnectedWifi;

    }

    @Override
    public void onMenuselected(int postion) {
        Log.d(LOG_TAG, "nav clicked back main" + postion);
        if (postion != 0) {
            notelist = noteDb.getAlldataByType(String.valueOf(postion));
            notelistAdapter notelistadapter = new notelistAdapter(this, this, notelist);
            notelistadapter.notifyDataSetChanged();
            recyclerView.setAdapter(notelistadapter);
        } else {
            notelist = noteDb.getAlldata();
            notelistAdapter notelistadapter = new notelistAdapter(this, this, notelist);
            notelistadapter.notifyDataSetChanged();
            recyclerView.setAdapter(notelistadapter);
        }
        mLeftDrawerLayout.closeDrawer();
    }

    public interface Callbacks {
        public int onClosedCallBack();
    }

    private void loadnotelist(ArrayList<note> notelist) {
        Log.d(LOG_TAG, "load note1");
        this.notelist = noteDb.getAlldata();


    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        loadnotelist(notelist);
        notelistAdapter notelistadapter = new notelistAdapter(this, this, notelist);
        notelistadapter.notifyItemInserted(notelist.size() + 1);
        recyclerView.setAdapter(notelistadapter);
        cnt = noteDb.getCnt();
        mMenuFragment.setCnt(cnt[0], cnt[1], cnt[2], cnt[3], cnt[4], cnt[5]);
        if (notelist.size() != noteDb.getTottalcnt()) {
            Snackbar.make(recyclerView.getRootView(), "添加成功", Snackbar.LENGTH_SHORT).show();


        }

        //Refresh your stuff here
    }

    @Override
    public void onClick(View view) {
        final View v = view;
        Intent intent = new Intent(v.getContext(), AddActivity.class);
        Log.d(LOG_TAG, "On click" + v.getTag());
        if (v.getTag().equals(TAG_ADD_NOTE)) {

            intent.putExtra("NOTE_FUNC", "NEW");
            int lastid = 0;
            if (notelist.size() != 0) {
                lastid = Integer.valueOf(notelist.get(0).getNoteid());
            }
            intent.putExtra("NOTE_SIZE", lastid);
            Log.d(LOG_TAG, "note size" + notelist.size());


        }
    }

    public void checkLatestUpdate() {
        String url = "http://api.fir.im/apps/latest/57bc6764ca87a81c6900166d?api_token=91fc537cc09d094d178dabd3311d81e1";

        try {
            PackageInfo pkInfor = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pkInfor.versionName;
            Log.d(LOG_TAG, "version id " + version);
        } catch (Exception e) {

        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject versionShort;
                JSONObject update_url;
                JSONObject direct_install_url;

                try {
                    newUrl = response.getString("update_url");
                    newVersion = response.getString("versionShort");
                    Log.d(LOG_TAG, "update url " + newUrl);
                    Log.d(LOG_TAG, "version vs " + newVersion + version);
                    if (!version.equals(newVersion)) {
                        Log.d(LOG_TAG, "start mew dialog ");
                        new MaterialDialog.Builder(context).title("发现新的更新")
                                .positiveText("前去更新")
                                .negativeText("没有流量了")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        // TODO

                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(newUrl));
                                        startActivity(i);

                                    }
                                }).show();
                    }
                } catch (Exception e) {

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(LOG_TAG, "error resp");
                Snackbar.make(recyclerView.getRootView(), "好像网络有问题>_<", Snackbar.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(request);
    }


    NavigationView.OnNavigationItemSelectedListener navlistner = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            int id = menuItem.getItemId();
            Log.d(LOG_TAG, "nav clicked back main");
            //noinspection SimplifiableIfStatement
            mLeftDrawerLayout.closeDrawer();
            switch (id) {
                case R.id.updateimg:
                    checkLatestUpdate();
                    break;

            }

            return false;
        }
    };

}
