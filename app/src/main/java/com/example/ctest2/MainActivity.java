package com.example.ctest2;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CTInboxListener,DisplayUnitListener {
    Button createu,pushpbt,appinbox,getmsg,inappnotif,nativedisp,inboxcount,delinbox;
    CardView c;
    TextView  text1,titlem,msg,txtinbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        CleverTapAPI.setDebugLevel(3);
        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
           // cleverTapDefaultInstance.getInboxMessageCount();
           // cleverTapDefaultInstance.getAllInboxMessages();
        }
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"divyesh","divyesh","divyesh", NotificationManager.IMPORTANCE_MAX,true);
        createu = findViewById(R.id.createuser);
        pushpbt = findViewById(R.id.pushnotification);
        text1 = findViewById(R.id.textv);
        appinbox = findViewById(R.id.appinbox);
        getmsg = findViewById(R.id.getmsg);
        inappnotif = findViewById(R.id.inappnotif);
        nativedisp = findViewById(R.id.nativedisp);
        inboxcount = findViewById(R.id.inboxcount);
        delinbox = findViewById(R.id.delinbox);
        txtinbox = findViewById(R.id.txtinbox);
        c=findViewById(R.id.c1);
        titlem = findViewById(R.id.titlem);
        msg = findViewById(R.id.msg);
        appinbox.setOnClickListener(v->{
           // cleverTapDefaultInstance.showAppInbox();

            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("Promotions");
            tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

            CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
            styleConfig.setFirstTabTitle("Text Tab");
            styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
            styleConfig.setTabBackgroundColor("#FF0000");
            styleConfig.setSelectedTabIndicatorColor("#0000FF");
            styleConfig.setSelectedTabColor("#0000FF");
            styleConfig.setUnselectedTabColor("#FFFFFF");
            styleConfig.setBackButtonColor("#FF0000");
            styleConfig.setNavBarTitleColor("#FF0000");
            styleConfig.setNavBarTitle("MY INBOX");
            styleConfig.setNavBarColor("#FFFFFF");
            styleConfig.setInboxBackgroundColor("#ADD8E6");
            if (cleverTapDefaultInstance != null) {
                cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
            }
        });

        getmsg.setOnClickListener(v -> { clevertapDefaultInstance.pushEvent("biswagetmsg"); });
       pushpbt.setOnClickListener(v -> { clevertapDefaultInstance.pushEvent("biswapushbut"); });
       inappnotif.setOnClickListener(v->{clevertapDefaultInstance.pushEvent("biswaappnotif");});

       nativedisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clevertapDefaultInstance.pushEvent("biswanative");
            }
        });

        inboxcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int ct= cleverTapDefaultInstance.getInboxMessageCount(); // to count total inbox msg
//              Log.d("Count","Count"+cleverTapDefaultInstance.getInboxMessageCount());
               //  text1.setText(ct);
                String ctr= String.valueOf(ct);
                txtinbox.setText("Your inbox message count is "+ctr);

                //Log.d("Count","data: "+cleverTapDefaultInstance.getAllInboxMessages());
            }
        });

        delinbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleverTapDefaultInstance.deleteInboxMessage("1645782481_1645788940"); //add the msg id to be deleted
            }
        });

        createu.setOnClickListener(view -> {
            // each of the below mentioned fields are optional
            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
            profileUpdate.put("Name", "clevertapdivyesh");    // String
            profileUpdate.put("Identity", 1211);      // String or number
            profileUpdate.put("Email", "divyesh@abc.com"); // Email address of the user
            profileUpdate.put("Phone", "+918800991711");   // Phone (with the country code, starting with +)
            profileUpdate.put("Gender", "M");             // Can be either M or F
            profileUpdate.put("DOB", new Date());            // Date of Birth. Set the Date object to the appropriate value first
            // optional fields. controls whether the user will be sent email, push etc.

            profileUpdate.put("MSG-email", true);        // Disable email notifications
            profileUpdate.put("MSG-push", true);          // Enable push notifications
            profileUpdate.put("MSG-sms", true);          // Disable SMS notifications
            profileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
            ArrayList<String> stuff = new ArrayList<String>();
            stuff.add("bag");
            stuff.add("shoes");
            profileUpdate.put("MyStuff", stuff);                        //ArrayList of Strings
            String[] otherStuff = {"Jeans","Perfume"};
            profileUpdate.put("MyStuff", otherStuff);                   //String Array
            clevertapDefaultInstance.onUserLogin(profileUpdate);
            clevertapDefaultInstance.pushEvent("Product viewed");
            text1.setText("User Created");
        });
    }


    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        for (int i = 0; i <units.size() ; i++) {
            CleverTapDisplayUnit unit = units.get(i);
            prepareDisplayView(unit);
        }
    }

    private void prepareDisplayView(CleverTapDisplayUnit unit) {
        for (CleverTapDisplayUnitContent i:unit.getContents()) {
            titlem.setText(i.getTitle());
            msg.setText(i.getMessage());
            //Notification Viewed Event
            CleverTapAPI.getDefaultInstance(this).pushDisplayUnitViewedEventForID(unit.getUnitID());

            //Notification Clicked Event
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CleverTapAPI.getDefaultInstance(getApplicationContext()).pushDisplayUnitClickedEventForID(unit.getUnitID());

                }
            });
        }
    }

    @Override
    public void inboxDidInitialize() {


    }

    @Override
    public void inboxMessagesDidUpdate() {

    }
}