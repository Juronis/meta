package lt.vaidotas.metasite;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String turinys="";
		try {
			turinys = readFile("labas.txt", Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(turinys);
		while (matcher.find()) {
		    System.out.println(matcher.group());
		}
    }
    
    static String readFile(String path, Charset encoding) 
    		  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
