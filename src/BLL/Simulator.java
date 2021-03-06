package BLL;

import Model.Event;
import Model.PQ;
import Model.Sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
			System.out.println("nombre d'event restant : " + eventQ.getEventList().size());
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
				/******************************************/
				
				
				Sim sim = E.getSubject();

				switch (E.getEvent()) {
					case Naissance:
	                    //n1
	                    // Set death time
						sim.setDeathTime(E.getTime() + ageModel.randomAge(new Random()));
						
	                                        
						// On enfile l'evenement de mort dans la timeline
						eventQ.insert(new Event(sim, sim.getDeathTime(), Event.EventType.Mort));
	                                        
	                    //n2
						if (sim.getSex() == Sim.Sex.F) {
							eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)), Event.EventType.Accouplement));
						}

						eventQ.insert(sim);
						simQ.insert(sim);
						break;
					
					case Accouplement:
						if (sim.isMatingAge(E.getTime())) {
	                                            
		                    Sim mate = ageModel.selectMate(sim, eventQ.getSimList(), E);
		                    sim.setMate(mate); // Register both mates together
		                    mate.setMate(sim);
		                    // Create baby
		                    Sim baby = new Sim(sim, mate, E.getTime() + 0.75 /*9 mois plus tard*/, ageModel.getRandomSex());
		                    Event birth = new Event(baby, sim, mate, baby.getBirthTime(), Event.EventType.Naissance);
		                    eventQ.insert(birth);
						}
						//r3
						eventQ.insert(new Event(sim, (E.getTime() + AgeModel.randomWaitingTime(new Random(), AgeModel.reproduction_rate)),
								Event.EventType.Accouplement));
						break;
	                case Mort:
	                	eventQ.removeSim(E.getSubject());
	                	break;
				}
			}
			else {
				if(E.getEvent() == Event.EventType.Naissance)
					System.out.println("Bébé null...");
				else if(E.getEvent() == Event.EventType.Mort)
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
                coalescence(simQ.getSimList());
		
	}
	
	private void output(PQ eventQ, PQ simQ, double Tmax) {
		System.out.println("\n*********************************\nOLD_EVENTS : ");
		for (Event event : old_events) {
			System.out.println(event.getTime());
		}
		System.out.println("end_OLD_EVENTS\n*********************************\n");
		System.out.println("Max time seen :\n" + time + "\n");
		System.out.println("Tmax :\n" + Tmax + "\n");
	}

    public void coalescence(ArrayList<Sim> simQ) {
        Map<String, Sim> coalescence = new HashMap<String, Sim>();
        ArrayList<Sim> allMales = simQ;
        for (int i = 0; i < allMales.size(); i++) {
            if (allMales.get(i).getSex() == Sim.Sex.F) {
                allMales.remove(i);
            }
        }
        ArrayList<Sim> potential = new ArrayList<>();
        ArrayList<String> listOfIndexes = new ArrayList<>();
        
        for (int i = allMales.size(); i > 0; i--) {
            Sim sim = allMales.remove(i-1);
            i = allMales.size();
            if (potential.contains(sim)) {
                coalescence.putIfAbsent(sim.getSim_ident(), sim);
                if (!listOfIndexes.contains(sim.getSim_ident())) {
                    listOfIndexes.add(sim.getSim_ident());
                }

            }else {
            potential.add(sim);
            }
        }
        System.out.println("COALESCENCE OF ALL FATHERS");
        for (int i = 0; i < coalescence.size(); i++) {
            System.out.println(coalescence.get(listOfIndexes.get(i)));
        }
        
    }
}
