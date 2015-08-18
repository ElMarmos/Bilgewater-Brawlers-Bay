package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class BlackMarketChampion extends Model {
    
	/**
	 * Champion id
	 */
	public int champion_id;
	
	/**
	 * Number of times players bought the item Lost Chapters with the champion
	 */
	public int lost_chapters;
	
	/**
	 * Number of times players bought the item Lost Chapters and win the game with the champion
	 */
	public int lost_chapters_win;
	
	/**
	 * Number of times players bought the item Rite of Ruin with the champion
	 */
	public int rite_of_ruin;
	
	/**
	 * Number of times players bought the item Rite of Ruin and win the game with the champion
	 */
	public int rite_of_ruin_win;
	
	/**
	 * Number of times players bought the item Netherstride Grimoire with the champion
	 */
	public int netherside_grimoire;
	
	/**
	 * Number of times players bought the item Netherstride Grimoire and win the game with the champion
	 */
	public int netherside_grimoire_win;
	
	/**
	 * Number of times players bought the item Box Arcana with the champion
	 */
	public int box_arcana;
	
	/**
	 * Number of times players bought the item Box Arcana and win the game with the champion
	 */
	public int box_arcana_win;
	
	/**
	 * Number of times players bought the item Martyrs Gambit with the champion
	 */
	public int martyrs_gambit;
	
	/**
	 * Number of times players bought the item Martyrs Gambit and win the game with the champion
	 */
	public int martyrs_gambit_win;
	
	/**
	 * Number of times players bought the item Death Mans Plate with the champion
	 */
	public int death_mans_plate;
	
	/**
	 * Number of times players bought the item Death Mans Plate and win the game with the champion
	 */
	public int death_mans_plate_win;
	
	/**
	 * Number of times players bought the item Murksphere Plate with the champion
	 */
	public int murksphere;
	
	/**
	 * Number of times players bought the item Murksphere and win the game with the champion
	 */
	public int murksphere_win;
	
	/**
	 * Number of times players bought the item Swinglers Orb Plate with the champion
	 */
	public int swinglers_orb;
	
	/**
	 * Number of times players bought the item Swinglers Orb and win the game with the champion
	 */
	public int swinglers_orb_win;
	
	/**
	 * Number of times players bought the item Globe of trust Plate with the champion
	 */
	public int globe_of_trust;
	
	/**
	 * Number of times players bought the item Globe of trust and win the game with the champion
	 */
	public int globe_of_trust_win;
	
	/**
	 * Number of times players bought the item Typhoon Claws with the champion
	 */
	public int thypoon_claws;
	
	/**
	 * Number of times players bought the item Thypoon Claws and win the game with the champion
	 */
	public int thypoon_claws_win;
	
	/**
	 * Number of times players bought the item Trickters Glass with the champion
	 */
	public int trickters_glass;
	
	/**
	 * Number of times players bought the item Trickters Glass and win the game with the champion
	 */
	public int trickters_glass_win;
	
	/**
	 * Number of times players bought the item Flesheater with the champion
	 */
	public int flesheater;
	
	/**
	 * Number of times players bought the item Flesheater and win the game with the champion
	 */
	public int flesheater_win;
	
	/**
	 * Number of times players bought the item Staff of Flowing Water with the champion
	 */
	public int staff_of_flowing_water;
	
	/**
	 * Number of times players bought the item Staff of Flowing Water and win the game with the champion
	 */
	public int staff_of_flowing_water_win;
	
	/**
	 * Number of times players bought the item Puppeter with the champion
	 */
	public int puppeteer;
	
	/**
	 * Number of times players bought the item Puppeter and win the game with the champion
	 */
	public int puppeteer_win;
	
	/**
	 * Number of times players bought the item Mirage Blade with the champion
	 */
	public int mirage_blade;
	
	/**
	 * Number of times players bought the item Mirage Blade and win the game with the champion
	 */
	public int mirage_blade_win;
	
	/**
	 * Region
	 */
	@ManyToOne
	public RegionTotal regionTotal;
}
