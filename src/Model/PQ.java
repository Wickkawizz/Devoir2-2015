/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 * @author Arkapin£
 * @author WickkaWizz
 */
//PQ = Priority queue
//Custom class for a PQ. Needs to be ordered by the time property as a Min-Heap. Voir les algorithmes vu en classe pour l'implementation
// https://ift2015h21.files.wordpress.com/2021/02/ift2015h21-06note-pq.pdf
public class PQ{
    //This class will be used for the event queue and the sim population, since both needs to use this data structure
    ArrayList<Sim> simList;
    ArrayList<Sim> deadSimList;
    ArrayList<Event> eventList;

    public PQ() {
        this.simList = new ArrayList<>();
        this.deadSimList = new ArrayList<>();
        this.eventList = new ArrayList<>();
    }

    public ArrayList<Sim> getSimList() {
        return simList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }
    
    
    //I use a lot of repetition of code here, but it's to make sure there is no exception on the ambiguity of the type used in the interactions.
    // Maybe an alternative with the Generic data type exists, but it seems to lead to more ambiguity.
    
    // https://ift2015h21.files.wordpress.com/2021/02/ift2015h21-06note-pq.pdf
    // Page 4
    //Swim for the simList
    public void swim(Sim v, int i){
	    double p = Math.floor(i/2);
	    // 1 is returned if the sim in the list is greater than the one given in argument
	    while (p !=0 && simList.get((int) p).compareTo(v) > 0){
	        simList.add(i, simList.get((int) p)/*-1 ?? maybe the indexation will be bad*/);
	        i = (int) p;
	        
	        // mais on veut pas le plafond, on le floor non? En effet, you're right.
	        p = Math.floor(i/2);
	        }
	    simList.add(i,v);
    }
    
    //Swim for the eventlist
    public void swim(Event v, int i){
	    int p = Math.floorDiv(i, 2);

	    // 1 is returned if the sim in the list is greater than the one given in argument
	    while (p != 0 && eventList.get(p-1).compareTo(v) > 0){	    	
	        eventList.add(i-1, eventList.get(p-1)/*-1 ?? maybe the indexation will be bad*/);
	        //eventList.remove(i);
	        i = p;
	        p = Math.floorDiv(i, 2);
        }
	    eventList.add(i-1,v);
    }
    
    // https://ift2015h21.files.wordpress.com/2021/02/ift2015h21-06note-pq.pdf
    // Page 4
    public void sink(Sim v, int i){
	    int c = MinChildSim(i, simList);
	    // -1 is returned if the sim in the list is lesser than the one given in argument
	    while(c != 0 && simList.get(c).compareTo(v) < 0){
	        simList.add(i, simList.get(c)/*-1 ?? maybe the indexation will be bad*/);
	        i = c;
	        c = MinChildSim(i, simList);
	        }
	    simList.add(i, v);
    }
    
    public void sink(Event v, int i){
	    int c = MinChildEvent(i, eventList);
	    // -1 is returned if the sim in the list is lesser than the one given in argument
	    
	    
	    while(c != 0 && eventList.get(c-1) != null && eventList.get(c-1).compareTo(v) < 0){
	        eventList.add(i, eventList.get(c-1)/*-1 ?? maybe the indexation will be bad*/);
	        i = c;
	        c = MinChildEvent(i, eventList);

		    System.out.println("\n---\nc : " + c + "\n---");
		    System.out.println("\n---\neventList.size() : " + eventList.size() + "\n---");
		    if(c == eventList.size()) {
		    	System.out.println("fuck");
		    }
        }
	    eventList.add(i-1, v);
    }
    
	public int MinChildSim(int i, ArrayList<Sim> list) {
		int j;
		if (2 * i > list.size()) {
			j = 0;
		} else if ((2 * i + 1 <= list.size()) && (list.get(2 * i + 1).compareTo(list.get(2 * i)) < 0)) {
			j = 2 * i + 1;
		} else {
			j = 2 * i;
		}

		return j;
	}
    
	// issue is raised here*** --> (list.get(2 * i + 1) returns null for some reason
	public int MinChildEvent(int i, ArrayList<Event> list) {
		int j;
		//System.out.println("\n\n---------------------------------");
		System.out.println("i : " + i);
		System.out.println("2i : " + 2 * i);
		System.out.println("list.size() : " + list.size());
		System.out.println("2i + 1 </*=*/ list.size() : " + (2 * i + 1 </*=*/ list.size()));
		//System.out.println("j : " + j);
		System.out.println("---------------------------------");
		
		/* "2 * i + 1 <= list.size()" retourne vrai mais list.get(2 * i + 1) retourne null??
		 * ca fait pas de sens? */
		
		
		/*
		 * **Avec le '<=' qui devient un '<' la condition fonctionne comme voulue je pense**
		 * Va falloir tester plus en detail, mais je pense que l'algo du prof start son array a '1' et non '0'
		 * "Entrée: indice i > 0, taille n" (taken from pseudo-code)
		 * */
		/*if (2 * i + 1 <= list.size()) {
			System.out.println(list.size() - (2*i+1) + " (index)");
			System.out.println("list.size() : " + list.size());
		}*/
		
		if (2 * i > list.size()) {
			j = 0;
		} else if ((2 * i + 1 <= list.size()) && (list.get(2 * i + 1-1).compareTo(list.get(2 * i-1)) < 0)) {
			j = 2 * i + 1;
		} else {
			j = 2 * i;
		}
		return j;
	}
    
    public void insert(Event e) {
        int n = eventList.size() + 1;
        swim(e,n);
    }
    
    public void insert(Sim s) {
        int n = simList.size();// + 1;
        swim(s,n);
    }

    public boolean isEmptyEvent() {
    	System.out.println(eventList.isEmpty());
        return eventList.isEmpty();
    }
    
    public boolean isEmptyList(ArrayList<Sim> list) {
        return list.isEmpty();
    }

    public Event deleteMinEvent() {
        int n = eventList.size() - 1; //-1 --> because array start at 0, not 1
        
        Event r = eventList.get(0);
        Event v = eventList.get(n);
        
        // n + 1 to get actual size
        eventList.subList(n, n + 1).clear();
        //eventList.set(n - 1, null);
        
        
        //n = n - 1;
        if (n > 0) {
            sink(v, 1);
        }
        return r;
    }
    
    public Sim deleteMinSim() {
        int n = simList.size() - 1; //-1 --> because array start at 0, not 1
        
        Sim r = simList.get(0);
        Sim v = simList.get(n);
        
        // n + 1 to get actual size
        eventList.subList(n, n + 1).clear();
        //simList.set(n - 1, null);
        
        n = n - 1;
        if (n > -1) {
            sink(v, 0);
        }
        return r;
    }

    public void removeSim(Sim sim) {
        simList.remove(sim);
        int n = deadSimList.size();// + 1;
        double p = Math.floor(n/2);
        // 1 is returned if the sim in the list is greater than the one given in argument
        while (p !=0 && deadSimList.get((int) p).compareTo(sim) > 0){
            deadSimList.add(n, deadSimList.get((int) p)/*-1 ?? maybe the indexation will be bad*/);
            n = (int) p;
            p = Math.floor(n/2);//n/2 + ((n % 2 == 0) ? 0:1); // C'est le plafond de n/2: https://stackoverflow.com/questions/7139382/java-rounding-up-to-an-int-using-math-ceil
            }
        System.out.println(n);
        
        deadSimList.add(n,sim);
    }
    
}
