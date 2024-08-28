package com.ericsson.eniq.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import com.adventnet.snmp.mibs.MibNode;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.ericsson.eniq.dto.MibNodeDto;

/**
 *
 * @author etarvol
 *
 */
public class MibUtils {

    public static String accessToString(int acceessCode) {
        String access = "not defined";

        switch (acceessCode) {
            case SnmpAPI.NOACCESS:
                access = "not-accessible";
                break;

            case SnmpAPI.RONLY:
                access = "read-only";
                break;

            case SnmpAPI.RWRITE:
                access = "read-write";
                break;

            case SnmpAPI.WONLY:
                access = "write-only";
                break;

            case SnmpAPI.RCREATE:
                access = "read-create";
                break;

            case SnmpAPI.ACCESSFORNOTIFY:
                access = "accessible-for-notify";
                break;
        }

        return access;

    }

    public static Object deserialize(String path) {
        Object obj = null;

        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            obj = ois.readObject();
            ois.close();
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @SuppressWarnings("rawtypes")
    public static String formatOID(Vector v) {
        String oid = "";

        for (Object i : v) {
            int n = Integer.parseInt(i.toString());
            oid += "." + n;
        }

        return oid;
    }

    public static String getMibFileName(String str) {
        String[] s1 = str.split(":");
        String[] s2 = s1[s1.length - 1].split("\\\\");
        return s2[s2.length - 1];
    }

    /**
     * Method which parses error message and returns filename of mib
     * 
     * @param str
     *            Error msg
     * @return filename of mib
     */
    public static String getMibNotFound(String str) {
        String[] s1 = str.split(":");
        String[] s2 = s1[s1.length - 1].split("/");
        return s2[s2.length - 1];
    }

    public static void printMibInfo(ArrayList<MibNodeDto> nodes) {
        for (MibNodeDto info : nodes) {
            System.out.println();
            info.print();
            System.out.println();
        }
    }

    public static void printMibNodes(ArrayList<MibNode> nodes) {
        for (MibNode node : nodes) {
            System.out.println();
            MibNodeDto info = new MibNodeDto(node);
            info.print();
            System.out.println();
        }
    }

    public static String removeMibExtension(String str) {
        String returnName = "";
        String[] names = str.split("\\.");
        if (names.length > 0) {
            returnName = names[0];
        }

        return returnName;
    }

    public static void serialize(String path, Object obj) {

        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            fos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeStringToFile(String filePath, String str) {
        str.replaceAll("\n", System.getProperty("line.separator"));

        File f = new File(filePath);

        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
            out.write(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
