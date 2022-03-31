package account.model;

public interface UserDetailsDao {

    void updateFailAttempts(String username);

    void resetFailAttempts(String username);

}
