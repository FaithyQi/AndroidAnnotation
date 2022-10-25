package com.hero.libcompile;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.hero.libannotation.FragmentDestination;
import com.squareup.javapoet.JavaFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.hero.libAnnotation.FragmentDestination"})
public class MyProcessor extends AbstractProcessor {

    Messager messager;
    Filer filer;
    Elements elements;
    Gson gsonTool;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        gsonTool = new Gson();
        printNote("-->init()");


    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        HashSet<String> set = new HashSet<String>();
        set.add(FragmentDestination.class.getCanonicalName());
        printNote("-->getSupportedAnnotationTypes()");
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        if(set.size()==0||elements==null){
            return true;
        }

        printNote("--> process()");


        HashMap<String, Map<String,Object>> datas = new HashMap<>();

        for (Element element:elements){

            TypeElement typeElement = (TypeElement) element;
            FragmentDestination fd =  element.getAnnotation(FragmentDestination.class);
            String fullName = typeElement.getQualifiedName().toString();

            printNote(String.format("扫描-->%s  -->%s",fullName,fd.pageUrl()));

           datas.put(fd.pageUrl(),createData(typeElement,fd));
        }


        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            //这里只为拿路劲
            JavaFileObject fo =  filer.createClassFile("annTest.txt");
            String rPath = fo.toUri().getPath();
            printNote("获取到的地址-->"+rPath);
            String assetsPath = rPath.substring(0, rPath.indexOf("app")+4)+"src/main/assets/";
            fo.delete();
            printNote("assetsPath = "+assetsPath);

            File file = new File(assetsPath);

            if(!file.exists()){
                file.mkdirs();
            }

            File destFile = new File(file,"destination.json");
            if(destFile.exists()){
                destFile.delete();
            }

            if(destFile.createNewFile()){
                String res = gsonTool.toJson(datas);
                printNote("写入数据-->"+res);

                fos = new FileOutputStream(destFile);
                osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

                osw.write(res);
                osw.flush();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if(osw!=null){
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(fos!=null){

                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }


        return false;
    }

    private HashMap<String,Object> createData(TypeElement element,FragmentDestination fd){
        HashMap<String,Object> pairs = new HashMap<>();
        pairs.put("needLogin",fd.needLogin());
        pairs.put("pageUrl",fd.pageUrl());
        pairs.put("className",element.getQualifiedName().toString());


        return pairs;
    }

    private void printNote(String str){
        messager.printMessage(Diagnostic.Kind.NOTE,str);
    }

}