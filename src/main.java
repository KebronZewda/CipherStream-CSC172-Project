import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class main {
    static String fileName;
    static File file;
    static String secretKey;
    static String[] roundKeys = new String[10];
    static String[][] S = new String[][]{
            {"01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111", "11000101", "00110000", "00000001", "01100111", "00101011", "11111110", "11010111", "10101011", "01110110"},
            {"11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111", "11110000", "10101101", "11010100", "10100010", "10101111", "10011100", "10100100", "01110010", "11000000"},
            {"10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111", "11001100", "00110100", "10100101", "11100101", "11110001", "01110001", "11011000", "00110001", "00010101"},
            {"00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101", "10011010", "00000111", "00010010", "10000000", "11100010", "11101011", "00100111", "10110010", "01110101"},
            {"00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010", "10100000", "01010010", "00111011", "11010110", "10110011", "00101001", "11100011", "00101111", "10000100"},
            {"01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001", "01011011", "01101010", "11001011", "10111110", "00111001", "01001010", "01001100", "01011000", "11001111"},
            {"11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011", "10000101", "01000101", "11111001", "00000010", "01111111", "01010000", "00111100", "10011111", "10101000"},
            {"01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000", "11110101", "10111100", "10110110", "11011010", "00100001", "00010000", "11111111", "11110011", "11010010"},
            {"11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100", "00010111", "11000100", "10100111", "01111110", "00111101", "01100100", "01011101", "00011001", "01110011"},
            {"01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000", "10001000", "01000110", "11101110", "10111000", "00010100", "11011110", "01011110", "00001011", "11011011"},
            {"11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100", "01011100", "11000010", "11010011", "10101100", "01100010", "10010001", "10010101", "11100100", "01111001"},
            {"11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110", "10101001", "01101100", "01010110", "11110100", "11101010", "01100101", "01111010", "10101110", "00001000"},
            {"10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100", "11000110", "11101000", "11011101", "01110100", "00011111", "01001011", "10111101", "10001011", "10001010"},
            {"01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110", "00001110", "01100001", "00110101", "01010111", "10111001", "10000110", "11000001", "00011101", "10011110"},
            {"11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110", "10010100", "10011011", "00011110", "10000111", "11101001", "11001110", "01010101", "00101000", "11011111"},
            {"10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010", "01101000", "01000001", "10011001", "00101101", "00001111", "10110000", "01010100", "10111011", "00010110"}
    };

    private static final int[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    public static String toBinary(String data) {
        StringBuilder binaryData = new StringBuilder();
        for (char c : data.toCharArray()) {
            String binary = Integer.toBinaryString(c);
            binary = String.format("%8s", binary).replace(' ', '0');
            binaryData.append(binary);
        }
        return binaryData.toString();
    }
    public static String fromBinary(String binaryData) {
        StringBuilder charData = new StringBuilder();
        binaryData = binaryData.replaceAll("\\s", "");
        for (int i = 0; i < binaryData.length(); i += 8) {
            String binaryChunk = binaryData.substring(i, Math.min(i + 8, binaryData.length()));
            while (binaryChunk.length() < 8) {
                binaryChunk = "0" + binaryChunk;
            }
            int charCode = Integer.parseInt(binaryChunk, 2);
            charData.append((char) charCode);
        }
        return charData.toString();
    }
    public static String[] splitInitial(String binaryData) {
        StringBuilder splitString = new StringBuilder();
        int chunkSize = 64;
        for (int i = 0; i < binaryData.length(); i += chunkSize) {
            String binaryChunk = binaryData.substring(i, Math.min(i + chunkSize, binaryData.length()));
            while (binaryChunk.length() < chunkSize) {
                binaryChunk += "0";
            }
            splitString.append(binaryChunk).append("\n");
        }
        return splitString.toString().split("\n");
    }
    public static String[] splitHalf(String binaryInput) {
        StringBuilder halfString = new StringBuilder();

        int chunkSize = 32;
        for (int i = 0; i < binaryInput.length(); i += chunkSize) {
            String binaryChunk = binaryInput.substring(i, Math.min(i + chunkSize, binaryInput.length()));
            halfString.append(binaryChunk).append("\n");
        }
        String[] half = halfString.toString().split("\n");
        return half;
    }
    public static String shiftIt(String binary){
        return binary.substring(1) + binary.substring(0, 1);
    }

    public static String[] keyScheduleTransform(String inputKey) {
        String C0 = inputKey.substring(0, 28);
        String D0 = inputKey.substring(28, 56);

        for (int i = 0; i < 10; i++) {

            String roundKeyCandidate = shiftIt(C0) + shiftIt(D0);

            roundKeys[i] = roundKeyCandidate.substring(0, 32);

            C0 = roundKeyCandidate.substring(0, 28);
            D0 = roundKeyCandidate.substring(28, 56);
        }
        return roundKeys;
    }
    public static String sBox(String binaryInput) {
        int decimal = Integer.parseInt(binaryInput, 2);

        String hex = String.format("%02X", decimal);

        int row = 0, column = 0;
        if (hex.length() >= 2) {
            row = Integer.parseInt(hex.substring(0, 1), 16);
            column = Integer.parseInt(hex.substring(1, 2), 16);
        }

        return S[row][column];
    }

    public static String functionF(String rightHalf, String subKey){
        String xOrItResult = xorIt(rightHalf, subKey);

        String one = xOrItResult.substring(0, 8);
        String two = xOrItResult.substring(8, 16);
        String three = xOrItResult.substring(16, 24);
        String four = xOrItResult.substring(24, 32);

        String resultOne = sBox(one);
        String resultTwo = sBox(two);
        String resultThree = sBox(three);
        String resultFour = sBox(four);


        StringBuilder combined = new StringBuilder(resultOne + resultTwo + resultThree + resultFour);
        String combinedResult = combined.toString();


        return permuteIt(combinedResult);
    }

    public static String xorIt(String binary1, String binary2){
        StringBuilder resultStr = new StringBuilder();

        for (int x = 0; x < binary1.length(); x++) {
            if (binary1.charAt(x) == binary2.charAt(x)) {
                resultStr.append("0");
            } else {
                resultStr.append("1");
            }
        }

        return resultStr.toString();
    }
    public static String permuteIt (String binaryInput) {
        StringBuilder permuted = new StringBuilder();
        for (int i : P) {
            permuted.append(binaryInput.charAt(i - 1));
        }

        return permuted.toString();
    }
    public static String encryptBlock(String block, String inputKey) {
        keyScheduleTransform(inputKey);

        String[] halves = splitHalf(block);
        String leftHalf = halves[0];
        String rightHalf = halves[1];

        for (int i = 0; i < roundKeys.length; i++) {
            String functionOutput = functionF(rightHalf, roundKeys[i]);
            String newRightHalf = xorIt(leftHalf, functionOutput);
            leftHalf = rightHalf;
            rightHalf = newRightHalf;
        }

        return leftHalf + rightHalf;
    }
    public static String decryptBlock(String block, String inputKey) {
        keyScheduleTransform(inputKey);

        String[] halves = splitHalf(block);
        String leftHalf = halves[0];
        String rightHalf = halves[1];

        for (int i = roundKeys.length - 1; i >= 0; i--) {
            String functionOutput = functionF(leftHalf, roundKeys[i]);
            String newLeftHalf = xorIt(rightHalf, functionOutput);
            rightHalf = leftHalf;
            leftHalf = newLeftHalf;
        }

        return leftHalf + rightHalf;
    }

    public static String readFile() {
        StringBuilder fileContent = new StringBuilder();
        try {
            file = new File(fileName);
            Scanner s = new Scanner(file);

            while (s.hasNextLine()) {
                String data = s.nextLine();
                fileContent.append(data).append("\n");
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return fileContent.toString();
    }
    public static String encryption(String longBinaryInput, String inputKey) {
        StringBuilder encryptedAnswer = new StringBuilder();
        String[] splitted = splitInitial(longBinaryInput);
        for (int i = 0; i < splitted.length; i++) {
           encryptedAnswer.append(encryptBlock(splitted[i], inputKey));
        }
        return encryptedAnswer.toString();
    }
    public static String decryption(String longBinaryInput, String inputKey) {
        StringBuilder decryptedAnswer = new StringBuilder();
        String [] decrpytedSplit = splitInitial(longBinaryInput);
        for (int i = decrpytedSplit.length - 1; i >= 0; i--) {
            decryptedAnswer.append(decryptBlock(decrpytedSplit[i], inputKey));
        }
        return decryptedAnswer.toString();
    }
    public static void writeToFile(String data, String outputFileName) {
        try {
            FileWriter fileWriter = new FileWriter(outputFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(data);
            printWriter.close();
            fileWriter.close();
            System.out.println("Data has been written to " + outputFileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }
    }

    public static void runTests() {
        System.out.println("Output for encryption(all ones, all ones): " + encryptBlock("1111111111111111111111111111111111111111111111111111111111111111", "1111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println("Output for encryption(all zeros, all ones): " + encryptBlock("0000000000000000000000000000000000000000000000000000000000000000", "1111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println("Output for encryption(all zeros, all zeros): " + encryptBlock("0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"));
        System.out.println("Output for encryption(block, all zeros), where block = 1100110010000000000001110101111100010001100101111010001001001100: " + encryptBlock("1100110010000000000001110101111100010001100101111010001001001100", "0000000000000000000000000000000000000000000000000000000000000000"));
        System.out.println("Output for decryption(all ones, all ones): " + decryptBlock("1111111111111111111111111111111111111111111111111111111111111111", "1111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println("Output for decryption(all zeros, all ones): " + decryptBlock("0000000000000000000000000000000000000000000000000000000000000000", "1111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println("Output for decryption(all zeros, all zeros): " + decryptBlock("0000000000000000000000000000000000000000000000000000000000000000", "0000000000000000000000000000000000000000000000000000000000000000"));
        System.out.println("Output for decryption(block, all ones), where block = 0101011010001110111001000111100001001110010001100110000011110101: " + decryptBlock("0101011010001110111001000111100001001110010001100110000011110101", "1111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println("Output for decryption(block, all zeros), where block = 0011000101110111011100100101001001001101011010100110011111010111: " + decryptBlock("0011000101110111011100100101001001001101011010100110011111010111", "0000000000000000000000000000000000000000000000000000000000000000"));
    }

    public static void main (String[] args) {
        runTests();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String choice = scanner.nextLine().toUpperCase();

        if (choice.equals("E") || choice.equals("D")) {
            System.out.print("Filename: ");
            fileName = scanner.nextLine();
            String fileContent = readFile();
            System.out.print("Secret key: ");
            secretKey = scanner.nextLine();

            if (choice.equals("E")) {
                String binaryData = toBinary(fileContent);
                String answer = encryption(binaryData, secretKey);
                System.out.println(answer);
                System.out.print("Output file: ");
                String encryptedOutputName = scanner.nextLine();
                writeToFile(answer, encryptedOutputName);
            }

            else if (choice.equals("D")) {
                String decryptAnswer = fromBinary(decryption(fileContent, secretKey));
                System.out.println(decryptAnswer);
                System.out.print("Output file: ");
                String decryptedOutputName = scanner.nextLine();
                writeToFile(decryptAnswer, decryptedOutputName);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
}
