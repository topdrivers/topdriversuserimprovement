package com.topdrivers.userv2.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.topdrivers.userv2.Helper.SharedHelper;
import com.topdrivers.userv2.R;

import org.json.JSONObject;

public class InvoceDailog extends BottomSheetDialogFragment {
    static View vie;
    static JSONObject requestStatusCheckObject;
    MyBoldTextView lblBasePrice, lblExtraPrice, lblTimeTaken,
            lblDistancePrice, lblTaxPrice, lblTotalPrice, lblPaymentTypeInvoice,
            lblDiscountPrice, lblWalletPrice, booking_id, lblDistanceCovered;
    ImageView imgPaymentTypeInvoice;
    MyButton btnPayNow;
    MyButton btnPaymentDoneBtn;
    LinearLayout discountDetectionLayout, walletDetectionLayout;
    LinearLayout bookingIDLayout;
    LinearLayout lnrInvoice;

    public static InvoceDailog newInstance(View view, JSONObject requestStatusCheckObjec) {
        vie = view;
        requestStatusCheckObject = requestStatusCheckObjec;
        return new InvoceDailog();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.invoce_layout, container,
                false);
        lnrInvoice = (LinearLayout) rootView.findViewById(R.id.lnrInvoice);
        lblBasePrice = (MyBoldTextView) rootView.findViewById(R.id.lblBasePrice);
        lblExtraPrice = (MyBoldTextView) rootView.findViewById(R.id.lblExtraPrice);
        lblDistancePrice = (MyBoldTextView) rootView.findViewById(R.id.lblDistancePrice);
        lblTimeTaken = (MyBoldTextView) rootView.findViewById(R.id.lblTimeTaken);
        //lblCommision = (MyBoldTextView) rootView.findViewById(R.id.lblCommision);
        lblTaxPrice = (MyBoldTextView) rootView.findViewById(R.id.lblTaxPrice);
        lblTotalPrice = (MyBoldTextView) rootView.findViewById(R.id.lblTotalPrice);
        lblDistanceCovered = (MyBoldTextView) rootView.findViewById(R.id.lblDistanceCovered);
        lblPaymentTypeInvoice = (MyBoldTextView) rootView.findViewById(R.id.lblPaymentTypeInvoice);
        booking_id = (MyBoldTextView) rootView.findViewById(R.id.booking_id);
        imgPaymentTypeInvoice = (ImageView) rootView.findViewById(R.id.imgPaymentTypeInvoice);
        btnPayNow = (MyButton) rootView.findViewById(R.id.btnPayNow);
        btnPaymentDoneBtn = (MyButton) rootView.findViewById(R.id.btnPaymentDoneBtn);
        bookingIDLayout = (LinearLayout) rootView.findViewById(R.id.bookingIDLayout);
        walletDetectionLayout = (LinearLayout) rootView.findViewById(R.id.walletDetectionLayout);
        discountDetectionLayout =
                (LinearLayout) rootView.findViewById(R.id.discountDetectionLayout);
        lblWalletPrice = (MyBoldTextView) rootView.findViewById(R.id.lblWalletPrice);
        lblDiscountPrice = (MyBoldTextView) rootView.findViewById(R.id.lblDiscountPrice);
        // get the views and attach the listener

        JSONObject payment =
                requestStatusCheckObject.optJSONObject(
                        "payment");
        String isPaid =
                requestStatusCheckObject.optString(
                        "paid");
        int totalRideAmount =
                payment.optInt(
                        "total_amount_given");
        int walletAmountDetected =
                payment.optInt(
                        "wallet");
        int couponAmountDetected =
                payment.optInt(
                        "discount");
        String paymentMode =
                requestStatusCheckObject.optString(
                        "payment_mode");
        lblDistanceCovered.setText(requestStatusCheckObject.optString("distance") + " KM");
        lblBasePrice.setText(SharedHelper.getKey(getContext(), "currency") + "" + payment.optString("fixed"));
        lblTaxPrice.setText(SharedHelper.getKey(getContext(), "currency") + "" + payment.optString("tax"));
        lblDistancePrice.setText(SharedHelper.getKey(getContext(), "currency")
                + "" + payment.optString("distance"));
        lblTimeTaken.setText(requestStatusCheckObject.optString("travel_time") + " mins");
        lblDiscountPrice.setText(SharedHelper.getKey(getContext(), "currency") + "" + couponAmountDetected);
        lblWalletPrice.setText(SharedHelper.getKey(getContext(), "currency") + "" + walletAmountDetected);
        lblPaymentTypeInvoice.setText(paymentMode);
        //lblCommision.setText
        // (SharedHelper.getKey
        // (context, "currency") + ""
        // + payment
        // .optString("commision"));
        lblTotalPrice.setText(SharedHelper.getKey(getContext(), "currency") + "" + payment.optString("total_amount_given"));


        if (requestStatusCheckObject.optString(
                "booking_id") != null &&
                !requestStatusCheckObject.optString(
                        "booking_id").equalsIgnoreCase("")) {
            booking_id.setText(requestStatusCheckObject.optString("booking_id"));
        } else {
            bookingIDLayout.setVisibility(View.GONE);
            booking_id.setVisibility(View.GONE);
        }
        lnrInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;

    }


}