package ro.ubbcluj.map.thecoders.utils.events;

import ro.ubbcluj.map.thecoders.domain.User;

public class UserChangeEvent implements Event{

    private ChangeEventType type;
    private User data, oldData;

    public UserChangeEvent(ChangeEventType type, User data){
        this.type = type;
        this.data = data;
    }
    public UserChangeEvent(ChangeEventType type, User data, User oldData){
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType(){ return this.type;}
    public User getData(){ return this.data; }
    public User getOldData(){ return this.oldData; }

}
