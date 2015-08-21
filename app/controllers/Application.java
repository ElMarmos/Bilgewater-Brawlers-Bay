package controllers;

import play.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.*;

public class Application extends Controller {

	public static void index() {
		populateDb();
		render();
	}

	public static String populateDb() {

		try {
			RegionTotal r = new RegionTotal();
			r.region = "LAN";
			r.region_id = "LA1";
			r.save();
			System.out.println("save");

			BufferedReader br = new BufferedReader(new FileReader(
					"./public/LAN.json"));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				System.out.println("van " + i);
				setMatch(line, r);
				i++;
			}

			br.close();
			return "";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static String setMatch(String matchId, RegionTotal r) {
		HttpResponse homePage = WS
				.url("https://lan.api.pvp.net/api/lol/lan/v2.2/match/"
						+ matchId
						+ "?includeTimeline=true&api_key=c23d24ee-d074-4000-a7f0-4711741570c4")
				.get();
		JsonParser parser = new JsonParser();
		try {
			Thread.sleep(1000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		// System.out.println(homePage.getString());
		JsonObject obj = (JsonObject) parser.parse(homePage.getString());
		setMatchAvg(obj, r);
		setKrakensAvg(obj, r);
		setBlackMarketChampion(obj, r);
		return "";
	}

	public static String setMatchAvg(JsonObject obj, RegionTotal r) {
		double AvgDuration = obj.get("matchDuration").getAsDouble() / 100000;
		double AvgTowerKills = 0;
		double AvgKills = 0;
		double AvgAssists = 0;
		double AvgMinionsKilled = 0;
		double AvgNeutralMinionsKilled = 0;
		double AvgGoldEarned = 0;
		double AvgDoubleKills = 0;
		double AvgTripleKills = 0;
		double AvgQuadraKills = 0;
		double AvgPentaKills = 0;
		double AvgDragon = 0;
		double AvgBaron = 0;
		double AvgDeaths = 0;

		JsonArray teams = obj.getAsJsonArray("teams");
		for (int i = 0; i < teams.size(); i++) {
			AvgTowerKills += teams.get(i).getAsJsonObject().get("towerKills")
					.getAsDouble() / 2;
			AvgDragon += teams.get(i).getAsJsonObject().get("dragonKills")
					.getAsDouble() / 2;
			AvgBaron += teams.get(i).getAsJsonObject().get("baronKills")
					.getAsDouble() / 2;
		}
		AvgTowerKills = AvgTowerKills / 10000;
		AvgDragon = AvgDragon / 10000;
		AvgBaron = AvgBaron / 10000;

		JsonArray participants = obj.getAsJsonArray("participants");
		for (int i = 0; i < participants.size(); i++) {

			JsonObject stats = participants.get(i).getAsJsonObject()
					.get("stats").getAsJsonObject();
			AvgKills += stats.get("kills").getAsDouble() / 100000;
			AvgAssists += stats.get("assists").getAsDouble() / 100000;
			AvgMinionsKilled += stats.get("minionsKilled").getAsDouble() / 100000;
			AvgNeutralMinionsKilled += stats.get("neutralMinionsKilled")
					.getAsDouble() / 100000;
			AvgGoldEarned += stats.get("goldEarned").getAsDouble() / 100000;
			AvgDoubleKills += stats.get("doubleKills").getAsDouble() / 100000;
			AvgTripleKills += stats.get("tripleKills").getAsDouble() / 100000;
			AvgQuadraKills += stats.get("quadraKills").getAsDouble() / 100000;
			AvgPentaKills += stats.get("pentaKills").getAsDouble() / 100000;
			AvgDeaths += stats.get("deaths").getAsDouble() / 100000;

		}

		r.average_game_time += AvgDuration;
		r.avg_turrets_destroyed += AvgTowerKills;
		r.avg_kills += AvgKills;
		r.avg_assists += AvgAssists;
		r.avg_minions_killed += AvgMinionsKilled;
		r.avg_neutral_monsters_killed += AvgNeutralMinionsKilled;
		r.avg_gold += AvgGoldEarned;
		r.avg_double_kills += AvgDoubleKills;
		r.avg_triple_kills += AvgTripleKills;
		r.avg_quadra_kills += AvgQuadraKills;
		r.avg_penta_kills += AvgPentaKills;
		r.average_dragons += AvgDragon;
		r.average_barons += AvgBaron;
		r.avg_deaths += AvgDeaths;

		return "";
	}

	public static String setKrakensAvg(JsonObject obj, RegionTotal r) {
		JsonObject timeline = obj.getAsJsonObject("timeline");
		JsonArray frames = timeline.getAsJsonArray("frames");
		JsonArray teams = obj.getAsJsonArray("teams");
		JsonArray participants = obj.getAsJsonArray("participants");
		int winner = 0;
		HashMap<Integer, Integer> championByParticipant = new HashMap<Integer, Integer>();

		for (int i = 0; i < participants.size(); i++) {
			JsonObject participant = participants.get(i).getAsJsonObject();
			int participantId = participant.get("participantId").getAsInt();
			int championId = participant.get("championId").getAsInt();
			championByParticipant.put(participantId, championId);
		}

		for (int i = 0; i < teams.size(); i++) {
			JsonObject team = teams.get(i).getAsJsonObject();
			int teamId = team.get("teamId").getAsInt();
			boolean won = team.get("winner").getAsBoolean();
			if (won) {
				winner = teamId;
			}

		}

		for (int i = 0; i < frames.size(); i++) {

			if (frames.get(i).getAsJsonObject().getAsJsonArray("events") != null) {
				JsonArray events = frames.get(i).getAsJsonObject()
						.getAsJsonArray("events");
				for (int j = 0; j < events.size(); j++) {
					JsonObject event = events.get(j).getAsJsonObject();
					String eventType = event.get("eventType").toString();
					if (eventType.equals("\"ITEM_PURCHASED\"")) {
						int itemId = event.get("itemId").getAsInt();
						int participantId = event.get("participantId")
								.getAsInt();

						if (itemId == 3611 || itemId == 3612 || itemId == 3613
								|| itemId == 3614 || itemId == 3615
								|| itemId == 3621 || itemId == 3624) {

							r.average_krakens += 5 / 10000;
						}
						if (itemId == 3616 || itemId == 3622 || itemId == 3625) {

							r.average_krakens += 10 / 10000;
						}
						if (itemId == 3617 || itemId == 3623 || itemId == 3626) {

							r.average_krakens += 20 / 10000;
						}
						if (itemId == 3611) {
							BrawlersHired bh = BrawlersHired.find(
									"byChampion_id",
									championByParticipant.get(participantId))
									.first();
							if (bh == null) {
								bh = new BrawlersHired();
								bh.champion_id = championByParticipant
										.get(participantId);
							}
							// Razorfin
							bh.razorfins += 1;
							if (winner == 100) {
								if (participantId <= 5) {
									bh.razorfins_win += 1;
								}
							} else if (winner == 200) {
								if (participantId > 5) {
									bh.razorfins_win += 1;
								}
							}

							bh.save();
						}
						if (itemId == 3612) {
							BrawlersHired bh = BrawlersHired.find(
									"byChampion_id",
									championByParticipant.get(participantId))
									.first();
							if (bh == null) {
								bh = new BrawlersHired();
								bh.champion_id = championByParticipant
										.get(participantId);
							}
							// Ironback
							bh.ironbacks += 1;
							if (winner == 100) {
								if (participantId <= 5) {
									bh.ironbacks_win += 1;

								}
							} else if (winner == 200) {
								if (participantId > 5) {
									bh.ironbacks_win += 1;
								}
							}
							bh.save();
						}
						if (itemId == 3613) {
							BrawlersHired bh = BrawlersHired.find(
									"byChampion_id",
									championByParticipant.get(participantId))
									.first();
							if (bh == null) {
								bh = new BrawlersHired();
								bh.champion_id = championByParticipant
										.get(participantId);
							}
							// Plundercrab
							bh.plundercrabs += 1;
							if (winner == 100) {
								if (participantId <= 5) {
									bh.plundercrabs_win += 1;

								}
							} else if (winner == 200) {
								if (participantId > 5) {
									bh.plundercrabs_win += 1;
								}
							}
							bh.save();
						}
						if (itemId == 3614) {
							BrawlersHired bh = BrawlersHired.find(
									"byChampion_id",
									championByParticipant.get(participantId))
									.first();
							if (bh == null) {
								bh = new BrawlersHired();
								bh.champion_id = championByParticipant
										.get(participantId);
							}
							// Ocklepod
							bh.ocklepods += 1;
							if (winner == 100) {
								if (participantId <= 5) {
									bh.ocklepods_win += 1;
								}
							} else if (winner == 200) {
								if (participantId > 5) {
									bh.ocklepods_win += 1;
								}
							}
							bh.save();
						}

					}
				}
			}

		}

		return "";
	}
	
	public static String setBlackMarketChampion(JsonObject obj, RegionTotal region) {
		int[] itemsIDs = new int[] { 3150, 3652, 3744, 3745, 3911, 3924, 3742,
				3829, 3840, 3841, 3844, 3430, 3431, 3434, 3433 };

		JsonArray data = obj.getAsJsonArray("participants");
		for (int i = 0; i < data.size(); i++) {
			int champId = data.get(i).getAsJsonObject().get("championId")
					.getAsInt();

			BlackMarketChampion blackMarketChampion = BlackMarketChampion.find(
					"byChampion_id", champId).first();

			if (blackMarketChampion == null) {
				blackMarketChampion = new BlackMarketChampion();
				blackMarketChampion.champion_id = champId;
				blackMarketChampion.regionTotal = region;
				blackMarketChampion.save();
			}

			Champion champion = Champion.find("byChampion_id", champId).first();

			if (champion == null) {
				champion = new Champion();
				champion.champion_id = champId;
				champion.regionTotal = region;

			}

			JsonObject stats = data.get(i).getAsJsonObject().get("stats")
					.getAsJsonObject();
			boolean winner = stats.get("winner").getAsBoolean();

			champion.kills = stats.get("kills").getAsInt();
			champion.deaths = stats.get("deaths").getAsInt();
			champion.assists = stats.get("assists").getAsInt();
			champion.games++;

			if (winner) {
				champion.wins++;
			}
			champion.save();

			int idItem1 = stats.get("item0").getAsInt();
			int idItem2 = stats.get("item1").getAsInt();
			int idItem3 = stats.get("item2").getAsInt();
			int idItem4 = stats.get("item3").getAsInt();
			int idItem5 = stats.get("item4").getAsInt();
			int idItem6 = stats.get("item5").getAsInt();
			for (int j = 0; j < itemsIDs.length; j++) {
				if (idItem1 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				} else if (idItem2 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				} else if (idItem3 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				} else if (idItem4 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				} else if (idItem5 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				} else if (idItem6 == itemsIDs[j]) {
					if (j == 0) {
						blackMarketChampion.mirage_blade++;
						if (winner)
							blackMarketChampion.mirage_blade_win++;
					}
					if (j == 1) {
						blackMarketChampion.thypoon_claws++;
						if (winner)
							blackMarketChampion.thypoon_claws_win++;
					}
					if (j == 2) {
						blackMarketChampion.staff_of_flowing_water++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 3) {
						blackMarketChampion.puppeteer++;
						if (winner)
							blackMarketChampion.staff_of_flowing_water_win++;
					}
					if (j == 4) {
						blackMarketChampion.martyrs_gambit++;
						if (winner)
							blackMarketChampion.martyrs_gambit_win++;
					}
					if (j == 5) {
						blackMarketChampion.flesheater++;
						if (winner)
							blackMarketChampion.flesheater_win++;
					}
					if (j == 6) {
						blackMarketChampion.death_mans_plate++;
						if (winner)
							blackMarketChampion.death_mans_plate_win++;
					}
					if (j == 7) {
						blackMarketChampion.trickters_glass++;
						if (winner)
							blackMarketChampion.trickters_glass_win++;
					}
					if (j == 8) {
						blackMarketChampion.globe_of_trust++;
						if (winner)
							blackMarketChampion.globe_of_trust_win++;
					}
					if (j == 9) {
						blackMarketChampion.swinglers_orb++;
						if (winner)
							blackMarketChampion.swinglers_orb_win++;
					}
					if (j == 10) {
						blackMarketChampion.murksphere++;
						if (winner)
							blackMarketChampion.murksphere_win++;
					}
					if (j == 11) {
						blackMarketChampion.rite_of_ruin++;
						if (winner)
							blackMarketChampion.rite_of_ruin_win++;
					}
					if (j == 12) {
						blackMarketChampion.netherside_grimoire++;
						if (winner)
							blackMarketChampion.netherside_grimoire_win++;
					}
					if (j == 13) {
						blackMarketChampion.box_arcana++;
						if (winner)
							blackMarketChampion.box_arcana_win++;
					}
					if (j == 14) {
						blackMarketChampion.lost_chapters++;
						if (winner)
							blackMarketChampion.lost_chapters_win++;
					}
				}
			}
			blackMarketChampion.save();
		}

		return "";
	}

}