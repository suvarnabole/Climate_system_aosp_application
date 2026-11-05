package com.example.climate_application;

import com.example.climate_application.ICarClimateCallback;
import com.example.climate_application.ClimateStateParcel;

interface ICarClimateService {
    ClimateStateParcel getClimateState();
    void setAcPower(boolean enabled);
    void setDriverTemperature(float temperature);
    void setPassengerTemperature(float temperature);
    void setFanSpeed(int speed);
    void setFanDirection(int direction);
    void setRecirculation(boolean enabled);
    void setFrontDefrost(boolean enabled);
    void setRearDefrost(boolean enabled);

    void registerCallback(ICarClimateCallback callback);
    void unregisterCallback(ICarClimateCallback callback);
}
