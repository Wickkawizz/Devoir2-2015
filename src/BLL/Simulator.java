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
	// private ArrayList<Sim> arrayList;
	Double time = 0.00;
	AgeModel ageModel;

	public Simulator() {
		ageModel = new AgeModel();
	}

	// Teachers implementation of the run. Will have to implement functionalities
	// accordingly. Gives a good idea of how the app should function
	public void simulate(int n, double Tmax) {
		PQ eventQ = new PQ(); // file de priorité d'événements
		
		// je pense que simQ sert a rien --> on peut utiliser le simList dans eventQ non?
		PQ simQ = new PQ(); // file de priorité de sims

		for (int i = 0; i < n; i++) {
			Sim fondateur = new Sim(ageModel.getRandomSex()); // sexe au hasard, naissance à 0.0
			Event E = new Event(fondateur, this.time, Event.EventType.Naissance); // nouvel événement de naissance pour fondateur à 0.0
			eventQ.insert(E); // insertion dans la file de priorité

			fondateur.setDeathTime(ageModel.randomAge(new Random()));
			if (fondateur.getSex() == Sim.Sex.F) {
				eventQ.insert(new Event(fondateur, E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate),
						Event.EventType.Accouplement));
			}

			// On enfile l'evenement de mort dans la timeline
			eventQ.insert(new Event(fondateur, time + fondateur.getDeathTime(), Event.EventType.Mort));
		}
		while (!eventQ.isEmptyEvent(eventQ.getEventList())) {
			Event E = eventQ.deleteMinEvent(); // prochain événement
			if (E.getTime() > Tmax)
				break; // arrêter à Tmax
			if (E.getSubject().getDeathTime() > E.getTime()) {
				Sim sim;
				
				switch (E.getEvent()) {
				case Naissance:
					//sim = new Sim(E.getMom(), E.getDad(), E.getTime(), ageModel.getRandomSex()); //Deja créé dans l'accouplement
                                        
                                        //n1
                                        // Set death time
					E.getSubject().setDeathTime(E.getTime() + ageModel.randomAge(new Random()));
                                        
					// On enfile l'evenement de mort dans la timeline
					eventQ.insert(new Event(E.getSubject(), E.getSubject().getDeathTime(), Event.EventType.Mort));
                                        
                                        //n2
					if (E.getSubject().getSex() == Sim.Sex.F) {
						eventQ.insert(new Event(E.getSubject(), (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)), Event.EventType.Accouplement));
					}
                                        
                                        //n3
					eventQ.getSimList().add(E.getSubject());
					break;
				
				case Accouplement://aka reproduction
					sim = E.getSubject(); // get la mere
					
					// ***mais ce ca-ci est deja traiter avant le switch***
					//r1
					//if(sim.getDeathTime() < E.getTime()) break;// deja morte, pas de reproduction
					
					//r2
					if (sim.isMatingAge(E.getTime())) {
                                            
                                            Sim mate = ageModel.selectMate(sim, eventQ, E);
                                            sim.setMate(mate); // Register both mates together
                                            // Create baby
                                            Sim baby = new Sim(sim, mate, E.getTime() + 0.75 /*9 mois plus tard*/, ageModel.getRandomSex());
                                            Event birth = new Event(baby, sim, mate, E.getTime(), Event.EventType.Naissance);
                                            eventQ.insert(birth);
                                            
                                            //simQ.getSimList().add(baby); //we should only keep EventQ for this I think.
                                            //eventQ.getSimList().add(baby); // cela devrait etre fait dans l'evenement de Naissance
                                            
                                            
						
						// Je crois avoir simplifier tout ceci avec la methode agemodel.selectMate() (Tirer du code du prof sur la page du devoir)
                                                /*
						// verify if sim has mate or not
                                                
						if (mate != null && mate.isMatingAge(E.getTime())) {
							// verify infidelity
							
							
							// Create baby
							Sim baby = new Sim(sim, mate, E.getTime(), ageModel.getRandomSex());
							simQ.getSimList().add(baby);
							eventQ.getSimList().add(baby);
						} else {
							// register x and y as mates for the future

							// no current mate --> find mate
                                                        // Il y a un probleme avec ca par contre. Ce n'est pas des chances equiprobables. Le premier sim dans la liste va toujours etre priorise sur les autres.
							for (Sim potentialMate : simQ.getSimList()) {
								if (potentialMate.isInARelationship(E.getTime())) {
                                                                    //Verify infidelity
                                                                    //if yes -> create baby and break;
                                                                    //if no, continue to search for potential mate
								}
                                                                //If no relationship -> new mate + create baby and break;
							}

						}
						// choose partner for reproduction accordingly
					}*/
					//r3
					eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)),
							Event.EventType.Accouplement));
					break;
				}
                                case Mort: //aka Deaderinoo Ripperoni
					eventQ.removeSim(E.getSubject());
					break;
			} // else rien à faire avec E car son sujet est mort
		}
	}
}
}
