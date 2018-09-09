package com.example.lab.android.nuc.crizyandroid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {


    private ViewPager mViewPager;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private Context con = this;
    private int stopthread = 0;

    //记录上一次点击的位置
    private int oldPosition = 0;

    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d
    };

    //存放图片的标题
    private String[] titls = new String[]{
            "中北大学的图书馆一角,下雪了！",
            "中北打学17级学生的第一节军体理论课",
            "中北大学的17实践部团土集合",
            "中北大学安卓实验室比赛迫在眉睫"
    };

    private TextView title;

    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;


    private Button settingsButton;
    private Button lightButton;
    //定义飞机飞行的速度
    private int speed = 10;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉窗口标题
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        //全屏显示
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);xx
        setContentView(R.layout.activity_main);
        //获取布局文件中的LinearLayout容器
//        LinearLayout root = (LinearLayout) findViewById(R.id.root);
//        plane();
        lightButton = (Button) findViewById(R.id.changeButton);
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LightActivity.class);
                startActivity(intent);
            }
        });


        settingsButton = (Button) findViewById(R.id.button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.vp);

        //显示图片

        images = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }

        //显示的小点

        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));
        dots.add(findViewById(R.id.dot_3));

        title = (TextView) findViewById(R.id.title);
        title.setText(titls[0]);

        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                title.setText(titls[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

                oldPosition = position;

                currentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                stopthread = 1;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopthread = 1;
                        break;
                    case MotionEvent.ACTION_UP:
                        stopthread = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopthread = 1;
                        break;
                }
                return false;
            }});
    }

    /**
     * 自定义Adapter
     * @author wanghao
     */
    private class ViewPagerAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView v = images.get(position);
            final int num = position;
            v.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    Toast.makeText(con, "this is" + num,500).show();
                }
            });
            container.addView(images.get(position));
            return v;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /*
    利用线程池执行图片轮播
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }


    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            if(stopthread==0)
            {
                currentItem = (currentItem + 1) % imageIds.length;
                mHandler.sendEmptyMessage(0);
            }
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPager.setCurrentItem(currentItem);

        };
    };
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
}
