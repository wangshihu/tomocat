package com.huihui.core.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by hadoop on 2015/8/1 0001.
 */
public abstract class ManagerBase implements Mananager {
    private Random random;

    /**
     * The default message digest algorithm to use if we cannot use
     * the requested one.
     */
    protected static final String DEFAULT_ALGORITHM = "MD5";

    /**
     * A String initialization parameter used to increase the entropy of
     * the initialization of our random number generator.
     */
    protected String entropy = null;

    /**
     * The number of random bytes to include when generating a
     * session identifier.
     */
    protected static final int SESSION_ID_BYTES = 16;

    /**
     * The Java class name of the random number generator class to be used
     * when generating session identifiers.
     */
    protected String randomClass = "java.security.SecureRandom";

    /**
     * Return the MessageDigest implementation to be used when
     * creating session identifiers.
     */
    protected MessageDigest digest = null;
    protected synchronized String generateSessionId() {

// Generate a byte array containing a session identifier
        Random random = getRandom();
        byte bytes[] = new byte[SESSION_ID_BYTES];
        getRandom().nextBytes(bytes);
        bytes = getDigest().digest(bytes);

// Render the result as a String of hexadecimal digits
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10)
                result.append((char) ('0' + b1));
            else
                result.append((char) ('A' + (b1 - 10)));
            if (b2 < 10)
                result.append((char) ('0' + b2));
            else
                result.append((char) ('A' + (b2 - 10)));
        }
        return (result.toString());

    }

    /**
     * Return the random number generator instance we should use for
     * generating session identifiers.  If there is no such generator
     * currently defined, construct and seed a new one.
     */
    public synchronized Random getRandom() {

        if (this.random == null) {
            synchronized (this) {
                if (this.random == null) {
                    // Calculate the new random number generator seed
                    long seed = System.currentTimeMillis();
                    char entropy[] = getEntropy().toCharArray();
                    for (int i = 0; i < entropy.length; i++) {
                        long update = ((byte) entropy[i]) << ((i % 8) * 8);
                        seed ^= update;
                    }
                    try {
                        // Construct and seed a new random number generator
                        Class clazz = Class.forName(randomClass);
                        this.random = (Random) clazz.newInstance();
                        this.random.setSeed(seed);
                    } catch (Exception e) {
                        // Fall back to the simple case
                        this.random = new Random();
                        this.random.setSeed(seed);
                    }
                }
            }
        }

        return (this.random);
    }


    /**
     * Return the MessageDigest object to be used for calculating
     * session identifiers.  If none has been created yet, initialize
     * one the first time this method is called.
     */
    public synchronized MessageDigest getDigest() {

        if (this.digest == null) {
            try {
                this.digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                try {
                    this.digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
                } catch (NoSuchAlgorithmException f) {

                    this.digest = null;
                }
            }

        }
        return (this.digest);

    }

    /**
     * Return the entropy increaser value, or compute a semi-useful value
     * if this String has not yet been set.
     */
    public String getEntropy() {

        // Calculate a semi-useful value if this has not been set
        if (this.entropy == null)
            setEntropy(this.toString());

        return (this.entropy);

    }


    /**
     * Set the entropy increaser value.
     *
     * @param entropy The new entropy increaser value
     */
    public void setEntropy(String entropy) {

        String oldEntropy = entropy;
        this.entropy = entropy;

    }
}
