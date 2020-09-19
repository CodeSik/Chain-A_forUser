package com.example.chaina.map.gara;

import java.util.ArrayList;

public class Data {
    private static ArrayList<Person> scene1;
    private static ArrayList<Person> scene2;
    private static ArrayList<Person> scene3;

    public static ArrayList<Person> getScene1() {
        if(scene1 == null) {
            scene1 = new ArrayList<>();
            ArrayList<Trace> traces = new ArrayList<>();

            traces.add(new Trace("37.636761319856376", "126.78761435991836", "", "", ""));
            traces.add(new Trace("37.62687513898674", "126.80178000984121", "", "", ""));
            traces.add(new Trace("37.62342140293069", "126.80541401790221", "", "", ""));
            traces.add(new Trace("37.625746475938016", "126.81700779821762", "", "", ""));
            traces.add(new Trace("37.62214562038438", "126.81901025166742", "", "", ""));
            traces.add(new Trace("37.61797354058824", "126.82463845420544", "", "", ""));
            traces.add(new Trace("37.61841160714447", "126.82844325012518", "", "", ""));
            traces.add(new Trace("37.6217272271839", "126.82843563122468", "능굴동산 산동네 카페", "경기 고양시 능곡1동 110-32", "2020.09.11 16시 39분 32초"));

            ArrayList<Trace> traces2 = new ArrayList<>();
            traces2.add(new Trace("37.61797354058824", "126.82463845420544", "", "", ""));
            traces2.add(new Trace("37.61841160714447", "126.82844325012518", "", "", ""));
            traces2.add(new Trace("37.62498322079305", "126.83721856555057", "롯데마트", "경기 고양시 능곡1동 110-64", "2020.09.11 16시 49분 2초"));
            traces2.add(new Trace("37.64834038537595", "126.83988713869309", "", "", ""));

            scene1.add(new Person(0, traces));
            scene1.add(new Person( 1, traces2));
        }

        return scene1;
    }

    public static ArrayList<Person> getScene2() {
        if(scene2 == null) {
            scene2 = new ArrayList<>();
        }

        return scene2;
    }

    public static ArrayList<Person> getScene3() {
        if(scene3 == null) {
            scene3 = new ArrayList<>();
        }

        return scene3;
    }
}
