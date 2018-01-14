package by.overpass.hunger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import by.overpass.hunger.datamanipulation.CartController;

public class FinalInfoActivity extends AppCompatActivity {

    private ImageView successTick;
    private Button btnGoToStartMenu;
    private TextView confirmationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_info);

        successTick = (ImageView) findViewById(R.id.tickImageView);
        btnGoToStartMenu = (Button) findViewById(R.id.btnGoToStart);
        confirmationTextView = (TextView) findViewById(R.id.successTextView);

        successTick.startAnimation(AnimationUtils.loadAnimation(this, R.anim.tick_animation));
        confirmationTextView.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.order_confirmation_animation));

        CartController.currentCustomerID = -1;
        CartController.currentOrderID = -1;

        btnGoToStartMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(btnGoToStartMenu.getContext(), StartMenuActivity.class);
                startActivity(intent);
            }
        });

    }
}
