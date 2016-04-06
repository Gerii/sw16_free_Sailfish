package com.example.andrea.littewhale.uitest;

import android.test.ActivityInstrumentationTestCase2;

import com.example.andrea.littewhale.EditLocations;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

/**
 * Created by clemens on 05.04.16.
 */
public class EditLocationsTest extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public EditLocationsTest(){
        super(EditLocations.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());
    }

    public void tearDown() throws Exception {

    }

    public void testPlus(){
        mySolo.clickOnButton("btnAdd");
    }
}