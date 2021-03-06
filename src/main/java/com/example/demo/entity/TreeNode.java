package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "树形节点")
public class TreeNode {

    @ApiModelProperty(value = "当前节点id")
    protected int id;

    @ApiModelProperty(value = "父节点id")
    protected int parentId;

    @ApiModelProperty(value = "子节点列表")
    protected List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node) {
        children.add(node);
    }

}
