package com.example.demo.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CodeGeneratorUtil {

    /**
     * <p>
     * 读取控制台内容；表名，多个英文逗号分割
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        //代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java"); //生成文件的输出目录
        gc.setAuthor("fll"); //开发人员
        gc.setOpen(false); //生成完后是否打开资源管理器
        gc.setServiceName("%sService");//去service的I前缀
        //gc.setSwagger2(true); //开启 swagger2 模式
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true");
        //dsc.setSchemaName("public");
        dsc.setDbType(DbType.MYSQL); //数据库类型
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.example.demo");//父包名
        pc.setEntity("entity");//实体类包名
        pc.setController("controller");//控制类包名
        pc.setService("service");//service包名
        pc.setServiceImpl("service.impl");//service实现类包名
        pc.setMapper("mapper");//mapper包名
        mpg.setPackageInfo(pc);

        String scanner = scanner("表名和表备注及主键类型(一次只能生成一个表，例：users-用户表-Integer)");
        String[] split = scanner.split("-");
        /**
         * 自定义配置
         */
        InjectionConfig cfg = new InjectionConfig() {
            //自定义属性注入
            //在.ftl或者是.vm模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> map = new HashMap<>();
                map.put("author", this.getConfig().getGlobalConfig().getAuthor());
                map.put("datetime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                map.put("pathName", split[0].replaceAll("_", ""));
                String className = "";
                if(split[0].contains("_")) {
                    String[] split2 = split[0].split("_");
                    for(int i = 0; i < split2.length; i++) {
                        className += upperFirst(split2[i]);
                    }
                } else {
                    className = upperFirst(split[0]);
                }
                map.put("className", className);
                map.put("classname", lowerFirst(className));
                map.put("comments", split[1]);
                map.put("primaryKeyType", split[2]);
                this.setMap(map);
            }
        };
        //自定义输出配置, 自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();
        //如果模板引擎是 velocity
        String templatePath_xml = "/templates/mapper.xml.vm";
        focList.add(new FileOutConfig(templatePath_xml) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + "/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        //配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //控制是否按默认的来生成文件，设置为null则不生成默认的
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setController("templates/controller.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setInclude(split[0]);
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略,下划线到驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略,下划线到驼峰命名
        strategy.setEntityLombokModel(true);//自动lombok
        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);//是否生成实体时，生成字段注解
        strategy.setControllerMappingHyphenStyle(false);//驼峰转连字符
        mpg.setStrategy(strategy);
        //执行
        mpg.execute();
    }

    /**
     * 将字符串的首字母转小写
     * @param str 需要转换的字符串
     * @return
     */
    private static String lowerFirst(String str){
        //进行字母的ascii编码后移，效率要高于截取字符串进行转换的操作
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 将字符串的首字母转大写
     * @param str 需要转换的字符串
     * @return
     */
    private static String upperFirst(String str) {
        //进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

}
