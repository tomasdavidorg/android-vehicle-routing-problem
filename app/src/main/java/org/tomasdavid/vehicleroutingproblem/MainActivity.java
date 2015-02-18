package org.tomasdavid.vehicleroutingproblem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;

import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.AirLocation;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        VehicleRoutingSolution vrs = init();

        new SolverTask(this).execute(vrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private VehicleRoutingSolution init() {
        VehicleRoutingSolution vrs = new VehicleRoutingSolution();

        StringBuilder s = new StringBuilder();
        s.append("INFO: \n");

        AirLocation loc1 = new AirLocation();
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        loc1.setName("one");
        loc1.setId(1l);

        AirLocation loc2 = new AirLocation();
        loc2.setLatitude(0);
        loc2.setLongitude(0.3);
        loc2.setName("two");
        loc2.setId(2l);

        AirLocation loc3 = new AirLocation();
        loc3.setLatitude(0.3);
        loc3.setLongitude(0.3);
        loc3.setName("three");
        loc3.setId(3l);

        AirLocation loc4 = new AirLocation();
        loc4.setLatitude(0.3);
        loc4.setLongitude(0);
        loc4.setName("four");
        loc4.setId(4l);

        Depot dep1 = new Depot();
        dep1.setLocation(loc1);
        dep1.setId(5l);

        Vehicle veh1 = new Vehicle();
        veh1.setDepot(dep1);
        veh1.setCapacity(20);
        veh1.setId(6l);

        Vehicle veh2 = new Vehicle();
        veh2.setDepot(dep1);
        veh2.setCapacity(5);
        veh2.setId(7l);

        Customer cus1 = new Customer();
        cus1.setDemand(5);
        cus1.setLocation(loc2);
        cus1.setId(8l);

        Customer cus2 = new Customer();
        cus2.setDemand(9);
        cus2.setLocation(loc3);
        cus2.setId(9l);

        Customer cus3 = new Customer();
        cus3.setDemand(11);
        cus3.setLocation(loc4);
        cus3.setId(10l);

        List<Location> ll = new ArrayList<>();
        ll.add(loc1);
        ll.add(loc2);
        ll.add(loc3);
        ll.add(loc4);

        List<Depot> dl = new ArrayList<>();
        dl.add(dep1);

        s.append("Depot " + dep1.getId() + " on location " +
                dep1.getLocation().getLatitude() + "x" + dep1.getLocation().getLongitude() + "\n");

        List<Vehicle> vl = new ArrayList<>();
        vl.add(veh1);
        vl.add(veh2);

        for (Vehicle v : vl) {
            s.append("Vehicle " + v.getId() + " with capacity " + v.getCapacity() + "\n");
        }

        List<Customer> cl = new ArrayList<>();
        cl.add(cus1);
        cl.add(cus2);
        cl.add(cus3);

        for (Customer c : cl) {
            s.append("Customer " + c.getId() + " with demand " + c.getDemand() + " on location " +
                    c.getLocation().getLatitude() + "x" + c.getLocation().getLongitude() +  "\n");
        }

        vrs.setId(99l);
        vrs.setLocationList(ll);
        vrs.setDepotList(dl);
        vrs.setVehicleList(vl);
        vrs.setCustomerList(cl);
        vrs.setName("myvrp");



        ((TextView)findViewById(R.id.text_info)).setText(s);

        return vrs;
    }
}
