package com.murtraja.sensorization;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

	TextView sensorData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SensorManager sMgr;
		sMgr = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		List<Sensor> list = sMgr.getSensorList(Sensor.TYPE_ALL);
		sensorData = (TextView) findViewById(R.id.sensorData);
		Button postbtn = (Button) findViewById(R.id.postButton);
		boolean flag = true;
		for(Sensor sensor: list){
			if(sensor.getType()==sensor.TYPE_LIGHT)
			{
				Log.d("MMR", sensor.toString());
				sensorData.setText("Light sensor available!");
				flag = false;
			}
		}
		if (flag)
		{
			Log.d("MMR", "no light sensor found");
			sensorData.setText("Light sensor not found");
			postbtn.setEnabled(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No light sensor found! Sorry...")
			.setTitle("H/w N/A");
			final ActionBarActivity act = this;
			builder.setPositiveButton("exit", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					act.finish();
				}
			}).show();
			//AlertDialog dialog = builder.create();

		}
		sMgr.registerListener(this, sMgr.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

		postbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etName = (EditText)findViewById(R.id.sensorName);
				String Name = etName.getText().toString();
				EditText etServer = (EditText)findViewById(R.id.serverURI);
				String serverURL = etServer.getText().toString();
				Log.d("MMR", Name);
				if(Name==null || "".equals(Name))
				{
					etName.setError("required");
					return;
				}
				if(serverURL == null || "".equals(serverURL))
				{
					etServer.setError("required");
					return;
				}
				PostData pd = new PostData();
				pd.execute(serverURL, Name, sensorData.getText().toString());
			}
		});


	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType()==Sensor.TYPE_LIGHT)
		{

			sensorData.setText(Float.toString(event.values[0]));
			//Log.d("MMR","onSensorChanged invoked");
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if(sensor.getType()==sensor.TYPE_LIGHT)
		{
			Log.d("MMR", "onAccuracyChanged invoked");
		}
	}
}
