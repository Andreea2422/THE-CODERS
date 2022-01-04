package ro.ubbcluj.map.thecoders.services;

import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.paging.PagingRepository;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observable;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Service<ID, E extends Entity<ID>> implements Observable<UserChangeEvent> {
    private PagingRepository<Long, User> repo;
    public Service(PagingRepository<Long, User> repo){
        this.repo = repo;
    }


    public Iterable<User> getAll(){
        return repo.findAll();
    }

    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {

    }

    @Override
    public void notifyObservers(UserChangeEvent t) {

    }
}
