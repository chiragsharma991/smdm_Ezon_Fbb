package apsupportapp.aperotechnologies.com.designapp.Httpcall;

import org.json.JSONObject;

import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.MPM.mpm_model;

/**
 * Created by csuthar on 03/07/17.
 */

public interface HttpPostResponse
{
    void PostResponse(JSONObject response);

    void PostDataNotFound();


}
