/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * @author Arkapin£
 * @author WickkaWizz
 */
public class Event implements Comparable<Event>{

    
    // might change place to somewhere that generates the events instead of here
    public enum EventType {Naissance, Mort, Accouplement};
    
    Sim subject;
    Sim mom;
    Sim dad;
    Double time;
    EventType event;

    public Event(Sim subject, Double time, EventType event) {
        this.subject = subject;
        this.time = time;
        this.event = event;
    }

    public Event(Sim subject, Sim mom, Sim dad, Double time, EventType event) {
        this.subject = null;
        this.mom = mom;
        this.dad = dad;
        this.time = time;
        this.event = event;
    }
    
    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.time);
    }
    
    public Sim getSubject() {
        return subject;
    }

    public void setSubject(Sim subject) {
        this.subject = subject;
    }
    
    public Sim getMom() {
    	return mom;
    }
    
    public void setMom(Sim mom) {
    	this.mom = mom;
    }
    
    public Sim getDad() {
    	return dad;
    }
    
    public void setDad(Sim dad) {
    	this.dad = dad;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Event{" + "subject=" + subject + ", mom=" + mom + ", dad=" + dad + ", time=" + time + ", event=" + event + '}';
    }

    
}
