package controllers;

import java.util.List;

import javax.persistence.Query;

import com.google.gson.JsonObject;

import models.RegionTotal;
import play.mvc.*;

public class Regional extends Controller {

	public static void index() {
		render();
	}

	public static String loadPage(){
		Query q = RegionTotal.em().createNativeQuery("select * from regiontotal order by region");
		List<Object[]> resultado = q.getResultList();

		JsonObject js = new JsonObject();

		double world_barons = 0;
		double world_dragons = 0;
		double world_game_time = 0;
		double world_krakens = 0;
		double world_assists = 0;
		double world_deaths = 0;
		double world_doubles = 0;
		double world_gold = 0;
		double world_kills = 0;
		double world_cs = 0;
		double world_ncs = 0;
		double world_pentas = 0;
		double world_quadras = 0;
		double world_triples = 0;
		double world_turrets = 0;
		double world_longest = 0;

		int i = 1;    	
		for(Object[] o : resultado){
			JsonObject region = new JsonObject();
			double avg_barons = (double) o[1];
			double avg_dragons = (double) o[2];
			double avg_game_time = (double) o[3];
			double avg_krakens = (double) o[4];
			double avg_assists = (double) o[5];
			double avg_deaths = (double) o[6];
			double avg_doubles = (double) o[7];
			double avg_gold = (double) o[8];
			double avg_kills = (double) o[9];
			double avg_cs = (double) o[10];
			double avg_ncs = (double) o[11];
			double avg_pentas = (double) o[12];
			double avg_quadras = (double) o[13];
			double avg_triples = (double) o[14];
			double avg_turrets = (double) o[15];
			double avg_longest = (double) o[16];

			region.addProperty("barons", avg_barons);
			region.addProperty("dragons", avg_dragons);
			region.addProperty("time", avg_game_time);
			region.addProperty("krakens", avg_krakens);
			region.addProperty("assists", avg_assists);
			region.addProperty("deaths", avg_deaths);
			region.addProperty("doubles", avg_doubles);
			region.addProperty("gold", avg_gold);
			region.addProperty("kills", avg_kills);
			region.addProperty("cs", avg_cs);
			region.addProperty("ncs", avg_ncs);
			region.addProperty("pentas", avg_pentas);
			region.addProperty("quadras", avg_quadras);
			region.addProperty("triples", avg_triples);
			region.addProperty("turrets", avg_turrets);
			region.addProperty("longest", avg_longest);

			js.add(i+"", region);

			world_barons += avg_barons/10;
			world_dragons += avg_dragons/10;
			world_game_time += avg_game_time/10;
			world_krakens += avg_krakens/10;
			world_assists += avg_assists/10;
			world_deaths += avg_deaths/10;
			world_doubles += avg_doubles/10;
			world_gold += avg_gold/10;
			world_kills += avg_kills/10;
			world_cs += avg_cs/10;
			world_ncs += avg_ncs/10;
			world_pentas += avg_pentas/10;
			world_quadras += avg_quadras/10;
			world_triples += avg_triples/10;
			world_turrets += avg_turrets/10;
			world_longest += avg_longest/10;

			i++;
		}

		JsonObject world = new JsonObject();

		world.addProperty("barons", world_barons);
		world.addProperty("dragons", world_dragons);
		world.addProperty("time", world_game_time);
		world.addProperty("krakens", world_krakens);
		world.addProperty("assists", world_assists);
		world.addProperty("deaths", world_deaths);
		world.addProperty("doubles", world_doubles);
		world.addProperty("gold", world_gold);
		world.addProperty("kills", world_kills);
		world.addProperty("cs", world_cs);
		world.addProperty("ncs", world_ncs);
		world.addProperty("pentas", world_pentas);
		world.addProperty("quadras", world_quadras);
		world.addProperty("triples", world_triples);
		world.addProperty("turrets", world_turrets);
		world.addProperty("longest", world_longest);

		js.add("world", world);

		return js.toString();
	}

}
