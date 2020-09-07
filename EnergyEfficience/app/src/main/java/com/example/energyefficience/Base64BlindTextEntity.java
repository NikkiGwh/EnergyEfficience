package com.example.energyefficience;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Base64BlindTextEntity {
    public Base64BlindTextEntity(int Id, String BlindText, String EncodedText) {
        this.BlindText = BlindText;
        this.EncodedText = EncodedText;
        this.Id = Id;
    }

    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String BlindText;
    private String EncodedText;

    public void setEncodedText(String encodedText) {
        EncodedText = encodedText;
    }
    public String getBlindText() {
        return BlindText;
    }

    public int getId() {
        return Id;
    }

    public String getEncodedText() {
        return EncodedText;
    }

}
