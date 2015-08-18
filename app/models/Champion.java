package models;

import play.*;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Champion extends Model {
    
	/**
	 * Champion id
	 */
	@Unique
	public int champion_id;
	
	/**
	 * Average kills with the champion	
	 */
	public int kills;
	
	/**
	 * Average deaths with the champion
	 */
	public int deaths;
	
	/**
	 * Average assists with the champion
	 */
	public int assists;
	
	/**
	 * Average wins with the champion
	 */
	public int wins;
	
	/**
	 * Games with the champion	
	 */
	public int games;
	
	/**
	 * Region
	 */
	@ManyToOne
	public RegionTotal regionTotal;
	
	public Champion(){
		
	}
	
}
