package tna.aperotechnologies.com.tna;

/**
 * Created by mpatil on 13/08/15.
 */
public class StructureDataActDet
{
    public String FieldName = null;
    public String FieldCode = null;
    public String Value = null;
    public String Activityid = null;

    public StructureDataActDet(String Activityid, String FieldName, String FieldCode, String Value) {
        this.Activityid = Activityid;
        this.FieldCode = FieldCode;
        this.FieldName = FieldName;
        this.Value = Value;

    }



    public String getActivityId(){
        return Activityid;
    }
    public void setActivityid(String Activityid)
    {
        this.Activityid = Activityid;
    }
    public String getFieldName() {
        return FieldName;
    }
    public void setFieldName(String FieldName)
    {
        this.FieldName = FieldName;
    }

    public String getFieldCode() {
        return FieldCode;
    }
    public void setFieldCode(String FieldCode)
    {
        this.FieldCode = FieldCode;
    }

    public String getValue() { return Value; }
    public void setValue(String Value)
    {
        this.Value = Value;
    }






}
