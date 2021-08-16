package cn.ty;

import java.io.*;

public class HebingSql {
    private static final String console_dir = "D:\\workspace\\work\\ecs\\hotfix-futian.0719-ECS.V11.0.20210325.01release\\sql\\console\\add\\";
    private static final String console_target = "D:\\workspace\\work\\ecs\\hotfix-futian.0719-ECS.V11.0.20210325.01release\\sql\\console\\add\\all.sql";

    private static final String fssc_dir = "D:\\workspace\\work\\ecs\\hotfix-futian.0719-ECS.V11.0.20210325.01release\\sql\\fssc\\add\\";
    private static final String fssc_target = "D:\\workspace\\work\\ecs\\hotfix-futian.0719-ECS.V11.0.20210325.01release\\sql\\fssc\\add\\all.sql";

    public static void main(String[] args) {
        new HebingSql().makeAddSql();
    }

    public String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拼接sql语句
     *
     * @param
     * @Description
     * @Author tianyang
     * @project ${PROJECT_NAME}
     * @version 1.0
     * @Date 2021/6/10 19:09
     * @Return void
     */
    public void makeAddSql() {
        File sourceDir = new File(console_dir);
        File targetFile = new File(console_target);
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        String line = null;
        FileWriter fw = null;
        BufferedWriter writer = null;
        System.out.println("--start--");
        try {
            fw = new FileWriter(targetFile);
            writer = new BufferedWriter(fw);

            for (File file : sourceDir.listFiles()) {
                if(file.isDirectory()){
                    continue;
                }
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//构造一个BufferedReader类来读取文件
                while ((line = br.readLine()) != null) {//使用readLine方法，一次读一行
                    writer.write(line);
                    writer.newLine();//换行
                }
                br.close();
                writer.newLine();//换行
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != writer) {
                    writer.close();
                }
                if (null != fw) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("--end--");
    }


}
