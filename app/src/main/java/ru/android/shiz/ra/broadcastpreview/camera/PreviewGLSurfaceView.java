package ru.android.shiz.ra.broadcastpreview.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;
import android.util.Size;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kassava on 19.09.16.
 */
public class PreviewGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    public static String LOG_TAG = PreviewGLSurfaceView.class.getSimpleName();

    private int mTextureID;
    private SurfaceTexture mSurfaceTexture;
    private DirectDrawer mDirectDrawer;
    private SurfaceTextureListener mSurfaceTextureListener;
    private boolean mIsFirstFrame = true;
    private Size mDefaultPreviewSize;

    public PreviewGLSurfaceView(Context context, Size defaultSize) {
        super(context);
        mDefaultPreviewSize = defaultSize;
        mTextureID = createTextureID();
        mSurfaceTexture = new SurfaceTexture(mTextureID);
        setEGLContextClientVersion(2);
        setRenderer(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(LOG_TAG, "onSurfaceCreated");

        mIsFirstFrame = true;
//        mTextureID = createTextureID();
//        mSurfaceTexture = new SurfaceTexture(mTextureID);
        mSurfaceTexture.setDefaultBufferSize(mDefaultPreviewSize.getWidth(), mDefaultPreviewSize.getHeight());
        mDirectDrawer = new DirectDrawer(mTextureID);
        mSurfaceTexture.setOnFrameAvailableListener(this);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onDrawFrame(GL10 gl) {
        if (mIsFirstFrame) {
            mIsFirstFrame = false;
            mSurfaceTextureListener.onSurfaceTextureAvailable(mSurfaceTexture);
        }
        //Infact, GLSurfaceView will create Context, Display, Surface before onDrawFrame begin.
        mSurfaceTexture.updateTexImage();

        float[] mtx = new float[16];
        mSurfaceTexture.getTransformMatrix(mtx);

        //GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mDirectDrawer.draw(mtx);
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurfaceTextureListener(SurfaceTextureListener surfaceTextureListener) {
        mSurfaceTextureListener = surfaceTextureListener;
    }

    public static interface SurfaceTextureListener {
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture);
    }
}
