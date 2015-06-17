package com.projetoles.model;

import java.text.ParseException;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.projetoles.dao.POST;

public class UserMapper extends Mapper {

/*	@SuppressLint("SimpleDateFormat") 
	private static Set<Favorite> getFavorites(JSONArray arrayFavorites) throws JSONException, ParseException {
		Set<Favorite> favorites = new HashSet<Favorite>();
		for (int i = 0; i < arrayFavorites.length(); i++) {
			JSONObject favoriteObj = arrayFavorites.getJSONObject(i);
			Long productId = Long.parseLong(favoriteObj.getString("product_id"));
			String formattedDate = DateUtility.getFormattedDate(Long.parseLong(favoriteObj.getString("date")));
			
			favorites.add(new Favorite(productId, formattedDate));
		}
		return favorites;
	}*/
	
	public static User mapJsonToUser(JSONObject obj) throws JSONException, ParseException {
		String login = obj.getString("login");
		String email = obj.getString("email");
		String name = obj.getString("name");
		String password = obj.getString("password");
		//Gender gender = Gender.getGender(obj.getString("gender"));
		//String photoUrl = obj.getString("photo_url");
		
		//Set<Long> shops = getSetFromJSON(obj, "shops");
		//JSONArray jsonArray = obj.getJSONArray("favorites");
		
		User userFound = new User(login, email, name, password);
		return userFound;
	}
	
	public static POST.Builder mapUserToRequest(User user) {
		POST.Builder postRequest = (POST.Builder) new POST.Builder()
				.addParam("login", user.getLogin())
				.addParam("email", user.getEmail())
				.addParam("name", user.getName() == null ? "" : user.getName())
				.addParam("password", user.getPassword());
				//.addParam("gender", user.getGender() == null ? "" : user.getGender().toString());
		return postRequest;
	}
	
}
