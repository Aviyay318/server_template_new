package com.app.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Objects extends BaseEntity{
    private String name;
    private String svg;
    private  List<Map<String, String>> svgList;

    public Objects() {
        svgList = new ArrayList<>();
    }

    public void addSvg(String name, String svg){
        Map<String, String> item = new HashMap<>();
        item.put("name", name);
        item.put("svg", svg);
        svgList.add(item);
    }

    public List<Map<String, String>> getSvgList() {
        return svgList;
    }

    @Override
    public String toString() {
        return "Objects{" +
                "svgList=" + svgList +
                '}';
    }
}
