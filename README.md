# PureReader

[![Apache License 2.0](https://camo.githubusercontent.com/5dcb57e59f46a4ed65fafc343ab810e35086e21d/68747470733a2f2f696d672e736869656c64732e696f2f3a6c6963656e73652d6170616368652d626c75652e737667)](https://www.apache.org/licenses/LICENSE-2.0.html) ![Build Status](https://camo.githubusercontent.com/d1607e97ba02ce1c92fc87e916e346b2c1ae59e0/68747470733a2f2f7472617669732d63692e6f72672f546f6e6e794c2f5061706572506c616e652e7376673f6272616e63683d6d6173746572)
> 应用下载地址：[酷安](https://www.coolapk.com/)  https://github.com/lecymeng/PureReader/releases

## Introduce
PureReader：使用Gank.io Api开发的符合Google Material Design的Android客户端。项目采取的是RxJava + Retrofit + MVP架构开发, 图片加载模块使用了Glide + Fresco，基本所有的UI都是基于原生实现，或是在原生上进行修改，现主要包括：干货区、妹子专栏，收藏三个子模块~ <https://github.com/lecymeng/PureReader>

## Screenshots

|   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-32-34.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-32-47.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-34-00.png)    |
| :--: | :--: | :--: |
|   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-34-07.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-33-39.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-33-48.png)   |
|   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-34-11.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-34-14.png)   |   ![](http://blog-1251678165.coscd.myqcloud.com/2018-04-16-enframe_2018-04-16-16-34-29.png)   |


## Features 特性
- 1、基本遵循Google Material Design设计风格。
- 2、数据来自干货集中营内容。
- 3、MVP的项目应用。
- 4、`BottomNavigationView`搭配`TabLayout`和`ViewPager`的具体使用。
- 5、`RxJava` && `Retrofit` 完成网络请求。
- 6、`ToolBar`的全方面使用。
- 7、`Glide`加载图片，处理图片缓存，Fresco加载大图
- 8、`SwipeRefreshLayout`结合`RecyclerView`下拉刷新上拉加载。

### Version
#### V1.01（04-16）
> TODO：收藏和搜索文章，夜间模式完全适配，缓存处理

- 1、每日干货
- 2、各个类别文章
- 3、加载干货详情
- 4、妹子图库，大图加载
- 5、夜间模式适配
- 6、尽可能规范MVP框架，后期会慢慢调整

## Libraries

- [Glide](https://github.com/bumptech/glide)
- [Okhttp](http://square.github.io/okhttp/)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [Retrofit](https://square.github.io/retrofit/)
- [Butterknife](http://jakewharton.github.io/butterknife/)
- [fresco](https://github.com/facebook/fresco)

## Thanks

- 感谢代码家~[gank.io](http://gank.io/) 提供的数据
- 参考项目：[Todo MVP architecture](https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava/),  [Zhihu Daily](https://daily.zhihu.com/),；designed with [Material Design](https://material.io/) style.

## Issues 宝贵意见
如果有任何问题，请到github的[issue处](https://github.com/lecymeng/PureReader/issues)写上你不明白的地方，也可以通过下面提供的方式联系我，我会及时给予帮助。

## End
> 注意：此开源项目仅做学习交流使用，如用到实际项目还需多考虑其他因素如并发等，请多多斟酌。如果你觉得不错，对你有帮助，欢迎点个star，follow，也可以帮忙分享给你更多的朋友，这是给我最大的动力与支持。

## About me
- **Blog：** [http://weicools.com/](http://weicools.com/)
- **Email：** lecymeng@outlook.com

## License

```
Copyright (C) 2018 Zhengwei Zhang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```