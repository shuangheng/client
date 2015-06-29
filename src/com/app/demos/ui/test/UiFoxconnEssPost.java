package com.app.demos.ui.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.test.Zsf_Http;
import com.app.demos.util.foxconn_ESS_zsf.AppClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tom on 15-6-21.
 */
public class UiFoxconnEssPost extends Activity implements View.OnClickListener {
    private EditText editText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_foxconn_ess_post_test);

        TAG = "UiFoxconnEssPost";
        editText = (EditText) findViewById(R.id.ui_foxconn_ess_post_edit);
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

        button1.setText("myzsfDormInfoPost");
        button2.setText("userInfoPost");
        button3.setText("empMobilePost");
        button4.setText("userLogPost");
        button5.setText("dailyIssuePost");
        button6.setText("getUserCLossLogPost");
        button7.setText("getRelationshipPost");
        button8.setText("getRMDAwardTypePost");
        button9.setText("getPushedBgPost");
        button10.setText("getNoPassReasonPost");
        button11.setText("getUserShortCutNewPost");
        button12.setText("getRecentRecordPost");
        button13.setText("getQuestionListPost");
        button14.setText("getAutoRespondUrlPost");

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
    }

    @Override
    public void onClick(View v) {
        final String[] strings = editText.getText().toString().split("&");
        switch (v.getId()) {
            case R.id.ui_foxconn_ess_post_btn1:
                ExecutorService service = Executors.newFixedThreadPool(3);

                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run");

                        try {
                            AppClient.myzsfDormInfoPost(strings[0]);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                // 在未来某个时间执行给定的命令
                //service.execute(run);
                service.submit(run);
                // }
                // 关闭启动线程
                service.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
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
                            AppClient.userInfoPost(strings[0]);

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
                            AppClient.empMobilePost(strings[0]);

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
                            AppClient.userLogPost(strings[0]);

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
                            AppClient.dailyIssuePost(strings[0], strings[1], strings[2]);

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
                            AppClient.getUserCLossLogPost(strings[0]);

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
                            AppClient.getRelationshipPost();

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
                            AppClient.getRMDAwardTypePost();

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
                            AppClient.getPushedBgPost(strings[0]);

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
                            AppClient.getNoPassReasonPost(strings[0]);

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
                            AppClient.getUserShortCutNewPost(strings[0], strings[1]);

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
                            AppClient.getRecentRecordPost(strings[0]);

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
                            AppClient.getQuestionListPost();

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
                            AppClient.getAutoRespondUrlPost();

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
    }
}
