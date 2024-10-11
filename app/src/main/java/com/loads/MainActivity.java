package com.loads;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadDexClass();
            }
        }).start();
    }


    // DexClassLoader (String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent)
    private void loadDexClass() {
        try {
            File OutputDir = getCacheDir();
            FileUtils.extractAssets(getApplicationContext(), "target.jar");

            File desFile = getFileStreamPath("target.jar");
            Log.i("sanbo", "loadDexClass copy success");
            /**
             * 参数1 dexPath：待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限
             * 参数2 optimizedDirectory：解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写（安全性考虑），所以只能放在data/data下。
             * 参数3 libraryPath：指向包含本地库(so)的文件夹路径，可以设为null
             * 参数4 parent：父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
             */
            DexClassLoader classLoader = new DexClassLoader(
                    desFile.getAbsolutePath()
                    , OutputDir.getAbsolutePath()
                    , null
                    , getClassLoader()
            );

            Log.i("sanbo", "loadDexClass classLoader: " + classLoader);

            Class GoTestClass = classLoader.loadClass("com.xxx.GoTest");
            Log.i("sanbo", "loadDexClass GoTestClass: " + GoTestClass);

//            com.xxx.GoTest
//            init(Context ctx, String key, String value)
            Method m = GoTestClass.getDeclaredMethod("init"
                    , Context.class, String.class, String.class);
            Log.i("sanbo", "loadDexClass m: " + m);

            m.invoke(null, getApplicationContext(), "KEY1", "VALUE1");
            Log.i("sanbo", "loadDexClass call succcess. ");
        } catch (Throwable e) {
            Log.e("sanbo", Log.getStackTraceString(e));
        }

    }
}