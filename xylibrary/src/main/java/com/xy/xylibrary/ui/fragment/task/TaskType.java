package com.xy.xylibrary.ui.fragment.task;

import org.litepal.crud.LitePalSupport;

/**
 * 任务状态
 * */
public class TaskType extends LitePalSupport {

    public String taskId;//任务id

    public int tasksize;//任务次数

    public int taskfinishsize;//任务完成次数

    public boolean ISfinish;//任务是否完成

    public int schedule;//任务进度

    public int tasktype;//任务类型

    public boolean IsDouble;//是否翻倍

    public int gold; //金币数

    public boolean ISStartTask;//是否开始任务

    public String time;

    public String link;

    public long CompleteMinTime;
}
