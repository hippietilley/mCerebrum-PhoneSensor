package org.md2k.phonesensor.phone.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.md2k.datakitapi.DataKitApi;
import org.md2k.datakitapi.datatype.DataTypeFloatArray;
import org.md2k.datakitapi.source.datasource.DataSource;
import org.md2k.datakitapi.source.datasource.DataSourceBuilder;
import org.md2k.datakitapi.source.datasource.DataSourceType;
import org.md2k.datakitapi.time.DateTime;
import org.md2k.phonesensor.phone.CallBack;

/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class Compass extends PhoneSensorDataSource implements SensorEventListener{
    private static final String TAG = Compass.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    public static final String NORMAL="Normal: ~6 Hz";
    public static final String UI="UI: ~16 Hz";
    public static final String GAME="Game: ~50 Hz";
    public static final String FASTEST="Fastest: ~100Hz";
    public static String[] frequencyOptions={NORMAL,UI,GAME,FASTEST};

    public DataSourceBuilder createDataSourceBuilder() {
        DataSourceBuilder dataSourceBuilder=super.createDataSourceBuilder();
        if(dataSourceBuilder==null) return null;
        dataSourceBuilder=dataSourceBuilder.setMetadata("frequency", frequency);
        return dataSourceBuilder;
    }

    public void updateDataSource(DataSource dataSource){
        super.updateDataSource(dataSource);
        frequency=dataSource.getMetadata().get("frequency");
    }
    public Compass(Context context, boolean enabled) {
        super(context, DataSourceType.COMPASS, enabled);
        frequency=UI;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] samples=new float[3];
        samples[0]=event.values[0];
        samples[1]=event.values[1];
        samples[2]=event.values[2];
        DataTypeFloatArray dataTypeFloatArray=new DataTypeFloatArray(DateTime.getDateTime(),samples);
        mDataKitApi.insert(dataSourceClient, dataTypeFloatArray);
        callBack.onReceivedData(dataTypeFloatArray);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void unregister(){
        mSensorManager.unregisterListener(this);
    }

    public void register(DataKitApi dataKitApi, DataSource dataSource, CallBack newcallBack) {
        mDataKitApi = dataKitApi;
        dataSourceClient = dataKitApi.register(dataSource).await();
        callBack = newcallBack;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if(frequency.equals(UI))
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        else if(frequency.equals(GAME))
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        else if(frequency.equals(FASTEST))
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        else if(frequency.equals(NORMAL))
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
