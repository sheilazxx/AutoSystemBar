一句代码适配4.4以上沉浸状态栏和沉浸导航栏, 状态栏颜色和样式自动根据状态栏下面的背景颜色自动调整, 适配魅族，小米等国产手机.

#### Sample
![Git](gif.gif)
![Image](Screenshot_01.png)
![Image](Screenshot_02.png)
![Image](Screenshot_03.png)

#### Usage

```java
    SystemBarHelper.Builder().into(activity)
```

```java
    SystemBarHelper.Builder()
                    .statusBarColor()    // 设置状态栏颜色
                    .statusBarFontStyle()  // 设置状态栏时间,电量的风格, 6.0以上, 部分国产机可以不用6.0以上.
                    .navigationBarColor()  // 设置导航栏颜色
                    .enableImmersedStatusBar()  // 布局嵌入状态栏，例如图片嵌入状态栏
                    .enableImmersedNavigationBar()  // 布局嵌入导航栏，例如图片嵌入导航栏
                    .enableAutoSystemBar(false)  // 根据状态栏下面的背景颜色自动调整状态栏的颜色, 自动调整状态栏时间,电量的风格, 默认是开启的
                    .into(this)
```

```java
   SystemBarHelper helper = SystemBarHelper.Builder().into(activity);    // SystemBarHelper也Builder有相应的方法,方便动态设置
   helper.setNavigationBarColor()
   helper.setStatusBarColor()
   helper.statusBarFontStyle()
   helper.enableImmersedStatusBar()
   helper.enableImmersedNavigationBar()
```

#### Feature
1. 根据状态栏下面的背景颜色自动调整状态栏的颜色
2. 根据状态栏下面的背景颜色自动调整状态栏时间,电量等风格
3. 设置图片嵌入状态栏,图片嵌入导航栏
4. 修个状态栏的颜色和导航栏颜色