package models;

import play.*;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class RegionTotal extends Model {
    
	/**
	 * Region id
	 */
	@Unique
	public String region_id;
	
	/**
	 * Region name
	 */
	@Unique
	public String region;
	
	/**
	 * Average kills in the region
	 */
	public double avg_kills;
	
	/**
	 * Average deaths in the region
	 */
	public double avg_deaths;
	
	/**
	 * Average assists in the region
	 */
	public double avg_assists;
	
	/**
	 * Average minions killed in the region
	 */
	public double avg_minions_killed;
	
	/**
	 * Average neutral monsters killed  in the region
	 */
	public double avg_neutral_monsters_killed;
	
	/**
	 * Average gold earned in the region
	 */
	public double avg_gold;
	
	/**
	 * Average double kills in the region
	 */
	public double avg_double_kills;
	
	/**
	 * Average triple kills in the region
	 */
	public double avg_triple_kills;
	
	/**
	 * Average quadra kills in the region
	 */
	public double avg_quadra_kills;
	
	/**
	 * Average penta kills in the region
	 */
	public double avg_penta_kills;
	
	/**
	 * Average turrets destroyed in the region
	 */
	public double avg_turrets_destroyed;
	
	/**
	 * Time of the longest game in the region
	 */
	public double longest_game_time;
	
	/**
	 * Average game time in the region
	 */
	public double average_game_time;
	
	/**
	 * Average krakens spent in the region
	 */
	public double average_krakens;
	
	/**
	 * Average dragons killed in the region
	 */
	public double average_dragons;
	
	/**
	 * Average barons killed in the region
	 */
	public double average_barons;
	
	/**
	 * Brawlers statics
	 */
	@OneToOne(mappedBy="regionTotal", cascade=CascadeType.ALL)
	public BrawlersHired brawlersHireds;
	
	/**
	 * List of champions items stats
	 */
	@OneToMany(mappedBy="regionTotal", cascade=CascadeType.ALL)
	public List<BlackMarketChampion> blackMarketChampions;
	
	/**
	 * List of champions stats
	 */
	@OneToMany(mappedBy="regionTotal", cascade=CascadeType.ALL)
	public List<Champion> champions;	
	
	public RegionTotal(){
		blackMarketChampions = new ArrayList<>();
		champions = new ArrayList<>();
	}
}
