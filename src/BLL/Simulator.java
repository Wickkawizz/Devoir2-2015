package BLL;

import Model.Event;
import Model.PQ;
import Model.Sim;

//import java.util.ArrayList;
import java.util.Random;
import projet2.pkg2015.AgeModel;

/**
 * @author Arkapin£
 * @author WickkaWizz
 */
// this is the class that will run the simulation. Called by main
public class Simulator {
    //private ArrayList<Sim> arrayList;
	Double time = 0.00;
    AgeModel ageModel;

    public Simulator() {
        ageModel = new AgeModel();
    }
    
    //Teachers implementation of the run. Will have to implement functionalities accordingly. Gives a good idea of how the app should function
    public void simulate(int n, double Tmax){
    PQ eventQ = new PQ(); // file de priorité d'événements
    PQ simQ = new PQ(); // file de priorité de sims
    
    for (int i=0; i<n; i++)
    {
       Sim fondateur = new Sim(ageModel.getRandomSex()); // sexe au hasard, naissance à 0.0
       Event E = new Event(fondateur, this.time, Event.EventType.Naissance);//nouvel événement de naissance pour fondateur à 0.0
       
       fondateur.setDeathTime(ageModel.randomAge(new Random()));
                  if (fondateur.getSex() == Sim.Sex.F) {
                      eventQ.insert(new Event(fondateur, E.getTime() + ageModel.randomAge(new Random()), Event.EventType.Accouplement));
                  }
                  
                  eventQ.insert(new Event(fondateur, time + fondateur.getDeathTime(), Event.EventType.Mort)); // On enfile l'evenement de mort dans la timeline
                  
       eventQ.insert(E); // insertion dans la file de priorité
    }
    while (!eventQ.isEmptyEvent(eventQ.getEventList()))
    {
       Event E = eventQ.deleteMinEvent(); // prochain événement
       if (E.getTime() > Tmax) break; // arrêter à Tmax
       if (E.getSubject().getDeathTime() > E.getTime())
       {
    	  Sim sim;
          switch(E.getEvent()){
              case Naissance:
            	  // How to get/choose mother/father??
                  sim = new Sim(E.getMom(), E.getDad(), E.getTime(), ageModel.getRandomSex());
                  sim.setDeathTime(ageModel.randomAge(new Random()));
                  if (sim.getSex() == Sim.Sex.F) {
                      eventQ.insert(new Event(sim, E.getTime() + ageModel.randomAge(new Random()), Event.EventType.Accouplement));
                  }
                  
                  eventQ.insert(new Event(sim, time + sim.getDeathTime(), Event.EventType.Mort)); // On enfile l'evenement de mort dans la timeline
                  
                  // add new sim to simQ ?
                  simQ.getSimList().add(sim);
                  break;
              case Accouplement:
            	  sim = E.getSubject();
                  if (sim.isMatingAge(E.getTime())) {
                	  Sim mate = sim.getMate();
                	  //verify if sim has mate or not
                	  if(mate != null) {
                		  //verify infidelity
                	  } else {
                		  //register x and y as mates for the future
                		  
                		  // no current mate --> find mate
                		  sim.getSex();
                		  for (Sim potentialMate : simQ.getSimList()) {
                			  if(potentialMate.isInARelationship(E.getTime())) {
                				  
                			  }
                		  }
                		  
                		  
                	  }
                      //choose partner for reproduction accordingly
                  }
                  break;
              case Mort:
                  simQ.removeSim(E.getSubject());
                  break;
          }
       } // else rien à faire avec E car son sujet est mort
    }
 }
    //Use AgeModel instead
    //Gompertz-Makeham's law
    public Double generateDeathTime(){
        
        return 0.00;
    }
    
    //Processus de Poisson
    public Double generateReproductionTime(){
        
        return 0.00;
    }
    
}
