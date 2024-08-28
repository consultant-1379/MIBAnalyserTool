package com.ericsson.eniq.dto;

import java.util.Vector;

import com.adventnet.snmp.mibs.LeafSyntax;
import com.adventnet.snmp.mibs.MibNode;
import com.ericsson.eniq.threads.SharedMatMibOperations;
import com.ericsson.eniq.utils.Loader;
import com.ericsson.eniq.utils.MibUtils;

import javafx.scene.Node;

/**
 * Class which hold all the data for the counters
 *
 * @author etarvol
 *
 */
public class MibNodeDto {

    public static final String PRIMARY = "Primary";
    public static final String DEPENDENCIE = "Dependencies";
    public static final String ERROR = "Error";

    public static final String SCALAR = "Scalar";
    public static final String TABULAR = "Tabular";

    public static final String TABLE = "Table";
    public static final String TABLE_ENTRY = "Table Entry";
    public static final String INDEX_COUNTER = "Index Counter";
    public static final String COUNTER = "Counter";
    public static final String UNDEFINED = "Undefined";

    private String access = null;
    private String defVal = null;
    private String description = null;
    private String entryType = null;
    private String error = null;
    private String indexes = null;
    private String mibFileName = null;
    private String mibName = null;
    private String mibType = null;
    private String name = null;
    private String objects = null;
    private String oid = null;
    private MibNodeDto parent = null;
    private String status = null;
    private String syntax = null;
    private String syntaxDescription = null;
    private String importedMibName = null;

    private String type = null;

    public MibNodeDto(MibNode node) {
        parse(node);
    }

    public MibNodeDto(MibNode node, String mibFileNameParam) {
        parse(node);
        setMibFileName(mibFileNameParam);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void parse(MibNode node) {
        if (node == null) {
            return;
        }

        oid = MibUtils.formatOID(node.getOIDVectorIds());
        if (node.getLabel() != null) {
            name = node.getLabel();
        } else if (node.getRowName() != null) {
            name = node.getRowName();
        }

        importedMibName = node.getImportedModuleName();

        mibName = node.getModuleName();

        LeafSyntax syntax_temp = node.getSyntax();
        if (syntax_temp != null) {
            syntax = syntax_temp.getName();
            syntaxDescription = syntax_temp.getDescription();
        }

        access = MibUtils.accessToString(node.getAccess());
        status = node.getStatus();
        defVal = node.getDefval();
        Vector<String> indxv = null;
        MibNode parentNode = node.getParent();
        if (parentNode == null) {
            indxv = node.getIndexNames();
        } else {
            indxv = parentNode.getIndexNames();
            parent = new MibNodeDto(parentNode);
        }
        indexes = indxv != null ? indxv.toString().replace("[", "").replace("]", "") : "";
        description = node.getDescription();
        Vector objv = node.getObjects();
        objects = objv != null ? objv.toString().replace("[", "").replace("]", "") : "";
        mibType = assignType(mibName);

        if (node.isScalar()) {
            setType(SCALAR);
        } else if (node.isLeaf()) {
            setType(TABULAR);
        }

        entryType = assignEntryType(node);

        //
        // Adding possible parser options
        //
    }

    private String assignEntryType(MibNode node) {
        if (node.isTable()) {
            return TABLE;
        }

        if (node.isTableEntry()) {
            return TABLE_ENTRY;
        }

        if (node.isIndex()) {
            return INDEX_COUNTER;
        }

        if (node.isLeaf()) {
            return COUNTER;
        }

        return UNDEFINED;
    }

    private String assignType(String name) {

        SharedMatMibOperations shared = SharedMatMibOperations.getInstance();

        if (shared.getDependancieMibList().containsKey(name)) {
            return DEPENDENCIE;
        }
        if (shared.getPrimaryMibList().containsKey(name)) {
            return PRIMARY;
        }
        if (shared.getParsedWithErrorMibList().containsKey(name)) {
            return ERROR;
        }

        return null;
    }

    public String getAccess() {
        return access;
    }

    public String getDefVal() {
        return defVal;
    }

    public String getDescription() {
        return description;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getError() {
        return error;
    }

    public Node getImage() {
        return Loader.getInstance().getEntryTypeImage(this);
    }

    public String getIndexes() {
        return indexes;
    }

    public String getMibFileName() {
        return mibFileName;
    }

    public String getMibName() {
        return mibName;
    }

    public String getMibType() {
        return mibType;
    }

    public String getName() {
        return name;
    }

    public String getObjects() {
        return objects;
    }

    public String getOid() {
        return oid;
    }

    public MibNodeDto getParent() {
        return parent;
    }

    public String getStatus() {
        return status;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getType() {
        return type;
    }

    public boolean isCounter() {
        return this.entryType.equals(COUNTER);
    }

    public boolean isIndexCounter() {
        return this.entryType.equals(INDEX_COUNTER);
    }

    public boolean isTable() {
        return this.entryType.equals(TABLE);
    }

    public boolean isTableEntry() {
        return this.entryType.equals(TABLE_ENTRY);
    }

    public boolean isUndefind() {
        return this.entryType.equals(UNDEFINED);
    }

    public void print() {
        if (parent != null) {
            System.out.println("\t\t\t Parent Node: " + parent.getName());
        }
        System.out.println("\t\t\t OID: " + oid);
        System.out.println("\t\t\t Name: " + name);
        System.out.println("\t\t\t MIB Name: " + mibName);
        System.out.println("\t\t\t MIB Type: " + type);
        System.out.println("\t\t\t Entry Type: " + entryType);
        System.out.println("\t\t\t Syntax: " + syntax);
        System.out.println("\t\t\t Access: " + access);
        System.out.println("\t\t\t Status: " + status);
        System.out.println("\t\t\t DefVal: " + defVal);
        System.out.println("\t\t\t Indexes: " + indexes);
        System.out.println("\t\t\t Objects: " + objects);
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setDefVal(String defVal) {
        this.defVal = defVal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public void setMibFileName(String mibFileName) {
        this.mibFileName = mibFileName;
    }

    public void setMibName(String mibName) {
        this.mibName = mibName;
    }

    public void setMibType(String mibType) {
        this.mibType = mibType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setParent(MibNodeDto parent) {
        this.parent = parent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MibInfoDto [oid=" + oid + ", syntax=" + syntax + ", access=" + access + ", status=" + status + ", defVal=" + defVal + ", indexes="
                + indexes + ", objects=" + objects + "]";
    }

    public String getSyntaxDescription() {
        return syntaxDescription;
    }

    public void setSyntaxDescription(String syntaxDescription) {
        this.syntaxDescription = syntaxDescription;
    }

    public String getImportedMibName() {
        return importedMibName;
    }

    public void setImportedMibName(String importedMibName) {
        this.importedMibName = importedMibName;
    }

}
