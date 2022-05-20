package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model 
{
	private RiversDAO dao;
	private List<River> fiumi;
	private PriorityQueue<Flow> coda;
	
	public Model()
	{
		this.dao = new RiversDAO();
		
		this.fiumi = dao.getAllRivers();
		
		for(River fiume: fiumi)
		{
			dao.getFlows(fiume);
		}
	}
	
	public List<River> getRivers()
	{
		return this.fiumi;
	}
	
	public LocalDate getStartDate(River fiume)
	{
		if(!fiume.getFlows().isEmpty())
		{
			return fiume.getFlows().get(0).getDay();
		}
		else
		{
			return null;
		}
	}
	
	public LocalDate getEndDate(River fiume)
	{
		if(!fiume.getFlows().isEmpty())
		{
			return fiume.getFlows().get(fiume.getFlows().size()-1).getDay();
		}
		else
		{
			return null;
		}
	}

	public int getNumMeasurements(River fiume)
	{
		return fiume.getFlows().size();
	}
	
	public double getFMed(River fiume)
	{
		double avg = 0;
		double somma = 0;
		
		for(Flow f: fiume.getFlows())
		{
			somma = somma + f.getFlow();
		}
		
		avg = somma / fiume.getFlows().size();
		fiume.setFlowAvg(avg);
		
		return avg;
	}

	public SimulationResult simula(River fiume, double k)
	{
		this.coda = new PriorityQueue<Flow>();
		this.coda.addAll(fiume.getFlows());
		
		List<Double> capacity = new ArrayList<Double>();
		
		double Q = k * 30 * convertM3SecToM3Day(fiume.getFlowAvg());
		double C = Q/2;
		double fOutMin = convertM3SecToM3Day(0.8 * fiume.getFlowAvg());
		
		int numberOfDays = 0;
		
		while(!this.coda.isEmpty())
		{
			Flow flusso = this.coda.poll();
			
			double fOut = fOutMin;
			
			if(Math.random() > 0.95)
			{
				fOut = 10 * fOutMin;
			}
			
			C = C + convertM3SecToM3Day(flusso.getFlow());
			
			if(C > Q)
			{
				C = Q;
			}
			
			if(C < fOut)
			{
				numberOfDays++;
				C = 0;
			}
			else
			{
				C = C - fOut;
			}
			
			capacity.add(C);
		}
		
		double sumC = 0;
		
		for(Double x: capacity)
		{
			sumC = sumC + x;
		}
		
		double avgC = sumC / capacity.size();
		
		return new SimulationResult(avgC, numberOfDays);
	}

	private double convertM3SecToM3Day(double flow) 
	{
		return flow*60*60*24;
	}
	
	private double convertM3DayToM3Sec(double flow) 
	{
		return flow/60/60/24;
	}
	
}
