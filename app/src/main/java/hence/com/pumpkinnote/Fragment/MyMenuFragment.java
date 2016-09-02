package hence.com.pumpkinnote.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mxn.soul.flowingdrawer_core.MenuFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import hence.com.pumpkinnote.MainActivity;
import hence.com.pumpkinnote.Network.VolleySingleton;
import hence.com.pumpkinnote.R;

import static hence.com.pumpkinnote.R.id.menu_update;

/**
 * Created by Hence on 2016/8/18.
 */

public class MyMenuFragment extends MenuFragment implements View.OnClickListener{

    private ImageView ivMenuUserProfilePhoto;
    private TextView totalcnt;
    private TextView homecnt;
    private TextView workcnt;
    private TextView catcnt;
    private TextView qustcnt;
    private TextView imptcnt;
    public static final String LOG_TAG = "Log";
    private CardView updatecard;
    private int tnum;
    private int hnum;
    private int wnum;
    private int cnum;
    private int qnum;
    private int inum;
    private Menu menu;
    public NavigationView vNavigation;


    private String newUrl;
    private String version;
    private String newVersion;
    private Context context;

    private int menuitem;

    private VolleySingleton volleySignleton;
    private RequestQueue requestQueue;

    private OnNavigationItemSelectedListener mCallback;

    public interface OnNavigationItemSelectedListener{
        public void onMenuselected(int postion);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            mCallback = (OnNavigationItemSelectedListener) activity;
        }catch (Exception e){

        }
    }

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void setCnt(int tnum, int hnum, int wnum,int cnum,  int qnum, int inum) {
        Log.d(LOG_TAG,"setup for text ");
        this.tnum=tnum;
        this.hnum=hnum;
        this.wnum=wnum;
        this.cnum=cnum;
        this.qnum=qnum;
        this.inum=inum;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        Log.d(LOG_TAG,"menu update ");
        menu.getItem(R.id.menu_all).setTitle("全部   "+tnum);
        menu.getItem(R.id.menu_home).setTitle("生活   "+hnum);
        menu.getItem(R.id.menu_work).setTitle("工作   "+wnum);
        menu.getItem(R.id.menu_pet).setTitle("宠物   "+cnum);
        menu.getItem(R.id.menu_unknow).setTitle("未知   "+qnum);
        menu.getItem(R.id.menu_important).setTitle("重要   "+inum);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        Log.d(LOG_TAG,"menu fragment ");

        vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setItemIconTintList(null);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Log.d(LOG_TAG,"nav clicked");
                int id = item.getItemId();
                switch (id){
                    case menu_update:
                        checkLatestUpdate();
                        break;
                    case R.id.menu_home:
                        Log.d(LOG_TAG,"home clicked");
                        mCallback.onMenuselected(1);
                        break;
                    case R.id.menu_all:
                        Log.d(LOG_TAG,"all clicked");
                        mCallback.onMenuselected(0);
                        break;
                    case R.id.menu_work:
                        Log.d(LOG_TAG,"work clicked");
                        mCallback.onMenuselected(2);
                        break;
                    case R.id.menu_pet:
                        Log.d(LOG_TAG,"pet clicked");
                        mCallback.onMenuselected(3);
                        break;
                    case R.id.menu_unknow:
                        Log.d(LOG_TAG,"unknow clicked");
                        mCallback.onMenuselected(4);
                        break;
                    case R.id.menu_important:
                        Log.d(LOG_TAG,"imp clicked");
                        mCallback.onMenuselected(5);
                        break;


                }
                return false;
            }
        });

        menu= vNavigation.getMenu();

        updateMenutitle();





        totalcnt = (TextView) view.findViewById(R.id.totcnt);
        totalcnt.setTag("totalcnt");
        totalcnt.setOnClickListener(this);
        homecnt = (TextView) view.findViewById(R.id.homecnt);
        homecnt.setTag("homecnt");
        homecnt.setOnClickListener(this);
        workcnt = (TextView) view.findViewById(R.id.workcnt);
        workcnt.setTag("workcnt");
        workcnt.setOnClickListener(this);
        catcnt = (TextView) view.findViewById(R.id.petcnt);
        catcnt.setTag("catcnt");
        catcnt.setOnClickListener(this);
        qustcnt = (TextView) view.findViewById(R.id.unknowcnt);
        qustcnt.setTag("qustcnt");
        qustcnt.setOnClickListener(this);
        imptcnt = (TextView) view.findViewById(R.id.impcnt);
        imptcnt.setTag("imptcnt");
        imptcnt.setOnClickListener(this);
        updatecard= (CardView) view.findViewById(R.id.cardupdate);
        imptcnt.setTag("updatecard");
        updatecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG,"update card clickable");
                checkLatestUpdate();
            }
        });
        return setupReveal(view);
    }



    public void updateMenutitle(){
        Log.d(LOG_TAG,"menu update ");
        menu.getItem(0).setTitle("全部   "+tnum);
        menu.getItem(1).setTitle("生活   "+hnum);
        menu.getItem(2).setTitle("工作   "+wnum);
        menu.getItem(3).setTitle("宠物   "+cnum);
        menu.getItem(4).setTitle("未知   "+qnum);
        menu.getItem(5).setTitle("重要   "+inum);
    }


    public void onOpenMenu() {
        //    Toast.makeText(getActivity(),"onOpenMenu",Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"setup for text on open ");
//        totalcnt.setText(String.valueOf(tnum));

  //      homecnt.setText(String.valueOf(hnum));
     //   workcnt.setText(String.valueOf(wnum));
    //    catcnt.setText(String.valueOf(cnum));
    //    qustcnt.setText(String.valueOf(qnum));
    //    imptcnt.setText(String.valueOf(inum));
    }

    public void onCloseMenu() {
        //       Toast.makeText(getActivity(),"onCloseMenu",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG,"On click"+view.getTag());
        if (view.getTag().equals("totalcnt")){
            menuitem=0;
            onCloseMenu();
        }
        if (view.getTag().equals("updatecard")){

        }
    }

    public void checkLatestUpdate() {
        String url = "http://api.fir.im/apps/latest/57bc6764ca87a81c6900166d?api_token=91fc537cc09d094d178dabd3311d81e1";
//Network
        volleySignleton = VolleySingleton.getInstance();
        requestQueue = volleySignleton.getmRequestQueue();
        try {
            PackageInfo pkInfor = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pkInfor.versionName;
            Log.d(LOG_TAG, "version id " + version);
        } catch (Exception e) {

        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                context=getActivity();

                JSONObject versionShort;
                JSONObject update_url;
                JSONObject direct_install_url;

                try {
                    newUrl=response.getString("update_url");
                    newVersion=response.getString("versionShort");
                    Log.d(LOG_TAG, "update url " + newUrl);
                    Log.d(LOG_TAG, "version vs " + newVersion+ version);
                    if (!version.equals(newVersion)) {
                        Log.d(LOG_TAG, "start mew dialog " );
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
                    }else{
                        Snackbar.make(updatecard.getRootView(), "你已经是最新版本了", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(LOG_TAG, "error resp");
                Snackbar.make(updatecard.getRootView(), "好像网络有问题>_<", Snackbar.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(request);
    }

}