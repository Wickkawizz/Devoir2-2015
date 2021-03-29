package BLL;

import Model.Event;
import Model.PQ;
import Model.Sim;

import java.util.ArrayList;
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
	ArrayList<Event> old_events = new ArrayList<Event>();
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

			//eventQ.insert(fondateur);
			simQ.insert(fondateur);
		}
		System.out.println("Time : " + time);
		System.out.println("pause");
		
		while (!eventQ.isEmptyEvent()) {
			//output(eventQ, simQ);
			Event E = eventQ.deleteMinEvent(); // prochain événement
			System.out.println(eventQ.getEventList().size());
			if (E == null) {
				nullCount++;
				System.out.println("E is null?? Total null so far : " + nullCount);
				break;
			}
			else if (E.getTime() > Tmax) {
				System.out.println("E.getTime() : " + E.getTime());
				System.out.println("Tmax!");
				break; // arrêter à Tmax
			}
			else if(E.getTime() < time) {
				System.out.println("event is too old");
			}
			else if (E.getSubject() != null && E.getSubject().getDeathTime() >= E.getTime()) {
				
				/******************************************/
				if(E.getTime() >= time)
					time = E.getTime();// rough time update
				else 
					for(int i = 0; i < 10; i++)
						System.out.println("devrait pas etre ici...");
				/******************************************/
				
				
				Sim sim = E.getSubject();

				switch (E.getEvent()) {
					case Naissance:
						//sim = new Sim(E.getMom(), E.getDad(), E.getTime(), ageModel.getRandomSex()); //Deja créé dans l'accouplement
	                                        
	                    //n1
	                    // Set death time
						sim.setDeathTime(E.getTime() + ageModel.randomAge(new Random()));
						
						System.out.println("____________________________________________________________");
						System.out.println("Current time : " + time);
						System.out.println("DeathTime : " + (E.getTime() + ageModel.randomAge(new Random())));
						System.out.println("____________________________________________________________");
						
	                                        
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
						}
						//r3
						eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)),
								Event.EventType.Accouplement));
						System.out.println("reproduction");
						break;
	                case Mort: //aka Deaderinoo Ripperoni
	                	System.out.println("Dead");
	                	eventQ.removeSim(E.getSubject());
	                	break;
				} // else rien à faire avec E car son sujet est mort
			}
			else {
				if(E.getEvent() != Event.EventType.Accouplement)
					System.out.println("devrait pas etre ici ._.");
			}
			old_events.add(E);
			if(eventQ.isEmptyEvent()) {
				System.out.println("NO MORE EVENTS. SIMULATION COMPLETED");
				System.out.println("TIME TO T_MAX : " + (Tmax - time));
			}
		}
		//System.out.println("done");
		output(eventQ, simQ, Tmax);
		
	}
	
	private void output(PQ eventQ, PQ simQ, double Tmax) {
		/*System.out.println("+++++++++++++++++++++++++++++++\nsimQ :");
		for (var sim : simQ.getSimList()) {
			System.out.println(sim.getDeathTime());
		}
		System.out.println("simQ end\n*********************************\n");
		System.out.println("+++++++++++++++++++++++++++++++\neventQ :");
		for (var sim : eventQ.getSimList()) {
			System.out.println(sim.getDeathTime());
		}
		System.out.println("eventQ end\n*********************************\n");
		System.out.println("simQ.getSimList().size() - eventQ.getSimList().size() : " + (simQ.getSimList().size() - eventQ.getSimList().size()));*/
		
		//somehow les events sont pas trier en ordre de temps???
		System.out.println("\n*********************************\nOLD_EVENTS : ");
		for (var event : old_events) {
			System.out.println(event.getTime());
		}
		System.out.println("end_OLD_EVENTS\n*********************************\n");
		System.out.println("Max time seen :\n" + time + "\n");
		System.out.println("Tmax :\n" + Tmax + "\n");
	}
}
