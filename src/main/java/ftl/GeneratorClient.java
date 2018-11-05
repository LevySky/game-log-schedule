package ftl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorClient {
    private static File javaFile_controller = null;
    private static File javaFile_mapper = null;
    private static File javaFile_service = null;

    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] names = {"TaskCount"};

        for (String name : names) {
            map = new HashMap<String, Object>();
            map.put("name", name);
            map.put("reload", false);
            list.add(map);
        }

        for (Map<String, Object> _map : list) {
            createFile(_map);
        }
    }


    private static void createFile(Map<String, Object> map) {
        Configuration cfg = new Configuration();
        try {
            // 步骤一：指定 模板文件从何处加载的数据源，这里设置一个文件目录
            cfg.setDirectoryForTemplateLoading(new File("./src/main/java/ftl/"));
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            // 步骤二：获取 模板文件
            Template template_mapper = cfg.getTemplate("mapper.ftl");
            Template template_service = cfg.getTemplate("service.ftl");

            // 步骤三：创建 数据模型
            Map<String, Object> map_mapper = createMapperDataModel((String) map.get("name"), (Boolean) map.get("reload"));
            Map<String, Object> map_service = createServiceDataModel((String) map.get("name"), (Boolean) map.get("reload"));
            // 步骤四：合并 模板 和 数据模型
            // 创建.java类文件

            if (javaFile_mapper != null) {
                Writer javaWriter_mapper = new FileWriter(javaFile_mapper);
                template_mapper.process(map_mapper, javaWriter_mapper);
                javaWriter_mapper.flush();
                javaWriter_mapper.close();
                System.out.println((String) map.get("name") + "Repository文件生成成功：" + javaFile_mapper.getCanonicalPath());

            } else {
                System.out.println((String) map.get("name") + "Repository文件生成失败!");
            }

            if (javaFile_service != null) {
                Writer javaWriter_service = new FileWriter(javaFile_service);

                template_service.process(map_service, javaWriter_service);
                javaWriter_service.flush();
                javaWriter_service.close();
                System.out.println((String) map.get("name") + "Service文件生成成功：" + javaFile_controller.getCanonicalPath());
            } else {
                System.out.println((String) map.get("name") + "Service文件生成失败!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建数据模型
     *
     * @return
     */
    private static Map<String, Object> createMapperDataModel(String name, Boolean reload) {


        Map<String, Object> root = new HashMap<String, Object>();

        Mapper mapper = new Mapper();
        mapper.setClassName(name + "Repository");
        mapper.setEntityName(name);
        mapper.setJavaPackage("com.spc.count.repository");

        // 创建.java类文件
        File outDirFile = new File("./src/main/java/");
        if (!outDirFile.exists()) {
            outDirFile.mkdir();
        }

        javaFile_mapper = toJavaFilename(outDirFile, mapper.getJavaPackage(), mapper.getClassName(), reload);

        root.put("mapper", mapper);
        return root;
    }


    /**
     * 创建数据模型
     *
     * @return
     */
    private static Map<String, Object> createServiceDataModel(String name, Boolean reload) {


        Map<String, Object> root = new HashMap<String, Object>();

        Service service = new Service();
        service.setClassName(name + "Manager");
        service.setEntityName(name);
        service.setJavaPackage("com.spc.count.manager");
        service.setMapperName(name + "Repository");
        // 创建.java类文件
        File outDirFile = new File("./src/main/java/");
        if (!outDirFile.exists()) {
            outDirFile.mkdir();
        }

        javaFile_service = toJavaFilename(outDirFile, service.getJavaPackage(), service.getClassName(), reload);

        root.put("service", service);
        return root;
    }




    /**
     * 创建.java文件所在路径 和 返回.java文件File对象
     *
     * @param outDirFile    生成文件路径
     * @param javaPackage   java包名
     * @param javaClassName java类名
     * @return
     */
    private static File toJavaFilename(File outDirFile, String javaPackage, String javaClassName, Boolean reload) {
        String packageSubPath = javaPackage.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        File file = new File(packagePath, javaClassName + ".java");
        if (!packagePath.exists()) {
            packagePath.mkdirs();
        }

        if (file.exists()) {
            if (reload) {
                return file;
            } else {
                return null;
            }
        } else {
            return file;
        }


    }


    /**
     * 创建数据模型
     * @return
     */
   /* private static Map<String, Object> createEntityDataModel() {
    	
    	
        Map<String, Object> root = new HashMap<String, Object>();
        Entity user = new Entity();
        user.setJavaPackage("com.spc.count.entity"); // 创建包名
        user.setClassName("User");  // 创建类名
        user.setConstructors(true); // 是否创建构造函数
        // user.setSuperclass(person);
         
        List<Property> propertyList = new ArrayList<Property>();
         
        // 创建实体属性一 
        Property attribute1 = new Property();
        attribute1.setJavaType("String");
        attribute1.setPropertyName("name");
        attribute1.setPropertyType(PropertyType.String);
         
        // 创建实体属性二
        Property attribute2 = new Property();
        attribute2.setJavaType("int");
        attribute2.setPropertyName("age");
        attribute2.setPropertyType(PropertyType.Int);
         
        propertyList.add(attribute1);
        propertyList.add(attribute2);
         
        // 将属性集合添加到实体对象中
        user.setProperties(propertyList);
         
        // 创建.java类文件
        File outDirFile = new File("./src/main/java/");
        if(!outDirFile.exists()){
            outDirFile.mkdir();
        }
         
        javaFile = toJavaFilename(outDirFile, user.getJavaPackage(), user.getClassName());
         
        root.put("entity", user);
        return root;
    }*/

}