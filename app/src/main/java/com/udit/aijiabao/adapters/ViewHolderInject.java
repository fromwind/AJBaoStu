package com.udit.aijiabao.adapters;

public abstract class ViewHolderInject<T> {
    /**
     * 装载数据
     * <功能详细描述>
     *
     * @param data
     * @see [类、类#方法、类#成员]
     */
    public abstract void loadData(T data, int position);
}