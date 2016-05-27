package tag.tagqrcode;

import java.util.Date;

/**
 * Created by Rupak Adhikari on 1/16/2015.
 * class for assigning setter and getter for fileds of QrCode table in sqlite
 */
public class QrCode {

    private int id;
    String qr_Code;
     String date;


    public QrCode(int id, String qr_Code, String date) {
        this.id = id;
        this.qr_Code = qr_Code;
        this.date = date;
    }

    public int getId(){
        return id;
    }

    public String getQr_Code(){
        return qr_Code;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setQr_Code(String qr_Code) {
        this.qr_Code = qr_Code;
    }

    public String getDate(){

        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}