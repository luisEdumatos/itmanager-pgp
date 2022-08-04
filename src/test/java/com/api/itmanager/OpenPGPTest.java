package com.api.itmanager;

import com.api.itmanager.modules.pgp.OpenPGP;
import org.bouncycastle.openpgp.PGPException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
public class OpenPGPTest extends ApiItmanagerApplicationTests {
/*
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenPGPTest.class);
    private static final SecureRandom SECURE_RANDOM;
    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize a strong secure random instance", e);
        }
    }

    private OpenPGP openPgp;

    @Before
    public void setUp() {
        openPgp = new OpenPGP(SECURE_RANDOM);
    }

    @Test
    public void generateJavaKeys() throws PGPException {

        OpenPGP.ArmoredKeyPair armoredKeyPair = openPgp
                .generateKeys(2048, "Luis", "luisedu.unifal@gmail.com", "123456");

        assertThat(armoredKeyPair).hasNoNullFieldsOrProperties();

        LOGGER.info("java's private key ring:\n" + armoredKeyPair.privateKey());
        LOGGER.info("java's public key ring:\n" + armoredKeyPair.publicKey());
    }


    @Test
    public void generateAvajKeys() throws PGPException {

        OpenPGP.ArmoredKeyPair armoredKeyPair = openPgp
                .generateKeys(2048, "Luis", "luisedu.unifal@gmail.com", "123456");

        assertThat(armoredKeyPair).hasNoNullFieldsOrProperties();

        LOGGER.info("avaj's private key ring:\n" + armoredKeyPair.privateKey());
        LOGGER.info("avaj's public key ring:\n" + armoredKeyPair.publicKey());
    }

    @Test
    public void encryptSignedMessageAsJavaAndDecryptItAsAvaj() throws IOException, PGPException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {

        String unencryptedMessage = "Message from java to avaj: you're all backwards!";
        unencryptedMessage = "[{\"id\":1,\"name\":\"Cliente 1\",\"cnpj\":\"12345678912345\",\"address\":\"Rua teste\"},{\"id\":2,\"name\":\"Cliente 2\",\"cnpj\":\"12345678912345\",\"address\":\"Rua teste\"},{\"id\":3,\"name\":\"Cliente 3\",\"cnpj\":\"12345678912345\",\"address\":\"Rua teste\"}]";

        String encryptedMessage = openPgp.encryptAndSign(
                unencryptedMessage,
                "luisedu.unifal@gmail.com",
                "123456",
                OpenPGP.ArmoredKeyPair.of(LUIS_PRIVATE_KEY, LUIS_PUBLIC_KEY),
                "luismatos@frwk.com.br",
                AVAJ_PUBLIC_KEY);

        assertThat(encryptedMessage).isNotEmpty();

        LOGGER.info("java's encrypted message to avaj:\n" + encryptedMessage);

        String messageDecryptedByAvaj = openPgp.decryptAndVerify(
                encryptedMessage,
                "123456",
                OpenPGP.ArmoredKeyPair.of(AVAJ_PRIVATE_KEY, AVAJ_PUBLIC_KEY),
                "luisedu.unifal@gmail.com",
                LUIS_PUBLIC_KEY);

        System.out.println(messageDecryptedByAvaj);
        assertThat(messageDecryptedByAvaj).isEqualTo(unencryptedMessage);
    }

    private static final String LUIS_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xsBNBGLqd9sBCAC9FXEF6Qz1N/e80KV1UJpQNY/AEtiCvFjndc5XRQwJfTlN\n" +
            "EVoeL650yvU5UzZLf0J7wB8Bqympnv262CPDQxu4Ns9hzptypK6jqD415sc1\n" +
            "+KR833kCE7mPuA+7pG+PVUHsF1kQljxsgHjmev1CXlJuomOTxTXPmSUdOePK\n" +
            "91qZ1aS6ghAAs1Fh34mSi2UrG5qA9HlWf26GuLhaUda4qri37aWaBFT6e1Lo\n" +
            "8T6D7YX+ip01KELkLg2fJbCOE0rWTqN3GT0CNIpNlwuBy+ZFRvQNjewzN0Kg\n" +
            "bO34ZV77n4qbRCatibUFyoVocXfrDzzHTTtZfgFVm9v9VNyyqWx41U1XABEB\n" +
            "AAHNJ0x1aXMgRWR1YXJkbyA8bHVpc2VkdS51bmlmYWxAZ21haWwuY29tPsLA\n" +
            "jQQQAQgAIAUCYup32wYLCQcIAwIEFQgKAgQWAgEAAhkBAhsDAh4BACEJECSE\n" +
            "vUV3lhZ/FiEETuPkCfEKTmGlGFYuJIS9RXeWFn9sLgf8CBTers/QlJ25rkMW\n" +
            "QxIvH0LnNwWWn0yYUiyk/am156qIN+VNKpnpzYjR3G/Z7y4/hQRhtuC5Ye32\n" +
            "leaP2cagOIqJ+M4bKVwf2TjHlccn8vhztAdnu0E0etXNyGXDArte2Zgm44uY\n" +
            "p1xf+r0vqOwSGr4MCam7yPhT7DjV7+Yv8r1u3AXZq8VhX6IzE2VY47knChwT\n" +
            "AIvGg6pO1szgbBo+VrEtVuwuV4lEaEPrEqU/tiLxEkWGaHmXRwsrIyI2HQ07\n" +
            "cm9rd8qBxW4YMLkf+VHg7uTRGBNcvUbZ3qIudkrkn8uWuRri0mvZIFkkbboP\n" +
            "X6TYFZUsYqc6GIhYgjR3RAkCic7ATQRi6nfbAQgAuXh87Vu3Kxm5dg6QRLlp\n" +
            "qH6a+7n9C7a7BswIMiGsScM6mQ6N9T+SFiirEJ8CYgO/v7G7JEY6N9bBpwjD\n" +
            "DsZ4dl+Hz6tFP+eRgZOFJYZsn/Wp8bFCDXxgsXcHXGB9WZwl4/xwAgyxWgwp\n" +
            "eQvdYedLja6mt6opyK5y0g/GMohJqln9zTEA8/9nRyGLaXFfrIU38I5iF+S9\n" +
            "bmghZhozqRgtEN/+NHLmFvYP7RmJ9LzhThZR2bSZZ775k58x0xdI6iRQs2+g\n" +
            "qJ2+aviHLvQDJ2C7FFdiCNAsKehGBIF25JrfaForF79EywrPpB7HA+bANPTp\n" +
            "yQi6ath5+tIRUYOA8g/WdQARAQABwsB2BBgBCAAJBQJi6nfbAhsMACEJECSE\n" +
            "vUV3lhZ/FiEETuPkCfEKTmGlGFYuJIS9RXeWFn/xYwf/U/DSq/dcE/1EWN/A\n" +
            "Lrw1H62DwoKUyZpOkdeUj6D32knjJq8ZP+a5kgw2jVSd2SzdubcKpHB+kN7O\n" +
            "P5SQ1t8dAhNh9uF5h4F7rHeyegaYwgVUzxiubX7n9Dob4acgHDyjlI70oLEZ\n" +
            "eTsf4cxE2RGVduYPVGIIvxBJ9wPNy1SEft7iAL0fn8wqdBIlAXc1iNKRV2Bt\n" +
            "OSE39nvVVhoVb4x5+GtYt7Kl1TTo86ZWTlp/TYNUVagpuMkItxEeQQPlvEo+\n" +
            "97dkN/4ndHZOkpxDTAd7d3I8cP0HuI8T4O16EnPBelD2Ycw8J+ph8xDcITV9\n" +
            "dKsuqwqp5LEuii++9YfY2E6eVA==\n" +
            "=BBT3\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    private static final String LUIS_PRIVATE_KEY = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xcMGBGLqd9sBCAC9FXEF6Qz1N/e80KV1UJpQNY/AEtiCvFjndc5XRQwJfTlN\n" +
            "EVoeL650yvU5UzZLf0J7wB8Bqympnv262CPDQxu4Ns9hzptypK6jqD415sc1\n" +
            "+KR833kCE7mPuA+7pG+PVUHsF1kQljxsgHjmev1CXlJuomOTxTXPmSUdOePK\n" +
            "91qZ1aS6ghAAs1Fh34mSi2UrG5qA9HlWf26GuLhaUda4qri37aWaBFT6e1Lo\n" +
            "8T6D7YX+ip01KELkLg2fJbCOE0rWTqN3GT0CNIpNlwuBy+ZFRvQNjewzN0Kg\n" +
            "bO34ZV77n4qbRCatibUFyoVocXfrDzzHTTtZfgFVm9v9VNyyqWx41U1XABEB\n" +
            "AAH+CQMItuj/PLmqZczgc0QB/ncjpymeMhFxHIGmXFY/ExX4K5gmWmKsoftb\n" +
            "p7l+1gECN+2Lrkz7da0wL7l25sIb04c3BLzVbOz1B5TkpHZUkXsWNqSlun4s\n" +
            "EXKZ+FXNnr6hcCLGSAOLRfyXbGl1W9bGid4KD0VzsZrY1G+PuZCJv5T67dP8\n" +
            "rLiA0bB9JMaz4TT5ICv6LatiZhIBE8+/lDqMU1xLish3jThoIy20XkYYxPJW\n" +
            "kYaN5rZmntoyKyDYBetQe+jEnrXFsdj033FVLOJprMsJgktQ9t5kjvxN06qN\n" +
            "np0oWw35DGdVKKGZtcuvbfn+X5EncFvWNbwHI5Tu699vBU+0uLnc20KYj37X\n" +
            "psQj3oz+cTd+/C7+myDIfTwNqcB3bWgmvEuAqjiozy+G/LLVrTF5PfhPR503\n" +
            "Cha5KpmKqIoUDyhlaD91u/wyeAjT8gD2pcLWcejVb/jjGOB1MWobWsx4OZvS\n" +
            "QA3kdgAlt28pjhcuplxpbEEfAcavjCI2oVvO1o3ZTe5w+UARBGeO8USM8Cqv\n" +
            "PjrHuA7/34zq8Sa3x+YKS6xQjCPJL8oKcdPlPxzhdFtNF4A4TSrJouSQthfx\n" +
            "bmSFMihVcwBnmW+I5qumboQppHND1TInMjo27tbpELoxrP7TowqydNy2qmZy\n" +
            "Lgoi5OOu/ER+e0qzO8WmBIXQtwj2UqTKx9XlCAvX6cHRRsNafZkwrMzGDK73\n" +
            "V7Bg+hw5a+ID6ePv9/nkGXKnA7wkXl/5zerzANENI5apavFuC9e28udBKmOO\n" +
            "DyWraWVVRqr0cIziO9M1aAw9egumYD+1yNxGYwaojElBIwAuyGBZ8Z8Eghv+\n" +
            "KEMuFWLBVPcDsJZ1p2c6PMLEowvsPNYZqL7vwTWN7BvP0z+YkIOcZSYHd1dP\n" +
            "oWo0PLfkTo5MqyVu3SFBCNyakWVJQFHezSdMdWlzIEVkdWFyZG8gPGx1aXNl\n" +
            "ZHUudW5pZmFsQGdtYWlsLmNvbT7CwI0EEAEIACAFAmLqd9sGCwkHCAMCBBUI\n" +
            "CgIEFgIBAAIZAQIbAwIeAQAhCRAkhL1Fd5YWfxYhBE7j5AnxCk5hpRhWLiSE\n" +
            "vUV3lhZ/bC4H/AgU3q7P0JSdua5DFkMSLx9C5zcFlp9MmFIspP2pteeqiDfl\n" +
            "TSqZ6c2I0dxv2e8uP4UEYbbguWHt9pXmj9nGoDiKifjOGylcH9k4x5XHJ/L4\n" +
            "c7QHZ7tBNHrVzchlwwK7XtmYJuOLmKdcX/q9L6jsEhq+DAmpu8j4U+w41e/m\n" +
            "L/K9btwF2avFYV+iMxNlWOO5JwocEwCLxoOqTtbM4GwaPlaxLVbsLleJRGhD\n" +
            "6xKlP7Yi8RJFhmh5l0cLKyMiNh0NO3Jva3fKgcVuGDC5H/lR4O7k0RgTXL1G\n" +
            "2d6iLnZK5J/Llrka4tJr2SBZJG26D1+k2BWVLGKnOhiIWII0d0QJAonHwwYE\n" +
            "Yup32wEIALl4fO1btysZuXYOkES5aah+mvu5/Qu2uwbMCDIhrEnDOpkOjfU/\n" +
            "khYoqxCfAmIDv7+xuyRGOjfWwacIww7GeHZfh8+rRT/nkYGThSWGbJ/1qfGx\n" +
            "Qg18YLF3B1xgfVmcJeP8cAIMsVoMKXkL3WHnS42upreqKciuctIPxjKISapZ\n" +
            "/c0xAPP/Z0chi2lxX6yFN/COYhfkvW5oIWYaM6kYLRDf/jRy5hb2D+0ZifS8\n" +
            "4U4WUdm0mWe++ZOfMdMXSOokULNvoKidvmr4hy70AydguxRXYgjQLCnoRgSB\n" +
            "duSa32haKxe/RMsKz6QexwPmwDT06ckIumrYefrSEVGDgPIP1nUAEQEAAf4J\n" +
            "Awgp3/QU7S3BU+Ci808/JlCr/EDPJwH0punDcNFv+DYEw4oa8WSojWTiD6ke\n" +
            "WSYS57AOtgNONpUBrg/0IuOgkhpxPf8DmuRV/L2OkpqYDKDLguW3ldG251iE\n" +
            "uppJI0OhbeACorA4exEA2TrixoDgRwO2Nr7ADlILigpmyPLZuq6R0IU9uigu\n" +
            "boxEAVlyHabpSE1fKUsMXNxEdAdLDF53sHaluiGvteMC8QoP1ie7XGK6d4zY\n" +
            "BQ1NWeuVyDZtowr2Zk4Gq6kOfKMY3sJ5Xgv9NDS1glnG3vKqt+Rs6KK8q82R\n" +
            "GPGjFC8YdG8V3HN4lfuJJax39qansEmYCw/p0prwmT8lJqjKhMYB9L2Ylafi\n" +
            "vqb6/qKHMQ5nth+vXVXf8cDnM4QeXmsqK/rmR9OSz3vueDfjz335nuapESzP\n" +
            "bP8K1uzTnS7LKBqVR0ki/0OXaCaK1wAqKm0zyH0VpLFGkB/juZAnFHNQZZ4b\n" +
            "f6STsb8rBJGzK56O0enlQTeYZ7Mo6cW72gaKmoqLxuXb7MeBDKGS1O0sBfVy\n" +
            "atR+bau+vOwCx6PjqlHdw0zz69hLsfmd1pzZtCzF9m4NYGRK/m9B/YyLfPhO\n" +
            "cmzBpLbSDf8FAlUdsV1bdXzy6LIPVdc1InjV3Z29DFbibZPsAowsd3u+jlRH\n" +
            "3lfE6B3mYmHxa64girRlxPyohrGlwTDHTICHTCvvPQ4hI7Z1JSSxRKjZW3j0\n" +
            "EdLzs1E0mYkURd3+te2VTQn2QYRPOLGZ1IHAB6UZvS8u3Zt2chgEZmH9aE4H\n" +
            "ALg2Fm1A6l8LgtXkM5SYK55WW2cxqEZjLnmcAovebxg+29YXAsNFDP56noke\n" +
            "6logZ90l5OzJGdgGJ06+YY8ADQ2LI7zBHct2P3MgLUAHLz6z3J3mpktzPf5G\n" +
            "z40xXdB004tetshdqwdAjB2TsdXCwHYEGAEIAAkFAmLqd9sCGwwAIQkQJIS9\n" +
            "RXeWFn8WIQRO4+QJ8QpOYaUYVi4khL1Fd5YWf/FjB/9T8NKr91wT/URY38Au\n" +
            "vDUfrYPCgpTJmk6R15SPoPfaSeMmrxk/5rmSDDaNVJ3ZLN25twqkcH6Q3s4/\n" +
            "lJDW3x0CE2H24XmHgXusd7J6BpjCBVTPGK5tfuf0OhvhpyAcPKOUjvSgsRl5\n" +
            "Ox/hzETZEZV25g9UYgi/EEn3A83LVIR+3uIAvR+fzCp0EiUBdzWI0pFXYG05\n" +
            "ITf2e9VWGhVvjHn4a1i3sqXVNOjzplZOWn9Ng1RVqCm4yQi3ER5BA+W8Sj73\n" +
            "t2Q3/id0dk6SnENMB3t3cjxw/Qe4jxPg7XoSc8F6UPZhzDwn6mHzENwhNX10\n" +
            "qy6rCqnksS6KL771h9jYTp5U\n" +
            "=tKXO\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

    private static final String AVAJ_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xsBNBGLqeIMBCACggyV1nxM8bIPlsAEs3wYZ72oy18xykXcGzW7a9vyJOo4U\n" +
            "IdLvCqX437Mq+hOcgiFWVcsMIw1q9SMRUXGTECFe1kdlwGdFRFVvAxamRFdo\n" +
            "YY+sewImRjzWNadXTC0Tkz5/9ZK4PDewwD99CU/HcprrTVeWRG7UACbqTPG4\n" +
            "iKN9U9RQpRuo0jqbo/aztZB6Qr0XEaNzDmoXQrQxVwlNYwNHmuHCPMwCX+RH\n" +
            "d5oPCYq1KNSef3DKRBcLBieSmKQrjk9r4KRpYkYJwq8GSYbnVu4HJ3oYdbDj\n" +
            "BP5+ivmSDOG8W2Ol8ztThZ5pUBaHFv0cFRD5yogBz++Bd2gdiDLUEHFPABEB\n" +
            "AAHNHkFyYXVqbyA8bHVpc21hdG9zQGZyd2suY29tLmJyPsLAjQQQAQgAIAUC\n" +
            "Yup4gwYLCQcIAwIEFQgKAgQWAgEAAhkBAhsDAh4BACEJEFyH8mfE4resFiEE\n" +
            "j8dLq9/pXAWZ06amXIfyZ8Tit6x59wf+Mev5x3raUFzr6heRgyEL9Ng4/cST\n" +
            "GzcR2gWyBEh0oUzhIzL3T4rcQAiKWoGK/ALTSHxKO6NDHaEOH1bDPfMdeIoe\n" +
            "l5T48mvE4a2popF2fCss85gaIZuvIE+K1VEFw0+pmpS4m0bd1BNd4vNowfra\n" +
            "R59Wge657VksIWDkL3cTKOhCFeZEpqu2ldV4pYEFIWqvqivB31E3g3BL42so\n" +
            "9JVvO5U2kJ/S+TOhL+zMft25TsC8HfHO82ZNlPL3VR5n79GE92iQZUsca2ZM\n" +
            "ixiQxTYGRSY11Tsh9YLcw6x0Zra+3ojIyrVcuSwgje7cU3o8SfVHU4BldAKw\n" +
            "d4DC/1nq+DETk87ATQRi6niDAQgAn3k6nk079mL64XwYvemxJE6QjplCegs9\n" +
            "Qx7+KUCceiAVpDLEzv0WMIqR0gkbyB26onSyOjSb+3qCXSxbKvHkn7dEjOVb\n" +
            "HaCIMMN+SbERm+WacQBQxEP1PzzRLMs0QEg+3yPr+tYTV1GshIYuNhwbi8LK\n" +
            "Pb/L774u7DjUlhNUIxnVTC+THw8p/dmqyADJjR+UgOXEgNjPIkbUYGTxXDXQ\n" +
            "gUwLO4HEofhAW52LCglRHPg+ikj8fa/ZtX5t2w/tdE68i8fyxmXFazHUAvrW\n" +
            "AQfs5GvpPSk87JiL1Gl7x2KJcXBSFZ1XnvCMzBw1qU1FrCp9UaDfSLjSW6c0\n" +
            "bBBL4xfjOQARAQABwsB2BBgBCAAJBQJi6niDAhsMACEJEFyH8mfE4resFiEE\n" +
            "j8dLq9/pXAWZ06amXIfyZ8Tit6yVtAgAk0V7l0xeeFtZbsfS8rEAw+UbBhSc\n" +
            "abWiHNUtqNrdm+LxYcjmBpeX4kZuu9THim/aniyA6CPsKlbK4CMmFA2A/jI7\n" +
            "B/hmY5bInvJ+qkFFCJJH2M9xxxjGXD1QWtBVnIEOV63hCcQmQlcISg9pmy3i\n" +
            "zDj1WMObIBr3Wyj6CUH6zDZ344BaY8O5ZoH7rTkT+/cWWjwVpfP++tVoRE0M\n" +
            "W/NOQS/VVC8Av9CNzJSN8XxX/BRC3j4Mushv1U/pchVG0sgKocX44sE2iXGh\n" +
            "83nIgFzX80MeccCu3GyaV9TojoHX+cvHU7Y7X91PMi5Sqc7rFvh3CVLUp7to\n" +
            "wHRbjjuHRo1P2w==\n" +
            "=HdQf\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    private static final String AVAJ_PRIVATE_KEY = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xcMGBGLqeIMBCACggyV1nxM8bIPlsAEs3wYZ72oy18xykXcGzW7a9vyJOo4U\n" +
            "IdLvCqX437Mq+hOcgiFWVcsMIw1q9SMRUXGTECFe1kdlwGdFRFVvAxamRFdo\n" +
            "YY+sewImRjzWNadXTC0Tkz5/9ZK4PDewwD99CU/HcprrTVeWRG7UACbqTPG4\n" +
            "iKN9U9RQpRuo0jqbo/aztZB6Qr0XEaNzDmoXQrQxVwlNYwNHmuHCPMwCX+RH\n" +
            "d5oPCYq1KNSef3DKRBcLBieSmKQrjk9r4KRpYkYJwq8GSYbnVu4HJ3oYdbDj\n" +
            "BP5+ivmSDOG8W2Ol8ztThZ5pUBaHFv0cFRD5yogBz++Bd2gdiDLUEHFPABEB\n" +
            "AAH+CQMIaz5QMOwTwjvgNdUuItDWwQpIX5Ssh6ugR4vl9bLhwPzMn2ch6teI\n" +
            "8FUxMVb9VnTA3dGngCEAn2KRE/6PDX8P0xFv76zeOcprZqJ5XkWqHnesvcwX\n" +
            "SG44+2oWFNSClIZAtOM0YEqKaol0lb/mitkonsN6E++XN4yHD4fzdhV6T0RA\n" +
            "35DXLYqADAstT45cCdwoucB0Gf5zHrbW0A9e1hN/loxH/5DClCZQkcaYYCJ0\n" +
            "wOp3pbglqJ2B3SWvvZpcIgkvishS/7DN+rtT5Pox4MBOpVjU+lvhZprnOgDc\n" +
            "YvoJyZaKW/HhNU4PBtrd2mJvBtkpfvsSHSBgvTjA+X65/XX1At3up9MxjUqY\n" +
            "9MySpblhkJ9UMeIfFWqY/5dzR402c3/7NR5zsB6oponDn0TMLRdrYDyOWOiy\n" +
            "fgB+IrvJmLrvq8j7VgZJwwG8crQ0yFVylQbWfrWQ/qU1TfLYj8pjJDY9UBGr\n" +
            "ajxGmh1xJXdQ2fL+DDJmXsbpzY62VbzxatnC1jb0xmvp6S9iy3tlzby7qzjQ\n" +
            "tNKowU3G3lH8Xg4c3BgLhraAN3kT7OC7Z9/zOMNdqLPQ4kRUvWE6GAG9WiL7\n" +
            "mwIag+5wXp8xwRsZnw8t7AL6LfCL2JEmXWpo/bOXYx+cOKX4AGH++jIqkr9v\n" +
            "MppDHpxRHOYkGrLJyr3ijPWHqp4Kb2MLGy5RBHRfIclqGqReqXEVab0OyvdX\n" +
            "/A0QE59ch+4BKwGUfEaRAMl5bIhUxvshk+hDvW1t9XpMw+S/aW3PMz6ZSg4d\n" +
            "Wt504Z0Zpg3jh25fzSYHxmXSm4khAYKmet8o0okyQ+nRwY/ktsbNqZP73JVw\n" +
            "VSdaVrSbZL6O+hZrt+xeLNBI2gXHyAf0I0VvMqqFWQDxtUQy/bKTVoDzP7V5\n" +
            "CpB6BS874R3HP6YuqXEKyRFtYtGx/TpJzR5BcmF1am8gPGx1aXNtYXRvc0Bm\n" +
            "cndrLmNvbS5icj7CwI0EEAEIACAFAmLqeIMGCwkHCAMCBBUICgIEFgIBAAIZ\n" +
            "AQIbAwIeAQAhCRBch/JnxOK3rBYhBI/HS6vf6VwFmdOmplyH8mfE4resefcH\n" +
            "/jHr+cd62lBc6+oXkYMhC/TYOP3Ekxs3EdoFsgRIdKFM4SMy90+K3EAIilqB\n" +
            "ivwC00h8SjujQx2hDh9Wwz3zHXiKHpeU+PJrxOGtqaKRdnwrLPOYGiGbryBP\n" +
            "itVRBcNPqZqUuJtG3dQTXeLzaMH62kefVoHuue1ZLCFg5C93EyjoQhXmRKar\n" +
            "tpXVeKWBBSFqr6orwd9RN4NwS+NrKPSVbzuVNpCf0vkzoS/szH7duU7AvB3x\n" +
            "zvNmTZTy91UeZ+/RhPdokGVLHGtmTIsYkMU2BkUmNdU7IfWC3MOsdGa2vt6I\n" +
            "yMq1XLksII3u3FN6PEn1R1OAZXQCsHeAwv9Z6vgxE5PHwwYEYup4gwEIAJ95\n" +
            "Op5NO/Zi+uF8GL3psSROkI6ZQnoLPUMe/ilAnHogFaQyxM79FjCKkdIJG8gd\n" +
            "uqJ0sjo0m/t6gl0sWyrx5J+3RIzlWx2giDDDfkmxEZvlmnEAUMRD9T880SzL\n" +
            "NEBIPt8j6/rWE1dRrISGLjYcG4vCyj2/y+++Luw41JYTVCMZ1Uwvkx8PKf3Z\n" +
            "qsgAyY0flIDlxIDYzyJG1GBk8Vw10IFMCzuBxKH4QFudiwoJURz4PopI/H2v\n" +
            "2bV+bdsP7XROvIvH8sZlxWsx1AL61gEH7ORr6T0pPOyYi9Rpe8diiXFwUhWd\n" +
            "V57wjMwcNalNRawqfVGg30i40lunNGwQS+MX4zkAEQEAAf4JAwiIlyx63REe\n" +
            "JuB/A5LTdQVoJP9AQhb+c3LCj2YP206UKrmgWYSvd2I68p8pYN2+T3stmoxb\n" +
            "mc/EgPusUqjMbmKjSPCGETfwKyaMuXnT/h6z55k6kXQ1062zyHsCC3Mq2zxb\n" +
            "NPCQfpvdRKRdyHJ+t/K6NhoeU6AgX7pHbKPNjBT4stQBf59Fn/ee4z30WVk0\n" +
            "Uktj0z89IBM5ii7VqPHXfhDXvX2Y41Ju0BDjCTfl0YJ1mwT7QZ7aD+hhXJF4\n" +
            "M0nojqUop/332R8Jdnv8ka/gwPuKkiNnPZ3JaD7oh7uFXdKCjTEzt6DEmLQ3\n" +
            "fx4mFF9Tfjyx/lWG2YEeEPrFjaIMmiRhTi2WbmyUJAXAcIAQd/kL89dsSHnR\n" +
            "jOHjXGuXVZeWurUw0z5G9H3LAN4SJle/rFH3C0ZeD83En7n+nUcjdcPa+kXy\n" +
            "k+1Bp3ZsQSMkQ0jib0IX442ODPER+NFlnhTgTJ6Y0b9xG+7eosthCXtxCZpf\n" +
            "Se2ppD+WPszXHZqNwnCQIaIaUxboKd08RIiVvdZbEVwiPpOQKDehVLIi9W20\n" +
            "rSV3IDN2UNOP/Wf1OHkKz1uYrmNZMERB05abljOeTxstsReZvMUifQrWMwqw\n" +
            "ixPxeFRwGC0rxJY+q+GZLMZPvd1GgS9plUR0W30zvM+FcFtJDZ3DutOskT64\n" +
            "gWM2Af4lhC5NIjtO3Qm3GtNsw6fd48fHIEM2PaEAyjVFlHT/oLwyUyCD6gYY\n" +
            "u8cGiyqnQTWB9eoXDSsh3rDIqxaREliY6rYx/kLZEy5xnYjPLTUihBAd6Kzy\n" +
            "7gA3ZtYn9YHohfPtzf3QaGOQz1w8eeNUHkYgXmDy85z+8TsexMwcAHgMXlWb\n" +
            "zKVyJG9vlmWYHYZfvcBSR7ZjE/Y05a4BqHZqVm+TEXC8Pxxfs1WhxJ30g6LO\n" +
            "0sHXtzZnDqYfx9zCwHYEGAEIAAkFAmLqeIMCGwwAIQkQXIfyZ8Tit6wWIQSP\n" +
            "x0ur3+lcBZnTpqZch/JnxOK3rJW0CACTRXuXTF54W1lux9LysQDD5RsGFJxp\n" +
            "taIc1S2o2t2b4vFhyOYGl5fiRm671MeKb9qeLIDoI+wqVsrgIyYUDYD+MjsH\n" +
            "+GZjlsie8n6qQUUIkkfYz3HHGMZcPVBa0FWcgQ5XreEJxCZCVwhKD2mbLeLM\n" +
            "OPVYw5sgGvdbKPoJQfrMNnfjgFpjw7lmgfutORP79xZaPBWl8/761WhETQxb\n" +
            "805BL9VULwC/0I3MlI3xfFf8FELePgy6yG/VT+lyFUbSyAqhxfjiwTaJcaHz\n" +
            "eciAXNfzQx5xwK7cbJpX1OiOgdf5y8dTtjtf3U8yLlKpzusW+HcJUtSnu2jA\n" +
            "dFuOO4dGjU/b\n" +
            "=sDt9\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

/*
    @Test
    public void encryptMessageAsJavascriptAndDecryptItAsJava() throws PGPException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        String unencryptedMessage = "Message from javascript to java: how's life in the back end?";

        String encryptedMessage = openPgp.encryptAndSign(
                unencryptedMessage,
                JAVASCRIPT_USER_ID_EMAIL,
                JAVASCRIPT_PASSPHRASE,
                OpenPGP.ArmoredKeyPair.of(JAVASCRIPT_PRIVATE_KEYS, JAVASCRIPT_PUBLIC_KEYS),
                JAVA_USER_ID_EMAIL,
                JAVA_PUBLIC_KEYS);

        assertThat(encryptedMessage).isNotEmpty();

        LOGGER.info("javascript's encrypted message to java:\n" + encryptedMessage);

        String messageDecryptedByJava = openPgp.decryptAndVerify(
                encryptedMessage,
                JAVA_PASSPHRASE,
                OpenPGP.ArmoredKeyPair.of(JAVA_PRIVATE_KEYS, JAVA_PUBLIC_KEYS),
                JAVASCRIPT_USER_ID_EMAIL,
                JAVASCRIPT_PUBLIC_KEYS);

        assertThat(messageDecryptedByJava).isEqualTo(unencryptedMessage);
    }

    private static final String JAVASCRIPT_ENCRYPTED_MESSAGE_TO_JAVA = "-----BEGIN PGP MESSAGE-----\n" +
            "Version: OpenPGP.js v3.0.11\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "wcBMAw2t7NW0cb1oAQgAmSFw5kjMOoaWutOIDyE7i7T8NeZPV8Q3yAh+vqCP\n" +
            "FGSSLlEArsmAGacCdcYV1Cxanp7lgs775bjarEX8HIHrh8LuAM6cg9Tby4e7\n" +
            "NdCgJxTGQbdY9HErYYzCDJ0TOpVYAXn47SWTd2Dvdf3CdlOQkeJkgn61LekG\n" +
            "uI76y9LkrBDZhJYPL3HmG2+lK8+NKnQPt3Vg1dBks/j+9XrWXKuiYb4gWgP7\n" +
            "EtFwazpUJaRFqMFFUa9G1qPeZ6nlyMK19vL+sW5vaLRcl2iVv1WZdNU1aXBm\n" +
            "1E8pZ3b4C38QMbFq/iFrSsLxlSyBOXcmRiiddTgov1jwgq39tyGB6xbyXAE+\n" +
            "7dLA1gFr4PDnVn/Txhm11UfWy+bGWNsKBFCa5ifyBdgH7WdMVMQJuZ12HXNc\n" +
            "v14eUdc8cYSfRBUuRfnZjzkJZOBLuExbdJJIp2tGs0qTPzErGoVHi7u2hGNJ\n" +
            "NZpwD9EfguS8wzZ7xXmzD+LtoMjiRGB3EwJz46IZhXpUPOm+wydcht82p6aA\n" +
            "sBCqAK22YLltcTcOsXf9mND7GDe1tNd4N3oGV6kA1EyCJYg8T9eMOKtCg1Zp\n" +
            "joVnRq9tNV2HvhAzgDB90TJD8J97LmGZ425Ytn3QjvGS5dCUtFFjUC2KqcYv\n" +
            "+M5rxyD74V+9VbceFzVqaWIUywoRHqQfPFtgPAHfS7AqsiaabTq+KEocbwsK\n" +
            "EX5gFiPnnXBIU9xZe9AXHWty6z4srN/irAQMd+fLAXEis7ULidjnPE4u1A9L\n" +
            "jzK/q4MDXq3dbZ+KP3kbXGbfms8fErLT1UgcRvQv5tjvT25bSNlNVOSIV1LX\n" +
            "Tw5YIIyoYIo77XVqq+sORpEvy0ojcSu2HMt64403CLGVW2zEgcacITaiQu4u\n" +
            "GoCDncI=\n" +
            "=O0yP\n" +
            "-----END PGP MESSAGE-----\n";
    private static final String JAVASCRIPT_UNENCRYPTED_MESSAGE_TO_JAVA = "A message encrypted by javascript";
*/
}

