package it.polito.tdp.rivers.db;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RiversDAO {

	public List<River> getAllRivers() 
	{
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) 
			{
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}

			conn.close();
			
		} 
		catch (SQLException e) 
		{
			throw new RuntimeException("SQL Error");
		}

		return rivers;
	}
	
	public List<Flow> getFlows(River fiume)
	{
		final String sql = "SELECT id, day, flow "
				+ "FROM flow "
				+ "WHERE river=? "
				+ "ORDER BY day";
		
		List<Flow> flussi = new LinkedList<Flow>();
		
		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fiume.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) 
			{
				LocalDate x = res.getDate("day").toLocalDate();
				flussi.add(new Flow(x, res.getDouble("flow"), fiume));
			}

			Collections.sort(flussi);
			fiume.setFlows(flussi);
			conn.close();
			
		} 
		catch (SQLException e) 
		{
			throw new RuntimeException("SQL Error");
		}
		
		return flussi;
	}
	
	
}
