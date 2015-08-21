package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.BlackMarketChampion;
import models.Champion;
import models.ChampionStatics;
import models.RegionTotal;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.*;

public class Champions extends Controller {

	/**
	 * Displays champions view index.
	 */
	public static void index() {
		List<ChampionStatics> campeones = ChampionStatics.find("order by name")
				.fetch();
		render(campeones);
	}

	public static String getChampionResume(String id) {
		ChampionStatics cs = ChampionStatics.find("byId", Long.parseLong(id))
				.first();
		return cs.square;
	}

	/**
	 * Gets the ids, names and keys from all champions using the RG API, then
	 * creates and stores the generated data in the database.
	 * 
	 * @return nothing
	 */
	public static String getChamps() {
		HttpResponse homePage = WS
				.url("https://global.api.pvp.net/api/lol/static-data/lan/v1.2/champion?dataById=true&champData=image&api_key=33b1e8e7-58e3-4e15-a8bb-157f50a879a5")
				.get();
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(homePage.getString());

		JsonElement data = obj.get("data");

		for (Map.Entry<String, JsonElement> entry : ((JsonObject) data)
				.entrySet()) {
			String id = (entry.getValue().getAsJsonObject().get("id"))
					.toString();
			String name = (entry.getValue().getAsJsonObject().get("name"))
					.toString().replace("\"", "");
			String key = (entry.getValue().getAsJsonObject().get("key"))
					.toString().replace("\"", "");

			String urlLoading = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/"
					+ key + "_0.jpg";
			String urlSquare = "http://ddragon.leagueoflegends.com/cdn/5.15.1/img/champion/"
					+ key + ".png";

			ChampionStatics cs = new ChampionStatics();
			cs.champion_id = Integer.parseInt(id);
			cs.name = name;
			cs.loading = urlLoading;
			cs.square = urlSquare;
			cs.save();
		}
		return "";
	}

	
}
