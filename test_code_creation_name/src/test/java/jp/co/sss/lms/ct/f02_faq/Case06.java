package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	private final int PORT = 8080;
	private WebDriver driver = WebDriverUtils.webDriver;

	@BeforeAll
	static void before() {
		createDriver();
	}

	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:" + PORT + "/lms");
		assertEquals("ログイン | LMS", driver.getTitle());
		assertEquals("ログイン", driver.findElement(By.tagName("h2")).getText());
		assertEquals("ログイン", driver.findElement(By.className("btn-primary")).getAttribute("value"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		driver.findElement(By.name("loginId")).clear();
		driver.findElement(By.name("loginId")).sendKeys("StudentAA03");

		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("Sakura1120");

		driver.findElement(By.className("btn-primary")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("コース詳細 | LMS"));

		assertEquals("コース詳細 | LMS", driver.getTitle());

		String h2Text = driver.findElement(By.tagName("h2")).getText();
		assertTrue(h2Text.contains("DEMOコース"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		driver.findElement(By.className("dropdown-toggle")).click();
		driver.findElement(By.linkText("ヘルプ")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("ヘルプ | LMS"));

		assertEquals("ヘルプ | LMS", driver.getTitle());
		assertEquals("ヘルプ", driver.findElement(By.tagName("h2")).getText());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		String Handle = driver.getWindowHandle();
		driver.findElement(By.linkText("よくある質問")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		String newHandle = null;
		for (String id : driver.getWindowHandles()) {
			if (!id.equals(Handle)) {
				newHandle = id;
			}
		}

		driver.switchTo().window(newHandle);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h2")));

		assertEquals("よくある質問 | LMS", driver.getTitle());
		assertEquals("よくある質問", driver.findElement(By.tagName("h2")).getText());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		driver.findElement(By.linkText("【人材開発支援助成金】")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("dt")));

		List<WebElement> dtElements = driver.findElements(By.tagName("dt"));

		WebElement dtElement1 = dtElements.get(0);
		assertEquals("セルフ・キャリアドック制度とは何か", dtElement1.findElements(By.tagName("span")).get(1).getText());

		WebElement dtElement2 = dtElements.get(1);
		assertEquals("事業所が変わった場合、何かしら手続きをする必要がありますか？", dtElement2.findElements(By.tagName("span")).get(1).getText());

		WebElement dtElement3 = dtElements.get(2);
		assertEquals("助成金書類の作成方法が分かりません", dtElement3.findElements(By.tagName("span")).get(1).getText());

		scrollTo("300");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		List<WebElement> dtElements = driver.findElements(By.tagName("dt"));
		List<WebElement> ddElements = driver.findElements(By.tagName("dd"));

		dtElements.get(2).click();
		dtElements.get(1).click();
		dtElements.get(0).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(ddElements.get(0)));

		assertEquals(
				"労働者にジョブカードを活用した、キャリアコンサルタントによるキャリアコンサルティングを定期的に提供するものです。 なお、セルフ・キャリアドック制度を就業規則または労働協約に規定し、また、「セルフ・キャリアドック実施計画書」の作成が別途必要となります。",
				ddElements.get(0).findElements(By.tagName("span")).get(1).getText());
		assertEquals(
				"以前は変更申請の必要がございましたが、2020年4月～は変更届の必要がなくなりました。",
				ddElements.get(1).findElements(By.tagName("span")).get(1).getText());
		assertEquals(
				"LMSマニュアルを参考に、LMSから助成金の書類をダウンロードしてください。 手引きもご用意させていただいておりますので、必ずご一読ください。 ダウンロードした助成金の書類には、基本的な御社の情報・研修情報が予め記載されております。 ご不明な点がございましたら、営業担当または東京ITスクール運営事務局までご連絡ください。",
				ddElements.get(2).findElements(By.tagName("span")).get(1).getText());

		getEvidence(new Object() {
		}, "1");

		scrollTo("600");
		getEvidence(new Object() {
		}, "2");
	}

}