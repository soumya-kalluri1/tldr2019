import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesRequest;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.amazonaws.*;
import com.amazonaws.services.comprehend.model.InputDataConfig;
import com.amazonaws.services.comprehend.model.KeyPhrase;
public class App 
{
	//private static final String size = "s"
//	private static final int CHAR_SIZE = "s".getytes();
    public static void main( String[] args )
    {
    	
    	String text = "";
    	try {
        text = new String(Files.readAllBytes(Paths.get("C:\\Users\\Soumya Kalluri\\School\\College\\ScientificArticleTest.txt"))) ;
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	String[] splits = stringSplitter( text );
    	
        // Create credentials using a provider chain. For more information, see
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
        AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
 
        AmazonComprehend comprehendClient =
            AmazonComprehendClientBuilder.standard()
                                         .withCredentials(awsCreds)
                                         .withRegion("us-west-2")
                                         .build();
                                         
        // Call detectKeyPhrases API
        System.out.println("Calling DetectKeyPhrases");
        HashMap<String, Integer> map = new HashMap<String,Integer>();
        
        for( int j = 0; j < splits.length; j++ ) {
        	DetectKeyPhrasesRequest detectKeyPhrasesRequest = new DetectKeyPhrasesRequest().withText(splits[j])
                                                                                       .withLanguageCode("en");
        	DetectKeyPhrasesResult detectKeyPhrasesResult = comprehendClient.detectKeyPhrases(detectKeyPhrasesRequest);
        	ArrayList<KeyPhrase> list = (ArrayList<KeyPhrase>) detectKeyPhrasesResult.getKeyPhrases();
        	for( int i = 0; i < list.size(); i++ ) {
        		if ( map.containsKey(list.get(i).getText()) ) {
        			map.put(list.get(i).getText(), map.get(list.get(i).getText()) + 1);
        		} else {
        			map.put( list.get(i).getText(), 1);
        		}
        	}
        }
        ArrayList<String> freqKeys = new ArrayList<String>();
        Set<String> keySet = map.keySet();
        Object[] keys = keySet.toArray();
        
//        for( Integer i: map.values() ) {
//        	System.out.println( i );
//        	if( i > 1 ) {
//        		freqKeys.add();
//        	}
//        }
        for( int i=0; i<keys.length; i++) {
        	if( map.get(keys[i]) > 1 ) {
        		freqKeys.add((String)keys[i]);
        	}
        }
        
        for( String key : freqKeys ) {
        	System.out.println(key);
        }
        
        System.out.println("End of DetectKeyPhrases\n");
    }
    
    private static String[] stringSplitter( String text ) {
    	String[] split = new String[ (text.length() / 2500) + 1];
    	int index = 0; 
    	for( int i = 0; i < text.length(); i+=2500 ) {
    		if( i+2500 < text.length()) {
    		split[index] = text.substring(i,i+2500); 
    		} else {
    		  split[index] = text.substring(i, text.length());
    		}
    		index++;
    	}
    	return split;
    	
    }
    
}