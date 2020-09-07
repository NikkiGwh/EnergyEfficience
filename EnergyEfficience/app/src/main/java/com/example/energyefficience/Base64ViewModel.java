package com.example.energyefficience;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class Base64ViewModel extends AndroidViewModel {

    private Base64Repository mBase64Respository;
    private LiveData<List<Base64BlindTextEntity>> allText;

    public Base64ViewModel(Application application) {
       super(application);
        mBase64Respository = new Base64Repository(application);
        allText = mBase64Respository.getAllEntries();
    }
    LiveData<List<Base64BlindTextEntity>> getAllEntries()
    {
        return allText;
    }
    public void insert(Base64BlindTextEntity text)
    {
        mBase64Respository.insert(text);
    }
}
