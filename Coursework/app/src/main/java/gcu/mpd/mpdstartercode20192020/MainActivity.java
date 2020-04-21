//Martin Abadzheiv S1518532
package gcu.mpd.mpdstartercode20192020;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView rawDataDisplay2;
    private String result2 = "";
    private EditText textTest;
    private Button startButton2;
    private Button anotherButton;
    private Button yetAnotherButton;
    private String urlSource;
    private String urlSource1 = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String urlSource2 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlSource3 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rawDataDisplay2 = (TextView)findViewById(R.id.rawDataDisplay);
        textTest = (EditText) findViewById(R.id.lengthEditText);
        startButton2 = (Button)findViewById(R.id.startButton);
        startButton2.setOnClickListener(this);
        anotherButton = (Button)findViewById(R.id.button2);
        anotherButton.setOnClickListener(this);
        yetAnotherButton = (Button)findViewById(R.id.button3);
        yetAnotherButton.setOnClickListener(this);
    }

    public LinkedList<Roadworks> parseData(String dataToParse)
    {
        Roadworks roadwork = null;
        LinkedList <Roadworks> list1 = null;
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                if(eventType == XmlPullParser.START_TAG)
                {

                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        list1  = new LinkedList<Roadworks>();
                        roadwork = new Roadworks();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","Item Start Tag found");
                        roadwork = new Roadworks();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        String temp = xpp.nextText();
                        Log.e("MyTag","Title is " + temp);
                        roadwork.setTitle(temp);
                    }
                    else
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            String temp = xpp.nextText();
                            Log.e("MyTag","Description is " + temp);
                            roadwork.setDescription(temp);

                        }
                        else
                            if (xpp.getName().equalsIgnoreCase("link"))
                            {
                                String temp = xpp.nextText();
                                Log.e("MyTag","Link is " + temp);
                                roadwork.setLink(temp);
                            }
                            else
                                if (xpp.getName().equalsIgnoreCase("georss:point"))
                                {
                                    Log.e("MyTag","Skip");
                                }
                                else
                                    if (xpp.getName().equalsIgnoreCase("pubDate"))
                                    {
                                        Log.e("MyTag","Skip");
                                    }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","widget is " + roadwork.toString());
                        list1.add(roadwork);
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = list1.size();
                        Log.e("MyTag","collection size is " + size);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return list1;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");
        return list1;

    }

    public void onClick(View aview)
    {
        if (aview == startButton2) {
            rawDataDisplay2.setText("");
            urlSource = urlSource1;
            startProgress();
        }
        if (aview==anotherButton)
        {
            rawDataDisplay2.setText("");
            urlSource = urlSource2;
            startProgress();
        }
        if (aview==yetAnotherButton)
        {
            rawDataDisplay2.setText("");
            urlSource = urlSource3;
            startProgress();
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                in.readLine();
                // Throw away the first 2 header lines before parsing
                while ((inputLine = in.readLine()) != null)
                {
                    result2 = result2 + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }
            //
            // Now that you have the xml data you can parse it
            //
            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    LinkedList<Roadworks> list1 = null;
                    list1 = parseData(result2);
                    if(textTest.getText().length() == 0) {
                        rawDataDisplay2.setText("");
                        rawDataDisplay2.setText(list1.toString());
                    }
                    if(textTest.getText().length() != 0) {
                        String list2 = "";
                        if(list1.toString().toLowerCase().contains(textTest.getText().toString().toLowerCase())) {
                            list2 = list2 + list1.toString();
                        }
                            else {
                                Toast.makeText(MainActivity.this, "No search results", Toast.LENGTH_SHORT).show();
                            }
                        rawDataDisplay2.setText("");
                        rawDataDisplay2.setText(list2.toString());
                        }

                    }
            });
        }

    }



} // End of MainActivity
