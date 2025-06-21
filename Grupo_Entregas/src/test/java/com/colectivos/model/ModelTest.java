package com.colectivos.model;

import java.util.ArrayList;
import java.util.List;

public class ModelTest {
    public static void main (String arg[]) {
        Stop stop1 = new Stop(4986451, "Albarracin 123", null);
        List<Passenger> list = new ArrayList<>();
        Passenger pas1 = new Passenger(1, stop1);
        Passenger pas2 = new Passenger(98452041, stop1);
        Passenger pas3 = new Passenger(99651650, stop1);
        Passenger pas4 = new Passenger(49485415, stop1);
        Passenger pas5 = new Passenger(11111111, stop1);
        list.add(pas1);
        list.add(pas2);
        list.add(pas3);
        list.add(pas4);
        list.add(pas5);
        Stop stop2 = new Stop(4464184, "Gales 123", list);
        Stop stop3 = new Stop(4122111, "España 255", list);
        List<Stop> stopList = new ArrayList<>();
        stopList.add(stop2);
        stopList.add(stop3);
        Line line = new Line("A", stopList);
        Bus bus = new Bus(12345678, line);

        //System.out.println(pas1);
        System.out.println(bus);
    }

}
