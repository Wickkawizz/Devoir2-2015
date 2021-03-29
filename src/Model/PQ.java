/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Arkapin£
 * @author WickkaWizz
 */
// PQ = Priority queue
// Custom class for a PQ. Needs to be ordered by the time property as a Min-Heap. Voir les algorithmes vu en classe pour l'implementation
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
    
    
    // https://studium.umontreal.ca/pluginfile.php/6306965/mod_resource/content/1/ift2015h21-06prezB.pdf
    // Page 19/21
    //Swim for the simList
    public void swim(Sim v, int i){
	    int p = i/2;//auto rounding
	    while(p > 0) {
	    	Sim e = simList.get(p -1);
	    	if(e.compareTo(v) <= 0) break;
	    	if(i-1 == simList.size()) break;
	    	simList.set(i -1, e);
	    	i = p;
	    	p = i >> 1; //décalage >> par 1 = div par 2
	    }
	    simList.add(i -1, v);
	    
	    /*
	     * Nous avons tous essayer mais c'est la seule facon que l'on a trouvé pour correctement classer les event (par temps)
	     * Ce n'est pas efficace, mais ca nous permet de continuer sur le reste du projet
	     * */
	    simList.sort(new Comparator<Sim>() {
	    	@Override
			public int compare(Sim o1, Sim o2) {
				return o1.compareTo(o2);
			}
	    	
		});
    }
    
    //Swim for the eventlist
    public void swim(Event v, int i){
	    int p = i/2;//auto rounding
	    while(p > 0) {
	    	Event e = eventList.get(p -1);
	    	if(e.compareTo(v) <= 0) break;
	    	if(i-1 == eventList.size()) break;
	    	eventList.set(i -1, e);
	    	i = p;
	    	p = i >> 1; //décalage >> par 1 = div par 2
	    }
	    eventList.add(i -1, v);
	    
	    /*
	     * Nous avons tous essayer mais c'est la seule facon que l'on a trouvé pour correctement classer les event (par temps)
	     * Ce n'est pas efficace, mais ca nous permet de continuer sur le reste du projet
	     * */
	    eventList.sort(new Comparator<Event>() {
	    	@Override
			public int compare(Event o1, Event o2) {
				return o1.compareTo(o2);
			}
	    	
		});
    }
    
    // https://studium.umontreal.ca/pluginfile.php/6306965/mod_resource/content/1/ift2015h21-06prezB.pdf
    // Page 20/21
    public void sink(Sim v, int i){
	    int c = MinChildSim(i, simList);
	    int n = simList.size() -1;
	    while(c <= n && c >= 1) {
	    	var e = simList.get(c -1);
	    	if(c < n) {
	    		var e2 = simList.get(c);
	    		if(e2.compareTo(e) < 0) {
	    			c++;
	    			e = e2;
	    		}
	    	}
	    	if(e.compareTo(v) >= 0) break;
	    	
	    	simList.set(i -1, e);
	    	i = c;
	    	c = i << 1; // décalage gauche = mul par 2
	    }
	    simList.set(i -1, v);
	    
	    
	    /*
	     * Nous avons tous essayer mais c'est la seule facon que l'on a trouvé pour correctement classer les sim (par deathtime)
	     * Ce n'est pas efficace, mais ca nous permet de continuer sur le reste du projet
	     * */
	    simList.sort(new Comparator<Sim>() {
	    	@Override
			public int compare(Sim o1, Sim o2) {
				return o1.compareTo(o2);
			}
	    	
		});
    }
    
    public void sink(Event v, int i){
	    //int c = 2 * i; // indice de l’enfant gauche
	    int c = MinChildEvent(i, eventList);
	    int n = eventList.size() -1;
	    while(c <= n && c >= 1) {
	    	var e = eventList.get(c -1);
	    	if(c < n) {
	    		var e2 = eventList.get(c);
	    		if(e2.compareTo(e) < 0) {
	    			c++;
	    			e = e2;
	    		}
	    	}
	    	if(e.compareTo(v) >= 0) break;
	    	
	    	eventList.set(i -1, e);
	    	i = c;
	    	c = i << 1; // décalage gauche = mul par 2
	    }
	    eventList.set(i -1, v);
	    
	    
	    /*
	     * Nous avons tous essayer mais c'est la seule facon que l'on a trouvé pour correctement classer les event (par temps)
	     * Ce n'est pas efficace, mais ca nous permet de continuer sur le reste du projet
	     * */
	    eventList.sort(new Comparator<Event>() {
	    	@Override
			public int compare(Event o1, Event o2) {
				return o1.compareTo(o2);
			}
	    	
		});
    }
    
	public int MinChildSim(int i, ArrayList<Sim> list) {		
		int j = 0;
		
		if (2 * i <= list.size() -1) {
			j = 2 * i;
		} else if ((2 * i + 1 <= list.size() -1) && list.get(2 * i).getDeathTime() < list.get(2 * i -1).getDeathTime()){
			j = 2 * i + 1;
		}
		return j;
	}
    
	public int MinChildEvent(int i, ArrayList<Event> list) {
		int j = 0;
		
		if (2 * i <= list.size() -1) {
			j = 2 * i;
		} else if ((2 * i + 1 <= list.size() -1) && list.get(2 * i).getTime() < list.get(2 * i -1).getTime()){
			j = 2 * i + 1;
		}
		return j;
	}
    
    public void insert(Event e) {
        int n = eventList.size();
        n = n + 1;
        swim(e, n);
    }
    
    public void insert(Sim s) {
        int n = simList.size();
        n = n + 1;
        swim(s, n);
    }

    public boolean isEmptyEvent() {
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
        eventList.subList(n, n + 1).clear(); //deletes last entry
        
        
        n = n - 1; // -1 parcequ on vient d enlever le dernier element
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
        eventList.subList(n, n + 1).clear(); //deletes last entry
        
        n = n - 1; // -1 parcequ on vient d enlever le dernier element
        if (n > 0) {
            sink(v, 1);
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
        
        deadSimList.add(n,sim);
    }
    
}
