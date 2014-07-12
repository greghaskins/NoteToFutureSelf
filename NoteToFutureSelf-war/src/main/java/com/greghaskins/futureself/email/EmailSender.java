package com.greghaskins.futureself.email;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	public void sendMessage(final Recipient recipient, final Subject subject, final Body body)
			throws EmailSendingException {
		try {
			final MimeMessage message = createMessage(recipient, subject, body);
			sendMessage(message);
		} catch (final MessagingException e) {
			throw new EmailSendingException(e);
		}
	}

	private static MimeMessage createMessage(final Recipient recipient, final Subject subject,
			final Body body) throws MessagingException {
		final MimeMessage message = createBlankMessage();
		final InternetAddress internetAddress = new InternetAddress(recipient.emailAddress);
		message.addRecipient(javax.mail.Message.RecipientType.TO, internetAddress);
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

	public static class Recipient {

		private final String emailAddress;

		public Recipient(final String emailAddress) {
			this.emailAddress = emailAddress;
		}

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
