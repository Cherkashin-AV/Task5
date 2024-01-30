package ru.vtb.javaCourse.Task5;

import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class Utils {
    static private String schemaDB;

    @Value("${spring.datasource.schema}")
    private static void setSchemaDB(String schemaDB_) {
        schemaDB = schemaDB_;
    }

    public static String getSchemaDB() {
        return schemaDB;
    }

    public static String getTableNameFromEntity(Class cl){
        String result = null;
        try {
            return ((Table) cl.getAnnotation(Table.class)).name();
        }
        catch (NullPointerException e) {
            return cl.getName().replaceAll(cl.getPackageName() + ".", "");
        }
    }

}
