package com.spc.schedule.storage.bean;

public class HandleResult {

    private boolean ok ;
    private String resone;

    public HandleResult(boolean ok,String resone){
         this.ok = ok;
         this.resone = resone;
    }

    public static HandleResult of(boolean ok,String resone){
        HandleResult hr = new HandleResult(ok,resone);
        return hr;
    }


    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getResone() {
        return resone;
    }

    public void setResone(String resone) {
        this.resone = resone;
    }
}
