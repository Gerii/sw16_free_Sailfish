package com.example.andrea.littewhale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andrea.utils.InputFilterDouble;
import com.example.andrea.utils.InputFilterInt;

public class EnterCoordinates extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enter_coordinates);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
        if (button != null)
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                    if (decimalLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.INVISIBLE);
                    }
                    setViewDecimalToTime(false, 0, 0);
                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                    if (timeLayout != null) {
                        timeLayout.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            });


        RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);
        if (button2 != null)
            button2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                    setViewTimeToDecimal(false, null);

                    if (decimalLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.VISIBLE);
                    }
                    if (timeLayout != null) {
                        timeLayout.setVisibility(LinearLayout.INVISIBLE);
                    }
                }
            });


        Button startNavigationButton = (Button) findViewById(R.id.buttonStartNavigating);
        if (startNavigationButton != null)
            startNavigationButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    goToNavigation();
                }
            });

        Button useExistingLocationBtn = (Button) findViewById(R.id.useExistingLocation);
        if (useExistingLocationBtn != null) {
            useExistingLocationBtn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(EnterCoordinates.this, EditLocations.class);
                    EnterCoordinates.this.startActivityForResult(myIntent, 1);
                }
            });
        }


        EditText editTextSecondLatitude = (EditText) findViewById(R.id.editTextSecondLatitude);
        EditText editTextSecondLongitude = (EditText) findViewById(R.id.editTextSecondLongitude);

        EditText editTextMinuteLatitude = (EditText) findViewById(R.id.editTextMinuteLatitude);
        EditText editTextMinuteLongitude = (EditText) findViewById(R.id.editTextMinuteLongitude);

        EditText editTextDegreeTimeLatitude = (EditText) findViewById(R.id.editTextDegreeTimeLatitude);
        EditText editTextDegreeTimeLongitude = (EditText) findViewById(R.id.editTextDegreeTimeLongitude);

        EditText editTextDegreeDecimalLatitude = (EditText) findViewById(R.id.editTextDegreeDecimalLatitude);
        EditText editTextDegreeDecimalLongitude = (EditText) findViewById(R.id.editTextDegreeDecimalLongitude);

        editTextSecondLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextSecondLatitude)});
        editTextSecondLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextSecondLongitude)});
        editTextMinuteLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextMinuteLatitude)});
        editTextMinuteLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextMinuteLongitude)});

        editTextDegreeTimeLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 90, editTextDegreeTimeLatitude)});
        editTextDegreeTimeLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 180, editTextDegreeTimeLongitude)});

        editTextDegreeDecimalLatitude.setFilters(new InputFilter[]{new InputFilterDouble(0, 90, editTextDegreeDecimalLatitude)});
        editTextDegreeDecimalLongitude.setFilters(new InputFilter[]{new InputFilterDouble(0, 180, editTextDegreeDecimalLongitude)});

        View.OnKeyListener enterListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    goToNavigation();
                    return true;
                } else {
                    return false;
                }
            }
        };
        editTextSecondLongitude.setOnKeyListener(enterListener);
        editTextDegreeDecimalLongitude.setOnKeyListener(enterListener);
    }

    private void goToNavigation() {
        Intent myIntent = new Intent(EnterCoordinates.this, NavigationActivity.class);
        double targetLatitude = 0;
        double targetLongitude = 0;

        RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
        RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);

        String[] cardinalDirection = new String[2];

        if (button != null && button.isChecked()) {
            double[] decimal = readTimeFormat();
            targetLatitude = decimal[0];
            targetLongitude = decimal[1];

            cardinalDirection[0] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).getSelectedItem().toString();
            cardinalDirection[1] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).getSelectedItem().toString();

        } else if (button2 != null && button2.isChecked()) {
            String latitude = ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).getText().toString();
            String longitude = ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).getText().toString();


            targetLatitude = Double.parseDouble(latitude);
            targetLongitude = Double.parseDouble(longitude);

            cardinalDirection[0] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).getSelectedItem().toString();
            cardinalDirection[1] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).getSelectedItem().toString();
        }

        if (cardinalDirection[0].equals("S")) {
            targetLatitude *= -1;
        }
        if(cardinalDirection[1].equals("W")) {
            targetLongitude *= -1;
        }


        double[] target = new double[2];

        target[0] = targetLatitude;
        target[1] = targetLongitude;

        myIntent.putExtra("TargetCoords", target);
        myIntent.putExtra("CardinalDirection", cardinalDirection);
        myIntent.putExtra("TargetCoords", target);
        EnterCoordinates.this.startActivity(myIntent);
    }

    private void goToInfo() {
        Intent myIntent = new Intent(EnterCoordinates.this, AboutActivity.class);
        EnterCoordinates.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entercoordinates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            goToInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w("ON ACTIVITY RESULT", "Enter coordinates");

        if (requestCode == 1) {

            if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                if (data == null) {
                    Log.e("ERROR", "data null");

                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Log.w("ON ACTIVITY RESULT", "set lon and lat");
                    Double lon = extras.getDouble("LocationLongitude");
                    Double lat = extras.getDouble("LocationLatitude");
                    String name = extras.getString("LocationName");

                    RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
                    RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);

                    if (button != null && button.isChecked()) {
                        setViewDecimalToTime(true, lon, lat);

                    } else if (button2 != null && button2.isChecked()) {
                        double[] coords = {lat, lon};
                        setViewTimeToDecimal(true, coords);
                    } else {
                        Log.e("ERROR", "WTF?!?!?!");
                    }

                    TextView locationName = (TextView) findViewById(R.id.cityName);
                    if (locationName != null) {
                        locationName.setText(name);
                    }


                } else {
                    Log.e("ERROR", "EXTRAS NULL");


                }
            } else {
                Log.e("ERROR", "Result code not okay");
                Log.e("ERROR", Integer.toString(resultCode));

            }
        }

    }

    private int convertToInt(String numberString) {

        try {
            return Integer.parseInt(numberString);
        } catch (Exception e) {
            return 0;
        }
    }

    private void setViewTimeToDecimal(boolean parameters, double[] coords) {
        double[] decimal = null;

        int longitudeSelectionIndex;
        int latitudeSelectionIndex;

        if(!parameters) {
            decimal = readTimeFormat();
            longitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).getSelectedItemPosition();
            latitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).getSelectedItemPosition();
        } else {
            decimal = coords;
            longitudeSelectionIndex = 0;
            latitudeSelectionIndex = 0;

            if (decimal[0] < 0) {
                decimal[0] *= -1;
                latitudeSelectionIndex = 1;
            }

            if (decimal[1] < 0) {
                decimal[1] *= -1;
                longitudeSelectionIndex = 1;
            }
        }

        ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).setText(Double.toString(decimal[0]));
        ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).setText(Double.toString(decimal[1]));
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).setSelection(longitudeSelectionIndex);
    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double) minute / 60) + ((double) second / 3600);
    }

    private double[] readTimeFormat() {
        double[] decimal = new double[2];
        int degreesLatitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString());
        int minutesLatitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());

        int degreesLongitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLongitude)).getText().toString());
        int secondsLongitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLongitude)).getText().toString());

        decimal[0] = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        decimal[1] = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);

        return decimal;
    }

    private void setViewDecimalToTime(boolean parameters, double lon, double lat) {
        double longitude = 0;
        double latitude = 0;

        if (!parameters) {
            try {
                longitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).getText().toString());
                latitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).getText().toString());
            } catch (Exception e) {

            }
        } else {
            longitude = Math.abs(lon);
            latitude = Math.abs(lat);
        }

        int[] longitudeArray = decimalToTimeConversion(longitude);
        int[] latitudeArray = decimalToTimeConversion(latitude);

        ((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).setText(Integer.toString(latitudeArray[0]));
        ((TextView) findViewById(R.id.editTextMinuteLatitude)).setText(Integer.toString(latitudeArray[1]));
        ((TextView) findViewById(R.id.editTextSecondLatitude)).setText(Integer.toString(latitudeArray[2]));

        ((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).setText(Integer.toString(longitudeArray[0]));
        ((TextView) findViewById(R.id.editTextMinuteLongitude)).setText(Integer.toString(longitudeArray[1]));
        ((TextView) findViewById(R.id.editTextSecondLongitude)).setText(Integer.toString(longitudeArray[2]));

        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).getSelectedItemPosition();

        if (parameters) {
            if (lon < 0) {
                longitudeSelectionIndex = 1;
            } else {
                longitudeSelectionIndex = 0;
            }
            if (lat < 0) {
                latitudeSelectionIndex = 1;
            } else {
                latitudeSelectionIndex = 0;
            }
        }

        ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).setSelection(longitudeSelectionIndex);


    }

    private int[] decimalToTimeConversion(double degree) {
        int[] result = new int[3];
        result[0] = (int) Math.floor(degree);
        degree -= result[0];
        degree *= 60;
        result[1] = (int) Math.floor(degree);
        degree -= result[1];
        degree *= 60;
        result[2] = (int) Math.floor(degree);

        return result;
    }

}
