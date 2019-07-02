package com.qhuytu.jthriftclient;

import com.qhuytu.jthriftfiles.services.NoteService;
import com.qhuytu.jthriftfiles.services.servicesConstants;
import com.qhuytu.jthriftfiles.structs.Note;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

public class Client {
    public static void main(String[] args) {
        TFramedTransport transport = new TFramedTransport(new TSocket("localhost", 9090));
        try {
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);

            dítribute(protocol);
        } catch (TTransportException ex) {
            System.out.println("Fails to connect to server: " + ex.getMessage());
        } finally {
            transport.close();
        }
    }

    private static void dítribute(TProtocol protocol) {
        NoteService.Client noteClient = new NoteService.Client(new TMultiplexedProtocol(protocol, servicesConstants.NOTESERVICE));
        performNote(noteClient);
    }

    private static void performNote(NoteService.Client client) {
        try {
            Note newNote = new Note();
            newNote.content = "Hello world";

            client.addNote(newNote);

            Note findNote = client.getNote(1);
            System.out.println("Note 1 content: " + findNote.content);
            System.out.println("Note 1 status: " + findNote.isFinish);
            client.triggerNote(1);
            findNote = client.getNote(1);
            System.out.println("Note 1 status after trigger: " + findNote.isFinish);

            findNote = client.getNote(0);
            System.out.println("Note 0 content: " + findNote.content);
        } catch (TException ex) {
            System.out.println("Fails to perform NoteService: " + ex.getMessage());
        }
    }
}
