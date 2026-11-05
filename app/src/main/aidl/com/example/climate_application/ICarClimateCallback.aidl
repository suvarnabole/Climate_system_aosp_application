package com.example.climate_application;

import com.example.climate_application.ClimateStateParcel;

interface ICarClimateCallback {
    void onClimateStateChanged(in ClimateStateParcel state);
}
