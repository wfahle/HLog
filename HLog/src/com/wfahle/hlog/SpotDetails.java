package com.wfahle.hlog;

import android.os.Parcel;
import android.os.Parcelable;

public class SpotDetails implements Parcelable {
	private String call ;
	private String itemDescription;
	private String freq;
	private int imageNumber;

	// Parcelable implementation:

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(imageNumber);
        out.writeString(call);
        out.writeString(freq);
        out.writeString(itemDescription);
    }

    public static final Parcelable.Creator<SpotDetails> CREATOR
            = new Parcelable.Creator<SpotDetails>() {
        public SpotDetails createFromParcel(Parcel in) {
            return new SpotDetails(in);
        }

        public SpotDetails[] newArray(int size) {
            return new SpotDetails[size];
        }
    };
    
    private SpotDetails(Parcel in) {
        imageNumber = in.readInt();
        call = in.readString();
        freq = in.readString();
        itemDescription = in.readString();
    }
    
    
    public SpotDetails(String call, String freq, String msg, int imageNumber) {
    	this.call = call;
    	this.freq = freq;
    	this.itemDescription = msg;
    	this.imageNumber = imageNumber;
    }
    // rest of class
	public String getCall() {
		return call;
	}
	
	public void setCall(String call) {
		this.call = call;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}
	
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	public String getFrequency() {
		return freq;
	}
	
	public void setFrequency(String freq) {
		this.freq = freq;
	}
	
	public int getImageNumber() {
		return imageNumber;
	}
	
	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}
	
	@Override
	public String toString() {
		String ret = call+" " + freq + " " + itemDescription;
		return ret;
	}
	
}
