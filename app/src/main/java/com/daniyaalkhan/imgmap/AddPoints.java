package com.daniyaalkhan.imgmap;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.Arrays;

public class AddPoints extends AppCompatActivity {

    private ImageView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);

        final Bundle extras = getIntent().getExtras();

        final ImageView chartView = findViewById(R.id.chartView);
        chartView.setImageURI(Uri.parse(extras.getString("uri")));

        this.pinView = findViewById(R.id.pin);
        pinSet(new PointF(0,0));

        final EditText latTextView = findViewById(R.id.latitude);
        final EditText lonTextView = findViewById(R.id.longitude);

        final EditText xPixelView = findViewById(R.id.xPixel);
        final EditText yPixelView = findViewById(R.id.yPixel);

        Button confirmButton = findViewById(R.id.confirm);

        //Check if these are first set of coordinates
        if(extras.containsKey("set1")){

            setTitle("Second point coordinates");
            confirmButton.setText("Confirm");
            Log.d("Received Extras", Arrays.toString(extras.getFloatArray("geocoords1")));

        }else
            setTitle("First point coordinates");

        //Update pin position whenever it's changed
        TextWatcher pinUpdater = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                float x=0, y=0;

                if(!xPixelView.getText().toString().isEmpty()) x = Float.valueOf(xPixelView.getText().toString());
                if(!yPixelView.getText().toString().isEmpty()) y = Float.valueOf(yPixelView.getText().toString());

                pinSet(new PointF(x, y));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        xPixelView.addTextChangedListener(pinUpdater);
        yPixelView.addTextChangedListener(pinUpdater);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean errorFlag = false;

                String geoCoordsString[] = {latTextView.getText().toString(), lonTextView.getText().toString()};
                String pixelCoordsString[] = {xPixelView.getText().toString(), yPixelView.getText().toString()};

                //Check if all fields are filled
                if(geoCoordsString[0].isEmpty()){
                    latTextView.setError("Please enter a latitude");
                    errorFlag = true;
                }
                if(geoCoordsString[1].isEmpty()){
                    lonTextView.setError("Please enter a longitude");
                    errorFlag = true;
                }
                if(pixelCoordsString[0].isEmpty()){
                    xPixelView.setError("Please enter X Pixel");
                    errorFlag = true;
                }
                if(pixelCoordsString[1].isEmpty()){
                    yPixelView.setError("Please enter Y Pixel");
                    errorFlag = true;
                }


                if(!errorFlag){

                    //Check if these are first set of coordinates
                    if (extras.containsKey("set1")){

                        //Set variables
                        String icao=extras.getString("icao");

                        Uri uri = Uri.parse(extras.getString("uri"));

                        PointF geocords1, geocords2, pixelcords1, pixelcords2;

                        //float[] receivedGeoCords=extras.getFloatArray("geocoords1"), receivedPixelCoords=extras.getFloatArray("pixelcoords1");
                        //geocords1 = new PointF(receivedGeoCords[0], receivedGeoCords[1]);
                        //pixelcords1 = new PointF(receivedPixelCoords[0], receivedPixelCoords[1]);

                        //Log.d("GeoCords",geocords1.toString());

                    }else{

                        Intent addNextPoints = new Intent(getApplicationContext(), AddPoints.class);
                        addNextPoints.putExtra("set1", true);

                        //Send previous data as is
                        addNextPoints.putExtra("uri", extras.getString("uri"));
                        addNextPoints.putExtra("icao", extras.getString("icao"));

                        //Send coordinates
                        float[] geoPointsFloat = {Float.valueOf(geoCoordsString[0]), Float.valueOf(geoCoordsString[1])};
                        float[] pixelPointsFloat = {Float.valueOf(pixelCoordsString[0]), Float.valueOf(pixelCoordsString[1])};
                        addNextPoints.putExtra("geocoords1", geoPointsFloat);
                        addNextPoints.putExtra("pixelcoords1", pixelPointsFloat);

                        //Move to add next points
                        startActivity(addNextPoints);

                    }

                }
            }
        });
    }

    private void pinSet(PointF coords){

        int height=pinView.getHeight();

        pinView.setX(coords.x);

        //Image starts at top left, but pin is at bottom left:
        pinView.setY(coords.y-height);

    }
}
