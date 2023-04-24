package com.kosec.hersafety;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{
    private static int countPowerOff = 0;

    public MyReceiver ()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
            countPowerOff++;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
        }
        else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
            if (countPowerOff > 2)
            {
                countPowerOff=0;
                Toast.makeText(context, "MAIN ACTIVITY IS BEING CALLED ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        }
    }
}
