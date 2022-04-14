package com.example.androidtest3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Button btnShape, btnColor, btnUndo, btnRedo;

    private Bitmap baseBitmap;
    private ImageView iv;
    private Canvas canvas;
    private Paint paint;
    private float baseRadio = 5;
    private String baseColor = "#000000";
    private float radio;
    float prePressure = 0;

    private Vector<Vector<Segment>> vec;
    private int cnt = 0;

    private TextView nowColorTv_1, nowShapeTv_1, nowPressureTv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShape = findViewById(R.id.btn_shape);
        btnColor = findViewById(R.id.btn_color);
        btnUndo = findViewById(R.id.btn_undo);
        btnRedo = findViewById(R.id.btn_redo);

        OnClick onClick = new OnClick();
        btnShape.setOnClickListener(onClick);
        btnColor.setOnClickListener(onClick);
        btnUndo.setOnClickListener(onClick);
        btnRedo.setOnClickListener(onClick);

        nowColorTv_1 = findViewById(R.id.nowColorTv_1);
        nowShapeTv_1 = findViewById(R.id.nowShapeTv_1);
        nowPressureTv_1 = findViewById(R.id.nowPressureTv_1);

        iv = findViewById(R.id.iv_1);
        paint = new Paint();
        radio = baseRadio;
        paint.setStrokeWidth(radio);
        iv.setOnTouchListener(touch);


        vec = new Vector<>();
    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
        float startX;
        float startY;
        float mxRadio = baseRadio * (float) 1.5;
        float mnRadio = baseRadio * (float) 0.5;
        float changeRadio = baseRadio * (float) 0.03;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            mxRadio = baseRadio * (float) 1.5;
            mnRadio = baseRadio * (float) 0.5;
            changeRadio = baseRadio * (float) 0.03;

            paint.setColor(Color.parseColor(baseColor));
            nowPressureTv_1.setText("当前压力 ：" + prePressure);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    paint.setStrokeWidth(radio);
                    if (baseBitmap == null) {
                        baseBitmap = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        canvas = new Canvas(baseBitmap);
                        canvas.drawColor(Color.WHITE);
                    }
                    startX = event.getX();
                    startY = event.getY();
                    Vector<Segment> tmp = new Vector<>();
                    vec.add(tmp);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float stopX = event.getX();
                    float stopY = event.getY();

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            float nowPressure = event.getPressure();
                            if (nowPressure > prePressure) radio += changeRadio;
                            else if (nowPressure < prePressure) radio -= changeRadio;
                            prePressure = nowPressure;

//                            nowPressureTv_1.setText("当前压力 ：" + nowPressure);

                            radio = Math.min(radio, mxRadio);
                            radio = Math.max(radio, mnRadio);

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();

                    paint.setStrokeWidth(radio);
                    canvas.drawLine(startX, startY, stopX, stopY, paint);

                    Segment tmpSeg = new Segment(startX, startY, stopX, stopY, baseColor, baseRadio);
//                    vec[cnt].add(tmpSeg);

                    Vector tmpVec = vec.get(cnt);
                    tmpVec.add(tmpSeg);

                    startX = event.getX();
                    startY = event.getY();
                    iv.setImageBitmap(baseBitmap);
                    break;
                case MotionEvent.ACTION_UP:
                    radio = baseRadio;
                    cnt++;
                    break;
                default:
                    break;
            }

            return true;
        }
    };

    protected void resumeCanvas() {
        if (baseBitmap != null) {
            baseBitmap = Bitmap.createBitmap(iv.getWidth(),
                    iv.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmap);
            canvas.drawColor(Color.WHITE);
            iv.setImageBitmap(baseBitmap);
//            Toast.makeText(MainActivity.this, "清除画板成功", Toast.LENGTH_LONG).show();
        }
    }

    protected void paintCanvas() {
        resumeCanvas();
        String s = "in paint canvas";
        Log.d(s, "in paint canvas");
        for (int i = 0; i < cnt; ++i) {
            Vector<Segment> tmpVec = vec.get(i);
            int siz = tmpVec.size();
            for (int j = 0; j < siz; ++j) {
                Segment tmpSeg = tmpVec.get(j);
                paint.setStrokeWidth(tmpSeg.segRadio);
                paint.setColor(Color.parseColor(tmpSeg.segColor));
                canvas.drawLine(tmpSeg.segStartX, tmpSeg.segStartY, tmpSeg.segEndX, tmpSeg.segEndY, paint);
            }
        }
        iv.setImageBitmap(baseBitmap);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_shape:
                    AlertDialog.Builder builderShape = new AlertDialog.Builder(MainActivity.this);
                    View viewShape = LayoutInflater.from(MainActivity.this).inflate(R.layout.shape_dialog, null);
                    Button btnMx = viewShape.findViewById(R.id.btn_1);
                    Button btnMed = viewShape.findViewById(R.id.btn_2);
                    Button btnMn = viewShape.findViewById(R.id.btn_3);
                    EditText etShape = viewShape.findViewById(R.id.shapeDiaEv_1);
                    AlertDialog dailogShape = builderShape.setTitle("请选择线形").setView(viewShape).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (etShape.getText().equals("") || etShape.getText().length() == 0)
                                return;
                            String tmpShape = "" + etShape.getText();
                            float tmpRadio = Float.parseFloat(tmpShape);
                            baseRadio = tmpRadio;
                            radio = baseRadio;
                            nowShapeTv_1.setText("当前线形基础半径：" + baseRadio);
                            Toast.makeText(MainActivity.this, "确定选择 : " + etShape.getText(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "取消选择", Toast.LENGTH_LONG).show();
                        }
                    }).setCancelable(false).create();
                    btnMx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baseRadio = 15;
                            radio = baseRadio;
                            nowShapeTv_1.setText("当前线形基础半径：15");
                            dailogShape.dismiss();
                        }
                    });
                    btnMed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baseRadio = 10;
                            radio = baseRadio;
                            nowShapeTv_1.setText("当前线形基础半径：10");
                            dailogShape.dismiss();
                        }
                    });
                    btnMn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baseRadio = 5;
                            radio = baseRadio;
                            nowShapeTv_1.setText("当前线形基础半径：5");
                            dailogShape.dismiss();
                        }
                    });
                    dailogShape.show();
                    break;
                case R.id.btn_color:
                    AlertDialog.Builder builderColor = new AlertDialog.Builder(MainActivity.this);
                    View viewColor = LayoutInflater.from(MainActivity.this).inflate(R.layout.color_dialog, null);
                    Button btn_black = viewColor.findViewById(R.id.btn_black);
                    Button btn_red = viewColor.findViewById(R.id.btn_red);
                    Button btn_yellow = viewColor.findViewById(R.id.btn_yellow);
                    Button btn_blue = viewColor.findViewById(R.id.btn_blue);
                    Button btn_green = viewColor.findViewById(R.id.btn_green);
                    Button btn_orange = viewColor.findViewById(R.id.btn_orange);
                    Button btn_grey = viewColor.findViewById(R.id.btn_grey);

                    EditText etColor = viewColor.findViewById(R.id.colorDiaEv_1);
                    AlertDialog dialogColor = builderColor.setTitle("请选择颜色").setView(viewColor).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (etColor.getText().equals("") || etColor.getText().length() == 0)
                                return;
                            String tmpColor = "#" + etColor.getText();
                            baseColor = tmpColor;
                            nowColorTv_1.setText("当前颜色：" + tmpColor);
                            Toast.makeText(MainActivity.this, "确定选择 : #" + etColor.getText(), Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "取消选择", Toast.LENGTH_LONG).show();
                        }
                    }).create();
                    btn_black.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#000000";
                            nowColorTv_1.setText("当前颜色：#000000");
                            dialogColor.dismiss();
                        }
                    });
                    btn_red.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#880d1e";
                            nowColorTv_1.setText("当前颜色：#880d1e");
                            dialogColor.dismiss();
                        }
                    });
                    btn_yellow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#fcbf49";
                            nowColorTv_1.setText("当前颜色：#fcbf49");
                            dialogColor.dismiss();
                        }
                    });
                    btn_blue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#006ba6";
                            nowColorTv_1.setText("当前颜色：#006ba6");
                            dialogColor.dismiss();
                        }
                    });
                    btn_green.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#134611";
                            nowColorTv_1.setText("当前颜色：#134611");
                            dialogColor.dismiss();
                        }
                    });
                    btn_orange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#ff9505";
                            nowColorTv_1.setText("当前颜色：#ff9505");
                            dialogColor.dismiss();
                        }
                    });
                    btn_grey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            baseColor = "#454955";
                            nowColorTv_1.setText("当前颜色：#454955");
                            dialogColor.dismiss();
                        }
                    });
                    dialogColor.show();
                    break;
                case R.id.btn_undo:
//                    vec.remove(cnt);
                    if (cnt == 0) return;
                    vec.remove(cnt - 1);
                    cnt--;
                    paintCanvas();
                    break;
                case R.id.btn_redo:
                    resumeCanvas();
                    vec.clear();
                    cnt = 0;
                    prePressure = 0;
                    nowPressureTv_1.setText("当前压力 ：" + prePressure);
                    break;
            }
        }
    }
}