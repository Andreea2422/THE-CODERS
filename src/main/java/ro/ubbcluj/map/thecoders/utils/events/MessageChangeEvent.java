package ro.ubbcluj.map.thecoders.utils.events;

import ro.ubbcluj.map.thecoders.domain.Message;

public class MessageChangeEvent implements Event {
    private ChangeEventType type;
    private Message data, oldData;

    public MessageChangeEvent(ChangeEventType type){
        this.type = type;
        //this.data = data;
    }
//
//    public MessageChangeEvent(ChangeEventType type, Message data, Message oldData){
//        this.type = type;
//        this.data = data;
//        this.oldData = oldData;
//    }

    public ChangeEventType getType(){ return this.type;}
    public Message getData(){ return this.data; }
    public Message getOldData(){ return this.oldData; }

}

