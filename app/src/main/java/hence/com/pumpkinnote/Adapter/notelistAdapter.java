package hence.com.pumpkinnote.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import hence.com.pumpkinnote.AddActivity;
import hence.com.pumpkinnote.R;
import hence.com.pumpkinnote.database.imageDatabase;
import hence.com.pumpkinnote.database.note;
import hence.com.pumpkinnote.database.noteDatabase;


/**
 * Created by Hence on 2016/8/10.
 */

public class notelistAdapter extends RecyclerSwipeAdapter<notelistAdapter.noteViewholder> {

    private LayoutInflater layoutInflater;
    private ArrayList<note> noteList = new ArrayList<>();
    private Context context;
    private final Activity activity;


    private noteDatabase noteDb;
    private imageDatabase imageDb;
    private String cityname;
    private noteViewholder holder;

    public static final String LOG_TAG = "Log";


    @Override
    public noteViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.notecard, parent, false);
        noteViewholder viewholder = new noteViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final noteViewholder holder, final int position) {

        //   Log.d(LOG_TAG,"rebuild Recycler View n"+ position);
        note currNote = noteList.get(position);
        String notesumm = currNote.getNotedetail();
        Log.d(LOG_TAG, "curr note type" + currNote.getNotetype());
        //     Log.d(LOG_TAG,"break postion"+ notesumm.indexOf("\n"));
        int posofbreak = notesumm.indexOf("\n");
        if (notesumm.length() < 20 && posofbreak < 0) {
            holder.notedtial.setText(notesumm);
        } else if (posofbreak > 0) {
            Log.d(LOG_TAG, "sub string" + notesumm.substring(0, posofbreak) + "...");
            holder.notedtial.setText(notesumm.substring(0, posofbreak) + "...");
        } else {
            holder.notedtial.setText(notesumm.substring(0, 20) + "...");
        }
        holder.nodatedate.setText(foramtDate(currNote.getNotedate()));
        if (currNote.getNotetype() == null) {
            holder.notetype.setImageResource(R.drawable.home);
        } else {
            switch (currNote.getNotetype()) {
                case "1":
                    holder.notetype.setImageResource(R.drawable.home);
                    break;
                case "2":
                    holder.notetype.setImageResource(R.drawable.work);
                    break;
                case "3":
                    holder.notetype.setImageResource(R.drawable.cat);
                    break;
                case "4":
                    holder.notetype.setImageResource(R.drawable.question);
                    break;
                case "5":
                    holder.notetype.setImageResource(R.drawable.important);
                    break;

            }
        }
        Log.d(LOG_TAG, "curr note flag" + currNote.getNoteimgflag() + currNote.getNotevicflag());
        if (currNote.getNoteimgflag().equals("Y")) {
            Log.d(LOG_TAG, "curr note flag visibal img");
                    holder.noteimgFlag.setVisibility(View.VISIBLE);
        }
        if (currNote.getNotevicflag().equals("Y")) {
            Log.d(LOG_TAG, "curr note flag visible voice");
            holder.notevicFlag.setVisibility(View.VISIBLE);
        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });


        holder.notedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(LOG_TAG, "On Click delete" + position);
                noteDb = new noteDatabase(context);
                imageDb = new imageDatabase(context);
                noteDb.deleteData(noteList.get(position).getNoteid());
                imageDb.deleteImagebyId(noteList.get(position).getNoteid());
                noteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, noteList.size());
                Snackbar.make(view.getRootView(),"删除成功", Snackbar.LENGTH_SHORT).show();

            }
        });

        holder.notedtial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "On Click detail" + position);
                note currNote = noteList.get(position);
                Intent intent = new Intent(view.getContext(), AddActivity.class);
                intent.putExtra("NOTE_DTIL", currNote.getNotedetail());
                intent.putExtra("NOTE_DATE", currNote.getNotedate());
                intent.putExtra("NOTE_ID", currNote.getNoteid());
                intent.putExtra("NOTE_TYPE", currNote.getNotetype());
                intent.putExtra("NOTE_FUNC", "UPDATE");
                context.startActivity(intent);

            }
        });


    }

    public String foramtDate(String date) {
        String datefm;
        datefm = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8) + " " + date.substring(8, 10) + ":" + date.substring(10, 12);
        return datefm;

    }

    public void onUpdate() {

        notifyDataSetChanged();
    }


    public void addData(note noteadd) {

        note nt1 = noteadd;
        noteList.add(nt1);

        notifyItemInserted(noteList.size());

    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "size of note" + noteList.size());

        return noteList.size();

    }


    public notelistAdapter(Context context, Activity activity, ArrayList<note> data) {
        //     Log.d(LOG_TAG,"Color adapter first"+data.size());
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
        this.noteList = data;


        //      colorlist=new int[weatherArrayList.size()];
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swap_item;
    }


    public class noteViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView notedtial;
        private TextView nodatedate;
        private ImageView notetype;
        private SwipeLayout swipeLayout;
        private ImageView notedelete;
        private ImageView noteimgFlag;
        private ImageView notevicFlag;
        private CardView cardView;


        public noteViewholder(View itemView) {
            super(itemView);
            notedtial = (TextView) itemView.findViewById(R.id.notelist_summ);
            nodatedate = (TextView) itemView.findViewById(R.id.notelist_date);

            notetype = (ImageView) itemView.findViewById(R.id.notelist_type);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swap_item);
            notedelete = (ImageView) itemView.findViewById(R.id.notelist_delete);
            noteimgFlag = (ImageView) itemView.findViewById(R.id.img_flag);
            notevicFlag = (ImageView) itemView.findViewById(R.id.voice_flag);
            cardView= (CardView) itemView.findViewById(R.id.card_view);


        }

        @Override
        public void onClick(View v) {


            Intent intent = new Intent(v.getContext(), AddActivity.class);
            Log.d(LOG_TAG, "On Click" + getAdapterPosition());


            //      ActivityOptionsCompat compat= ActivityOptionsCompat.makeSceneTransitionAnimation(activity,null);

            //      context.startActivity(intent, compat.toBundle());

            context.startActivity(intent);


        }
    }

}









