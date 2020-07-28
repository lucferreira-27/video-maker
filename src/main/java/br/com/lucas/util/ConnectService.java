package br.com.lucas.util;

import java.io.IOException;
import java.net.ConnectException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ConnectService {
	private WebClient webClient;
	private HtmlPage page;

	public ConnectService(String url) throws ConnectException {
		// TODO Auto-generated constructor stub
		webClient = new WebClient(BrowserVersion.FIREFOX_60);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(10000);
		connect(url);
	}

	private void connect(String url) throws ConnectException {
		try {
			page = webClient.getPage(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectException("Erro ao se conectar a " + url + "");
		}
	}

	public void click(HtmlElement a) {
		try {
			a.click();
		webClient.waitForBackgroundJavaScript(10000);

			page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public HtmlPage getPage() {
		return page;
	}

	public void setPage(HtmlPage page) {
		this.page = page;
	}

}
