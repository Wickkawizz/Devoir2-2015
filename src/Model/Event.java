/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author WickkaWizz
 */
public class Event {
    // might change place to somewhere that generates the events instead of here
    public enum EventType {Naissance, Mort, Accouplement};
    
    Sim subject;
    Double time;
    EventType event;

    public Event(Sim subject, Double time, EventType event) {
        this.subject = subject;
        this.time = time;
        this.event = event;
    }

    public Sim getSubject() {
        return subject;
    }

    public void setSubject(Sim subject) {
        this.subject = subject;
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

    
}
