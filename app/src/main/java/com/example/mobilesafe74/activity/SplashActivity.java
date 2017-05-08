package com.example.mobilesafe74.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int UPDATE_VERSION = 100;//需要进行更新版本的状态码
    private static final int ENTER_HOME = 101;//进入主界面的状态码
    private static final int URL_ERROR = 102;//url异常的状态码
    private static final int IO_ERROR = 103;//进入io异常的状态码
    private static final int JSON_ERROR = 104;//进入json解析异常的状态码
    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:

                    enterHome();
                    break;
                case URL_ERROR:

                    Toast.makeText(SplashActivity.this, "URL异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "读取异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "Json解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

            }
        }
    };


    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.mipmap.ic_launcher).
                setTitle("版本更新").
                setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //拿到新应用的下载地址,接着更新
                downloadApk();
            }

            private void downloadApk() {
                //检查sdk卡是否挂载了
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    //设置apk下载的路径
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobliesafe74.apk";
                    //下载的逻辑
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            //下载成功
                            Log.i(TAG, "下载成功");
                            File file = responseInfo.result;
                            installApk(file);

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            //下载失败
                            Log.i(TAG, "下载失败");
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            Log.i(TAG, "下载开始");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            Log.i(TAG, "下载中ing.....");
                        }
                    });
                }

            }
        });
        builder.setNegativeButton("梢后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
                dialog.dismiss();//关闭对话框,其实有没有都是没所谓的
            }
        });
        //点击返回键,代表用户不想更新,程序直接进入主页面
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });

        builder.show();


    }

    /**
     * 安装应用程序的方法
     *
     * @param file
     */
    private void installApk(File file) {
        /*
                        <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" />
                <data android:scheme="file" />
                <data android:mimeType="application/vnd.android.package-archive" />
         */
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//       startActivity(intent);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:  //安装界面的请求码
                enterHome();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化ui
        initUI();

        //初始化数据
        initData();

        //初始化动画效果
        initAnimation();

    }



    /**
     * 初始化动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.setAnimation(alphaAnimation);

    }

    /**
     * 更新数据的方法,包括检查版本号以及显示版本名称
     */
    private void initData() {
        //1.显示应用版本名称
        tv_version_name.setText("版本名称:" + getVersionName());
        //2.检测版本号,看看是否有更新
        //2.1获得本地的版本号
        mLocalVersionCode = getVersionCode();
        //2.2获得服务器的版本号
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false) == true) {
            checkVersion();
        } else {
            mHandler.sendEmptyMessageAtTime(ENTER_HOME, 4000);
        }


    }

    /**
     * 从服务器返回版本等系统
     */
    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                long startTime1 = SystemClock.currentThreadTimeMillis();
                try {
                    //http://192.168.1.235:8080/update74.json
                    //http://192.168.1.235:8080/TestWeb01/update74.json
                 /*   URL url = new URL("http://192.168.1.235:8080/TestWeb01/update74.json");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();
                    if (code == 200) {

                        InputStream in = connection.getInputStream();
                        String content = StreamUtil.streamToString(in);
                        System.out.println("解析成功了喔" + content);


                    }*/

                    //"http://192.168.1.235:8080/update74.json"
                    URL address = new URL(ConstantValue.UPDATEURL);
                    //            OkHttpClient client = new OkHttpClient();

                    OkHttpClient client = new OkHttpClient.Builder().
                            connectTimeout(2, TimeUnit.SECONDS).
                            readTimeout(2, TimeUnit.SECONDS).build();


                    Request request = new Request.Builder().
                            url(address).
                            build();
                    Response response = client.newCall(request).execute();
                    String responceData = response.body().string();


                    Log.i(TAG, responceData);
                    System.out.println("解析成功了!!!" + responceData);
/**
 * {
 versionName:"2.0",
 versionCode:"2",
 versionDes:"2.0版本正式发布,真的酷炫完美,叶哥出品,必属佳品",
 downloadUrl:"http://www.ooxx.com/oxx.apk"
 }
 *
 */

                    JSONObject jsonObject = new JSONObject(responceData);

                    String versionName = jsonObject.getString("versionName");
                    String versionCode = jsonObject.getString("versionCode");
                    mVersionDes = jsonObject.getString("versionDes");
                    mDownloadUrl = jsonObject.getString("downloadUrl");

                    Log.i("xixi", "name:" + versionName);
                    Log.i("xixi", "code:" + versionCode);
                    Log.i("xixi", "des:" + mVersionDes);
                    Log.i("xixi", "url" + mDownloadUrl);


                    /**
                     * 本地版本号对比服务器版本号
                     */
                    if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                        msg.what = UPDATE_VERSION;
                    } else {
                        //没有更新就进入主界面
                        msg.what = ENTER_HOME;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    long endTime = System.currentTimeMillis();
                    long endTime1 = SystemClock.currentThreadTimeMillis();
                  if ((endTime-startTime)<4000){
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                   /* if (endTime1 - startTime1 < 4000) {
                        SystemClock.sleep(4000 - (endTime - startTime));
                    }*/
                    mHandler.sendMessage(msg);//发送消息机制回消息队列里面
                }
            }
        }).start();

    }

    /**
     * 获得版本号的方法
     *
     * @return 非0代表成功, 0代表异常
     */
    private int getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 获得版本名称,如果返回null代表有异常
     *
     * @return
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 更新ui的方法
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

    }
}
