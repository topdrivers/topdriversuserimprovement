package com.topdrivers.userv2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.topdrivers.userv2.Helper.CustomDialog;
import com.topdrivers.userv2.Helper.SharedHelper;
import com.topdrivers.userv2.Helper.URLHelper;
import com.topdrivers.userv2.R;
import com.topdrivers.userv2.TopdriversApplication;
import com.topdrivers.userv2.Utils.MyButton;
import com.topdrivers.userv2.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.topdrivers.userv2.TopdriversApplication.trimMessage;

public class RatingActivity extends AppCompatActivity {

    CustomDialog customDialog;
    String feedBackRating;
    RatingBar ratingProviderRate;
    EditText txtCommentsRate;
    Utilities utils = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        txtCommentsRate = (EditText) findViewById(R.id.txtComments);
        ratingProviderRate = (RatingBar) findViewById(R.id.ratingProviderRate);
        MyButton btnSubmitReview = (MyButton) findViewById(R.id.btnSubmitReview);
        LinearLayout lnrRateProvider = (LinearLayout) findViewById(R.id.lnrRateProvider);
        LayerDrawable drawable = (LayerDrawable) ratingProviderRate.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"),
                PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"),
                PorterDuff.Mode.SRC_ATOP);
        feedBackRating = "1";
        ratingProviderRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if (rating < 1.0f) {
                    ratingProviderRate.setRating(1.0f);
                    feedBackRating = "1";
                }
                feedBackRating = String.valueOf((int) rating);
            }
        });
      
        /*lnrRateProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.hideKeypad(getActivity(), getActivity().getCurrentFocus());
            }
        });*/
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReviewCall();
            }
        });
        txtCommentsRate.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txtCommentsRate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submitReviewCall();
                }
                return false;
            }
        });


    }

    public void submitReviewCall() {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(this, "request_id"));
            object.put("rating", feedBackRating);
            object.put("comment", "" + txtCommentsRate.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLHelper.RATE_PROVIDER_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                utils.print("SubmitRequestResponse", response.toString());
                utils.hideKeypad(RatingActivity.this, getCurrentFocus());
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();

                Intent mainIntent = new Intent(RatingActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
                // ((MainActivity) getActivity()).callRefreshHomeFragment();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {


                        } else if (response.statusCode == 401) {
                            refreshAccessToken("SUBMIT_REVIEW");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));

                        } else if (response.statusCode == 503) {

                        } else {

                        }

                    } catch (Exception e) {

                    }

                } else {

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(RatingActivity.this, "token_type") + " "
                        + SharedHelper.getKey(RatingActivity.this, "access_token"));
                return headers;
            }
        };

        TopdriversApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void refreshAccessToken(final String tag) {

        JSONObject object = new JSONObject();
        try {

            object.put("grant_type", "refresh_token");
            object.put("client_id", URLHelper.client_id);
            object.put("client_secret", URLHelper.client_secret);
            object.put("refresh_token", SharedHelper.getKey(this, "refresh_token"));
            object.put("scope", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLHelper.login, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                utils.print("SignUpResponse", response.toString());
                SharedHelper.putKey(RatingActivity.this, "access_token", response.optString("access_token"));
                SharedHelper.putKey(RatingActivity.this, "refresh_token", response.optString("refresh_token"));
                SharedHelper.putKey(RatingActivity.this, "token_type", response.optString("token_type"));
                if (tag.equalsIgnoreCase("SUBMIT_REVIEW")) {
                    submitReviewCall();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = "";
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    SharedHelper.putKey(RatingActivity.this, "loggedIn",
                            getResources().getString(R.string.False));
                    utils.GoToBeginActivity(RatingActivity.this);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        TopdriversApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
