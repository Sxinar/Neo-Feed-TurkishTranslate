package com.google.android.libraries.gsa.d.a;

import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.view.WindowManager.LayoutParams;

import com.google.android.libraries.i.LauncherOverlayInterfaceBinder;
import com.google.android.libraries.i.d;

final class OverlayControllerBinder extends LauncherOverlayInterfaceBinder implements Runnable {

    int mOptions = 0;
    final String mPackageName;
    private final OverlaysController overlaysController;
    final int mCallerUid;
    final int mServerVersion;
    final int mClientVersion;
    BaseCallback baseCallback = new BaseCallback();
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper(), this.baseCallback);
    boolean mLastAttachWasLandscape;

    public OverlayControllerBinder(OverlaysController overlaysControllerVar, int callerUid, String packageName, int serverVersion, int clientVersion) {
        this.overlaysController = overlaysControllerVar;
        this.mCallerUid = callerUid;
        this.mPackageName = packageName;
        this.mServerVersion = serverVersion;
        this.mClientVersion = clientVersion;
    }

    private void checkCallerId() {
        if (Binder.getCallingUid() != this.mCallerUid) {
            throw new RuntimeException("Invalid client");
        }
    }

    public synchronized void cnK() {
        checkCallerId();
        Message.obtain(this.mainThreadHandler, 3).sendToTarget();
    }

    public synchronized void aL(float f) {
        checkCallerId();
        Message.obtain(this.mainThreadHandler, 4, f).sendToTarget();
    }

    public synchronized void cnL() {
        checkCallerId();
        Message.obtain(this.mainThreadHandler, 5).sendToTarget();
    }

    public synchronized void a(LayoutParams layoutParams, d dVar, int clientOptions) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("layout_params", layoutParams);
        bundle.putInt("client_options", clientOptions);
        a(bundle, dVar);
    }

    public synchronized void a(Bundle bundle, d dVar) {
        checkCallerId();
        this.overlaysController.handler.removeCallbacks(this);
        Configuration configuration = bundle.getParcelable("configuration");
        this.mLastAttachWasLandscape = configuration != null && configuration.orientation == 2;
        BL(bundle.getInt("client_options", 7));
        Message.obtain(this.mainThreadHandler, 0, 1, 0, Pair.create(bundle, dVar)).sendToTarget();
    }

    public synchronized void od(boolean z) {
        checkCallerId();
        Message.obtain(this.mainThreadHandler, 0, 0, 0).sendToTarget();
        this.overlaysController.handler.postDelayed(this, z ? 5000 : 0);
    }

    public synchronized void BJ(int i) {
        checkCallerId();
        this.mainThreadHandler.removeMessages(1);
        if ((i & 2) == 0) {
            this.mainThreadHandler.sendMessageDelayed(Message.obtain(this.mainThreadHandler, 1, i), 100);
        } else {
            Message.obtain(this.mainThreadHandler, 1, i).sendToTarget();
        }
    }

    public synchronized void onPause() {
        BJ(0);
    }

    private synchronized void BL(int i) {
        synchronized (this) {
            int i2 = i & 15;
            if ((i2 & 1) != 0) {
                i2 = 1;
            }
            if (this.mOptions != i2) {
                BaseCallback baseCallbackVar;
                this.mainThreadHandler.removeCallbacksAndMessages(null);
                Message.obtain(this.mainThreadHandler, 0, 0, 0).sendToTarget();
                of(true);
                this.mOptions = i2;
                if (this.mOptions == 1) {
                    baseCallbackVar = new MinusOneOverlayCallback(this.overlaysController, this);
                } else {
                    baseCallbackVar = new BaseCallback();
                }
                this.baseCallback = baseCallbackVar;
                this.mainThreadHandler = new Handler(Looper.getMainLooper(), this.baseCallback);
            }
        }
    }

    public synchronized void onResume() {
        BJ(3);
    }

    public synchronized void fI(int i) {
        checkCallerId();
        this.mainThreadHandler.removeMessages(6);
        Message.obtain(this.mainThreadHandler, 6, 0, i).sendToTarget();
    }

    public synchronized void BK(int i) {
        checkCallerId();
        this.mainThreadHandler.removeMessages(6);
        Message.obtain(this.mainThreadHandler, 6, 1, i).sendToTarget();
    }

    public final synchronized boolean a(byte[] bArr, Bundle bundle) {
        Message.obtain(this.mainThreadHandler, 8, new ByteBundleHolder(bArr, bundle)).sendToTarget();
        return true;
    }

    public synchronized void oe(boolean z) {
        int i = 0;
        synchronized (this) {
            checkCallerId();
            this.mainThreadHandler.removeMessages(7);
            Handler handler = this.mainThreadHandler;
            if (z) {
                i = 1;
            }
            Message.obtain(handler, 7, i, 0).sendToTarget();
        }
    }

    public String HB() {
        return this.overlaysController.HA().HB();
    }

    public boolean HC() {
        return this.overlaysController.HA().HC();
    }

    public void run() {
        destroy();
    }

    final void destroy() {
        synchronized (this.overlaysController) {
            this.overlaysController.handler.removeCallbacks(this);
            of(false);
        }
    }

    private synchronized void of(boolean z) {
        int i = 0;
        synchronized (this) {
            Handler handler = this.mainThreadHandler;
            if (z) {
                i = 1;
            }
            Message.obtain(handler, 2, i, 0).sendToTarget();
        }
    }

    final void a(d dVar, int i) {
        if (dVar != null) {
            try {
                dVar.BI(this.overlaysController.Hx() | i);
            } catch (Throwable ignored) {

            }
        }
    }
}
