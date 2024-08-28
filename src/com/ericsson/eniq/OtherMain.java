package com.ericsson.eniq;

import java.io.*;
import java.util.*;

import com.adventnet.snmp.mibs.*;

public class OtherMain {
    final static MibOps mibOps = new MibOps();
    final static String PATH = "C:/SIM_mibs";

    public static void main(String[] args) {
        final File files = new File(PATH);
        final List<LoadThread> threads = new ArrayList<LoadThread>();
        final List<String> list = Arrays.asList(files.list());
        final List<String> sublist = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            sublist.add(PATH + "/" + list.get(i));

            if (i % 10 == 0) {
                System.out.println(sublist);
                threads.add(new LoadThread(sublist, mibOps));
                sublist.clear();
                System.out.println();
                System.out.println();
            }
        }

        for (LoadThread thread : threads) {
            thread.start();
        }
    }
}

class LoadThread extends Thread {
    final static String PATH = "C:/SIM_mibs";

    private List<String> files;
    private MibOps mibops;

    public LoadThread(List<String> files, MibOps mibops) {
        this.files = files;
        this.mibops = mibops;
    }

    @Override
    public void run() {

        for (int i = 0; i < files.size(); i++) {
            System.out.println("Loading " + files.get(i));
            try {
                mibops.loadMibModule(files.get(i));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MibException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class MibOps extends MibOperations {
    public MibOps() {

    }

    @Override
    public synchronized MibModule loadMibModule(String arg0) throws MibException, IOException, FileNotFoundException {
        return super.loadMibModule(arg0);
    }

}