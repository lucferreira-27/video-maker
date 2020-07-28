package br.com.lucas.util;

import express.Express;

public class JavaHTTPServer extends Thread {

	private boolean done = false;
	private String code;
	private Express app = new Express();

	public void run() {
		synchronized (this) {
			connectToServer();
			while(done != true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			notify();

		}
	}

	public void connectToServer() {
		


		app.get("/oauth2callback", (req, res) -> {

			code = req.getQuery("code");
			//System.out.println("> Consent given: " + code);

			res.send("<h1> Thank you! </h1> <p> Please close this tab <p>");
			done = true;
		}).listen();
		
		

	}

	public void closeServer() {
		app.stop();
	}

	public Express getApp() {
		return app;
	}

	public void setApp(Express app) {
		this.app = app;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}