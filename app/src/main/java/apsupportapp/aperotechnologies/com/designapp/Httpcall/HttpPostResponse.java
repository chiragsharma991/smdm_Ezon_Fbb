package apsupportapp.aperotechnologies.com.designapp.Httpcall;

import org.json.JSONObject;

/**
 * Created by csuthar on 03/07/17.
 */

public interface HttpPostResponse
{
    void PostResponse(JSONObject response);

    void PostDataNotFound();


}
