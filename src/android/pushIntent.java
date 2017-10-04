package org.cordova.pushIntent;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.os.Bundle;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.PluginResult;

/**
 * This class echoes a string called from JavaScript.
 */
public class pushIntent extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getPushIntent")) {
            this.getPushIntent(callbackContext);
            return true;
        }
        return false;
    }

    private void getPushIntent(CallbackContext callbackContext) {
        Bundle extras = cordova.getActivity().getIntent().getExtras();
        if (extras != null)
        {
            PluginResult pluginResult = new  PluginResult(PluginResult.Status.OK, convertBundleToJson(extras));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
        else
            callbackContext.error("no data");
        
    }

    private static JSONObject convertBundleToJson(Bundle extras) {
        try {
            JSONObject json = new JSONObject();
            JSONObject additionalData = new JSONObject();

            // Add any keys that need to be in top level json to this set
            HashSet<String> jsonKeySet = new HashSet();
            Collections.addAll(jsonKeySet, "title","message","count","sound","image");

            Iterator<String> it = extras.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = extras.get(key);


                if (jsonKeySet.contains(key)) {
                    json.put(key, value);
                }
                else if (key.equals("coldstart")) {
                    additionalData.put(key, extras.getBoolean("coldstart"));
                }
                else if (key.equals("foreground")) {
                    additionalData.put(key, extras.getBoolean("foreground"));
                }
                else if (key.equals("dismissed")) {
                    additionalData.put(key, extras.getBoolean("dismissed"));
                }
                else if ( value instanceof String ) {
                    String strValue = (String)value;
                    try {
                        // Try to figure out if the value is another JSON object
                        if (strValue.startsWith("{")) {
                            additionalData.put(key, new JSONObject(strValue));
                        }
                        // Try to figure out if the value is another JSON array
                        else if (strValue.startsWith("[")) {
                            additionalData.put(key, new JSONArray(strValue));
                        }
                        else {
                            additionalData.put(key, value);
                        }
                    } catch (Exception e) {
                        additionalData.put(key, value);
                    }
                }
            } // while

            json.put("additionalData", additionalData);

            return json;
        }
        catch( JSONException e) {
        }
        return null;
    }

}
