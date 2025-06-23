package com.example.foodapp;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView dislayimages;
    private int[] imageIds = {
            R.drawable.boluclac,
            R.drawable.cachimchienmam,
            R.drawable.cadieuhonghap,
            R.drawable.changanuong,
            R.drawable.changarutxuong,
            R.drawable.dauhuchien,
            R.drawable.gachienmam,
            R.drawable.gakhosa,
            R.drawable.thitboxao
    };

    private int currentIndex = 0;
    private Handler handler = new Handler();
    private Runnable imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dislayimages = findViewById(R.id.dislayimages);

        imageSlider = new Runnable() {
            @Override
            public void run() {
                dislayimages.setImageResource(imageIds[currentIndex]);
                currentIndex = (currentIndex + 1) % imageIds.length;
                handler.postDelayed(this, 3000); // 3 giây
            }
        };

        handler.post(imageSlider);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(imageSlider);
    }
}
