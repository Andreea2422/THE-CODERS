package ro.ubbcluj.map.thecoders.domain.validators;

import ro.ubbcluj.map.thecoders.domain.User;

public class UserValidator implements Validator<User> {
@Override
        public void validate(User entity) throws ValidationException {
          if(entity.getId() == null)
                  throw new ValidationException("ID cannot be null");
          if(entity.getFirstName().equals(""))
                  throw new ValidationException("First name cannot be null");
          if(entity.getLastName().equals(""))
                  throw  new ValidationException("Last name cannot be null");
          if(entity.getFirstName().contains("1") || entity.getFirstName().contains("2") || entity.getFirstName().contains("3") ||
                  entity.getFirstName().contains("4") || entity.getFirstName().contains("5") || entity.getFirstName().contains("6") ||
                  entity.getFirstName().contains("7") || entity.getFirstName().contains("8") || entity.getFirstName().contains("9") || entity.getFirstName().contains("0"))
                  throw  new ValidationException("First name cannot contain digits");
          if(entity.getLastName().contains("1") || entity.getLastName().contains("2") || entity.getLastName().contains("3") ||
                  entity.getLastName().contains("4") || entity.getLastName().contains("5") || entity.getLastName().contains("6") ||
                  entity.getLastName().contains("7") || entity.getLastName().contains("8") || entity.getLastName().contains("9") || entity.getLastName().contains("0"))
                  throw new ValidationException("Last name cannot contain digits");
        }
}
