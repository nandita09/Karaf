package com.krf.pkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.net.ssl.HttpsURLConnection;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UserList;
import twitter4j.conf.ConfigurationBuilder;
import org.json.*;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String bearer_key;
	private static HttpsURLConnection connection;
	private static URL url;

	private static String APIKey = "aHx5MvNzosI49PRXvdlpaNATk";
	private static String APIsecretkey = "KGcZkqyBfZB13OqT0Z7vzwcZAbtYevRp2tFbvi9CBbWDkmAQWy";
	private static String Accesstoken = "1045873131510845441-pYCWdxhJECHzKzTJHaj3SDL02PX0bw";
	private static String Accesstokensecret = "Y8tXXsC8KJV3tYbOfDbe1opmrpWRvPUhJkm8hbnYiLLEV";

	private String consumer_key = "aHx5MvNzosI49PRXvdlpaNATk";
	private String consumer_secret = "KGcZkqyBfZB13OqT0Z7vzwcZAbtYevRp2tFbvi9CBbWDkmAQWy";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
		super();
	}

	/***
	 * makeConnection is used to establishing Http connection
	 * 
	 * @param urlString
	 */
	public static void makeConnection(String urlString) {
		try {
			url = new URL(urlString);
			connection = (HttpsURLConnection) url.openConnection();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void application_only_auth() {
		try {
			makeConnection("https://api.twitter.com/oauth2/token");
			String consumer_key_encode = URLEncoder.encode(consumer_key, "UTF-8");
			String consumer_secret_encode = URLEncoder.encode(consumer_secret, "UTF-8");
			String encode_key = consumer_key_encode + ":" + consumer_secret_encode;
			String Base64_encode = "Basic" + " " + Base64.getEncoder().encodeToString(encode_key.getBytes());
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", Base64_encode);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write("grant_type=client_credentials");
			osw.flush();
			osw.close();
			connection.connect();
			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());
			if (connection.getResponseMessage().equals("OK")) {
				JSONObject json = inputStreamToJSON();
				bearer_key = json.getString("access_token");
				System.out.println(json.getString("token_type"));
				System.out.println(json.getString("access_token"));

			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Mapping JSON object to front-end
	 * 
	 * @return JSON object
	 * @throws IOException
	 */
	public static JSONObject inputStreamToJSON() throws IOException {
		java.io.InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer jsonLines = new StringBuffer();
		while ((line = br.readLine()) != null) {
			jsonLines.append(line);
			System.out.println(line);
		}
		br.close();
		JSONObject output = new JSONObject(jsonLines.toString());

		return output;
	}

	/***
	 *Author --> Nandita Singh
	 *Function to retrieve all Time line post 
	 * @return all Time line post
	 */
	public static String apiGetHomeTimeline() {

		String output = "";

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		try {
			twitter.getHomeTimeline();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		List<Status> statuses = null;
		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		for (Status status : statuses) {
			output = output + "<br>" + " " + "@" + status.getUser().getScreenName() + " - " + status.getText() + "\n"
					+ "</br>";
		}
		System.out.println(output);

		return output;

	}

	/***
	 * Author --> Nandita Singh
	 * Function to update Time line status
	 * @param post
	 * @return status of the post
	 */

	public static String apiUpdateStatus(String post) {

		String output = "";

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		Status status = null;
		try {
			status = twitter.updateStatus(post);
			output = output + " " + "Successfully updated the status to [" + status.getText() + "].";
			return output;
		} catch (TwitterException e) {
			e.printStackTrace();
			output = output + "<br>" + " " + "Update Failed [" + status.getText() + "]." + "</br>";
			return output;
		}

	}

	/***
	 * Author --> Madhuri Dilip Kumar
	 * Creates the UserList
	 * 
	 * @param userListName
	 * @return Userlist
	 */

	public static String apiCreateUserlist(String userListName) {

		String output = "";

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		try {
			String description = "testing lists";
			UserList list = twitter.createUserList(userListName, true, description);
			System.out.println("Successfully created a list (id:" + list.getId() + ", slug:" + list.getSlug() + ").");
			output = output + "<br>" + " " + "Successfully created a list (id:" + list.getId() + ", slug:"
					+ list.getSlug() + ")." + "</br>";
		} catch (TwitterException te) {
			te.printStackTrace();
			output = output + " " + "Failed to create a list: " + te.getMessage();
		}
		return output;
	}

	/***
	 * Author --> Madhuri Dilip Kumar
	 * Deletes the Userlist
	 * 
	 * @param userListName
	 * @return Userlist
	 */

	public static String apiDestroyUserlist(String userListName) {

		String output = "";

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);

		try {
			twitter4j.User user = twitter.verifyCredentials();
			ResponseList<UserList> lists = twitter.getUserLists(user.toString());
			for (UserList list : lists) {
				if (list.getName().equals(userListName)) {
					UserList blist = twitter.destroyUserList(list.getId());
					System.out.println(
							"Successfully deleted the list (id:" + blist.getId() + ", slug:" + blist.getSlug() + ").");

					output = output + "<br>" + " " + "Successfully deleted the list (id:" + blist.getId() + ", slug:"
							+ blist.getSlug() + ")." + "</br>";

				}
			}

		} catch (TwitterException te) {
			te.printStackTrace();
			output = output + "<br>" + " " + "Failed to delete a list: " + te.getMessage() + "</br>";

		}
		return output;
	}
	
	/***
	 * Author --> Yagna Priya Damodaran
	 * Function to list UserLists
	 * @return UserLists
	 */

	public static String apiGetUserlist() {

		String output = "";

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);

		try {
			twitter4j.User user = twitter.verifyCredentials();
			ResponseList<UserList> lists = twitter.getUserLists(user.toString());
			for (UserList list : lists) {
				output = output + "<br>" + " " + "id:" + list.getId() + ", name:" + list.getName() + ", description:"
						+ list.getDescription() + ", slug:" + list.getSlug() + "" + "\n" + "</br>";
				System.out.println(output);

			}

		} catch (TwitterException te) {
			te.printStackTrace();
			output = output + "<br>" + " " + "Failed to list the User list: " + "\n" + te.getMessage() + "</br>";

		}
		return output;
	}

	/***
	 * Author --> Yagna Priya Damodaran
	 * Search of the Tweets having the keyword string
	 * @param queryName
	 * @return Tweets having the keyword string
	 */

	public static List<Status> searchQuerys(String queryName) {

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		Query query = new Query(queryName);
		QueryResult result;
		System.out.println("queryName:" + queryName);
		System.out.println("query:" + query);
		List<Status> tweets = null;
		try {
			result = twitter.search(query);
			tweets = result.getTweets();
			System.out.println(tweets);
			return tweets;
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}

	}

	/***
	 * Author --> Radhika Reddy Maduri
	 * Displays the twitter Privacy policy
	 * 
	 * @return Twitter Privacy policy
	 */

	public static String getPrivacyPolicy() {
		String output = null;

		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		String policy = null;
		try {
			policy = twitter.getPrivacyPolicy();
			System.out.println("policy: " + policy);
		} catch (TwitterException e) {
			e.printStackTrace();
			output = output + "<br>" + " " + "Failed to retrieve privacy policy: " + e.getMessage() + "</br>";
		}

		return policy;
	}

	/***
	 * Author --> Radhika Reddy Maduri
	 * Displays the different user categories
	 * @return different user categories
	 */

	public static ResponseList<twitter4j.Category> getSuggestedUserCategories() {
		Twitter twitter = initTwitter(APIKey, APIsecretkey, Accesstoken, Accesstokensecret);
		ResponseList<twitter4j.Category> categories = null;
		try {
			categories = twitter.getSuggestedUserCategories();
			for (twitter4j.Category c : categories) {
				System.out.println(c.getName() + ":" + c.getSlug());
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return categories;
	}

	/***
	 * Function to establish Twitter connection using OAuth
	 * 
	 * @param APIKey
	 * @param APIsecretkey
	 * @param Accesstoken
	 * @param Accesstokensecret
	 * @return Twitter Instance
	 */

	public static Twitter initTwitter(String APIKey, String APIsecretkey, String Accesstoken,
			String Accesstokensecret) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(APIKey).setOAuthConsumerSecret(APIsecretkey)
				.setOAuthAccessToken(Accesstoken).setOAuthAccessTokenSecret(Accesstokensecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

}
