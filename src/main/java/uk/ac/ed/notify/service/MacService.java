/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;


//Ref: https://www.jira.is.ed.ac.uk/browse/WEB007-6 
public class MacService {}

/**
 * Created by rgood on 24/07/2015.
 */
/**
 * Calculates a secure MAC (message authentication code) from an array of strings and shared secret.
 param values – Parameters must first be sorted alphabetically by parameter name, then the values of these sorted parameters passed to calculateSecureMac
 return The calculated MAC
 */
/*
@Service
public class MacService {

    //@Value("${mac.sharedsecret}")
    private String sharedSecret="secret12345";

    static Logger logger = Logger.getLogger(MacService.class);


    public String getSecureMAC (final String[] values) throws
            NoSuchAlgorithmException
    {

// concatenate param values
        final int size = values.length;
        String paramString = "";
        for(int i=0; i<size; i++)
        {
            paramString += values[i];
        }
// get md5 hash from ascii value and secret
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] hashBytes = md.digest((paramString + sharedSecret).getBytes()); md.reset();
// convert to hex
        String mac = "";
        String hexByte;

        for (int k=0; k<hashBytes.length; k++)
        {
            hexByte = Integer.toHexString(hashBytes[k] < 0 ? hashBytes[k] + 256 : hashBytes[k]);
            mac += (hexByte.length()==1) ? "0" + hexByte : hexByte;
        }
        return mac; }


        public String getTimestamp()
        {
            return Integer.toString((int)(System.currentTimeMillis() / 1000));
        }

}
*/