package controllers;

import java.util.List;

import com.google.gson.JsonObject;

import models.BlackMarketChampion;
import models.RegionTotal;
import play.mvc.*;

public class Items extends Controller {

	public static void index() {
		render();
	}

	public static String getAllItemsInfo(){

		List<RegionTotal> regions = RegionTotal.findAll();
		
		JsonObject resultado = new JsonObject();
		
		int i = 1;
		for(RegionTotal r : regions){
			List<BlackMarketChampion> bmc = r.blackMarketChampions;

			int total = 0;

			int boxArcana = 0;
			int deathMansPlate = 0;
			int flesheater = 0;
			int globeOfTrust = 0;
			int lostChapters = 0;
			int martyrsGambit = 0;
			int mirageBlade = 0;
			int murksphere = 0;
			int nethersideGrimoire = 0;
			int puppeteer = 0;
			int riteOfRuin = 0;
			int staffOfFlowing = 0;
			int swinglersOrb = 0;
			int typhoonClaws = 0;
			int trickstersGlass = 0;

			int boxArcanaWin = 0;
			int deathMansPlateWin = 0;
			int flesheaterWin = 0;
			int globeOfTrustWin = 0;
			int lostChaptersWin = 0;
			int martyrsGambitWin = 0;
			int mirageBladeWin = 0;
			int murksphereWin = 0;
			int nethersideGrimoireWin = 0;
			int puppeteerWin = 0;
			int riteOfRuinWin = 0;
			int staffOfFlowingWin = 0;
			int swinglersOrbWin = 0;
			int typhoonClawsWin = 0;
			int trickstersGlassWin = 0;

			for (BlackMarketChampion bm : bmc) {
				total += bm.box_arcana+bm.death_mans_plate+bm.flesheater+bm.globe_of_trust+bm.lost_chapters+bm.martyrs_gambit+bm.mirage_blade+bm.murksphere+bm.netherside_grimoire+bm.puppeteer+bm.rite_of_ruin+bm.staff_of_flowing_water+bm.swinglers_orb+bm.thypoon_claws+bm.trickters_glass;
				boxArcana += bm.box_arcana;
				deathMansPlate += bm.death_mans_plate;
				flesheater += bm.flesheater;
				globeOfTrust += bm.globe_of_trust;
				lostChapters += bm.lost_chapters;
				martyrsGambit += bm.martyrs_gambit;
				mirageBlade += bm.mirage_blade;
				murksphere += bm.murksphere;
				nethersideGrimoire += bm.netherside_grimoire;
				puppeteer += bm.puppeteer;
				riteOfRuin += bm.rite_of_ruin;
				staffOfFlowing += bm.staff_of_flowing_water;
				swinglersOrb += bm.swinglers_orb;
				typhoonClaws += bm.thypoon_claws;
				trickstersGlass += bm.trickters_glass;


				boxArcanaWin += bm.box_arcana_win;
				deathMansPlateWin += bm.death_mans_plate_win;
				flesheaterWin += bm.flesheater_win;
				globeOfTrustWin += bm.globe_of_trust_win;
				lostChaptersWin += bm.lost_chapters_win;
				martyrsGambitWin += bm.martyrs_gambit_win;
				mirageBladeWin += bm.mirage_blade_win;
				murksphereWin += bm.murksphere_win;
				nethersideGrimoireWin += bm.netherside_grimoire_win;
				puppeteerWin += bm.puppeteer_win;
				riteOfRuinWin += bm.rite_of_ruin_win;
				staffOfFlowingWin += bm.staff_of_flowing_water_win;
				swinglersOrbWin += bm.swinglers_orb_win;
				typhoonClawsWin += bm.thypoon_claws_win;
				trickstersGlassWin += bm.trickters_glass_win;
			}

			JsonObject regionItems = new JsonObject();
			JsonObject item1 = new JsonObject();
			JsonObject item2 = new JsonObject();
			JsonObject item3 = new JsonObject();
			JsonObject item4 = new JsonObject();
			JsonObject item5 = new JsonObject();
			JsonObject item6 = new JsonObject();
			JsonObject item7 = new JsonObject();
			JsonObject item8 = new JsonObject();
			JsonObject item9 = new JsonObject();
			JsonObject item10 = new JsonObject();
			JsonObject item11 = new JsonObject();
			JsonObject item12 = new JsonObject();
			JsonObject item13 = new JsonObject();
			JsonObject item14 = new JsonObject();
			JsonObject item15 = new JsonObject();

			item1.addProperty("wR", ((double)boxArcanaWin/boxArcana)*100);
			item2.addProperty("wR", ((double)deathMansPlateWin/deathMansPlate)*100);
			item3.addProperty("wR", ((double)flesheaterWin/flesheater)*100);
			item4.addProperty("wR", ((double)globeOfTrustWin/globeOfTrust)*100);
			item5.addProperty("wR", ((double)lostChaptersWin/lostChapters)*100);
			item6.addProperty("wR", ((double)martyrsGambitWin/martyrsGambit)*100);
			item7.addProperty("wR", ((double)mirageBladeWin/mirageBlade)*100);
			item8.addProperty("wR", ((double)murksphereWin/murksphere)*100);
			item9.addProperty("wR", ((double)nethersideGrimoireWin/nethersideGrimoire)*100);
			item10.addProperty("wR", ((double)puppeteerWin/puppeteer)*100);
			item11.addProperty("wR", ((double)riteOfRuinWin/riteOfRuin)*100);
			item12.addProperty("wR", ((double)staffOfFlowingWin/staffOfFlowing)*100);
			item13.addProperty("wR", ((double)swinglersOrbWin/swinglersOrb)*100);
			item14.addProperty("wR", ((double)typhoonClawsWin/typhoonClaws)*100);
			item15.addProperty("wR", ((double)trickstersGlassWin/trickstersGlass)*100);

			item1.addProperty("bR", ((double)boxArcana/total)*100);
			item2.addProperty("bR", ((double)deathMansPlate/total)*100);
			item3.addProperty("bR", ((double)flesheater/total)*100);
			item4.addProperty("bR", ((double)globeOfTrust/total)*100);
			item5.addProperty("bR", ((double)lostChapters/total)*100);
			item6.addProperty("bR", ((double)martyrsGambit/total)*100);
			item7.addProperty("bR", ((double)mirageBlade/total)*100);
			item8.addProperty("bR", ((double)murksphere/total)*100);
			item9.addProperty("bR", ((double)nethersideGrimoire/total)*100);
			item10.addProperty("bR", ((double)puppeteer/total)*100);
			item11.addProperty("bR", ((double)riteOfRuin/total)*100);
			item12.addProperty("bR", ((double)staffOfFlowing/total)*100);
			item13.addProperty("bR", ((double)swinglersOrb/total)*100);
			item14.addProperty("bR", ((double)typhoonClaws/total)*100);
			item15.addProperty("bR", ((double)trickstersGlass/total)*100);

			regionItems.add("3434", item1);
			regionItems.add("3742", item2);
			regionItems.add("3924", item3);
			regionItems.add("3840", item4);
			regionItems.add("3433", item5);
			regionItems.add("3911", item6);
			regionItems.add("3150", item7);
			regionItems.add("3844", item8);
			regionItems.add("3431", item9);
			regionItems.add("3745", item10);
			regionItems.add("3430", item11);
			regionItems.add("3744", item12);
			regionItems.add("3841", item13);
			regionItems.add("3652", item14);
			regionItems.add("3829", item15);
			
			resultado.add(bmc.get(i).regionTotal.region, regionItems);
			
			i++;
		}

		return resultado.toString();
	}

}
