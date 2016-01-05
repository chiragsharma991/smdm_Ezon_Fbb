package tna.aperotechnologies.com.tna;

/**
 * Created by mpatil on 17/07/15.
 */
//Used for listview data items of Field Selection at LLGeneration and Change Fields at Line List
public class ActivityColumnList
{
    String code = null;
    String desc = null;
    boolean selected = false;

    public ActivityColumnList(String code, String desc, boolean selected)
    {
        super();
        this.code = code;
        this.desc = desc;
        this.selected = selected;
    }

    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getdesc()
    {
        return desc;
    }
    public void setdesc(String desc)
    {
        this.desc = desc;
    }

    public boolean isSelected()
    {
        return selected;
    }
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    public void toggleChecked() {
        selected = !selected ;
    }
}
