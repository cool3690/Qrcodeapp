package com.cs.qrcode;

import android.util.AttributeSet;
import android.view.View;

import android.content.Context;
import java.util.HashMap;

class Mp  {

    HashMap<String,String> map=new HashMap();
    HashMap<String,String> status_map=new HashMap();

    public Mp() {

        map.put("A1","人資課");
        map.put("A2","會計課");
        map.put("A3","總務課");
        map.put("B1","業務課");
        map.put("C1","運輸課");
        map.put("D1","資訊課");
        map.put("D2","研發課");
        map.put("D3","檢測課");
        map.put("E1","進料課");
        map.put("E2","品保課");
        map.put("E3","調度課");
        map.put("E4","廠務課");
        map.put("F1","拌合控制課");
        map.put("F2","設備維護課");
        map.put("F0","設備維護課");
        map.put("G1","人文企劃課");
        status_map.put("1","良好");
        status_map.put("2","遺失");
        status_map.put("3","損壞");
    }

    public String search(String str) {
        if(map.containsKey(str))
             return map.get(str);
        else
            return "0";
    }
    public String search_status(String str) {
        if(status_map.containsKey(str))
            return status_map.get(str);
        else
            return "0";
    }

}
