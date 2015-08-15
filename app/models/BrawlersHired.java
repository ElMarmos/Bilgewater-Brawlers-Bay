package models;

import play.*;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class BrawlersHired extends Model {
    
	/**
	 * Champion id
	 */
	@Unique
	public int champion_id;
	
	/**
	 * Number of times players bought ironbacks with the champion
	 */
	public int ironbacks;
	
	/**
	 * Number of times players bought ironbacks and win the game with the champion
	 */
	public int ironbacks_win;
	
	/**
	 * Number of times players bought ocklepods with the champion
	 */
	public int ocklepods;
	
	/**
	 * Number of times players bought ocklepods and win the game with the champion
	 */
	public int ocklepods_win;
	
	/**
	 * Number of times players bought plundercrabs with the champion
	 */
	public int plundercrabs;
	
	/**
	 * Number of times players bought plundercrabs and win the game with the champion
	 */
	public int plundercrabs_win;
	
	/**
	 * Number of times players bought razorfins with the champion
	 */
	public int razorfins;
	
	/**
	 * Number of times players bought razorfins and win the game with the champion
	 */
	public int razorfins_win;
	
	/**
	 * Region
	 */
	@OneToOne
	public RegionTotal regionTotal;
	
}
