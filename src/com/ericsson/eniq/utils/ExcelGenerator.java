package com.ericsson.eniq.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jxl.*;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

/**
 * Class which generates excel output
 * 
 * @author etarvol
 *
 */
public class ExcelGenerator {
    private static ExcelGenerator thisInstance = null;
    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;

    @SuppressWarnings("rawtypes")
    private TableView table = null;

    private ExcelGenerator() {

    }

    public static ExcelGenerator getInstance() {
        if (thisInstance == null) {
            thisInstance = new ExcelGenerator();
        }

        return thisInstance;
    }

    /**
     * Creating new Excel Document
     * 
     * @param excelFile
     *            File location where document will be created
     * @param tableParam
     *            Table from where data is being pulled and saved
     * @throws IOException
     * @throws WriteException
     */
    @SuppressWarnings("rawtypes")
    public void createDocument(File excelFile, TableView tableParam) throws IOException, WriteException {
        this.table = tableParam;
        excelFile.createNewFile();
        excelFile.mkdirs();

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workBook = Workbook.createWorkbook(excelFile);
        workBook.createSheet("MAT Output", 0);
        WritableSheet excelSheet = workBook.getSheet(0);
        populateSheet(excelSheet);

        workBook.write();
        workBook.close();

    }

    /**
     * Method for creating headers(columns)
     * 
     * @param sheet
     *            Current WritableSheet object
     * @throws WriteException
     */
    @SuppressWarnings("rawtypes")
    private void populateSheet(WritableSheet sheet) throws WriteException {
        //
        // Formatting our fonts
        //
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10pt);
        times.setWrap(true);
        WritableFont times10ptBoldNoUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldNoUnderline);
        timesBoldUnderline.setWrap(true);
        //
        // Formating our cells
        //
        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);
        //
        // Creating headers
        //
        int columnIndex = 0;
        ObservableList columns = this.table.getColumns();
        for (Object c : columns) {
            TableColumn column = (TableColumn) c;
            addCaption(sheet, columnIndex, 0, column.getText());
            formatColumns(sheet, columnIndex);
            formatRows(sheet, 0);

            int row = 0;
            while (column.getCellData(row) != null) {
                addLabel(sheet, columnIndex, row + 1, column.getCellData(row).toString());
                formatRows(sheet, row + 1);
                formatColumns(sheet, columnIndex);
                row++;
            }

            columnIndex++;
        }

    }

    private void formatColumns(WritableSheet sheet, int column) {
        CellView cellView = sheet.getColumnView(column);
        cellView.setAutosize(true);
        sheet.setColumnView(column, cellView);
    }

    private void formatRows(WritableSheet sheet, int row) throws RowsExceededException {
        CellView cellView = sheet.getRowView(row);
        cellView.setAutosize(true);
        sheet.setRowView(row, cellView);
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    @SuppressWarnings("unused")
    private void addNumber(WritableSheet sheet, int column, int row, Integer integer) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

}
