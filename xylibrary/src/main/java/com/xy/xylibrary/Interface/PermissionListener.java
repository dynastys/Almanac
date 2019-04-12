package com.xy.xylibrary.Interface;

import java.util.List;

/**
 * zhouwei
 * 权限申请接口
 */

public interface PermissionListener {
    void granted();
    void denied(List<String> deniedList);
}