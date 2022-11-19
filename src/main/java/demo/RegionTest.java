package demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class RegionTest {
    public static void main(String[] args) throws JsonProcessingException {
        List<Region> regions = Arrays.asList(
                new Region(1,"东汉行政区划",0,1),
                new Region(2,"司隶",1,2),
                new Region(3,"京兆尹",2,3),
                new Region(4,"河东郡",2,4),
                new Region(5,"河内郡",2,5),

                new Region(6,"豫州",1,6),
                new Region(7,"颍川郡",6,7),
                new Region(8,"沛国",6,8),
                new Region(9,"阳翟县",7,9),
                new Region(10,"许县",7,10),

                new Region(11,"冀州",1,11),
                new Region(12,"渤海郡",11,12),
                new Region(13,"中山国",11,12),

                new Region(14,"兖州",1,12),
                new Region(15,"陈留郡",14,12),
                new Region(16,"泰山郡",14,12),

                new Region(17,"徐州",1,12),
                new Region(18,"琅琊国",17,12),
                new Region(19,"广陵郡",17,12),

                new Region(20,"青州",1,12),
                new Region(21,"济南国",20,12),
                new Region(22,"平原郡",20,12),

                new Region(23,"荆州",1,12),
                new Region(24,"南郡",23,12),
                new Region(25,"长沙郡",23,12),

                new Region(26,"扬州",1,12),
                new Region(27,"九江郡",26,12),
                new Region(28,"豫章郡",26,12),

                new Region(29,"益州",1,12),
                new Region(30,"汉中郡",29,12),
                new Region(31,"蜀郡",29,12),

                new Region(32,"凉州",1,12),
                new Region(33,"陇西郡",32,12),
                new Region(34,"武都郡",32,12),

                new Region(35,"并州",1,12),
                new Region(36,"上党郡",35,12),
                new Region(37,"太原郡",35,12),

                new Region(38,"幽州",1,12),
                new Region(39,"上谷郡",38,12),
                new Region(40,"渔阳郡",38,12),


                new Region(41,"交州",1,12),
                new Region(42,"南海郡",41,12),
                new Region(43,"交趾郡",41,12)

        );

        List<Region> regionTree = Region.getRegionTree(regions);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(regionTree));
        System.out.println(objectMapper.writeValueAsString(Region.getSubRegionTreeById(2,regionTree)));
        System.out.println(Region.getParentsById(10,regionTree));
        System.out.println(Region.getFullRegionNameById("/",10,regionTree));

    }


}
