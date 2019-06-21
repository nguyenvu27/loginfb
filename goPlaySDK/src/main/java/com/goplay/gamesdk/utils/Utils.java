package com.goplay.gamesdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.goplay.gamesdk.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Utils {
//    public static final int IO_BUFFER_SIZE = 8 * 1024;
//    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.GET_ACCOUNTS};
//    public static final int REQUEST_CONTACT = 108;
//    private static final int KEY_SIZE = 32;
//    private static final int IV_SIZE = 16;
//    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
//    public static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
//    private static int ITERATION_COUNT = 1000;
//    private static int KEY_LENGTH = 256;
//    private static String DELIMITER = "]";
//    private static final int PKCS5_SALT_LENGTH = 8;

    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            deviceId = decrypt("sid.sdk", context);
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = generateRandomString(context);
                encrypt(deviceId, "sid.sdk", context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId = "";
        }
        return deviceId;
    }

    public static String generateRandomString(Context context) {

        String material = UUID.randomUUID().toString() + context.getPackageName();
        return generateHashMD5(material);
    }

    public static String generateHashMD5(String input) {
        String result = input;
        if (input != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(input.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                while (result.length() < 32)
                    result = "0" + result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

//    public static Account[] getAccounts(AccountManager am) {
//        Account[] accounts = am.getAccountsByType("com.google");
//        return accounts;
//    }

//    public static String[] getAccountNames(Account[] accounts) {
//        String[] names = new String[accounts.length];
//        for (int i = 0; i < names.length; i++) {
//            names[i] = accounts[i].name;
//        }
//        return names;
//    }

    public static String getAgencyId(Context context) {
        String agencyId = "";
        try {

            // Get AgencyID from file config
            try {
                // InputStream is = context.getAssets().open("agency.txt");
                InputStream is = context.getResources().openRawResource(R.raw.agency);

                byte[] b = new byte[is.available()];
                is.read(b);
                agencyId = new String(b);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return agencyId;
    }

//    public static boolean verifyContactPermissions(Activity activity) {
//
//        // Check if we have read or write permission
//
//        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
//
//        // int readPermission = ActivityCompat.checkSelfPermission(activity,
//        // Manifest.permission.USE_CREDENTIALS);
//
//        if (writePermission != PackageManager.PERMISSION_GRANTED) {
//
//            // We don't have permission so prompt the user
//
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_CONTACT, REQUEST_CONTACT);
//
//            return false;
//
//        } else
//
//            return true;
//
//    }


    private static boolean fileExists(String fileName, Context context) {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }

    private static void writeToFile(String fileName, byte[] bytes, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to " + fileName, e);
        }
    }


    //region code mơi

    public static String encrypt(String plaintext, String fileName, Context context) {
//        byte[] salt = generateSalt();
//        SecretKey key = deriveKeyPbkdf2(salt, "trungnn307splay2018");
        writeToFile(fileName, plaintext.getBytes(), context);
        return plaintext;
    }

    public static String decrypt(String fileName, Context context) {
        return TextUtils.isEmpty(getStringFromFile(fileName, context)) ? "" : getStringFromFile(fileName, context);
//        return TextUtils.isEmpty(getStringFromFile1(fileName, context)) ? "" : decryptPbkdf2(getStringFromFile1(fileName, context), "trungnn307splay2018");
    }

//    public static byte[] generateSalt() {
//        byte[] b = new byte[PKCS5_SALT_LENGTH];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(b);
//
//        return b;
//    }

//    public static SecretKey deriveKeyPbkdf2(byte[] salt, String password) {
//        try {
//            long start = System.currentTimeMillis();
//            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
//                    ITERATION_COUNT, KEY_LENGTH);
//            SecretKeyFactory keyFactory = SecretKeyFactory
//                    .getInstance(PBKDF2_DERIVATION_ALGORITHM);
//            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
//
//            return new SecretKeySpec(keyBytes, "AES");
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public static String encrypt(String plaintext, SecretKey key, byte[] salt) {
//        try {
//            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//
//            byte[] iv = generateIv(cipher.getBlockSize());
//            IvParameterSpec ivParams = new IvParameterSpec(iv);
//            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
//
//            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));
//
//            if (salt != null) {
//                return String.format("%s%s%s%s%s", toBase64(salt), DELIMITER,
//                        toBase64(iv), DELIMITER, toBase64(cipherText));
//            }
//
//            return String.format("%s%s%s", toBase64(iv), DELIMITER,
//                    toBase64(cipherText));
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public static byte[] generateIv(int length) {
//        byte[] b = new byte[length];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(b);
//
//        return b;
//    }

//    public static String toBase64(byte[] bytes) {
//        return Base64.encodeToString(bytes, Base64.NO_WRAP);
//    }

//    public static byte[] fromBase64(String base64) {
//        return Base64.decode(base64, Base64.NO_WRAP);
//    }


//    public static String decryptPbkdf2(String ciphertext, String password) {
//        String[] fields = ciphertext.split(DELIMITER);
//        if (fields.length != 3) {
//            throw new IllegalArgumentException("Invalid encypted text format");
//        }
//
//        byte[] salt = fromBase64(fields[0]);
//        byte[] iv = fromBase64(fields[1]);
//        byte[] cipherBytes = fromBase64(fields[2]);
//        SecretKey key = deriveKeyPbkdf2(salt, password);
//
//        return decrypt(cipherBytes, key, iv);
//    }

//    public static String decrypt(byte[] cipherBytes, SecretKey key, byte[] iv) {
//        try {
//            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//            IvParameterSpec ivParams = new IvParameterSpec(iv);
//            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
//            byte[] plaintext = cipher.doFinal(cipherBytes);
//
//            return new String(plaintext, "UTF-8");
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String getStringFromFile(String fileName, Context context) {
        if (fileExists(fileName, context)) {
            FileInputStream fin = null;
            String s = "";
            try {
                File file = new File(context.getFilesDir(), fileName);
                fin = new FileInputStream(file);
                byte[] fileContent = new byte[(int) file.length()];
                fin.read(fileContent);

                s = new String(fileContent);
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
                try {
                    if (fin != null)
                        fin.close();
                } catch (IOException ioe) {
                    System.out.println("Error while closing stream: " + ioe);
                }
            } catch (IOException ioe) {
                System.out.println("Exception while reading file " + ioe);
                try {
                    if (fin != null)
                        fin.close();
                } catch (IOException ioe1) {
                    System.out.println("Error while closing stream: " + ioe1);
                }
            } finally {
                try {
                    if (fin != null)
                        fin.close();
                } catch (IOException ioe) {
                    System.out.println("Error while closing stream: " + ioe);
                }

            }

            return s;
        } else return "";
    }

    //end region


//    public static String getAdvertiserId(Context context) {
//        //
//        AdvertisingIdClient.Info adInfo = null;
//        try {
//            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
//        } catch (IOException e) {
//            // ...
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // ...
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        }
//        return adInfo.getId();
//    }

}
