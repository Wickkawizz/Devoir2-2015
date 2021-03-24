/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL;

import Model.Event;
import Model.PQ;
import Model.Sim;
import java.util.Random;
import pedigree.AgeModel;

/**
 *
 * @author WickkaWizz
 */
// this is the class that will run the simulation. Called by main
public class Simulator {
    Double time = 0.00;
    AgeModel ageModel;

    public Simulator() {
        ageModel = new AgeModel();
    }
    
    //Teachers implementation of the run. Will have to implement functionalities accordingly. Gives a good idea of how the appe should function
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
    while (!eventQ.isEmpty())
    {
       Event E = eventQ.deleteMin(); // prochain événement
       if (E.getTime() > Tmax) break; // arrêter à Tmax
       if (E.getSubject().getDeathTime() > E.getTime())
       {
          switch(E.getEvent()){
              case Naissance:
                  Sim sim = new Sim(mother, father, E.getTime(), ageModel.getRandomSex());
                  sim.setDeathTime(ageModel.randomAge(new Random()));
                  if (sim.getSex() == Sim.Sex.F) {
                      eventQ.insert(new Event(sim, E.getTime() + ageModel.randomAge(new Random()), Event.EventType.Accouplement));
                  }
                  
                  eventQ.insert(new Event(sim, time + sim.getDeathTime(), Event.EventType.Mort)); // On enfile l'evenement de mort dans la timeline
                  break;
              case Accouplement:
                  if (sim.isMatingAge(E.getTime())) {
                      //verify if sim has mate or not
                      //verify infidelity
                      //choose partner for reproduction accordingly
                      //register x and y as mates for the future
                  }
                  break;
              case Mort:
                  simQ.removeSim(sim);
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
