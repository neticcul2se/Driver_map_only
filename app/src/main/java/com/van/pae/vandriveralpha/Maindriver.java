package com.van.pae.vandriveralpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Maindriver extends FragmentActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
     GoogleApiClient googleApiClient;

    setserver ipp = new setserver();
    String ipserver =ipp.ipserver;
    String udpipserver=ipp.ipudp;
    int reset=0;
    GoogleMap googleMap;

    double lati,longti;
    TextView hs;
    Marker mymarker;
    int zooms=0;
    Button pluss,minuss,bnt_des;
    String[] all;
    int size;
    int calsize;
    String msgs;
String Des1,Des2,dessent;
    int c_bnt=0;
String resultserver,msg;
    String drivername,insertresult,hassit;
    Boolean timeuser=true;
String iddriver,idvans;
    ImageView logout;
    int closemap=0;
    TextView rounds;
    int c_round=0;


    List<Marker> markers = new ArrayList<Marker>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindriver);




        pluss=(Button)findViewById(R.id.plus);
        minuss=(Button)findViewById(R.id.minus);
        bnt_des=(Button)findViewById(R.id.des);
        hs=(TextView)findViewById(R.id.pp);
        logout=(ImageView)findViewById(R.id.logout);
        rounds=(TextView)findViewById(R.id.round);


        if(android.os.Build.VERSION.SDK_INT>9){

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        Intent i =getIntent();
        drivername=i.getStringExtra("drivername");
        iddriver=i.getStringExtra("iddriver");
        idvans=i.getStringExtra("idvan");

        try {
            URL urls = new URL(ipserver+"insertdriveronline.php");
            JSONObject Joserver = new JSONObject();
            Joserver.put("code","21");
            Joserver.put("iddriver",iddriver);
            Joserver.put("idvans",idvans);



            insertresult=httppost(urls, Joserver);
           // Toast.makeText(Maindriver.this, insertresult, Toast.LENGTH_LONG).show();
            JSONObject Jgetmsg = new JSONObject(insertresult);
            Des1=Jgetmsg.getString("roaddes");
            Des2=Jgetmsg.getString("roadsrc");
            hassit=Jgetmsg.getString("hassit");
            c_round=Jgetmsg.getInt("c_round");






            bnt_des.setText(Des1);
            hs.setText(hassit);
            dessent=bnt_des.getText().toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Toast.makeText(Maindriver.this,drivername,Toast.LENGTH_LONG).show();



        try {
            URL urls = new URL(ipserver+"chang_des.php");
            JSONObject jo2=new JSONObject();
            jo2.put("code","15");
            jo2.put("iddriver",iddriver);
            jo2.put("idvan",idvans);
            jo2.put("desvalue",dessent);
            resultserver=httppost(urls, jo2);

            JSONObject Jserver = new JSONObject(resultserver);
            msg=Jserver.getString("msg");

            //Toast.makeText(Maindriver.this,resultserver,Toast.LENGTH_LONG).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder rouds = new AlertDialog.Builder(Maindriver.this);
                rouds.setTitle("เริ่มรอบรถใหม่");
                rouds.setMessage("ต้องการเริ่มรอบการเดินรถใหม่หรือไม่");
                rouds.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            URL urls = new URL(ipserver + "insertround.php");
                            JSONObject jo = new JSONObject();
                            jo.put("code", "25");
                            jo.put("idvan", idvans);
                            jo.put("round", c_round);


                            resultserver = httppost(urls, jo);

                            //  JSONObject Jserver = new JSONObject(resultserver);
                            hassit = "0";
                            hs.setText(hassit);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                rouds.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                rouds.show();




            }
        });





        bnt_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(c_bnt==0) {
                    bnt_des.setText(Des2);
                    dessent=bnt_des.getText().toString();


                    c_bnt=1;
                }else{
                    bnt_des.setText(Des1);
                    dessent=bnt_des.getText().toString();


                    c_bnt=0;

                }
                try {
                    URL urls = new URL(ipserver+"chang_des.php");
                    JSONObject jo2=new JSONObject();
                    jo2.put("code","15");
                    jo2.put("iddriver",iddriver);
                    jo2.put("idvan",idvans);
                    jo2.put("desvalue",dessent);
                    resultserver=httppost(urls, jo2);

                    JSONObject Jserver = new JSONObject(resultserver);
                    msg=Jserver.getString("msg");

                    //Toast.makeText(Maindriver.this,idvans+","+iddriver,Toast.LENGTH_LONG).show();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pluss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    URL urls=new URL(ipserver+"driver_sit.php");
                    JSONObject jo =new JSONObject();
                    jo.put("code","11");
                    jo.put("iddriver",iddriver);
                    jo.put("idvan",idvans);
                    jo.put("operetor","plus");


                    resultserver=httppost(urls, jo);

                    JSONObject Jserver = new JSONObject(resultserver);
                    msg=Jserver.getString("hassit");
                 //Toast.makeText(Maindriver.this, resultserver, Toast.LENGTH_LONG).show();
                    hassit=msg;
                    hs.setText(hassit);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        minuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    URL urls=new URL(ipserver+"driver_sit.php");
                    JSONObject jo =new JSONObject();
                    jo.put("code","11");
                    jo.put("iddriver",iddriver);
                    jo.put("idvan",idvans);
                    jo.put("operetor","minus");

                    resultserver=httppost(urls, jo);

                    JSONObject Jserver = new JSONObject(resultserver);
                    msg=Jserver.getString("hassit");
                     // Toast.makeText(Maindriver.this, resultserver, Toast.LENGTH_LONG).show();
                    hassit=msg;
                    hs.setText(hassit);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

            Thread driverlocation = new Thread(new driverlocation());
            driverlocation.start();
            Thread getusers = new Thread(new getuser());
            getusers.start();
    }

    public String httppost(URL url,JSONObject jo) {
        StringBuilder str = new StringBuilder();
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url.toURI());
            // Prepare JSON to send by setting the entity
            httppost.setEntity(new StringEntity(jo.toString(), "UTF-8"));
            // Set up the header types needed to properly transfer JSON
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Accept-Encoding", "application/json");


            // Execute get
            HttpResponse response = httpClient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return str.toString();

    }



    public  void makerMy(double latitude,double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        if (mymarker != null)
        {
            mymarker.remove();
        }



        mymarker =  googleMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_car))


        );




        if(zooms==0) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        }

    }

    @Override
    public void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {

            googleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(Maindriver.this,""+location.getLatitude(),Toast.LENGTH_LONG).show();
        double latitude= location.getLatitude();
        double longitude = location.getLongitude();
        lati=latitude;
        longti=longitude;
        makerMy(latitude, longitude);

        if(closemap==1) {

            try {

                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }




    @Override
    public void onConnected(Bundle bundle) {


        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);



        //Boolean xx=locationAvailability.isLocationAvailable();
        //Toast.makeText(Maindriver.this, xx.toString(), Toast.LENGTH_LONG).show();



       LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)
                .setFastestInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

/*
        } else {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    class driverlocation implements Runnable{

        final android.os.Handler handler = new android.os.Handler();

        @Override
        public void run() {

            while (timeuser){

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String c_datetime = df.format(c.getTime());

                int sortport = 4444;
                DatagramSocket s =null;
                InetAddress localip = null;
                try {
                    s=new DatagramSocket();
                    localip = InetAddress.getByName(udpipserver);
                    //localip = InetAddress.getByName("192.168.56.1");

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                String data="22,"+iddriver+","+idvans+","+lati+","+longti+","+c_datetime;
                int msg_length=data.length();
                byte[] message=data.getBytes();
                DatagramPacket sent=new DatagramPacket(message,msg_length,localip,sortport);
                try {
                    s.send(sent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                    try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    class getuser implements Runnable{
        final android.os.Handler handler = new android.os.Handler();

        @Override
        public void run() {

            while (timeuser){
                long start = System.currentTimeMillis();
                long end = start + 20 * 1000;

                int sortport = 4444;
                DatagramSocket s =null;
                InetAddress localip = null;
                try {
                    s=new DatagramSocket();
                    localip = InetAddress.getByName(udpipserver);
                    //localip = InetAddress.getByName("192.168.56.1");

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                String data="20,"+idvans;
                int msg_length=data.length();
                byte[] message=data.getBytes();
                DatagramPacket sent=new DatagramPacket(message,msg_length,localip,sortport);
                try {
                    s.send(sent);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                while (System.currentTimeMillis() < end) {

                    byte[] message2 = new byte[1500];
                    DatagramPacket packet = new DatagramPacket(message2, message2.length);
                    try {
                        s.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    msgs = new String(packet.getData(), 0, packet.getLength());
//                  Toast.makeText(Maindriver.this,msgs,Toast.LENGTH_LONG).show();
                    all = msgs.split(",");
                    size = all.length;
                    calsize = (size - 1) / 3;
                    handler.post(new Runnable() {
                        public void run() {
                            for (Marker ss : markers) {
                                ss.remove();
                            }
                            markers.clear();

                            // addMaker
                            int j = 1;
                            int k = 2;
                            int l = 3;

                            //Toast.makeText(Maindriver.this, msgs, Toast.LENGTH_LONG).show();
                            for (int i = 0; i < calsize; i++) {
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(all[k]), Double.parseDouble(all[l])))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_me))
                                );

                                markers.add(marker);

                                j = j + 3;
                                l = l + 3;
                                k = k + 3;
                            }


                        }
                    });


                }
            }

        }
    }





}
