package com.spc.mysql.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class ResultSetMapper<T> {


    private PropertyDescriptor[] pd;

    @SuppressWarnings("unchecked")
    public  List<T> mapRersultSetToObject(ResultSet rs, Class outputClass) {
        List<T> outputList = null;
        try {
            // make sure resultset is not null
            if (rs != null) {
                // check if outputClass has 'Entity' annotation
                if (outputClass.isAnnotationPresent(Entity.class)) {
                    // get the resultset metadata
                    ResultSetMetaData rsmd = rs.getMetaData();
                    // get all the attributes of outputClass
                    Field[] fields = outputClass.getDeclaredFields();
                    while (rs.next()) {
                        T bean = (T) outputClass.newInstance();
                        for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                            // getting the SQL column name
                            String columnName = rsmd.getColumnName(_iterator + 1);
                            // reading the value of the SQL column
                            Object columnValue = rs.getObject(_iterator + 1);
                            // iterating over outputClass attributes to check if
                            // any attribute has 'Column' annotation with
                            // matching 'name' value
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(Column.class)) {
                                    Column column = field.getAnnotation(Column.class);
                                    if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
                                        BeanUtils.setProperty(bean, field.getName(), columnValue);
                                        break;
                                    }
                                }
                            }
                        }
                        if (outputList == null) {
                            outputList = new ArrayList<T>();
                        }
                        outputList.add(bean);
                    }

                } else {
                    // throw some error
                }
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return outputList;
    }



    public String getQueryParams(T bean ,Map<String,Object> mapFilter){
        PropertyDescriptor[]  pds = PropertyUtils.getPropertyDescriptors(bean.getClass());

        StringBuffer sb = new StringBuffer();
        try {
            for(PropertyDescriptor pd : pds){
                if("class".equals(pd.getName()) || mapFilter.get(pd.getName()) != null){
                    continue;
                }
                Object value = PropertyUtils.getProperty(bean, pd.getName());
                if(value != null && StringUtils.isNotEmpty(value.toString())){
                    sb.append(" and ").append(pd.getName()).append("=").append(value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getQueryParams4Boss(T bean ,Map<String,Object> mapFilter){
        PropertyDescriptor[]  pds = PropertyUtils.getPropertyDescriptors(bean.getClass());

        StringBuffer sb = new StringBuffer();
        try {
            for(PropertyDescriptor pd : pds){
                if("class".equals(pd.getName()) || mapFilter.get(pd.getName()) != null){
                    continue;
                }
                Object value = PropertyUtils.getProperty(bean, pd.getName());
                if(value != null && StringUtils.isNotEmpty(value.toString())){


                    String name = pd.getName().replaceAll("([A-Z])","_$1").toLowerCase();

                    sb.append(" and ").append(name).append("=").append(value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }





}