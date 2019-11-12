package com.example.edgedetection2;



import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    //Data
    protected CameraBridgeViewBase mCamera;

    //Create load callback
    protected LoaderCallbackInterface mCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            //If not success, call base method
            if (status != LoaderCallbackInterface.SUCCESS) super.onManagerConnected(status);
            else {
                //Enable camera if connected to library
                if (mCamera != null) mCamera.enableView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Super
        super.onCreate(savedInstanceState);

        //Set layout
        setContentView(R.layout.activity_main);

        //Get camera
        View camera = findViewById(R.id.OpenCVCamera);
        if (camera != null && camera instanceof CameraBridgeViewBase) {
            //Configure camera
            mCamera = (CameraBridgeViewBase)camera;
            mCamera.setCvCameraViewListener(this);

            requestPermissions(new String[] { Manifest.permission.CAMERA }, 1);
        }
    }

    @Override
    protected void onResume() {
        //Super
        super.onResume();

        //Try to init
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mCallback);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //Do nothing
    }

    @Override
    public void onCameraViewStopped() {
        //Do nothing
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Get edge from the image
        Mat result = new Mat();
        Imgproc.Canny(inputFrame.rgba(), result, 70, 100);

        //Return result
        return result;
    }

    @Override
    protected void onPause() {
        //Disable camera
        super.onPause();
        if (mCamera != null) mCamera.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onPause();
        if (mCamera != null) mCamera.disableView();
    }
}