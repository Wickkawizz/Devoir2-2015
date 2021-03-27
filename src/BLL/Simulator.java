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
				eventQ.insert(new Event(fondateur, E.getTime() + ageModel.randomAge(new Random()),
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
					sim = new Sim(E.getMom(), E.getDad(), E.getTime(), ageModel.getRandomSex());

					if (sim.getSex() == Sim.Sex.F) {
						eventQ.insert(new Event(sim, E.getTime() + ageModel.randomAge(new Random()), Event.EventType.Accouplement));
					}

                    // Set death time
					sim.setDeathTime(E.getTime() + ageModel.randomAge(new Random()));

					// On enfile l'evenement de mort dans la timeline
					eventQ.insert(new Event(sim, time + sim.getDeathTime(), Event.EventType.Mort)); 

					// add new sim to simQ ?
					simQ.getSimList().add(sim);
					// or?
					eventQ.getSimList().add(sim);
					break;
				
				case Accouplement://aka reproduction
					sim = E.getSubject(); // get la mere
					
					// ***mais ce ca-ci est deja traiter avant le switch***
					//r1
					//if(sim.getDeathTime() < E.getTime()) break;// deja morte, pas de reproduction
					
					//r2
					if (sim.isMatingAge(E.getTime())) {
						Sim mate = sim.getMate();
						
						
						// verify if sim has mate or not
						if (mate != null && mate.isMatingAge(E.getTime())) {
							// verify infidelity
							
							
							// Create baby
							Sim baby = new Sim(sim, mate /*?*/, E.getTime(), ageModel.getRandomSex());
							simQ.getSimList().add(baby);
							eventQ.getSimList().add(baby);
						} else {
							// register x and y as mates for the future

							// no current mate --> find mate
							for (Sim potentialMate : simQ.getSimList()) {
								if (potentialMate.isInARelationship(E.getTime())) {

								}
							}

						}						
						// choose partner for reproduction accordingly
					}
					//r3
					eventQ.insert(new Event(sim, E.getTime() + ageModel.randomAge(new Random()),
							Event.EventType.Accouplement));
					break;
				
				case Mort:
					simQ.removeSim(E.getSubject());
					break;
				}
			} // else rien à faire avec E car son sujet est mort
		}
	}

	// Use AgeModel instead --> can we delete?
	// Gompertz-Makeham's law
	public Double generateDeathTime() {

		return 0.00;
	}

	// Processus de Poisson
	public Double generateReproductionTime() {

		return 0.00;
	}

}
