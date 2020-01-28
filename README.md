MaxBannerImageContainer
=====

MaxBannerImageContainer is a custom viewgroup that is animated the commonly hero display view used in Max + App. The effect is as follows:

(MaxBannerImageContainer是仿照Max+App中常用英雄展示控件制作的一个控件。效果如下:)

![](/show.gif)

How to use:

(使用方式：)

1. Add it in your root build.gradle at the end of repositories:

    ``` xml
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
    	
2. Add the dependency

    ```implementation 'com.github.drogbababa:MaxBannerImageContainer:v1.1'```

3. In Activity.xml

    ``` xml
    <com.drogbababa.maxbannerimagecontainer.MaxBannerImageContainer
            android:id="@+id/max_banner_view"
            android:layout_width="300dp"
            android:layout_height="200dp"
            android:layout_marginLeft="20dp"
            app:radius="@dimen/radius11">
     </com.drogbababa.maxbannerimagecontainer.MaxBannerImageContainer>
     ```

4. In Java file
    - Extends MaxBannerBaseAdapter. [Adapter example](sample/src/main/java/com/drogbababa/maxbannerimagecontainer/ExampleImageAdapter.java)
    - set Adapter to maxbannerimagecontainer. [Activity example](sample/src/main/java/com/drogbababa/maxbannerimagecontainer/MainActivity.java)
    
Any bugs in use are welcome to issue~