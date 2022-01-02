package ro.ubbcluj.map.thecoders.repository;

public class RepoException extends RuntimeException{
    private String msg;
    public RepoException(String message){
        this.msg = message;
    }
    public String getErrorMessage(){
        return msg;
    }
}

