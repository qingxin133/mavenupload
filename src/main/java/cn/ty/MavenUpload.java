package cn.ty;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * @ProjectName: mavenupload
 * @Package: cn.ty
 * @ClassName: MavenUpload
 * @Author: tianyang
 * @Description: maven包上传脚本生成
 * @Date: 2021/6/11 11:51
 * @Version: 1.0
 */
public class MavenUpload {

    public static void main(String[] args) {
        new MavenUpload().makeMavenUploadStr();
    }

    /**
     * 生成上传maven私库语句
     *
     * @param
     * @Description
     * @Author tianyang
     * @project ${PROJECT_NAME}
     * @version 1.0
     * @Date 2021/6/10 19:09
     * @Return void
     */
    public void makeMavenUploadStr() {
        File dir = new File("C:\\Users\\qingx\\Desktop\\新建文件夹\\");
//        Pattern pattern = Pattern.compile("^*<groupId>(*)</groupId>");
//        String patternStr = "[\\W\\w]*<parent><groupId>(.*)</groupId>.*</parent>.*";
        // 创建 Pattern 对象
//        Pattern p = Pattern.compile(patternStr);
        String parentArtifactId="";
        String parentGroupId="";
        String parentVersion="";
        String artifactId="";
        String groupId="";
        String version="";
        StringBuffer sbf=new StringBuffer();
        Map nameMap = new HashMap();
        List<String> pomList = new ArrayList<>();
        List<String> jarList = new ArrayList<>();
        for (File f : dir.listFiles()) {
            nameMap.put(f.getName(),"1");
        }
//        System.out.println(nameMap.toString());
        for (File f : dir.listFiles(new PomFileFilter())) {
//        File f = new File("D:\\workspace\\work\\ecs\\hotfix-futian.0605-ECS.V11.0.20210325.01release\\jar\\add\\shardingsphere\\encrypt-core-common-4.1.1-YN-RC1.pom");
            String fileName = f.getName().replaceAll("[.][^.]+$", "");
//            String fileContext = readToString(f).replaceAll("\\r\\n", "").replaceAll("\\s+", "");
            //1.创建DocumentBuilderFactory对象
             parentArtifactId="";
             parentGroupId="";
             parentVersion="";
             artifactId="";
             groupId="";
             version="";
//            System.out.println(fileName);
            try {
                org.json.JSONObject rootJson = new org.json.JSONObject();
//                JSONObject jsonObj = XML.toJSONObject(fileContext);
                SAXBuilder builder = new SAXBuilder();//创建一个新的SAXBuilder-JDOM解析器来解析XML文件
                //解析xml文件:使用SAXBuilder的实例化对象builder的build方法,将路径中的XML文件解析为Document对象
                Document parse = builder.build(f);
                //获取xml文件根节点    <excel id="student" code="student" name="学生信息导入">
                Element root = parse.getRootElement();
                //获取模板名称,也就是获取根节点的name属性   root.getAttribute("name").getValue()
                rootJson.put(root.getName(), iterateElement(root));
                org.json.JSONObject projectJson = rootJson.getJSONObject("project");
                if(projectJson.has("parent")){
                    JSONObject parentJson = projectJson.getJSONArray("parent").getJSONObject(0);
                    if(parentJson.has("groupId"))
                        parentGroupId =  parentJson.getJSONArray("groupId").getString(0);

                    if(parentJson.has("version"))
                        parentVersion = parentJson.getJSONArray("version").getString(0);
                }
                if(projectJson.has("groupId")){
                    groupId = projectJson.getJSONArray("groupId").getString(0);
                }
                if(projectJson.has("artifactId")){
                    artifactId = projectJson.getJSONArray("artifactId").getString(0);
                }
                if(projectJson.has("version")){
                    version = projectJson.getJSONArray("version").getString(0);
                }
                if(StringUtils.isBlank(groupId)){
                    groupId = parentGroupId;
                }
                if(StringUtils.isBlank(version)){
                    version = parentVersion;
                }

//        System.out.println("parentGroupId:"+parentGroupId);
//        System.out.println("parentArtifactId:"+parentArtifactId);
//        System.out.println("parentVersion:"+parentVersion);
//        System.out.println("groupId:"+groupId);
//        System.out.println("artifactId:"+artifactId);
//        System.out.println("version:"+version);
                //jar
                if(StringUtils.isBlank(groupId) || StringUtils.isBlank(artifactId) || StringUtils.isBlank(version)){
                    throw new RuntimeException("---数据不能为空---");
                }
                if(null!= nameMap.get(fileName+".jar")){
                    getJarStr(sbf,fileName,groupId,artifactId,version);
                    jarList.add(sbf.toString());
                }else{
                    //pom
                    getPomStr(sbf,fileName,groupId,artifactId,version);
                    pomList.add(sbf.toString());
                }
                sbf.delete(0,sbf.length());
//                break;
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println(fileName);
            }

        }

        for(String jar:jarList){
            System.out.println(jar);
        }
        for(String pom:pomList){
            System.out.println(pom);
        }
    }
    public void getJarStr(StringBuffer sbf,String fileName,String groupId,String artifactId,String version){
        sbf.append("mvn deploy:deploy-file -Dmaven.test.skip=true ");
        sbf.append(" -Dfile=.\\").append(fileName).append(".jar");
        sbf.append(" -DgroupId=").append(groupId);
        sbf.append(" -DartifactId=").append(artifactId);
        sbf.append(" -Dversion=").append(version);
        sbf.append(" -Dpackaging=jar -DrepositoryId=nexus -Durl=http://repo.ca-sinfusi.com:8081/repository/thirdparty/ ");
        sbf.append(" -DpomFile=.\\").append(fileName).append(".pom");
    }
    public void getPomStr(StringBuffer sbf,String fileName,String groupId,String artifactId,String version){
        sbf.append("mvn deploy:deploy-file -Dmaven.test.skip=true ");
        sbf.append(" -Dfile=.\\").append(fileName).append(".pom");
        sbf.append(" -DgroupId=").append(groupId);
        sbf.append(" -DartifactId=").append(artifactId);
        sbf.append(" -Dversion=").append(version);
        sbf.append(" -Dpackaging=pom -DrepositoryId=nexus -Durl=http://repo.ca-sinfusi.com:8081/repository/thirdparty/");
    }

    /**

     * 一个迭代方法

     *

     * @param element

     * : org.jdom.Element

     * @return java.util.Map 实例

     */

    @SuppressWarnings("unchecked")

    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();

        Element et = null;

        Map obj = new HashMap();

        List list = null;

        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();

            et = (Element) jiedian.get(i);

            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)

                    continue;

                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());

                }

                list.add(iterateElement(et));

                obj.put(et.getName(), list);

            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());

                }

                list.add(et.getTextTrim());

                obj.put(et.getName(), list);

            }

        }

        return obj;

    }

    class PomFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else {
                String name = file.getName();
                if (name.endsWith(".pom")) {
                    return true;
                } else {
                    return false;
                }
            }

        }
    }

}
