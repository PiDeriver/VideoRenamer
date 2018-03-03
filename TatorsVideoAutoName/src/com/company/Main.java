package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * original author - Kevin Sikes 2017-2018
 * @author Kevin Sikes
 * @version 3/3/2018
 *
 * Prompts user for file paths and renames files based on their specifications. Currently incomplete but will work very well for qualification matches
 */
public class Main {
    static String startFileDirectory;
    static File startFileFiles;
    static String videoDirectory;
    static String qualDir;
    static File qualFolder;
    static String quarterDir;
    static File quarterFolder;
    static String semiDir;
    static File semiFolder;
    static String finalDir;
    static File finalFolder;
    static File video;
    static boolean everyOther;

    /**
     * prompts the user for file paths and names so the videos can be renamed
     * @param args empty string array to start main method
     * @exception NumberFormatException thrown at some incorrect user answers
     * @exception IOException thrown when fails to move or find files
     * @exception Exception thrown when it fails to create folders
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String statement;

        //prompt file path
        while(true) {
            System.out.println("What is the file directory for unnamed videos?");
            startFileDirectory = input.nextLine();
            System.out.println("is this the correct file path?" + startFileDirectory);
            statement = input.nextLine();
            if(statement.toLowerCase().equals("yes")){
                startFileFiles = new File(startFileDirectory);
                break;
            }
            else if(statement.toLowerCase().equals("no")){

            }
            else{
                System.out.println("Please input a valid answer");
            }
        }
        //prompt file storage directory
        while(true){
            System.out.println("What is the output file directory?");
            videoDirectory = input.nextLine();
            System.out.println("is this the correct file path?" + videoDirectory);
            statement = input.nextLine();
            if(statement.toLowerCase().equals("yes")){
                try{
                    qualDir = videoDirectory+"\\qualification";
                    qualFolder = new File(qualDir);
                    qualFolder.mkdirs();

                    quarterDir = videoDirectory+"\\quarter-final";
                    quarterFolder = new File(quarterDir);
                    quarterFolder.mkdirs();

                    semiDir = videoDirectory+"\\semi-final";
                    semiFolder = new File(semiDir);
                    semiFolder.mkdirs();

                    finalDir = videoDirectory+"\\final";
                    finalFolder = new File(finalDir);
                    finalFolder.mkdirs();
                }
                catch (Exception e){
                    System.out.println("folderCreationFailure");
                }
                break;
            }
            else if(statement.toLowerCase().equals("no")){

            }
            else{
                System.out.println("Please input a valid answer");
            }
        }
        //how videos taken
        while(true){
            System.out.println("Is every other video going to be renamed (yes)? if not every video in order will be renamed (no) \n please answer with yes or no");
            statement = input.nextLine();
            if(statement.toLowerCase().equals("yes")){
                everyOther = true;
                break;
            }
            else if(statement.toLowerCase().equals("no")){
                everyOther = false;
                break;
            }
            else{
                System.out.println("Please input a valid answer");
            }
        }

        //loop for videos

        boolean exit = false;
        String startVideoName = "";
        File moveFile;
        File holdFile;
        video = new File(videoDirectory);
        int matchStart = 1;
        String matchType = "";
        while(exit != true) {
            //starting video name
            while (exit != true) {
                System.out.println("What is the starting video name? or type \'exit\' to exit the program \n example: MVI_2912");
                statement = input.nextLine();
                if (statement.toLowerCase().equals("exit")) {
                    exit = true;
                    break;
                }
                else {
                    startVideoName = statement;
                    System.out.println("is this the correct starting video name?" + statement);
                    statement = input.nextLine();
                    if (statement.toLowerCase().equals("yes")) {
                        break;
                    }
                    else if (statement.toLowerCase().equals("no")) {

                    }
                    else {
                        System.out.println("Please input a valid answer");
                    }
                }

            }
            //qualifications, quarter, semi-final or final?
            while(exit!=true){
                System.out.println("Is this qual, quarter, semi, or final matches?");
                statement=input.nextLine().toLowerCase();
               if(statement.equals("qual") || statement.equals("quarter") || statement.equals("semi") || statement.equals("final")){
                   matchType = statement;
                   break;
               }
               else{
                   System.out.println("Please input a valid answer");
               }
            }
            //starting match number?
            while(exit!=true && (matchType.equals("qual") || matchType.equals("final"))){
                System.out.println("What is the starting match number?\n(example for qual or final:1)");
                statement=input.nextLine();
                try {
                    matchStart = Integer.parseInt(statement);
                    break;
                }
                catch (NumberFormatException e) {
                    System.out.println("Please input a valid answer");
                }

            }
            //move Files
            //Edge case, if video name reaches MVI_9999 if it changes the file name to MVI_00001 the code will break whenever the largest place changes. EX: MVI_09999 to MVI_1#### but should work past the benchmarks
            int nextMatch = matchStart;
            String videoName = startVideoName;
            if (exit!=true){
                try {
                    //https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#move-java.nio.file.Path-java.nio.file.Path-java.nio.file.CopyOption...-
                    if(matchType.equals("qual") || matchType.equals("final")){
                        if(matchType.equals("qual")){
                            if(everyOther){
                                for (int x = startFileFiles.listFiles().length; x>0; x -= 2){
                                    holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                                    moveFile = new File(qualDir + "\\Q" + nextMatch + ".MP4");
                                    //
                                    System.out.println(holdFile.toString());
                                    System.out.println(moveFile.toString());
                                    //
                                    Files.copy(holdFile.toPath(),moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    /**String first = videoName.substring(0,videoName.length()-2);
                                    String last = videoName.substring(videoName.length()-2);
                                    int lastFinal = Integer.parseInt(last) + 2;
                                    videoName = first + lastFinal;*/
                                    String first = videoName.substring(0,videoName.length()-4);
                                    String last = videoName.substring(videoName.length()-4);
                                    int lastFinal = Integer.parseInt(last) + 2;
                                    //System.out.println(lastFinal);
                                    if(lastFinal < 1000){
                                        videoName = first + "0" + lastFinal;
                                    }
                                    else if(lastFinal<100){
                                        videoName = first + "00" + lastFinal;
                                    }
                                    else if(lastFinal<10){
                                        videoName = first + "000" + lastFinal;
                                    }
                                    else{
                                        videoName = first + lastFinal;
                                    }
                                    nextMatch++;
                                }
                            }
                            else{
                                for (int x = startFileFiles.listFiles().length; x>0; x--){
                                    holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                                    moveFile = new File(qualDir + "\\Q" + nextMatch + ".MP4");
                                    //
                                    System.out.println(holdFile.toString());
                                    System.out.println(moveFile.toString());
                                    //
                                    Files.copy(holdFile.toPath(),moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    /**
                                    String first = videoName.substring(0,videoName.length()-2);
                                    String last = videoName.substring(videoName.length()-2);
                                    int lastFinal = Integer.parseInt(last) + 1;
                                    videoName = first + lastFinal;
                                    */
                                    /////
                                    String first = videoName.substring(0,videoName.length()-4);
                                    String last = videoName.substring(videoName.length()-4);
                                    int lastFinal = Integer.parseInt(last) + 1;
                                    //System.out.println(lastFinal);
                                    if(lastFinal < 1000){
                                        videoName = first + "0" + lastFinal;
                                    }
                                    else if(lastFinal<100){
                                        videoName = first + "00" + lastFinal;
                                    }
                                    else if(lastFinal<10){
                                        videoName = first + "000" + lastFinal;
                                    }
                                    else{
                                        videoName = first + lastFinal;
                                    }
                                    /////
                                    nextMatch++;
                                }
                            }
                        }
                        else if(matchType.equals("final")){
                            if(everyOther){
                                for (int x = startFileFiles.listFiles().length; x>0; x -= 2){
                                    holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                                    moveFile = new File(finalDir + "\\F" + nextMatch + ".MP4");
                                    //
                                    System.out.println(holdFile.toString());
                                    System.out.println(moveFile.toString());
                                    //
                                    Files.copy(holdFile.toPath(),moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    /**String first = videoName.substring(0,videoName.length()-2);
                                     String last = videoName.substring(videoName.length()-2);
                                     int lastFinal = Integer.parseInt(last) + 2;
                                     videoName = first + lastFinal;*/
                                    String first = videoName.substring(0,videoName.length()-4);
                                    String last = videoName.substring(videoName.length()-4);
                                    int lastFinal = Integer.parseInt(last) + 2;
                                    //System.out.println(lastFinal);
                                    if(lastFinal < 1000){
                                        videoName = first + "0" + lastFinal;
                                    }
                                    else if(lastFinal<100){
                                        videoName = first + "00" + lastFinal;
                                    }
                                    else if(lastFinal<10){
                                        videoName = first + "000" + lastFinal;
                                    }
                                    else{
                                        videoName = first + lastFinal;
                                    }
                                    nextMatch++;
                                }
                            }
                            else{
                                for (int x = startFileFiles.listFiles().length; x>0; x--){
                                    holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                                    moveFile = new File(finalDir + "\\F" + nextMatch + ".MP4");
                                    //
                                    System.out.println(holdFile.toString());
                                    System.out.println(moveFile.toString());
                                    //
                                    Files.copy(holdFile.toPath(),moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    /**
                                     String first = videoName.substring(0,videoName.length()-2);
                                     String last = videoName.substring(videoName.length()-2);
                                     int lastFinal = Integer.parseInt(last) + 1;
                                     videoName = first + lastFinal;
                                     */
                                    /////
                                    String first = videoName.substring(0,videoName.length()-4);
                                    String last = videoName.substring(videoName.length()-4);
                                    int lastFinal = Integer.parseInt(last) + 1;
                                    //System.out.println(lastFinal);
                                    if(lastFinal < 1000){
                                        videoName = first + "0" + lastFinal;
                                    }
                                    else if(lastFinal<100){
                                        videoName = first + "00" + lastFinal;
                                    }
                                    else if(lastFinal<10){
                                        videoName = first + "000" + lastFinal;
                                    }
                                    else{
                                        videoName = first + lastFinal;
                                    }
                                    /////
                                    nextMatch++;
                                }
                            }
                        }
                    }
                    else if(matchType.equals("quarter")){
                        String match;

                        System.out.println("What is the starting match number?\n(ex: 1-2)");
                        statement=input.nextLine();
                        match = statement;
                        while (true) {
                            //if(match is corret format){
                            switch (match) {
                                case "1-3":
                                    while (true) {
                                        System.out.println("Was there a 1-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 2-3)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                case "2-3":
                                    while (true) {
                                        System.out.println("Was there a 2-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 3-3)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                case "3-3":
                                    while (true) {
                                        System.out.println("Was there a 3-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 2-2)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                case "4-3":
                                    while (true) {
                                        System.out.println("Was there a 4-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 2-2)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                            moveFile = new File(quarterDir + "\\QF" + match + ".MP4");
                            //
                            System.out.println(holdFile.toString());
                            System.out.println(moveFile.toString());
                            //
                            Files.copy(holdFile.toPath(), moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            /**
                             String first = videoName.substring(0,videoName.length()-2);
                             String last = videoName.substring(videoName.length()-2);
                             int lastFinal = Integer.parseInt(last) + 1;
                             videoName = first + lastFinal;
                             */
                            /////
                            String first = videoName.substring(0, videoName.length() - 4);
                            String last = videoName.substring(videoName.length() - 4);
                            int lastFinal = Integer.parseInt(last) + 1;
                            //System.out.println(lastFinal);
                            if (lastFinal < 1000) {
                                videoName = first + "0" + lastFinal;
                            } else if (lastFinal < 100) {
                                videoName = first + "00" + lastFinal;
                            } else if (lastFinal < 10) {
                                videoName = first + "000" + lastFinal;
                            } else {
                                videoName = first + lastFinal;
                            }

                            if (match.equals("4-3")) {
                                break;
                            }

                            if (match.substring(0, 1).equals("1") || match.substring(0, 1).equals("2") || match.substring(0, 1).equals("3")) {
                                match = (Integer.parseInt(match.substring(0, 1))+1) + match.substring(match.length() - 2);
                            } else if (match.substring(0, 1).equals("4")) {
                                match = "1-" + (Integer.parseInt(match.substring(match.length() - 1)) + 1);
                            }
                        }

                        //}
                    }
                    else if(matchType.equals("semi")){
                        String match;

                        System.out.println("What is the starting match number?\n(ex: 1-2)");
                        statement=input.nextLine();
                        match = statement;
                        while (true) {
                            //if(match is corret format){
                            switch (match) {
                                case "1-3":
                                    while (true) {
                                        System.out.println("Was there a 1-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 2-3)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                case "2-3":
                                    while (true) {
                                        System.out.println("Was there a 2-3 match?");
                                        statement = input.nextLine();
                                        if (statement.toLowerCase().equals("yes")) {
                                            break;
                                        } else if (statement.toLowerCase().equals("no")) {
                                            System.out.println("What was the next match? (ex: 3-3)");
                                            match = input.nextLine();
                                            break;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            holdFile = new File(startFileDirectory + "\\" + videoName + ".MP4");
                            moveFile = new File(semiDir + "\\SF" + match + ".MP4");
                            //
                            System.out.println(holdFile.toString());
                            System.out.println(moveFile.toString());
                            //
                            Files.copy(holdFile.toPath(), moveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            /**
                             String first = videoName.substring(0,videoName.length()-2);
                             String last = videoName.substring(videoName.length()-2);
                             int lastFinal = Integer.parseInt(last) + 1;
                             videoName = first + lastFinal;
                             */
                            /////
                            String first = videoName.substring(0, videoName.length() - 4);
                            String last = videoName.substring(videoName.length() - 4);
                            int lastFinal = Integer.parseInt(last) + 1;
                            //System.out.println(lastFinal);
                            if (lastFinal < 1000) {
                                videoName = first + "0" + lastFinal;
                            } else if (lastFinal < 100) {
                                videoName = first + "00" + lastFinal;
                            } else if (lastFinal < 10) {
                                videoName = first + "000" + lastFinal;
                            } else {
                                videoName = first + lastFinal;
                            }

                            if (match.equals("2-3")) {
                                break;
                            }

                            if (match.substring(0, 1).equals("1")) {
                                match = (Integer.parseInt(match.substring(0, 1))+1) + match.substring(match.length() - 2);
                            } else if (match.substring(0, 1).equals("2")) {
                                match = "1-" + (Integer.parseInt(match.substring(match.length() - 1)) + 1);
                            }
                        }

                        //}
                    }
                } catch (IOException e) {
                    System.out.println("^^^ Renaming Failed for last displayed file ^^^ \n \n");
                    //e.printStackTrace();
                }
            }
        }
        System.out.println("\n Exiting");
    }


}
