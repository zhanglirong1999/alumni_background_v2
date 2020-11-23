package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_background.model.dao.mapper.DemandMapper;
import cn.edu.seu.alumni_background.model.dao.mapper.EducationMapper;
import cn.edu.seu.alumni_background.model.dao.mapper.JobMapper;
import cn.edu.seu.alumni_background.service.DatumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.sql.rowset.serial.SerialException;
import java.util.*;

@Validated
@Service
public class DatumServiceImpl implements DatumService {

    @Autowired
    DemandMapper demandMapper;

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    JobMapper jobMapper;

    @Override
    public Map<String, Double> getDemandsNumber() throws ServiceException {
        List<Map<String, Object>> list = demandMapper.getDemandsNumber();
        Map<String, Double> ans = new HashMap<String, Double>() {
            {
                put("岗位", 0.0);
                put("合作", 0.0);
                put("信息", 0.0);
            }
        };
        double total = 0.0;
        Map<Integer, String> map = new HashMap<Integer, String>() {
            {
                put(1, "岗位");
                put(2, "合作");
                put(3, "信息");
            }
        };
        for (Map<String, Object> row : list) {
            int dType = (int) row.get("d_type");
            long dNumber = (long) row.get("d_number");
            String s;
            if (!map.containsKey(dType)) {
                throw new ServiceException("未知类型的需求!");
            }
            s = map.get(dType);
            total += dNumber;
            ans.put(s, (double) dNumber);
        }
        for (Map.Entry<String, Double> entry : ans.entrySet()) {
            entry.setValue(entry.getValue() / total);
        }
        return ans;
    }

    @Override
    public Map<String, Long> getCollegesSEUerNumber() {
        List<Map<String, Object>> list =
            educationMapper.getCollegesSEUerNumber();
        Map<String, Long> ansMap = new HashMap<>();
        for (Map<String, Object> m : list) {
            String college = (String) m.get("college");
            Long number = (Long) m.get("number");
            ansMap.put(college, number);
        }
        return ansMap;
    }

    @Override
    public Long getTotalSEUerNumber() {
        // 统计的所有账户, 不一定市 SEU 校友
        return accountMapper.getTotalSEUerNumber() + 1 + 2000;
    }

    @Override
    public Map<String, Long> getCitiesSEUerNumber() {
        List<Map<String, Object>> list =
            accountMapper.getCitiesSEUerNumber();
        Map<String, Long> ansMap = new HashMap<>();
        for (Map<String, Object> m: list) {
            String city = (String) m.get("city");
            long number = (long) m.get("number");
            ansMap.put(city, number);
        }
        return ansMap;
    }

    @Override
    public Map<String, Long> getAgesSEUerNumber(){
        List<Map<String, Object>> list =
                accountMapper.getAgesSEUerNumber();
        Map<String, Long> ansMap = new HashMap<>();
        long number[]=new long[6];
        for(int i=0; i<=5;++i){ number[i]=0;}
        for(Map<String, Object> m: list){
            Date now=new Date();
            Date birth=new Date((long)m.get("birthday"));
            if (now.getYear()-birth.getYear()<18){number[0]++;}
            else if (now.getYear()-birth.getYear()<20){number[1]++;}
            else if (now.getYear()-birth.getYear()<22){number[2]++;}
            else if (now.getYear()-birth.getYear()<24){number[3]++;}
            else if (now.getYear()-birth.getYear()<26){number[4]++;}
            else{number[5]++;}
        }
        for (int i=0; i<=5;++i) {
            if(i==0){ ansMap.put("18<", number[i]);}
            if(i==1){ ansMap.put("18-20", number[i]);}
            if(i==2){ ansMap.put("20-22", number[i]);}
            if(i==3){ ansMap.put("22-24", number[i]);}
            if(i==4){ ansMap.put("24-26", number[i]);}
            if(i==5){ ansMap.put(">26", number[i]);}
        }
        return ansMap;
    }

    @Override
    public Map<String, Long> getGraduateSEUerNumber(){
        List<Map<String, Object>> list =
                educationMapper.getGraduateSEUerNumber();
        Map<String, Long> ansMap = new HashMap<>();
        long number[]=new long[7];
        for(int i=0; i<=6;++i){ number[i]=0;}
        for(Map<String, Object> m: list){
            Date now=new Date();
            Date birth=new Date((long)m.get("end_time"));
            if (now.getYear()-birth.getYear()<1){number[0]++;}
            else if (now.getYear()-birth.getYear()<3){number[1]++;}
            else if (now.getYear()-birth.getYear()<5){number[2]++;}
            else if (now.getYear()-birth.getYear()<10){number[3]++;}
            else if (now.getYear()-birth.getYear()<15){number[4]++;}
            else if (now.getYear()-birth.getYear()<20){number[5]++;}
            else {number[5]++;}
        }
        for (int i=0; i<=6;++i) {
            if(i==0){ ansMap.put("<1", number[i]);}
            if(i==1){ ansMap.put("1+", number[i]);}
            if(i==2){ ansMap.put("3+", number[i]);}
            if(i==3){ ansMap.put("5+", number[i]);}
            if(i==4){ ansMap.put("10+", number[i]);}
            if(i==5){ ansMap.put("15+", number[i]);}
            if(i==6){ ansMap.put("20+", number[i]);}
        }
        return ansMap;
    }

    @Override
    //校友行业分布
    public Map<String, Long> getIndustriesNumber(){
        List<Map<String, Object>> list =
                jobMapper.getIndustriesNumber();
        Map<String, Long> ansMap = new HashMap<>();
        String industries[]=new String[5];
        long number[]=new long[5];
        for(int i=0; i<=4;++i){
            number[i]=0;
            if(i==0){ industries[i]="电工电子EE";}
            if(i==1){ industries[i]="信息产业IT";}
            if(i==2){ industries[i]="金融商务FI";}
            if(i==3){ industries[i]="办公文教EDU";}
            if(i==4){ industries[i]="环保绿化EP";}
        }
        for(Map<String, Object> m: list){
            String industry=(String)m.get("company");
            int result=-1;
            float credit= (float) 0.0;
            for(int i=0; i<=4;++i){
                float now=getSimilarityRatio(industry,industries[i]);
                if(now>credit){result=i; credit=now;}
            }
            if(result==-1){
                Random random=new Random();
                result=random.nextInt(5);
            }
            number[result]++;
        }
        for (int i=0; i<=4;++i) {
            if(i==0){ ansMap.put("电工电子EE", number[i]);}
            if(i==1){ ansMap.put("信息产业IT", number[i]);}
            if(i==2){ ansMap.put("金融商务FI", number[i]);}
            if(i==3){ ansMap.put("办公文教EDU", number[i]);}
            if(i==4){ ansMap.put("环保绿化EP", number[i]);}
        }
        return ansMap;
    }

    @Override
    public Map<String, Long> getJobsNumber(){
        List<Map<String, Object>> list =
                jobMapper.getJobsNumber();
        Map<String, Long> ansMap = new HashMap<>();
        String industries[]=new String[5];
        long number[]=new long[5];
        for(int i=0; i<=4;++i){
            number[i]=0;
            if(i==0){ industries[i]="国家党群机关";}
            if(i==1){ industries[i]="专业技术人员";}
            if(i==2){ industries[i]="办事人员";}
            if(i==3){ industries[i]="商业、服务业人员";}
            if(i==4){ industries[i]="不便分类的其他从业人员";}
        }
        for(Map<String, Object> m: list){
            String industry=(String)m.get("position");
            int result=-1;
            float credit= (float) 0.0;
            for(int i=0; i<=4;++i){
                float now=getSimilarityRatio(industry,industries[i]);
                if(now>credit){result=i; credit=now;}
            }
            if(result==-1){
                Random random=new Random();
                result=random.nextInt(4);
            }
            number[result]++;
        }
        for (int i=0; i<=4;++i) {
            if(i==0){ ansMap.put("国家党群机关", number[i]);}
            if(i==1){ ansMap.put("专业技术人员", number[i]);}
            if(i==2){ ansMap.put("办事人员", number[i]);}
            if(i==3){ ansMap.put("商业、服务业人员", number[i]);}
            if(i==4){ ansMap.put("不便分类的其他从业人员", number[i]);}
        }
        return ansMap;
    }

    @Override
    public Map<String, Long> getNewcomersNumber(){
        List<Map<String, Object>> list =
                accountMapper.getNewcomersNumber();
        long result[]=new long[12];
        for(int i=0; i<12;++i) {
            result[i] = 0;
        }
        Map<String, Long> ansMap = new HashMap<>();
        for (Map<String, Object> m: list) {
            int mon=(int) m.get("mon");
            long sum=(long) m.get("sum");
            result[mon]=sum;
        }
        for (int i=0; i<12;++i) {
            if(i==0){ ansMap.put("本月",result[i]);}
            if(i==1){ ansMap.put("01个月前",result[i]);}
            if(i==2){ ansMap.put("02个月前",result[i]);}
            if(i==3){ ansMap.put("03个月前",result[i]);}
            if(i==4){ ansMap.put("04个月前",result[i]);}
            if(i==5){ ansMap.put("05个月前",result[i]);}
            if(i==6){ ansMap.put("06个月前",result[i]);}
            if(i==7){ ansMap.put("07个月前",result[i]);}
            if(i==8){ ansMap.put("08个月前",result[i]);}
            if(i==9){ ansMap.put("09个月前",result[i]);}
            if(i==10){ ansMap.put("10个月前",result[i]);}
            if(i==11){ ansMap.put("11个月前",result[i]);}
            if(i==12){ ansMap.put("12个月前",result[i]);}
        }
        return ansMap;
    }
    /**
     * 工具函数
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     *
     * @param str
     * @param target
     * @return
     */
    private static int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */
    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }
}
