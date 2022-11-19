package demo;

import lombok.Data;


import java.util.*;
import java.util.stream.Collectors;

@Data
public class Region {
    private Integer regionId;

    /**
     * parentId = 0 为 root
     */
    private Integer parentId;

    private String regionName;

    private Integer score;

    private List<Region> children;

    private List<Integer> parentIds;

    public Region(Integer regionId, String regionName, Integer parentId, Integer score){
        this.regionId = regionId;
        this.parentId = parentId;
        this.regionName = regionName;
        this.score = score;

    }


    public static List<Region> getRegionTree(List<Region> regions) {
        List<Region> regionTree = regions.stream()
                // 过滤所有 root
                .filter(region -> region.getParentId() == 0)
                .map(region -> {
                    region.setChildren(getChildren(region, regions));
                    return region;
                }).collect(Collectors.toList());
        buildScoreCount(regionTree);
        return regionTree;
    }

    private static List<Region> getChildren(Region root, List<Region> all) {
        return all.stream().filter(region -> Objects.equals(region.getParentId(),root.getRegionId())).map(region -> {
            region.setChildren(getChildren(region, all));
            return region;
        }).collect(Collectors.toList());
    }

    /**
     * 计算 score
     * @param regionTree
     */
    private static void buildScoreCount(List<Region> regionTree) {
        for (Region root: regionTree) {
            if (root.getChildren() != null){
                calculateScoreCount(root,root.getChildren());
            }
        }
    }

    /**
     *
     * @param root 该行政区作为 root
     * @param children 该行政区所有的直属的下级行政区
     * @return 返回 root 下所有的 score 之和
     */
    private static int calculateScoreCount(Region root, List<Region> children) {
        for (Region child: children) {
            if (child.getChildren() == null){ // 叶子节点
                return child.getScore();
            } else {
                calculateScoreCount(child,child.getChildren());
                root.setScore(root.getScore() + child.getScore());
            }
        }
        return root.getScore();
    }

    /**
     *
     * @param regionId
     * @param regionTree
     * @return 返回以 regionId 为 root 的区划树
     */
    public static List<Region> getSubRegionTreeById(Integer regionId, List<Region> regionTree){
        List<Region> regionList = new ArrayList<>();
        Queue queue = new ArrayDeque<List<Region>>();
        for (Region region: regionTree) {
            if (region.getRegionId().equals(regionId)){
                regionList.add(region);
                return regionList;
            }

            if(region.getChildren() != null){
                queue.offer(region.getChildren());
            }

            while (!queue.isEmpty()){
                List<Region> children = (List<Region>) queue.poll();
                for (Region child: children) {
                    if (region.getRegionId().equals(regionId)){
                        if (child.getChildren() != null){
                            queue.offer(child.getChildren());
                        }
                    } else { // 找到了该节点
                        regionList.add(child);
                        return regionList;
                    }
                }
            }
        }
        return regionList;
    }

    /**
     *
     * @param regionId
     * @param regionTree
     * @return 返回 regionId 所有上级区域
     */
    public static List<Integer> getParentsById(Integer regionId, List<Region> regionTree){
        Queue queue = new LinkedList<List<Region>>();
        // regionId -> parentsOf(regionId)
        Map map = new HashMap<Integer,List<Integer>>();
        for (Region region : regionTree) {
            if(region.getRegionId().equals(regionId)){
                // root 没有父节点
                return new ArrayList<>();
            } else{
                map.put(region.getRegionId(),new ArrayList<>());
            }

            // 设置下一层的父节点列表
            if(region.getChildren() != null && region.getChildren().size() > 0){
                setNextLevelParent(queue, map, region);
            }

            while (!queue.isEmpty()){
                List<Region> children = (List<Region>) queue.poll();
                for (Region child: children) {
                    if (child.getRegionId().equals(regionId)){  // 找到了该节点
                        List<Integer> parents = (List<Integer>) map.get(child.getRegionId());
                        return parents;
                    }else{
                        if (child.getChildren() != null){
                            setNextLevelParent(queue,map,child);
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private static void setNextLevelParent(Queue queue, Map map, Region currentRegion) {
        // 得到当前节点的所有父节点
        List<Integer> parentList = (List<Integer>) map.get(currentRegion.getRegionId());
        Integer regionId = currentRegion.getRegionId();
        currentRegion.getChildren().forEach(r -> {
            List<Integer> nextLevelParents = new ArrayList<>();
            nextLevelParents.add(regionId);
            nextLevelParents.addAll(parentList);
            map.put(r.getRegionId(),nextLevelParents);
        });
        queue.offer(currentRegion.getChildren());
    }

    public static String getFullRegionNameById(String separator, Integer regionId, List<Region> regionTree){
        Queue queue = new LinkedList<List<Region>>();
        Map map = new HashMap<Integer,Region>();
        for(Region region: regionTree){
            // root
            if (region.getRegionId().equals(regionId)){
                return region.getRegionName();
            }
            map.put(region.getRegionId(),region);

            if (region.getChildren() != null){
                queue.offer(region.getChildren());
            }

            while (!queue.isEmpty()){
                List<Region> children = (List<Region>) queue.poll();
                for(Region child: children){
                    Region parentRegion = (Region) map.get(child.getParentId());
                    if (child.getRegionId().equals(regionId)){ // find
                        return parentRegion.getRegionName() + separator + child.getRegionName();
                    } else {
                        // set full region name
                        child.setRegionName(parentRegion.getRegionName() + separator + child.getRegionName());
                        map.put(child.getRegionId(),child);
                        if (child.getChildren() != null){
                            queue.offer(child.getChildren());
                        }
                    }
                }
            }
        }
        return null;
    }


}
