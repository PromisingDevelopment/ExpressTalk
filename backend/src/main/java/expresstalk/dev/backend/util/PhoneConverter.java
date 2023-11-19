package expresstalk.dev.backend.util;

public class PhoneConverter {
    public static String stripNonDigits(String phoneNumber) {
        return phoneNumber.replaceAll("\\D+", "");
    }

    // Converts phone number from 0951234567 to 380951234567
    public static String convertToLongFormat(String phoneNumber) {
        if(phoneNumber.startsWith("0")) {
            return "38" + phoneNumber;
        }
        else if (phoneNumber.startsWith("38")){
            return phoneNumber;
        }
        else {
            throw new RuntimeException("Invalid phone number to convert to long format.");
        }
    }

    public static String convertToDBFormat(String phoneNumber) {
        return convertToLongFormat(stripNonDigits(phoneNumber));
    }
}
