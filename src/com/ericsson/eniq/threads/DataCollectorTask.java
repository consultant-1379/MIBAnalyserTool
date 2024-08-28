package com.ericsson.eniq.threads;

import java.util.Enumeration;
import java.util.Vector;

import com.adventnet.snmp.mibs.*;
import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.utils.*;

import javafx.concurrent.Task;

public class DataCollectorTask extends Task<Void> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Void call() throws Exception {

        SharedMatMibOperations shared = SharedMatMibOperations.getInstance();
        MibOperations mibOperations = shared.get();

        Enumeration mibModules = mibOperations.getMibModules();
        while (mibModules.hasMoreElements()) {

            MibModule mibModule = (MibModule) mibModules.nextElement();

            MibNode node = null;
            MibNodeDto mibNodeDto = null;
            String fileName = MibUtils.getMibFileName(mibModule.getFileName());
            Vector<MibNode> childList = null;

            Enumeration<MibNode> definedNodes = mibModule.getDefinedNodes();

            if (!shared.getDependancieMibList().containsKey(mibModule.getName())) {
                shared.addPrimaryMib(mibModule.getName(), mibModule.getFileName());
            }
            while (definedNodes.hasMoreElements()) {
                //
                // Getting node
                //
                node = definedNodes.nextElement();
                //
                // Getting node's possible name
                //
                String nodeName = null;
                if (node.getLabel() != null) {
                    nodeName = node.getLabel();
                } else if (node.getRowName() != null) {
                    nodeName = node.getRowName();
                }
                //
                // Updating progress label
                //
                LoadingLabelQueue.getInstance().insertString("Parsing mib node..." + nodeName);
                //
                // Getting child list
                //
                int nodeSizeList = node.getChildList().size();
                childList = node.getChildList();
                //
                // Adding node to the list
                //
                mibNodeDto = new MibNodeDto(node, fileName);
                Search.getInstance().add(mibNodeDto);

                //
                // Iterating through children if any
                //
                for (int i = 0; i < nodeSizeList; i++) {
                    mibNodeDto = new MibNodeDto(childList.get(i), fileName);
                    Search.getInstance().add(mibNodeDto);
                }

            }
        }

        return null;
    }
}
