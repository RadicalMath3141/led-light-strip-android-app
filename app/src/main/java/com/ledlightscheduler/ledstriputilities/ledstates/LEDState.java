package com.ledlightscheduler.ledstriputilities.ledstates;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ledlightscheduler.arduinopackets.DeserializerHandler;

public class LEDState implements Parcelable {

    private Color color;
    private long duration;

    public LEDState(){
        color = new Color(0,0,0);
        duration = 0;
    }

    public LEDState(Color color, long duration){
        this.color = color;
        this.duration = duration;
    }

    public Color getColor(long currentTime){
        return color;
    }

    public Color getColor(){
        return color;
    }

    public long getDuration(){
        return duration;
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public TransitionLEDState toTransitionLEDState(){
        return new TransitionLEDState(color, color, duration);
    }

    protected LEDState(Parcel in) {
        color = (Color) in.readValue(Color.class.getClassLoader());
        duration = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(color);
        dest.writeLong(duration);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LEDState> CREATOR = new Parcelable.Creator<LEDState>() {
        @Override
        public LEDState createFromParcel(Parcel in) {
            return new LEDState(in);
        }

        @Override
        public LEDState[] newArray(int size) {
            return new LEDState[size];
        }
    };

    public String serialize(){
        return "[" + duration + "," + color.serialize() + "]";
    }

    public static LEDState deserialize(String input){
        if(input.length() >= 11){
            DeserializerHandler handler = new DeserializerHandler(input);
            long duration = handler.getNextInteger();
            Color startColor = Color.deserialize(handler.getNextItemInBrackets());
            String possibleEndColor = handler.getNextItemInBrackets();
            if(!possibleEndColor.equals("")){
                return new TransitionLEDState(startColor, Color.deserialize(possibleEndColor), duration);
            } else {
                return new LEDState(startColor, duration);
            }
        }
        return new LEDState();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof LEDState){
            LEDState ledState = (LEDState) other;
            return ledState.color.equals(color) && ledState.duration == duration;
        }
        return false;
    }
}