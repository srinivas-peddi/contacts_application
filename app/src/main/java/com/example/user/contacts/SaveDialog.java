package com.example.user.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class SaveDialog extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dialog);
        TextView numberView= findViewById(R.id.number_save_dialog);
        numberView.setText(getIntent().getExtras().getString("contactNumber"));
    }

    public void saveNumber(View view)
    {
        Intent intent1 = new Intent(this,AddContact.class);
        intent1.putExtra("contactobj",new ContactPOJO("",getIntent().getExtras().getString("contactNumber"),"",0,null));
        intent1.putExtra("position",-1);
        startActivity(intent1);
        finish();
    }

    public void discardNumber(View view)
    {
        finish();
    }
}
