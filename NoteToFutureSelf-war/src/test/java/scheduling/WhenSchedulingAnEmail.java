package scheduling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.greghaskins.futureself.FutureSelfServlet;

public class WhenSchedulingAnEmail {

	private final LocalServiceTestHelper serviceHelper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig());

	@Before
	public void setUp() {
		this.serviceHelper.setUp();
		this.serviceHelper.setEnvIsLoggedIn(true);
		this.serviceHelper.setEnvAuthDomain("google.com");
	}

	@After
	public void tearDown() {
		this.serviceHelper.tearDown();
	}

	@Test
	public void itUsesTheCurrentUsersEmailAddress() throws Exception {
		final String currentUserEmail = "currentUser@example.com";
		this.serviceHelper.setEnvEmail(currentUserEmail);

		final List<String> emailAddressesUsed = doPostAndGetEmailAddressesUsed();

		assertThat(emailAddressesUsed, contains(currentUserEmail));
	}

	private List<String> doPostAndGetEmailAddressesUsed() throws ServletException, IOException {
		final ArrayList<String> emailAddressesUsed = new ArrayList<>();
		final FutureSelfServlet servlet = new FutureSelfServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void sendEmailDirectly(final String emailAddress) {
				emailAddressesUsed.add(emailAddress);
			}
		};

		servlet.doPost(mock(HttpServletRequest.class, Mockito.RETURNS_DEEP_STUBS),
				mock(HttpServletResponse.class, Mockito.RETURNS_DEEP_STUBS));
		return emailAddressesUsed;
	}

}
