package com.example.demo.utils;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 范乐乐
 * @Date 2021-06-10
 * @Descriprion
 * 用于导入导出操作的工具类
 */
public class JxlsExcelUtil {
    /**
     * 以流的形式导出Excel
     * @param is  导出模板流
     * @param os  导出文件流
     * @param model 导出参数
     * @throws IOException
     */
    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = new Context(model);;
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();

        //jxls 2.4.6版本
        /*//设置静默模式，不报警告（静默模式是在导出excel模板时候，如果模板中标签名没有在传入的map中找到数值，会打印报告某某某标签没有赋值。如果开启静默模式后则不会报告。放心，出现严重异常还是会报错的。）
        //evaluator.getJexlEngine().setSilent(true);
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("utils", new JxlsExcelUtils());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);*/

        //jxls 2.10.0版本
        JexlBuilder jb = new JexlBuilder();
        //设置静默模式，不报警告（静默模式是在导出excel模板时候，如果模板中标签名没有在传入的map中找到数值，会打印报告某某某标签没有赋值。如果开启静默模式后则不会报告。放心，出现严重异常还是会报错的。）
        //jb.silent(true);
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<>();
        funcs.put("utils", new JxlsExcelUtil());//添加自定义功能
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        //必须要这个，否者表格函数统计会错乱（函数统计错乱，这个必须设置（setUseFastFormulaProcessor(false)），要不后续文章中会写到怎么做excel分sheet输出，如果不设置成false，以后用excel自带函数统计相加会加错地方。）
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);

    }

    /**
     * 以文件形式导出Excel
     * @param xls  模板文件
     * @param out  导出文件
     * @param model  导出参数
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    /**
     * 以流的形式导出Excel
     * @param templatePath 模板文件路径
     * @param os  导出流
     * @param model 导出参数
     * @throws Exception
     */
    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        File template = getTemplate(templatePath);
        if(template != null){
            exportExcel(new FileInputStream(template), os, model);
        } else {
            throw new Exception("Excel 模板未找到！");
        }
    }

    //获取jxls模版文件
    public static File getTemplate(String path){
        File template = new File(path);
        if(template.exists()){
            return template;
        }
        return null;
    }

    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    /**
     * 以流的形式导入Excel
     * @param inputXML
     * @param inputXLS
     * @param model
     * @throws IOException
     */
    public static XLSReadStatus importExcel(InputStream inputXML , InputStream inputXLS, Map model) throws IOException {
        XLSReader mainReader = null;
        try {
            mainReader = ReaderBuilder.buildFromXML( inputXML  );
        } catch (IOException e1) {
           // log.error("读取配置文件失败"+e1);
           // throw new ApplicationException(ErrorCode.READ_CONFIG_FAIL.code(), ErrorCode.READ_CONFIG_FAIL.message());
        } catch (SAXException e1) {
           // log.error("读取配置文件失败"+e1);
           // throw new ApplicationException(ErrorCode.READ_CONFIG_FAIL.code(), ErrorCode.READ_CONFIG_FAIL.message());
        }

        try {
            XLSReadStatus readStatus = mainReader.read(inputXLS, model);
            return readStatus;
        } catch (InvalidFormatException e1) {
        	//log.error("将数据映射到bean时出错"+e1);
            //throw new ApplicationException(ErrorCode.MAPPED_FAIL.code(), ErrorCode.MAPPED_FAIL.message());
        } catch (IOException e1) {
            //log.error("将数据映射到bean时出错"+e1);
           // throw new ApplicationException(ErrorCode.MAPPED_FAIL.code(), ErrorCode.MAPPED_FAIL.message());
        }
        return null;
    }

    /**
     * 以文件形式导入Excel
     * @param xls
     * @param in
     * @param model
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static XLSReadStatus importExcel(File xls, File in, Map model) throws FileNotFoundException, IOException {
        return importExcel(new FileInputStream(xls), new FileInputStream(in), model);
    }

    /**
     * 以路径形式传入参数然后进行导入操作
     * @param rulePath
     * @param templeatePath
     * @param model
     * @throws Exception
     */
    public static XLSReadStatus importExcel(String rulePath, String templeatePath, Map<String, Object> model) throws Exception {
        File ruleFile = getTemplate(rulePath);
        File templeateFile = getTemplate(templeatePath);
        if(ruleFile == null){
            throw new Exception("Excel 模板未找到。");
        } if(templeateFile==null){
            throw new Exception("Excel 模板未找到。");
        }else {
            return importExcel(new FileInputStream(ruleFile), new FileInputStream(templeateFile), model);
        }
    }
}
