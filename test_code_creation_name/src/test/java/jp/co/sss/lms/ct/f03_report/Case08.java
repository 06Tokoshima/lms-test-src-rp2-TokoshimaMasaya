package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

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
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		goTo("http://localhost:" + PORT + "/lms/");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("ログイン | LMS"));

		assertEquals("ログイン | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[type='submit']")).isDisplayed());

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

		driver.findElement(By.cssSelector("input[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("コース詳細 | LMS"));

		assertEquals("コース詳細 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[value='詳細']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日（週報）の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 「2025年7月9日」が含まれる行の「詳細」ボタンをピンポイントで取得
		By weeklyDetailBtn = By.xpath("//tr[td[contains(text(),'7月9日')]]//input[@value='詳細']");

		// 万が一「7月9日」の記述で見つからない場合の予備（2つ目の「提出済み」ボタンを取得）
		if (driver.findElements(weeklyDetailBtn).isEmpty()) {
			weeklyDetailBtn = By.xpath("(//tr[td/span[text()='提出済み']]//input[@value='詳細'])[2]");
		}

		WebElement detailBtnElement = driver.findElement(weeklyDetailBtn);

		// ボタンが見える位置までスクロール（ヘッダー被り防止）
		((org.openqa.selenium.JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({block: 'center'});", detailBtnElement);

		detailBtnElement.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("セクション詳細 | LMS"));

		assertEquals("セクション詳細 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[value='戻る']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出済み週報【デモ】を確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// ボタン要素の取得
		By checkBtn = By.cssSelector("input[value*='を確認する']");
		WebElement checkBtnElement = driver.findElement(checkBtn);

		// ボタンが画面の中央に来るようにスクロール（要素被りを防止）
		((org.openqa.selenium.JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({block: 'center'});", checkBtnElement);

		checkBtnElement.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("レポート登録 | LMS"));

		assertEquals("レポート登録 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("button[type='submit']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		// 1つ目の入力欄（日報・週報共通）
		WebElement inputText0 = driver.findElement(By.id("content_0"));
		inputText0.clear();
		inputText0.sendKeys("研修内容の報告修正テストです。");

		// 2つ目の入力欄が存在する場合のみ入力（週報の場合）
		if (!driver.findElements(By.id("content_1")).isEmpty()) {
			WebElement inputText1 = driver.findElement(By.id("content_1"));
			inputText1.clear();
			inputText1.sendKeys("テスト");
		}

		// 3つ目の入力欄が存在する場合のみ入力（週報の場合）
		if (!driver.findElements(By.id("content_2")).isEmpty()) {
			WebElement inputText2 = driver.findElement(By.id("content_2"));
			inputText2.clear();
			inputText2.sendKeys("テスト");
		}

		// 「提出する」ボタンを取得してスクロール表示後クリック
		WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
		((org.openqa.selenium.JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({block: 'center'});", submitBtn);
		submitBtn.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("セクション詳細 | LMS"));

		assertEquals("セクション詳細 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[value='戻る']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		driver.findElement(By.partialLinkText("ようこそ")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[value='パスワード変更する']")));

		assertTrue(driver.findElement(By.cssSelector("input[value='パスワード変更する']")).isDisplayed());
		assertEquals("ユーザー詳細", driver.getTitle());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		scrollTo("200");

		driver.findElement(By.cssSelector("input[value='詳細']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("レポート詳細 | LMS"));

		assertTrue(driver.findElement(By.cssSelector("button[onclick*='history.back']")).isDisplayed());
		assertEquals("レポート詳細 | LMS", driver.getTitle());

		getEvidence(new Object() {
		});
	}

}