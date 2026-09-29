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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		By unsubmittedDetailBtn = By.xpath("//tr[td/span[text()='未提出']]//input[@value='詳細']");
		driver.findElement(unsubmittedDetailBtn).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("セクション詳細 | LMS"));

		assertEquals("セクション詳細 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[value='戻る']")).isDisplayed());

		scrollTo("200");

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		driver.findElement(By.cssSelector("input[value*='を提出する']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("レポート登録 | LMS"));

		assertEquals("レポート登録 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("button[type='submit']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		WebElement inputText = driver.findElement(By.id("content_0"));
		inputText.clear();
		inputText.sendKeys("本日の研修内容の報告テストです。");

		scrollTo("500");

		driver.findElement(By.cssSelector("button[type='submit']")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.titleIs("セクション詳細 | LMS"));

		assertEquals("セクション詳細 | LMS", driver.getTitle());
		assertTrue(driver.findElement(By.cssSelector("input[value='戻る']")).isDisplayed());
		assertTrue(driver.findElement(By.cssSelector("input[value*='提出済み']")).isDisplayed());

		scrollTo("200");

		getEvidence(new Object() {
		});
	}

}