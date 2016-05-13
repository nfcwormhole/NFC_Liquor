package com.nfc.apps;

public class NDEFStructure {

    private String message;
    private String date;
    private String type;

    public NDEFStructure(String message, String date, String type) {
        super();
        this.setMessage(message);
        this.setDate(date);
        this.setType(type);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
