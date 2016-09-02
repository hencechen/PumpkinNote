package hence.com.pumpkinnote.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;

import hence.com.pumpkinnote.AddActivity;
import hence.com.pumpkinnote.FullimageActivity;
import hence.com.pumpkinnote.R;
import hence.com.pumpkinnote.database.imageDatabase;
import hence.com.pumpkinnote.database.noteDatabase;

import static android.R.attr.bitmap;


/**
 * Created by Hence on 2016/8/21.
 */

public class ImagenoteAdapter extends RecyclerView.Adapter<ImagenoteAdapter.imageViewholder> {

    private ArrayList<String> imagefilepath;
    private Context context;
    private LayoutInflater layoutInflater;
    private imageViewholder holder;
    private imageDatabase idb;
    private String  noteid;
    private noteDatabase ndb;

    public static final String LOG_TAG = "Log";

    @Override
    public imageViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "image recycler view create");
        View v = layoutInflater.inflate(R.layout.image_layout, parent, false);
        imageViewholder viewholder = new imageViewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(imageViewholder holder, int position) {

        File imgFile = new File(imagefilepath.get(position));
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            holder.imageView.setImageBitmap(bitmap);
        }else {

        }
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "image recycler view size" + imagefilepath.size());
        if (imagefilepath.size()==0){
            Log.d(LOG_TAG, "curr note size 0" + noteid);
            ndb=new noteDatabase(context);
            ndb.updateImgflag(noteid,"N");
        }
        return imagefilepath.size();
    }

    public void addData(String filepath) {
        File imgFile = new File(filepath);
        if (imgFile.exists()) {
            imagefilepath.add(filepath);
        }
        notifyItemInserted(imagefilepath.size());
    }

    public ImagenoteAdapter(Context context, ArrayList<String> data, String noteid) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        imagefilepath = data;
        this.noteid=noteid;
    }

    public class imageViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        boolean isImageFitToScreen;

        public imageViewholder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_note);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), FullimageActivity.class);
                    intent.putExtra("IMG_URI",imagefilepath.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    idb=new imageDatabase(context);

                    new MaterialDialog.Builder(context).title("要删除这张照片吗？")
                            .positiveText("决定了")
                            .negativeText("再想想")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // TODO

                                    Log.d(LOG_TAG,"Long Click");
                                    Log.d(LOG_TAG,"Long Click delet "+imagefilepath.get(getAdapterPosition()));
                                    idb.deleteImagebyPath(imagefilepath.get(getAdapterPosition()));

                                    imagefilepath.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            }).show();
                    return false;
                }
            });

        }
    }
}
