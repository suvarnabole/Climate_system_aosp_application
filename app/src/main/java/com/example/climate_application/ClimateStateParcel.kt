package com.example.climate_application

import android.os.Parcel
import android.os.Parcelable

data class ClimateStateParcel(
    val acPower: Boolean,
    val driverTemperature: Float,
    val passengerTemperature: Float,
    val fanSpeed: Int,
    val fanDirection: Int,
    val recirculation: Boolean,
    val frontDefrost: Boolean,
    val rearDefrost: Boolean,
    val connected: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (acPower) 1 else 0)
        parcel.writeFloat(driverTemperature)
        parcel.writeFloat(passengerTemperature)
        parcel.writeInt(fanSpeed)
        parcel.writeInt(fanDirection)
        parcel.writeByte(if (recirculation) 1 else 0)
        parcel.writeByte(if (frontDefrost) 1 else 0)
        parcel.writeByte(if (rearDefrost) 1 else 0)
        parcel.writeByte(if (connected) 1 else 0)
    }

    override fun describeContents(): Int = 0

    fun toUiState(): ClimateUiState {
        return ClimateUiState(
            acPower = acPower,
            driverTemperature = driverTemperature,
            passengerTemperature = passengerTemperature,
            fanSpeed = fanSpeed,
            fanDirection = fanDirection,
            recirculation = recirculation,
            frontDefrost = frontDefrost,
            rearDefrost = rearDefrost,
            connected = connected
        )
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ClimateStateParcel> {
            override fun createFromParcel(parcel: Parcel): ClimateStateParcel {
                return ClimateStateParcel(parcel)
            }

            override fun newArray(size: Int): Array<ClimateStateParcel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
