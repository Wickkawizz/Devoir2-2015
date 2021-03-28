package BLL;

import Model.Event;
import Model.Event.EventType;
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
	int nullCount = 0;

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

			if (fondateur.getSex() == Sim.Sex.F) {
				eventQ.insert(new Event(fondateur, E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate),
						Event.EventType.Accouplement));
			}

			fondateur.setDeathTime(ageModel.randomAge(new Random()));
			// On enfile l'evenement de mort dans la timeline
			eventQ.insert(new Event(fondateur, time + fondateur.getDeathTime(), Event.EventType.Mort));

			eventQ.insert(fondateur);
			simQ.insert(fondateur);
		}
		while (!eventQ.isEmptyEvent()) {
			//output(eventQ, simQ);
			Event E = eventQ.deleteMinEvent(); // prochain événement
			if (E == null) {
				nullCount++;
				System.out.println("E is null?? Total null so far : " + nullCount);
			}
			else if (E.getTime() > Tmax) {
				System.out.println("E.getTime() : " + E.getTime());
				System.out.println("Tmax!");
				break; // arrêter à Tmax
			}
			else if (E.getSubject().getDeathTime() > E.getTime()) {
				if(E.getTime() > time)
					time = E.getTime();// rough time update
				Sim sim = E.getSubject();
				EventType eventType = E.getEvent();
				System.out.println("timefweogfwregunwrghj : " + E.getTime());
				switch (eventType) {
				case Naissance:
					//sim = new Sim(E.getMom(), E.getDad(), E.getTime(), ageModel.getRandomSex()); //Deja créé dans l'accouplement
                                        
                                        //n1
                                        // Set death time
					sim.setDeathTime(E.getTime() + ageModel.randomAge(new Random()));
                                        
					// On enfile l'evenement de mort dans la timeline
					eventQ.insert(new Event(sim, sim.getDeathTime(), Event.EventType.Mort));
                                        
                                        //n2
					if (sim.getSex() == Sim.Sex.F) {
						eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)), Event.EventType.Accouplement));
					}
                                        
                                        //n3
					//eventQ.getSimList().add(E.getSubject());
					eventQ.insert(sim);
					simQ.insert(sim);
					break;
				
				case Accouplement://aka reproduction
					
					// finalement on le fait avant le switch
					//sim = E.getSubject(); // get la mere
					
					// ***mais ce ca-ci est deja traiter avant le switch***
					//r1
					//if(sim.getDeathTime() < E.getTime()) break;// deja morte, pas de reproduction
					
					//r2
					if (sim.isMatingAge(E.getTime())) {
                                            
	                    Sim mate = ageModel.selectMate(sim, eventQ.getSimList(), E);
	                    sim.setMate(mate); // Register both mates together
	                    mate.setMate(sim);
	                    // Create baby
	                    Sim baby = new Sim(sim, mate, E.getTime() + 0.75 /*9 mois plus tard*/, ageModel.getRandomSex());
	                    Event birth = new Event(baby, sim /*mom*/, mate /*dad*/, baby.getBirthTime(), Event.EventType.Naissance);
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
						*/
	                    //r3
	                    eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)),
							Event.EventType.Accouplement));
                    	System.out.println("reproduction");
						break;
					}
                    case Mort: //aka Deaderinoo Ripperoni
                    	System.out.println("Dead");
                    	eventQ.removeSim(E.getSubject());
                    	break;
				} // else rien à faire avec E car son sujet est mort
			}
		}
		//System.out.println("done");
		output(eventQ, simQ);
		
	}
	
	private void output(PQ eventQ, PQ simQ) {
		System.out.println("+++++++++++++++++++++++++++++++\nsimQ :");
		for (var sim : simQ.getSimList()) {
			System.out.println(sim.getDeathTime());
		}
		System.out.println("simQ end\n*********************************\n");
		System.out.println("+++++++++++++++++++++++++++++++\neventQ :");
		for (var sim : eventQ.getSimList()) {
			System.out.println(sim.getDeathTime());
		}
		System.out.println("eventQ end\n*********************************\n");
		System.out.println("simQ.getSimList().size() - eventQ.getSimList().size() : " + (simQ.getSimList().size() - eventQ.getSimList().size()));
		System.out.println("TimeTimeTimeTimeTimeTimeTimeTimeTime :\n" + time + "\nTimeTimeTimeTimeTimeTimeTimeTimeTime");
	}
}
