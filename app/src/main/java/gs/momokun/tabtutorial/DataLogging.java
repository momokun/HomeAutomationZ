package gs.momokun.tabtutorial;

/**
 * Created by ElmoTan on 12/6/2016.
 */

public class DataLogging {

    int _id;
    String _date;
    String _temp;

    public DataLogging(){

    }

    public DataLogging(int id, String date, String temp){
        this._id = id;
        this._date = date;
        this._temp = temp;
    }

    public DataLogging(String date, String temp){
        this._date = date;
        this._temp = temp;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_temp() {
        return _temp;
    }

    public void set_temp(String _temp) {
        this._temp = _temp;
    }
}




