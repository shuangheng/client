package com.app.demos.ui.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;
import com.app.demos.list.bitmap_load_list.FileCache;
import com.app.demos.model.DromInfo;
import com.app.demos.sqlite.DromInfoSqlite;
import com.app.demos.ui.test.swipebacktest.DemoActivity;
import com.app.demos.util.SDUtil;
import com.app.demos.util.foxconn_ESS_zsf.AppClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tom on 15-6-21.
 */
public class UiFoxconnEssPost extends SwipeBackActivity implements View.OnClickListener {
    private TextView textView;
    private EditText editText;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;
    private Button button12;
    private Button button13;
    private Button button14;
    private String TAG;
    private String result = "null";
    private Context context = this;

    private DromInfoSqlite dromInfoSqlite;
    private DromInfo dromInfo;
    private Button button15;
    private ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_foxconn_ess_post_test);

        dromInfoSqlite = new DromInfoSqlite(this);

        TAG = "UiFoxconnEssPost";
        imageView = (ImageView)findViewById(R.id.ui_foxconn_ess_post_image);
        textView = (TextView) findViewById(R.id.ui_foxconn_ess_post_text);
        editText = (EditText) findViewById(R.id.ui_foxconn_ess_post_edit);
        button0 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn0);
        button1 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn1);
        button2 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn2);
        button3 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn3);
        button4 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn4);
        button5 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn5);
        button6 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn6);
        button7 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn7);
        button8 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn8);
        button9 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn9);
        button10 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn10);
        button11 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn11);
        button12 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn12);
        button13 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn13);
        button14 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn14);
        button15 = (Button) findViewById(R.id.ui_foxconn_ess_post_btn15);

        button0.setText("UserLoginIn (empno, emppwd)");
        button1.setText("myzsfDormInfoPost (empno)");
        button15.setText("getUserShortCutNew (empno)");
        button2.setText("userInfoPost (empno)");
        button3.setText("empMobilePost (empno) !!");
        button4.setText("userLogPost (empno)");
        button5.setText("dailyIssuePost (empno/start/end)");
        button6.setText("getUserCLossLogPost  (empno)!!");
        button7.setText("getRelationshipPost (null)");
        button8.setText("getRMDAwardTypePost (null)");
        button9.setText("getPushedBgPost (empno)!!");
        button10.setText("getNoPassReasonPost (factory)");
        button11.setText("getUserShortCutNewPost");
        button12.setText("getRecentRecordPost (empno)");
        button13.setText("getQuestionListPost (null)");
        button14.setText("getAutoRespondUrlPost (null)");

        //Button[] buttons = {button1}
        //setButtonClick();
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        button10.setOnClickListener(this);
        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                overlay(UiSearchHistory.class);
                return true;
            }
        });

        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                overlay(DemoActivity.class);
                overridePendingTransition(R.anim.in_from_right, 0);
                return false;
            }
        });

    }

    private void setButtonClick (Button[] buttons) {
        for (Button b : buttons) {
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onTaskComplete(String result) {
        super.onTaskComplete(result);
        textView.setText(result);
        Bitmap bitmap = null;
        FileCache fileCache = new FileCache(context);
        File saveFilePath = fileCache.getFile("1234");
        JSONObject localJSONObject;

        try {
            localJSONObject = new JSONObject(result).getJSONArray("DormInfo").getJSONObject(0);

            String str2 = localJSONObject.getString("EMPNO");
            String name = localJSONObject.getString("EMPNAME");
            String str4 = localJSONObject.getString("DISTRICTNAME");
            String str5 = localJSONObject.getString("DORMDISTRICTNAME");
            String str6 = localJSONObject.getString("BUILDING");
            String str7 = localJSONObject.getString("ROOM");
            String str8 = localJSONObject.getString("BED");
            String str9 = localJSONObject.getString("INDATE");
            String str10 = localJSONObject.getString("MOBILE");

            dromInfo = new DromInfo(str2.substring(1), str2, name, result, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        try {
            localJSONObject = new JSONObject(result).getJSONArray("RecentRecord").getJSONObject(0);

            String str2 = localJSONObject.getString("PHOTOSTR");
            byte[] bytes = str2.getBytes();
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                OutputStream fos = new FileOutputStream(saveFilePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                //回收
                bitmap.recycle();
                fos.flush();
                fos.close();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageResource(R.drawable.s_5);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }*/
        dromInfoSqlite.updateDromInfo(dromInfo);

    }

    @Override
    public void onClick(View v) {
        final String[] strings = editText.getText().toString().split("&");
        switch (v.getId()) {
            case R.id.ui_foxconn_ess_post_btn0:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run");
                        try {
                            result = AppClient.UserLoginIn(strings[0], strings[1]);
                            //result = AppClient.getVerificationCode(strings[0], strings[1]);
                            sendMessage(BaseTask.TEST_FoxconnEss, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ui_foxconn_ess_post_btn1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run");

                        try {
                            result = AppClient.myzsfDormInfoPost(strings[0]);
                            //result = AppClient.compareVerificationCode(strings[0], strings[1]);
                            sendMessage(BaseTask.TEST_FoxconnEss, result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ui_foxconn_ess_post_btn15:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run");
                        try {
                            result = AppClient.getUserShortCutNew(strings[0]);
                            sendMessage(BaseTask.TEST_FoxconnEss, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
///////////////////////
            case R.id.ui_foxconn_ess_post_btn2:
                ExecutorService service1 = Executors.newFixedThreadPool(3);

                Runnable run1 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run1");

                        try {
                            result = AppClient.userInfoPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service1.submit(run1);
                // }
                // 关闭启动线程
                service1.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service1.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn3:
                ExecutorService service12 = Executors.newFixedThreadPool(3);

                Runnable run12 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run12");

                        try {
                            result = AppClient.empMobilePost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service12.submit(run12);
                // }
                // 关闭启动线程
                service12.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service12.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn4:
                ExecutorService service123 = Executors.newFixedThreadPool(3);

                Runnable run123 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run123");

                        try {
                            result = AppClient.userLogPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service123.submit(run123);
                // }
                // 关闭启动线程
                service123.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service123.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
                ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn5:
                ExecutorService service124 = Executors.newFixedThreadPool(3);

                Runnable run124 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.dailyIssuePost(strings[0], strings[1], strings[2]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service124.submit(run124);
                // }
                // 关闭启动线程
                service124.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service124.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.ui_foxconn_ess_post_btn6:
                ExecutorService service1246 = Executors.newFixedThreadPool(3);

                Runnable run1246 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getUserCLossLogPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service1246.submit(run1246);
                // }
                // 关闭启动线程
                service1246.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service1246.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn7:
                ExecutorService service1247 = Executors.newFixedThreadPool(3);

                Runnable run1247 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getRelationshipPost();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service1247.submit(run1247);
                // }
                // 关闭启动线程
                service1247.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service1247.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn8:
                ExecutorService service1248 = Executors.newFixedThreadPool(3);

                Runnable run1248 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getRMDAwardTypePost();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service1248.submit(run1248);
                // }
                // 关闭启动线程
                service1248.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service1248.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn9:
                ExecutorService service1249 = Executors.newFixedThreadPool(3);

                Runnable run1249 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getPushedBgPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service1249.submit(run1249);
                // }
                // 关闭启动线程
                service1249.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service1249.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn10:
                ExecutorService service12491 = Executors.newFixedThreadPool(3);

                Runnable run12491 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getNoPassReasonPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service12491.submit(run12491);
                // }
                // 关闭启动线程
                service12491.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service12491.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn11:
                ExecutorService service11249 = Executors.newFixedThreadPool(3);

                Runnable run11249 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getUserShortCutNewPost(strings[0], strings[1]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service11249.submit(run11249);
                // }
                // 关闭启动线程
                service11249.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service11249.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn12:
                ExecutorService service12492 = Executors.newFixedThreadPool(3);

                Runnable run12492 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getRecentRecordPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service12492.submit(run12492);
                // }
                // 关闭启动线程
                service12492.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service12492.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn13:
                ExecutorService service12493 = Executors.newFixedThreadPool(3);

                Runnable run12493 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getQuestionListPost();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service12493.submit(run12493);
                // }
                // 关闭启动线程
                service12493.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service12493.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            ///////////////////////////////////////////////////////////////
            case R.id.ui_foxconn_ess_post_btn14:
                ExecutorService service12494 = Executors.newFixedThreadPool(3);

                Runnable run12494 = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "HTTP_test");

                        try {
                            result = AppClient.getAutoRespondUrlPost();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service12494.submit(run12494);
                // }
                // 关闭启动线程
                service12494.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service12494.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            }

            textView.setText(result);
    }
}
