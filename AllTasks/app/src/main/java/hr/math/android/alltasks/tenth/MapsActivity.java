package hr.math.android.alltasks.tenth;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import hr.math.android.alltasks.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap, map;
    private static final LatLng AMFITEATAR = new LatLng(44.873222,13.850155);
    private static final LatLng BUJE = new LatLng(45.413944,13.665951);
    private static final LatLng LUBENICE = new LatLng(44.8741918, 14.3177131);
    private String[] gradovi = {"PULA", "ZAGREB", "OSIJEK", "SPLIT"};
    private LatLng[] coords = {new LatLng(44.873222,13.850155), new LatLng(45.8239131, 15.9795961),
            new LatLng(45.5463866, 18.6538395), new LatLng(43.5149084, 16.4140641)};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(LUBENICE).title("Marker in Lubenice"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LUBENICE));

        map = mMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_sethybrid:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.menu_showtraffic:
                map.setTrafficEnabled(true);
                break;
            case R.id.menu_zoomin:
                map.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.menu_zoomout:
                map.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.menu_gotolocation:
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(AMFITEATAR) // Sets the center of the map to
                        .zoom(17)                   // Sets the zoom
                        .bearing(90) // Sets the orientation of the camera to east
                        .tilt(30)    // Sets the tilt of the camera to 30 degrees
                        .build();    // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                break;
            case R.id.menu_addmarker:
                map.addMarker(new MarkerOptions()
                        .position(AMFITEATAR)
                        .title("Arena")
                        .icon(BitmapDescriptorFactory
                        // .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        .fromResource(R.drawable.pushpin)));
                break;
            case R.id.menu_getcurrentlocation:
                // ---get your current location and display a blue dot---
                map.setMyLocationEnabled(true);

                break;
            case R.id.menu_showcurrentlocation:
                Location myLocation = map.getMyLocation();
                LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                        myLocation.getLongitude());

                CameraPosition myPosition = new CameraPosition.Builder()
                        .target(myLatLng).zoom(17).bearing(90).tilt(30).build();
                map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(myPosition));
                //da ucita kartu na kraju
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
            case R.id.menu_lineconnecttwopoints:
                //---add a marker at Apple---
                map.addMarker(new MarkerOptions()
                        .position(BUJE)
                        .title("Buje")

                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_AZURE)));

                //---draw a line connecting Apple and Golden Gate Bridge---
                map.addPolyline(new PolylineOptions()
                        .add(AMFITEATAR, BUJE).width(5).color(Color.RED));
                break;
            case R.id.menu_lineconnectfour:
                double minDistance = Double.POSITIVE_INFINITY;
                String city1 = "";
                String city2 = "";
                for (int i=0; i<coords.length; i++)
                    for (int j=i+1; j<coords.length; j++) {
                        map.addPolyline(new PolylineOptions().add(coords[i], coords[j]).width(5).color(Color.MAGENTA));
                        double currentDistance = distance(coords[i], coords[j]);
                        if(minDistance > currentDistance) {
                            minDistance = currentDistance;
                            city1 = gradovi[i];
                            city2 = gradovi[j];
                        }
                    }
                Toast.makeText(this, "Closest cities are: " + city1 + " and " + city2, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private double distance(LatLng a, LatLng b) {
        return (a.latitude - b.latitude)*(a.latitude - b.latitude)
                + (a.longitude - b.longitude)*(a.longitude - b.longitude);
    }

}
