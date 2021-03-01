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

        checkSensors();

        lastUpdate = System.currentTimeMillis();
    }


    private void checkSensors() {
        if (sensorManager != null) {
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                sensorManager.registerListener(this,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
                setAccelerometerParams();

            } else {
                view.setText(R.string.no_accelerometer);
            }

            if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
                sensorManager.registerListener(this,
                        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                        SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                lumView.setText(R.string.light_sensor_available);
            }
        }
    }


    private void setAccelerometerParams() {
        view.append("\n\n");

        view.append(getString(R.string.resolucio) + sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getResolution());
        view.append(getString(R.string.rang) + sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getMaximumRange());
        view.append(getString(R.string.consum) + sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getPower());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            getAccelerometer(event);
        else
            getLuminosity(event);
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
        if (accelationSquareRoot >= 2) {
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

    private void getLuminosity(SensorEvent event) {

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