package com.pinterest.android.pdk;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class PDKException extends Exception {

    static final long serialVersionUID = 1;
    private int _statusCode = -1;
    private String _detailMessage = "";
    protected String _baseUrl;
    protected String _method;

    public PDKException() {
        super();
    }

    public PDKException(String message) {
        super(message);
        _detailMessage = message;
    }

    public PDKException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PDKException(VolleyError error) {
        super();
        String message = "";
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            message = new String(error.networkResponse.data);
            _detailMessage = message;
            _statusCode = error.networkResponse.statusCode;
        }

        if (message.length() > 0 && message.startsWith("{")) {
            try {
                JSONObject errObj = new JSONObject(message);

                if (errObj.has("status")) {
                    _statusCode = errObj.getInt("status");
                }
                if (errObj.has("messsage")) {
                    _detailMessage = errObj.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // This error happens because the server sends a 401 (Unauthorized) but does not give a "WWW-Authenticate" which is a hint for the client what to do next.
        // The "WWW-Authenticate" Header tells the client which kind of authentication is needed (either Basic or Digest).
        // This is usually not very useful in headless http clients, but that's how the standard is defined.
        // The error occurs because the lib tries to parse the "WWW-Authenticate" header but can't.
        if (error != null && error.getMessage() != null && error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found")){
            _detailMessage = "You are not permitted to access that resource";
            _statusCode = 401;
        }
    }

    public void setStatusCode(int stausCode) {
        _statusCode = stausCode;
    }

    public void setDetailMessage(String detailMessage) {
        _detailMessage = detailMessage;
    }

    public int getStatusCode() {

        return _statusCode;
    }

    public String getDetailMessage() {
        return _detailMessage;
    }

}