package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.samples.vision.ocrreader.utils.Type;

public class ScrollingActivity extends AppCompatActivity {
    LinearLayout instrument;
    LinearLayout media;
    LinearLayout circuit;
    LinearLayout assembling;
    //LinearLayout geo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("Asignaturas");
        //getActionBar().setBackgroundDrawable(new ColorDrawable(0xff78b2ba));
        instrument = (LinearLayout) findViewById(R.id.instruments);
        media = (LinearLayout) findViewById(R.id.media);
        circuit = (LinearLayout) findViewById(R.id.circuit);
        assembling = (LinearLayout) findViewById(R.id.assembling);
        //geo = (LinearLayout) findViewById(R.id.geografia);
        instrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("TYPE", Type.TYPE_INSTRUMENT);
                startActivity(intent);
            }
        });
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("TYPE", Type.TYPE_MEDIA);
                startActivity(intent);
            }
        });
        circuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("TYPE", Type.TYPE_CIRCUIT);
                startActivity(intent);
            }
        });
        assembling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("TYPE", Type.TYPE_ASSEMBLING);
                startActivity(intent);
            }
        });
        /*geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);
                intent.putExtra("TYPE", Type.TYPE_GEOGRAPHY);
                startActivity(intent);
            }
        });*/
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
