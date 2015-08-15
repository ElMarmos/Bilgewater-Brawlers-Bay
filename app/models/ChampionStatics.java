package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ChampionStatics extends Model {
    
	
	/**
	 * Champion id
	 */
	@Unique
	public int champion_id;
	
	/**
	 * Name of the champion
	 */
	public String name;
	
	/**
	 * Url to square image of the champion
	 */
	@MaxSize(1000)
	public String square;
	
	/**
	 * Url to loading image of the champion
	 */
	@MaxSize(1000)
	public String loading;
}
