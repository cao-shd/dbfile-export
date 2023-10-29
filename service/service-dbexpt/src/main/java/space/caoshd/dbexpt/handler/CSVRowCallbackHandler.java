package space.caoshd.dbexpt.handler;

import space.caoshd.dbexpt.bo.FileExportTypeTextBO;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CSVRowCallbackHandler implements RowCallbackHandler {

    /**
     * 自动生成头信息 [false]
     */
    private boolean autoHeader = false;

    /**
     * 换行符 [LF]
     */
    private String lineSeparator = "\n";

    /**
     * 字段分割符 [,]
     */
    private char fieldSeparator = (char) 44;

    /**
     * 封闭字符串 ["]
     */
    private String enclosedBy = "\"";

    private FileWriter writer;

    public CSVRowCallbackHandler(
        FileWriter writer
    ) {
        this(writer, null);
    }

    public CSVRowCallbackHandler(FileWriter writer, FileExportTypeTextBO config) {
        this.writer = writer;
        if (config != null) {
            this.autoHeader = config.isAutoHeader();
            this.lineSeparator = Optional.ofNullable(config.getLineSeparator()).orElse(enclosedBy);
            this.fieldSeparator = config.getFieldSeparator() == 0 ? fieldSeparator : config.getFieldSeparator();
            this.enclosedBy = Optional.ofNullable(config.getWarpWith()).orElse(enclosedBy);
        }
    }


    public void setAutoHeader(boolean autoHeader) {
        this.autoHeader = autoHeader;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public void setFieldSeparator(char fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    public void setEnclosedBy(String enclosedBy) {
        this.enclosedBy = enclosedBy;
    }

    public void setWriter(FileWriter writer) {
        this.writer = writer;
    }


    @Override
    public void processRow(ResultSet rs) {
        try {
            int columnCount = rs.getMetaData().getColumnCount();
            if (autoHeader) {
                List<String> columnLabels = new ArrayList<>();
                for (int columnNo = 1; columnNo <= columnCount; columnNo++) {
                    String columnLabel = rs.getMetaData().getColumnLabel(columnNo);
                    columnLabels.add(columnLabel);
                }
                writer.write(createLine(columnLabels));
            }

            List<String> fieldValues = new ArrayList<>();
            for (int columnNo = 1; columnNo <= columnCount; columnNo++) {
                String fieldValue = rs.getString(columnNo);
                fieldValues.add(fieldValue);
            }
            writer.write(createLine(fieldValues));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createLine(List<String> fieldValues) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fieldValues.size(); i++) {
            String fieldValue = fieldValues.get(i);
            stringBuilder.append(enclosedBy);
            stringBuilder.append(fieldValue);
            stringBuilder.append(enclosedBy);
            if (i != fieldValues.size() - 1) {
                stringBuilder.append(fieldSeparator);
            } else {
                stringBuilder.append(lineSeparator);
            }
        }
        return stringBuilder.toString();
    }

}
