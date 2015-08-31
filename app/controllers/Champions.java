package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

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
	 * Gets the ids, names and keys from all champions using the RG API, then
	 * creates and stores the generated data in the database.
	 * 
	 * @return nothing
	 */
	public static String getChamps() {
		HttpResponse homePage = WS
				.url("https://global.api.pvp.net/api/lol/static-data/lan/v1.2/champion?dataById=true&champData=image&api_key=")
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
	
	/**
	 * Return JSON with the statics of the champion
	 * 
	 * @param id
	 *            - the champion id on the database
	 * @return Json as string
	 */
	public static String getChampionResume(String id) {
		ChampionStatics cs = ChampionStatics.find("byChampion_id", Integer.parseInt(id)).first();
		int championId = Integer.parseInt(id);
		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("#0.00", simbolo);

		// General stats of champion
		List<Champion> champStats = Champion.find("byChampion_id", championId).fetch();
		int avgKills = 0;
		int avgDeaths = 0;
		int avgAssists = 0;
		double games = 0;
		double avgWins = 0;
		for (Champion champion : champStats) {
			avgKills += champion.kills;
			avgDeaths += champion.deaths;
			avgAssists += champion.assists;
			games += champion.games;
			avgWins += champion.wins;
		}
		avgKills = (int) (avgKills / games);
		avgDeaths = (int) (avgDeaths / games);
		avgAssists = (int) (avgAssists / games);
		avgWins = (avgWins / games) * 100;
		games = (games / 200000) * 100;
		String play = df.format(games);
		String wins = df.format(avgWins);
		// General stats of champion

		// Brawlers stats of champion

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
			totalBrawlers += brawlerHired.ironbacks;
			totalBrawlers += brawlerHired.ocklepods;
			totalBrawlers += brawlerHired.plundercrabs;
			totalBrawlers += brawlerHired.razorfins;
			avgIronbacks += brawlerHired.ironbacks;
			avgIronBacks_win += brawlerHired.ironbacks_win;
			avgOcklepods += brawlerHired.ocklepods;
			avgOcklepods_win += brawlerHired.ocklepods_win;
			avgPlundercrabs += brawlerHired.plundercrabs;
			avgPlundercrabs_win += brawlerHired.plundercrabs_win;
			avgRazorfins += brawlerHired.razorfins;
			avgRazorfins_win += brawlerHired.razorfins_win;
		}
		avgIronBacks_win = (avgIronBacks_win / avgIronbacks) * 100;
		avgIronbacks = (avgIronbacks / totalBrawlers) * 100;
		avgOcklepods_win = (avgOcklepods_win / avgOcklepods) * 100;
		avgOcklepods = (avgOcklepods / totalBrawlers) * 100;
		avgPlundercrabs_win = (avgPlundercrabs_win / avgPlundercrabs) * 100;
		avgPlundercrabs = (avgPlundercrabs / totalBrawlers) * 100;
		avgRazorfins_win = (avgRazorfins_win / avgRazorfins) * 100;
		avgRazorfins = (avgRazorfins / totalBrawlers) * 100;

		String ironBacks = df.format(avgIronbacks);
		String ironBacks_win = df.format(avgIronBacks_win);
		String ocklepods = df.format(avgOcklepods);
		String ocklepods_win = df.format(avgOcklepods_win);
		String plundercrabs = df.format(avgPlundercrabs);
		String plundercrabs_win = df.format(avgIronBacks_win);
		String razorfins = df.format(avgRazorfins);
		String razorfins_win = df.format(avgRazorfins_win);

		// Brawlers stats of champion
		String json = "{" + "\"name\":\"" + cs.name + "\"," + "\"square\":\"" + cs.square + "\"," + "\"kills\":"
				+ avgKills + "," + "\"deaths\":" + avgDeaths + "," + "\"assists\":" + avgAssists + "," + "\"wins\":\""
				+ wins + "\"," + "\"play\":\"" + play + "\"," + "\"ironbacks\":" + ironBacks + ","
				+ "\"ironbacks_win\":" + ironBacks_win + "," + "\"ocklepods\":" + ocklepods + "," + "\"ocklepods_win\":"
				+ ocklepods_win + "," + "\"plundercrabs\":" + plundercrabs + "," + "\"plundercrabs_win\":"
				+ plundercrabs_win + "," + "\"razorfins\":" + razorfins + "," + "\"razorfins_win\":" + razorfins_win
				+ "}";
		return json;
	}

	/**
	 * Return JSON with the relevant black market´s item buy rate
	 * 
	 * @param id
	 *            - the champion id on the database
	 * @param region
	 *            - the region id on the database
	 * @return Json as string
	 */
	public static String getRelevantBMItemWin(String id, String name) {
		
		String regionId = (BigInteger) RegionTotal.em()
				.createNativeQuery("SELECT id FROM regiontotal WHERE region ='" + name + "'").getSingleResult() + "";
		

		Query q = BlackMarketChampion.em().createNativeQuery(
				"SELECT * FROM blackmarketchampion where champion_id =" + id + "and regiontotal_id =" + regionId);
		Object[] bmc = (Object[]) q.getSingleResult();
		

		class ItemBlackMarket implements Comparable<ItemBlackMarket> {
			private String name;
			private double count;
			private int id;
			private int count2;

			public ItemBlackMarket(String n, double c, int i,int c2) {
				name = n;
				count = c;
				id = i;
				count2=c2;
			}

			@Override
			public int compareTo(ItemBlackMarket o) {
				// TODO Auto-generated method stub
				return Integer.compare(this.count2, o.count2); 
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return name + "";
			}

			public String getName() {
				return name;
			}

			public double getCount() {
				return count;
			}

			public int getId() {
				return id;
			}
			
			public int getCount2(){
				return count2;
			}

		}
		
			
		int poxArcana = (int) bmc[1];		
		int deathMansPlate = (int) bmc[4];		
		int flesHeater = (int) bmc[6];		
		int globeOfTrust = (int) bmc[8];		
		int lostChapter = (int) bmc[10];		
		int martyrsGambit = (int) bmc[12];		
		int mirageBlade = (int) bmc[14];		
		int murkSphere = (int) bmc[16];		
		int netherStrideGrimoire = (int) bmc[18];		
		int puppeteer = (int) bmc[20];		
		int riteOfRuin = (int) bmc[22];		
		int staffOfFlowingWater = (int) bmc[24];		
		int swinglersOrb = (int) bmc[26];		
		int typhoonClaws = (int) bmc[28];		
		int tricktersGlass = (int) bmc[30];
		
		
		
		int poxArcanaWin = (int) bmc[2];		
		int deathMansPlateWin = (int) bmc[5];		
		int flesHeaterWin = (int) bmc[7];		
		int globeOfTrustWin = (int) bmc[9];		
		int lostChapterWin = (int) bmc[11];
		int martyrsGambitWin = (int) bmc[13];		
		int mirageBladeWin = (int) bmc[15];		
		int murkSphereWin = (int) bmc[17];		
		int netherStrideGrimoireWin = (int) bmc[19];		
		int puppeteerWin = (int) bmc[21];		
		int riteOfRuinWin = (int) bmc[23];		
		int staffOfFlowingWaterWin = (int) bmc[25];		
		int swinglersOrbWin = (int) bmc[27];		
		int typhoonClawsWin = (int) bmc[29];		
		int tricktersGlassWin = (int) bmc[31];
		
		ItemBlackMarket poxArcanaW = new ItemBlackMarket("Pox Arcana", poxArcana == 0 ? 0 :(double)poxArcanaWin/poxArcana*100, 3434,poxArcanaWin);		
		ItemBlackMarket deathMansPlateW = new ItemBlackMarket("Dead Man's Plate", deathMansPlate == 0 ? 0 :(double)deathMansPlateWin/deathMansPlate*100, 3742,deathMansPlateWin);		
		ItemBlackMarket fleshEaterW = new ItemBlackMarket("Flesheater", flesHeater == 0 ? 0 :(double)flesHeaterWin/flesHeater*100, 3924,flesHeaterWin);		
		ItemBlackMarket globeOfTrustW = new ItemBlackMarket("Globe of Trust", globeOfTrust == 0 ? 0 :(double)globeOfTrustWin/globeOfTrust*100, 3840,globeOfTrustWin);		
		ItemBlackMarket lostChapterW = new ItemBlackMarket("Lost Chapter", lostChapter == 0 ? 0 :(double)lostChapterWin/lostChapter*100, 3433,lostChapterWin);		
		ItemBlackMarket martyrsGambitW = new ItemBlackMarket("Martyr's Gambit", martyrsGambit == 0 ? 0 :(double)martyrsGambitWin/martyrsGambit*100, 3911,martyrsGambitWin);		
		ItemBlackMarket mirageBladeW = new ItemBlackMarket("Mirage Blade", mirageBlade == 0 ? 0 :(double)mirageBladeWin/mirageBlade*100, 3150,mirageBladeWin);		
		ItemBlackMarket murkSphereW = new ItemBlackMarket("Murksphere", murkSphere == 0 ? 0 :(double)murkSphereWin/murkSphere*100, 3844,murkSphereWin);		
		ItemBlackMarket netherStrideGrimoireW = new ItemBlackMarket("Netherstride Grimoire", netherStrideGrimoire == 0 ? 0 :(double)netherStrideGrimoireWin/netherStrideGrimoire*100,3431,netherStrideGrimoireWin);		
		ItemBlackMarket puppeteerW = new ItemBlackMarket("Puppeteer", puppeteer == 0 ? 0 :(double)puppeteerWin/puppeteer*100, 3745,puppeteerWin);		
		ItemBlackMarket riteOfRuinW = new ItemBlackMarket("Rite of Ruin", riteOfRuin == 0 ? 0 :(double)riteOfRuinWin/riteOfRuin*100, 3430,riteOfRuinWin);	
		ItemBlackMarket staffOfFlowingWaterW = new ItemBlackMarket("Staff of Flowing Water", staffOfFlowingWater == 0 ? 0 :(double)staffOfFlowingWaterWin/staffOfFlowingWater*100,3744,staffOfFlowingWaterWin);		
		ItemBlackMarket swinglersOrbW = new ItemBlackMarket("Swindler's Orb", swinglersOrb == 0 ? 0 :(double)swinglersOrbWin/swinglersOrb*100, 3841,swinglersOrbWin);		
		ItemBlackMarket typhoonClawsW = new ItemBlackMarket("Typhoon Claws", typhoonClaws == 0 ? 0 :(double)typhoonClawsWin/typhoonClaws*100, 3652,typhoonClawsWin);		
		ItemBlackMarket tricktersGlassW = new ItemBlackMarket("Trickster's Glass", tricktersGlass == 0 ? 0 :(double)tricktersGlassWin/tricktersGlass*100, 3829,tricktersGlassWin);	
		
		ArrayList <ItemBlackMarket> winItem = new ArrayList<ItemBlackMarket>();

		winItem.add(poxArcanaW);
		winItem.add(deathMansPlateW);
		winItem.add(fleshEaterW);
		winItem.add(globeOfTrustW);
		winItem.add(lostChapterW);
		winItem.add(martyrsGambitW);
		winItem.add(mirageBladeW);
		winItem.add(murkSphereW);
		winItem.add(netherStrideGrimoireW);
		winItem.add(puppeteerW);
		winItem.add(riteOfRuinW);
		winItem.add(staffOfFlowingWaterW);
		winItem.add(swinglersOrbW);
		winItem.add(typhoonClawsW);
		winItem.add(tricktersGlassW);
				
		Collections.sort(winItem,Collections.reverseOrder());
		
		JsonArray  ja=new JsonArray();
		
		for (int i = 0; i < 4; i++) {
			
			ItemBlackMarket it=winItem.get(i);
			
			JsonObject js = new JsonObject();
			js.addProperty("id", it.id);
			js.addProperty("name", it.name);
			js.addProperty("count", it.count);			
			ja.add(js);
			
		}
		
		

		return ja.toString();
	}

	/**
	 * Return JSON with the relevant black market´s item buy rate
	 * 
	 * @param id
	 *            - the champion id on the database
	 * @param region
	 *            - the region id on the database
	 * @return Json as string
	 */
	public static String getRelevantBMItemBuy(String id, String name) {
	
		String regionId = (BigInteger) RegionTotal.em()
				.createNativeQuery("SELECT id FROM regiontotal WHERE region ='" + name + "'").getSingleResult() + "";
		

		Query q = BlackMarketChampion.em().createNativeQuery(
				"SELECT * FROM blackmarketchampion where champion_id =" + id + "and regiontotal_id =" + regionId);
		Object[] bmc = (Object[]) q.getSingleResult();
	

		class ItemBlackMarket implements Comparable<ItemBlackMarket> {
			private String name;
			private double count;
			private int id;

			public ItemBlackMarket(String n, double c, int i) {
				name = n;
				count = c;
				id = i;
			}

			@Override
			public int compareTo(ItemBlackMarket o) {
				// TODO Auto-generated method stub
				return Double.compare(this.count, o.count);
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return name + "";
			}

			public String getName() {
				return name;
			}

			public double getCount() {
				return count;
			}

			public int getId() {
				return id;
			}

		}
		int poxArcana = (int) bmc[1];		
		int deathMansPlate = (int) bmc[4];		
		int flesHeater = (int) bmc[6];		
		int globeOfTrust = (int) bmc[8];		
		int lostChapter = (int) bmc[10];		
		int martyrsGambit = (int) bmc[12];		
		int mirageBlade = (int) bmc[14];		
		int murkSphere = (int) bmc[16];		
		int netherStrideGrimoire = (int) bmc[18];		
		int puppeteer = (int) bmc[20];		
		int riteOfRuin = (int) bmc[22];		
		int staffOfFlowingWater = (int) bmc[24];		
		int swinglersOrb = (int) bmc[26];		
		int typhoonClaws = (int) bmc[28];		
		int tricktersGlass = (int) bmc[30];
		
		int sum=poxArcana+deathMansPlate+flesHeater+globeOfTrust+lostChapter+martyrsGambit+mirageBlade+murkSphere+netherStrideGrimoire+puppeteer+riteOfRuin+staffOfFlowingWater+swinglersOrb+typhoonClaws+tricktersGlass;
				

		ItemBlackMarket poxArcanaI = new ItemBlackMarket("Pox Arcana",(double) poxArcana/sum*100, 3434);		
		ItemBlackMarket deathMansPlateI = new ItemBlackMarket("Dead Man's Plate", (double)deathMansPlate/sum*100, 3742);		
		ItemBlackMarket fleshEaterI = new ItemBlackMarket("Flesheater", (double)flesHeater/sum*100, 3924);		
		ItemBlackMarket globeOfTrustI = new ItemBlackMarket("Globe of Trust", (double)globeOfTrust/sum*100, 3840);		
		ItemBlackMarket lostChapterI = new ItemBlackMarket("Lost Chapter", (double)lostChapter/sum*100, 3433);		
		ItemBlackMarket martyrsGambitI = new ItemBlackMarket("Martyr's Gambit", (double)martyrsGambit/sum*100, 3911);		
		ItemBlackMarket mirageBladeI = new ItemBlackMarket("Mirage Blade", (double)mirageBlade/sum*100, 3150);		
		ItemBlackMarket murkSphereI = new ItemBlackMarket("Murksphere", (double)murkSphere/sum*100, 3844);		
		ItemBlackMarket netherStrideGrimoireI = new ItemBlackMarket("Netherstride Grimoire", (double)netherStrideGrimoire/sum*100,3431);
		ItemBlackMarket puppeteerI = new ItemBlackMarket("Puppeteer", (double)puppeteer/sum*100, 3745);		
		ItemBlackMarket riteOfRuinI = new ItemBlackMarket("Rite of Ruin", (double)riteOfRuin/sum*100, 3430);		
		ItemBlackMarket staffOfFlowingWaterI = new ItemBlackMarket("Staff of Flowing Water", (double)staffOfFlowingWater/sum*100, 3744);		
		ItemBlackMarket swinglersOrbI = new ItemBlackMarket("Swindler's Orb", (double)swinglersOrb/sum*100, 3841);		
		ItemBlackMarket typhoonClawsI = new ItemBlackMarket("Typhoon Claws", (double)typhoonClaws/sum*100, 3652);
		ItemBlackMarket tricktersGlassI = new ItemBlackMarket("Trickster's Glass", (double)tricktersGlass/sum*100, 3829);
		

		ArrayList <ItemBlackMarket> buyItem = new ArrayList<ItemBlackMarket>();

		buyItem.add(poxArcanaI);
		buyItem.add(deathMansPlateI);
		buyItem.add(fleshEaterI);
		buyItem.add(globeOfTrustI);
		buyItem.add(lostChapterI);
		buyItem.add(martyrsGambitI);
		buyItem.add(mirageBladeI);
		buyItem.add(murkSphereI);
		buyItem.add(netherStrideGrimoireI);
		buyItem.add(puppeteerI);
		buyItem.add(riteOfRuinI);
		buyItem.add(staffOfFlowingWaterI);
		buyItem.add(swinglersOrbI);
		buyItem.add(typhoonClawsI);
		buyItem.add(tricktersGlassI);

		
		
		Collections.sort(buyItem,Collections.reverseOrder());
		
		JsonArray  ja=new JsonArray();
		
		for (int i = 0; i < 4; i++) {
			
			ItemBlackMarket it=buyItem.get(i);
			
			JsonObject js = new JsonObject();
			js.addProperty("id", it.id);
			js.addProperty("name", it.name);
			js.addProperty("count", it.count);			
			ja.add(js);
			
		}

		return ja.toString();
		
	}

	
}
