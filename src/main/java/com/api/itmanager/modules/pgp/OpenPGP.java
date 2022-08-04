package com.api.itmanager.modules.pgp;

import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.callbacks.KeyringConfigCallbacks;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.InMemoryKeyring;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.util.io.Streams;

import java.io.*;
import java.security.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class OpenPGP {
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private final SecureRandom secureRandom;

    public OpenPGP(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public String encryptAndSign(String unencryptedMessage,
                                 String receiverUserId,
                                 String receiverArmoredPublicKey)
            throws IOException, PGPException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {

        InMemoryKeyring keyring = keyringEnc(receiverArmoredPublicKey);

        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(encryptedOutputStream);
                OutputStream bouncyGPGOutputStream = BouncyGPG
                        .encryptToStream()
                        .withConfig(keyring)
                        .withStrongAlgorithms()
                        .toRecipient(receiverUserId)
                        .andDoNotSign()
                        .armorAsciiOutput()
                        .andWriteTo(bufferedOutputStream)
        ) {
            Streams.pipeAll(new ByteArrayInputStream(unencryptedMessage.getBytes()), bouncyGPGOutputStream);
        }

        return encryptedOutputStream.toString();
    }

    public String decryptAndVerify(String encryptedMessage,
                                   String receiverPassphrase,
                                   ArmoredKeyPair receiverArmoredKeyPair)
            throws IOException, PGPException, NoSuchProviderException {

        InMemoryKeyring keyring = keyringDec(receiverPassphrase, receiverArmoredKeyPair);

        ByteArrayOutputStream unencryptedOutputStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(unencryptedOutputStream);
                InputStream bouncyGPGInputStream = BouncyGPG
                        .decryptAndVerifyStream()
                        .withConfig(keyring)
                        .andIgnoreSignatures()
                        .fromEncryptedInputStream(new ByteArrayInputStream(encryptedMessage.getBytes(UTF_8)))
        ) {
            Streams.pipeAll(bouncyGPGInputStream, bufferedOutputStream);
        }

        return unencryptedOutputStream.toString(UTF_8.name());
    }

    private InMemoryKeyring keyringDec(String passphrase, ArmoredKeyPair armoredKeyPair)
            throws IOException, PGPException {
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(KeyringConfigCallbacks.withPassword(passphrase));
        keyring.addSecretKey(armoredKeyPair.privateKey().getBytes(UTF_8));
        keyring.addPublicKey(armoredKeyPair.publicKey().getBytes(UTF_8));

        return keyring;
    }

    private InMemoryKeyring keyringEnc(String... recipientsArmoredPublicKey) throws IOException, PGPException {
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(KeyringConfigCallbacks.withUnprotectedKeys());
        for (String recipientArmoredPublicKey : recipientsArmoredPublicKey) {
            keyring.addPublicKey(recipientArmoredPublicKey.getBytes(UTF_8));
        }
        return keyring;
    }

    public static class ArmoredKeyPair {

        private final String privateKey;
        private final String publicKey;

        private ArmoredKeyPair(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String privateKey() {
            return privateKey;
        }

        public String publicKey() {
            return publicKey;
        }

        public static ArmoredKeyPair of(String privateKey, String publicKey) {
            return new ArmoredKeyPair(privateKey, publicKey);
        }
    }

}
