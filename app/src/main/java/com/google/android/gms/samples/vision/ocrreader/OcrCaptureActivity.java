/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSource;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSourcePreview;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.samples.vision.ocrreader.utils.ElementModel;
import com.google.android.gms.samples.vision.ocrreader.utils.Type;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity for the multi-tracker app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class OcrCaptureActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String TextBlockObject = "String";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    ArrayList<ElementModel> elements;
    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    LinearLayout dialog;
    public void initElements(){
        int id=-1;
        elements = new ArrayList<>();
        ElementModel galvanometro = new ElementModel();
        galvanometro.setId(id++);
        galvanometro.setTitle("Galvanómetro");
        galvanometro.setUniquename("galvanometro");
        galvanometro.setDescription("Instrumento que se usa para detectar y medir la corriente eléctrica. Se trata de un transductor analógico electromecánico que produce una deformación de rotación en una aguja o puntero en respuesta a la corriente eléctrica que fluye a través de su bobina.");
        galvanometro.setKnowmore("https://es.wikipedia.org/wiki/Galvan%C3%B3metro");
        galvanometro.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        galvanometro.setPhoto(R.drawable.galvanometro);
        galvanometro.setTitleknowmore("Saber más..");
        galvanometro.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"galvanometro",Type.COLOR_INSTRUMENT));
        elements.add(galvanometro);


        ElementModel amperimetro = new ElementModel();
        amperimetro.setId(id++);
        amperimetro.setTitle("Amperímetro");
        amperimetro.setUniquename("amperimetro");
        amperimetro.setDescription("Un amperímetro en términos generales, es un simple galvanómetro (instrumento para detectar pequeñas cantidades de corriente), con una resistencia en paralelo, llamada \"resistencia shunt\"");
        amperimetro.setKnowmore("https://es.wikipedia.org/wiki/Amper%C3%ADmetro");
        amperimetro.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        amperimetro.setPhoto(R.drawable.amperimetro);
        amperimetro.setTitleknowmore("Saber más..");
        amperimetro.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"amperimetro",Type.COLOR_INSTRUMENT));
        elements.add(amperimetro);

        ElementModel voltimetro = new ElementModel();
        voltimetro.setId(id++);
        voltimetro.setTitle("Voltímetro");
        voltimetro.setUniquename("voltimetro");
        voltimetro.setDescription("Instrumento que sirve para medir la diferencia de potencial entre dos puntos de un circuito eléctrico");
        voltimetro.setKnowmore("https://www.youtube.com/watch?v=_dzfN0jXzKU");
        voltimetro.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        voltimetro.setPhoto(R.drawable.voltimetro);
        voltimetro.setTitleknowmore("Saber más..");
        voltimetro.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"voltimetro",Type.COLOR_INSTRUMENT));
        elements.add(voltimetro);

        ElementModel osciloscopioanalogico = new ElementModel();
        osciloscopioanalogico.setId(id++);
        osciloscopioanalogico.setTitle("Osciloscopio analógico");
        osciloscopioanalogico.setUniquename("oscanalogico");
        osciloscopioanalogico.setDescription("Osciloscopios que trabajan directamente con una señal aplicada, y ésta, amplificada, desvía un haz de electrones verticalmente, proporcional a su valor. En un tubo de rayos catódicos llega el rayo de electrones generado por el cátodo y acelerado por el ánodo.");
        osciloscopioanalogico.setKnowmore("http://sebastiandeargentina.over-blog.com/article-que-osciloscopio-analogico-85905406.html");
        osciloscopioanalogico.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        osciloscopioanalogico.setPhoto(R.drawable.osciloscopioanalogico);
        osciloscopioanalogico.setTitleknowmore("Saber más..");
        osciloscopioanalogico.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"osciloscopio analogico",Type.COLOR_INSTRUMENT));
        elements.add(osciloscopioanalogico);


        ElementModel osciloscopiodigital= new ElementModel();
        osciloscopiodigital.setId(id++);
        osciloscopiodigital.setTitle("Osciloscopio digital");
        osciloscopiodigital.setUniquename("oscdigital");
        osciloscopiodigital.setDescription("Instrumento de visualización electrónico para la representación gráfica de señales eléctricas que pueden variar en el tiempo");
        osciloscopiodigital.setKnowmore("https://www.youtube.com/watch?v=2U-mR62OVUg");
        osciloscopiodigital.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        osciloscopiodigital.setPhoto(R.drawable.osciloscopiodigital);
        osciloscopiodigital.setTitleknowmore("Saber más..");
        osciloscopiodigital.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"osciloscopio digital",Type.COLOR_INSTRUMENT));
        elements.add(osciloscopiodigital);

        ElementModel analizadorespectro= new ElementModel();
        analizadorespectro.setId(id++);
        analizadorespectro.setTitle("Analizador de espectro");
        analizadorespectro.setUniquename("anaespectro");
        analizadorespectro.setDescription("Equipo de medición electrónica que permite visualizar en una pantalla los componentes espectrales en un espectro de frecuencias de las señales presentes en la entrada, pudiendo ser ésta cualquier tipo de ondas eléctricas, acústicas u ópticas");
        analizadorespectro.setKnowmore("https://www.youtube.com/watch?v=aLTCGUQr7cU");
        analizadorespectro.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        analizadorespectro.setPhoto(R.drawable.analizador);
        analizadorespectro.setTitleknowmore("Saber más..");
        analizadorespectro.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"analizador de espectro",Type.COLOR_INSTRUMENT));
        elements.add(analizadorespectro);

        ElementModel partrenzado= new ElementModel();
        partrenzado.setId(id++);
        partrenzado.setTitle("Par Trenzado");
        partrenzado.setUniquename("partrenzado");
        partrenzado.setDescription("Tipo de cable que tiene dos conductores eléctricos aislados y entrelazados para anular las interferencias de fuentes externas y diafonía de los cables adyacentes. Fue inventado por Alexander Graham Bell en 1881");
        partrenzado.setKnowmore("https://es.wikipedia.org/wiki/Cable_de_par_trenzado");
        partrenzado.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        partrenzado.setPhoto(R.drawable.partrenzado);
        partrenzado.setTitleknowmore("Ver código de colores...");
        partrenzado.setTypeElement(new Type(Type.TYPE_MEDIA,"par trenzado",Type.COLOR_MEDIA));
        elements.add(partrenzado);

        ElementModel polarizacion= new ElementModel();
        polarizacion.setId(id++);
        polarizacion.setTitle("Polarización");
        polarizacion.setUniquename("polarizacion");
        polarizacion.setDescription("Figura geométrica que traza el extremo del vector campo eléctrico a una cierta distancia de la antena, al variar el tiempo. La polarización puede ser lineal, circular y elíptica. La polarización lineal puede tomar distintas orientaciones (horizontal, vertical, +45º, -45º). ");
        polarizacion.setKnowmore("https://www.youtube.com/watch?v=_QvsWyRrYKQ");
        polarizacion.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        polarizacion.setPhoto(R.drawable.polarizacion);
        polarizacion.setTitleknowmore("Ver vídeo...");
        polarizacion.setTypeElement(new Type(Type.TYPE_MEDIA,"par trenzado",Type.COLOR_MEDIA));
        elements.add(polarizacion);

        ElementModel circuito1= new ElementModel();
        circuito1.setId(id++);
        circuito1.setTitle("Esquema eléctrico de una lavadora");
        circuito1.setUniquename("circuito 1");
        circuito1.setDescription("¿Cómo de complejo es el esquema eléctrico de una lavadora convencional? ");
        circuito1.setKnowmore("https://bestengineeringprojects.com/electronics-washing-machine-control-circuit-diagram/");
        circuito1.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        circuito1.setPhoto(R.drawable.circuito);
        circuito1.setTitleknowmore("Ver explicación..");
        circuito1.setTypeElement(new Type(Type.TYPE_CIRCUIT,"circuito1",Type.COLOR_CIRCUIT));
        elements.add(circuito1);

        ElementModel circuito2= new ElementModel();
        circuito2.setId(id++);
        circuito2.setTitle("Ejercicio a resolver");
        circuito2.setUniquename("circuito 2");
        circuito2.setDescription("¿Cual es la resistencia equivalente en el circuito representado en la Figura 3? ");
        circuito2.setKnowmore("http://www.iesmajuelo.com/~tecno/comun/ejercicioscircuitosresueltos.pdf");
        circuito2.setTypeknowmore(ElementModel.TYPE_KNOWMORE_DOCUMENT);
        circuito2.setPhoto(R.drawable.circuito);
        circuito2.setTitleknowmore("Ver solución..");
        circuito2.setTypeElement(new Type(Type.TYPE_CIRCUIT,"circuito2",Type.COLOR_CIRCUIT));
        elements.add(circuito2);

        ElementModel montaje1= new ElementModel();
        montaje1.setId(id++);
        montaje1.setTitle("¿Cómo se ensambla un dispositivo electrónico?");
        montaje1.setUniquename("montaje 1");
        montaje1.setDescription("¿Quieres conocer el proceso de ensamblado de un dispositivo eletrónico?.");
        montaje1.setKnowmore("https://www.youtube.com/watch?v=8P9gHmsmZiE");
        montaje1.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        montaje1.setPhoto(R.drawable.assembling);
        montaje1.setTitleknowmore("Ver vídeo..");
        montaje1.setTypeElement(new Type(Type.TYPE_ASSEMBLING,"montaje 1",Type.COLOR_ASSEMBLING));
        elements.add(montaje1);

        ElementModel montaje2= new ElementModel();
        montaje2.setId(id++);
        montaje2.setTitle("¿De que están hechos nuestros smartphones?");
        montaje2.setUniquename("montaje 2");
        montaje2.setDescription("¿Cómo de laborioso es ensamblar un dispositivo con miles de componentes como es un smartphone?.");
        montaje2.setKnowmore("https://www.youtube.com/watch?v=gBL-u53sy_o");
        montaje2.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        montaje2.setPhoto(R.drawable.assembling);
        montaje2.setTitleknowmore("Ver vídeo..");
        montaje2.setTypeElement(new Type(Type.TYPE_ASSEMBLING,"montaje 2",Type.COLOR_ASSEMBLING));
        elements.add(montaje2);
        /*ElementModel trigonometria = new ElementModel();
        trigonometria.setId(0);
        trigonometria.setTitle("Galvanometro");
        trigonometria.setUniquename("coseno");
        trigonometria.setKnowmore("https://es.wikipedia.org/wiki/Coseno");
        trigonometria.setDescription("El coseno es una función par y continua con periodo 2Pi , y además una función trascendente. Su nombre se abrevia cos.");
        trigonometria.setPhoto(R.drawable.trigonometria);
        trigonometria.setTypeknowmore(ElementModel.TYPE_KNOWMORE_URL);
        trigonometria.setTitleknowmore("Saber más..");
        trigonometria.setTypeElement(new Type(Type.TYPE_MEDIA,"coseno",Type.COLOR_MEDIA));
        elements.add(trigonometria);

        ElementModel celula = new ElementModel();
        celula.setId(1);
        celula.setTitle("Célula");
        celula.setUniquename("celula");
        celula.setKnowmore("https://www.youtube.com/watch?v=PTrOSGYC6BU");
        celula.setDescription("Unidad anatómica fundamental de todos los organismos vivos, generalmente microscópica, formada por citoplasma, uno o más núcleos y una membrana que la rodea.");
        celula.setPhoto(R.drawable.celula);
        celula.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        celula.setTitleknowmore("Ver Vídeo");
        celula.setTypeElement(new Type(Type.TYPE_INSTRUMENT,"celula",Type.COLOR_INSTRUMENT));
        elements.add(celula);

        ElementModel bisectriz = new ElementModel();
        bisectriz.setId(2);
        bisectriz.setTitle("Bisectriz");
        bisectriz.setUniquename("bisectriz");
        bisectriz.setKnowmore("https://www.youtube.com/watch?v=9mxavzDneR8");
        bisectriz.setDescription("Semirrecta que parte del vértice de un ángulo y lo divide en dos partes iguales.");
        bisectriz.setPhoto(R.drawable.bisectriz);
        bisectriz.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        bisectriz.setTitleknowmore("¿Cómo se calcula?");
        bisectriz.setTypeElement(new Type(Type.TYPE_DRAW,"bisectriz",Type.COLOR_DRAW));
        elements.add(bisectriz);

        ElementModel servo = new ElementModel();
        servo.setId(3);
        servo.setTitle("Servo");
        servo.setUniquename("servo");
        servo.setKnowmore("https://www.youtube.com/watch?v=mk9UkQCeENc");
        servo.setDescription("Dispositivo similar a un motor de corriente continua que tiene la capacidad de ubicarse en cualquier posición dentro de su rango de operación, y mantenerse estable en dicha posición");
        servo.setPhoto(R.drawable.servo);
        servo.setTypeknowmore(ElementModel.TYPE_KNOWMORE_VIDEO);
        servo.setTitleknowmore("Mira que lo conforma");
        servo.setTypeElement(new Type(Type.TYPE_TECHNOLOGY,"servo",Type.COLOR_CIRCUIT));
        elements.add(servo);

        ElementModel peninsula = new ElementModel();
        peninsula.setId(4);
        peninsula.setTitle("Península");
        peninsula.setUniquename("peninsula");
        peninsula.setKnowmore("https://www.google.es/maps/place/Pen%C3%ADnsula+de+Zapata/@22.4092406,-82.1360882,134638m/data=!3m1!1e3!4m5!3m4!1s0x8f2cd22e7ad12367:0x518906bd1fd98fcc!8m2!3d22.3044444!4d-81.3777778");
        peninsula.setDescription("Extensión de tierra que está rodeada de agua por todas partes excepto por una zona o istmo que la une al continente.");
        peninsula.setPhoto(R.drawable.peninsula);
        peninsula.setTypeknowmore(ElementModel.TYPE_KNOWMORE_MAP);
        peninsula.setTitleknowmore("Ejemplo: Península de Zapata (Cuba)");
        peninsula.setTypeElement(new Type(Type.TYPE_GEOGRAPHY,"peninsula",Type.COLOR_GEO));
        elements.add(peninsula);*/
    }
    /**
     * Initializes the UI and creates the detector pipeline.
     */
    ImageView image;
    TextView title;
    TextView description;
    TextView knowmore;
    int type;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.ocr_capture);
        type = getIntent().getIntExtra("TYPE",0);
        initElements();
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay);
        dialog = (LinearLayout) findViewById(R.id.dialoginformation);
        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        knowmore = (TextView) findViewById(R.id.knowmore);
        // read parameters from the intent used to launch the activity.
        boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
        boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        /*Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();*/
        dialog.setVisibility(LinearLayout.GONE);
        if(type==Type.TYPE_INSTRUMENT) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Type.COLOR_INSTRUMENT)));
            getSupportActionBar().setTitle("Instrumentos");
        }
        if(type==Type.TYPE_MEDIA) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Type.COLOR_MEDIA)));
            getSupportActionBar().setTitle("Medios de TX");
        }
        if(type==Type.TYPE_CIRCUIT) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Type.COLOR_CIRCUIT)));
            getSupportActionBar().setTitle("Circuitos");
        }
        if(type==Type.TYPE_ASSEMBLING) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Type.COLOR_ASSEMBLING)));
            getSupportActionBar().setTitle("Montaje y ensamblado");
        }
        /*if(type==Type.TYPE_GEOGRAPHY) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Type.COLOR_GEO)));
            getSupportActionBar().setTitle("Geografía");
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog.setVisibility(LinearLayout.GONE);
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dialog.setVisibility(LinearLayout.GONE);
            showing = false;
        }
    };
    boolean showing=false;
    int previous=-1;
    Handler mHandlerThread = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                if (msg.what >=0){
                    if(previous !=msg.what || !showing) {
                        previous = msg.what;
                        showing = true;

                            dialog.setVisibility(LinearLayout.VISIBLE);
                            setInformationDialog(msg.what);
                            this.postDelayed(runnable,5000);


                        }

            }
        }
    };

    public void setInformationDialog(final int position){
        title.setText(elements.get(position).getTitle());
        description.setText(elements.get(position).getDescription());
        image.setImageResource(elements.get(position).getPhoto());
        knowmore.setText(elements.get(position).getTitleknowmore());
        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(elements.get(position).getKnowmore()));
                startActivity(i);
            }
        });

    }
    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated processor instance
        // is set to receive the text recognition results and display graphics for each text block
        // on screen.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrDetectorProcessor(mGraphicOverlay,mHandlerThread,elements,type));

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        mCameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                .build();

    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();


    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // Check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * onTap is called to capture the first TextBlock under the tap location and return it to
     * the Initializing Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private boolean onTap(float rawX, float rawY) {
        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {
                Intent data = new Intent();
                data.putExtra(TextBlockObject, text.getValue());
                setResult(CommonStatusCodes.SUCCESS, data);
                finish();
            }
            else {
                Log.d(TAG, "text data is null");
            }
        }
        else {
            Log.d(TAG,"no text detected");
        }
        return text != null;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }
}
