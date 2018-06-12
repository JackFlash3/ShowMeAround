package com.example.prodigalson7.showme.concurrency;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.MyStep;
import com.example.prodigalson7.showme.Model.Util;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by ProdigaLsON7 on 03/04/2018.
 *
 * https://maps.googleapis.com/maps/api/directions/json?origin=Haifa&destination=Tel-Aviv&key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E
 */

public class FetchRoute extends AsyncTask {
    private String URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private final String KEY = "AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E";
    private LatLng origin;                                    //origin Marker
    private LatLng destination;                         //destination Marker
    private List<MyStep> mRoute ;                   //route steps

    public FetchRoute(@NonNull  MyLocation destination,  @NonNull List<MyStep> mRoute) {
        this.origin = new LatLng(Util.getInstance().getCurrentLocation().getLat(), Util.getInstance().getCurrentLocation().getLon());
        this.destination = new LatLng(destination.getLat(), destination.getLon());
        this.mRoute = mRoute;
        //create the URL
        URL = URL + "origin="+this.origin.latitude+","+this.origin.longitude+
                "&destination="+this.destination.latitude+","+this.destination.longitude+
                "&key="+KEY;
    }

    @Override
    protected Object doInBackground(Object[] objects) {


        //1. DownLoad places
        try {
            getRoutes();
        } catch (JSONException e) {
            String msg = e.getMessage();
        }
        //2. reset blocking flag
        Util.getInstance().setConcurrencyFlag(false);
        return null;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    private boolean getRoutes()  throws JSONException{
        JSONObject serverResponse = null;   //Result from the server
        InputStream mInputStream;

        try {
            mInputStream = getStream(URL);                                                  //create an inputStream pipe
            serverResponse = convertStreamToJSON(mInputStream);     //convert stream to a JSONObject
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        //deciphering the URL
        return parseJsonObject(serverResponse);
    }

    //Download the JSONObject using URLConnection
    private InputStream getStream(String mURL) {
        try {
            InputStream is = new URL(mURL).openStream();
            return is;
        } catch (Exception ex) {
            return null;
        }
    }

    //Convert InputStream to JSONObject
    private JSONObject convertStreamToJSON(InputStream inputStreamObject) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
        return jsonObject;
    }

    private boolean parseJsonObject(JSONObject serverResponse) {
        try {
            if (serverResponse != null) {
                JSONArray jRoutess = serverResponse.getJSONArray("routes");
                for (int i = 0; i < jRoutess.length(); i++) {
                    JSONObject route = jRoutess.getJSONObject(i);
                    if (route.has("legs")) {
                        JSONArray jLegs = route.getJSONArray("legs");
                        for (int j = 0; j < jLegs.length(); j++) {
                            JSONObject step = jLegs.getJSONObject(j);
                            if (step.has("steps")){
                                JSONArray jSteps = step.getJSONArray("steps");
                                for (int m=0; m < jSteps.length(); m++){
                                    JSONObject jStep = jSteps.getJSONObject(m);
                                    LatLng start_location = null;
                                    LatLng end_location = null;
                                    if (jStep.has("start_location")){
                                        JSONObject jStart_location = jStep.getJSONObject("start_location");
                                        double lat = jStart_location.getDouble("lat");
                                        double lng = jStart_location.getDouble("lng");
                                        start_location = new LatLng(lat, lng);
                                    }
                                    if (jStep.has("end_location")){
                                        JSONObject jEnd_location = jStep.getJSONObject("end_location");
                                        double lat = jEnd_location.getDouble("lat");
                                        double lng = jEnd_location.getDouble("lng");
                                        end_location = new LatLng(lat, lng);
                                    }
                                    //append MyStep to the route
                                    if ((start_location != null) && (end_location != null)) {
                                        mRoute.add(new MyStep(start_location, end_location));
                                    }

                                }
                            }
                        }
                    }
                }
            }
            } catch(JSONException e){
                e.printStackTrace();
            }

        return true;
    }



        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<END TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
}
/*
{"bounds":{"northeast":{"lat":32.8001603,"lng":34.989013},"southwest":{"lat":32.7759748,"lng":34.9647139}},"copyrights":"Map data ©2018 Google, Mapa GISrael","legs":[{"distance":{"text":"7.5 km","value":7491},"duration":{"text":"17 mins","value":1022},"end_address":"Wizo St 19, Haifa, Israel","end_location":{"lat":32.7989943,"lng":34.9713608},"start_address":"IEC Tower, Haifa","start_location":{"lat":32.7877938,"lng":34.9649053},"steps":[{"distance":{"text":"0.1 km","value":123},"duration":{"text":"1 min","value":31},"end_location":{"lat":32.7888638,"lng":34.9647079},"html_instructions":"Head <b>north<\/b> toward <b>Kastra St<\/b><div style=\"font-size:0.9em\">Restricted usage road<\/div>","polyline":{"points":"uzbgEualtE{A@g@?Q?ODOF_@V"},"start_location":{"lat":32.7877938,"lng":34.9649053},"travel_mode":"DRIVING"},{"distance":{"text":"0.2 km","value":236},"duration":{"text":"1 min","value":50},"end_location":{"lat":32.7888205,"lng":34.9670671},"html_instructions":"Turn <b>right<\/b> onto <b>Kastra St<\/b><div style=\"font-size:0.9em\">Partial restricted usage road<\/div>","maneuver":"turn-right","polyline":{"points":"kacgEm`ltEGAKEKGACGI?U?a@?k@Dy@F_AL}BT}A"},"start_location":{"lat":32.7888638,"lng":34.9647079},"travel_mode":"DRIVING"},{"distance":{"text":"85 m","value":85},"duration":{"text":"1 min","value":19},"end_location":{"lat":32.7891767,"lng":34.9678012},"html_instructions":"At the roundabout, take the <b>2nd<\/b> exit","maneuver":"roundabout-right","polyline":{"points":"cacgEeoltE@A@A?A@??A?A?A?A?A?A?A?A?AAA?AAA?AA??AA?AAU]GMKSGMEKKS"},"start_location":{"lat":32.7888205,"lng":34.9670671},"travel_mode":"DRIVING"},{"distance":{"text":"1.4 km","value":1438},"duration":{"text":"1 min","value":89},"end_location":{"lat":32.7821457,"lng":34.977588},"html_instructions":"Continue onto <b>Flieman St<\/b>","polyline":{"points":"kccgEwsltECMCI?EAC?G@I@IH]`EgEx@uAPc@Ps@RcA@yA@sA?mA@]F]F[DQJ]JULSLSPOZUXMXGj@IlAEz@@~FJp@Aj@Mx@WpCyAn@g@z@}@b@w@`@aAVcAP{ABiAEuAIkAKiAMiAAM"},"start_location":{"lat":32.7891767,"lng":34.9678012},"travel_mode":"DRIVING"},{"distance":{"text":"1.5 km","value":1509},"duration":{"text":"2 mins","value":97},"end_location":{"lat":32.77814499999999,"lng":34.986672},"html_instructions":"Continue onto <b>Derech WeinShall<\/b>","polyline":{"points":"mwagE}pntEEaAAy@B}@Hq@Jm@Lk@Tm@NWTWXUVMXKZEL@|@PLFJDLFLFPL~@f@b@N`@Dd@@z@Mt@Wj@c@Z_@P]Ja@NuAHmBDoADaAHsADUJe@LWLQVUVMVK\\G~BQh@GTE\\OTQTWHMHMDMFQ@M@KBK?KAe@Ei@AOOe@EOGMS]KM_@_@s@k@oB_Bs@k@_Aq@q@i@"},"start_location":{"lat":32.7821457,"lng":34.977588},"travel_mode":"DRIVING"},{"distance":{"text":"0.8 km","value":790},"duration":{"text":"2 mins","value":97},"end_location":{"lat":32.78349840000001,"lng":34.9872377},"html_instructions":"Continue straight onto <b>Derech Freud<\/b>","maneuver":"straight","polyline":{"points":"k~`gEuiptEu@o@o@w@]c@o@s@[i@MSg@_Aa@y@]u@QUUKQCU@QHOHCBGFILGNOn@Ox@M\\GLGHEFKJODA?I@K?KCKC[MMGQGMEOCOAMAE?M?Q@K@KBMBIBIDOFGDMHg@b@aAx@KJSLOHIBC?E?C?CAEACACEGM"},"start_location":{"lat":32.77814499999999,"lng":34.986672},"travel_mode":"DRIVING"},{"distance":{"text":"1.6 km","value":1621},"duration":{"text":"5 mins","value":315},"end_location":{"lat":32.7969606,"lng":34.9842922},"html_instructions":"Turn <b>left<\/b> onto <b>Khorev St<\/b>\/<b>Route 672<\/b><div style=\"font-size:0.9em\">Continue to follow Route 672<\/div><div style=\"font-size:0.9em\">Go through 1 roundabout<\/div>","maneuver":"turn-left","polyline":{"points":"{_bgEgmptEKWILYVYTC@MJuAz@g@\\UNOJ_An@u@ZaAX_@Js@Lm@HQ@yAFm@@Q?UEAAICKGIEOMkAoAUWOMOKKISKCAQGIAKCIAK?eBA_A?s@AWA?AA?AAA??AA?AAA?A?A?A?A?A?A@A?A@A??@A??@A??@A??@?@A??@?@A@?@?@?@?@?@kBJgBH}@FO@k@H_@Ba@Di@Dw@LMBQBKBQHSFm@Vy@`@{@`@[Na@NsBt@cA\\oAd@cAZmBl@k@P]JYFSD"},"start_location":{"lat":32.78349840000001,"lng":34.9872377},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":438},"duration":{"text":"2 mins","value":94},"end_location":{"lat":32.7974213,"lng":34.9798227},"html_instructions":"Turn <b>left<\/b> onto <b>Tsafririm St<\/b>","maneuver":"turn-left","polyline":{"points":"_tdgEyzotEKBBP@R@H?F?NAJAHCHEL[bAELK`@YjBEZIh@[vBAL?L?L@RB\\JfAFx@@RDv@@TBR?DBJ"},"start_location":{"lat":32.7969606,"lng":34.9842922},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":101},"duration":{"text":"1 min","value":15},"end_location":{"lat":32.7966131,"lng":34.9793913},"html_instructions":"Continue onto <b>Bo'az St<\/b>","polyline":{"points":"{vdgE{~ntE@BBFDFFDNHj@TpAZ"},"start_location":{"lat":32.7974213,"lng":34.9798227},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":96},"duration":{"text":"1 min","value":13},"end_location":{"lat":32.7962511,"lng":34.9785157},"html_instructions":"Turn <b>right<\/b> onto <b>Rakhel St<\/b>","maneuver":"turn-right","polyline":{"points":"yqdgEe|ntEXRBBBFPd@Ln@?F@F@F@^"},"start_location":{"lat":32.7966131,"lng":34.9793913},"travel_mode":"DRIVING"},{"distance":{"text":"0.7 km","value":696},"duration":{"text":"2 mins","value":109},"end_location":{"lat":32.7996623,"lng":34.9726689},"html_instructions":"At the roundabout, take the <b>1st<\/b> exit onto <b>Alexander Yannai St<\/b>","maneuver":"roundabout-right","polyline":{"points":"qodgEwvntEA@A??@A??@A??@A??@A@?@?@A?A?C@C?C@QJIBKHiBpAKJMJILEHGNERKj@ALEJELEDIHYNOHKDIFIFKJGFMROPq@bAYd@OTOVKVIVELCNOx@G\\ER[bBMp@CJKr@Kj@m@zC"},"start_location":{"lat":32.7962511,"lng":34.9785157},"travel_mode":"DRIVING"},{"distance":{"text":"59 m","value":59},"duration":{"text":"1 min","value":33},"end_location":{"lat":32.7998412,"lng":34.9732504},"html_instructions":"Turn <b>right<\/b> onto <b>Shlomtsiyon HaMalka St<\/b>","maneuver":"turn-right","polyline":{"points":"{degEermtEO[GSCMAOCWAM"},"start_location":{"lat":32.7996623,"lng":34.9726689},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":299},"duration":{"text":"1 min","value":60},"end_location":{"lat":32.7989943,"lng":34.9713608},"html_instructions":"Turn <b>left<\/b> onto <b>Wizo St<\/b><div style=\"font-size:0.9em\">Destination will be on the left<\/div>","maneuver":"turn-left","polyline":{"points":"_fegEyumtEO@C?C@CBCBCDCFCHEPCVC`@Ch@?bAAV?TBRDVDJNPLLNJRFZ@nABj@C"},"start_location":{"lat":32.7998412,"lng":34.9732504},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}],"overview_polyline":{"points":"uzbgEualtEcC@a@Do@^GAWMIM?w@DeBT}DT}A@ABE?KEKCC]k@Sa@Q_@GW?[Jg@`EgEx@uAb@wARcA@yA@aDH{@Lm@Vs@Zg@l@e@r@Uj@IlAEzHLp@Aj@Mx@WpCyAn@g@z@}@b@w@`@aAVcAP{ABiAEuAUuCOwAG{BB}@Hq@XyAd@eAn@m@p@Yh@CjAXXL^T~@f@b@NfAFz@Mt@Wj@c@Z_@\\_ANuAHmBJqCNiBJe@LWd@g@n@Y\\G~BQ~@M\\OTQ^e@N[H_@DWAq@Gy@Uu@[k@k@m@wGiFgByAmA{Ao@s@[i@u@sA_AoBQUUKQCU@a@RKJQ\\_@hBUj@MP[PK@WCgAa@]I]Cq@Bc@Jo@\\iCvBc@LQEKSKWILs@l@QLcDtB_An@u@ZaBd@aBVkBH_A@WGUKYSaBgB_@Yu@_@k@GeDAkACAACAGCI@IDEJ?D?@kBLeDP{@JcD\\}@TaA^uBbAuFrBmH`CwA\\Fv@AZERs@`CeAhH?|@XrDJfBDNHNVNj@TpAZ\\VTl@PfA?`@C@CDABGBc@PuBzAYVOVMb@Mx@KXONi@XULURwApBi@z@[n@Od@ShAw@fEiAfGWo@E]Ee@S@GDGHQz@GjAAzABh@Jb@\\^b@RjBDj@C"},"summary":"Derech WeinShall","warnings":[],"waypoint_order":[]}
 */