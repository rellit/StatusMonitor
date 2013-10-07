package de.timherbst.statusserver.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import de.timherbst.statusserver.service.MessageAggregator;

public class RefreshingServlet implements Servlet {

	RefreshThread refreshThread;

	@Override
	public void destroy() {
		if (refreshThread != null && refreshThread.isAlive())
			refreshThread.setStop(true);
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		System.out.println("Start refreshing Thread");
		refreshThread = new RefreshThread();
		refreshThread.start();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	private class RefreshThread extends Thread {
		private boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				MessageAggregator.refreshMessages();
				try {
					Thread.sleep(90000);
				} catch (InterruptedException e) {

				}
			}
		}

		public void setStop(boolean stop) {
			this.stop = stop;
		}

	}

}
