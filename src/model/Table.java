package model;

import java.awt.geom.Point2D;

import parmenidianEnumerations.Status;

public class Table {
	
	private String tableName;
	private Point2D coordinates;
	private int tableStatus = Status.UNDEFINED.getValue();
	private int birthVersion = 0; //added on 2018-11-06
	private int attrsNumAtBirth = 0; //added on 2018-11-23
	private int duration = 0; //added on 2018-11-23
	private int sumUpdates = 0; //added on 2018-11-26
	private double atu = 0.0; //added on 2018-11-27
	private int survivor=0;//1 if tables exists in the last version, 0 otherwise
	
	public Table(String name){
		
		tableName=name.trim();
		
	}
	
	public Table(String name,Point2D c){
		
		tableName=name.trim();
		coordinates=c;
		
	}
	
	public String getTableName(){
		
		return tableName;
	}
	
	public void setTableStatus(int status){
		
		tableStatus=status;
	}

	public int getTableStatus() {
		return tableStatus;
	}

	public Point2D getCoords() {
		return coordinates;
	}

	public void setCoords(Object object) {
		this.coordinates = (Point2D) object;
	}
	
	/**
	 * 
	 * @param id birth version id
	 * @since 2018-11-06
	 */
	public void setBirthVersion(int id) {
		
		this.birthVersion = id;
		
	}

	/**
	 * @return birth version of the table 
	 * @since 2018-11-06
	 */
	public int getBirthVersion() {
		
		return this.birthVersion;
		
	}
	
	
	/**
	 * 
	 * @param number of attributes at birth version
	 * @since 2018-11-23
	 */
	public void setAttrsNumAtBirth(int attrsNum) {
		
		this.attrsNumAtBirth = attrsNum;
		
	}
	

	/**
	 * 
	 * @return number of attributes at birth version
	 * @since 2018-11-23
	 */
	public int getAttrsNumAtBirth() {
		
		return this.attrsNumAtBirth;
		
	}
	
	
	/**
	 * 
	 * @param table's duration
	 * @since 2018-11-23
	 */
	public void setDuration(int dur) {
		
		this.duration = dur;
		
	}

	
	/**
	 * 
	 * @return table's duration
	 * @since 2018-11-23
	 */
	public int getDuration() {
		
		return this.duration;
		
	}
	
	/**
	 * 
	 * @param set the number of table's updates.
	 * @since 2018-11-23
	 */
	public void setSumUpdates(int i) {
		
		this.sumUpdates = i;
		
	}

	/**
	 * Determines whether or not the table exists in last version.
	 * @param existsInLastVersion
	 * @since 2018-12-07
	 */
	public void setSurvivorStatus(int existsInLastVersion) {
		
		this.survivor = existsInLastVersion;
	}
	
	/**
	 * 
	 * @return table's sum of updates.
	 * @since 2018-11-23
	 */
	public int getSumUpdates() {
		
		return this.sumUpdates;
		
	}
	
	/**
	 * 
	 * @return 1 if the tables is survivor, 0 otherwise.
	 */
	public int getSurvivorStatus() {
		
		return this.survivor;
	}
	
	/**
	 * Computes table's Average Transitional Update (ATU) = sum(upd.)/dur.
	 * @author KD
	 * @since 2018-11-27
	 */
	public void setATU() {
		
		if (this.duration>0)
			this.atu = this.sumUpdates/(double)this.duration;
		else
			System.err.println("Table's duration is set to zero. Division by zero is not allowed.");
		
	}

	
	
	/**
	 * 
	 * @return table's Average Transitional Update (ATU) = sum(upd.)/dur.
	 * @author KD
	 * @since 2018-11-27
	 */
	public double getATU() {
		
		return this.atu;
		
	}
	
}
 