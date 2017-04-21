package com.tjstudy.bledemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.tjstudy.bledemo.base.BaseActivity;
import com.tjstudy.bledemo.base.onPermissionCallbackListener;

import java.util.List;

public class MainActivity extends BaseActivity {

    private BluetoothManager mBleManager;
    private ScanCallback mBleCallBack;
    Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;//扫描的时间
    private BluetoothAdapter mBleAdapter;
    private BluetoothLeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        initBLE();

        findViewById(R.id.btn_startScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启扫描
                scanDevice(true);
            }
        });
    }

    private void initBLE() {
        mBleManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBleAdapter = mBleManager.getAdapter();
        scanner = mBleAdapter.getBluetoothLeScanner();
        //搜索到一个设备 就返回一个
        mBleCallBack = new ScanCallback() {
            //搜索到一个设备 就返回一个
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.e("MainActivity", "device address = " + result.getDevice().getAddress());
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e("MainActivity", "errorCode:" + errorCode);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.e("MainActivity", "onBatchScanResults");
            }
        };
    }


    /**
     * 是否开启scan device
     *
     * @param enable
     */
    private void scanDevice(boolean enable) {
        if (enable) {
            Log.e("MainActivity", "在扫描");
            //10s之后 自动关闭扫描
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("MainActivity", "关闭扫描");
                    scanner.stopScan(mBleCallBack);
                }
            }, SCAN_PERIOD);
            scanner.stopScan(mBleCallBack);//网上博客有说 先关闭在启动成功率会高一些
            scanner.startScan(mBleCallBack);
        } else {
            Log.e("MainActivity", "关闭扫描");
            scanner.stopScan(mBleCallBack);
        }
    }

    /**
     * 申请动态权限
     */
    private void initPermission() {
        onRequestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                new onPermissionCallbackListener() {
                    @Override
                    public void onGranted() {
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissions) {
                    }
                });
    }
}
