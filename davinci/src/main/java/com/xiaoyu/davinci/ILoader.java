package com.xiaoyu.davinci;

public interface ILoader {

    void load(LoadOptions loadOptions);

    void clearDiskCache();

    void clearMemoryCache();

}
