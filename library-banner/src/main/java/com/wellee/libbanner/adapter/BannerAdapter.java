package com.wellee.libbanner.adapter;

import android.view.View;

/**
 * 适配器模式 参考ListView
 */
public abstract class BannerAdapter {

    /**
     * 获取itemView
     *
     * @param position position
     * @return View
     */
    public abstract View getItemView(int position, View convertView);

    /**
     * 获取条目数量
     *
     * @return 数量
     */
    public abstract int getItemCount();

    /**
     * 当前位置的描述
     *
     * @param position position
     * @return 文本
     */
    public CharSequence getItemDesc(int position) {
        return "";
    }
}
