package sending;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.mail.MailServicePb.MailMessage;
import com.google.appengine.api.mail.dev.LocalMailService;
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.greghaskins.futureself.email.EmailSender;
import com.greghaskins.futureself.email.EmailSender.Body;
import com.greghaskins.futureself.email.EmailSender.Recipient;
import com.greghaskins.futureself.email.EmailSender.Subject;
import com.greghaskins.futureself.email.EmailSendingException;

public class WhenDeliveringAnEmail {

	// TODO Validation?

	private LocalServiceTestHelper serviceTestHelper;
	private LocalMailService mailService;

	@Before
	public void before() {
		this.mailService = setUpMockMailService();
	}

	private LocalMailService setUpMockMailService() {
		final LocalMailServiceTestConfig mailServiceTestConfig = new LocalMailServiceTestConfig();
		mailServiceTestConfig.setLogMailBody(true);
		this.serviceTestHelper = new LocalServiceTestHelper(mailServiceTestConfig);
		this.serviceTestHelper.setUp();
		return LocalMailServiceTestConfig.getLocalMailService();
	}

	@After
	public void after() {
		this.serviceTestHelper.tearDown();
	}

	private MailMessage sendMessage(final Recipient recipient, final Subject subject,
			final Body body) throws Exception {
		final EmailSender emailSender = new EmailSender();
		emailSender.sendMessage(recipient, subject, body);
		return this.mailService.getSentMessages().get(0);
	}

	@Test
	public void itGoesToTheCorrectRecipientAddress() throws Exception {
		final String expectedEmailAddress = "myself@example.com";

		final MailMessage mailMessage = sendMessage(new Recipient(expectedEmailAddress),
				new EmailSender.Subject(""), new Body(""));

		assertThat(mailMessage.getTo(0), is(expectedEmailAddress));
	}

	@Test
	public void itHasTheCorrectSubjectLine() throws Exception {
		final String expectedSubject = "An important topic";

		final MailMessage mailMessage = sendMessage(new Recipient("some@example.com"), new Subject(
				expectedSubject), new Body(""));

		assertThat(mailMessage.getSubject(), is(expectedSubject));
	}

	@Test
	public void itHasTheCorrectBodyText() throws Exception {
		final String expectedBodyText = "The meat of the matter";

		final MailMessage mailMessage = sendMessage(new Recipient("some@example.com"), new Subject(
				""), new Body(expectedBodyText));

		assertThat(mailMessage.getTextBody(), is(expectedBodyText));
	}

	@Test(expected = EmailSendingException.class)
	public void itThrowsAnEmailSendingExceptionIfTheAddressIsBogus() throws Exception {
		sendMessage(new Recipient(",&51"), new Subject("something"), new Body("something"));
	}

}
