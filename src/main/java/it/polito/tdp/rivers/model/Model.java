package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.rivers.db.RiversDAO;


public class Model {

	public void model() {
		dao.getAllRivers(rivers);
	}
	RiversDAO dao= new RiversDAO();
	public Map<Integer,River> rivers=new HashMap<Integer, River>();
	
	LinkedList<Flow> flows;
	public List<String> caricaDatiFiume(int id){
		
		flows =new LinkedList<Flow>(dao.getRiverFlows(id));
		rivers.get(id).setFlows(flows);
		LocalDate min=null;
		LocalDate max=null;
		double sum=0;
		
		for(Flow f: flows)
		{
			if(min==null)
				min=f.getDay();
			if(max==null)
				max=f.getDay();
			
			if(f.getDay().compareTo(min)<0)
			{
				min = f.getDay();
			}
			if(f.getDay().compareTo(max)>0)
			{
				max = f.getDay();
			}
			sum+=f.getFlow();
			
		}
		LinkedList<String> out=new LinkedList<>();
		out.add(min.toString());
		out.add(max.toString());
		out.add(String.valueOf(sum/flows.size()));
		rivers.get(id).setFlowAvg(sum/flows.size());
		out.add(String.valueOf(flows.size()));
		return out;
	}
	
	
	//SIMULAZIONE
	
		
	double Q;
	double C;
	double fOutMin;
	
	public int noErogazioneMin=0;
	public double Cmed;
	
	
	public void init(int id, double k) {
		noErogazioneMin=0;
		Cmed=0;
		Q= k*rivers.get(id).getFlowAvg()*30*60*60*24;
		C=Q/2;
		fOutMin=0.8*rivers.get(id).getFlowAvg()*60*60*24;
		System.out.println("Q: "+Q+" C: "+C);
		
	}
	
	public void run( int id) {
		
		for(Flow f: rivers.get(id).getFlows())
		{
			C+=f.getFlow()*60*60*24;
			if(C>Q)
			{
				C=Q;
			}
			boolean val = new Random().nextInt(20)==0;
			if(val)
			{
				C-=10*fOutMin;
			}
			else
			{
				C-=fOutMin;
			}
			if(C<0)
			{
				C=0;
				noErogazioneMin++;
			}
			Cmed+=C;
			System.out.println(C);
		}
		Cmed=Cmed/ rivers.get(id).getFlows().size();
		
	}

	
	
}
