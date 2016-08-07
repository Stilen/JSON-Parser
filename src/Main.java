import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main
{
  static String url = "http://api.goeuro.com/api/v2/position/suggest/en/";
  static String welcome = "This app returns the ID, name, type, latitude and longitude of one city.";
  static String input;
  
  public static void main(String[] args)
    throws IOException, JSONException
  {
	if(args.length!=1 || isWord(args[0])==false){
		System.err.println("The format of your input is incorrect, please insert the name of a city.");
		System.exit(0);
	}
	input = args[0];
    JSONArray json = readJsonFromUrl(url + input);
    if (json.length() == 0)
    {
      System.out.println("There is no information about " + input + ".");
      System.exit(0);
    }
    printer(json, input);
  }
  
  
  public static boolean isWord(String s)
  {
    char[] chars = s.toCharArray();
    char[] arrayOfChar1;
    int j = (arrayOfChar1 = chars).length;
    for (int i = 0; i < j; i++)
    {
      char c = arrayOfChar1[i];
      if (!Character.isLetter(c)) {
        return false;
      }
    }
    return true;
  }
  
  
  private static String readAll(Reader rd)
    throws IOException
  {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1)
    {
      sb.append((char)cp);
    }
    return sb.toString();
  }
  
  
  
  public static JSONArray readJsonFromUrl(String url)
    throws IOException, JSONException
  {
    InputStream is = new URL(url).openStream();
    try
    {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONArray json = new JSONArray(jsonText);
      return json;
    }
    finally
    {
      is.close();
    }
  }
  
  
  
  public static void printer(JSONArray array, String input)
    throws JSONException, IOException
  {
    FileWriter fw = new FileWriter(input + ".csv");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length(); i++)
    {
      JSONObject obj = array.getJSONObject(i);
      sb.append(obj.get("_id"));
      sb.append(",");
      sb.append(obj.get("name"));
      sb.append(",");
      sb.append(obj.get("type"));
      sb.append(",");
      
      JSONObject geo = (JSONObject)obj.get("geo_position");
      sb.append(geo.get("latitude"));
      sb.append(",");
      sb.append(geo.get("longitude"));
      sb.append("\n");
    }
    fw.write(sb.toString());
    fw.close();
    System.out.println(input+".csv was successfully created!");
  }
}
