package Service;
import DAO.SocialMediaDAO;
import Model.Account;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;
    
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account createAccount(Account account){
        return socialMediaDAO.createAccount(account);
    }

}
