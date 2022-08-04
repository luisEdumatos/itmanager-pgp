package com.api.itmanager.modules.pgp;

import com.api.itmanager.modules.client.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class PgpMain {
    public static void main(String[] args) throws PGPException, IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        OpenPGP openPGP = new OpenPGP(SECURE_RANDOM);;

        ObjectMapper objectMapper = new ObjectMapper();

        List<Client> clients = new ArrayList<>();

        for (int i=0; i<100; i++) {
            clients.add(new Client(1L, "Cliente 1", "12345678912345", "Rua teste"));
        }

        String unencryptedMessage = objectMapper.writeValueAsString(clients);

        String encryptedMessage = encryptSignedMessage(openPGP, unencryptedMessage);

        String decryptedMessage = decryptSignedMessage(openPGP, encryptedMessage);

        System.out.println();
        System.out.println(encryptedMessage);
        System.out.println();
        System.out.println(decryptedMessage);

    }

    private static final SecureRandom SECURE_RANDOM;
    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize a strong secure random instance", e);
        }
    }

    public static String encryptSignedMessage(OpenPGP openPgp, String unencryptedMessage) throws IOException, PGPException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        String encryptedMessage = openPgp.encryptAndSign(
                unencryptedMessage,
                "infra@agenciasonora.com.br",
                SONORA_PUBLIC_KEY);

       return encryptedMessage;
    }

    public static String decryptSignedMessage(OpenPGP openPgp, String encryptedMessage) throws PGPException, IOException, NoSuchProviderException {
        String decryptedMessage = openPgp.decryptAndVerify(
                encryptedMessage,
                "12345124124268989",
                OpenPGP.ArmoredKeyPair.of(SONORA_PRIVATE_KEY, SONORA_PUBLIC_KEY));

        return decryptedMessage;
    }

    private static final String ARAUJO_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xsBNBGLqvRIBCAC8WHGuxsILyxbWlEahM5ne+q8lAwK7xM77p5GR02I8kbOQ\n" +
            "TMwAnHDi+DOColC5CWV4lVUmvb5BJ0i+T3jRFso6PpYasKbYJ4Foi3971d2m\n" +
            "iFAzpYVK3VNNtQPpaEzGbqN46xKiF0iWFRFcTDNNNWmaEiKF+/OEL/9I+uLZ\n" +
            "O+jJiXywmO0I/15t+2ljiqp1e4JfzpeA3sN2NJtRrAvsdFSbh5/0/21TRVUk\n" +
            "CQqUs0lR1+MHSxT2o9LKKPjlMJvGVATbEEm7FwOlUI5ZnA+9kvyC/+v9niUc\n" +
            "IPIFW44n8wuOotTy7PIi27/4FXZ+pR0g1aj+OOz6YjsqfO9EDxATQNjvABEB\n" +
            "AAHNHmFyYXVqbyA8bHVpc21hdG9zQGZyd2suY29tLmJyPsLAjQQQAQgAIAUC\n" +
            "Yuq9EgYLCQcIAwIEFQgKAgQWAgEAAhkBAhsDAh4BACEJEDmVHMdXKyveFiEE\n" +
            "0ZdH3ljnU5NN4OQzOZUcx1crK94XRQf9G9Q0MPPU3Y8cy19ur6ChzGKxVyVF\n" +
            "He7Oc1ZfIstrcJaMtfCqcSjrpwOkq3pUvoPLBlF2Ry64711PBwIVCtH0FvrX\n" +
            "eODtMRLs/R6mVV2pKO6rbIPmhsARBW2IZ+tzK2G0lq/UwIqoMQjpu+u1z21/\n" +
            "o2wwz90MfGV0YAf1v8aLA1SB2TxtO2LEHtnVc64mHofaUX1BdS8DpFWxcCxs\n" +
            "oWuwSudB6FOApLRiD8L7J9lBgEcmvjAnAGQUaSIeXV1lMvGQoPDgL2MWu+Kj\n" +
            "XHLq73Yb289fCjDiaH7UmNc5ZjYoVH9HL+4ze/NeY249L7Jm4X3U0DN9iGdy\n" +
            "i3iIeqZEt/0+qs7ATQRi6r0SAQgAyFB4ol6z81iEEMopXY9Rdhkq8xa7tIf+\n" +
            "iTDoSu3Q2FT0S3e1dzzjr0As6ZS7CYpdDmmWP2tS64Say+O7Ub5WwekWp5wt\n" +
            "grnSoLCITRvR8Lzkq2fBu491ThPfDbtN3xCG8FO2B62ZD6AIjH7v28CCzxyg\n" +
            "3zs0Tev15m+b4Sw0XNBcLovNTap2CUQwjH3JUvoywctmpe9hSX+oCyFdm0p+\n" +
            "MBHeF6B2dlW/TzQHlF7Tl42//BCJPBpV/GWDBZUMHsXbJ76Ndw0odWgP6Crh\n" +
            "X4T/LtdeTWw5bCxtBLuTp0buQdzc7UQ9yU9YW5bp//DD8bjrp+D+yEmvq8Ea\n" +
            "55cDGM43VwARAQABwsB2BBgBCAAJBQJi6r0SAhsMACEJEDmVHMdXKyveFiEE\n" +
            "0ZdH3ljnU5NN4OQzOZUcx1crK95FdwgArbpTwZauAJ59tJeO3AXa0r9aRwEk\n" +
            "76VbN8hRi7XnzUts9g+HGB/j1GIBqfck1SL4Ret7rgaOxGoeMXJ2RGTpgAe8\n" +
            "L0m9ayLs3mCFFnm4mr6kTdRUDIJSpHjJD9qG25uxAXFGROH2zLfwywHY9ZYw\n" +
            "/j9JfH6ASSj1OpusIWLSgb60bnKNhCNyLskpdB2R0I/rAlVQKtiQ8iLOu5I+\n" +
            "Mi6R5oxEd9HyN3bWqfT0S0Zp81tuCH9BFc6En9i/kivqNYZjL1ok5QLNNKTV\n" +
            "UFgUTSG5GqLl7h4Ke7Aw4y+8xm92af4EYBWUbbwpoNrlDUNYHvw2KtW4cDfH\n" +
            "eH1XBvkDXILhwA==\n" +
            "=MZaX\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    private static final String ARAUJO_PRIVATE_KEY = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xcMGBGLqvRIBCAC8WHGuxsILyxbWlEahM5ne+q8lAwK7xM77p5GR02I8kbOQ\n" +
            "TMwAnHDi+DOColC5CWV4lVUmvb5BJ0i+T3jRFso6PpYasKbYJ4Foi3971d2m\n" +
            "iFAzpYVK3VNNtQPpaEzGbqN46xKiF0iWFRFcTDNNNWmaEiKF+/OEL/9I+uLZ\n" +
            "O+jJiXywmO0I/15t+2ljiqp1e4JfzpeA3sN2NJtRrAvsdFSbh5/0/21TRVUk\n" +
            "CQqUs0lR1+MHSxT2o9LKKPjlMJvGVATbEEm7FwOlUI5ZnA+9kvyC/+v9niUc\n" +
            "IPIFW44n8wuOotTy7PIi27/4FXZ+pR0g1aj+OOz6YjsqfO9EDxATQNjvABEB\n" +
            "AAH+CQMIPrZzf4CHJrTgzk/8mDEPQV+/rt41b6Ol98Hc8Ecy5BBirWRBkD8Q\n" +
            "PBNCIdYTGF6JbsBtQkYSoP+Oo9apG4gi8Rlgz/vfGXMcol+bxNKcV07yPPDv\n" +
            "7IBWR3lGTD2auePpd6fUWPbCPaf93i8Fi1OiY3NXAGCfKCKijIqIwLhLpdUO\n" +
            "HhccBcPpVxYqe9r7+/m8fa26XhP9BnAJi9+5Crz5GMVFNN9A9mCIcHoTOs2z\n" +
            "WcBd4STOOSGYNO9jsoWI8HRR8JDINFQAmw6Gqa+gLshlE56FQr0OC1I4h3jw\n" +
            "0ryP0B7urci29vQayWg4Y3MWxVAzyDiXvlBMWs2z1t8yGcMl9xMrOJHUjiXq\n" +
            "zfpCez6cHnSjhdkq1aTMEFvGpDk58nyAibrB170sKZbbq+XBBkNqCYxGSLOT\n" +
            "pJxxaeeDgqdHDJjB42LGraDC0Vg+igYmo3K4S4P2iNdrcd2XjeU9t83cFBdN\n" +
            "WdqytnxB+kHJypRmY/bYjKqHZ2xKtg/DRDXhQGIbcqhOT5jl8Khawmd/lZgA\n" +
            "9cU0flgOqsTy98YdRsIjWwisjWxKSl9eAn005l3eTGUsUnY54E47OlT0YyKX\n" +
            "5a14wRlHdTJTXF02tpCIHgz6l+Ad9UloppFrWvRRlNcI3OP6SjBmU/aWsL9w\n" +
            "dNfQX4uF0gJmFB6C0zqOUu5ykNsUM/TsDWavUiQUUUArm3wCr/i1ufWxoFUl\n" +
            "hlCwSLX8kZ2s7YC6jsk7wBAUagh1TV0gZ/OiQgqeuInKSz03P96YbrVwiE2X\n" +
            "vjMLEHcDrJakpIarAMHhRhZcziAjRfBTsHDnWudFKBRB1gYsS53ktGVAr3ST\n" +
            "KLxL2HgRYIT/qpECsWt2L2y/JTu8toHEycFaKMXAcpMihlYWgysqFOSkTIPO\n" +
            "mI7VoRU21MRC9TkWRZc2KIV/HV7h0PtezR5hcmF1am8gPGx1aXNtYXRvc0Bm\n" +
            "cndrLmNvbS5icj7CwI0EEAEIACAFAmLqvRIGCwkHCAMCBBUICgIEFgIBAAIZ\n" +
            "AQIbAwIeAQAhCRA5lRzHVysr3hYhBNGXR95Y51OTTeDkMzmVHMdXKyveF0UH\n" +
            "/RvUNDDz1N2PHMtfbq+gocxisVclRR3uznNWXyLLa3CWjLXwqnEo66cDpKt6\n" +
            "VL6DywZRdkcuuO9dTwcCFQrR9Bb613jg7TES7P0eplVdqSjuq2yD5obAEQVt\n" +
            "iGfrcythtJav1MCKqDEI6bvrtc9tf6NsMM/dDHxldGAH9b/GiwNUgdk8bTti\n" +
            "xB7Z1XOuJh6H2lF9QXUvA6RVsXAsbKFrsErnQehTgKS0Yg/C+yfZQYBHJr4w\n" +
            "JwBkFGkiHl1dZTLxkKDw4C9jFrvio1xy6u92G9vPXwow4mh+1JjXOWY2KFR/\n" +
            "Ry/uM3vzXmNuPS+yZuF91NAzfYhncot4iHqmRLf9PqrHwwYEYuq9EgEIAMhQ\n" +
            "eKJes/NYhBDKKV2PUXYZKvMWu7SH/okw6Ert0NhU9Et3tXc8469ALOmUuwmK\n" +
            "XQ5plj9rUuuEmsvju1G+VsHpFqecLYK50qCwiE0b0fC85KtnwbuPdU4T3w27\n" +
            "Td8QhvBTtgetmQ+gCIx+79vAgs8coN87NE3r9eZvm+EsNFzQXC6LzU2qdglE\n" +
            "MIx9yVL6MsHLZqXvYUl/qAshXZtKfjAR3hegdnZVv080B5Re05eNv/wQiTwa\n" +
            "VfxlgwWVDB7F2ye+jXcNKHVoD+gq4V+E/y7XXk1sOWwsbQS7k6dG7kHc3O1E\n" +
            "PclPWFuW6f/ww/G466fg/shJr6vBGueXAxjON1cAEQEAAf4JAwhQN0piI59L\n" +
            "FeBICepS6TwLOrKds7soPzg8HF+OvWcQ3jbDlXi69syOCjy5P/rj97mixH+2\n" +
            "ZUVL4UhRxvaZh5wdEicJwepf2kRSn+3k56agrzg7NYyizbUOAnnwjZdYYFOs\n" +
            "5tvscKSdhhSHzOQbhyb6fYwr5tG52gVOkppYxYKn9sryA+zt11HPxJAdAdmS\n" +
            "DvHzmdo1WCO7EZ7AOdgDJkuRr8oLQs9npADGyall5b/tondaOiyl2gYF1FrM\n" +
            "GvMWKq6pgTvLLazOvHrR86T21AOJLAomjoW2Uyy+ABsgus9YgQrkEdnzMWWM\n" +
            "w9rm9p/WJBSpztR1KFmBzfx+sUhStWNwJGWcpgsE5ByEG2DR2fvykJfoYifP\n" +
            "dn3yfZP4Qqn/CDZdnLpntuT/sPB3SQeXt7M8fDpx1f3KrJbPP/P3IpbOQLHd\n" +
            "aJ9MCOu4MTawIEoF86ynYh3PDOtdAzUsQVPn+SuRUPTG1PlFS3dDeJne8J+y\n" +
            "T4h/ub+C/y22vbGt328g8Ip9ZBM4OPCXM7sT6qbAqRkALYme9yPPOrX/WNx/\n" +
            "yDH7cCEJC4aFsRDSCh43vXSlSNw7KUVn6+R6mIFM+OSkvvH1jcnCDZQ0EC+i\n" +
            "oXw82JTfmyw0Vr+HnV4GLdM4fRTlqgWrJig+iPV8BnaUURqieUaDy1TzZQcb\n" +
            "XsvMsv4eqaV0ejS373ZUmMs6skteXfmch3N1jDq5DoFQR2tI9CR8Pr/4kt6j\n" +
            "8mgTcHMlvuDK2PKVe6otCkfmzaLRe4HTdMj/ZBsJ7LdFbp4DWsJHZpHNnBF7\n" +
            "df6T0wE+Haa7oLUOjdt9R5lCcZy+5sZ7573jIEcme1DaxWCyEAdn+3Pw/0aD\n" +
            "5h76Be7QZQzKp5aJkQZjzCsMia40ix68DY7ctOB3/mO+OCjm0mN+Msk4lZ2b\n" +
            "8cR9gMJteKj43tvCwHYEGAEIAAkFAmLqvRICGwwAIQkQOZUcx1crK94WIQTR\n" +
            "l0feWOdTk03g5DM5lRzHVysr3kV3CACtulPBlq4Ann20l47cBdrSv1pHASTv\n" +
            "pVs3yFGLtefNS2z2D4cYH+PUYgGp9yTVIvhF63uuBo7Eah4xcnZEZOmAB7wv\n" +
            "Sb1rIuzeYIUWebiavqRN1FQMglKkeMkP2obbm7EBcUZE4fbMt/DLAdj1ljD+\n" +
            "P0l8foBJKPU6m6whYtKBvrRuco2EI3IuySl0HZHQj+sCVVAq2JDyIs67kj4y\n" +
            "LpHmjER30fI3dtap9PRLRmnzW24If0EVzoSf2L+SK+o1hmMvWiTlAs00pNVQ\n" +
            "WBRNIbkaouXuHgp7sDDjL7zGb3Zp/gRgFZRtvCmg2uUNQ1ge/DYq1bhwN8d4\n" +
            "fVcG+QNcguHA\n" +
            "=YE3B\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

    private static final String SONORA_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "\n" +
            "mQINBGLrt8YBEAD2zD+KMOuZOaqZWAfP3rfRQj10zn4yOYJOYfwLWcWUGiHPAh6I\n" +
            "qbNpKH6mRyvnbk8j4e4l+PePoYUqIVamDImKagmum9YFh8/GsdDP21pss6RPcbyn\n" +
            "ZIjqhrRORUCgyNsOkp+rzZxth4vU/A9CkNy3Zv/vx38uJ5Af2xsBzcrP8C1P3RoD\n" +
            "F3kR2ff18s3YGf9kvDRXrIHA5rIoeDKVPmQJT5YVw88MO4SkWokjU/W1u6i8MO0x\n" +
            "PQOglRKyskUspusxJkROgxEwCN9nxNIeRDtYKO4kAl6rEKTEzeObS7p45EfoTYc+\n" +
            "TyGAumt4soXbeThLrDfLtnsHkXI+5KuKvH63vHAqi16tNDP6TY81Byhb1vHBE5ga\n" +
            "x9MK3nlCdw3A0JjaanIUDoJhgReudxlwpwoKPHivhRR59G5tWIsDfsrGW9TuxgN7\n" +
            "TQa7Or+3D46K6hRm01PwCjYpUyCp5DQff81qW9boO6NE5BvghNgGU13f1A64i9to\n" +
            "euwD3Av0jUhk5W3jfVjPNGitqY+oCstWAQYHSq+5eJ9MIySC+rOU6DalNBF07jwl\n" +
            "N6VdxgwQSIxx7zjmhAvc/XwMOxbHIg7rMZNA8+4RMtyPyYFcSHIDGdhlw0VOThmR\n" +
            "onjTvXWj9OD9l7y+5iVxiFdbxdUfl8wnRgdmo8QQRZW56mwEdcwlOPsY8QARAQAB\n" +
            "tCR0ZXN0LTA3IDxpbmZyYUBhZ2VuY2lhc29ub3JhLmNvbS5icj6JAlQEEwEKAD4W\n" +
            "IQQmciPtC+5ZnG5c4ISvnJ1wGiuaXwUCYuu3xgIbAwUJA8PfqgULCQgHAgYVCgkI\n" +
            "CwIEFgIDAQIeAQIXgAAKCRCvnJ1wGiuaX0jcEADCPlsNnjOcK76uxP0QX6G3tFF9\n" +
            "mqMzpcELGFgAaQRT9faAJ+G87VFbLF1wnHmB6fmZ/2w2vWqbueNZwAcDXA2giDbD\n" +
            "mGF5XoG54YWhzX+pHTaF0czSvuul99km7aEl43mMZhePIIyLKwrD2a8nkkJ1TAu/\n" +
            "u2aeG35PlLF55+8iENWOTwVIKRYBNzqWNoJ0Igq3BKOrOfAvuXRHT/pfSACBUKBg\n" +
            "YkbDucd7BgqNGYpq6QA71vkLkCZTAsYlB+HM5/xyME6tFfO4RHVTwfqChRA1X1xG\n" +
            "aOQ5IMD4608A3ARQintRsrR0vuYDL41lR8nLVSEL0W4GnpimhDRJFfVrExqP8xR1\n" +
            "9izyQav061wEtZ4hiiZk6CvrBOOwKSe/gd4x1y0ACBbIl9KqtPKasX6eWuw0fqAU\n" +
            "IaUujn2vC/ZOQ4aNw1rV/8XHzGk/r9VNnS3HA4gwTZKPswsH68LfmY75LmpyRpsq\n" +
            "sPE8tswXkWvHXRSnj+Ltxf3sa5+SJl1+P2X3EotJsGFmCA/vd8C9/sR/ewTH6UL4\n" +
            "AS7UIdwI9WA8fhSCh1M3WOPmravu33uC0zAD6Y73FY3mpQcChoBVWR2PJTIhq3E6\n" +
            "OoCfmVlxfLyFyErDacPgYQLU+SuZcnys/wOKy1LgQXC6J6QA5nKwgS8VXi7W4qfg\n" +
            "aROSq/5uhrBMDoaDubkCDQRi67fGARAAzplkpsOQGfFo+HBcQDJoYrRYn0wR6J2F\n" +
            "nRoYq8Ct1Kcw4Ro4GUBjzPgmEvYJ01t+pzn+oNHDFHZ+Qaq0mSylvBrnZwdgbjl3\n" +
            "cKdjXEacPMnTpxL37caoGnp4broT/kQvFJy7cUIkrbIfVU7RUMro8LrO7zdIIs8v\n" +
            "NJQAXSoNP0+DWBUOFt7fQV++1JuJtSxwRwbV9PruZyw8f5sI1b0SPX2zRnPrCLjE\n" +
            "3ss+OgnlOjiddNJ9Kbjsf2WVerYDF9GPRSLscl9Xs75ucU5vz7O9rGDGIKrloBZE\n" +
            "0m6KvcP2pJ/QT2fczZ9+Mcr52dPFPhTHwcfFOtQ1ZuWNgRwhP3t91BI/wVqtoVBz\n" +
            "XSM/QNJpp7OxIM2NigFtp9m4f25Z9q1UTpODAhI/9xL1go0KPz3o2eVUSNG/O4OY\n" +
            "p6LXM84peqKSR1FdpH29yCAHPyhT1Fceu94cIQZX5fKiRnwmCFlPM8V3lW75NQy4\n" +
            "OsrZXsn7VLKUTyXS7lqgNVTcDUelkGVp9TwYOKCkb4qZD/ezeCHmZeq+lKgG5yNY\n" +
            "mHSAQQ+o5Az0ou6/fUDuvLhGEsTtynydLt1wDX6FdLCl/U7vdntBreyZ18mVzGHU\n" +
            "pnsoj/PC0zPVO8NDDCrlB4MsxVX88pYC7lfzEPS83eDsZ0obIqyGCOmIN84dgs/H\n" +
            "bAV3/6tMbqEAEQEAAYkCPAQYAQoAJhYhBCZyI+0L7lmcblzghK+cnXAaK5pfBQJi\n" +
            "67fGAhsMBQkDw9+qAAoJEK+cnXAaK5pf+uUQAKbxlG1a7Gv4X69svLRQkUdT+gTm\n" +
            "BCbfsvx5kn2Ei+lP8C6U/WKdAifc7SIOFv+LjVCDKIva56fcUUT1KtsPN0H7LVmC\n" +
            "+oubmkNRu8sXSF+6lWV71l3zQAUAOBojKD9h6cZ6NV4bjJMg8DPOeGWwoKwYRk+e\n" +
            "ApQpaD5MCMNsArXm6zYJiwx6XgJ/nYmvFf+IFofzajXZo0EnU5ZUK+c51ChjxJaZ\n" +
            "grzgCsRlBi/sHmydIBK1cv6fz67iB4pzP3DKtMiPkaADCV+0uqKJrHoPjCiJBVLS\n" +
            "d7lhhRlJ6k3SFxnlqj8E/9B8OBBL6uNKrRdWsgOLX7Qb2CAppT1mBdJXaCcwv2b7\n" +
            "8gDqD8S+ZRsGZnZMC0rV7WRq5LHg9Dx7G2qM5W7ByHvnVkIJq78eoeSi26vhIONx\n" +
            "8j+6cDpD/NeLxKtKlNraLTRS6a9jxLOOs9DanM2TWAGHK+ZyAL2G/jlAKkgqJbmB\n" +
            "BWhR+dJAocHzPkDhCo8RLM7LRJhArEwjo5UUhadBf0/tkIu6pID9aysYABEfkDqm\n" +
            "NbVrCbzMe6PA6oUka5nXFe9qa4rlcAAzDffM4+uAqgt/AiF/t7HlBWHhF6jnuhww\n" +
            "ih0Y9ydpwQoLYOQDORsyXgTiLRI6GkFNvFwz8SwrZmFDcWuWDIeiyh0GEwqOEHbl\n" +
            "GYJG37BjS6IXhspp\n" +
            "=ytRa\n" +
            "-----END PGP PUBLIC KEY BLOCK-----";
  /*
    private static final String SONORA_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: OpenPGP.js v4.10.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "xsBNBGLqvYkBCADF3yv14QAQV7QxhO+UPjYxY6kNaXkyN9Lixb/LVG3Og6MU\n" +
            "/ZUWso1klqgy5A/IObdZLCnB3uVPJ++BTuv34ntZV7ktzBGw6SYnBnLqQFAV\n" +
            "SpmDbIeoumRbMVwfCWmHy2HE8iviYqOgUUiFNJ1iHYu/A0F06UuvKNPXpige\n" +
            "2f7Kd4ShxPY4Sfvi7EkoVgLwA/kkj+XjCFpl36edwRKl51NtdsULQ1cwQVEH\n" +
            "3irP43Mo8jLEWjvcHH8z+tOUXMkzLm7NvMxCPgD6okJII+OmIYCNYi8+lckS\n" +
            "p7C+qnfaJxA4QPl2nDSzYHl61sY9yH0BHIFbFuPHH11wpZwtoiIUMjgpABEB\n" +
            "AAHNKnNvbm9yYSA8c2FtdWVsLmZvbnRlQGFnZW5jaWFzb25vcmEuY29tLmJy\n" +
            "PsLAjQQQAQgAIAUCYuq9iQYLCQcIAwIEFQgKAgQWAgEAAhkBAhsDAh4BACEJ\n" +
            "EGHcu+mQfbhwFiEEUDu4wFAxS5Lu8dG1Ydy76ZB9uHDxzQf5AEARdTh5zU25\n" +
            "c0WC+FYqvyfV88ktn9In+vcwtM78q18gnu81d5z2VA2XyE/94updsmnQUrHd\n" +
            "KuvQEMIPd3kZN1G2cuX9dQJk/Xkb/I2E9bcT3jUoCrL5yx4rgKLVTUjs7jzm\n" +
            "5fRp/L8P/hCe7ujPujjmrq0xPXjzTBROt5D/7+WBV9VHE2pBJ9s6kNilbza+\n" +
            "noa4NoG+YOKzan51BJGbDSrWqEHwoTCqvRyUZvtfpbxh+Hyw/qEMqp5gG66a\n" +
            "qUaBFEdc7t22BQLQ9ybrbcCMJExiLklbrzcdOPPBYLSt52cOYaAh5YnyTSQ1\n" +
            "tOFgazb/kUyPgY9VKBN7XdNezaHmR87ATQRi6r2JAQgAqBDIF/ExPDBgUsXE\n" +
            "xbLmeHBJGy3PQhwZ/408OomXBMnfyIGWrUm81GRaNxpbSbUQFpa+TySJWRO7\n" +
            "M3nxPnghSROIGxleDsIaw9JH/8Y6fFBeSK+KHjvLck8bqUesd85V729xZNyW\n" +
            "k7t7zBc1UKjWLIGVsWtGQOR1j5u0ukO9SbgTXXbr6ZTW07Lh4bQm+3DRoNTy\n" +
            "syR6+7e7L6bChvKSPbT3aqRQFMUKAbbbI1/VVz3wnYYSgSJg26+zuYoVJTAo\n" +
            "p2HzAQF0w0XLglHEI9/o/cJJO/U/wR4FyWjuhv0C1t+FVkp2ZmwehjQhIDKB\n" +
            "6qiSEOW7J4FgzA3dSHMEqb+LLQARAQABwsB2BBgBCAAJBQJi6r2JAhsMACEJ\n" +
            "EGHcu+mQfbhwFiEEUDu4wFAxS5Lu8dG1Ydy76ZB9uHDbYwf/SB+xFeCJbCBv\n" +
            "95hLhIpZxUvvANxtCiRmx3aEo4nOC7av6XA5j8almP4MCfi5YofniIx+OwJP\n" +
            "KaUFXTUpN71YyspgN5d1mf0LupVBi9AdL2rP8PfklCtX36l9HWO5gTM13nqy\n" +
            "Ylz5ysDDQOP7CdfqHLoG5YSlPGEZox20LTh0x8y5bH9uKsJCXmCez8aMEkFA\n" +
            "VSX/VuNCeRNjhb28Lp1ZzxDJBvyQZ2prrDRy93SfJUro8mA2q4d6/zM9Rw0f\n" +
            "WBPkIubCIm7BOmyXul8AdDWPoWul5408W4af4ZmwpZtWywwISVo5jGNFOkU+\n" +
            "x4n309rBLYiSAA33iaxNLkq9TUBTEQ==\n" +
            "=Plw7\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";
*/
    private static final String SONORA_PRIVATE_KEY = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
          "\n" +
          "lQcYBGLrt8YBEAD2zD+KMOuZOaqZWAfP3rfRQj10zn4yOYJOYfwLWcWUGiHPAh6I\n" +
          "qbNpKH6mRyvnbk8j4e4l+PePoYUqIVamDImKagmum9YFh8/GsdDP21pss6RPcbyn\n" +
          "ZIjqhrRORUCgyNsOkp+rzZxth4vU/A9CkNy3Zv/vx38uJ5Af2xsBzcrP8C1P3RoD\n" +
          "F3kR2ff18s3YGf9kvDRXrIHA5rIoeDKVPmQJT5YVw88MO4SkWokjU/W1u6i8MO0x\n" +
          "PQOglRKyskUspusxJkROgxEwCN9nxNIeRDtYKO4kAl6rEKTEzeObS7p45EfoTYc+\n" +
          "TyGAumt4soXbeThLrDfLtnsHkXI+5KuKvH63vHAqi16tNDP6TY81Byhb1vHBE5ga\n" +
          "x9MK3nlCdw3A0JjaanIUDoJhgReudxlwpwoKPHivhRR59G5tWIsDfsrGW9TuxgN7\n" +
          "TQa7Or+3D46K6hRm01PwCjYpUyCp5DQff81qW9boO6NE5BvghNgGU13f1A64i9to\n" +
          "euwD3Av0jUhk5W3jfVjPNGitqY+oCstWAQYHSq+5eJ9MIySC+rOU6DalNBF07jwl\n" +
          "N6VdxgwQSIxx7zjmhAvc/XwMOxbHIg7rMZNA8+4RMtyPyYFcSHIDGdhlw0VOThmR\n" +
          "onjTvXWj9OD9l7y+5iVxiFdbxdUfl8wnRgdmo8QQRZW56mwEdcwlOPsY8QARAQAB\n" +
          "AA/+KlctOXyeFtoNWiR/ppp7p8N4/q6keOJj6CWoU5wxqPB4x2aCLYkLtilT3RwN\n" +
          "vLUwj6Plsw8NDc+0pF822eMBv8TkZ1fl9mnv7BM6k0Z7M3X4ZQbKyl8VnELC8UCx\n" +
          "T0RWTKA8E4IBacsc4svhpFZs/2wE0bLEr3rrH4P8FKmlN1pps69nlIs04qql/0SK\n" +
          "Kx5+sd4WDsqSMjGB9C524yLij7RfN9mxjlQxs+6yY2S2nViWIL82GkHw+t5IaG7s\n" +
          "LjZu2C5L/ro+uyJyjX93Wujy03OmM3hSnH+M/RSjjVOHKNFlQsX2Q2rZARg6cukR\n" +
          "zO71iQoBQopKfihmVMLvaSxggrfpX1lFYT3AZk64wEvNbc+bCoXQmLUQIvlyUUIY\n" +
          "7M3OrChw5ZtWC95bqzqo5svZPI44C5pgeaOvNoyIvzCQCJLa8N0Ms/BQIeZxcX58\n" +
          "OVglE9TwvSNtIVZ4vIV6yESv9VdiINweeDL7z6ln1qhSQzxxkECr3jWswta0271n\n" +
          "xT25v1TGSZYt3ngHTir0GUCgna7fznipc0j6ikeiSWWpLR+NkGQ4FlwHWMaZVuO3\n" +
          "cN9an2riBqGbf5Ji/rJ3bQxQwid7jJxH3GXKwGcDUZbLIKzRgyzgPUxq2gyNYviw\n" +
          "7osKORfRJs6J254QICWg2IfghoG10l0qlLUw5gM5dM5N2gkIAPh88DxRTh+kphaD\n" +
          "UqBbar0Jg8qOWxVPGk4toJ9FEkpNks02ByTZzAgil6NOllglNEFxpl9jTF75A0e9\n" +
          "LnHX7+wFs+TGFyX731NoYWeaH9Rf4EX+Ohww4gS7rQ6P3r5iwSn/QNgCLtk/Pkxp\n" +
          "E6RLR+pY5XbRESmXeMzTf+68Nl3eAYyoEHSXldtFTXe7aehLiyeoyTSZN3l8skwI\n" +
          "1DbvbvY4/fjBE4BogjFhtXyxmqOo8BYVUfn+vx5IKzspmMjaGvqA/AeB0k4PWLsa\n" +
          "zmYRgVmQmakAJWu4P2rofGDyFOH507V8tNJgJwq3GrdU8+wi9bnd6DzJu7UKYW3e\n" +
          "C2jfkdUIAP5COrFOLqjfPuxrru6CIfNf3+oCmVQTB2BCGU0G98E+J4TkwQ2vwFkg\n" +
          "OPYlvC3yYqthVyp+mNULK5I/sYY7gDm+rv01rEK6QL6ciqENBhX1GSL7BWZzGO94\n" +
          "VG6m3Z8Kx/40IoRIkMRQEVaUQ7Pj2RKBRRdWWIMXiLzffO9u5doWZhnGGvacVaL3\n" +
          "e4yOP+/uD3Na+FCvWDJCqMaypxoGIedmXzdRI4qA/1/W4/n89TfJr75zpTemTbfw\n" +
          "rjKNrGWbu1q2UO4BO3gpzGliQfw41saTP0ZWfu0ae37Qsot/5/5RuJXwrvcu0KQ3\n" +
          "wRmXrTFc3g++mv5tTQE9FqLlBt0JXK0IAOo811KpQn5EWPGAZglx4gCIvKyfYar3\n" +
          "+FizCjs5R2Tnby38FTewAMUlFKnaaEKcZXDD3ptkTDU41TL/F3LvCKlN+VwRdNE5\n" +
          "ffCwOtTbdNlLdKynzah4wAQhN/e7vto6JmbJhdpgRdmsvEN3nUjatQokbK1emFFE\n" +
          "7FxdfC5J2h4L2umz/bbzd/8fdvPqNgqhjN4nkXJ/gN3Hfp7LxH5jQRq+R1LP+lTU\n" +
          "lneAWNwBt7K//l1gp6ZiL5PuFJ5fB0/JiqV5sPoTdI2PLgB4AcqkOdpG6Cpr6t/Z\n" +
          "Ccv2/ddEZKOEPN6W2cVc+wy3vlW+Es15pwFqaroliRXlKgOaCXBa9RqDsbQkdGVz\n" +
          "dC0wNyA8aW5mcmFAYWdlbmNpYXNvbm9yYS5jb20uYnI+iQJUBBMBCgA+FiEEJnIj\n" +
          "7QvuWZxuXOCEr5ydcBorml8FAmLrt8YCGwMFCQPD36oFCwkIBwIGFQoJCAsCBBYC\n" +
          "AwECHgECF4AACgkQr5ydcBorml9I3BAAwj5bDZ4znCu+rsT9EF+ht7RRfZqjM6XB\n" +
          "CxhYAGkEU/X2gCfhvO1RWyxdcJx5gen5mf9sNr1qm7njWcAHA1wNoIg2w5hheV6B\n" +
          "ueGFoc1/qR02hdHM0r7rpffZJu2hJeN5jGYXjyCMiysKw9mvJ5JCdUwLv7tmnht+\n" +
          "T5SxeefvIhDVjk8FSCkWATc6ljaCdCIKtwSjqznwL7l0R0/6X0gAgVCgYGJGw7nH\n" +
          "ewYKjRmKaukAO9b5C5AmUwLGJQfhzOf8cjBOrRXzuER1U8H6goUQNV9cRmjkOSDA\n" +
          "+OtPANwEUIp7UbK0dL7mAy+NZUfJy1UhC9FuBp6YpoQ0SRX1axMaj/MUdfYs8kGr\n" +
          "9OtcBLWeIYomZOgr6wTjsCknv4HeMdctAAgWyJfSqrTymrF+nlrsNH6gFCGlLo59\n" +
          "rwv2TkOGjcNa1f/Fx8xpP6/VTZ0txwOIME2Sj7MLB+vC35mO+S5qckabKrDxPLbM\n" +
          "F5Frx10Up4/i7cX97GufkiZdfj9l9xKLSbBhZggP73fAvf7Ef3sEx+lC+AEu1CHc\n" +
          "CPVgPH4UgodTN1jj5q2r7t97gtMwA+mO9xWN5qUHAoaAVVkdjyUyIatxOjqAn5lZ\n" +
          "cXy8hchKw2nD4GEC1PkrmXJ8rP8DistS4EFwuiekAOZysIEvFV4u1uKn4GkTkqv+\n" +
          "boawTA6Gg7mdBxgEYuu3xgEQAM6ZZKbDkBnxaPhwXEAyaGK0WJ9MEeidhZ0aGKvA\n" +
          "rdSnMOEaOBlAY8z4JhL2CdNbfqc5/qDRwxR2fkGqtJkspbwa52cHYG45d3CnY1xG\n" +
          "nDzJ06cS9+3GqBp6eG66E/5ELxScu3FCJK2yH1VO0VDK6PC6zu83SCLPLzSUAF0q\n" +
          "DT9Pg1gVDhbe30FfvtSbibUscEcG1fT67mcsPH+bCNW9Ej19s0Zz6wi4xN7LPjoJ\n" +
          "5To4nXTSfSm47H9llXq2AxfRj0Ui7HJfV7O+bnFOb8+zvaxgxiCq5aAWRNJuir3D\n" +
          "9qSf0E9n3M2ffjHK+dnTxT4Ux8HHxTrUNWbljYEcIT97fdQSP8FaraFQc10jP0DS\n" +
          "aaezsSDNjYoBbafZuH9uWfatVE6TgwISP/cS9YKNCj896NnlVEjRvzuDmKei1zPO\n" +
          "KXqikkdRXaR9vcggBz8oU9RXHrveHCEGV+XyokZ8JghZTzPFd5Vu+TUMuDrK2V7J\n" +
          "+1SylE8l0u5aoDVU3A1HpZBlafU8GDigpG+KmQ/3s3gh5mXqvpSoBucjWJh0gEEP\n" +
          "qOQM9KLuv31A7ry4RhLE7cp8nS7dcA1+hXSwpf1O73Z7Qa3smdfJlcxh1KZ7KI/z\n" +
          "wtMz1TvDQwwq5QeDLMVV/PKWAu5X8xD0vN3g7GdKGyKshgjpiDfOHYLPx2wFd/+r\n" +
          "TG6hABEBAAEAD/oC0bGsYfoFCDTAW7XyJTXxL3VcbBsI9DnpS9yj3ryiGWUK4OYv\n" +
          "0WxjNNlXjb55B4dpIQFi89kASufvZBsz68aE4SgAvjqM9BekawMczg5a+OT2I38/\n" +
          "iH2Z/RguwgPpYFUO1RIsnUq3Yr+eDoKmIkF70Nknk/B1+by5Sd7Rs22SZfavx9Ur\n" +
          "j19wxEDFsV8kTWuCGDTo2dhq6KRYZR1OX4ngjeCGWmkHc0NldMf5njgZaEHp+o02\n" +
          "007WPX5YDUrp9lDPPToiF+ZQMRqo82Yf1lvFC5Eci3Rxb8H0Tj0yRUEOXBHPvMYB\n" +
          "rMnZxFbBmwvWzGbXs5ufsUGOynZY0wrqZrhP1QGPzlCSOYljkGcyl96duYvYa0OX\n" +
          "qZx2RZ6zVlfA8l7QGbMxF73o1hL/W4vF1NXqSb4+Ti7pVKF3AhZCFdg6fu7ficma\n" +
          "c7GsaFexiGhEOkkEFkQNDtbjT7tQtRiRviLgPqSWdu58nzrVg+ZRW4F4AwlNfXcC\n" +
          "jcY5l51NZBSRIgzJfuIgxVvzM3YPLAH8NFO3htmmaN7ku5YWQSzM2iGgd9pUgvMY\n" +
          "JuBkiGH/VABJdTe2mCOLIfaW9yMzO3VU0Vk3ej/S0ztdGZATi7zXHwX477FMHXcX\n" +
          "BM5I8NXgAp0Hrk/PmzbRvUMNqPkhgtO9A5PWuUPzYPqVVC+7tYxtXLVAqQgA4Xaw\n" +
          "waIsNkfL+pgHecnUZsQE7wM0WO1AhVuwBQ6MqVx/SzYYDhnLtKZF3D15M5Fqohvu\n" +
          "9Hm0GokxKiU7RiBW+Msmm58D0bNpzP73aVka5PD1nMNHX2RklhAFdt7fAGGev1R/\n" +
          "YKX6mHqM57QBcixX+wd/gMcXknwuvaW3Y/2pCNh89ryWOgMi4vLAVDcOSw1eD6/A\n" +
          "r4wJRNFVXE21QSOWEIbqPX9kKShCnODGO15AmMYa4nv2TNk2o6a2z7Y4k2pUu1QY\n" +
          "WVu2HlVTMArjklSbi3MzXyn/TeteLG605lqPp7gGUkvp1Z4/WGpJBihpneCrl76N\n" +
          "gf7VeNVNWMNW5z+BGQgA6pShwzmxOuLreH1CL9YCh96gaiYTrEmeBMC6pQiaTwnR\n" +
          "J14rvYMwwxKL/sNDAdVR+amRxYG6ApJPi8b7xcWkAcjh1840F6IcMURcL2VgezvY\n" +
          "XeQj4Fk8lKoeCSgIjAOQy9DEtUZS6vfA0GhXbpohYQPXAPzOAU6NignXVhhqo1lI\n" +
          "ybhlwIhriFYgNsDR+qdKfk6yurIhFT/glHxAcDWfbzM2O5RNdYsIBArJL1UqokWs\n" +
          "W9fv+Ud/VT0ss7iFsxnrTDlaSPKEyH0gNJG+ikv4XDSLAqT+5QhVZJCFR1LkZY9t\n" +
          "CtzgbwaJviZcQP/xmOWlJGwJXzeooc7RQJDQ1TjiyQf/aEzZqG9sBx1wxtyCWVW5\n" +
          "QKeGlb/7Qqb238YIJ+W8gFxYjF/9ROaL1+vM+wsoJd8c4qRTRe4XYWvvpRt7kn7P\n" +
          "XKv0FUa6jv4yIbTdR6oDbE/XoW3fr3Mx5yjcm1d0CFUB9S98Y6CZ8Qeo0WHchyb6\n" +
          "fe1GXicotr5s3Ta1LoAcogG6zPS6JwnB9eF1wVLqkqXYe5KLCZ0jo1EaDCvuwg3W\n" +
          "Jm2Y0mVNKXpTsbcnblcWrJEpfE4aQl4mHPNBTEN1Ke5hIiDaDbshyFt2J2ed7Txa\n" +
          "TrEQLuCyQ6FkVDfSx2QpNyYjGCMzT3OBMhuEf4IIoV3dIN0kfcphe+S3rliMj2Ky\n" +
          "WXC2iQI8BBgBCgAmFiEEJnIj7QvuWZxuXOCEr5ydcBorml8FAmLrt8YCGwwFCQPD\n" +
          "36oACgkQr5ydcBorml/65RAApvGUbVrsa/hfr2y8tFCRR1P6BOYEJt+y/HmSfYSL\n" +
          "6U/wLpT9Yp0CJ9ztIg4W/4uNUIMoi9rnp9xRRPUq2w83QfstWYL6i5uaQ1G7yxdI\n" +
          "X7qVZXvWXfNABQA4GiMoP2Hpxno1XhuMkyDwM854ZbCgrBhGT54ClCloPkwIw2wC\n" +
          "tebrNgmLDHpeAn+dia8V/4gWh/NqNdmjQSdTllQr5znUKGPElpmCvOAKxGUGL+we\n" +
          "bJ0gErVy/p/PruIHinM/cMq0yI+RoAMJX7S6oomseg+MKIkFUtJ3uWGFGUnqTdIX\n" +
          "GeWqPwT/0Hw4EEvq40qtF1ayA4tftBvYICmlPWYF0ldoJzC/ZvvyAOoPxL5lGwZm\n" +
          "dkwLStXtZGrkseD0PHsbaozlbsHIe+dWQgmrvx6h5KLbq+Eg43HyP7pwOkP814vE\n" +
          "q0qU2totNFLpr2PEs46z0NqczZNYAYcr5nIAvYb+OUAqSColuYEFaFH50kChwfM+\n" +
          "QOEKjxEszstEmECsTCOjlRSFp0F/T+2Qi7qkgP1rKxgAER+QOqY1tWsJvMx7o8Dq\n" +
          "hSRrmdcV72priuVwADMN98zj64CqC38CIX+3seUFYeEXqOe6HDCKHRj3J2nBCgtg\n" +
          "5AM5GzJeBOItEjoaQU28XDPxLCtmYUNxa5YMh6LKHQYTCo4QduUZgkbfsGNLoheG\n" +
          "ymk=\n" +
          "=62eJ\n" +
          "-----END PGP PRIVATE KEY BLOCK-----";

}
