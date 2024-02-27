package mailTest;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;
import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
public class FetchOTP {
	
	    // Available in the API tab of a server
	    String apiKey = "URs7ZyAsj2pPJxUqLtLpEAXiqhgpF0UE";  //  YOUR_API_KEY - mailosaur profile, seetings, api key
	    String serverId = "sestf3pi";   //SERVER_ID
	    String serverDomain = "sestf3pi.mailosaur.net";   //SERVER_DOMAIN   anything@sestf3pi.mailosaur.net
        String from= "noreply@groww.in";
        WebDriver driver;
        
	    public String getRandomEmail() {
	    	return "user"+System.currentTimeMillis() +"@"+serverDomain; 
	    }
	    
	     public Message waitForEmail(String emailId, MailosaurClient mailosaur) {
			Wait<MailosaurClient> wait= new FluentWait<>(mailosaur)
			.withTimeout(Duration.ofSeconds(30))
			.pollingEvery(Duration.ofMillis(100))
			.ignoring(Exception.class);
         	
			return wait.until(mailosaurClient ->{
         			try {
         				// search for email
         			    MessageSearchParams params = new MessageSearchParams();
         			    params.withServer(serverId);

         			    SearchCriteria criteria = new SearchCriteria();
         			    criteria.withSentTo(emailId);	
         			    criteria.withSentFrom(from);
         			    
         			    Message message = mailosaur.messages().get(params, criteria);
         			    return message;
         			    
         			}catch(MailosaurException | IOException e) {
         				// return null if emailid not found
         			return null;
         			}
         			}) ;   			      			 
	     }
			
			
	    @Test 
		  public void testExample() throws IOException, MailosaurException {
		    String emailId= getRandomEmail();
		    System.setProperty("webdriver.chrome.driver", "C:\\Users\\Reka\\Drivers\\chromedriver.exe");	
		    
		    ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			chromeOptions.setAcceptInsecureCerts(true);
			chromeOptions.addArguments("--remote-allow-origins=*");	  	
		    driver= new ChromeDriver(chromeOptions);
		    //driver.manage().deleteAllCookies();
		    //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
		    driver.get("https://groww.in/login");
		    driver.findElement(By.id("login_email1")).sendKeys(emailId);
		    driver.findElement(By.xpath("//span[text()='Continue']")).click();
		    
		    
		    MailosaurClient mailosaur = new MailosaurClient(apiKey);
		    Message message=waitForEmail(emailId, mailosaur);
		    
//		    MessageSearchParams params = new MessageSearchParams();
//		    params.withServer(serverId);
//		    SearchCriteria criteria = new SearchCriteria();
//		    criteria.withSentTo(emailId);
//		    criteria.withSentFrom(from);
//		    Message message = mailosaur.messages().get(params, criteria);
		    
		    // otp from subject
		   
		    String subject =message.subject();  //if u want to fetch from body : message.text().body()
		    System.out.println(subject);
		    Pattern pattern = Pattern.compile("Your email verification OTP is .*([0-9]{6}).*");
	        Matcher matcher = pattern.matcher(subject);
	        matcher.find();

	        String otp= matcher.group(1);
	        System.out.println(otp);
		    
	        
	        driver.findElement(By.id("signup_otp1")).sendKeys(otp);
	        driver.findElement(By.xpath("//span[text()='Submit']")).click();    
		    
	    }
}
