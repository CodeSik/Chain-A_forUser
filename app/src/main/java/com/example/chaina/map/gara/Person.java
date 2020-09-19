package com.example.chaina.map.gara;

import java.util.ArrayList;

public class Person {
    public Person(int state, ArrayList<Trace> traces) {
        this.state = state;
        this.traces = traces;
    }

    private int state;  // 0: 미확진, 1: 확진
    private ArrayList<Trace> traces;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ArrayList<Trace> getTraces() {
        return traces;
    }

    public void setTraces(ArrayList<Trace> traces) {
        this.traces = traces;
    }
}
