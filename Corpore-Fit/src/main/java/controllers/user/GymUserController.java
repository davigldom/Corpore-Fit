package controllers.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GymRatingService;
import services.GymService;
import services.UserService;
import controllers.AbstractController;
import domain.Gym;
import domain.GymRating;
import domain.User;

@Controller
@RequestMapping("/gym/user")
public class GymUserController extends AbstractController {

	@Autowired
	private GymService gymService;

	@Autowired
	private UserService userService;

	@Autowired
	private GymRatingService gymRatingService;

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Gym gym;
		Collection<GymRating> grGym;
		double averageRating = 0.0;
		boolean hasGymUser = true;
		boolean alreadyRated = false;

		User principal;
		principal = this.userService.findByPrincipal();

		if (principal.getSubscription() == null) {
			hasGymUser = false;
			gym = null;
		} else {
			gym = principal.getSubscription().getGym();
			grGym = new ArrayList<GymRating>(gym.getGymRatings());
			final Collection<GymRating> grUser = new ArrayList<GymRating>(
					principal.getGymRatings());
			double allRatings = 0.0;
			for (final GymRating g : grGym) {
				allRatings += g.getRating();
				averageRating = allRatings / grGym.size();
				if (grUser.contains(g))
					alreadyRated = true;
			}
		}

		// Check if the user is subscribed to a gym and if he is subscribed to
		// the gym displayed
		boolean isSubscribed = false;
		boolean hasSubscription = false;
		if (principal.getSubscription() != null) {
			hasSubscription = true;
			if (principal.getSubscription().getGym().equals(gym))
				isSubscribed = true;
		}

		result = new ModelAndView("gym/display");
		if (gym != null)
			result.addObject("gym", gym);

		result.addObject("hasGymUser", hasGymUser);
		result.addObject("principal", principal);
		result.addObject("averageRating", averageRating);
		result.addObject("alreadyRated", alreadyRated);
		result.addObject("isSubscribed", isSubscribed);
		result.addObject("hasSubscription", hasSubscription);

		return result;
	}

	@RequestMapping(value = "/rate", method = RequestMethod.GET)
	public ModelAndView rate(@RequestParam final int gymId, final int rating) {
		ModelAndView result;
		final Gym gym = this.gymService.findOne(gymId);
		final User principal = this.userService.findByPrincipal();

		final GymRating gr = this.gymRatingService.create();
		gr.setRating(rating);

		final Collection<GymRating> grUser = new ArrayList<GymRating>(
				principal.getGymRatings());
		final Collection<GymRating> grGym = new ArrayList<GymRating>(
				gym.getGymRatings());
		boolean alreadyRated = false;

		for (final GymRating g : grUser)
			if (grGym.contains(g))
				alreadyRated = true;

		Assert.isTrue(!alreadyRated);

		Assert.isTrue(principal.getSubscription().getGym().equals(gym));

		final GymRating saved = this.gymRatingService.save(gr);

		this.gymService.saveRating(gym, saved);

		this.userService.saveRating(principal, saved);

		result = new ModelAndView("redirect:/gym/display.do?gymId=" + gymId);

		return result;
	}

	@RequestMapping("/list-close")
	public ModelAndView list() throws IOException {
		ModelAndView result;

		Map<Gym, String> gymsDistance = new HashMap<Gym, String>();
		
		ArrayList<Gym> gyms2 = (ArrayList<Gym>) this.gymService.findAll();		
		ArrayList<String> values = new ArrayList<String>();
		
		final ArrayList<Gym> gyms = (ArrayList<Gym>) this.gymService.findAll();

		for (int i = 0; i < gyms.size(); i++) {

			URL url = new URL(
					"https://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ URLEncoder.encode(this.userService
									.findByPrincipal().getAddress(), "UTF-8")
							+ "&destinations="
							+ URLEncoder.encode(gyms.get(i).getAddress(),
									"UTF-8")
							+ "&key=AIzaSyA0ldlMQPJcOllaG902itsqprRM-JXH9Wc");

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");

			InputStream is = connection.getInputStream();
			StringBuilder sb = new StringBuilder();

			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			try {
				JSONObject json = new JSONObject(sb.toString());
				JSONArray rows = (JSONArray) json.get("rows");
				JSONObject elements = rows.getJSONObject(0);
				JSONArray elements2 = (JSONArray) elements.get("elements");
				JSONObject elements3 = elements2.getJSONObject(0);
				JSONObject distance = (JSONObject) elements3.get("distance");

				gymsDistance.put(gyms.get(i), distance.get("text").toString());
				gyms2.add(gyms.get(i));
				values.add(distance.get("text").toString());
			} catch (Exception e) {
				gymsDistance.put(gyms.get(i), "");
			}
		}

//		Map<Gym, String> gymsOrdered = new HashMap<Gym, String>();
//
//		int i = 0;
//		for (Gym gym : gymsDistance.keySet()) {
//			if (i == 3)
//				break;
//			String value = gymsDistance.get(gym);
//
//			for (String string : gymsOrdered.values()) {
//				if (Integer.valueOf(value) > Integer.valueOf(string))
//					value = string;
//			}
//
//			gymsOrdered.put(gym, value);
//			gymsDistance.remove(gym);
//			
//			i++;
//
//		}

		result = new ModelAndView("gym/list-close");
		result.addObject("gyms2", gymsDistance);
		result.addObject("gyms", gyms2);
		result.addObject("values", values);
		result.addObject("requestURI", "/gym/user/list-close.do");

		return result;
	}

}
