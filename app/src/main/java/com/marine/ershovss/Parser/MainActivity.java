package com.marine.ershovss.Parser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1= (TextView)findViewById(R.id.tv1);

        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            ArrayList<Shortcut> shortcuts = parseXML(parser);

            String text="";

            for(Shortcut shortcut :shortcuts)
            {

                text = "category : "+shortcut.getCategory()+" description : "+shortcut.getDescription()+" shortcut : "+shortcut.getShortcut()+"\n";
            }

            tv1.setText(text);



        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private ArrayList<Shortcut> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Shortcut> shortcuts = null;
        int eventType = parser.getEventType();
        Shortcut shortcut = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;

            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    shortcuts = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name =="category"){
                        shortcut = new Shortcut();
                        shortcut.category=parser.getAttributeValue(null,"category");
                    } else if (shortcut != null){
                        if (name.equals("description")){
                            shortcut.description = parser.nextText();
                        } else if (name.equals("capital")){
                            shortcut.shortcut = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("shortcut") && shortcut != null){
                        shortcuts.add(shortcut);
                    }
            }
            eventType = parser.next();
        }

        return shortcuts;

    }

    
}
