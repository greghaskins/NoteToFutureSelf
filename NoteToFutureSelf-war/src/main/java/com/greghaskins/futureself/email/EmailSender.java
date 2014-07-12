package com.greghaskins.futureself.email;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	public void sendMessage(final InternetAddress recipient, final Subject subject, final Body body) {
		try {
			final MimeMessage message = createMessage(recipient, subject, body);
			sendMessage(message);
		} catch (final MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static MimeMessage createMessage(final InternetAddress recipient,
			final Subject subject, final Body body) throws MessagingException {
		final MimeMessage message = createBlankMessage();
		message.addRecipient(javax.mail.Message.RecipientType.TO, recipient);
		message.setSubject(subject.text);
		message.setText(body.text);
		return message;
	}

	private static MimeMessage createBlankMessage() {
		final Session session = Session.getDefaultInstance(new Properties());
		final MimeMessage message = new MimeMessage(session);
		return message;
	}

	private static void sendMessage(final MimeMessage message) throws MessagingException {
		Transport.send(message);
	}

	public static class Subject {

		private final String text;

		public Subject(final String text) {
			this.text = text;
		}

	}

	public static class Body {

		private final String text;

		public Body(final String text) {
			this.text = text;
		}

	}

}
