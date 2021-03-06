package com.project.nicki.displaystabilizer.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.project.nicki.displaystabilizer.R;
import com.project.nicki.displaystabilizer.contentprovider.DemoDraw;
import com.project.nicki.displaystabilizer.dataprocessor.proCamera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class DemoDrawUI extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "getFrontcam";

/////from getFrontcam
    public static Mat curMat;
    public Mat preMat;
    public Mat nxtMat;
    public MatOfKeyPoint preKeypoints, nxtKeypoints;
    DemoDraw DD;
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;
    private Mat mRgba, mGray;
    public static Handler proCameraHandler, goto_results;
    private HandlerThread mHandlerThread;
    private Runnable mRunnable = new proCamera();
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DD = new DemoDraw(this);
        DD = (DemoDraw) findViewById(R.id.view);
        //DD.setVisibility(DemoDraw.VISIBLE);
        //setContentView(DD);


        //from getFrontcam
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_demo_draw_ui);
        //mOpenCvCameraView.setMaxFrameSize(800, 600);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.demo_draw_camera_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mHandlerThread = new HandlerThread("Deyu");
        mHandlerThread.start();
        //////////////////


    }


    //from getFrontcam
    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (DemoDraw.drawing == true) {
            Log.d(TAG, "onCameraFrame");
            //mRgba = inputFrame.gray();
            curMat = inputFrame.gray();
            //proCameraHandler = new Handler(mHandlerThread.getLooper());
            //proCameraHandler.post(mRunnable);

            //Runnable mproCamera = new proCamera() ;
            //Thread thread = new Thread(mproCamera);
            //thread.start();
            mRunnable.run();
        }
        return proCamera.proMat;
    }
    ///////////


}







