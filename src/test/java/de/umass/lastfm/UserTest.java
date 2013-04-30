/**
 * 
 */
package de.umass.lastfm;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.Before;

/**
 * @author Thom Wiggers
 *
 */
public class UserTest {

    @Before
    public void preconditions() {
      Assume.assumeTrue(!PrivateKeys.APIKEY.equals(""));
    }

    /**
     * Test method for {@link de.umass.lastfm.User#getRecentTracks(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetRecentTracksStringString() {
	// random user with privacy set
	final PaginatedResult<Track> res = User.getRecentTracks("MacGyverNL",
		PrivateKeys.APIKEY);
	Assert.assertFalse(res.getResult().isSuccessful());
	Assert.assertEquals(
		"This user has made their recent tracks private. Only they can access them with an authenticated session",
		res.getResult().getErrorMessage());
    }

}
