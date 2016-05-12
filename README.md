# AndroidTech
This is a performance optimization project source code, including optimization of UI/memory and other aspects.
这是一个教学DEMO，主要是性能优化方面的一些案例，架构主要分成三个方面。  
一.UI层(界面)  
1.全局只有两个Activity，启动页面AppStartActivity和主页面HomepageActivity，其它都是Fragment，所有界面间的切换都是Fragment的切换。  
2.所有的界面都在：com.ycl.androidtech.fragment包下，下一级目录：  
(1)base：Fragment基类。  
(2)homepage:首页。  
(3)ui: 界面设计的一些DEMO。  
(4)memery:内存优化的一些DEMO。
二.工具
1.监控工具
(1)UI卡顿监控 com.ycl.androidtech.monitor.ui。
(2)内存监控：com.ycl.androidtech.monitor.memory。
(3)耗时上报工具：com.ycl.androidtech.monitor.time
三.引擎模块
1.图片引擎：MiniImageLoader.
2.数据库引擎。