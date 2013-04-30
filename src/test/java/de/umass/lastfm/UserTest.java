/**
 * 
 */
package de.umass.lastfm;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * @author Thom Wiggers
 *
 */
public class UserTest {

    /**
     * Test method for {@link de.umass.lastfm.User#getRecentTracks(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetRecentTracksStringString() {
	final PaginatedResult<Track> res = User.getRecentTracks("MacGyverNL",
		PrivateKeys.APIKEY);
	assertFalse(res.getResult().isSuccessful());
    }

}
