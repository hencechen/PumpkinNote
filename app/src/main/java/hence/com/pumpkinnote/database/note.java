package hence.com.pumpkinnote.database;

import android.content.Context;

/**
 * Created by Hence on 2016/8/8.
 */

public class note {
    private String noteid;
    private String notetype;
    private String notedate;
    private String notedetail;
    private String noteimgflag;
    private String notevicflag;
    private noteDatabase db;
    private Context context;

    public note (String noteid, String notetype, String notedate, String notedetail,String noteimgflag, String notevicflag){
        this.notedate=notedate;
        this.notedetail=notedetail;
        this.noteid=noteid;
        this.notetype=notetype;
        this.noteimgflag=noteimgflag;
        this.notevicflag=notevicflag;

    }

    public note(){

    }

    public void addnote(Context context){
        this.context=context;
        db=new noteDatabase(context);
        db.insertData(noteid,notetype,notedate,notedetail);
    }

    public void setNotetype(String notetype) {
        this.notetype = notetype;


    }



    public void setNotedate(String notedate) {
        this.notedate = notedate;
    }

    public void setNotedetail(String notedetail) {
        this.notedetail = notedetail;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }

    public void setNoteimgflag(String noteimgflag) {
        this.noteimgflag = noteimgflag;
    }

    public void setNotevicflag(String notevicflag) {
        this.notevicflag = notevicflag;
    }

    public String getNotedate() {
        return notedate;
    }

    public String getNoteid() {
        return noteid;
    }

    public String getNotedetail() {
        return notedetail;
    }

    public String getNotetype() {
        return notetype;
    }

    public String getNoteimgflag() {
        return noteimgflag;
    }

    public String getNotevicflag() {
        return notevicflag;
    }
}

