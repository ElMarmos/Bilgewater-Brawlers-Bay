package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.BlackMarketChampion;
import models.BrawlersHired;
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

	/**
	 * Return JSON with the statics of the champion
	 * @param id - the champion id on the database
	 * @return Json as string
	 */
	public static String getChampionResume(String id) {
		ChampionStatics cs = ChampionStatics.find("byId", Long.parseLong(id))
				.first();
		int championId = cs.champion_id;
		DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
	    simbolo.setDecimalSeparator('.');
	    simbolo.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("#0.00", simbolo); 
		
		//General stats of champion
		List<Champion> champStats = Champion.find("byChampion_id", championId).fetch();
		int avgKills = 0;
		int avgDeaths = 0;
		int avgAssists = 0;
		double games = 0;
		double avgWins = 0;
		for (Champion champion : champStats) {
			avgKills+=champion.kills;
			avgDeaths+=champion.deaths;
			avgAssists+=champion.assists;
			games+=champion.games;
			avgWins+=champion.wins;
		}
		avgKills = (int) (avgKills/games);
		avgDeaths = (int) (avgDeaths/games);
		avgAssists = (int) (avgAssists/games);
		avgWins = (avgWins/games)*100;
		games = (games/200000)*100;
		String play = df.format(games);
		String wins = df.format(avgWins);
		//General stats of champion
		
		//Brawlers stats of champion
		
		int totalBrawlers = 0;
		double avgIronbacks = 0;
		double avgIronBacks_win = 0;
		double avgOcklepods = 0;
		double avgOcklepods_win = 0;
		double avgPlundercrabs = 0;
		double avgPlundercrabs_win = 0;
		double avgRazorfins = 0;
		double avgRazorfins_win = 0;
		List<BrawlersHired> champBrawlers = BrawlersHired.find("byChampion_id", championId).fetch();
		for (BrawlersHired brawlerHired : champBrawlers) {
			totalBrawlers+=brawlerHired.ironbacks;
			totalBrawlers+=brawlerHired.ocklepods;
			totalBrawlers+=brawlerHired.plundercrabs;
			totalBrawlers+=brawlerHired.razorfins;
			avgIronbacks+=brawlerHired.ironbacks;
			avgIronBacks_win+=brawlerHired.ironbacks_win;
			avgOcklepods+=brawlerHired.ocklepods;
			avgOcklepods_win+=brawlerHired.ocklepods_win;
			avgPlundercrabs+=brawlerHired.plundercrabs;
			avgPlundercrabs_win+=brawlerHired.plundercrabs_win;
			avgRazorfins+=brawlerHired.razorfins;
			avgRazorfins_win+=brawlerHired.razorfins_win;
		}
		avgIronBacks_win = (avgIronBacks_win/avgIronbacks)*100;
		avgIronbacks = (avgIronbacks/totalBrawlers)*100;
		avgOcklepods_win = (avgOcklepods_win/avgOcklepods)*100;
		avgOcklepods = (avgOcklepods/totalBrawlers)*100;
		avgPlundercrabs_win = (avgPlundercrabs_win/avgPlundercrabs)*100;
		avgPlundercrabs = (avgPlundercrabs/totalBrawlers)*100;
		avgRazorfins_win = (avgRazorfins_win/avgRazorfins)*100;
		avgRazorfins = (avgRazorfins/totalBrawlers)*100; 
		
		String ironBacks = df.format(avgIronbacks);
		String ironBacks_win = df.format(avgIronBacks_win);
		String ocklepods = df.format(avgOcklepods);
		String ocklepods_win = df.format(avgOcklepods_win);
		String plundercrabs = df.format(avgPlundercrabs);
		String plundercrabs_win = df.format(avgIronBacks_win);
		String razorfins = df.format(avgRazorfins);
		String razorfins_win = df.format(avgRazorfins_win);
		
		//Brawlers stats of champion
		String json = "{"
							+ "\"name\":\""+cs.name+"\","
							+ "\"square\":\""+cs.square+"\","
							+ "\"kills\":"+avgKills+","
							+ "\"deaths\":"+avgDeaths+","
							+ "\"assists\":"+avgAssists+","
							+ "\"wins\":\""+wins+"\","
							+ "\"play\":\""+play+"\","
							+ "\"ironbacks\":"+ironBacks+","
							+ "\"ironbacks_win\":"+ironBacks_win+","
							+ "\"ocklepods\":"+ocklepods+","
							+ "\"ocklepods_win\":"+ocklepods_win+","
							+ "\"plundercrabs\":"+plundercrabs+","
							+ "\"plundercrabs_win\":"+plundercrabs_win+","
							+ "\"razorfins\":"+razorfins+","
							+ "\"razorfins_win\":"+razorfins_win
					+ "}";	
		return json;
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
