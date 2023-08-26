package seleniumProjects;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class BrokenLinkTest {

	@Test
	public void findBrokenLinks() throws MalformedURLException, IOException {
		//System.setProperty("webdriver.chrome.driver", "/Users/christiansanchez/seleniumcourse/chromedriver");
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get("https://www.makemysushi.com/");
		
		//1. Get the list of links and images
		List<WebElement> linksList = driver.findElements(By.tagName("a"));
		linksList.addAll(driver.findElements(By.tagName("img")));
		
		System.out.println("Size of full links and images --> "+linksList.size());
		
		List<WebElement> activeLinks = new ArrayList<WebElement>();
		
		//2. Iterate LinkList : exclude all the links/images that don't have href attribute or Javascript
		for(int i = 0; i < linksList.size(); i++)
		{
			System.out.println(linksList.get(i).getAttribute("href"));
			if(linksList.get(i).getAttribute("href") != null && (! linksList.get(i).getAttribute("href").contains("javascript"))) {
				activeLinks.add(linksList.get(i));
			}
		}
		
		//3. Get the size of active links list:
		System.out.println("Size of active links and images --> "+activeLinks.size());
		
		//4. Check the href url with httpconnection api
		for(int j=0; j<activeLinks.size();j++)
		{
		 HttpURLConnection connection= (HttpURLConnection)new URL(activeLinks.get(j).getAttribute("href")).openConnection();
		 connection.connect();
		 String response = connection.getResponseMessage(); //ok
		 connection.disconnect();
		 System.out.println(activeLinks.get(j).getAttribute("href") + "-->" + response);
		}
		
		driver.close();
	}
}

