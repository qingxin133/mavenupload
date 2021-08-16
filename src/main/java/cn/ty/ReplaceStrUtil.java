package cn.ty;

/**
 * @ProjectName: mavenupload
 * @Package: cn.ty
 * @ClassName: ReplaceStrUtil
 * @Author: tianyang
 * @Description: 字符串替换
 * @Date: 2021/6/19 12:29
 * @Version: 1.0
 */
public class ReplaceStrUtil {


    public static void main(String[] args) {
       new ReplaceStrUtil().doTestReplace();
    }

    public  void doTestReplace(){
        String shardingTable="T_BO_AREA_SHM_DEF_001_2019";
        String createStatement="CREATE TABLE `t_bo_area_shm_def_001`";
        String next = shardingTable.substring(0, shardingTable.lastIndexOf("_"));
        createStatement=createStatement.replace(next.toLowerCase(), shardingTable.toLowerCase());
        System.out.println(next.toLowerCase());
        System.out.println(shardingTable.toLowerCase());
        System.out.println(createStatement);
    }
}
