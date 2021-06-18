import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypter {

    private static Encrypter encrypter;

    private Encrypter(){
    }

    private static Encrypter getInstance(){
        if (encrypter == null){
            encrypter  = new Encrypter();
        }
        return encrypter;
    }

    public static String encrypt(String password) {
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        md.update(password.getBytes());

        return new String(md.digest());
    }
}
