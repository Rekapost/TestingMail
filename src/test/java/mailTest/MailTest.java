package mailTest;
import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Attachment;
import com.mailosaur.models.Code;
import com.mailosaur.models.Link;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


//  From Naveen Automation Labs:  https://www.youtube.com/watch?v=Il8efgsiJyw
// https://mailosaur.com/app/get-started
	public class MailTest {
	  @Test 
	  public void testExample() throws IOException, MailosaurException {
	    // Available in the API tab of a server
	    String apiKey = "URs7ZyAsj2pPJxUqLtLpEAXiqhgpF0UE";  //  YOUR_API_KEY - mailosaur profile, seetings, api key
	    String serverId = "sestf3pi";   //SERVER_ID
	    String serverDomain = "sestf3pi.mailosaur.net";   //SERVER_DOMAIN   anything@sestf3pi.mailosaur.net

	    MailosaurClient mailosaur = new MailosaurClient(apiKey);

	    MessageSearchParams params = new MessageSearchParams();
	    params.withServer(serverId);

	    SearchCriteria criteria = new SearchCriteria();
	    criteria.withSentTo("anything@" + serverDomain);

	    Message message = mailosaur.messages().get(params, criteria);
	    System.out.println(message.subject());
	    //System.out.println(message.from());
	    System.out.println(message.from().get(0).email());
	    System.out.println(message.to().get(0).email());
	    System.out.println(message.cc());
	    
	    assertNotNull(message);
	    assertEquals("Fwd: Automation Testing Openings - Immediate Joiners [5 Years to 10 Years] <> Tata Consultancy Services", message.subject());   //My email subject
	  
	    //body
	    System.out.println(message.text().body()); 
	    assertTrue(message.text().body().contains("Have you Worked in TCS"));
	  
	   //links
	   // How many links?
//	    console.log(message.html.links.length) // 2
//	    const firstLink = message.html.links[0]
//	    console.log(firstLink.text) // "Google Search"
//	    console.log(firstLink.href) // "https://www.google.com/"
	    System.out.println(message.html().links().size()); 
	    List<Link> allLinks=message.html().links();
	    for(Link eachLink:allLinks) {
	    	String linkName = eachLink.text();
	    	System.out.println(linkName);
	    	String href=eachLink.href();
	    	System.out.println(href);
	    }
	    
	    Link firstLink = message.html().links().get(0);
	    System.out.println(firstLink.text()); // "Google Search"
        System.out.println(firstLink.href()); // "https://www.google.com/"
	  
	  //attachement
//        console.log(message.attachments.length) // 2
//        const firstAttachment = message.attachments[0]
//        console.log(firstAttachment.fileName) // "contract.pdf"
//        console.log(firstAttachment.contentType) // "application/pdf"
//        		
//        const firstAttachment = message.attachments[0]
//        console.log(firstAttachment.length) // 4028
	  
        System.out.println(message.attachments().size()) ;
        Attachment firstAttachment = message.attachments().get(0);     
        System.out.println(firstAttachment.fileName()); 
        System.out.println(firstAttachment.contentType()); 
        System.out.println(firstAttachment.length()); 
	  
        
        // download 
        Attachment firstAttachment1 = message.attachments().get(0);
        byte[] file = mailosaur.files().getAttachment(firstAttachment1.id());
        Files.write(Paths.get(firstAttachment.fileName()), file);
	 
/*	  
	 // codes ; OTP NUMBER
        
     // How many codes?
        System.out.println(message.text().codes().size()); // 2

        Code firstCode = message.text().codes().get(0);
        System.out.println(firstCode.value()); // "456812"
        
     // How many codes?
        System.out.println(message.html().codes().size()); // 2

        Code firstCode1 = message.html().codes().get(0);
        System.out.println(firstCode1.value()); // "456812"
        
        
     // Finding a 6-digit code
     
        System.out.println(message.text().body()); // "Your access code is 243546."

        Pattern pattern = Pattern.compile(".*([0-9]{6}).*");
        Matcher matcher = pattern.matcher(message.text().body());
        matcher.find();

        System.out.println(matcher.group(1)); // "243546"
        
        
     // Finding an 8-digit alphanumeric code

        System.out.println(message.text().body()); // "Your access code is QZ524822."

        Pattern pattern1 = Pattern.compile(".*([A-Z]{2}[0-9]{6}).*");
        Matcher matcher1 = pattern1.matcher(message.text().body());
        matcher1.find();

        System.out.println(matcher1.group(1)); // "Q2ZH482S"
*/	  
  
	  }
	
}
