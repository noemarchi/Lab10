package it.polito.tdp.rivers.model;

public class SimulationResult 
{
	private double avgC;
	private int numberOfDays;
	
	public SimulationResult(double avgC, int numberOfDays) 
	{
		super();
		this.avgC = avgC;
		this.numberOfDays = numberOfDays;
	}

	public double getAvgC() {
		return avgC;
	}

	public void setAvgC(double avgC) {
		this.avgC = avgC;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	

}
