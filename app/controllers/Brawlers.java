package controllers;

import java.util.List;

import com.google.gson.JsonObject;

import models.*;
import play.mvc.*;

public class Brawlers extends Controller {

    public static void index() {
        render();
    }
    
    public static String getAllBrawlersInfo(){

		List<RegionTotal> regions = RegionTotal.findAll();
		
		JsonObject resultado = new JsonObject();
		
		int i = 1;
		for(RegionTotal r : regions){
			List<BrawlersHired> bmc = r.brawlersHireds;

			int total = 0;

			int razorfins = 0;
			int ironbacks = 0;
			int plundercrabs = 0;
			int ocklepods = 0;

			int razorfinsWin = 0;
			int ironbacksWin = 0;
			int plundercrabsWin = 0;
			int ocklepodsWin = 0;

			for (BrawlersHired bm : bmc) {
				total += bm.ironbacks+bm.ocklepods+bm.plundercrabs+bm.razorfins;
				razorfins += bm.razorfins;
				ironbacks += bm.ironbacks;
				plundercrabs += bm.plundercrabs;
				ocklepods += bm.ocklepods;


				razorfinsWin += bm.razorfins_win;
				ironbacksWin += bm.ironbacks_win;
				plundercrabsWin += bm.plundercrabs_win;
				ocklepodsWin += bm.ocklepods_win;
			}

			JsonObject regionItems = new JsonObject();
			JsonObject item1 = new JsonObject();
			JsonObject item2 = new JsonObject();
			JsonObject item3 = new JsonObject();
			JsonObject item4 = new JsonObject();

			item1.addProperty("wR", ((double)razorfinsWin/razorfins)*100);
			item2.addProperty("wR", ((double)ironbacksWin/ironbacks)*100);
			item3.addProperty("wR", ((double)plundercrabsWin/plundercrabs)*100);
			item4.addProperty("wR", ((double)ocklepodsWin/ocklepods)*100);

			item1.addProperty("bR", ((double)razorfins/total)*100);
			item2.addProperty("bR", ((double)ironbacks/total)*100);
			item3.addProperty("bR", ((double)plundercrabs/total)*100);
			item4.addProperty("bR", ((double)ocklepods/total)*100);

			regionItems.add("3611", item1);
			regionItems.add("3612", item2);
			regionItems.add("3613", item3);
			regionItems.add("3614", item4);
			
			resultado.add(bmc.get(i).regionTotal.region, regionItems);
			
			i++;
		}

		return resultado.toString();
	}

}
