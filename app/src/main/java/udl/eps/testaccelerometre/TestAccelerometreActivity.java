package udl.eps.testaccelerometre;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestAccelerometreActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean color = false;
    private TextView view;
    private long lastUpdate;
    private TextView background_color;
    private TextView lumView;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        view = findViewById(R.id.textView2);
        background_color = findViewById(R.id.textView);
        lumView = findViewById(R.id.textView3);

        background_color.setBackgroundColor(Color.GREEN);
        lumView.setBackgroundColor(Color.YELLOW);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
                sensorManager.registerListener(this,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
            } else{
                view.setText(R.string.no_accelerometer);
            }

        }
        // register this class as a listener for the accelerometer sensor

        lastUpdate = System.currentTimeMillis();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getAccelerometer(event);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2)
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;

            Toast.makeText(this, R.string.shuffed, Toast.LENGTH_SHORT).show();
            if (color) {
                background_color.setBackgroundColor(Color.GREEN);

            } else {
                background_color.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }

    private void getLuminosity(){

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}