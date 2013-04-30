/*
 * Copyright (c) 2012, the Last.fm Java Project and Committers
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.umass.lastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.umass.util.MapUtilities;
import de.umass.util.StringUtilities;
import de.umass.xml.DomElement;

/**
 * Contains user information and provides bindings to the methods in the user. namespace.
 *
 * @author Janni Kovacs
 */
public class User extends ImageHolder {

    static final ItemFactory<User> FACTORY = new UserFactory();

    private String id;
    private final String name;
    private final String url;

    private String realname;

    private String language;
    private String country;
    private int age = -1;
    private String gender;
    private boolean subscriber;
    private int numPlaylists;
    private int playcount;
    private Date registeredDate;

    private User(final String name, final String url) {
	this.name = name;
	this.url = url;
    }

    public String getName() {
	return name;
    }

    public String getRealname() {
	return realname;
    }

    public String getUrl() {
	return url;
    }

    public int getAge() {
	return age;
    }

    public String getCountry() {
	return country;
    }

    public String getGender() {
	return gender;
    }

    public String getLanguage() {
	return language;
    }

    public int getNumPlaylists() {
	return numPlaylists;
    }

    public int getPlaycount() {
	return playcount;
    }

    public boolean isSubscriber() {
	return subscriber;
    }

    public String getImageURL() {
	return getImageURL(ImageSize.MEDIUM);
    }

    public String getId() {
	return id;
    }

    public Date getRegisteredDate() {
	return registeredDate;
    }

    /**
     * Get a list of tracks by a given artist scrobbled by this user, including scrobble time. Can be limited to specific timeranges, defaults
     * to all time.
     *
     * @param user The last.fm username to fetch the recent tracks of
     * @param artist The artist name you are interested in
     * @param apiKey A Last.fm API key
     * @return a list of Tracks
     */
    public static PaginatedResult<Track> getArtistTracks(final String user, final String artist, final String apiKey) {
	return getArtistTracks(user, artist, 1, 0, 0, apiKey);
    }

    /**
     * Get a list of tracks by a given artist scrobbled by this user, including scrobble time. Can be limited to specific timeranges, defaults
     * to all time.
     *
     * @param user The last.fm username to fetch the recent tracks of
     * @param artist The artist name you are interested in
     * @param page An integer used to fetch a specific page of tracks
     * @param startTimestamp An unix timestamp to start at
     * @param endTimestamp An unix timestamp to end at
     * @param apiKey A Last.fm API key
     * @return a list of Tracks
     */
    public static PaginatedResult<Track> getArtistTracks(final String user, final String artist, final int page, final long startTimestamp, final long endTimestamp, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	params.put("user", user);
	params.put("artist", artist);
	params.put("page", String.valueOf(page));
	params.put("startTimestamp", String.valueOf(startTimestamp));
	params.put("endTimestamp", String.valueOf(endTimestamp));
	final Result result = Caller.getInstance().call("user.getArtistTracks", apiKey, params);
	return ResponseBuilder.buildPaginatedResult(result, Track.class);
    }

    public static PaginatedResult<User> getFriends(final String user, final String apiKey) {
	return getFriends(user, false, 1, 50, apiKey);
    }

    public static PaginatedResult<User> getFriends(final String user, final boolean recenttracks, final int page, final int limit, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getFriends", apiKey, "user", user, "recenttracks",
		String.valueOf(recenttracks ? 1 : 0), "limit", String.valueOf(limit), "page", String.valueOf(page));
	return ResponseBuilder.buildPaginatedResult(result, User.class);
    }

    public static Collection<User> getNeighbours(final String user, final String apiKey) {
	return getNeighbours(user, 100, apiKey);
    }

    public static Collection<User> getNeighbours(final String user, final int limit, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getNeighbours", apiKey, "user", user, "limit", String.valueOf(limit));
	return ResponseBuilder.buildCollection(result, User.class);
    }

    public static PaginatedResult<Track> getRecentTracks(final String user, final String apiKey) {
	return getRecentTracks(user, 1, 10, false, apiKey);
    }

    public static PaginatedResult<Track> getRecentTracks(final String user, final int page, final int limit, final String apiKey) {
	return getRecentTracks(user, page, limit, false, apiKey);
    }

    public static PaginatedResult<Track> getRecentTracks(final String user, final int page, final int limit, final boolean extended, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	params.put("user", user);
	params.put("limit", String.valueOf(limit));
	params.put("page", String.valueOf(page));
	params.put("extended", String.valueOf(extended));
	final Result result = Caller.getInstance().call("user.getRecentTracks", apiKey, params);
	return ResponseBuilder.buildPaginatedResult(result, Track.class);
    }

    public static Collection<Album> getTopAlbums(final String user, final String apiKey) {
	return getTopAlbums(user, Period.OVERALL, apiKey);
    }

    public static Collection<Album> getTopAlbums(final String user, final Period period, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getTopAlbums", apiKey, "user", user, "period", period.getString());
	return ResponseBuilder.buildCollection(result, Album.class);
    }

    public static Collection<Artist> getTopArtists(final String user, final String apiKey) {
	return getTopArtists(user, Period.OVERALL, 50, apiKey);
    }

    public static Collection<Artist> getTopArtists(final String user, final Period period, final String apiKey) {
	return getTopArtists(user, period, 50, apiKey);
    }

    public static Collection<Artist> getTopArtists(final String user, final Period period, final int limit, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getTopArtists", apiKey, "user", user, "period", period.getString(), "limit", String.valueOf(limit));
	return ResponseBuilder.buildCollection(result, Artist.class);
    }

    public static Collection<Track> getTopTracks(final String user, final String apiKey) {
	return getTopTracks(user, Period.OVERALL, apiKey);
    }

    public static Collection<Track> getTopTracks(final String user, final Period period, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getTopTracks", apiKey, "user", user, "period", period.getString());
	return ResponseBuilder.buildCollection(result, Track.class);
    }

    public static Collection<Tag> getTopTags(final String user, final String apiKey) {
	return getTopTags(user, -1, apiKey);
    }

    public static Collection<Tag> getTopTags(final String user, final int limit, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	params.put("user", user);
	MapUtilities.nullSafePut(params, "limit", limit);
	final Result result = Caller.getInstance().call("user.getTopTags", apiKey, params);
	return ResponseBuilder.buildCollection(result, Tag.class);
    }

    public static Chart<Album> getWeeklyAlbumChart(final String user, final String apiKey) {
	return getWeeklyAlbumChart(user, null, null, -1, apiKey);
    }

    public static Chart<Album> getWeeklyAlbumChart(final String user, final int limit, final String apiKey) {
	return getWeeklyAlbumChart(user, null, null, limit, apiKey);
    }

    public static Chart<Album> getWeeklyAlbumChart(final String user, final String from, final String to, final int limit, final String apiKey) {
	return Chart.getChart("user.getWeeklyAlbumChart", "user", user, "album", from, to, limit, apiKey);
    }

    public static Chart<Artist> getWeeklyArtistChart(final String user, final String apiKey) {
	return getWeeklyArtistChart(user, null, null, -1, apiKey);
    }

    public static Chart<Artist> getWeeklyArtistChart(final String user, final int limit, final String apiKey) {
	return getWeeklyArtistChart(user, null, null, limit, apiKey);
    }

    public static Chart<Artist> getWeeklyArtistChart(final String user, final String from, final String to, final int limit, final String apiKey) {
	return Chart.getChart("user.getWeeklyArtistChart", "user", user, "artist", from, to, limit, apiKey);
    }

    public static Chart<Track> getWeeklyTrackChart(final String user, final String apiKey) {
	return getWeeklyTrackChart(user, null, null, -1, apiKey);
    }

    public static Chart<Track> getWeeklyTrackChart(final String user, final int limit, final String apiKey) {
	return getWeeklyTrackChart(user, null, null, limit, apiKey);
    }

    public static Chart<Track> getWeeklyTrackChart(final String user, final String from, final String to, final int limit, final String apiKey) {
	return Chart.getChart("user.getWeeklyTrackChart", "user", user, "track", from, to, limit, apiKey);
    }

    public static LinkedHashMap<String, String> getWeeklyChartList(final String user, final String apiKey) {
	return Chart.getWeeklyChartList("user.getWeeklyChartList", "user", user, apiKey);
    }

    public static Collection<Chart> getWeeklyChartListAsCharts(final String user, final String apiKey) {
	return Chart.getWeeklyChartListAsCharts("user", user, apiKey);
    }

    /**
     * GetS a list of upcoming events that this user is attending.
     *
     * @param user The user to fetch the events for.
     * @param apiKey A Last.fm API key.
     * @return a list of upcoming events
     */
    public static PaginatedResult<Event> getEvents(final String user, final String apiKey) {
	return getEvents(user, -1, apiKey);
    }

    /**
     * GetS a list of upcoming events that this user is attending.
     *
     * @param user The user to fetch the events for.
     * @param page The page number to fetch. Defaults to first page.
     * @param apiKey A Last.fm API key.
     * @return a list of upcoming events
     */
    public static PaginatedResult<Event> getEvents(final String user, final int page, final String apiKey) {
	return getEvents(user, false, page, -1, apiKey);
    }

    /**
     * GetS a list of upcoming events that this user is attending.
     *
     * @param user The user to fetch the events for.
     * @param page The page number to fetch. Defaults to first page.
     * @param limit The number of results to fetch per page. Defaults to 50.
     * @param festivalsOnly Whether only festivals should be returned, or all events.
     * @param apiKey A Last.fm API key.
     * @return a list of upcoming events
     */
    public static PaginatedResult<Event> getEvents(final String user, final boolean festivalsOnly, final int page, final int limit, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	MapUtilities.nullSafePut(params, "user", user);
	MapUtilities.nullSafePut(params, "page", page);
	MapUtilities.nullSafePut(params, "limit", limit);
	if (festivalsOnly) {
	    params.put("festivalsonly", "1");
	}
	final Result result = Caller.getInstance().call("user.getEvents", apiKey, params);
	return ResponseBuilder.buildPaginatedResult(result, Event.class);
    }

    /**
     * Get the first page of a paginated result of all events a user has attended in the past.
     *
     * @param user The username to fetch the events for.
     * @param apiKey A Last.fm API key.
     * @return a list of past {@link Event}s
     */
    public static PaginatedResult<Event> getPastEvents(final String user, final String apiKey) {
	return getPastEvents(user, -1, apiKey);
    }

    /**
     * Gets a paginated list of all events a user has attended in the past.
     *
     * @param user The username to fetch the events for.
     * @param page The page number to scan to.
     * @param apiKey A Last.fm API key.
     * @return a list of past {@link Event}s
     */
    public static PaginatedResult<Event> getPastEvents(final String user, final int page, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	params.put("user", user);
	MapUtilities.nullSafePut(params, "page", page);
	final Result result = Caller.getInstance().call("user.getPastEvents", apiKey, params);
	return ResponseBuilder.buildPaginatedResult(result, Event.class);
    }

    public static PaginatedResult<Event> getRecommendedEvents(final Session session) {
	return getRecommendedEvents(1, session);
    }

    public static PaginatedResult<Event> getRecommendedEvents(final int page, final Session session) {
	final Result result = Caller.getInstance().call("user.getRecommendedEvents", session, "page", String.valueOf(page), "user",
		session.getUsername());
	return ResponseBuilder.buildPaginatedResult(result, Event.class);
    }

    /**
     * Gets a list of a user's playlists on Last.fm. Note that this method only fetches metadata regarding the user's playlists. If you want to
     * retrieve the list of tracks in a playlist use {@link Playlist#fetch(String, String) Playlist.fetch()}.
     *
     * @param user The last.fm username to fetch the playlists of.
     * @param apiKey A Last.fm API key.
     * @return a list of Playlists
     */
    public static Collection<Playlist> getPlaylists(final String user, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getPlaylists", apiKey, "user", user);
	if (!result.isSuccessful())
	    return Collections.emptyList();
	final Collection<Playlist> playlists = new ArrayList<Playlist>();
	for (final DomElement element : result.getContentElement().getChildren("playlist")) {
	    playlists.add(ResponseBuilder.buildItem(element, Playlist.class));
	}
	return playlists;
    }

    /**
     * Retrieves the loved tracks by a user.
     *
     * @param user The user name to fetch the loved tracks for.
     * @param apiKey A Last.fm API key.
     * @return the loved tracks
     */
    public static PaginatedResult<Track> getLovedTracks(final String user, final String apiKey) {
	return getLovedTracks(user, 1, apiKey);
    }

    /**
     * Retrieves the loved tracks by a user.
     *
     * @param user The user name to fetch the loved tracks for.
     * @param page The page number to scan to
     * @param apiKey A Last.fm API key.
     * @return the loved tracks
     */
    public static PaginatedResult<Track> getLovedTracks(final String user, final int page, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getLovedTracks", apiKey, "user", user, "page", String.valueOf(page));
	return ResponseBuilder.buildPaginatedResult(result, Track.class);
    }

    /**
     * Retrieves profile information about the specified user.
     *
     * @param user A username
     * @param apiKey A Last.fm API key.
     * @return User info
     */
    public static User getInfo(final String user, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getInfo", apiKey, "user", user);
	return ResponseBuilder.buildItem(result, User.class);
    }

    /**
     * Retrieves profile information about the authenticated user.
     *
     * @param session A session for the user, for whom to get the profile for
     * @return User info
     */
    public static User getInfo(final Session session) {
	final Result result = Caller.getInstance().call("user.getInfo", session);
	return ResponseBuilder.buildItem(result, User.class);
    }

    /**
     * Get Last.fm artist recommendations for a user.
     *
     * @param session A Session instance
     * @return a list of {@link Artist}s
     */
    public static PaginatedResult<Artist> getRecommendedArtists(final Session session) {
	return getRecommendedArtists(1, session);
    }

    /**
     * Get Last.fm artist recommendations for a user.
     *
     * @param page The page to fetch
     * @param session A Session instance
     * @return a list of {@link Artist}s
     */
    public static PaginatedResult<Artist> getRecommendedArtists(final int page, final Session session) {
	final Result result = Caller.getInstance().call("user.getRecommendedArtists", session, "page", String.valueOf(page));
	return ResponseBuilder.buildPaginatedResult(result, Artist.class);
    }

    /**
     * Shout on this user's shoutbox
     *
     * @param user The name of the user to shout on
     * @param message The message to post to the shoutbox
     * @param session A Session instance
     * @return the result of the operation
     */
    public static Result shout(final String user, final String message, final Session session) {
	return Caller.getInstance().call("user.shout", session, "user", user, "message", message);
    }

    /**
     * Gets a list of forthcoming releases based on a user's musical taste.
     *
     * @param user The Last.fm username
     * @param apiKey A Last.fm API key
     * @return a Collection of new {@link Album} releases
     */
    public static Collection<Album> getNewReleases(final String user, final String apiKey) {
	return getNewReleases(user, false, apiKey);
    }

    /**
     * Gets a list of forthcoming releases based on a user's musical taste.
     *
     * @param user The Last.fm username
     * @param useRecommendations If <code>true</code>, the feed contains new releases based on Last.fm's artist recommendations for this user.
     * Otherwise, it is based on their library (the default)
     * @param apiKey A Last.fm API key
     * @return a Collection of new {@link Album} releases
     */
    public static Collection<Album> getNewReleases(final String user, final boolean useRecommendations, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getNewReleases", apiKey, "user", user, "userecs", useRecommendations ? "1" : "0");
	return ResponseBuilder.buildCollection(result, Album.class);
    }

    /**
     * Returns the tracks banned by the user.
     *
     * @param user The user name
     * @param apiKey A Last.fm API key
     * @return the banned tracks
     */
    public static PaginatedResult<Track> getBannedTracks(final String user, final String apiKey) {
	return getBannedTracks(user, 1, apiKey);
    }

    /**
     * Returns the tracks banned by the user.
     *
     * @param user The user name
     * @param page The page number to fetch
     * @param apiKey A Last.fm API key
     * @return the banned tracks
     */
    public static PaginatedResult<Track> getBannedTracks(final String user, final int page, final String apiKey) {
	final Result result = Caller.getInstance().call("user.getBannedTracks", apiKey, "user", user, "page", String.valueOf(page));
	return ResponseBuilder.buildPaginatedResult(result, Track.class);
    }

    /**
     * Get shouts for a user.
     *
     * @param user The username to fetch shouts for
     * @param apiKey A Last.fm API key.
     * @return a page of <code>Shout</code>s
     */
    public static PaginatedResult<Shout> getShouts(final String user, final String apiKey) {
	return getShouts(user, -1, -1, apiKey);
    }

    /**
     * Get shouts for a user.
     *
     * @param user The username to fetch shouts for
     * @param page The page number to fetch
     * @param apiKey A Last.fm API key.
     * @return a page of <code>Shout</code>s
     */
    public static PaginatedResult<Shout> getShouts(final String user, final int page, final String apiKey) {
	return getShouts(user, page, -1, apiKey);
    }

    /**
     * Get shouts for a user.
     *
     * @param user The username to fetch shouts for
     * @param page The page number to fetch
     * @param limit An integer used to limit the number of shouts returned per page or -1 for default
     * @param apiKey A Last.fm API key.
     * @return a page of <code>Shout</code>s
     */
    public static PaginatedResult<Shout> getShouts(final String user, final int page, final int limit, final String apiKey) {
	final Map<String, String> params = new HashMap<String, String>();
	params.put("user", user);
	MapUtilities.nullSafePut(params, "limit", limit);
	MapUtilities.nullSafePut(params, "page", page);
	final Result result = Caller.getInstance().call("user.getShouts", apiKey, params);
	return ResponseBuilder.buildPaginatedResult(result, Shout.class);
    }

    /**
     * Get the user's personal tags.
     *
     * @param user The user who performed the taggings
     * @param tag The tag you're interested in
     * @param taggingType Either <code>Artist.class</code>, <code>Album.class</code> or <code>Track.class</code>
     * @param apiKey A Last.fm API key
     * @return the items the user has tagged with the specified tag
     * @throws IllegalArgumentException if <code>taggingType</code> is <code>null</code> or not one of the above mentioned classes
     */
    public static <T extends MusicEntry> PaginatedResult<T> getPersonalTags(final String user, final String tag, final Class<T> taggingType, final String apiKey) {
	return getPersonalTags(user, tag, taggingType, -1, -1, apiKey);
    }

    /**
     * Get the user's personal tags.
     *
     * @param user The user who performed the taggings
     * @param tag The tag you're interested in
     * @param taggingType Either <code>Artist.class</code>, <code>Album.class</code> or <code>Track.class</code>
     * @param page The page number to fetch
     * @param apiKey A Last.fm API key
     * @return the items the user has tagged with the specified tag
     * @throws IllegalArgumentException if <code>taggingType</code> is <code>null</code> or not one of the above mentioned classes
     */
    public static <T extends MusicEntry> PaginatedResult<T> getPersonalTags(final String user, final String tag, final Class<T> taggingType, final int page, final String apiKey) {
	return getPersonalTags(user, tag, taggingType, page, -1, apiKey);
    }

    /**
     * Get the user's personal tags.
     *
     * @param user The user who performed the taggings
     * @param tag The tag you're interested in
     * @param taggingType Either <code>Artist.class</code>, <code>Album.class</code> or <code>Track.class</code>
     * @param page The page number to fetch
     * @param limit The number of results to fetch per page. Defaults to 50
     * @param apiKey A Last.fm API key
     * @return the items the user has tagged with the specified tag
     * @throws IllegalArgumentException if <code>taggingType</code> is <code>null</code> or not one of the above mentioned classes
     */
    public static <T extends MusicEntry> PaginatedResult<T> getPersonalTags(final String user, final String tag, final Class<T> taggingType, final int page, final int limit, final String apiKey) {
	final Map<String, String> params = StringUtilities.map("user", user, "tag", tag);
	MapUtilities.nullSafePut(params, "page", page);
	MapUtilities.nullSafePut(params, "limit", limit);

	final String taggingTypeParam = "taggingtype";
	if (taggingType == Track.class) {
	    params.put(taggingTypeParam, "track");
	} else if (taggingType == Artist.class) {
	    params.put(taggingTypeParam, "artist");
	} else if (taggingType == Album.class) {
	    params.put(taggingTypeParam, "album");
	} else
	    throw new IllegalArgumentException("Parameter taggingType has to be one of Artist.class, Album.class or Track.class.");

	final Result result = Caller.getInstance().call("user.getPersonalTags", apiKey, params);
	if (!result.isSuccessful())
	    return new PaginatedResult<T>(0, 0, Collections.<T> emptyList(),
		    result);

	final String childElementName = params.get(taggingTypeParam) + "s";
	final DomElement contentElement = result.getContentElement();
	final DomElement childElement = contentElement.getChild(childElementName);
	return ResponseBuilder.buildPaginatedResult(result, contentElement,
		childElement, taggingType);
    }


    private static class UserFactory implements ItemFactory<User> {
	public User createItemFromElement(final DomElement element) {
	    final User user = new User(element.getChildText("name"), element.getChildText("url"));
	    user.id = element.getChildText("id");
	    if (element.hasChild("realname")) {
		user.realname = element.getChildText("realname");
	    }
	    ImageHolder.loadImages(user, element);
	    user.language = element.getChildText("lang");
	    user.country = element.getChildText("country");
	    if (element.hasChild("age")) {
		try {
		    user.age = Integer.parseInt(element.getChildText("age"));
		} catch (final NumberFormatException e) {
		    // no age
		}
	    }
	    user.gender = element.getChildText("gender");
	    user.subscriber = "1".equals(element.getChildText("subscriber"));
	    if (element.hasChild("playcount")) { // extended user information
		try {
		    user.playcount = Integer.parseInt(element.getChildText("playcount"));
		} catch (final NumberFormatException e) {
		    // no playcount
		}
	    }
	    if (element.hasChild("playlists")) { // extended user information
		try {
		    user.numPlaylists = Integer.parseInt(element.getChildText("playlists"));
		} catch (final NumberFormatException e) {
		    // no playlists
		}
	    }
	    if (element.hasChild("registered")) {
		final String unixtime = element.getChild("registered").getAttribute("unixtime");
		user.registeredDate = new Date(Long.parseLong(unixtime) * 1000);
	    }
	    return user;
	}
    }
}
