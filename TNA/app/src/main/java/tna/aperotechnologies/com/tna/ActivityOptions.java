package tna.aperotechnologies.com.tna;

/**
 * Created by mpatil on 13/08/15.
 */
public class ActivityOptions
{
    public String Desc = null;
    public boolean Checked = false;
    public String Id = null;

    public ActivityOptions(String Id, String Desc, boolean Checked) {
        this.Id = Id;
        this.Desc = Desc;
        this.Checked = Checked;

    }


    public String getActId(){
        return Id;
    }
    public String getActDesc() {
        return Desc;
    }
    public boolean getActChecked() { return Checked; }





}
