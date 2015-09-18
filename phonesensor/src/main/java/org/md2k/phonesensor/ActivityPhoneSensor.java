package org.md2k.phonesensor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.md2k.datakitapi.datatype.DataType;
import org.md2k.datakitapi.datatype.DataTypeDoubleArray;
import org.md2k.datakitapi.datatype.DataTypeFloat;
import org.md2k.datakitapi.datatype.DataTypeFloatArray;
import org.md2k.datakitapi.source.datasource.DataSource;
import org.md2k.phonesensor.phone.sensors.PhoneSensorDataSource;
import org.md2k.phonesensor.phone.sensors.PhoneSensorDataSources;
import org.md2k.utilities.Apps;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
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

public class ActivityPhoneSensor extends Activity {
    private static final String TAG = ActivityPhoneSensor.class.getSimpleName();
    HashMap<String, TextView> hashMapData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sensor);
        final Button buttonService = (Button) findViewById(R.id.buttonServiceStartStop);

        buttonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPhoneSensor.this, ServicePhoneSensor.class);

                if (buttonService.getText().equals("Start Service")) {
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
        ((TextView)findViewById(R.id.textViewTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPhoneSensor.this, ServicePhoneSensor.class);
                if(((TextView) findViewById(R.id.textViewTime)).getText().equals("OFF")){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                intent = new Intent(this, ActivityPhoneSensorSettings.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                intent = new Intent(this, ActivityAbout.class);
                startActivity(intent);
                break;
            case R.id.action_copyright:
                intent = new Intent(this, ActivityCopyright.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    TableRow createDefaultRow() {
        TableRow row = new TableRow(this);
        TextView tvSensor = new TextView(this);
        tvSensor.setText("sensor");
        tvSensor.setTypeface(null, Typeface.BOLD);
        tvSensor.setTextColor(getResources().getColor(R.color.teal_a700));
        TextView tvCount = new TextView(this);
        tvCount.setText("count");
        tvCount.setTypeface(null, Typeface.BOLD);
        tvCount.setTextColor(getResources().getColor(R.color.teal_a700));
        TextView tvFreq = new TextView(this);
        tvFreq.setText("freq.");
        tvFreq.setTypeface(null, Typeface.BOLD);
        tvFreq.setTextColor(getResources().getColor(R.color.teal_a700));
        TextView tvSample = new TextView(this);
        tvSample.setText("samples");
        tvSample.setTypeface(null, Typeface.BOLD);
        tvSample.setTextColor(getResources().getColor(R.color.teal_a700));
        row.addView(tvSensor);
        row.addView(tvCount);
        row.addView(tvFreq);
        row.addView(tvSample);
        return row;
    }

    void prepareTable(PhoneSensorDataSources phsDataSources) {
        ArrayList<PhoneSensorDataSource> phoneSensorDataSources = phsDataSources.getPhoneSensorDataSources();
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout);
        ll.removeAllViews();
        ll.addView(createDefaultRow());
        for (int i = 0; i < phoneSensorDataSources.size(); i++) {
            if (phoneSensorDataSources.get(i).isEnabled()) {
                String dataSourceType = phoneSensorDataSources.get(i).getDataSourceType();
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView tvSensor = new TextView(this);
                tvSensor.setText(dataSourceType.toLowerCase() + "\n(" + phoneSensorDataSources.get(i).getFrequency() + ")");
                TextView tvCount = new TextView(this);
                tvCount.setText("0");
                hashMapData.put(dataSourceType + "_count", tvCount);
                TextView tvFreq = new TextView(this);
                tvFreq.setText("0");
                hashMapData.put(dataSourceType + "_freq", tvFreq);
                TextView tvSample = new TextView(this);
                tvSample.setText("0");
                hashMapData.put(dataSourceType + "_sample", tvSample);
                row.addView(tvSensor);
                row.addView(tvCount);
                row.addView(tvFreq);
                row.addView(tvSample);
                row.setBackgroundResource(R.drawable.border);
                ll.addView(row);
            }
        }

    }

    void updateTable(Intent intent) {
        String sampleStr = "";
        String dataSourceType = ((DataSource) intent.getSerializableExtra("datasource")).getType();
        int count = intent.getIntExtra("count", 0);
        hashMapData.get(dataSourceType + "_count").setText(String.valueOf(count));

        double time = (intent.getLongExtra("timestamp", 0) - intent.getLongExtra("starttimestamp", 0)) / 1000.0;
        double freq = (double) count / time;
        hashMapData.get(dataSourceType + "_freq").setText(String.format("%.1f", freq));


        DataType data = (DataType) intent.getSerializableExtra("data");
        if (data instanceof DataTypeFloat) {
            sampleStr = String.format("%.1f", ((DataTypeFloat) data).getSample());
        } else if (data instanceof DataTypeFloatArray) {
            float[] sample = ((DataTypeFloatArray) data).getSample();
            for (int i = 0; i < sample.length; i++) {
                if (i != 0) sampleStr += ",";
                if (i % 3 == 0 && i != 0) sampleStr += "\n";
                sampleStr = sampleStr + String.format("%.1f", sample[i]);
            }
        } else if (data instanceof DataTypeDoubleArray) {
            double[] sample = ((DataTypeDoubleArray) data).getSample();
            for (int i = 0; i < sample.length; i++) {
                if (i != 0) sampleStr += ",";
                if (i % 3 == 0 && i != 0) sampleStr += "\n";
                sampleStr = sampleStr + String.format("%.1f", sample[i]);
            }
        }
        hashMapData.get(dataSourceType + "_sample").setText(sampleStr);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTable(intent);
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("phonesensor"));
        PhoneSensorDataSources phoneSensorDataSources;
        phoneSensorDataSources = new PhoneSensorDataSources(ActivityPhoneSensor.this);
        prepareTable(phoneSensorDataSources);
        mHandler.post(runnable);
        super.onResume();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(runnable);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            {
                long time = Apps.serviceRunningTime(ActivityPhoneSensor.this, Constants.SERVICE_NAME);
                if (time < 0) {
                    ((TextView) findViewById(R.id.textViewTime)).setText("OFF");

                    ((Button) findViewById(R.id.buttonServiceStartStop)).setText("Start Service");
                    findViewById(R.id.buttonServiceStartStop).setBackground(getResources().getDrawable(R.drawable.button_green));

                } else {
                    long runtime = time / 1000;
                    int second = (int) (runtime % 60);
                    runtime /= 60;
                    int minute = (int) (runtime % 60);
                    runtime /= 60;
                    int hour = (int) runtime;
                    ((TextView) findViewById(R.id.textViewTime)).setText(String.format("%02d:%02d:%02d", hour, minute, second));
                    ((Button) findViewById(R.id.buttonServiceStartStop)).setText("Stop Service");
                    findViewById(R.id.buttonServiceStartStop).setBackground(getResources().getDrawable(R.drawable.button_red));

                }
                mHandler.postDelayed(this, 1000);
            }
        }
    };
}
