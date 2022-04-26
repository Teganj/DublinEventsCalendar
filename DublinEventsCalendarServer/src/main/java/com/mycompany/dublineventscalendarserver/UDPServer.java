package com.mycompany.dublineventscalendarserver;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 *@author Tegan Jennings
 * 18/11/2021
 */
public class UDPServer {

    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    List<String> Events;

    public static void main(String[] args) {

        System.out.println("Opening port...\n");
        try {
            dgramSocket = new DatagramSocket(PORT);
            System.out.println("Connection Successful");
        } catch (SocketException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        run();
    }

    private static void run() {
        ArrayList<String> Events;
        ArrayList<String> EventsList;

        try {
            String messageIn, messageOut;
            messageOut = null;
            Events = new ArrayList<>();
            EventsList = new ArrayList<>();

            Events.add("10 January 2022 : 1pm : Carnival : Dublin 24.");
            Events.add("02 February 2022 : 3pm : Circus : Dublin 24.");
            Events.add("29 April 2022 : 10am : Womens Mini Marathon : Dublin.");
            Events.add("30 May 2022 : 5am : Darkness into Light : Dublin 22.");
            Events.add("17 July 2022 : 8am : Dublin Marathon : Dublin.");
            Events.add("31 August 2022 : 4pm : Art Museum : Dublin.");
            Events.add("11 October 2022 : 6pm : Pumpkin Patch : Dublin 14.");
            Events.add("19 October 2022 : 7pm : Pumpkin Patch : Dublin 22.");
            Events.add("25 October 2022 : 8pm : Firework Show : Dublin 24.");
            Events.add("02 November 2021 : 10pm : Firework Show : Dublin 22.");
            Events.add("19 December 2021 : 12pm : Santa Show : Dublin 22.");
            do {
                buffer = new byte[1024];
                inPacket = new DatagramPacket(buffer, buffer.length);
                dgramSocket.receive(inPacket);

                InetAddress clientAddress = inPacket.getAddress();
                int clientPort = inPacket.getPort();

                messageIn = new String(inPacket.getData(), 0, inPacket.getLength());

                //Grabbing Events with the inputting date
                //Adding to a new ArrayList and printing the new ArrayList
                for (int i = 0; i < Events.size(); i++) {
                    if (Events.get(i).contains(messageIn)) {
                        EventsList.add(Events.get(i) + "\n");
                    }
                    messageOut = ("Dublin Events for that date include: " + EventsList);
                }
                //Add Event to Arraylist if message contains Add
                if (messageIn.contains("Add")) {
                    messageIn = messageIn.replace("Add", "");
                    Events.add(messageIn);
                    System.out.println("Event Added Successfully.");
                    messageOut = ("Event Added: " + messageIn);
                }
                //If EventList is empty and no events for that date, print all events.
                else if (EventsList.isEmpty()) {
                    messageOut = "No events found, please try again. "
                            + "All Events in Dublin are as follows:\n " + Events;
                }

                outPacket = new DatagramPacket(messageOut.getBytes(),
                        messageOut.length(),
                        clientAddress,
                        clientPort);
                dgramSocket.send(outPacket);
                EventsList.removeAll(EventsList);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\n Closing connection... ");
            dgramSocket.close();
        }
    }
}
