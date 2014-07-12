package com.greghaskins.futureself;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.greghaskins.futureself.email.EmailSender;
import com.greghaskins.futureself.email.EmailSender.Body;
import com.greghaskins.futureself.email.EmailSender.Recipient;
import com.greghaskins.futureself.email.EmailSender.Subject;
import com.greghaskins.futureself.email.EmailSendingException;

public class FutureSelfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

		final User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		try {
			sendEmailDirectly(currentUser.getEmail());
			resp.getWriter().println("Sent.");
		} catch (final EmailSendingException e) {
			resp.getWriter().println("Failed.");
			Logger.getLogger(getServletName()).log(Level.SEVERE,
					"Failed to send email to " + currentUser.getEmail(), e);
		}

	}

	protected void sendEmailDirectly(final String emailAddress) throws EmailSendingException {
		new EmailSender().sendMessage(new Recipient(emailAddress), new Subject(
				"Test note to future self"), new Body(
				"This is a test message. If you're reading this, it worked."));
	}
}
