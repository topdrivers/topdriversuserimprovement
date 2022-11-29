package com.topdrivers.userv2.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kyra on 1/11/2016.
 */
public class PlacePredictions implements Serializable {

    public String strSourceLatitude = "";
    public String strSourceLongitude = "";
    public String strSourceLatLng = "";
    public String strSourceAddress = "";

    public String strDestLatitude = "";
    public String strDestLongitude = "";
    public String strDestLatLng = "";
    public String strDestAddress = "";

    public ArrayList<PlaceAutoComplete> getPlaces() {

        // list = new ArrayList<String>(new LinkedHashSet<String>(predictions));
        Set<PlaceAutoComplete> set = new HashSet<>(predictions);
        predictions.clear();
        predictions.addAll(set);
        return predictions;
    }

    public void setPlaces(ArrayList<PlaceAutoComplete> places) {
        this.predictions = places;
    }

    private ArrayList<PlaceAutoComplete> predictions;

}
